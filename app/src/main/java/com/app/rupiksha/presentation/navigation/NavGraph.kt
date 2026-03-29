package com.app.rupiksha.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.rupiksha.presentation.aeps.AepsKycScreen
import com.app.rupiksha.presentation.aeps.AepsScreen
import com.app.rupiksha.presentation.bbps.BbpsCategoriesScreen
import com.app.rupiksha.presentation.bbps.BbpsScreen
import com.app.rupiksha.presentation.dmt.*
import com.app.rupiksha.presentation.home.HomeScreen
import com.app.rupiksha.presentation.intro.IntroScreen
import com.app.rupiksha.presentation.login.LoginScreen
import com.app.rupiksha.presentation.login.otp.OtpScreen
import com.app.rupiksha.presentation.login.otp.PinVerifyScreen
import com.app.rupiksha.presentation.login.signin.SignInScreen
import com.app.rupiksha.presentation.login.signup.SignUpScreen
import com.app.rupiksha.presentation.payout.PayoutScreen
import com.app.rupiksha.presentation.profile.ProfileScreen
import com.app.rupiksha.presentation.recharge.RechargeScreen
import com.app.rupiksha.presentation.reports.ReportDetailsScreen
import com.app.rupiksha.presentation.reports.ReportScreen
import com.app.rupiksha.presentation.splash.SplashScreen
import com.app.rupiksha.presentation.support.SupportScreen
import com.app.rupiksha.presentation.wallet.WalletScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Intro.route) {
            IntroScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.SignIn.route) {
            SignInScreen(navController = navController)
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
        composable(
            route = Screen.Otp.route,
            arguments = listOf(navArgument("logkey") { type = NavType.StringType })
        ) { backStackEntry ->
            val logKey = backStackEntry.arguments?.getString("logkey") ?: ""
            OtpScreen(navController = navController, logKey = logKey)
        }
        composable(
            route = Screen.PinVerify.route,
            arguments = listOf(navArgument("logkey") { type = NavType.StringType })
        ) { backStackEntry ->
            val logKey = backStackEntry.arguments?.getString("logkey") ?: ""
            PinVerifyScreen(navController = navController, logKey = logKey)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Reports.route) {
            ReportScreen(navController = navController)
        }
        composable(route = Screen.Wallet.route) {
            WalletScreen(navController = navController)
        }
        composable(route = Screen.Support.route) {
            SupportScreen(navController = navController)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(
            route = Screen.Recharge.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val type = backStackEntry.arguments?.getString("type") ?: ""
            RechargeScreen(navController = navController, title = title, type = type)
        }
        composable(route = Screen.BbpsCategories.route) {
            BbpsCategoriesScreen(navController = navController)
        }
        composable(
            route = Screen.Bbps.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val type = backStackEntry.arguments?.getString("type") ?: ""
            BbpsScreen(navController = navController, title = title, type = type)
        }
        composable(
            route = Screen.Aeps.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val type = backStackEntry.arguments?.getString("type") ?: ""
            AepsScreen(navController = navController, title = title, type = type)
        }
        composable(
            route = Screen.AepsKyc.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("aepsStatus") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val aepsStatus = backStackEntry.arguments?.getString("aepsStatus") ?: ""
            AepsKycScreen(navController = navController, title = title, aepsStatus = aepsStatus)
        }
        composable(route = Screen.DmtLogin.route) {
            DmtLoginScreen(navController = navController)
        }
        composable(
            route = Screen.DmtRegister.route,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
                navArgument("dmtKey") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val dmtKey = backStackEntry.arguments?.getString("dmtKey") ?: ""
            DmtRegisterScreen(navController = navController, phone = phone, dmtKey = dmtKey)
        }
        composable(
            route = Screen.DmtVAadhar.route,
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            VAadharScreen(navController = navController, phone = phone)
        }
        composable(
            route = Screen.DmtOtp.route,
            arguments = listOf(
                navArgument("mobile") { type = NavType.StringType },
                navArgument("otpId") { type = NavType.StringType },
                navArgument("aadhar") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mobile = backStackEntry.arguments?.getString("mobile") ?: ""
            val otpId = backStackEntry.arguments?.getString("otpId") ?: ""
            val aadhar = backStackEntry.arguments?.getString("aadhar") ?: ""
            DmtOtpScreen(navController = navController, mobile = mobile, otpId = otpId, aadhar = aadhar)
        }
        composable(
            route = Screen.DmtKyc.route,
            arguments = listOf(
                navArgument("aadhar") { type = NavType.StringType },
                navArgument("otpId") { type = NavType.StringType },
                navArgument("mobile") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val aadhar = backStackEntry.arguments?.getString("aadhar") ?: ""
            val otpId = backStackEntry.arguments?.getString("otpId") ?: ""
            val mobile = backStackEntry.arguments?.getString("mobile") ?: ""
            DmtKycScreen(navController = navController, aadhar = aadhar, otpId = otpId, mobile = mobile)
        }
        composable(
            route = Screen.DmtDashboard.route,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
                navArgument("dmtKey") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val dmtKey = backStackEntry.arguments?.getString("dmtKey") ?: ""
            DmtDashboardScreen(navController = navController, phone = phone, dmtKey = dmtKey)
        }
        composable(route = Screen.DmtAddBeneficiary.route) {
            DmtAddBeneficiaryScreen(navController = navController)
        }
        composable(
            route = Screen.Payout.route,
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            PayoutScreen(navController = navController, title = title)
        }
        composable(
            route = Screen.ReportDetails.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val type = backStackEntry.arguments?.getString("type") ?: ""
            ReportDetailsScreen(navController = navController, title = title, type = type)
        }
    }
}
