package com.app.rupiksha.apis

import android.app.Activity
import android.app.Application
import android.content.IntentFilter
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.app.rupiksha.extra.NetworkChangeReceiver
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class AppController : Application(), DefaultLifecycleObserver {

    private var networkChangeReceiver: NetworkChangeReceiver? = null
    private var serviceTimeOut30Sec: ApiInterface? = null
    private var serviceTimeOut15Sec: ApiInterface? = null
    private var service: ApiInterface? = null

    companion object {
        private var mInstance: AppController? = null
        var mActivity: Activity? = null

        @JvmStatic
        fun getInstance(): AppController? = mInstance

        @JvmStatic
        fun getactivity(): Activity? = mActivity

        @JvmStatic
        fun setActivity(activity: Activity?) {
            mActivity = activity
        }

        @JvmStatic
        fun getmActivity(): Activity? = mActivity

        @JvmStatic
        fun setmActivity(activity: Activity?) {
            mActivity = activity
        }

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        serviceTimeOut30Sec = createRetrofitService30Sec(ApiInterface::class.java, ApiInterface.BASE_URL)
        serviceTimeOut15Sec = createRetrofitService15Sec(ApiInterface::class.java, ApiInterface.BASE_URL)
        service = createRetrofitService(ApiInterface::class.java, ApiInterface.BASE_URL)

        Handler(Looper.getMainLooper()).post { registerReceiverConnectionCheck() }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun <T> createRetrofitService30Sec(clazz: Class<T>, endPoint: String): T {
        val gson = GsonBuilder().create()
        val httpCacheDirectory = File(cacheDir, "cache_file")
        val cache = Cache(httpCacheDirectory, 20 * 1024 * 1024)

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(0, 5 * 60 * 1000, TimeUnit.SECONDS))
            .addInterceptor(CustomInterceptor(getInstance()!!, Locale.getDefault().language, "", 30))
            .cache(cache)
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(endPoint)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(clazz)
    }

    fun <T> createRetrofitService15Sec(clazz: Class<T>, endPoint: String): T {
        val gson = GsonBuilder().create()
        val httpCacheDirectory = File(cacheDir, "cache_file")
        val cache = Cache(httpCacheDirectory, 20 * 1024 * 1024)

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(0, 60 * 1000, TimeUnit.SECONDS))
            .addInterceptor(CustomInterceptor(getInstance()!!, Locale.getDefault().language, "", 15))
            .cache(cache)
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(endPoint)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(clazz)
    }

    fun <T> createRetrofitService(clazz: Class<T>, endPoint: String): T {
        val gson = GsonBuilder().create()
        val httpCacheDirectory = File(cacheDir, "cache_file")
        val cache = Cache(httpCacheDirectory, 20 * 1024 * 1024)

        val okHttpClient = OkHttpClient.Builder()
            .connectionPool(ConnectionPool(0, 5 * 60 * 1000, TimeUnit.SECONDS))
            .addInterceptor(CustomInterceptor(getInstance()!!, Locale.getDefault().language, "", 120))
            .cache(cache)
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(endPoint)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(clazz)
    }

    fun getApiInterfaceTimeOut30Sec(): ApiInterface? = serviceTimeOut30Sec

    fun getApiInterfaceTimeOut15Sec(): ApiInterface? = serviceTimeOut15Sec

    fun getApiInterface(): ApiInterface? = service

    private fun registerReceiverConnectionCheck() {
        networkChangeReceiver = NetworkChangeReceiver()
        val intentFilter = IntentFilter().apply {
            addAction("android.net.conn.CONNECTIVITY_CHANGE")
        }

        val handlerThread = HandlerThread("NetworkCheck")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)

        registerReceiver(networkChangeReceiver, intentFilter, null, handler)
    }

    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
        // Intent intent = new Intent("UserAppStatus");
        // intent.putExtra("userStatus","1");
        // sendBroadcast(intent);
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            // Intent intent = new Intent("UserAppStatus");
            // intent.putExtra("userStatus","0");
            // sendBroadcast(intent);
        }
    }
}
