package com.app.rupiksha.activities;

import static com.app.rupiksha.constant.AppConstants.KEY_TRANSACTION_SUCCESS;
import static com.app.rupiksha.constant.AppConstants.REQUEST_LOCATION_PERMISSION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.HomePagePresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityHomeBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.fragment.HomeFragment;
import com.app.rupiksha.fragment.ReportFragment;
import com.app.rupiksha.fragment.SupportFragment;
import com.app.rupiksha.fragment.WalletTransationFragment;
import com.app.rupiksha.interfaces.IHomeView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.ModelUserInfo;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.GPSTracker;
import com.app.rupiksha.utils.Utils;
import com.fingpay.microatmsdk.MicroAtmLoginScreen;
import com.fingpay.microatmsdk.utils.Constants;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
//import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import me.toptas.fancyshowcase.listener.OnCompleteListener;
import me.toptas.fancyshowcase.listener.OnViewInflateListener;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener, GPSTracker.GPSTrackerInterface, IHomeView {

    ActivityHomeBinding binding1;
    Activity activity;
    Context context;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private int currentItem = 1;
    private GPSTracker gps;
    private Double stringLatitude = Double.valueOf("0.0");
    private Double stringLongitude = Double.valueOf("0.0");
    private Location mLastLocation = null;
    private ModelUserInfo userInfo = null;
    String city_name;
    FancyShowCaseView mFancyShowCaseView1;
    FancyShowCaseView mFancyShowCaseView2;
    FancyShowCaseView mFancyShowCaseView3;
    FancyShowCaseView mFancyShowCaseView4;
    FancyShowCaseView mFancyShowCaseView5;
    FancyShowCaseView mFancyShowCaseView6;

    boolean showQuit = false;
    boolean tutorialDone = false;
    HomeFragment homeFragment;
    FancyShowCaseQueue fancyShowCaseQueue;
    HomePagePresenter presenter;
    TextView username, email, contact;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_home);
        binding1 = DataBindingUtil.setContentView(this, R.layout.activity_home);
        activity = HomeActivity.this;
        context = HomeActivity.this;
        presenter = new HomePagePresenter();
        presenter.setView(this);
        setSupportActionBar(binding1.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("");
        setUpDrawerNavigation();
        setUpBottomNavigation();

        userProfile();

        homeFragment = new HomeFragment();
        getSupportFragmentManager().findFragmentById(R.id.home);
        loadFragment(homeFragment);


        binding1.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, MyProfileActivity.class);
                startActivity(intent1);
            }
        });

        binding1.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, QRCodeActivity.class);
                startActivity(intent1);
            }
        });

