package com.app.rupiksha.presentation.itr

import android.app.DatePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
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
import com.app.rupiksha.models.BankDetailModel
import com.app.rupiksha.models.TaxFormDetailModel
import com.app.rupiksha.presentation.login.user_kyc.prepareFilePart
import com.app.rupiksha.utils.FileUtil
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxFormScreen(
    navController: NavController,
    viewModel: ItrViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Address Details
    var houseNumber by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }

    // Identity Details
    var aadharNumber by remember { mutableStateOf("") }
    var panNumber by remember { mutableStateOf("") }
    var panCardName by remember { mutableStateOf("") }
    var panFatherName by remember { mutableStateOf("") }
    var panDob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }

    // Files
    var imgFrontAdharName by remember { mutableStateOf("") }
    var imgBackAdharName by remember { mutableStateOf("") }
    var imgPanName by remember { mutableStateOf("") }
    var imgForm16Name by remember { mutableStateOf("") }
    var imgBankStatementName by remember { mutableStateOf("") }
    var imgMiscName by remember { mutableStateOf("") }

    // Financial Details
    var rentIncome by remember { mutableStateOf("") }
    var fdrInterest by remember { mutableStateOf("") }
    var sbiIncome by remember { mutableStateOf("") }

    // Business Details
    var returnType by remember { mutableStateOf("Business") }
    var businessFirmName by remember { mutableStateOf("") }
    var gstNo by remember { mutableStateOf("") }
    var cashSale by remember { mutableStateOf("") }
    var bankSale by remember { mutableStateOf("") }
    var debtors by remember { mutableStateOf("") }
    var creditors by remember { mutableStateOf("") }
    var cash by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    // ITR Login
    var itrUserName by remember { mutableStateOf("") }
    var itrPassword by remember { mutableStateOf("") }
    var itrMobile by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }
    var itrEmail by remember { mutableStateOf("") }

    // Dynamic Lists
    val investmentList by viewModel.investmentDetailList.collectAsState()
    val otherIncomeList by viewModel.otherIncomeList.collectAsState()
    val otherExpensesList by viewModel.otherExpensesList.collectAsState()
    val bankDetailList by viewModel.bankDetailList.collectAsState()

    val submitState by viewModel.submitState.collectAsState()
    val uploadState by viewModel.uploadState.collectAsState()

    LaunchedEffect(submitState) {
        if (submitState is Resource.Success) {
            Toast.makeText(context, submitState?.data?.msg ?: "Form Submitted", Toast.LENGTH_LONG).show()
            navController.popBackStack()
            viewModel.resetStates()
        } else if (submitState is Resource.Error) {
            Toast.makeText(context, submitState?.message, Toast.LENGTH_SHORT).show()
        }
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val d = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            val m = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
            panDob = "$year-$m-$d"
        },
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tax / ITR Form") },
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
                .verticalScroll(scrollState)
        ) {
            TabRow(selectedTabIndex = if (returnType == "Business") 0 else 1) {
                Tab(selected = returnType == "Business", onClick = { returnType = "Business" }) {
                    Text("Business", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = returnType == "Salary", onClick = { returnType = "Salary" }) {
                    Text("Salary", modifier = Modifier.padding(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("Address Details")
            OutlinedTextField(value = houseNumber, onValueChange = { houseNumber = it }, label = { Text("House/Residence Number") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Road / Street") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = area, onValueChange = { area = it }, label = { Text("Locality / Area") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City / Town") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = pincode, onValueChange = { pincode = it }, label = { Text("Pin Code") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle("Identity Details")
            OutlinedTextField(value = aadharNumber, onValueChange = { if(it.length <= 12) aadharNumber = it }, label = { Text("Aadhar Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            
            ItrImagePicker(label = "Aadhar Front Side", fileName = imgFrontAdharName) { uri ->
                viewModel.uploadImage(prepareFilePart(context, "file_name", uri)) { imgFrontAdharName = it }
            }
            ItrImagePicker(label = "Aadhar Back Side", fileName = imgBackAdharName) { uri ->
                viewModel.uploadImage(prepareFilePart(context, "file_name", uri)) { imgBackAdharName = it }
            }

            OutlinedTextField(value = panNumber, onValueChange = { panNumber = it }, label = { Text("PAN Card Number") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = panCardName, onValueChange = { panCardName = it }, label = { Text("PAN Holder Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = panFatherName, onValueChange = { panFatherName = it }, label = { Text("Father's Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = panDob,
                onValueChange = { },
                label = { Text("Date of Birth") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Text("Gender", modifier = Modifier.padding(top = 16.dp))
            Row {
                listOf("Male", "Female").forEach { g ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.selectable(selected = (gender == g), onClick = { gender = g }).padding(8.dp)) {
                        RadioButton(selected = (gender == g), onClick = { gender = g })
                        Text(text = g, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }

            ItrImagePicker(label = "PAN Card Copy", fileName = imgPanName) { uri ->
                viewModel.uploadImage(prepareFilePart(context, "file_name", uri)) { imgPanName = it }
            }

            if (returnType == "Salary") {
                ItrImagePicker(label = "Form 16", fileName = imgForm16Name) { uri ->
                    viewModel.uploadImage(prepareFilePart(context, "file_name", uri)) { imgForm16Name = it }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle("Income Details")
            OutlinedTextField(value = rentIncome, onValueChange = { rentIncome = it }, label = { Text("Rent Income") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = fdrInterest, onValueChange = { fdrInterest = it }, label = { Text("FDR Interest") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = sbiIncome, onValueChange = { sbiIncome = it }, label = { Text("SB Account Income") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

            if (returnType == "Business") {
                Spacer(modifier = Modifier.height(24.dp))
                SectionTitle("Business Details")
                OutlinedTextField(value = businessFirmName, onValueChange = { businessFirmName = it }, label = { Text("Business Firm Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = gstNo, onValueChange = { gstNo = it }, label = { Text("GST Number") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = cashSale, onValueChange = { cashSale = it }, label = { Text("Cash Sale") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = bankSale, onValueChange = { bankSale = it }, label = { Text("Bank Sale") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = debtors, onValueChange = { debtors = it }, label = { Text("Debtors") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = creditors, onValueChange = { creditors = it }, label = { Text("Creditors") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = cash, onValueChange = { cash = it }, label = { Text("Cash in Hand") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock Value") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SectionTitle("Bank Accounts")
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { viewModel.addBankDetailItem() }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Bank")
                }
            }
            bankDetailList.forEachIndexed { index, bank ->
                BankItemCard(bank = bank, onUpdate = { updatedBank ->
                    val newList = bankDetailList.toMutableList()
                    newList[index] = updatedBank
                    viewModel.bankDetailList.value = newList
                }, onUpload = { uri, onResult ->
                    viewModel.uploadImage(prepareFilePart(context, "file_name", uri), onResult)
                }, onDelete = {
                    val newList = bankDetailList.toMutableList()
                    newList.removeAt(index)
                    viewModel.bankDetailList.value = newList
                })
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SectionTitle("Other Incomes")
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { viewModel.addOtherIncomeItem() }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Other Income")
                }
            }
            otherIncomeList.forEachIndexed { index, item ->
                DynamicItemCard(item = item, onUpdate = { updated ->
                    val newList = otherIncomeList.toMutableList()
                    newList[index] = updated
                    viewModel.otherIncomeList.value = newList
                }, onUpload = { uri, onResult ->
                    viewModel.uploadImage(prepareFilePart(context, "file_name", uri), onResult)
                }, onDelete = {
                    val newList = otherIncomeList.toMutableList()
                    newList.removeAt(index)
                    viewModel.otherIncomeList.value = newList
                })
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SectionTitle("Other Expenses")
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { viewModel.addOtherExpenseItem() }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Other Expense")
                }
            }
            otherExpensesList.forEachIndexed { index, item ->
                DynamicItemCard(item = item, onUpdate = { updated ->
                    val newList = otherExpensesList.toMutableList()
                    newList[index] = updated
                    viewModel.otherExpensesList.value = newList
                }, onUpload = { uri, onResult ->
                    viewModel.uploadImage(prepareFilePart(context, "file_name", uri), onResult)
                }, onDelete = {
                    val newList = otherExpensesList.toMutableList()
                    newList.removeAt(index)
                    viewModel.otherExpensesList.value = newList
                })
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SectionTitle("Investments / Deductions")
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { viewModel.addInvestmentItem() }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Investment")
                }
            }
            investmentList.forEachIndexed { index, item ->
                DynamicItemCard(item = item, onUpdate = { updated ->
                    val newList = investmentList.toMutableList()
                    newList[index] = updated
                    viewModel.investmentDetailList.value = newList
                }, onUpload = { uri, onResult ->
                    viewModel.uploadImage(prepareFilePart(context, "file_name", uri), onResult)
                }, onDelete = {
                    val newList = investmentList.toMutableList()
                    newList.removeAt(index)
                    viewModel.investmentDetailList.value = newList
                })
            }

            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle("ITR Portal Login")
            OutlinedTextField(value = itrUserName, onValueChange = { itrUserName = it }, label = { Text("User Name (PAN)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = itrPassword, onValueChange = { itrPassword = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = itrMobile, onValueChange = { itrMobile = it }, label = { Text("Mobile Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            OutlinedTextField(value = whatsappNumber, onValueChange = { whatsappNumber = it }, label = { Text("WhatsApp Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            OutlinedTextField(value = itrEmail, onValueChange = { itrEmail = it }, label = { Text("Email ID") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val map = mutableMapOf<String, RequestBody>()
                    map["residence_number"] = houseNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["road_or_street"] = street.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["locality_or_area"] = area.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["city_or_town_or_district"] = city.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["pin_code"] = pincode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["aadhar_number"] = aadharNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["pan_card_number"] = panNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["pan_card_name"] = panCardName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["pan_card_father_name"] = panFatherName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["pan_card_dob"] = panDob.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["gender"] = gender.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["front_side_aadhar_file"] = imgFrontAdharName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["back_side_aadhar_file"] = imgBackAdharName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["pan_card_file"] = imgPanName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["itr_form_type"] = returnType.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["rent_income"] = rentIncome.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["fdr_intrest"] = fdrInterest.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["sbi_income"] = sbiIncome.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["itr_user_name"] = itrUserName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["itr_password"] = itrPassword.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["itr_mobile"] = itrMobile.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["whatsapp_number"] = whatsappNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["itr_email"] = itrEmail.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                    if (returnType == "Business") {
                        map["business_firm_name"] = businessFirmName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["gst_number"] = gstNo.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["cash_sale"] = cashSale.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["bank_sale"] = bankSale.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["debtors"] = debtors.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["creditors"] = creditors.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["cash"] = cash.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        map["stock"] = stock.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    }

            val investmentListValue = investmentList
            val otherIncomeListValue = otherIncomeList
            val otherExpensesListValue = otherExpensesList
            val bankDetailListValue = bankDetailList

            val investData = investmentListValue.filter { it.isOtherDocument }.let { Gson().toJson(it) }
            val bankData = bankDetailListValue.filter { it.isOtherDocument }.let { Gson().toJson(it) }
            val otherIncomeData = otherIncomeListValue.filter { it.isOtherDocument }.let { Gson().toJson(it) }
            val otherExpensesData = otherExpensesListValue.filter { it.isOtherDocument }.let { Gson().toJson(it) }

                    map["invest_data"] = investData.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["bank_data"] = bankData.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["other_income_json"] = otherIncomeData.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    map["other_expenses"] = otherExpensesData.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                    viewModel.submitForm(map)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                enabled = submitState !is Resource.Loading
            ) {
                if (submitState is Resource.Loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("SUBMIT ITR FORM", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun BankItemCard(
    bank: BankDetailModel,
    onUpdate: (BankDetailModel) -> Unit,
    onUpload: (Uri, (String) -> Unit) -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Account Details", fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red) }
            }
            OutlinedTextField(value = bank.bankName ?: "", onValueChange = { onUpdate(bank.copy(bankName = it)) }, label = { Text("Bank Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = bank.accountNumber ?: "", onValueChange = { onUpdate(bank.copy(accountNumber = it)) }, label = { Text("Account Number") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = bank.bankIfscCode ?: "", onValueChange = { onUpdate(bank.copy(bankIfscCode = it)) }, label = { Text("IFSC Code") }, modifier = Modifier.fillMaxWidth())
            ItrImagePicker(label = "Passbook/Cheque Image", fileName = bank.imageName ?: "") { uri ->
                onUpload(uri) { fileName -> onUpdate(bank.copy(imageUri = uri, imageName = fileName, isOtherDocument = true)) }
            }
        }
    }
}

@Composable
fun DynamicItemCard(
    item: TaxFormDetailModel,
    onUpdate: (TaxFormDetailModel) -> Unit,
    onUpload: (Uri, (String) -> Unit) -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Item Details", fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red) }
            }
            OutlinedTextField(value = item.title ?: "", onValueChange = { onUpdate(item.copy(title = it)) }, label = { Text("Title/Description") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = item.amount ?: "", onValueChange = { onUpdate(item.copy(amount = it)) }, label = { Text("Amount") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            ItrImagePicker(label = "Attachment", fileName = item.imageName ?: "") { uri ->
                onUpload(uri) { fileName -> onUpdate(item.copy(imageUri = uri, imageName = fileName, isOtherDocument = true)) }
            }
        }
    }
}

@Composable
fun ItrImagePicker(label: String, fileName: String, onImageSelected: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { it?.let { onImageSelected(it) } }
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Row(
            modifier = Modifier.fillMaxWidth().clickable { launcher.launch("*/*") }.background(Color(0xFFEEEEEE), shape = MaterialTheme.shapes.small).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = if (fileName.isEmpty()) "Tap to upload file" else "Uploaded: $fileName", color = if (fileName.isEmpty()) Color.Gray else Color(0xFF4CAF50), maxLines = 1)
        }
    }
}
