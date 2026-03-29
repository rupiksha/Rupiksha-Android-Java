package com.app.rupiksha.presentation.aeps

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.app.rupiksha.models.StateModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AepsKycScreen(
    navController: NavController,
    title: String,
    aepsStatus: String,
    viewModel: AepsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var shopName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf<StateModel?>(null) }
    var selectedBank by remember { mutableStateOf<BankModel?>(null) }
    var panNumber by remember { mutableStateOf("") }
    var aadharNumber by remember { mutableStateOf("") }
    var accountHolderName by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    var ifscCode by remember { mutableStateOf("") }
    var shopImageUrl by remember { mutableStateOf<Uri?>(null) }

    var showStateDialog by remember { mutableStateOf(false) }
    var showBankDialog by remember { mutableStateOf(false) }

    val stateListState by viewModel.stateListState.collectAsState()
    val bankListState by viewModel.bankListState.collectAsState()
    val kycState by viewModel.kycState.collectAsState()

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        shopImageUrl = uri
    }

    LaunchedEffect(Unit) {
        viewModel.getStateList()
        viewModel.getBankList()
    }

    LaunchedEffect(kycState) {
        if (kycState is Resource.Success) {
            Toast.makeText(context, kycState?.data?.message, Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            viewModel.resetTransactionState()
        } else if (kycState is Resource.Error) {
            Toast.makeText(context, kycState?.message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AEPS KYC") },
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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Personal Info
            Text("Personal Information", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            OutlinedTextField(
                value = mobile,
                onValueChange = { mobile = it },
                label = { Text("Mobile Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email ID") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Address Details", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = shopName,
                onValueChange = { shopName = it },
                label = { Text("Shop Name") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Full Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = pinCode,
                    onValueChange = { pinCode = it },
                    label = { Text("Pin Code") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            OutlinedTextField(
                value = selectedState?.name ?: "",
                onValueChange = { },
                label = { Text("Select State") },
                modifier = Modifier.fillMaxWidth().clickable { showStateDialog = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Identity & Bank Details", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = panNumber,
                onValueChange = { panNumber = it },
                label = { Text("PAN Number") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = aadharNumber,
                onValueChange = { aadharNumber = it },
                label = { Text("Aadhar Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = selectedBank?.name ?: "",
                onValueChange = { },
                label = { Text("Select Bank") },
                modifier = Modifier.fillMaxWidth().clickable { showBankDialog = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            OutlinedTextField(
                value = accountHolderName,
                onValueChange = { accountHolderName = it },
                label = { Text("Account Holder Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text("Account Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = ifscCode,
                onValueChange = { ifscCode = it },
                label = { Text("IFSC Code") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val map = mapOf(
                        "first_name" to firstName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "last_name" to lastName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "phone" to mobile.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "email" to email.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "shopname" to shopName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "address" to address.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "pincode" to pinCode.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "city" to city.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "state" to (selectedState?.id?.toString() ?: "").toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "pan" to panNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "aadhar" to aadharNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "bankAccountName" to accountHolderName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "companyBankAccountNumber" to accountNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "companyBankName" to (selectedBank?.name ?: "").toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "bankIfscCode" to ifscCode.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "lat" to "0.0".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "log" to "0.0".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        "outlet" to "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                    viewModel.submitKyc(map, null)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                enabled = kycState !is Resource.Loading
            ) {
                if (kycState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("SUBMIT KYC", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showStateDialog) {
        StateSelectionDialog(
            stateListState = stateListState,
            onDismiss = { showStateDialog = false },
            onStateSelected = {
                selectedState = it
                showStateDialog = false
            }
        )
    }

    if (showBankDialog) {
        BankSelectionDialog(
            bankListState = bankListState,
            onDismiss = { showBankDialog = false },
            onBankSelected = {
                selectedBank = it
                showBankDialog = false
            }
        )
    }
}

@Composable
fun StateSelectionDialog(
    stateListState: Resource<List<StateModel>>?,
    onDismiss: () -> Unit,
    onStateSelected: (StateModel) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search State...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
        },
        text = {
            Box(modifier = Modifier.height(400.dp)) {
                when (stateListState) {
                    is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    is Resource.Success -> {
                        val filtered = stateListState.data?.filter { it.name?.contains(searchQuery, ignoreCase = true) == true } ?: emptyList()
                        LazyColumn {
                            items(filtered) { state ->
                                ListItem(
                                    headlineContent = { Text(state.name ?: "") },
                                    modifier = Modifier.clickable { onStateSelected(state) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                    is Resource.Error -> Text(text = stateListState.message ?: "Error", color = Color.Red)
                    else -> {}
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
