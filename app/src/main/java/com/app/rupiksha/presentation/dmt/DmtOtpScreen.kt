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
fun DmtOtpScreen(
    navController: NavController,
    mobile: String,
    otpId: String,
    aadhar: String,
    viewModel: DmtViewModel = hiltViewModel()
) {
    var otpValue by remember { mutableStateOf("") }
    val validateOtpState by viewModel.validateOtpState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(validateOtpState) {
        validateOtpState?.let { state ->
            if (state is Resource.Success) {
                val response = state.data
                if (response != null) {
                    Toast.makeText(context, response.message ?: "OTP Verified", Toast.LENGTH_SHORT).show()
                    
                    val targetAadhar = response.data?.aadhar ?: response.aadhar ?: aadhar
                    val targetOtpId = response.data?.otpid ?: response.otpid ?: otpId
                    val targetMobile = response.data?.mobile ?: response.mobile ?: mobile
                    
                    navController.navigate(
                        Screen.DmtKyc.createRoute(
                            aadhar = targetAadhar,
                            otpId = targetOtpId,
                            mobile = targetMobile
                        )
                    )
                    viewModel.resetStates()
                }
            } else if (state is Resource.Error) {
                Toast.makeText(context, state.message ?: "Error", Toast.LENGTH_SHORT).show()
                viewModel.resetStates()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verify OTP") },
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
                text = "Enter OTP sent to $mobile",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = otpValue,
                onValueChange = { if (it.length <= 6) otpValue = it },
                label = { Text("OTP") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (otpValue.length >= 4) {
                        viewModel.validateOtp(otpValue, otpId, aadhar, mobile, "0.0", "0.0")
                    } else {
                        Toast.makeText(context, "Enter valid OTP", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                enabled = validateOtpState !is Resource.Loading
            ) {
                if (validateOtpState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("VERIFY OTP", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
