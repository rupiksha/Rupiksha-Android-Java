package com.app.rupiksha.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.rupiksha.R
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.AepsServiceModel
import com.app.rupiksha.models.BbpsServiceModel
import com.app.rupiksha.models.MoneyTransferServiceModel
import com.app.rupiksha.presentation.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val userInfo by viewModel.userInfo.collectAsState()
    val userProfileState by viewModel.userProfileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getUserProfile()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeDrawerContent(
                userInfo = userInfo, 
                onLogout = { viewModel.logout() },
                onProfileClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Profile.route)
                },
                onChangePasswordClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.ChangePassword.route)
                },
                onChangePinClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.ChangePin.route)
                },
                onBbpsReportsClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.BbpsReports.route)
                },
                onAddMemberClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.AddMember.route)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    profileImageUrl = userInfo?.profile,
                    onProfileClick = { navController.navigate(Screen.Profile.route) }
                )
            },
            bottomBar = { HomeBottomBar(navController) },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate(Screen.QrCode.route) }) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = "QR Scanner")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .background(Color(0xFFF5F5F5))
                ) {
                    // Wallet Balance Card
                    WalletBalanceCard(
                        balance = userProfileState?.data?.walletBalance ?: 0.0,
                        onRefresh = { viewModel.getUserProfile() },
                        onAddMoney = { navController.navigate(Screen.Wallet.route) } 
                    )

                    // Services Sections
                    if (userProfileState is Resource.Success) {
                        val data = userProfileState?.data?.data
                        
                        ServiceSection(title = "AEPS Services", items = data?.aeps ?: emptyList()) { service ->
                            navController.navigate(Screen.Aeps.createRoute(service.name ?: "", service.type ?: ""))
                        }
                        
                        ServiceSection(title = "BBPS Services", items = data?.bbps ?: emptyList()) { service ->
                            // Always go to Categories screen first for BBPS
                            navController.navigate(Screen.BbpsCategories.route)
                        }

                        ServiceSection(title = "Recharge & Utility", items = data?.utility ?: emptyList()) { service ->
                            val type = service.type ?: ""
                            val name = service.name ?: ""
                            when (type.lowercase()) {
                                "dmt" -> navController.navigate(Screen.DmtLogin.route)
                                "payout" -> navController.navigate(Screen.Payout.createRoute(name))
                                "qt" -> navController.navigate(Screen.QuickTransfer.createRoute(name))
                                "mobile", "dth" -> navController.navigate(Screen.Recharge.createRoute(name, type))
                                "cms" -> navController.navigate(Screen.Cms.createRoute(name))
                                "uti" -> navController.navigate(Screen.Uti.route)
                                "pan" -> navController.navigate(Screen.PanVerification.route)
                                "itr", "tax" -> navController.navigate(Screen.TaxForm.route)
                                else -> { /* Handle others */ }
                            }
                        }
                    } else if (userProfileState is Resource.Loading) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onMenuClick: () -> Unit, profileImageUrl: String?, onProfileClick: () -> Unit) {
    TopAppBar(
        title = { Text("Rupiksha") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            AsyncImage(
                model = profileImageUrl,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onProfileClick() },
                placeholder = painterResource(R.drawable.logo)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    )
}

@Composable
fun HomeDrawerContent(
    userInfo: com.app.rupiksha.models.ModelUserInfo?, 
    onLogout: () -> Unit,
    onProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onChangePinClick: () -> Unit,
    onBbpsReportsClick: () -> Unit,
    onAddMemberClick: () -> Unit
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = userInfo?.profile,
                contentDescription = null,
                modifier = Modifier.size(64.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = userInfo?.name ?: "User", fontWeight = FontWeight.Bold)
            Text(text = userInfo?.email ?: "", fontSize = 12.sp)
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            NavigationDrawerItem(
                label = { Text("Profile") },
                selected = false,
                onClick = onProfileClick,
                icon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("Add Member") },
                selected = false,
                onClick = onAddMemberClick,
                icon = { Icon(Icons.Default.GroupAdd, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("BBPS Reports") },
                selected = false,
                onClick = onBbpsReportsClick,
                icon = { Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("Change Password") },
                selected = false,
                onClick = onChangePasswordClick,
                icon = { Icon(Icons.Default.Lock, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("Change PIN") },
                selected = false,
                onClick = onChangePinClick,
                icon = { Icon(Icons.Default.Pin, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("Logout") },
                selected = false,
                onClick = onLogout,
                icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) }
            )
        }
    }
}

@Composable
fun HomeBottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = true,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.History, contentDescription = null) },
            label = { Text("Reports") },
            selected = false,
            onClick = { navController.navigate(Screen.Reports.route) }
        )
        Spacer(Modifier.weight(1f))
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
            label = { Text("Wallet") },
            selected = false,
            onClick = { navController.navigate(Screen.Wallet.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.SupportAgent, contentDescription = null) },
            label = { Text("Support") },
            selected = false,
            onClick = { navController.navigate(Screen.Support.route) }
        )
    }
}

@Composable
fun WalletBalanceCard(balance: Double, onRefresh: () -> Unit, onAddMoney: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Wallet Balance", color = Color.Gray, fontSize = 14.sp)
                Text(text = "₹ $balance", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = onRefresh) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
            Button(onClick = onAddMoney) {
                Text("ADD")
            }
        }
    }
}

@Composable
fun <T> ServiceSection(title: String, items: List<T>, onItemClick: (T) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.heightIn(max = 400.dp),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(items) { item ->
                ServiceItem(item, onItemClick)
            }
        }
    }
}

@Composable
fun <T> ServiceItem(item: T, onClick: (T) -> Unit) {
    val (name, iconUrl) = when (item) {
        is AepsServiceModel -> (item.name ?: "") to (item.icon ?: "")
        is BbpsServiceModel -> (item.name ?: "") to (item.icon ?: "")
        is MoneyTransferServiceModel -> (item.name ?: "") to (item.icon ?: "")
        else -> "" to ""
    }

    Column(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick(item) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, shape = CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = iconUrl,
                contentDescription = name,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = name,
            fontSize = 10.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 2
        )
    }
}
