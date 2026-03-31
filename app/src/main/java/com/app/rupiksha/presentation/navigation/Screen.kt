package com.app.rupiksha.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Intro : Screen("intro_screen")
    object Login : Screen("login_screen")
    object SignIn : Screen("signin_screen")
    object SignUp : Screen("signup_screen")
    object Home : Screen("home_screen")
    object Reports : Screen("reports_screen")
    object Wallet : Screen("wallet_screen")
    object Support : Screen("support_screen")
    object Profile : Screen("profile_screen")
    object UserKyc : Screen("user_kyc_screen")
    object PendingKyc : Screen("pending_kyc_screen/{email}/{phone}") {
        fun createRoute(email: String, phone: String) = "pending_kyc_screen/$email/$phone"
    }

    object AddMember : Screen("add_member_screen")
    object Recharge : Screen("recharge_screen/{title}/{type}") {
        fun createRoute(title: String, type: String) = "recharge_screen/$title/$type"
    }

    object Bbps : Screen("bbps_screen/{title}/{type}") {
        fun createRoute(title: String, type: String) = "bbps_screen/$title/$type"
    }

    object BbpsCategories : Screen("bbps_categories_screen")
    object Aeps : Screen("aeps_screen/{title}/{type}") {
        fun createRoute(title: String, type: String) = "aeps_screen/$title/$type"
    }

    object AepsKyc : Screen("aeps_kyc_screen/{title}/{aepsStatus}") {
        fun createRoute(title: String, aepsStatus: String) = "aeps_kyc_screen/$title/$aepsStatus"
    }

    object AepsTwoFactor : Screen("aeps_two_factor_screen/{title}/{type}") {
        fun createRoute(title: String, type: String) = "aeps_two_factor_screen/$title/$type"
    }

    object DmtLogin : Screen("dmt_login_screen")
    object DmtRegister : Screen("dmt_register_screen/{phone}/{dmtKey}") {
        fun createRoute(phone: String, dmtKey: String) = "dmt_register_screen/$phone/$dmtKey"
    }

    object DmtDashboard : Screen("dmt_dashboard_screen/{phone}/{dmtKey}") {
        fun createRoute(phone: String, dmtKey: String) = "dmt_dashboard_screen/$phone/$dmtKey"
    }

    object DmtAddBeneficiary : Screen("dmt_add_beneficiary_screen")
    object DmtVAadhar : Screen("v_aadhar_screen/{phone}") {
        fun createRoute(phone: String) = "v_aadhar_screen/$phone"
    }

    object DmtOtp : Screen("dmt_otp_screen/{mobile}/{otpId}/{aadhar}") {
        fun createRoute(mobile: String, otpId: String, aadhar: String) =
            "dmt_otp_screen/$mobile/$otpId/$aadhar"
    }

    object DmtKyc : Screen("dmt_kyc_screen/{aadhar}/{otpId}/{mobile}") {
        fun createRoute(aadhar: String, otpId: String, mobile: String) =
            "dmt_kyc_screen/$aadhar/$otpId/$mobile"
    }

    object Payout : Screen("payout_screen/{title}") {
        fun createRoute(title: String) = "payout_screen/$title"
    }

    object QuickTransfer : Screen("quick_transfer_screen/{title}") {
        fun createRoute(title: String) = "quick_transfer_screen/$title"
    }

    object Matm : Screen("matm_screen")
    object Cms : Screen("cms_screen/{title}") {
        fun createRoute(title: String) = "cms_screen/$title"
    }

    object QrCode : Screen("qr_code_screen")

    object Otp : Screen("otp_screen/{logkey}") {
        fun createRoute(logkey: String) = "otp_screen/$logkey"
    }

    object PinVerify : Screen("pin_verify_screen/{logkey}") {
        fun createRoute(logkey: String) = "pin_verify_screen/$logkey"
    }

    object ReportDetails : Screen("report_details_screen/{title}/{type}") {
        fun createRoute(title: String, type: String) = "report_details_screen/$title/$type"
    }

    object ChangePassword : Screen("change_password_screen")
    object ChangePin : Screen("change_pin_screen")
    object CommissionPlan : Screen("commission_plan_screen")
    object Uti : Screen("uti_screen")
}
