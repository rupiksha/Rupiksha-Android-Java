package com.app.rupiksha.presentation.uti

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.app.rupiksha.models.StateModel
import com.app.rupiksha.presentation.aeps.StateSelectionDialog
import com.app.rupiksha.presentation.recharge.ReceiptRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtiScreen(
    navController: NavController,
    viewModel: UtiViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val utiStatusState by viewModel.utiStatusState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.getUtiStatus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UTI / PSA Service") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (utiStatusState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Success -> {
                    val status = utiStatusState?.data?.utiStatus ?: ""
                    when (status.uppercase()) {
                        "YES" -> {
                            BuyCouponContent(viewModel, utiStatusState?.data?.couponCharge ?: 0.0)
                        }
                        "NO" -> {
                            PsaRegistrationContent(viewModel)
                        }
                        "PENDING" -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Application Pending", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    Text("Your PSA registration is under review.", color = Color.Gray)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Support: ${utiStatusState?.data?.data?.utiSupportData?.supportmail ?: "N/A"}")
                                    Text("Phone: ${utiStatusState?.data?.data?.utiSupportData?.supportphone ?: "N/A"}")
                                }
                            }
                        }
                        else -> {
                            PsaRegistrationContent(viewModel)
                        }
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = utiStatusState?.message ?: "Error",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PsaRegistrationContent(viewModel: UtiViewModel) {
    val context = LocalContext.current
    val stateListState by viewModel.stateListState.collectAsState()
    val registrationState by viewModel.registrationState.collectAsState()

    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var shopLocation by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var pan by remember { mutableStateOf("") }
    var aadhar by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf<StateModel?>(null) }
    var showStateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getStateList()
    }

    LaunchedEffect(registrationState) {
        if (registrationState is Resource.Success) {
            Toast.makeText(context, registrationState?.data?.message, Toast.LENGTH_LONG).show()
            viewModel.getUtiStatus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("PSA Registration", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = mobile, onValueChange = { mobile = it }, label = { Text("Mobile") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = shopLocation, onValueChange = { shopLocation = it }, label = { Text("Shop Address") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Home Address") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = pincode, onValueChange = { pincode = it }, label = { Text("Pincode") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        
        OutlinedTextField(
            value = selectedState?.name ?: "Select State",
            onValueChange = {},
            readOnly = true,
            label = { Text("State") },
            modifier = Modifier.fillMaxWidth().clickable { showStateDialog = true },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black, disabledBorderColor = Color.Gray)
        )

        OutlinedTextField(value = pan, onValueChange = { pan = it }, label = { Text("PAN Number") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = aadhar, onValueChange = { aadhar = it }, label = { Text("Aadhar Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && selectedState != null) {
                    viewModel.psaRegistration(name, aadhar, email, mobile, pan, address, shopLocation, pincode, selectedState!!.id)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = registrationState !is Resource.Loading
        ) {
            if (registrationState is Resource.Loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("SUBMIT REGISTRATION")
        }
    }

    if (showStateDialog) {
        StateSelectionDialog(stateListState = stateListState, onDismiss = { showStateDialog = false }) {
            selectedState = it
            showStateDialog = false
        }
    }
}

@Composable
fun BuyCouponContent(viewModel: UtiViewModel, couponCharge: Double) {
    val context = LocalContext.current
    val buyCouponState by viewModel.buyCouponState.collectAsState()
    
    var name by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }
    var showReceipt by remember { mutableStateOf(false) }

    LaunchedEffect(buyCouponState) {
        if (buyCouponState is Resource.Success) {
            showReceipt = true
        } else if (buyCouponState is Resource.Error) {
            Toast.makeText(context, buyCouponState?.message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Buy UTI Coupon", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Account Name") }, modifier = Modifier.fillMaxWidth())
        
        OutlinedTextField(
            value = couponCharge.toString(),
            onValueChange = {},
            label = { Text("Coupon Charge") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        OutlinedTextField(
            value = qty,
            onValueChange = { qty = it },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && qty.isNotEmpty()) {
                    viewModel.buyCoupon(qty)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = buyCouponState !is Resource.Loading
        ) {
            if (buyCouponState is Resource.Loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("BUY NOW")
        }
    }

    if (showReceipt) {
        val data = buyCouponState?.data?.data?.utiReceipt
        AlertDialog(
            onDismissRequest = { showReceipt = false },
            confirmButton = { Button(onClick = { showReceipt = false }) { Text("CLOSE") } },
            title = { Text("Coupon Purchase Result") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (data != null) {
                        ReceiptRow("Status", data.status ?: "N/A")
                        ReceiptRow("Transaction ID", data.txnid ?: "N/A")
                        ReceiptRow("Date", data.date ?: "N/A")
                        ReceiptRow(data.item1 ?: "Quantity", data.amount1.toString())
                        ReceiptRow(data.item2 ?: "Total", "₹ ${data.amount2}")
                    } else {
                        Text(buyCouponState?.data?.message ?: "Success")
                    }
                }
            }
        )
    }
}
