package com.app.rupiksha.presentation.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.KycModel
import com.app.rupiksha.models.ModelUserInfo
import com.app.rupiksha.utils.FileUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userInfo by viewModel.userInfoState.collectAsState()
    val kycInfo by viewModel.kycInfoState.collectAsState()
    val updateState by viewModel.updateProfileState.collectAsState()
    val context = LocalContext.current

    var isEditMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(userInfo?.name ?: "") }
    var email by remember { mutableStateOf(userInfo?.email ?: "") }
    var mobile by remember { mutableStateOf(userInfo?.mobile ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(updateState) {
        if (updateState is Resource.Success) {
            Toast.makeText(context, updateState?.data?.message ?: "Profile Updated", Toast.LENGTH_SHORT).show()
            isEditMode = false
            viewModel.resetUpdateState()
        } else if (updateState is Resource.Error) {
            Toast.makeText(context, updateState?.message ?: "Update Failed", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!isEditMode) {
                        IconButton(onClick = { 
                            name = userInfo?.name ?: ""
                            email = userInfo?.email ?: ""
                            mobile = userInfo?.mobile ?: ""
                            isEditMode = true 
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Header
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = imageUri ?: userInfo?.profile,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable(enabled = isEditMode) { launcher.launch("image/*") },
                    contentScale = ContentScale.Crop
                )
                if (isEditMode) {
                    Surface(
                        modifier = Modifier.size(36.dp).offset(x = (-4).dp, y = (-4).dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        tonalElevation = 2.dp
                    ) {
                        Icon(
                            Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (!isEditMode) {
                Text(text = userInfo?.name ?: "User Name", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = "Outlet: ${userInfo?.outlet ?: "N/A"}", color = Color.Gray, fontSize = 14.sp)
            } else {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isEditMode) {
                EditProfileForm(
                    email = email,
                    onEmailChange = { email = it },
                    mobile = mobile,
                    onMobileChange = { mobile = it },
                    onSave = {
                        val body = imageUri?.let { uri ->
                            try {
                                val file = FileUtil.from(context, uri)
                                file?.let {
                                    val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                                    MultipartBody.Part.createFormData("profile", it.name, requestFile)
                                }
                            } catch (e: Exception) { null }
                        }
                        viewModel.updateProfile(name, mobile, email, body)
                    },
                    onCancel = { isEditMode = false },
                    isLoading = updateState is Resource.Loading
                )
            } else {
                // Details Section
                ProfileInfoCard(userInfo, kycInfo)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun EditProfileForm(
    email: String,
    onEmailChange: (String) -> Unit,
    mobile: String,
    onMobileChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = mobile,
            onValueChange = onMobileChange,
            label = { Text("Mobile") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                } else {
                    Text("Save Changes")
                }
            }
        }
    }
}

@Composable
fun ProfileInfoCard(userInfo: ModelUserInfo?, kycInfo: KycModel?) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Personal Info", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileItem(icon = Icons.Default.Phone, label = "Mobile", value = userInfo?.mobile ?: "N/A")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                ProfileItem(icon = Icons.Default.Email, label = "Email", value = userInfo?.email ?: "N/A")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                ProfileItem(icon = Icons.Default.LocationOn, label = "Address", value = kycInfo?.address ?: "N/A")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("KYC Details", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileItem(icon = Icons.Default.Badge, label = "Aadhaar Number", value = kycInfo?.adhaar ?: "N/A")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                ProfileItem(icon = Icons.Default.CreditCard, label = "PAN Number", value = kycInfo?.pan ?: "N/A")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                ProfileItem(icon = Icons.Default.Store, label = "Shop Name", value = kycInfo?.shopname ?: "N/A")
            }
        }
    }
}

@Composable
fun ProfileItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}
