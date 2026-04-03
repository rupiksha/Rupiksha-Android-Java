package com.app.rupiksha.presentation.qtransfer

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.GlobalBankModel
import com.app.rupiksha.models.QtAccountModel
import com.app.rupiksha.presentation.recharge.ReceiptRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickTransferScreen(
    navController: NavController,
    screenTitle: String,
    viewModel: QuickTransferViewModel = hiltViewModel()
) {
    var mobile by remember { mutableStateOf("") }
    var account by remember { mutableStateOf("") }
    var ifsc by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedBank by remember { mutableStateOf<GlobalBankModel?>(null) }
    
    var showOtpDialog by remember { mutableStateOf(false) }
    var showReceiptDialog by remember { mutableStateOf(false) }
    var txnKey by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    val context = LocalContext.current
    val accountsState by viewModel.accountsState.collectAsState()
    val bankListState by viewModel.bankListState.collectAsState()
    val verifyState by viewModel.verifyState.collectAsState()
    val initiateState by viewModel.initiateState.collectAsState()
    val transactionState by viewModel.transactionState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (!granted) {
            Toast.makeText(context, "Location permission is required for financial transactions", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (permissions.any { ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED }) {
            permissionLauncher.launch(permissions)
        }
    }

    LaunchedEffect(mobile) {
        if (mobile.length == 10) {
            viewModel.fetchAccounts(mobile)
        }
    }

    LaunchedEffect(verifyState) {
        if (verifyState is Resource.Success) {
            name = verifyState?.data?.name ?: name
            Toast.makeText(context, "Account Verified", Toast.LENGTH_SHORT).show()
        } else if (verifyState is Resource.Error) {
            Toast.makeText(context, verifyState?.message ?: "Verification Failed", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(initiateState) {
        if (initiateState is Resource.Success) {
            txnKey = initiateState?.data?.txnKey ?: ""
            showOtpDialog = true
        } else if (initiateState is Resource.Error) {
            Toast.makeText(context, initiateState?.message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(transactionState) {
        if (transactionState is Resource.Success) {
            showOtpDialog = false
            showReceiptDialog = true
        } else if (transactionState is Resource.Error) {
            Toast.makeText(context, transactionState?.message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle) },
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
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = mobile,
                onValueChange = { if (it.length <= 10) mobile = it },
                label = { Text("Sender Mobile") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            if (accountsState is Resource.Success && accountsState?.data?.isNotEmpty() == true) {
                Text("Saved Accounts", fontWeight = FontWeight.Bold)
                // Using a Column instead of LazyColumn to avoid nesting scrollable error
                accountsState?.data?.forEach { acc ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {
                            account = acc.account ?: ""
                            ifsc = acc.ifsc ?: ""
                            name = acc.name ?: ""
                        }
                    ) {
                        ListItem(
                            headlineContent = { Text(acc.name ?: "") },
                            supportingContent = { Text("${acc.account} - ${acc.ifsc}") }
                        )
                    }
                }
            }

            Box {
                var expanded by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = selectedBank?.name ?: "Select Bank",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Bank") },
                    modifier = Modifier.fillMaxWidth().clickable { expanded = true },
                    trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, null) },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    if (bankListState is Resource.Success) {
                        bankListState?.data?.forEach { bank ->
                            DropdownMenuItem(
                                text = { Text(bank.name ?: "") },
                                onClick = {
                                    selectedBank = bank
                                    ifsc = bank.ifscGlobal ?: ifsc
                                    expanded = false
                                }
                            )
                        }
                    } else if (bankListState is Resource.Loading) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
            }

            OutlinedTextField(
                value = account,
                onValueChange = { account = it },
                label = { Text("Account Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = ifsc,
                onValueChange = { ifsc = it },
                label = { Text("IFSC Code") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(onClick = {
                        if (account.isNotEmpty() && ifsc.length >= 4 && mobile.length == 10) {
                            viewModel.verifyAccount(account, ifsc, mobile)
                        } else {
                            Toast.makeText(context, "Fill account, IFSC and mobile first", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("VERIFY")
                    }
                }
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Beneficiary Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    if (mobile.length == 10 && account.isNotEmpty() && amount.isNotEmpty()) {
                        viewModel.initiateTransaction(mobile, amount, account, ifsc, name)
                    } else {
                        Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                enabled = initiateState !is Resource.Loading
            ) {
                if (initiateState is Resource.Loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("TRANSFER NOW", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showOtpDialog) {
        AlertDialog(
            onDismissRequest = { showOtpDialog = false },
            title = { Text("Enter OTP") },
            text = {
                OutlinedTextField(
                    value = otp,
                    onValueChange = { otp = it },
                    label = { Text("OTP") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(
                    onClick = { 
                        if (otp.isNotEmpty()) viewModel.doTransaction(otp, txnKey)
                    },
                    enabled = transactionState !is Resource.Loading
                ) {
                    if (transactionState is Resource.Loading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    else Text("CONFIRM")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOtpDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showReceiptDialog) {
        QtReceiptDialog(response = transactionState?.data!!) {
            showReceiptDialog = false
            viewModel.resetStates()
            navController.popBackStack()
        }
    }
}

@Composable
fun QtReceiptDialog(response: BaseResponse, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("CLOSE") } },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.CheckCircle, null, tint = Color.Green, modifier = Modifier.size(64.dp))
                Text("Transaction Success", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            val data = response.data?.rechargeData
            Column(modifier = Modifier.fillMaxWidth()) {
                if (data != null) {
                    ReceiptRow("Status", data.status ?: "SUCCESS")
                    ReceiptRow("Txn ID", data.txnid ?: "N/A")
                    ReceiptRow("Date", data.date ?: "N/A")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    ReceiptRow("Amount", "₹ ${data.amount1}")
                    ReceiptRow("Charge", "₹ ${data.amount2}")
                    ReceiptRow("Total", "₹ ${data.total}")
                } else {
                    Text(response.message ?: "Transaction Finished")
                }
            }
        }
    )
}
