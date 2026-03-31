package com.app.rupiksha.presentation.matm

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.rupiksha.presentation.home.HomeViewModel
import com.app.rupiksha.storage.StorageUtil
import com.app.rupiksha.utils.Utils
import com.fingpay.microatmsdk.MicroAtmLoginScreen
import com.fingpay.microatmsdk.utils.Constants
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatmScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userInfo by viewModel.userInfo.collectAsState()
    
    var selectedType by remember { mutableStateOf("Cash Withdrawal") }
    var amount by remember { mutableStateOf("") }
    var remarks by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val types = listOf("Cash Withdrawal", "Balance Enquiry")

    val matmLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val status = data.getBooleanExtra("status", false)
                val message = data.getStringExtra("message") ?: "Transaction Finished"
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MATM Service") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Transaction Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    types.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedType = type
                                expanded = false
                                if (type == "Balance Enquiry") amount = "0"
                            }
                        )
                    }
                }
            }

            if (selectedType == "Cash Withdrawal") {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                label = { Text("Remarks") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if ((selectedType == "Cash Withdrawal" && amount.isNotEmpty()) || selectedType == "Balance Enquiry") {
                        if (remarks.isNotEmpty()) {
                            val intent = Intent(context, MicroAtmLoginScreen::class.java).apply {
                                putExtra(Constants.MERCHANT_USERID, "FINGPAY1234")
                                putExtra(Constants.MERCHANT_PASSWORD, "e6e061838856bf47e1de730719fb2609")
                                putExtra(Constants.AMOUNT, if (selectedType == "Balance Enquiry") "0" else amount)
                                putExtra(Constants.REMARKS, remarks)
                                putExtra(Constants.MOBILE_NUMBER, userInfo?.mobile ?: "")
                                putExtra(Constants.AMOUNT_EDITABLE, false)
                                putExtra(Constants.TXN_ID, generateClientRefID())
                                putExtra(Constants.SUPER_MERCHANTID, "1407")
                                putExtra(Constants.IMEI, Utils.getAndroidId(context))
                                putExtra(Constants.LATITUDE, 0.0) 
                                putExtra(Constants.LONGITUDE, 0.0)
                                putExtra(Constants.TYPE, if (selectedType == "Cash Withdrawal") Constants.CASH_WITHDRAWAL else Constants.BALANCE_ENQUIRY)
                                putExtra(Constants.MICROATM_MANUFACTURER, Constants.MoreFun)
                            }
                            matmLauncher.launch(intent)
                        } else {
                            Toast.makeText(context, "Enter remarks", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Enter amount", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("PROCEED", fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun generateClientRefID(): String {
    val prefix = "TXN"
    val sdf = SimpleDateFormat("yyMMddHHmm", Locale.getDefault())
    val timestamp = sdf.format(Date())
    val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val secureRandom = SecureRandom()
    val randomPart = StringBuilder()
    for (i in 0 until 5) {
        randomPart.append(allowedChars[secureRandom.nextInt(allowedChars.length)])
    }
    return prefix + timestamp + randomPart.toString()
}
