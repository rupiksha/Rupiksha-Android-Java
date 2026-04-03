package com.app.rupiksha.presentation.cms

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.presentation.home.HomeViewModel
import com.app.rupiksha.utils.Utils
import com.tapits.ubercms_bc_sdk.LoginScreen
import com.tapits.ubercms_bc_sdk.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CmsScreen(
    navController: NavController,
    title: String,
    homeViewModel: HomeViewModel = hiltViewModel(),
    viewModel: CmsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userInfo by homeViewModel.userInfo.collectAsState()
    val cmsState by viewModel.cmsState.collectAsState()
    
    var mobile by remember { mutableStateOf("") }

    LaunchedEffect(cmsState) {
        cmsState?.let {
            when (it) {
                is Resource.Success -> {
                    val url = it.data?.url
                    if (!url.isNullOrEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, it.data?.message ?: "Success", Toast.LENGTH_SHORT).show()
                    }
                    viewModel.resetCmsState()
                }
                is Resource.Error -> {
                    Toast.makeText(context, it.message ?: "Error", Toast.LENGTH_SHORT).show()
                    viewModel.resetCmsState()
                }
                is Resource.Loading -> {
                    // Show loading if needed
                }
            }
        }
    }

    val cmsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val response = result.data?.getStringExtra(Constants.MESSAGE) ?: "Finished"
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
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
            OutlinedTextField(
                value = mobile,
                onValueChange = { if (it.length <= 10) mobile = it },
                label = { Text("Mobile Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (mobile.length == 10) {
                        if (title.contains("2", ignoreCase = true)) {
                            val intent = Intent(context, LoginScreen::class.java).apply {
                                putExtra(Constants.MERCHANT_ID, userInfo?.outlet ?: "")
                                putExtra(Constants.SECRET_KEY, "adb9af1d9d8c573d8600d73754c248c9343ce074d68af0722af434bc922061fa")
                                putExtra(Constants.TYPE_REF, Constants.BILLERS)
                                putExtra(Constants.AMOUNT, "")
                                putExtra(Constants.REMARKS, "")
                                putExtra(Constants.MOBILE_NUMBER, userInfo?.mobile ?: "")
                                putExtra(Constants.SUPER_MERCHANTID, "1407")
                                putExtra(Constants.IMEI, Utils.getAndroidId(context))
                                putExtra(Constants.LATITUDE, 0.0)
                                putExtra(Constants.LONGITUDE, 0.0)
                                putExtra(Constants.NAME, userInfo?.name ?: "")
                                putExtra(Constants.REFERENCE_ID, "")
                            }
                            cmsLauncher.launch(intent)
                        } else {
                            viewModel.getCmsService(mobile)
                        }
                    } else {
                        Toast.makeText(context, "Enter valid mobile", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = cmsState !is Resource.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                if (cmsState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("SUBMIT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
