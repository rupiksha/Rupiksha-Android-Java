package com.app.rupiksha.presentation.support

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.rupiksha.R
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.SupportTicket
import com.app.rupiksha.models.SupportType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    navController: NavController,
    viewModel: SupportViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Contact", "Raise Ticket", "Tickets")
    
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Support") },
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
                        text = { Text(title, fontSize = 12.sp) }
                    )
                }
            }

            when (selectedTab) {
                0 -> ContactContent(viewModel)
                1 -> RaiseTicketContent(viewModel)
                2 -> TicketsListContent(viewModel)
            }
        }
    }
}

@Composable
fun ContactContent(viewModel: SupportViewModel) {
    val supportState by viewModel.supportState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getSupportData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        when (supportState) {
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val support = supportState?.data
                if (support != null) {
                    ContactInfoCard(icon = Icons.Default.Phone, label = "Phone", value = support.cnumber ?: "N/A")
                    Spacer(modifier = Modifier.height(12.dp))
                    ContactInfoCard(icon = Icons.Default.Email, label = "Email", value = support.cemail ?: "N/A")
                    Spacer(modifier = Modifier.height(12.dp))
                    ContactInfoCard(icon = Icons.Default.LocationOn, label = "Office", value = support.address ?: "N/A")
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Social Media", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        SocialMediaIcon(R.drawable.whatsapp) {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${support.cnumber}")))
                        }
                        SocialMediaIcon(R.drawable.facebook) {
                            support.facebook?.let { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it))) }
                        }
                        SocialMediaIcon(R.drawable.instagram) {
                            support.instagram?.let { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it))) }
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaiseTicketContent(viewModel: SupportViewModel) {
    val supportTypesState by viewModel.supportTypesState.collectAsState()
    val createTicketState by viewModel.createTicketState.collectAsState()
    val context = LocalContext.current

    var selectedType by remember { mutableStateOf<SupportType?>(null) }
    var txnId by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getSupportTypesAndTickets()
    }

    LaunchedEffect(createTicketState) {
        if (createTicketState is Resource.Success) {
            Toast.makeText(context, createTicketState?.data?.message, Toast.LENGTH_SHORT).show()
            txnId = ""
            message = ""
            selectedType = null
            viewModel.resetCreateState()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedType?.name ?: "Select Service",
                onValueChange = {},
                readOnly = true,
                label = { Text("Service Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                if (supportTypesState is Resource.Success) {
                    supportTypesState?.data?.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name ?: "") },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(value = txnId, onValueChange = { txnId = it }, label = { Text("Transaction ID") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Message") }, modifier = Modifier.fillMaxWidth().height(150.dp), maxLines = 5)

        Button(
            onClick = {
                if (selectedType != null && txnId.isNotEmpty() && message.isNotEmpty()) {
                    viewModel.createTicket(selectedType!!.name, txnId, message)
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = createTicketState !is Resource.Loading
        ) {
            if (createTicketState is Resource.Loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("SUBMIT TICKET")
        }
    }
}

@Composable
fun TicketsListContent(viewModel: SupportViewModel) {
    val tickets by viewModel.ticketsState.collectAsState()
    val supportTypesState by viewModel.supportTypesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getSupportTypesAndTickets()
    }

    if (supportTypesState is Resource.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(tickets) { ticket ->
                TicketItem(ticket = ticket)
            }
        }
    }
}

@Composable
fun TicketItem(ticket: SupportTicket) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "ID: ${ticket.ticketid}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    text = ticket.status ?: "PENDING",
                    color = if (ticket.status?.equals("SUCCESS", true) == true) Color(0xFF4CAF50) else Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Service: ${ticket.service}", fontSize = 12.sp, color = Color.Gray)
            Text(text = "Txn ID: ${ticket.txnid}", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = ticket.message ?: "", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Date: ${ticket.date}", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.align(Alignment.End))
        }
    }
}

@Composable
fun ContactInfoCard(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, fontSize = 12.sp, color = Color.Gray)
                Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun SocialMediaIcon(resId: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.White).clickable { onClick() }.padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = resId), contentDescription = null, modifier = Modifier.fillMaxSize())
    }
}
