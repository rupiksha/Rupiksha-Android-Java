package com.app.rupiksha.constant;

public class AppConstants {

    public static final String TAG_COUNTRY_PICKER = "COUNTRY_PICKER";
    public static final String TUTORIAL_SHOWN = "isTutorialShown";
    public static final int REQUEST_LOCATION = 1011;
    public static final String SHARED_PREF_NAME = "share_pref_globtr";
    public static final String SHARED_PREF_API_KEY = "globtr_device_token";
    public static final String SHARED_PREF_DEVICE_TOKEN = "ecuzen_device_token";
    public static final String SHARED_PREF_ACCESS_TOKEN = "globtr_ccess_token";
    public static final String SHARED_PREF_USER_INFO = "user_info";
    public static final String SHARED_PREF_KYC_INFO = "kyc_info";
    public static final String SHARED_PREF_DMT_INFO = "dmt_info";

    public static final String SHARED_PREF_SETTING_DATA = "setting_data";
    public static String SHARED_PREF_EndTime = "google_subscription_endtime";
    public static final String SHARED_PREF_TOKENTYPE = "token_type";
    public static final String SHARED_PREF_DMT_KEY = "dmtkey";

    public static final String SHARED_PREF_TOOLTIP2 = "tooltip";
    public static final String DEVICE_TYPE = "Android";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String GRANT_TYPE_PASS = "password";
    public static final String GRANT_TYPE_REFRESH_TOKEN = "refreshtoken";
    public static final String CLIENT_ID = "6";
    public static final String CLIENT_SECRET = "oawjChexncnEdwyMR6YHawUiLcWhzFfRGMPuoapTimy";
    public static final String KEY_LOGIN_FB = "Facebook";
    public static final String KEY_ROLE_ID = "1";
    public static final String KEY_PHONE_TYPE = "devicetype";
    public static final String KEY_LAST_LAT = "last_lat";
    public static final String txt_choose_from_camera = "Camera";
    public static final String txt_choose_from_gallery = "Gallery";
    public static final int REQUEST_CAMERA = 312;
    public static final int REQUEST_GALLERY = 325;
    public static final int REQUEST_PDF = 315;
    public static final String FILE_PATH_DIRECTORY = "/SamyTraveler";

    public static final String KEY_DEVICE_USERID = "onesignal_userid";
    public static final String KEY_DEVICE_PUSHTOKEN = "onesignal_pushtoken";

    public static final String KEY_STRIPE_PAYMENT = "pk_test_wslWFUqQcpw0b51uN77bjovu00sfRoCvE9";

    public static final String sharedPrefName = "SamyTraveler";
    public static final String KEY_DEVICE_TYPE = "Android";

    public static final String SHARED_PREF_UPDATE_TIME = "update_time";

    public static final String KEY_FB_ACCESS_TOKEN = "fb_access_token";
    public static final String KEY_FB_ACCESS_EXPIRES = "fb_access_expires";

    public static final String KEY_ORDER_CART = "order_cart";
    public static final String KEY_ENTRY_CART = "entry_cart";
    public static final String KEY_TOTAL_CREDIT = "total_credit";

    public static final String KEY_LAST_LNG = "last_lng";
    public static final int REQUEST_LOCATION_PERMISSION = 1004;

    public static final String KEY_NOTIFY_ADMIN = "OrderChangeStatus";
    public static final String KEY_NOTIFY_SUPER_ADMIN = "adminNotify";
    public static final String KEY_NOTIFY_ADMIN_CANCEL_ORDER = "adminCancelOrder";
    public static final String KEY_NOTIFY_REMIND_CLUB_TICKET = "RememberClubTickets";
    public static final String KEY_NOTIFY_ADMIN_CANCEL_TICKET = "EntryChangeStatus";

    public static final String KEY_NOTIFY_EMAIL_VERIFYED = "email_verified";
    public static final String KEY_NOTIFY_CHAT = "notify_type_chat_one_to_one";
    public static final String KEY_NOTIFY_REQUEST_ACCEPTED = "acceptBookings";
    public static final String KEY_NOTIFY_REQUEST_DECLINED = "declineBookings";
    public static final String KEY_NOTIFY_GIVE_RATING = "ratingBookings";
    public static final String KEY_NOTIFY_BOOKING_CANCELLED = "cancelledBookings";
    public static final String KEY_NOTIFY_BEFORE_TRIP = "beforeBookings";

    public static final long LOCATION_GET_INTERVAL_NORMAL = 2 * 60 * 1000;  //5 * 60 * 1000
    public static final long LOCATION_GET_INTERVAL_FAST = 2 * 60 * 1000;  //5 * 60 * 1000

    public static final String NOTIFY_CONNECTION_CHANGE = "connectionChange";

    public static final int LOCATION_PERMISSION_REQUEST = 8;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9;
    public static final int REQUEST_LOCATION_CHECK_SETTINGS = 10;
    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 11;
    public static final int FILTER_ACTIVITY_REQUEST_CODE = 1000;
    public static final int TRANSPORT_FILTER_ACTIVITY_REQUEST_CODE = 1001;
    public static final int LANGUAGE_FILTER_ACTIVITY_REQUEST_CODE = 1002;
    public static final int ACTIVITY_FILTER_ACTIVITY_REQUEST_CODE = 1003;
    public static final int CITY_PICKER_REQUEST_CODE = 1004;
    public static final int ADDRESS_PICKER_REQUEST_CODE = 1005;
    public static final int MY_SCAN_REQUEST_CODE = 1006;

    public static final String KEY_TRANSACTION_SUCCESS = "transaction_success";
    public static final String KEY_NOTIFY_TYPE_MOBILE = "notify_type_mobile";
    public static final String KEY_NOTIFY_TYPE_EMI = "notify_type_emi";

    //matm
    public static final String MERCHANT_USERID = "merchant_userid";
    public static final String MERCHANT_PASSWORD = "merchant_password";
    public static final String AMOUNT = "amount";
    public static final String REMARKS = "remarks";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String AMOUNT_EDITABLE = "amount_editable";
    public static final String TXN_ID = "txn_id";
    public static final String SUPER_MERCHANTID = "super_merchant_id";
    public static final String IMEI = "imei";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String TYPE = "type";

    // Types
    public static final String CASH_WITHDRAWAL = "CASH_WITHDRAWAL";
    public static final String CASH_DEPOSIT = "CASH_DEPOSIT";
    public static final String PURCHASE = "PURCHASE";
    public static final String CARD_ACTIVATION = "CARD_ACTIVATION";
    public static final String CHANGE_PIN = "CHANGE_PIN";
    public static final String PIN_RESET = "PIN_RESET";
    public static final String MINI_STATEMENT = "MINI_STATEMENT";
    public static final String BALANCE_ENQUIRY = "BALANCE_ENQUIRY";
    public static final String MICROATM_MANUFACTURER = "MICROATM_MANUFACTURER";

    public static class BroadcastNotifyAction {

        public static final String BROADCAST_NOTIFICATION = "com.agency55.todmarket.NOTIFICATION";

        public static final String BROADCAST_CONNECTION_CHANGE = "com.agency55.todmarket.CONNECTION_CHANGE";

    }
}
