package com.app.rupiksha.presentation.wallet

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.app.rupiksha.presentation.reports.DateButton
import com.app.rupiksha.presentation.reports.ReportDetailItem
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    navController: NavController,
    viewModel: WalletViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("History", "Transfer")
    
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = sdf.format(Date())
    var fromDate by remember { mutableStateOf(currentDate) }
    var toDate by remember { mutableStateOf(currentDate) }

    val walletReportState by viewModel.walletReportState.collectAsState()
    val fetchUserState by viewModel.fetchUserState.collectAsState()
    val transactionState by viewModel.transactionState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(selectedTab) {
        if (selectedTab == 0) {
            viewModel.getWalletReport(fromDate, toDate)
        }
    }

    LaunchedEffect(transactionState) {
        if (transactionState is Resource.Success) {
            Toast.makeText(context, transactionState?.data?.message, Toast.LENGTH_LONG).show()
            viewModel.resetStates()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wallet") },
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

            if (selectedTab == 0) {
                // Wallet History
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DateButton(label = "From: $fromDate", modifier = Modifier.weight(1f)) {
                            // Show Date Picker
                        }
                        DateButton(label = "To: $toDate", modifier = Modifier.weight(1f)) {
                            // Show Date Picker
                        }
                    }

                    when (walletReportState) {
                        is Resource.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        is Resource.Success -> {
                            val reports = walletReportState?.data ?: emptyList()
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
                        is Resource.Error -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = walletReportState?.message ?: "Error")
                            }
                        }
                        else -> {}
                    }
                }
            } else {
                // Wallet Transfer
                WalletTransferContent(viewModel)
            }
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
            label = { Text("Mobile Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        if (fetchUserState is Resource.Success) {
            Text(
                text = "Name: ${fetchUserState?.data?.data?.walletFetchUser?.name ?: "Unknown"}",
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
