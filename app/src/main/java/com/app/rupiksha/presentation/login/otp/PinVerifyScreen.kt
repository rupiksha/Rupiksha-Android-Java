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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinVerifyScreen(
    navController: NavController,
    logKey: String,
    viewModel: OtpViewModel = hiltViewModel()
) {
    var pinValue by remember { mutableStateOf("") }
    val verifyState by viewModel.otpVerifyState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(verifyState) {
        when (verifyState) {
            is Resource.Success -> {
                val body = verifyState?.data
                if (body != null) {
                    Toast.makeText(context, body.message, Toast.LENGTH_SHORT).show()
                    val kycStatus = body.data?.accountkycstatus?.kyc ?: ""
                    when (kycStatus) {
                        "YES" -> {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        "PENDING" -> {
                            // navController.navigate(Screen.PendingKyc.route)
                        }
                        else -> {
                            // navController.navigate(Screen.KycUser.route)
                        }
                    }
                    viewModel.resetState()
                }
            }
            is Resource.Error -> {
                Toast.makeText(context, verifyState?.message ?: "Verification Failed", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verify PIN") },
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
                text = "Enter your 6-digit PIN to continue",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = pinValue,
                onValueChange = { if (it.length <= 6) pinValue = it },
                label = { Text("PIN") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (pinValue.length < 4) {
                        Toast.makeText(context, "Please enter valid PIN", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.verifyOtp(logKey, pinValue)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = verifyState !is Resource.Loading
            ) {
                if (verifyState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("VERIFY PIN")
                }
            }
        }
    }
}
