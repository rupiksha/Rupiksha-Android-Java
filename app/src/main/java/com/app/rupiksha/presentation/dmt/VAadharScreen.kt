package com.app.rupiksha.presentation.dmt

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VAadharScreen(
    navController: NavController,
    phone: String,
    viewModel: DmtViewModel = hiltViewModel()
) {
    var aadhar by remember { mutableStateOf("") }
    val validateAadharState by viewModel.validateAadharState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(validateAadharState) {
        if (validateAadharState is Resource.Success) {
            val response = validateAadharState?.data
            if (response != null) {
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                // Original app navigates to DMTOtpActivity
                navController.navigate("dmt_otp_screen/${response.data.mobile}/${response.data.otpid}/${response.data.aadhar}")
                viewModel.resetStates()
            }
        } else if (validateAadharState is Resource.Error) {
            Toast.makeText(context, validateAadharState?.message ?: "Error", Toast.LENGTH_SHORT).show()
            viewModel.resetStates()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aadhar Verification") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Enter Aadhar Number",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = aadhar,
                onValueChange = { if (it.length <= 12) aadhar = it },
                label = { Text("Aadhar Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (aadhar.length == 12) {
                        viewModel.validateAadhar(aadhar, phone)
                    } else {
                        Toast.makeText(context, "Enter 12 digit aadhar number", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                enabled = validateAadharState !is Resource.Loading
            ) {
                if (validateAadharState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("VERIFY", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
