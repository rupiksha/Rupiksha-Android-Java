package com.app.rupiksha.presentation.recharge

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RechargeScreen(
    navController: NavController,
    title: String,
    type: String,
    viewModel: RechargeViewModel = hiltViewModel()
) {
    var mobileNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedOperator by remember { mutableStateOf<RechargeOperatorModel?>(null) }
    var showOperatorDialog by remember { mutableStateOf(false) }
    var showPlansDialog by remember { mutableStateOf(false) }

    val operatorsState by viewModel.operatorsState.collectAsState()
    val rechargeState by viewModel.rechargeState.collectAsState()
    val fetchOperatorState by viewModel.fetchOperatorState.collectAsState()
    val plans by viewModel.plansState.collectAsState()
    val planTitles by viewModel.planTitlesState.collectAsState()

    val isDth = type.equals("dth", ignoreCase = true)
    val inputLabel = if (isDth) "Customer ID / VC Number" else "Mobile Number"

    LaunchedEffect(mobileNumber) {
        if (!isDth && mobileNumber.length == 10) {
            viewModel.fetchOperator(mobileNumber)
        }
    }

    LaunchedEffect(fetchOperatorState) {
        if (fetchOperatorState is Resource.Success) {
            val operatorName = fetchOperatorState?.data?.operator
            if (operatorsState is Resource.Success && operatorName != null) {
                val operators = (operatorsState as Resource.Success).data
                selectedOperator = operators?.find { it.name.equals(operatorName, ignoreCase = true) }
            }
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { 
                    if (isDth || it.length <= 10) mobileNumber = it 
                },
                label = { Text(inputLabel) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = if (isDth) KeyboardType.Text else KeyboardType.Phone),
                trailingIcon = {
                    if (!isDth) {
                        IconButton(onClick = { /* Open Contacts Implementation */ }) {
                            Icon(Icons.Default.ContactPage, contentDescription = "Contacts")
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = selectedOperator?.name ?: "",
                onValueChange = { },
                label = { Text("Select Operator") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showOperatorDialog = true },
                enabled = false,
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                trailingIcon = {
                    if (selectedOperator != null) {
                        AsyncImage(
                            model = selectedOperator?.image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    if (!isDth && plans.isNotEmpty()) {
                        TextButton(onClick = { showPlansDialog = true }) {
                            Text("VIEW PLANS", color = Color.Red)
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (mobileNumber.isNotEmpty() && selectedOperator != null && amount.isNotEmpty()) {
                        viewModel.doRecharge(mobileNumber, selectedOperator!!.id, amount)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(8.dp),
                enabled = rechargeState !is Resource.Loading
            ) {
                if (rechargeState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("PROCEED TO RECHARGE", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showOperatorDialog) {
        OperatorSelectionDialog(
            operatorsState = operatorsState,
            onDismiss = { showOperatorDialog = false },
            onOperatorSelected = {
                selectedOperator = it
                showOperatorDialog = false
            }
        )
    }

    if (showPlansDialog) {
        PlansDialog(
            planTitles = planTitles,
            allPlans = plans,
            onDismiss = { showPlansDialog = false },
            onPlanSelected = {
                amount = it.rs.toString()
                showPlansDialog = false
            }
        )
    }

    // Handle Recharge Result
    when (rechargeState) {
        is Resource.Success -> {
            RechargeReceiptDialog(
                response = rechargeState!!.data!!,
                onDismiss = {
                    viewModel.resetRechargeState()
                    navController.popBackStack()
                }
            )
        }
        is Resource.Error -> {
            ErrorDialog(
                message = rechargeState!!.message ?: "Recharge Failed",
                onDismiss = { viewModel.resetRechargeState() }
            )
        }
        else -> {}
    }
}

@Composable
fun OperatorSelectionDialog(
    operatorsState: Resource<List<RechargeOperatorModel>>?,
    onDismiss: () -> Unit,
    onOperatorSelected: (RechargeOperatorModel) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Operator") },
        text = {
            Box(modifier = Modifier.height(400.dp)) {
                when (operatorsState) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is Resource.Success -> {
                        val operators = operatorsState.data ?: emptyList()
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(operators) { operator ->
                                Column(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable { onOperatorSelected(operator) },
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = operator.image,
                                        contentDescription = operator.name,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(Color.White),
                                        contentScale = ContentScale.Fit
                                    )
                                    operator.name?.let {
                                        Text(
                                            text = it,
                                            fontSize = 10.sp,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            modifier = Modifier.padding(top = 4.dp),
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Text(text = operatorsState.message ?: "Error loading operators", color = Color.Red)
                    }
                    else -> {}
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun PlansDialog(
    planTitles: List<PlanDataModel>,
    allPlans: List<AllPlans>,
    onDismiss: () -> Unit,
    onPlanSelected: (PlanDetailModel) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Recharge Plans") },
        text = {
            Column(modifier = Modifier.height(500.dp)) {
                if (planTitles.isNotEmpty()) {
                    ScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        edgePadding = 0.dp,
                        containerColor = Color.Transparent,
                        contentColor = Color.Red
                    ) {
                        planTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { title.opCatName?.let { Text(it, fontSize = 12.sp) } }
                            )
                        }
                    }

                    val selectedCatId = planTitles[selectedTabIndex].opCatId
                    val currentPlans = allPlans.find { it.catId == selectedCatId }?.catArray ?: emptyList()

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp)
                    ) {
                        items(currentPlans) { plan ->
                            PlanItem(plan = plan) {
                                onPlanSelected(plan)
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No plans available")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun PlanItem(plan: PlanDetailModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "₹ ${plan.rs}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Red)
                Text(text = plan.validity ?: "", fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = plan.desc ?: "", fontSize = 12.sp, color = Color.Black)
            if (plan.data != null || plan.sms != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    if (plan.data != null) {
                        Text(text = "Data: ${plan.data}", fontSize = 11.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (plan.sms != null) {
                        Text(text = "SMS: ${plan.sms}", fontSize = 11.sp, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}

@Composable
fun RechargeReceiptDialog(response: BaseResponse, onDismiss: () -> Unit) {
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
                Text("Recharge Result", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                val rechargeData = response.data?.rechargeData
                ReceiptRow("Transaction ID", rechargeData?.txnid ?: response.txnKey ?: "N/A")
                ReceiptRow("Date", rechargeData?.date ?: "N/A")
                ReceiptRow("Status", rechargeData?.status ?: response.status ?: "N/A")
                ReceiptRow("Operator", response.operator ?: "N/A")
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                if (rechargeData != null) {
                    ReceiptRow(rechargeData.item1 ?: "Amount", "₹ ${rechargeData.amount1}")
                    ReceiptRow(rechargeData.item2 ?: "Charge", "₹ ${rechargeData.amount2}")
                    ReceiptRow("Total", "₹ ${rechargeData.total}")
                }
            }
        }
    )
}

@Composable
fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Medium, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}

@Composable
fun ErrorDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        },
        icon = { Icon(Icons.Default.Error, contentDescription = null, tint = Color.Red) },
        title = { Text("Error") },
        text = { Text(message) }
    )
}
