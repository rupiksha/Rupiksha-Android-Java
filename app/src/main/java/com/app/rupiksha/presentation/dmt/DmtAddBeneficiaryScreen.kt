package com.app.rupiksha.presentation.dmt

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BankModel
import com.app.rupiksha.presentation.aeps.BankSelectionDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DmtAddBeneficiaryScreen(
    navController: NavController,
    viewModel: DmtViewModel = hiltViewModel()
) {
    var accountNumber by remember { mutableStateOf("") }
    var ifsc by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var selectedBank by remember { mutableStateOf<BankModel?>(null) }
    var showBankDialog by remember { mutableStateOf(false) }

    val dmtBanksState by viewModel.dmtBanksState.collectAsState()
    val addAccountState by viewModel.addAccountState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getDmtBankList()
    }

    LaunchedEffect(addAccountState) {
        if (addAccountState is Resource.Success) {
            Toast.makeText(context, addAccountState?.data?.message ?: "Beneficiary Added", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            viewModel.resetStates()
        } else if (addAccountState is Resource.Error) {
            Toast.makeText(context, addAccountState?.message ?: "Error adding beneficiary", Toast.LENGTH_SHORT).show()
            viewModel.resetStates()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Beneficiary") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text("Account Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = ifsc,
                onValueChange = { ifsc = it },
                label = { Text("IFSC Code") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Beneficiary Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = mobile,
                onValueChange = { if (it.length <= 10) mobile = it },
                label = { Text("Mobile Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = selectedBank?.name ?: "",
                onValueChange = { },
                label = { Text("Select Bank") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showBankDialog = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (validateInputs(context, accountNumber, ifsc, name, mobile, selectedBank)) {
                        viewModel.addDmtAccount(
                            bankId = selectedBank!!.id.toString(),
                            accountNumber = accountNumber,
                            ifsc = ifsc,
                            name = name,
                            mobile = mobile
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                enabled = addAccountState !is Resource.Loading
            ) {
                if (addAccountState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("ADD BENEFICIARY", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showBankDialog) {
        BankSelectionDialog(
            bankListState = dmtBanksState,
            onDismiss = { showBankDialog = false },
            onBankSelected = {
                selectedBank = it
                showBankDialog = false
            }
        )
    }
}

private fun validateInputs(
    context: android.content.Context,
    account: String, ifsc: String, name: String, mobile: String, bank: BankModel?
): Boolean {
    if (account.isEmpty() || ifsc.isEmpty() || name.isEmpty() || mobile.isEmpty() || bank == null) {
        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        return false
    }
    if (mobile.length != 10) {
        Toast.makeText(context, "Invalid mobile number", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}
