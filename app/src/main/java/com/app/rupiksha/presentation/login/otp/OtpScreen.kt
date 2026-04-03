package com.app.rupiksha.presentation.login.otp

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.presentation.navigation.Screen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(
    navController: NavController,
    logKey: String,
    viewModel: OtpViewModel = hiltViewModel()
) {
    var otpValue by remember { mutableStateOf("") }
    var ticks by remember { mutableIntStateOf(30) }
    val otpVerifyState by viewModel.otpVerifyState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        while (ticks > 0) {
            delay(1000)
            ticks--
        }
    }

    LaunchedEffect(otpVerifyState) {
        when (otpVerifyState) {
            is Resource.Success -> {
                val body = otpVerifyState?.data
                if (body != null) {
                    Toast.makeText(context, body.message, Toast.LENGTH_SHORT).show()
                    val kycStatus = body.data?.accountkycstatus?.kyc ?: ""
                    when (kycStatus.uppercase()) {
                        "YES" -> {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        "PENDING" -> {
                            val email = body.data?.profile?.email ?: ""
                            val phone = body.data?.profile?.mobile ?: ""
                            navController.navigate(Screen.PendingKyc.createRoute(email, phone)) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        else -> {
                            navController.navigate(Screen.UserKyc.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    }
                    viewModel.resetState()
                }
            }
            is Resource.Error -> {
                Toast.makeText(context, otpVerifyState?.message ?: "Verification Failed", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter the OTP sent to your mobile number",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = otpValue,
                onValueChange = { if (it.length <= 6) otpValue = it },
                label = { Text("OTP") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (ticks > 0) {
                Text(text = "($ticks sec)", color = Color.Gray)
            } else {
                TextButton(onClick = { 
                    ticks = 30
                    // viewModel.resendOtp(logKey)
                }) {
                    Text(text = "Resend OTP", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (otpValue.length < 4) {
                        Toast.makeText(context, "Please enter valid OTP", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.verifyOtp(logKey, otpValue)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = otpVerifyState !is Resource.Loading
            ) {
                if (otpVerifyState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("VERIFY OTP")
                }
            }
        }
    }
}
