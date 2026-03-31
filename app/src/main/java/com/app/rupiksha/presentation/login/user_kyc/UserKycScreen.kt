package com.app.rupiksha.presentation.login.user_kyc

import android.app.DatePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.AddressProof
import com.app.rupiksha.models.StateModel
import com.app.rupiksha.storage.StorageUtil
import com.app.rupiksha.utils.FileUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserKycScreen(
    navController: NavController,
    viewModel: UserKycViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Form states
    var fatherName by remember { mutableStateOf("") }
    var shopName by remember { mutableStateOf("") }
    var shopAddress by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var panNumber by remember { mutableStateOf("") }
    var aadharNumber by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    
    var selectedState by remember { mutableStateOf<StateModel?>(null) }
    var selectedDocType by remember { mutableStateOf<AddressProof?>(null) }

    var panImageUri by remember { mutableStateOf<Uri?>(null) }
    var aadharFrontUri by remember { mutableStateOf<Uri?>(null) }
    var aadharBackUri by remember { mutableStateOf<Uri?>(null) }
    var docImageUri by remember { mutableStateOf<Uri?>(null) }
    var selfieUri by remember { mutableStateOf<Uri?>(null) }

    val statesState by viewModel.statesState.collectAsState()
    val docsState by viewModel.documentsState.collectAsState()
    val kycSubmitState by viewModel.kycSubmitState.collectAsState()

    LaunchedEffect(kycSubmitState) {
        when (kycSubmitState) {
            is Resource.Success -> {
                Toast.makeText(context, kycSubmitState?.data?.message, Toast.LENGTH_LONG).show()
                viewModel.resetSubmitState()
                navController.popBackStack()
            }
            is Resource.Error -> {
                Toast.makeText(context, kycSubmitState?.message ?: "Error", Toast.LENGTH_SHORT).show()
                viewModel.resetSubmitState()
            }
            else -> {}
        }
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val d = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            val m = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
            dob = "$year-$m-$d"
        },
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User KYC") },
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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = fatherName,
                onValueChange = { fatherName = it },
                label = { Text("Father's Name") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dob,
                onValueChange = {},
                label = { Text("Date of Birth") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = shopName,
                onValueChange = { shopName = it },
                label = { Text("Shop Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = shopAddress,
                onValueChange = { shopAddress = it },
                label = { Text("Shop Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Personal Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = pincode,
                onValueChange = { pincode = it },
                label = { Text("Pincode") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = district,
                onValueChange = { district = it },
                label = { Text("District/City") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            StateDropdown(
                states = if (statesState is Resource.Success) statesState.data ?: emptyList() else emptyList(),
                selectedState = selectedState,
                onStateSelected = { selectedState = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = panNumber,
                onValueChange = { panNumber = it },
                label = { Text("PAN Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = aadharNumber,
                onValueChange = { aadharNumber = it },
                label = { Text("Aadhar Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            DocTypeDropdown(
                docs = if (docsState is Resource.Success) docsState.data ?: emptyList() else emptyList(),
                selectedDoc = selectedDocType,
                onDocSelected = { selectedDocType = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Upload Documents", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
            
            Spacer(modifier = Modifier.height(16.dp))

            ImagePickerItem(label = "PAN Card Image", selectedUri = panImageUri) { panImageUri = it }
            ImagePickerItem(label = "Aadhar Front Image", selectedUri = aadharFrontUri) { aadharFrontUri = it }
            ImagePickerItem(label = "Aadhar Back Image", selectedUri = aadharBackUri) { aadharBackUri = it }
            ImagePickerItem(label = "Address Proof Image", selectedUri = docImageUri) { docImageUri = it }
            ImagePickerItem(label = "Selfie with ID", selectedUri = selfieUri) { selfieUri = it }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (fatherName.isEmpty() || dob.isEmpty() || shopName.isEmpty() || panNumber.isEmpty() || 
                        aadharNumber.isEmpty() || selectedState == null || selectedDocType == null ||
                        panImageUri == null || aadharFrontUri == null || aadharBackUri == null || docImageUri == null) {
                        Toast.makeText(context, "Please fill all fields and upload required images", Toast.LENGTH_SHORT).show()
                    } else {
                        val storageUtil = StorageUtil(context)
                        val headers = mapOf(
                            "headerToken" to storageUtil.accessToken,
                            "headerKey" to storageUtil.apiKey
                        )
                        
                        val map = mutableMapOf<String, RequestBody>()
                        map["fatherName"] = fatherName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["aadharNumber"] = aadharNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["panNumber"] = panNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["address"] = address.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["shopAddress"] = shopAddress.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["dob"] = dob.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["shopName"] = shopName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["pinCode"] = pincode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["district"] = district.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["state"] = selectedState!!.id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["address_proof"] = selectedDocType!!.type.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                        val panPart = prepareFilePart(context, "panImage", panImageUri)
                        val aadharFrontPart = prepareFilePart(context, "aadharFront", aadharFrontUri)
                        val aadharBackPart = prepareFilePart(context, "aadharBack", aadharBackUri)
                        val docPart = prepareFilePart(context, "address_image", docImageUri)
                        val selfiePart = prepareFilePart(context, "selfie_with_employee", selfieUri)

                        viewModel.submitKyc(headers, map, panPart, aadharFrontPart, aadharBackPart, docPart, selfiePart)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = kycSubmitState !is Resource.Loading
            ) {
                if (kycSubmitState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("SUBMIT KYC")
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateDropdown(
    states: List<StateModel>,
    selectedState: StateModel?,
    onStateSelected: (StateModel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedState?.name ?: "Select State",
            onValueChange = {},
            readOnly = true,
            label = { Text("State") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            states.forEach { state ->
                DropdownMenuItem(
                    text = { Text(state.name) },
                    onClick = {
                        onStateSelected(state)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocTypeDropdown(
    docs: List<AddressProof>,
    selectedDoc: AddressProof?,
    onDocSelected: (AddressProof) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedDoc?.type ?: "Select Address Proof Type",
            onValueChange = {},
            readOnly = true,
            label = { Text("Address Proof") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            docs.forEach { doc ->
                DropdownMenuItem(
                    text = { Text(doc.type) },
                    onClick = {
                        onDocSelected(doc)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ImagePickerItem(
    label: String,
    selectedUri: Uri?,
    onUriSelected: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onUriSelected(it) }
    }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF0F0F0))
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                    Text(text = "Tap to upload", color = Color.Gray)
                }
            }
        }
    }
}

fun prepareFilePart(context: android.content.Context, partName: String, fileUri: Uri?): MultipartBody.Part? {
    if (fileUri == null) return null
    return try {
        val file = FileUtil.from(context, fileUri) ?: return null
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData(partName, file.name, requestFile)
    } catch (e: Exception) {
        null
    }
}
