package com.app.rupiksha.presentation.dmt

import android.widget.Toast
import androidx.compose.foundation.layout.*
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
import com.app.rupiksha.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DmtLoginScreen(
    navController: NavController,
    viewModel: DmtViewModel = hiltViewModel()
) {
    var phone by remember { mutableStateOf("") }
    val remitterLoginState by viewModel.remitterLoginState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(remitterLoginState) {
        remitterLoginState?.let { state ->
            when (state) {
                is Resource.Success -> {
                    val response = state.data
                    if (response != null) {
                        Toast.makeText(context, response.message ?: "Login successful", Toast.LENGTH_SHORT).show()
                        when (response.activity?.lowercase()) {
                            "dashboard" -> {
                                navController.navigate(Screen.DmtDashboard.createRoute(phone, response.dmtKey ?: "")) {
                                    popUpTo(Screen.DmtLogin.route) { inclusive = true }
                                }
                            }
                            "register" -> {
                                navController.navigate(Screen.DmtRegister.createRoute(phone, response.dmtKey ?: ""))
                            }
                            "vaadhar" -> {
                                navController.navigate(Screen.DmtVAadhar.createRoute(phone))
                            }
                            else -> {
                                // Default fallback to dashboard if key is present
                                if (response.dmtKey != null) {
                                    navController.navigate(Screen.DmtDashboard.createRoute(phone, response.dmtKey)) {
                                        popUpTo(Screen.DmtLogin.route) { inclusive = true }
                                    }
                                }
                            }
                        }
                        viewModel.resetStates()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, state.message ?: "Error", Toast.LENGTH_SHORT).show()
                    viewModel.resetStates()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Domestic Money Transfer") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Enter Remitter Mobile Number",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            
            Text(
                text = "Login or register to transfer money instantly",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { if (it.length <= 10) phone = it },
                label = { Text("Mobile Number") },
                placeholder = { Text("10-digit mobile number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (phone.length == 10) {
                        viewModel.remitterLogin(phone)
                    } else {
                        Toast.makeText(context, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)), // Deep Red
                enabled = remitterLoginState !is Resource.Loading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (remitterLoginState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("CONTINUE", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
