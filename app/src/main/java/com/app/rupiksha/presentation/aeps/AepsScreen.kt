package com.app.rupiksha.presentation.aeps

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
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
import com.app.rupiksha.models.BankModel
import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.models.DeviceModel
import com.app.rupiksha.presentation.recharge.ErrorDialog
import com.app.rupiksha.presentation.recharge.ReceiptRow
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AepsScreen(
    navController: NavController,
    title: String,
    type: String,
    viewModel: AepsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var transactionType by remember { mutableStateOf(title) }
    var mobileNumber by remember { mutableStateOf("") }
    var aadharNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedBank by remember { mutableStateOf<BankModel?>(null) }
    var selectedDevice by remember { mutableStateOf<DeviceModel?>(null) }
    
    var showBankDialog by remember { mutableStateOf(false) }
    var showDeviceDialog by remember { mutableStateOf(false) }

    val bankListState by viewModel.bankListState.collectAsState()
    val deviceList by viewModel.deviceListState.collectAsState()
    val transactionState by viewModel.transactionState.collectAsState()

    val rdServiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data
            val pidData = data?.getStringExtra("PID_DATA") ?: data?.getStringExtra("response")
            if (pidData != null) {
                val parsedData = parseAepsXml(pidData)
                if (parsedData.containsKey("errorCode") && parsedData["errorCode"] == "0") {
                    viewModel.performTransaction(
                        type = transactionType,
                        mobile = mobileNumber,
                        aadhar = aadharNumber,
                        bankId = selectedBank?.id ?: 0,
                        amount = amount,
                        pidData = pidData,
                        lat = "0.0",
                        log = "0.0",
                        parsedData = parsedData
                    )
                } else {
                    Toast.makeText(context, "Capture Failed: ${parsedData["errorInfo"] ?: "Unknown Error"}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(transactionType) },
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
            if (title == "Balance Enquiry" || title == "Withdrawal") {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = transactionType == "Balance Enquiry",
                        onClick = { transactionType = "Balance Enquiry" },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) { Text("Balance") }
                    SegmentedButton(
                        selected = transactionType == "Withdrawal",
                        onClick = { transactionType = "Withdrawal" },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) { Text("Withdrawal") }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { if (it.length <= 10) mobileNumber = it },
                label = { Text("Mobile Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = aadharNumber,
                onValueChange = { if (it.length <= 12) aadharNumber = it },
                label = { Text("Aadhar Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = selectedBank?.name ?: "",
                onValueChange = { },
                label = { Text("Select Bank") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showBankDialog = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            if (transactionType == "Withdrawal" || transactionType == "Aadhar Pay") {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = selectedDevice?.name ?: "",
                onValueChange = { },
                label = { Text("Select Device") },
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
                    if (validateAepsInputs(context, mobileNumber, aadharNumber, selectedBank, selectedDevice, transactionType, amount)) {
                        startRdService(context, selectedDevice!!, rdServiceLauncher)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                enabled = transactionState !is Resource.Loading
            ) {
                if (transactionState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("CAPTURE & PROCEED", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showBankDialog) {
        BankSelectionDialog(
            bankListState = bankListState,
            onDismiss = { showBankDialog = false },
            onBankSelected = {
                selectedBank = it
                showBankDialog = false
            }
        )
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

    when (transactionState) {
        is Resource.Success -> {
            AepsReceiptDialog(
                response = transactionState!!.data!!,
                onDismiss = {
                    viewModel.resetTransactionState()
                    navController.popBackStack()
                }
            )
        }
        is Resource.Error -> {
            ErrorDialog(message = transactionState!!.message ?: "Transaction Failed") {
                viewModel.resetTransactionState()
            }
        }
        else -> {}
    }
}

fun validateAepsInputs(
    context: android.content.Context,
    mobile: String,
    aadhar: String,
    bank: BankModel?,
    device: DeviceModel?,
    type: String,
    amount: String
): Boolean {
    if (mobile.length != 10) {
        Toast.makeText(context, "Enter 10 digit mobile number", Toast.LENGTH_SHORT).show()
        return false
    }
    if (aadhar.length != 12) {
        Toast.makeText(context, "Enter 12 digit aadhar number", Toast.LENGTH_SHORT).show()
        return false
    }
    if (bank == null) {
        Toast.makeText(context, "Select Bank", Toast.LENGTH_SHORT).show()
        return false
    }
    if (device == null) {
        Toast.makeText(context, "Select Device", Toast.LENGTH_SHORT).show()
        return false
    }
    if ((type == "Withdrawal" || type == "Aadhar Pay") && amount.isEmpty()) {
        Toast.makeText(context, "Enter Amount", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}

fun startRdService(
    context: android.content.Context,
    device: DeviceModel,
    launcher: androidx.activity.result.ActivityResultLauncher<Intent>
) {
    val intent = Intent()
    val packageName = when (device.id) {
        1 -> "com.mantra.mfs110.rdservice"
        2 -> "com.idemia.l1rdservice"
        3 -> "com.acpl.registersdk_l1"
        4 -> "com.secugen.rdservice"
        5 -> "com.mantra.rdservice"
        10 -> "in.gov.uidai.facerd"
        else -> "com.mantra.rdservice"
    }

    val isInstalled = try {
        context.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: Exception) {
        false
    }

    if (!isInstalled) {
        Toast.makeText(context, "${device.name} RD Service not installed", Toast.LENGTH_LONG).show()
        val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        context.startActivity(marketIntent)
        return
    }

    if (device.id == 10) {
        intent.action = "in.gov.uidai.facerd.CAPTURE" 
        val pidOptions = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PidOptions ver=\"1.0\" env=\"P\"><Opts fCount=\"\" fType=\"0\" iCount=\"1\" iType=\"1\" pCount=\"1\" pType=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"\" otp=\"\" wadh=\"\" posh=\"\" /><Demo>Demographic Attributes</Demo><CustOpts><Param name=\"txnId\" value=\"${System.currentTimeMillis()}\"/></CustOpts></PidOptions>"
        intent.putExtra("request", pidOptions)
    } else {
        intent.action = "in.gov.uidai.rdservice.fp.CAPTURE"
        val pidOptions = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"2\" iCount=\"0\" pCount=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" posh=\"UNKNOWN\" env=\"P\" wadh=\"\"/></PidOptions>"
        intent.putExtra("PID_OPTIONS", pidOptions)
    }
    
    try {
        launcher.launch(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to start RD Service", Toast.LENGTH_SHORT).show()
    }
}

fun parseAepsXml(xml: String): Map<String, String> {
    val map = mutableMapOf<String, String>()
    try {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc: Document = builder.parse(ByteArrayInputStream(xml.toByteArray()))
        val root = doc.documentElement

        val respList: NodeList = root.getElementsByTagName("Resp")
        if (respList.length > 0) {
            val resp = respList.item(0) as Element
            map["errorCode"] = resp.getAttribute("errCode")
            map["errorInfo"] = resp.getAttribute("errInfo")
            map["fCount"] = resp.getAttribute("fCount")
            map["fType"] = resp.getAttribute("fType")
            map["nmPoints"] = resp.getAttribute("nmPoints")
            map["qScore"] = resp.getAttribute("qScore")
        }

        val devInfoList: NodeList = root.getElementsByTagName("DeviceInfo")
        if (devInfoList.length > 0) {
            val devInfo = devInfoList.item(0) as Element
            map["dc"] = devInfo.getAttribute("dc")
            map["dpId"] = devInfo.getAttribute("dpId")
            map["mc"] = devInfo.getAttribute("mc")
            map["mi"] = devInfo.getAttribute("mi")
            map["rdsId"] = devInfo.getAttribute("rdsId")
            map["rdsVer"] = devInfo.getAttribute("rdsVer")
        }

        val paramList: NodeList = root.getElementsByTagName("Param")
        if (paramList.length > 0) {
            val param = paramList.item(0) as Element
            map["dsrno"] = param.getAttribute("value")
        }

        val skeyList: NodeList = root.getElementsByTagName("Skey")
        if (skeyList.length > 0) {
            val skey = skeyList.item(0) as Element
            map["ci"] = skey.getAttribute("ci")
            map["sessionKey"] = skey.textContent
        }

        val hmacList: NodeList = root.getElementsByTagName("Hmac")
        if (hmacList.length > 0) {
            map["hmac"] = hmacList.item(0).textContent
        }

        val dataList: NodeList = root.getElementsByTagName("Data")
        if (dataList.length > 0) {
            val data = dataList.item(0) as Element
            map["pidType"] = data.getAttribute("type")
            map["pidData"] = data.textContent
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return map
}

@Composable
fun BankSelectionDialog(
    bankListState: Resource<List<BankModel>>?,
    onDismiss: () -> Unit,
    onBankSelected: (BankModel) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Bank...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
        },
        text = {
            Box(modifier = Modifier.height(400.dp)) {
                when (bankListState) {
                    is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    is Resource.Success -> {
                        val banks = bankListState.data?.filter { it.name.contains(searchQuery, ignoreCase = true) } ?: emptyList()
                        LazyColumn {
                            items(banks) { bank ->
                                ListItem(
                                    headlineContent = { Text(bank.name) },
                                    modifier = Modifier.clickable { onBankSelected(bank) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                    is Resource.Error -> Text(text = bankListState.message ?: "Error", color = Color.Red)
                    else -> {}
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun DeviceSelectionDialog(
    devices: List<DeviceModel>,
    onDismiss: () -> Unit,
    onDeviceSelected: (DeviceModel) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select RD Device") },
        text = {
            LazyColumn {
                items(devices) { device ->
                    ListItem(
                        headlineContent = { Text(device.name) },
                        modifier = Modifier.clickable { onDeviceSelected(device) }
                    )
                    HorizontalDivider()
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun AepsReceiptDialog(response: BaseResponse, onDismiss: () -> Unit) {
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
                    tint = if (response.status == "SUCCESS") Color.Green else Color.Red,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(response.message ?: "Transaction Finished", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            val data = response.data?.receiptData
            Column(modifier = Modifier.fillMaxWidth()) {
                if (data != null) {
                    ReceiptRow("Status", data.status ?: "N/A")
                    ReceiptRow("Transaction ID", data.txnid ?: "N/A")
                    ReceiptRow("RRN", data.rrn ?: "N/A")
                    ReceiptRow("Bank", data.bank ?: "N/A")
                    ReceiptRow("Aadhar", data.aadhar ?: "N/A")
                    ReceiptRow("Date", data.date ?: "N/A")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    ReceiptRow("Amount", "₹ ${data.amount ?: "0.0"}")
                    if (data.txnamount != null && data.txnamount.isNotEmpty()) {
                        ReceiptRow("Withdrawal Amount", "₹ ${data.txnamount}")
                    }
                } else {
                    Text(response.message ?: "No details available")
                }
            }
        }
    )
}
