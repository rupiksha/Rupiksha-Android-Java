package com.app.rupiksha.apis;
//
//import static android.content.ContentValues.TAG;
//
//
//import static com.app.rupiksha.apis.ApiInterface.BASE_URL;
//
//import android.app.Activity;
//import android.app.Application;
//import android.content.IntentFilter;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.Looper;
//
//import androidx.appcompat.app.AppCompatDelegate;
//import androidx.lifecycle.Lifecycle;
//import androidx.lifecycle.LifecycleObserver;
//import androidx.lifecycle.OnLifecycleEvent;
//import androidx.lifecycle.ProcessLifecycleOwner;
//
//import com.app.rupiksha.extra.NetworkChangeReceiver;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import java.io.File;
//import java.util.Locale;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Cache;
//import okhttp3.ConnectionPool;
//import okhttp3.OkHttpClient;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//
//public
//class AppController extends Application implements LifecycleObserver
//{
//    private static final String ONESIGNAL_APP_ID = "9a757297-b6be-4320-9c5f-77bb02d92dce";
//    //public static final String TAG = com.doaid.rank.apis.AppController.class.getSimpleName();
//
//    private Activity mCurrentActivity = null;
//    private NetworkChangeReceiver networkChangeReceiver;
//    static Activity mActivity;
//    private static AppController mInstance;
//    private ApiInterface serviceTimeOut30Sec, serviceTimeOut15Sec, service;
//
//    public static void setmActivity(Activity mActivity) {
//       mActivity = mActivity;
//    }
//    public static Activity getmActivity() {
//        return mActivity;
//    }
//
//    static {
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//    }
//
//    public static synchronized AppController getInstance() {
//        return mInstance;
//    }
//
//
//    public static Activity getactivity() {
//        return mActivity;
//    }
//
//    public static void setActivity(Activity activity)
//    {
//        mActivity =activity;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mInstance = this;
//
//        // Enable verbose OneSignal logging to debug issues if needed.
//     /*   OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//
//        // OneSignal Initialization
//        OneSignal.initWithContext(this);
//        OneSignal.setAppId(ONESIGNAL_APP_ID);
//
//        AudienceNetworkAds.initialize(this);*/
//        serviceTimeOut30Sec = createRetrofitService30Sec(ApiInterface.class, BASE_URL);
//        serviceTimeOut15Sec = createRetrofitService15Sec(ApiInterface.class, BASE_URL);
//        service = createRetrofitService(ApiInterface.class, BASE_URL);
//
//        new Handler(Looper.getMainLooper()).post(this::registerReceiverConnectionCheck);
//       // new Handler(Looper.getMainLooper()).post(this:: registerReceiverUserStatusCheck);
//
//        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
//
//    }
//    public <T> T createRetrofitService30Sec(final Class<T> clazz, final String endPoint) {
//
//        Gson gson = new GsonBuilder().create();
//
//        File httpCacheDirectory = new File(getCacheDir(), "cache_file");
//        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .connectionPool(new ConnectionPool(0, 5 * 60 * 1000, TimeUnit.SECONDS))
//                .addInterceptor(new CustomInterceptor(getInstance(), Locale.getDefault().getLanguage(), "", 30))
//                .cache(cache)
//                .build();
//
//
//        //init retrofit
//        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
//                .baseUrl(endPoint)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        return retrofit.create(clazz);
//    }
//
//    public <T> T createRetrofitService15Sec(final Class<T> clazz, final String endPoint) {
//
//        Gson gson = new GsonBuilder().create();
//
//        File httpCacheDirectory = new File(getCacheDir(), "cache_file");
//        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .connectionPool(new ConnectionPool(0, 60 * 1000, TimeUnit.SECONDS))
//                .addInterceptor(new CustomInterceptor(getInstance(), Locale.getDefault().getLanguage(), "", 15))
//                .cache(cache)
//                .build();
//
//        //init retrofit
//        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
//                .baseUrl(endPoint)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        return retrofit.create(clazz);
//    }
//
//    public <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
//
//        Gson gson = new GsonBuilder().create();
//
//        File httpCacheDirectory = new File(getCacheDir(), "cache_file");
//        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectionPool(new ConnectionPool(0, 5 * 60 * 1000, TimeUnit.SECONDS))
//                .addInterceptor(new CustomInterceptor(getInstance(), Locale.getDefault().getLanguage(), "", 120))
//                .cache(cache)
//                .build();
//
//
//        //init retrofit
//        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
//                .baseUrl(endPoint)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        return retrofit.create(clazz);
//    }
//
//    public ApiInterface getApiInterfaceTimeOut30Sec() {
//        return serviceTimeOut30Sec;
//    }
//
//    public ApiInterface getApiInterfaceTimeOut15Sec() {
//        return serviceTimeOut15Sec;
//    }
//
//    public ApiInterface getApiInterface() {
//        return service;
//    }
//
//    private void registerReceiverConnectionCheck() {
//
//        networkChangeReceiver = new NetworkChangeReceiver();
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//
//        HandlerThread handlerThread = new HandlerThread(TAG);
//        handlerThread.start();
//        Looper looper = handlerThread.getLooper();
//        Handler handler = new Handler(looper);
//
//        registerReceiver(networkChangeReceiver, intentFilter, null, handler);
//    }
//
//   /* private void registerReceiverUserStatusCheck(){
//        checkUserStatusReceiver = new CheckUserStatusReceiver();
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("UserAppStatus");
//
//        HandlerThread handlerThread = new HandlerThread(TAG);
//        handlerThread.start();
//        Looper looper = handlerThread.getLooper();
//        Handler handler = new Handler(looper);
//
//        registerReceiver(checkUserStatusReceiver, intentFilter, null, handler);
//    }*/
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    public void appInResumeState() {
//       /* Intent intent = new Intent("UserAppStatus");
//        intent.putExtra("userStatus","1");
//        sendBroadcast(intent);*/
//    }
//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//
//        if(level == TRIM_MEMORY_UI_HIDDEN)
//        {
//          /*  Intent intent = new Intent("UserAppStatus");
//            intent.putExtra("userStatus","0");
//            sendBroadcast(intent);*/
//            /*new Handler(Looper.getMainLooper()).post(() -> {
//                if (checkUserStatusReceiver != null)
//                    unregisterReceiver(checkUserStatusReceiver);
//            });*/
//        }
//    }
//}
