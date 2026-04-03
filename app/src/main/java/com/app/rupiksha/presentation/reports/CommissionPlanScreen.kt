package com.app.rupiksha.presentation.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.app.rupiksha.models.FaqList
import com.app.rupiksha.models.FaqModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommissionPlanScreen(
    navController: NavController,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val commissionPlanState by viewModel.commissionPlanState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCommissionPlan()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Commission Plans") },
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
            when (commissionPlanState) {
                is Resource.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    val plans = commissionPlanState?.data ?: emptyList()
                    if (plans.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No commission plans found")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(plans) { plan ->
                                CommissionSection(plan = plan)
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = commissionPlanState?.message ?: "Error loading plans")
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun CommissionSection(plan: FaqModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = plan.label ?: "Service",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        plan.tableData?.forEach { item ->
            CommissionItem(item = item)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CommissionItem(item: FaqList) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = item.operator ?: "General",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "Comm: ${item.commission ?: "0"}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = if (item.froma != null) "Range: ${item.froma} - ${item.toa}" else "Package: ${item._package}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (item.percent != null) {
                    Text(
                        text = "Type: ${item.percent}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
