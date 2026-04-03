package com.app.rupiksha.presentation.wallet

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.AddfundBankModel
import com.app.rupiksha.presentation.reports.DateButton
import com.app.rupiksha.presentation.reports.ReportDetailItem
import com.app.rupiksha.ui.components.RupikshaDatePickerDialog
import com.app.rupiksha.utils.FileUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    navController: NavController,
    viewModel: WalletViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("History", "Transfer", "Add Fund")
    
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = sdf.format(Date())
    var fromDate by remember { mutableStateOf(currentDate) }
    var toDate by remember { mutableStateOf(currentDate) }

    val walletBalanceState by viewModel.walletBalanceState.collectAsState()

    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            0 -> viewModel.getWalletReport(fromDate, toDate)
            2 -> viewModel.getBankList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Wallet", style = MaterialTheme.typography.titleMedium)
                        if (walletBalanceState is Resource.Success) {
                            Text(
                                "Balance: ₹${walletBalanceState?.data?.walletBalance ?: "0.0"}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
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
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> WalletHistoryContent(viewModel, fromDate, toDate, { fromDate = it }, { toDate = it })
                1 -> WalletTransferContent(viewModel)
                2 -> AddFundContent(viewModel)
            }
        }
    }
}

@Composable
fun WalletHistoryContent(
    viewModel: WalletViewModel,
    fromDate: String,
    toDate: String,
    onFromDateChange: (String) -> Unit,
    onToDateChange: (String) -> Unit
) {
    val walletReportState by viewModel.walletReportState.collectAsState()
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DateButton(label = "From: $fromDate", modifier = Modifier.weight(1f)) {
                showFromDatePicker = true
            }
            DateButton(label = "To: $toDate", modifier = Modifier.weight(1f)) {
                showToDatePicker = true
            }
        }

        if (showFromDatePicker) {
            RupikshaDatePickerDialog(
                onDateSelected = { 
                    onFromDateChange(it)
                    viewModel.getWalletReport(it, toDate)
                },
                onDismiss = { showFromDatePicker = false }
            )
        }

        if (showToDatePicker) {
            RupikshaDatePickerDialog(
                onDateSelected = { 
                    onToDateChange(it)
                    viewModel.getWalletReport(fromDate, it)
                },
                onDismiss = { showToDatePicker = false }
            )
        }

        when (walletReportState) {
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val reports = walletReportState?.data ?: emptyList()
                if (reports.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No records found")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(reports) { report ->
                            ReportDetailItem(report = report)
                        }
                    }
                }
            }
            is Resource.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = walletReportState?.message ?: "Error")
                }
            }
            else -> {}
        }
    }
}

@Composable
fun WalletTransferContent(viewModel: WalletViewModel) {
    var phone by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val fetchUserState by viewModel.fetchUserState.collectAsState()
    val transactionState by viewModel.transactionState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(transactionState) {
        if (transactionState is Resource.Success) {
            Toast.makeText(context, transactionState?.data?.message, Toast.LENGTH_LONG).show()
            viewModel.resetStates()
            phone = ""
            amount = ""
            viewModel.getWalletBalance()
        } else if (transactionState is Resource.Error) {
            Toast.makeText(context, transactionState?.message, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = phone,
            onValueChange = { 
                phone = it
                if (it.length == 10) viewModel.fetchUser(it)
            },
            label = { Text("Receiver Mobile Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )

        if (fetchUserState is Resource.Success) {
            Text(
                text = "Receiver Name: ${fetchUserState?.data?.data?.walletFetchUser?.name ?: "Unknown"}",
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount to Transfer") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (phone.length == 10 && amount.isNotEmpty()) {
                    viewModel.doTransaction(phone, amount)
                } else {
                    Toast.makeText(context, "Please fill details correctly", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = transactionState !is Resource.Loading
        ) {
            if (transactionState is Resource.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("TRANSFER NOW")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFundContent(viewModel: WalletViewModel) {
    val bankListState by viewModel.bankListState.collectAsState()
    val addMoneyState by viewModel.addMoneyState.collectAsState()
    val context = LocalContext.current
    
    var amount by remember { mutableStateOf("") }
    var utr by remember { mutableStateOf("") }
    var selectedBank by remember { mutableStateOf<AddfundBankModel?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(addMoneyState) {
        if (addMoneyState is Resource.Success) {
            Toast.makeText(context, addMoneyState?.data?.message, Toast.LENGTH_LONG).show()
            viewModel.resetStates()
            amount = ""
            utr = ""
            selectedBank = null
            imageUri = null
        } else if (addMoneyState is Resource.Error) {
            Toast.makeText(context, addMoneyState?.message, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedBank?.let { "${it.name} (${it.account})" } ?: "Select Bank",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Bank") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (bankListState is Resource.Success) {
                    bankListState?.data?.forEach { bank ->
                        DropdownMenuItem(
                            text = { Text("${bank.name} - ${bank.account}") },
                            onClick = {
                                selectedBank = bank
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        OutlinedTextField(
            value = utr,
            onValueChange = { utr = it },
            label = { Text("UTR/Transaction ID") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Transaction Date") },
            modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
            trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        if (showDatePicker) {
            RupikshaDatePickerDialog(
                onDateSelected = { selectedDate = it },
                onDismiss = { showDatePicker = false }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFFEEEEEE))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Proof Image",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(48.dp))
                    Text("Upload Payment Proof")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val currentUri = imageUri
                if (amount.isNotEmpty() && selectedBank != null && utr.isNotEmpty() && currentUri != null) {
                    try {
                        val file = FileUtil.from(context, currentUri)
                        if (file != null) {
                            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                            val body = MultipartBody.Part.createFormData("proof", file.name, requestFile)
                            
                            viewModel.addMoney(
                                amount = amount,
                                bankId = selectedBank!!.id,
                                utr = utr,
                                date = selectedDate,
                                proofImage = body
                            )
                        } else {
                            Toast.makeText(context, "Error reading image file", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error processing image", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill all fields and upload proof", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = addMoneyState !is Resource.Loading
        ) {
            if (addMoneyState is Resource.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("SUBMIT REQUEST")
            }
        }
    }
}