//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
//            try {
//                String token = task.getResult();
//                Log.e("DeviceToken = ", token);
//                new StorageUtil(activity).setDeviceToken(token);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

        Intent intent = getIntent();
        if (intent != null) {
            if (getIntent().hasExtra("notifyType")) {
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        redirectbyNotification(intent, getIntent().getStringExtra("notifyType"));
                    }
                }, 500);
            }
        }

        if (new StorageUtil(activity).getUserInfo() != null)
            userInfo = new StorageUtil(activity).getUserInfo();

        if (checkLocationPermission()) {
            if (gps == null)
                gps = new GPSTracker(activity, this);
            mLastLocation = gps.getLocation();
            if (gps.canGetLocation() && mLastLocation != null) {
                StorageUtil.getSharedPref(HomeActivity.this).edit()
                        .putString(AppConstants.KEY_LAST_LAT, "" + mLastLocation.getLatitude())
                        .putString(AppConstants.KEY_LAST_LNG, "" + mLastLocation.getLongitude())
                        .apply();
                // getUserStatus();
            } else {
                gps.showSettingsAlert();
            }
        }

        if (new StorageUtil(activity).getTutorialShown(activity)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showNewTutorial();
                }
            }, 1000);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("notifyType")) {
            redirectbyNotification(intent, intent.getStringExtra("notifyType"));
        }
    }


    public void redirectbyNotification(Intent intent, String notifiactionType) {

        switch (notifiactionType) {
            case KEY_TRANSACTION_SUCCESS:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveOutFromApp();
        // super.onBackPressed();
    }

    private void moveOutFromApp() {
        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme)
                .setTitle("Are you sure you want to exit")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> finishAffinity())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create();
        alertDialog.show();
    }

    private void userLogout() {
        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getUserLogout(activity, headers, true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void userProfile() {
        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getViewProfile(activity, headers, false);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

//    private void getUserStatus()
//    {
//
//        stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
//        stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);
//
//        if (NetworkAlertUtility.isInternetConnection2(context)) {
//            String accessToken = new StorageUtil(activity).getAccessToken();
//            String apikey = new StorageUtil(activity).getApiKey();
//            //======================================
//            RequestBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("lat", String.valueOf(stringLatitude))
//                    .addFormDataPart("long", String.valueOf(stringLongitude))
//                    .addFormDataPart("device_id",  Utils.getAndroidId(this))
//                    .addFormDataPart("device_token", new StorageUtil(activity).getDeviceToken())
//                    .addFormDataPart("device_type", AppConstants.KEY_DEVICE_TYPE)
//                    .build();
//            //=======================================
//            HashMap<String, String> headers = new HashMap<>();
//            headers.put("headerToken", accessToken);
//            headers.put("headerKey", apikey);
//            presenter.getUserStatus(activity, headers, requestBody,false);
//
//        } else {
//            NetworkAlertUtility.showNetworkFailureAlert(context);
//        }
//
//    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gps = new GPSTracker(activity, this);
                mLastLocation = gps.getLocation();
                if (gps.canGetLocation()) {

                    StorageUtil.getSharedPref(activity).edit()
                            .putString(AppConstants.KEY_LAST_LAT, "" + mLastLocation.getLatitude())
                            .putString(AppConstants.KEY_LAST_LNG, "" + mLastLocation.getLongitude())
                            .apply();

                    Log.e("lat", String.valueOf(mLastLocation.getLatitude()));
                    Log.e("log", String.valueOf(mLastLocation.getLongitude()));

                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                        String stateName = addresses.get(0).getAdminArea();
                        city_name = addresses.get(0).getLocality();
                        Log.e("cityname", city_name);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //  getUserStatus();
                } else {
                    gps.showSettingsAlert();
                }
            }
        }
    }

    private void setUpBottomNavigation() {
        binding1.bottomNavigationView.setOnItemSelectedListener(this);
    }

    private void setUpDrawerNavigation() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding1.drawerLayout, binding1.toolbar, R.string.nav_open, R.string.nav_close);
        binding1.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        binding1.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white, null), PorterDuff.Mode.SRC_ATOP);
        binding1.navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            if (currentItem != 1) {
                currentItem = 1;
                loadFragment(HomeFragment.newInstance());
                setStatusBar(0);
                return true;
            }

        } else if (itemId == R.id.report) {
            if (currentItem != 2) {
                currentItem = 2;
                loadFragment(ReportFragment.newInstance());
                setStatusBar(1);
                return true;
            }

        } else if (itemId == R.id.wallet) {
            if (currentItem != 3) {
                currentItem = 3;
                loadFragment(WalletTransationFragment.newInstance());
                setStatusBar(2);
                return true;
            }

        } else if (itemId == R.id.support) {
            if (currentItem != 4) {
                currentItem = 4;
                loadFragment(SupportFragment.newInstance());
                setStatusBar(3);
                return true;
            }

        } else if (itemId == R.id.profile) {
            binding1.drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(context, MyProfileActivity.class));
            return true;

        } else if (itemId == R.id.change_password) {
            binding1.drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(context, ChangePasswordActivity.class));
            return true;

        } else if (itemId == R.id.tech_support) {
            binding1.drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(context, ChangePinActivity.class));
            return true;

        } else if (itemId == R.id.commissionplan) {
            binding1.drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(context, CommissionPlanActivity.class));
            return true;

        }/* else if (itemId == R.id.updateAepsKyc) {
            binding1.drawerLayout.closeDrawer(GravityCompat.START);
            if (userInfo != null && userInfo.getOutlet() != null && !userInfo.getOutlet().isEmpty()) {
                startActivity(new Intent(context, AepsKycActivity.class));
            } else {
                startActivity(new Intent(context, AepsKycActivity.class)); // Same as above?
            }
            return true;

        }*/ else if (itemId == R.id.r_c) {
            binding1.drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(context, RaisedTicketActivity.class));
            return true;

        } else if (itemId == R.id.privacy_policy) {
            binding1.drawerLayout.closeDrawer(GravityCompat.START);
            return true;

        } else if (itemId == R.id.t_c) {
            binding1.drawerLayout.closeDrawer(GravityCompat.START);
            // startActivity(new Intent(context, MyFavoriteToolsActivity.class));
            return true;

        } else if (itemId == R.id.logout) {
            binding1.drawerLayout.closeDrawer(GravityCompat.START);
            userLogout();
            return true;
        }

        return false;
    }

    private void setStatusBar(int pos) {
        if (pos == 0) {
            binding1.title.setText("");
            binding1.logo.setVisibility(View.GONE);
            binding1.circleImageView.setVisibility(View.VISIBLE);
        } else if (pos == 1) {
            binding1.title.setText("Reports");
            // binding.circleImageView.setVisibility(View.GONE);
            binding1.logo.setVisibility(View.GONE);
        } else if (pos == 2) {
            binding1.title.setText("Wallet Transaction");
            // binding.circleImageView.setVisibility(View.GONE);
            binding1.logo.setVisibility(View.GONE);
        } else if (pos == 3) {
            binding1.title.setText("Support");
            // binding.circleImageView.setVisibility(View.GONE);
            binding1.logo.setVisibility(View.GONE);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.animate_slide_left_enter,  // Enter animation
                        R.anim.animate_slide_left_exit  // Exit animation
                )
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null) // Optional: allows going back
                .commit();
