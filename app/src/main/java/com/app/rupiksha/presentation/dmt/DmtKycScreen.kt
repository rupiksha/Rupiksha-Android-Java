package com.app.rupiksha.presentation.dmt

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
import com.app.rupiksha.presentation.aeps.DeviceSelectionDialog
import com.app.rupiksha.presentation.aeps.startRdService
import com.app.rupiksha.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DmtKycScreen(
    navController: NavController,
    aadhar: String,
    otpId: String,
    mobile: String,
    viewModel: DmtViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedDevice by remember { mutableStateOf<DeviceModel?>(null) }
    var showDeviceDialog by remember { mutableStateOf(false) }
    
    val deviceList by viewModel.deviceListState.collectAsState()
    val biometricVerifyState by viewModel.biometricVerifyState.collectAsState()

    val rdServiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data
            val pidData = data?.getStringExtra("PID_DATA")
            if (pidData != null) {
                viewModel.biometricVerify(
                    mobile = mobile,
                    aadhar = aadhar,
                    fingerData = pidData,
                    otpId = otpId
                )
            }
        }
    }

    LaunchedEffect(biometricVerifyState) {
        if (biometricVerifyState is Resource.Success) {
            Toast.makeText(context, biometricVerifyState?.data?.message ?: "KYC Successful", Toast.LENGTH_SHORT).show()
            // After KYC success, usually go to login or dashboard
            navController.navigate(Screen.DmtLogin.route) {
                popUpTo(Screen.DmtLogin.route) { inclusive = true }
            }
            viewModel.resetStates()
        } else if (biometricVerifyState is Resource.Error) {
            Toast.makeText(context, biometricVerifyState?.message ?: "Verification Failed", Toast.LENGTH_SHORT).show()
            viewModel.resetStates()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DMT Biometric KYC") },
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
            Text(
                text = "Aadhar: $aadhar",
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
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (selectedDevice != null) {
                        startRdService(context, selectedDevice!!, rdServiceLauncher)
                    } else {
                        Toast.makeText(context, "Please select a device", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                enabled = biometricVerifyState !is Resource.Loading
            ) {
                if (biometricVerifyState is Resource.Loading) {
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
