package com.app.rupiksha.presentation.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.AEPSReportDetailModel
import com.app.rupiksha.ui.components.RupikshaDatePickerDialog
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailsScreen(
    navController: NavController,
    title: String,
    type: String,
    viewModel: ReportDetailsViewModel = hiltViewModel()
) {
    val reportState by viewModel.reportState.collectAsState()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = sdf.format(Date())
    
    var fromDate by remember { mutableStateOf(currentDate) }
    var toDate by remember { mutableStateOf(currentDate) }
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }
    
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.getReports(type, fromDate, toDate)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
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
            // Date Selection Row
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
                        fromDate = it
                        viewModel.getReports(type, it, toDate)
                    },
                    onDismiss = { showFromDatePicker = false }
                )
            }

            if (showToDatePicker) {
                RupikshaDatePickerDialog(
                    onDateSelected = { 
                        toDate = it
                        viewModel.getReports(type, fromDate, it)
                    },
                    onDismiss = { showToDatePicker = false }
                )
            }

            when (reportState) {
                is Resource.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    val reports = reportState?.data ?: emptyList()
                    if (reports.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No data found")
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(reports) { report ->
                                ReportDetailItem(report = report)
                            }
                            
                            if (viewModel.canLoadMore()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    }
                                    LaunchedEffect(Unit) {
                                        viewModel.getReports(type, fromDate, toDate, isInitial = false)
                                    }
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = reportState?.message ?: "An error occurred")
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun DateButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, fontSize = 12.sp)
        }
    }
}

@Composable
fun ReportDetailItem(report: AEPSReportDetailModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = report.txnid ?: "N/A", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    text = report.status ?: "PENDING",
                    color = when (report.status?.lowercase()) {
                        "success" -> Color(0xFF4CAF50)
                        "failure", "failed" -> Color.Red
                        else -> Color(0xFFFFC107)
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = report.date ?: "", fontSize = 12.sp, color = Color.Gray)
                Text(text = "₹ ${report.amount ?: "0.0"}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
            if (report.bank != null) {
                Text(text = "Bank: ${report.bank}", fontSize = 12.sp)
            }
            if (report.mobile != null) {
                Text(text = "Mobile: ${report.mobile}", fontSize = 12.sp)
            }
        }
    }
}
