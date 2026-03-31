package com.app.rupiksha.presentation.payout

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.app.rupiksha.models.PayoutAccountModel
import com.app.rupiksha.presentation.navigation.Screen
import com.app.rupiksha.presentation.recharge.ReceiptRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayoutScreen(
    navController: NavController,
    title: String,
    viewModel: PayoutViewModel = hiltViewModel()
) {
    val bankListState by viewModel.bankListState.collectAsState()
    val initiateState by viewModel.initiateTransactionState.collectAsState()
    val transactionState by viewModel.transactionState.collectAsState()
    val context = LocalContext.current

    var selectedBank by remember { mutableStateOf<PayoutAccountModel?>(null) }
    var amount by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var senderName by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf("IMPS") }

    var showOtpDialog by remember { mutableStateOf(false) }
    var showPinDialog by remember { mutableStateOf(false) }
    var txnKey by remember { mutableStateOf("") }
    var otpPinValue by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getPayoutBankList()
    }

    LaunchedEffect(initiateState) {
        if (initiateState is Resource.Success) {
            val response = initiateState?.data
            txnKey = response?.txnKey ?: ""
            if (response?.mode?.lowercase() == "otp") {
                showOtpDialog = true
            } else {
                showPinDialog = true
            }
        } else if (initiateState is Resource.Error) {
            Toast.makeText(context, initiateState?.message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(transactionState) {
        if (transactionState is Resource.Success) {
            showOtpDialog = false
            showPinDialog = false
            Toast.makeText(context, transactionState?.data?.message, Toast.LENGTH_LONG).show()
            viewModel.resetStates()
        } else if (transactionState is Resource.Error) {
            Toast.makeText(context, transactionState?.message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.DmtAddBeneficiary.route) }, // Payout uses similar "Add Account"
                containerColor = Color.Red,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Transfer Form
            OutlinedTextField(
                value = mobile,
                onValueChange = { if (it.length <= 10) mobile = it },
                label = { Text("Mobile Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bank Account Selection (Dropdown or simple selector)
            Box(modifier = Modifier.fillMaxWidth()) {
                var expanded by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = selectedBank?.account ?: "Select Bank Account",
                    onValueChange = { },
                    label = { Text("Bank Account") },
                    modifier = Modifier.fillMaxWidth().clickable { expanded = true },
                    enabled = false,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    when (bankListState) {
                        is Resource.Success -> {
                            bankListState?.data?.let { banks ->
                                it.forEach { bank ->
                                    DropdownMenuItem(
                                        text = { Text("${bank.bankName} - ${bank.account}") },
                                        onClick = {
                                            selectedBank = bank
                                            senderName = bank.name ?: ""
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        else -> {
                            DropdownMenuItem(text = { Text("Loading...") }, onClick = {})
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = senderName,
                onValueChange = { senderName = it },
                label = { Text("Sender Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mode Selection
            Text("Transfer Mode", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("IMPS", "NEFT", "RTGS").forEach { m ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = (selectedMode == m),
                            onClick = { selectedMode = m }
                        ).padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (selectedMode == m), onClick = { selectedMode = m })
                        Text(text = m, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (mobile.length == 10 && selectedBank != null && amount.isNotEmpty()) {
                        viewModel.initiateTransaction(
                            mobile = mobile,
                            bankId = selectedBank!!.id,
                            amount = amount,
                            senderName = senderName,
                            mode = selectedMode
                        )
                    } else {
                        Toast.makeText(context, "Fill all details", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                enabled = initiateState !is Resource.Loading
            ) {
                if (initiateState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("SEND OTP / PROCEED", fontWeight = FontWeight.Bold)
                }
            }

            // List of Accounts
            Spacer(modifier = Modifier.height(32.dp))
            Text("Registered Accounts", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            when (bankListState) {
                is Resource.Success -> {
                    val accounts = bankListState?.data ?: emptyList()
                    accounts.forEach { account ->
                        PayoutAccountItem(account = account) {
                            viewModel.deletePayoutAccount(account.id)
                        }
                    }
                }

                else -> {}
            }
        }
    }

    if (showOtpDialog || showPinDialog) {
        AlertDialog(
            onDismissRequest = {
                showOtpDialog = false
                showPinDialog = false
            },
            title = { Text(if (showOtpDialog) "Enter OTP" else "Enter PIN") },
            text = {
                OutlinedTextField(
                    value = otpPinValue,
                    onValueChange = { otpPinValue = it },
                    label = { Text("Code") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (otpPinValue.isNotEmpty()) {
                            viewModel.doPayoutTransaction(otpPinValue, txnKey)
                        }
                    },
                    enabled = transactionState !is Resource.Loading
                ) {
                    if (transactionState is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text("VERIFY")
                    }
                }
            }
        )
    }
}

@Composable
fun PayoutAccountItem(account: PayoutAccountModel, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = account.name ?: "", fontWeight = FontWeight.Bold)
                Text(
                    text = "${account.bankName} - ${account.account}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}
