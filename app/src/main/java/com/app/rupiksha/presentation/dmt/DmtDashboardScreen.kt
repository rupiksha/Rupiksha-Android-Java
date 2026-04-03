package com.app.rupiksha.presentation.dmt

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
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
import com.app.rupiksha.models.DMTBankdetailListModel
import com.app.rupiksha.presentation.navigation.Screen
import com.app.rupiksha.presentation.recharge.ReceiptRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DmtDashboardScreen(
    navController: NavController,
    phone: String,
    dmtKey: String,
    viewModel: DmtViewModel = hiltViewModel()
) {
    val initiateTransactionState by viewModel.initiateTransactionState.collectAsState()
    val doTransactionState by viewModel.doTransactionState.collectAsState()
    val bankListState by viewModel.bankListState.collectAsState()
    val context = LocalContext.current

    var showTransferDialog by remember { mutableStateOf(false) }
    var showOtpDialog by remember { mutableStateOf(false) }
    var showReceiptDialog by remember { mutableStateOf(false) }
    var selectedBeneficiary by remember { mutableStateOf<DMTBankdetailListModel?>(null) }
    var transferAmount by remember { mutableStateOf("") }
    var transferOtp by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getDmtAccountList()
    }

    LaunchedEffect(initiateTransactionState) {
        if (initiateTransactionState is Resource.Success) {
            showTransferDialog = false
            showOtpDialog = true
            Toast.makeText(context, initiateTransactionState?.data?.message, Toast.LENGTH_SHORT).show()
        } else if (initiateTransactionState is Resource.Error) {
            Toast.makeText(context, initiateTransactionState?.message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(doTransactionState) {
        if (doTransactionState is Resource.Success) {
            showOtpDialog = false
            showReceiptDialog = true
            viewModel.getDmtAccountList()
        } else if (doTransactionState is Resource.Error) {
            Toast.makeText(context, doTransactionState?.message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DMT Dashboard") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        navController.navigate(Screen.ReportDetails.createRoute("DMT Reports", "dmt-report"))
                    }) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.DmtAddBeneficiary.route) },
                containerColor = Color.Red,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Beneficiary")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            RemitterInfoCard(phone = phone)

            Text(
                text = "Beneficiaries",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            when (bankListState) {
                is Resource.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Success -> {
                    val beneficiaries = bankListState?.data?.data?.bankdetaillist ?: emptyList()
                    if (beneficiaries.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No beneficiaries found")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(beneficiaries) { beneficiary ->
                                BeneficiaryItem(
                                    beneficiary = beneficiary,
                                    onTransferClick = {
                                        selectedBeneficiary = beneficiary
                                        showTransferDialog = true
                                    },
                                    onDeleteClick = {
                                        viewModel.deleteAccount(beneficiary.beneId ?: "")
                                    }
                                )
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = bankListState?.message ?: "Error", color = Color.Red)
                    }
                }

                else -> {}
            }
        }
    }

    if (showTransferDialog) {
        AlertDialog(
            onDismissRequest = { showTransferDialog = false },
            title = { Text("Transfer Money to ${selectedBeneficiary?.name}") },
            text = {
                Column {
                    OutlinedTextField(
                        value = transferAmount,
                        onValueChange = { transferAmount = it },
                        label = { Text("Amount") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (transferAmount.isNotEmpty()) {
                            viewModel.initiateTransaction(
                                selectedBeneficiary?.beneId ?: "",
                                transferAmount
                            )
                        }
                    },
                    enabled = initiateTransactionState !is Resource.Loading
                ) {
                    if (initiateTransactionState is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("INITIATE")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showTransferDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showOtpDialog) {
        AlertDialog(
            onDismissRequest = { showOtpDialog = false },
            title = { Text("Enter OTP") },
            text = {
                Column {
                    OutlinedTextField(
                        value = transferOtp,
                        onValueChange = { transferOtp = it },
                        label = { Text("OTP") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (transferOtp.isNotEmpty()) {
                            viewModel.doTransaction(transferOtp)
                        }
                    },
                    enabled = doTransactionState !is Resource.Loading
                ) {
                    if (doTransactionState is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("CONFIRM")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showOtpDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showReceiptDialog) {
        DmtReceiptDialog(
            response = doTransactionState?.data!!,
            onDismiss = {
                showReceiptDialog = false
                viewModel.resetStates()
                transferAmount = ""
                transferOtp = ""
            }
        )
    }
}

@Composable
fun BeneficiaryItem(
    beneficiary: DMTBankdetailListModel,
    onTransferClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = beneficiary.name ?: "Unknown", fontWeight = FontWeight.Bold)
                Text(
                    text = "${beneficiary.bankname} - ${beneficiary.accno}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(text = "IFSC: ${beneficiary.ifsc}", fontSize = 12.sp, color = Color.Gray)
            }
            Row {
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
                Button(
                    onClick = onTransferClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("TRANSFER", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun DmtReceiptDialog(response: BaseResponse, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("CLOSE")
            }
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Transaction Success", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            val data = response.data?.dmtReceiptData
            Column(modifier = Modifier.fillMaxWidth()) {
                if (data != null) {
                    ReceiptRow("Status", data.status ?: "SUCCESS")
                    ReceiptRow("Transaction ID", data.txnid ?: "N/A")
                    ReceiptRow("Date", data.date ?: "N/A")
                    ReceiptRow("Name", data.name ?: "N/A")
                    ReceiptRow("Account", data.account ?: "N/A")
                    ReceiptRow("IFSC", data.ifsc ?: "N/A")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    ReceiptRow("Amount", "₹ ${data.amount1 ?: "0.0"}")
                    ReceiptRow("Charge", "₹ ${data.amount2 ?: "0.0"}")
                    ReceiptRow("Total", "₹ ${data.total ?: "0.0"}")
                } else {
                    Text(response.message ?: "Transaction Finished")
                }
            }
        }
    )
}

@Composable
fun RemitterInfoCard(phone: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Remitter Mobile", fontSize = 12.sp, color = Color.Gray)
                Text(text = phone, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