//        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    @Override
    public void onUserLocationChanged(Location location) {
        mLastLocation = location;
        if (gps.canGetLocation()) {
            StorageUtil.getSharedPref(HomeActivity.this).edit()
                    .putString(AppConstants.KEY_LAST_LAT, "" + mLastLocation.getLatitude())
                    .putString(AppConstants.KEY_LAST_LNG, "" + mLastLocation.getLongitude())
                    .apply();

            Log.e("lat", String.valueOf(mLastLocation.getLatitude()));
            Log.e("log", String.valueOf(mLastLocation.getLongitude()));

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                String stateName = addresses.get(0).getAdminArea();
                city_name = addresses.get(0).getLocality();
                Log.e("cityname", city_name);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            gps.showSettingsAlert();
        }
    }

    public void showNewTutorial() {


        mFancyShowCaseView1 = new FancyShowCaseView.Builder(this)
                .focusOn(binding1.fab)
                .title("Focus on View")
                .focusShape(FocusShape.CIRCLE)
                .customView(R.layout.layout_tooltip_first, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(@NonNull View view) {

                        view.findViewById(R.id.tvNext).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fancyShowCaseQueue.getCurrent().hide();
                            }
                        });
                        view.findViewById(R.id.tvQuitTutorial).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showQuit = true;
                                fancyShowCaseQueue.cancel(true);
                                new StorageUtil(activity).setTutorialShown(activity, false);
                            }
                        });
                    }
                })
                .closeOnTouch(false)
                .enableTouchOnFocusedView(true)
                .disableFocusAnimation()
                .build();

        mFancyShowCaseView2 = new FancyShowCaseView.Builder(this)
                .focusOn(binding1.circleImageView)
                .title("Focus on View")
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .customView(R.layout.layout_tooltip_second, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(@NonNull View view) {

                        view.findViewById(R.id.tvNext).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fancyShowCaseQueue.getCurrent().hide();
                            }
                        });
                        view.findViewById(R.id.tvQuitTutorial).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showQuit = true;
                                fancyShowCaseQueue.cancel(true);
                                new StorageUtil(activity).setTutorialShown(activity, false);
                            }
                        });
                    }
                })
                .closeOnTouch(false)
                .enableTouchOnFocusedView(true)
                .disableFocusAnimation()
                .build();

        mFancyShowCaseView3 = new FancyShowCaseView.Builder(this)
                .focusOn(binding1.drawerLayout)
                .title("Focus on View")
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .customView(R.layout.layout_tooltip_third, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(@NonNull View view) {

                        view.findViewById(R.id.tvQuitTutorial).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showQuit = true;
                                fancyShowCaseQueue.cancel(true);
                                new StorageUtil(activity).setTutorialShown(activity, false);
                            }
                        });
                    }
                })
                .closeOnTouch(false)
                .enableTouchOnFocusedView(true)
                .disableFocusAnimation()
                .build();

        mFancyShowCaseView4 = new FancyShowCaseView.Builder(this)
                .focusOn(homeFragment.binding.layoutHeader)
                .title("Focus on View")
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .customView(R.layout.layout_tooltip_four, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(@NonNull View view) {

                        view.findViewById(R.id.tvNext).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fancyShowCaseQueue.getCurrent().hide();
                            }
                        });
                        view.findViewById(R.id.tvQuitTutorial).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showQuit = true;
                                fancyShowCaseQueue.cancel(true);
                                new StorageUtil(activity).setTutorialShown(activity, false);
                            }
                        });
                    }
                })
                .closeOnTouch(false)
                .enableTouchOnFocusedView(true)
                .disableFocusAnimation()
                .build();

        mFancyShowCaseView5 = new FancyShowCaseView.Builder(this)
                .focusOn(homeFragment.binding.materialcard)
                .title("Focus on View")
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .customView(R.layout.layout_tooltip_five, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(@NonNull View view) {

                        view.findViewById(R.id.tvNext).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fancyShowCaseQueue.getCurrent().hide();
                            }
                        });
                        view.findViewById(R.id.tvQuitTutorial).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showQuit = true;
                                fancyShowCaseQueue.cancel(true);
                                new StorageUtil(activity).setTutorialShown(activity, false);
                            }
                        });
                    }
                })
                .closeOnTouch(false)
                .enableTouchOnFocusedView(true)
                .disableFocusAnimation()
                .build();

        mFancyShowCaseView6 = new FancyShowCaseView.Builder(this)
                .focusOn(homeFragment.binding.bbpsmaterialcard)
                .title("Focus on View")
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .customView(R.layout.layout_tooltip_six, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(@NonNull View view) {

                        view.findViewById(R.id.tvNext).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fancyShowCaseQueue.getCurrent().hide();
                            }
                        });
                        view.findViewById(R.id.tvQuitTutorial).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showQuit = true;
                                fancyShowCaseQueue.cancel(true);
                                new StorageUtil(activity).setTutorialShown(activity, false);
                            }
                        });
                    }
                })
                .closeOnTouch(false)
                .enableTouchOnFocusedView(true)
                .disableFocusAnimation()
                .build();

        fancyShowCaseQueue = new FancyShowCaseQueue()
                .add(mFancyShowCaseView1)
                .add(mFancyShowCaseView2)
                .add(mFancyShowCaseView4)
                .add(mFancyShowCaseView5)
                .add(mFancyShowCaseView6)
                .add(mFancyShowCaseView3);


        fancyShowCaseQueue.setCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                tutorialDone = true;
                showQuit = false;
                new StorageUtil(activity).setTutorialShown(activity, false);
            }
        });

        fancyShowCaseQueue.show();

    }

    @Override
    protected void onResume() {
        userProfile();

        //getUserStatus();
        super.onResume();
//        if (gps == null)
//            gps = new GPSTracker(this, this);
//
//        // agar abhi bhi GPS OFF hai to alert fir se dikhao
//        if (!gps.canGetLocation()) {
//            gps.showSettingsAlert();
//        } else {
//            // GPS on hai, location fetch karo
//            mLastLocation = gps.getLocation();
//            if (mLastLocation != null) {
//                StorageUtil.getSharedPref(HomeActivity.this).edit()
//                        .putString(AppConstants.KEY_LAST_LAT, "" + mLastLocation.getLatitude())
//                        .putString(AppConstants.KEY_LAST_LNG, "" + mLastLocation.getLongitude())
//                        .apply();
//            }
//        }
    }

    @Override
    public void onViewProfileSuccess(BaseResponse body) {
        if (body != null) {
            new StorageUtil(context).saveUserInfo(body.getData().getProfile());
            new StorageUtil(context).saveKYCInfo(body.getData().getKyc());
            View headerView = binding1.navigationView.getHeaderView(0);
            userInfo = new StorageUtil(this).getUserInfo();
            username = headerView.findViewById(R.id.textView2);
            email = headerView.findViewById(R.id.textView4);
            contact = headerView.findViewById(R.id.textView3);
            circleImageView = headerView.findViewById(R.id.circleImageView);
            if (!userInfo.getProfile().isEmpty()) {
                Glide.with(activity).load(userInfo.getProfile()).placeholder(R.color.white_60).into(circleImageView);
            }
            if (!userInfo.getProfile().isEmpty()) {
                Glide.with(activity).load(userInfo.getProfile()).placeholder(R.color.white_60).into(binding1.circleImageView);
            }

            username.setText(userInfo.getName()+" ("+userInfo.getUsername()+")");
            email.setText(userInfo.getEmail());
            contact.setText(userInfo.getMobile());

        }
    }

    @Override
    public void onCheckLogoutSuccess(BaseResponse body) {
        if (body != null) {
            new StorageUtil(context).clearAll();
            startActivity(new Intent(context, LoginActivity.class));
            finishAffinity();
        }
    }

    @Override
    public void onCheckStatusSuccess(BaseResponse body) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void enableLoadingBar(boolean enable, String s) {
        if (enable)
            Utils.showProgressDialog(activity, "");
        else
            Utils.hideProgressDialog();
    }

    @Override
    public void onError(String reason) {
        new CustomToastNotification(this, reason);
    }

    @Override
    public void dialogAccountDeactivate(String reason) {

    }

    @Override
    public void onErrorToast(String reason) {
        new CustomToastNotification(this, reason);
    }
}