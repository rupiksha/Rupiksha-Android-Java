package com.app.rupiksha.presentation.qtransfer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.GlobalBankModel
import com.app.rupiksha.models.QtAccountModel
import com.app.rupiksha.presentation.recharge.ReceiptRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickTransferScreen(
    navController: NavController,
    title: String,
    viewModel: QuickTransferViewModel = hiltViewModel()
) {
    var mobile by remember { mutableStateOf("") }
    var account by remember { mutableStateOf("") }
    var ifsc by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedBank by remember { mutableStateOf<GlobalBankModel?>(null) }
    
    var showBankDialog by remember { mutableStateOf(false) }
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

    LaunchedEffect(mobile) {
        if (mobile.length == 10) {
            viewModel.fetchAccounts(mobile)
        }
    }

    LaunchedEffect(verifyState) {
        if (verifyState is Resource.Success) {
            name = verifyState?.data?.data?.beneficiaryName ?: name
            Toast.makeText(context, "Account Verified", Toast.LENGTH_SHORT).show()
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
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(accountsState?.data!!) { acc ->
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
                        if (account.isNotEmpty() && ifsc.length >= 4) {
                            viewModel.verifyAccount(account, ifsc, mobile)
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
                Button(onClick = { viewModel.doTransaction(otp, txnKey) }) {
                    Text("CONFIRM")
                }
            }
        )
    }

    if (showReceiptDialog) {
        QtReceiptDialog(response = transactionState?.data!!) {
            showReceiptDialog = false
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
                    ReceiptRow("Amount", "₹ ${data.amount1 ?: "0.0"}")
                    ReceiptRow("Charge", "₹ ${data.amount2 ?: "0.0"}")
                    ReceiptRow("Total", "₹ ${data.total ?: "0.0"}")
                }
            }
        }
    )
}
