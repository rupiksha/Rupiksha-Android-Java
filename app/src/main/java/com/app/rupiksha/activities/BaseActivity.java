    package com.app.rupiksha.activities;


    import static com.app.rupiksha.constant.AppConstants.REQUEST_CAMERA;
    import static com.app.rupiksha.constant.AppConstants.REQUEST_GALLERY;
    import static com.app.rupiksha.constant.AppConstants.REQUEST_PDF;
    import static com.app.rupiksha.constant.AppConstants.txt_choose_from_camera;
    import static com.app.rupiksha.constant.AppConstants.txt_choose_from_gallery;

    import android.Manifest;
    import android.app.Activity;
    import android.app.AlertDialog;
    import android.app.Dialog;
    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.content.pm.ResolveInfo;
    import android.content.res.Resources;
    import android.graphics.Color;
    import android.graphics.Typeface;
    import android.graphics.drawable.ColorDrawable;
    import android.location.Address;
    import android.location.Geocoder;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.os.Environment;
    import android.provider.MediaStore;
    import android.util.Log;
    import android.util.TypedValue;
    import android.view.KeyEvent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.Button;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.app.NotificationManagerCompat;
    import androidx.core.content.ContextCompat;
    import androidx.core.content.FileProvider;
    import androidx.core.content.res.ResourcesCompat;
    import androidx.core.view.WindowInsetsControllerCompat;
    import androidx.databinding.DataBindingUtil;
    import androidx.databinding.ViewDataBinding;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentManager;
    import androidx.fragment.app.FragmentTransaction;

    import com.app.rupiksha.R;
    import com.app.rupiksha.fragment.BaseFragment;
    import com.app.rupiksha.interfaces.CallBack;
    import com.app.rupiksha.utils.PermissionCaller;
    import com.app.rupiksha.utils.Utils;
    import com.yalantis.ucrop.BuildConfig;
    import com.yalantis.ucrop.UCrop;

    import java.io.File;
    import java.io.IOException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
    import java.util.Locale;
    import java.util.Objects;
    import java.util.Stack;

    public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.fragmentnavigationhelper {
        private static BaseActivity instance;
        private static final String TAG = BaseActivity.class.getSimpleName();
        private BaseFragment mcurrentfragment;
        private int minnumberoffragments = 1;
        private Stack<Fragment> mfragments = new Stack<Fragment>();
        private static final int permission_location_request_code = 91;
        private CallBack callBack;
        private  static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 103;
        public static Uri captureMediaFile = null;
        private String userChooseTask = "";
        private String imageFileName = "";
        private String mCurrentPhotoPath = "";
        private AlertDialog alertDialogselectImage ;
        private Dialog progressDialog  = null;
        private String cropType = "Normal";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //AppController.setActivity(BaseActivity.this);
            instance = BaseActivity.this;
           /* LayoutInflater inflater = getLayoutInflater();
            //View contentview = inflater.inflate(getlayoutid(), null);
            setContentView(contentview);*/
        }

        public static BaseActivity getinstance() {
            return instance;
        }

        @Override
        public void addFragment(BaseFragment f, boolean clearBackStack, boolean addToBackstack) {
            addFragment(f, R.id.frameLayout, clearBackStack, addToBackstack);
        }

        public void addFragment(BaseFragment f, int layoutId, boolean clearBackStack, boolean addToBackstack) {
            if (clearBackStack) {
                clearfragmentbackstack();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

           // transaction.add(R.id.fragment_container, f);
            if (addToBackstack) {
                transaction.addToBackStack(null);
            }
            transaction.commitAllowingStateLoss();

            mcurrentfragment = f;
            mfragments.push(f);

            onfragmentbackstackchanged();
        }

        @Override
        public void replaceFragment(BaseFragment f, boolean clearBackStack, boolean addToBackstack) {
            replaceFragment(f, R.id.frameLayout, clearBackStack, addToBackstack);
        }

        @Override
        public void replaceFragment(BaseFragment f, View view, boolean clearBackStack, boolean addToBackstack) {
            replaceFragment(f, view, R.id.frameLayout, clearBackStack, addToBackstack);
        }

        @Override
        public void startActivity(Intent intent) {
            super.startActivity(intent);
        }

        public void backtolastfragment() {
            getSupportFragmentManager().popBackStack();
            if (mfragments.size() > 0) {
                mfragments.pop();
                mcurrentfragment = (BaseFragment) (mfragments.isEmpty() ? null : ((mfragments.peek()
                        instanceof BaseFragment) ? mfragments.peek() : null));
                onfragmentbackstackchanged();
            }

        }

        public void finishactivity() {
            onBackPressed();
        }

        public BaseFragment getcurrentfragment() {
            return mcurrentfragment;
        }

        @Override
        public boolean onKeyDown(int keycode, KeyEvent event) {
            if (keycode == KeyEvent.KEYCODE_BACK)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    onBack();
                }
                return true;
            }
            return super.onKeyDown(keycode, event);
        }

        @Override
        public void onBack() {

            int a = getSupportFragmentManager().getBackStackEntryCount();
            int b = getMinNumberOfFragments();

            int size = mfragments.size();
            if (getSupportFragmentManager().getBackStackEntryCount() <= getMinNumberOfFragments()) {
                finishactivity();
                return;
            }
            backtolastfragment();
        }

        public void replaceFragment(BaseFragment f, int layoutId, boolean clearBackStack, boolean addToBackstack) {
            if (clearBackStack) {
                clearfragmentbackstack();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // transaction.setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out, R.anim.card_flip_left_in, R.anim.card_flip_left_out);
            transaction.replace(layoutId, f);
            if (addToBackstack) {
                transaction.addToBackStack(null);
            }

            transaction.commitAllowingStateLoss();

            mcurrentfragment = f;
            mfragments.push(f);

            onfragmentbackstackchanged();
        }

        public void onfragmentbackstackchanged() {
            if (mcurrentfragment != null) {
            }
        }

        public int getMinNumberOfFragments() {
            return minnumberoffragments;
        }

        public void clearfragmentbackstack() {
            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount() - getMinNumberOfFragments(); i++) {
                fm.popBackStack();
            }

            if (!mfragments.isEmpty()) {
                Fragment homefragment = mfragments.get(0);
                mcurrentfragment = (BaseFragment) homefragment;
                mfragments.clear();
                mfragments.push(homefragment);
            }
        }

        public void replaceFragment(BaseFragment f, View view, int layoutId, boolean clearBackStack, boolean addToBackstack) {
            if (clearBackStack) {
                clearfragmentbackstack();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //transaction.setCustomAnimations( R.anim.x_slideup, 0, 0, R.anim.x_slidedown);

            transaction.replace(layoutId, f);
            if (addToBackstack) {
                transaction.addToBackStack(null);
            }

            transaction.commitAllowingStateLoss();

            mcurrentfragment = f;
            mfragments.push(f);

            onfragmentbackstackchanged();
        }

      //  protected abstract int getlayoutid();

        public static int getPixelValue(Context context, int dimenId) {
            Resources resources = context.getResources();
    //        Log.e("getPixelValue", "inDP: " + dimenId);
            //        Log.e("getPixelValue", "inPixelValue: " + dp);

            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dimenId,
                    resources.getDisplayMetrics());

        }


        public void openExternalWebView(String url) {
            if (url != null) {
                Intent browserIntent = new  Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        }


        /* public void  dialogNotification(String title,String message, String url) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            if (url != null && !url.isEmpty()) {
                builder.setNegativeButton(getResources().getString(R.string.btn_learn_more), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openExternalWebView(url);
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton(R.string.btn_to_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

            } else {
                builder.setPositiveButton(R.string.btn_to_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
            }
            builder.setTitle(title);
            builder.setMessage(message);
            AlertDialog alert = builder.create();
            alert.show();
        }*/

        /* public void selectImage(Context context,String cropType, CallBack callBack) {

            if (alertDialogselectImage != null && alertDialogselectImage.isShowing()) {
                alertDialogselectImage.dismiss();
            }

            this.callBack = callBack;
            this.cropType = cropType;
            final CharSequence[] options = {getResources().getString(R.string.txt_camera),getResources().getString(R.string.txt_gallery) };
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getResources().getString(R.string.txt_selectoption));

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals(getResources().getString(R.string.txt_camera)))
                    {
                        userChooseTask = txt_choose_from_camera;
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (checkAndRequestPermissions(BaseActivity.this)) {
                                openCamera();
                            }
                        } else {
                            openCamera();
                        }

                    }
                    else if (options[item].equals(getResources().getString(R.string.txt_gallery)))
                    {
                        userChooseTask = txt_choose_from_gallery;

                        if (Build.VERSION.SDK_INT >= 23) { // Call some material design APIs here
                            if(checkAndRequestPermissions(BaseActivity.this)) {
                                openGallery();
                            }
                        } else {
                            openGallery();
                        }
                    }
                }
            });

            alertDialogselectImage = builder.create();
            // show popup
            try {
                alertDialogselectImage.show();
                Typeface typeface = ResourcesCompat.getFont(BaseActivity.this, R.font.dmsans_medium);
                Typeface typeface1 = ResourcesCompat.getFont(BaseActivity.this, R.font.dmsans_medium);

                Button button = alertDialogselectImage.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setTextColor(getResources().getColor(R.color.white));
                button.setAllCaps(false);
                button.setTypeface(typeface);

                Button button1 = alertDialogselectImage.getButton(AlertDialog.BUTTON_NEGATIVE);
                button1.setTextColor(getResources().getColor(R.color.white));
                button1.setTypeface(typeface1);
                button1.setAllCaps(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


        public void selectImage(Context context,String cropType,CallBack callBack) {

            if (alertDialogselectImage != null && alertDialogselectImage.isShowing()) {
                alertDialogselectImage.dismiss();
            }

            this.callBack = callBack;
            this.cropType = cropType;
            final CharSequence[] options = { getResources().getString(R.string.txt_camera),getResources().getString(R.string.txt_gallery) ,getResources().getString(R.string.txt_pdf)};
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder.setTitle(getResources().getString(R.string.txt_selectoption));
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals(getResources().getString(R.string.txt_camera)))
                    {
                        userChooseTask = txt_choose_from_camera;
                        openCamera();
                    }
                    else if (options[item].equals(getResources().getString(R.string.txt_gallery)))
                    {
                        userChooseTask = txt_choose_from_gallery;
                        openGallery();
                    }
                    if (options[item].equals(getResources().getString(R.string.txt_pdf)))
                    {
                        userChooseTask = txt_choose_from_camera;
                        openPdf();
                    }
                }
            });

            alertDialogselectImage= builder.create();
            alertDialogselectImage.show();

            try {
                alertDialogselectImage.show();
                Typeface typeface = ResourcesCompat.getFont(BaseActivity.this, R.font.dmsans_medium);
                Typeface typeface1 = ResourcesCompat.getFont(BaseActivity.this, R.font.dmsans_medium);

                Button button = alertDialogselectImage.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setTextColor(getResources().getColor(R.color.color1F1F1F,null));
                button.setAllCaps(false);
                button.setTypeface(typeface);

                Button button1 = alertDialogselectImage.getButton(AlertDialog.BUTTON_NEGATIVE);
                button1.setTextColor(getResources().getColor(R.color.color1F1F1F,null));
                button1.setTypeface(typeface1);
                button1.setAllCaps(false);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void selectPdf(Context context,String cropType,CallBack callBack) {

            if (alertDialogselectImage != null && alertDialogselectImage.isShowing()) {
                alertDialogselectImage.dismiss();
            }

            this.callBack = callBack;
            this.cropType = cropType;
            final CharSequence[] options = { getResources().getString(R.string.txt_camera),getResources().getString(R.string.txt_gallery) ,getResources().getString(R.string.txt_pdf) };
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder.setTitle(getResources().getString(R.string.txt_selectoption));
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals(getResources().getString(R.string.txt_pdf)))
                    {
                        userChooseTask = txt_choose_from_camera;
                        openPdf();
                    }
                    if (options[item].equals(getResources().getString(R.string.txt_camera)))
                    {
                        userChooseTask = txt_choose_from_camera;
                        openCamera();
                    }
                    else if (options[item].equals(getResources().getString(R.string.txt_gallery)))
                    {
                        userChooseTask = txt_choose_from_gallery;
                        openGallery();
                    }
                }
            });

            alertDialogselectImage= builder.create();
            alertDialogselectImage.show();

            try {
                alertDialogselectImage.show();
                Typeface typeface = ResourcesCompat.getFont(BaseActivity.this, R.font.dmsans_medium);
                Typeface typeface1 = ResourcesCompat.getFont(BaseActivity.this, R.font.dmsans_medium);

                Button button = alertDialogselectImage.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setTextColor(getResources().getColor(R.color.color1F1F1F,null));
                button.setAllCaps(false);
                button.setTypeface(typeface);

                Button button1 = alertDialogselectImage.getButton(AlertDialog.BUTTON_NEGATIVE);
                button1.setTextColor(getResources().getColor(R.color.color1F1F1F,null));
                button1.setTypeface(typeface1);
                button1.setAllCaps(false);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void selectFile(Context context,String cropType,CallBack callBack) {

            if (alertDialogselectImage != null && alertDialogselectImage.isShowing()) {
                alertDialogselectImage.dismiss();
            }

            this.callBack = callBack;
            this.cropType = cropType;
            final CharSequence[] options = {getResources().getString(R.string.txt_file) };
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder.setTitle(getResources().getString(R.string.txt_selectoption));
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals(getResources().getString(R.string.txt_file)))
                    {
                        userChooseTask = txt_choose_from_camera;
                        openPdf();
                    }
                }
            });

            alertDialogselectImage= builder.create();
            alertDialogselectImage.show();

            try {
                alertDialogselectImage.show();
                Typeface typeface = ResourcesCompat.getFont(BaseActivity.this, R.font.dmsans_medium);
                Typeface typeface1 = ResourcesCompat.getFont(BaseActivity.this, R.font.dmsans_medium);

                Button button = alertDialogselectImage.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setTextColor(getResources().getColor(R.color.color1F1F1F,null));
                button.setAllCaps(false);
                button.setTypeface(typeface);

                Button button1 = alertDialogselectImage.getButton(AlertDialog.BUTTON_NEGATIVE);
                button1.setTextColor(getResources().getColor(R.color.color1F1F1F,null));
                button1.setTypeface(typeface1);
                button1.setAllCaps(false);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        public void openCamera() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            {
                if (!PermissionCaller.getInstance(this).isListOfPermission(new String[]{Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO},
                        REQUEST_CAMERA))
                    return;

                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    File tempFile = File.createTempFile("image", ".jpeg", new File(Objects.requireNonNull(Utils.getTempMediaDirectory(this))));
                    captureMediaFile = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
                    Log.e("capturemedia file url", "" + captureMediaFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, captureMediaFile);

                    List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        grantUriPermission(packageName, captureMediaFile, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, REQUEST_CAMERA);
            }else {
                if (!PermissionCaller.getInstance(this).isListOfPermission(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA))
                    return;

                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    File tempFile = File.createTempFile("image", ".jpeg", new File(Objects.requireNonNull(Utils.getTempMediaDirectory(this))));
                    captureMediaFile = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
                    Log.e("capturemedia file url", "" + captureMediaFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, captureMediaFile);

                    List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        grantUriPermission(packageName, captureMediaFile, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        }

        public File createImageFile() {
            String IMAGE_DIRECTORY_NAME = getString(R.string.app_name);
            // Create an image file name
            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                            .format(new Date());
            imageFileName = "Profile" + "_" + timeStamp + "_";

            // External sdcard location
            File mediaStorageDir = new  File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_NAME);


            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }

            File mediaFile =new  File(
                    mediaStorageDir.getPath() + File.separator
                            + "IMG_" + timeStamp + ".jpg");

            mCurrentPhotoPath = mediaFile.getAbsolutePath();
            return mediaFile;
        }

        public void openGallery() {
            Intent takePictureIntent = new Intent(Intent.ACTION_PICK);
            takePictureIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(takePictureIntent, REQUEST_GALLERY);
        }

        public void openPdf() {
           /* Intent pdfIntent = new  Intent(Intent.ACTION_GET_CONTENT);
            pdfIntent.setType( "application/pdf");
            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(pdfIntent, REQUEST_PDF);*/

            Intent intent;
            if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
                intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                intent.putExtra("CONTENT_TYPE", "*/*");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
            } else {

                String[] mimeTypes =
                        {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                "text/plain",
                                "application/pdf", "application/vnd.android.package-archive","application/json"};


                intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            }

            startActivityForResult(intent, REQUEST_PDF);

        }


        public static boolean checkAndRequestPermissions(final Activity context) {
            int ExtstorePermission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            int cameraPermission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (ExtstorePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded
                        .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(context, listPermissionsNeeded
                                .toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
            return true;
        }


        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);{
                switch (requestCode) {
                    case REQUEST_ID_MULTIPLE_PERMISSIONS:
                        if (userChooseTask == txt_choose_from_camera)
                                openCamera();
                        else if (userChooseTask == txt_choose_from_gallery)
                                openGallery();
                            break;
                }

            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (requestCode == UCrop.REQUEST_CROP) {
                Uri resultUri = UCrop.getOutput(data);
                callBack.setPhoto(resultUri);
            }
            if (requestCode == REQUEST_CAMERA) {
                if (resultCode == RESULT_OK) {
                    try {
                        String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                        File new_file = new File(BaseActivity.this.getCacheDir(), destinationFileName);
                        if (Uri.fromFile(new_file) != null) {
                            int x = 0;
                            int y = 0;

                            switch (cropType){
                                case "Normal":
                                    x = 1;
                                    y = 1;
                                    break;
                                case "Change":
                                    x = 16;
                                    y = 9;
                                    break;
                                case "Vertical":
                                    x = 9;
                                    y = 16;
                                    break;
                            }

                            UCrop.of(captureMediaFile, Uri.fromFile(new_file))
                                    .withAspectRatio(x, y)
                                    .start(BaseActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == REQUEST_GALLERY) {
                if (resultCode == RESULT_OK) {
                    try {
                        captureMediaFile = data.getData();
                        String destinationFileName = "profile"+System.currentTimeMillis() + ".png";
                       // File new_file = new File(BaseActivity.this.getExternalFilesDir("Image"), destinationFileName);
                        File new_file = new File(BaseActivity.this.getCacheDir(), destinationFileName);
                        if (Uri.fromFile(new_file) != null) {
                            int x = 0;
                            int y = 0;
                            switch (cropType){
                                case "Normal":
                                    x = 1;
                                    y = 1;
                                    break;
                                case "Change":
                                    x = 16;
                                    y = 9;
                                    break;
                                case "Vertical":
                                    x = 9;
                                    y = 16;
                                    break;
                            }
                            UCrop.of(captureMediaFile, Uri.fromFile(new_file))
                                    .withAspectRatio(x, y)
                                    .start(BaseActivity.this);
                        }
                   /* captureMediaFile = data.getData();
                    callBack.setPhoto(captureMediaFile);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if (requestCode == REQUEST_PDF) {
                if (resultCode == RESULT_OK) {
                    try {
                        captureMediaFile = data.getData();
                        callBack.setPhoto(captureMediaFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

       /* @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CAMERA) {
                if (resultCode == RESULT_OK) {
                    try {
                        callBack.setPhoto(captureMediaFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == REQUEST_GALLERY) {
                if (resultCode == RESULT_OK) {
                    captureMediaFile = data.getData();
                    callBack.setPhoto(captureMediaFile);
                }
            }
        }*/



        public static String getTempMediaDirectory(Context context) {
            String state = Environment.getExternalStorageState();
            File dir = null;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                dir = context.getExternalCacheDir();
            } else {
                dir = context.getCacheDir();
            }

            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }
            if (dir.exists() && dir.isDirectory()) {
                return dir.getAbsolutePath();
            }
            return null;
        }

        public void loadProgressBar(Context mContext) {
            if (progressDialog == null && mContext != null) {
                ViewDataBinding progresBarAllBinding = DataBindingUtil.inflate(LayoutInflater
                         .from(BaseActivity.this), R.layout.progres_bar_all, null, false);
    //          ImageLoadInView.loadProgressGif(progresBarAllBinding.ivProgressLoader);
                progressDialog = new Dialog(mContext);
                //Objects.requireNonNull(progressDialog.getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setCancelable(false);
                progressDialog.setContentView(progresBarAllBinding.getRoot());
            }

            try{
                if (progressDialog != null) {
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                }
            }catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            }
        }

        public void dismissProgressBar() {
            try{
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                this.progressDialog = null;
            }

        }
        /*public void userLogoutDeleteAccount(Context context) {
            new StorageUtil(context).clearAll();

            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finishAffinity();
        }*/

        public void openNotificationSetting(){
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
            } else {
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
            }
            startActivity(intent);
        }

        public boolean areNotificationsEnabled() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (!manager.areNotificationsEnabled()) {
                    return false;
                }
                List<NotificationChannel> channels = manager.getNotificationChannels();
                for (NotificationChannel channel : channels) {
                    if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                        return false;
                    }
                }
                return true;
            } else {
                return NotificationManagerCompat.from(BaseActivity.this).areNotificationsEnabled();
            }
        }

        public void changeStatusBarColor(int color){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
               // window.setNavigationBarColor(getResources().getColor(R.color.dilog_text_color2));
            }
        }

        public void changeNavigationBarColor(int color){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setNavigationBarColor(color);
            }
        }

        public void blockScreenShot(){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        }
        public void blockScreenShotFragment(){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        }

        public void setLightStatusBar(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Window window = getWindow();
                View view = window.getDecorView();
                new WindowInsetsControllerCompat(window, view).setAppearanceLightStatusBars(false);
            }
        }

        public void setDarkStatusBar(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Window window = getWindow();
                View view = window.getDecorView();
                new WindowInsetsControllerCompat(window, view).setAppearanceLightStatusBars(true);
            }
        }

        public String getCity(double mLat,double mLong){
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(BaseActivity.this, Locale.getDefault());
            try {
                String sPlace;
                addresses = geocoder.getFromLocation(mLat, mLong, 1);
                if(addresses.size()>0){
                    String city = addresses.get(0).getAddressLine(0);
                    Log.e("Address",city);
                    return city;
                }else{
                    return "";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "addresses";
        }

        public String getLocationFromAddress(Context context, String strAddress) {

            Geocoder coder = new Geocoder(context);
            List<Address> address;
            String  countryCode = "";
            String city = "";

            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                countryCode =  location.getCountryCode();
                city = location.getLocality();

            } catch (IOException ex) {

                ex.printStackTrace();
            }
            return city;
        }

        public static void openOtherEmail(Context context, String emailId){
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:" + emailId));
                i.putExtra(Intent.EXTRA_SUBJECT, "");
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    context.startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
        }

       /* public static void userLogoutDeleteAccount(Activity context)
        {
            //new StorageUtil(context).clearAll();
            Intent intent = new Intent(context, IntroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            context.finishAffinity();
        }*/



    }



