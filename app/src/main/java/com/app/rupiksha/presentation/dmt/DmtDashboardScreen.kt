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
import com.app.rupiksha.models.DMTBankdetailListModel
import com.app.rupiksha.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DmtDashboardScreen(
    navController: NavController,
    phone: String,
    dmtKey: String,
    viewModel: DmtViewModel = hiltViewModel()
) {
    val bankListState by viewModel.bankListState.collectAsState()
    val initiateTransactionState by viewModel.initiateTransactionState.collectAsState()
    val doTransactionState by viewModel.doTransactionState.collectAsState()
    val context = LocalContext.current

    var showTransferDialog by remember { mutableStateOf(false) }
    var showOtpDialog by remember { mutableStateOf(false) }
    var selectedBeneficiary by remember { mutableStateOf<DMTBankdetailListModel?>(null) }
    var transferAmount by remember { mutableStateOf("") }
    var transferPin by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getDmtBankList()
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
            Toast.makeText(context, doTransactionState?.data?.message, Toast.LENGTH_LONG).show()
            // Here you could show a success receipt
            viewModel.resetStates()
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
                    IconButton(onClick = { /* Navigate to History */ }) {
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
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No beneficiaries found")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(beneficiaries) { beneficiary ->
                                BeneficiaryItem(beneficiary = beneficiary) {
                                    selectedBeneficiary = beneficiary
                                    showTransferDialog = true
                                }
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
                            viewModel.initiateTransaction(selectedBeneficiary?.beneId ?: "", transferAmount)
                        }
                    },
                    enabled = initiateTransactionState !is Resource.Loading
                ) {
                    if (initiateTransactionState is Resource.Loading) {
                        CircularProgressIndicator(size = 20.dp, color = Color.White)
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
            title = { Text("Enter Transaction PIN") },
            text = {
                Column {
                    OutlinedTextField(
                        value = transferPin,
                        onValueChange = { transferPin = it },
                        label = { Text("PIN/OTP") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (transferPin.isNotEmpty()) {
                            viewModel.doTransaction(selectedBeneficiary?.beneId ?: "", transferAmount, transferPin)
                        }
                    },
                    enabled = doTransactionState !is Resource.Loading
                ) {
                    if (doTransactionState is Resource.Loading) {
                        CircularProgressIndicator(size = 20.dp, color = Color.White)
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
}

@Composable
fun RemitterInfoCard(phone: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Remitter:", fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = phone, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BeneficiaryItem(beneficiary: DMTBankdetailListModel, onTransferClick: () -> Unit) {
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
                Text(text = "${beneficiary.bankname} - ${beneficiary.accno}", fontSize = 12.sp, color = Color.Gray)
                Text(text = "IFSC: ${beneficiary.ifsc}", fontSize = 12.sp, color = Color.Gray)
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
