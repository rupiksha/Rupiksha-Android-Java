package com.app.rupiksha.presentation.bbps

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.BillerModel
import com.app.rupiksha.presentation.recharge.ErrorDialog
import com.app.rupiksha.presentation.recharge.ReceiptRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BbpsScreen(
    navController: NavController,
    title: String,
    type: String,
    viewModel: BbpsViewModel = hiltViewModel()
) {
    var selectedBiller by remember { mutableStateOf<BillerModel?>(null) }
    var showBillerDialog by remember { mutableStateOf(false) }
    
    var param1 by remember { mutableStateOf("") }
    var param2 by remember { mutableStateOf("") }
    var param3 by remember { mutableStateOf("") }
    var param4 by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val billerListState by viewModel.billerListState.collectAsState()
    val fetchBillState by viewModel.fetchBillState.collectAsState()
    val payBillState by viewModel.payBillState.collectAsState()

    LaunchedEffect(type) {
        viewModel.getBillerList(type)
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = selectedBiller?.name ?: "",
                onValueChange = { },
                label = { Text("Select Operator") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showBillerDialog = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                trailingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            )

            if (selectedBiller != null) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Param 1
                if (!selectedBiller?.param1.isNullOrEmpty()) {
                    OutlinedTextField(
                        value = param1,
                        onValueChange = { param1 = it },
                        label = { Text(selectedBiller?.param1 ?: "") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Param 2
                if (!selectedBiller?.param2.isNullOrEmpty()) {
                    OutlinedTextField(
                        value = param2,
                        onValueChange = { param2 = it },
                        label = { Text(selectedBiller?.param2 ?: "") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // If bill fetching is NOT supported or not yet fetched
                if (selectedBiller?.fetchBill == "0") {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = {
                            viewModel.payBill(
                                billerId = selectedBiller?.operator_id?.toIntOrNull() ?: 0,
                                amount = amount,
                                param1 = param1,
                                param2 = param2,
                                param3 = param3,
                                param4 = param4,
                                skey = "",
                                lat = "0.0",
                                log = "0.0"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        enabled = payBillState !is Resource.Loading
                    ) {
                        if (payBillState is Resource.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("PAY BILL", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Bill fetching supported
                    if (fetchBillState is Resource.Success) {
                        BillDetailsCard(response = fetchBillState!!.data!!)
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Button(
                            onClick = {
                                viewModel.payBill(
                                    billerId = selectedBiller?.operator_id?.toIntOrNull() ?: 0,
                                    amount = fetchBillState?.data?.data?.fetchdata?.item3value ?: "0",
                                    param1 = param1,
                                    param2 = param2,
                                    param3 = param3,
                                    param4 = param4,
                                    skey = fetchBillState?.data?.skey ?: "",
                                    lat = "0.0",
                                    log = "0.0"
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            enabled = payBillState !is Resource.Loading
                        ) {
                            if (payBillState is Resource.Loading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("PAY BILL", fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Button(
                            onClick = {
                                viewModel.fetchBill(
                                    billerId = selectedBiller?.operator_id?.toIntOrNull() ?: 0,
                                    param1 = param1,
                                    param2 = param2,
                                    param3 = param3,
                                    param4 = param4,
                                    lat = "0.0",
                                    log = "0.0"
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            enabled = fetchBillState !is Resource.Loading
                        ) {
                            if (fetchBillState is Resource.Loading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("FETCH BILL", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showBillerDialog) {
        BillerSelectionDialog(
            billerListState = billerListState,
            onDismiss = { showBillerDialog = false },
            onBillerSelected = {
                selectedBiller = it
                showBillerDialog = false
                viewModel.resetStates()
            }
        )
    }

    // Results
    when (payBillState) {
        is Resource.Success -> {
            BbpsReceiptDialog(
                response = payBillState!!.data!!,
                onDismiss = {
                    viewModel.resetStates()
                    navController.popBackStack()
                }
            )
        }
        is Resource.Error -> {
            ErrorDialog(message = payBillState!!.message ?: "Payment Failed") {
                viewModel.resetStates()
            }
        }
        else -> {}
    }
}

@Composable
fun BillDetailsCard(response: BaseResponse) {
    val data = response.data?.fetchdata
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Bill Details", fontWeight = FontWeight.Bold, color = Color.Gray)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            ReceiptRow(data?.paramname ?: "Name", data?.paramvalue ?: "")
            ReceiptRow(data?.item1 ?: "Bill Number", data?.item1value ?: "")
            ReceiptRow(data?.item2 ?: "Bill Date", data?.item2value ?: "")
            ReceiptRow(data?.item3 ?: "Amount", "₹ ${data?.item3value ?: "0"}")
        }
    }
}

@Composable
fun BillerSelectionDialog(
    billerListState: Resource<List<BillerModel>>?,
    onDismiss: () -> Unit,
    onBillerSelected: (BillerModel) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Select Provider")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )
            }
        },
        text = {
            Box(modifier = Modifier.height(400.dp)) {
                when (billerListState) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is Resource.Success -> {
                        val filteredList = billerListState.data?.filter { 
                            it.name?.contains(searchQuery, ignoreCase = true) == true
                        } ?: emptyList()
                        
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(filteredList) { biller ->
                                ListItem(
                                    headlineContent = { Text(biller.name ?: "") },
                                    modifier = Modifier.clickable { onBillerSelected(biller) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                    is Resource.Error -> {
                        Text(text = billerListState.message ?: "Error", color = Color.Red)
                    }
                    else -> {}
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun BbpsReceiptDialog(response: BaseResponse, onDismiss: () -> Unit) {
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
                Text("Payment Result", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                ReceiptRow("Status", response.status ?: "N/A")
                ReceiptRow("Message", response.message ?: "")
                
                if (response.data?.bbpsrecent?.isNotEmpty() == true) {
                    val recent = response.data.bbpsrecent[0]
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    ReceiptRow("Transaction ID", recent.txnid ?: "")
                    ReceiptRow("Date", recent.date ?: "")
                    ReceiptRow("Amount", "₹ ${recent.amount}")
                }
            }
        }
    )
}
