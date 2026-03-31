package com.app.rupiksha.presentation.aeps

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.app.rupiksha.models.DeviceModel
import com.app.rupiksha.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AepsTwoFactorScreen(
    navController: NavController,
    title: String,
    type: String,
    viewModel: AepsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedDevice by remember { mutableStateOf<DeviceModel?>(null) }
    var showDeviceDialog by remember { mutableStateOf(false) }
    
    val deviceList by viewModel.deviceListState.collectAsState()
    val twoFactorState by viewModel.twoFactorState.collectAsState()

    LaunchedEffect(twoFactorState) {
        if (twoFactorState is Resource.Success) {
            Toast.makeText(context, twoFactorState?.data?.message ?: "Verification Successful", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Aeps.createRoute(title, type)) {
                popUpTo(Screen.AepsTwoFactor.route) { inclusive = true }
            }
            viewModel.resetTransactionState()
        } else if (twoFactorState is Resource.Error) {
            Toast.makeText(context, twoFactorState?.message ?: "Verification Failed", Toast.LENGTH_SHORT).show()
            viewModel.resetTransactionState()
        }
    }

    val rdServiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data
            val pidData = data?.getStringExtra("PID_DATA") ?: data?.getStringExtra("response")
            if (pidData != null) {
                val parsedData = parseAepsXml(pidData)
                if (parsedData["errorCode"] == "0") {
                    viewModel.verifyTwoFactor(parsedData, "0.0", "0.0")
                } else {
                    Toast.makeText(context, "Capture Failed: ${parsedData["errorInfo"]}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AEPS 2FA") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Two-Factor Authentication Required",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = selectedDevice?.name ?: "",
                onValueChange = { },
                label = { Text("Select RD Device") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDeviceDialog = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (selectedDevice != null) {
                        startRdService(context, selectedDevice!!, rdServiceLauncher)
                    } else {
                        Toast.makeText(context, "Please select device", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                enabled = twoFactorState !is Resource.Loading
            ) {
                if (twoFactorState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("CAPTURE FINGERPRINT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showDeviceDialog) {
        DeviceSelectionDialog(
            devices = deviceList,
            onDismiss = { showDeviceDialog = false },
            onDeviceSelected = {
                selectedDevice = it
                showDeviceDialog = false
            }
        )
    }
}
