package com.app.rupiksha.utils;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffColorFilter;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.Parcelable;
//import android.provider.MediaStore;
//import android.provider.OpenableColumns;
//import android.provider.Settings;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.util.Patterns;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.webkit.MimeTypeMap;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.core.content.ContextCompat;
//import androidx.databinding.DataBindingUtil;
//
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.app.rupiksha.R;
//import com.app.rupiksha.activities.LoginActivity;
//import com.app.rupiksha.databinding.ProgresBarAllBinding;
//import com.app.rupiksha.storage.StorageUtil;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.yalantis.ucrop.UCrop;
//
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.Objects;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class Utils {
//    public static Dialog dialog;
//
//
//
//
//    public static void setBottomsheetNonDraggable(Activity context, BottomSheetDialog dialog) {
//        dialog.setCancelable(false);
//        dialog.getBehavior().setDraggable(false);
//    }
//
//
//    public static int getWindowHeight(Activity context) {
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        return displayMetrics.heightPixels;
//    }
//
//    public static int convertDpToPixel(int dp, Context context) {
//        return dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//    }
//
//    public static int convertSpToPixel(int sp, Context context) {
//        return sp * (int) (context.getResources().getDisplayMetrics().scaledDensity / DisplayMetrics.DENSITY_DEFAULT);
//    }
//
//    public static int convertPxToDp(int px, Context context) {
//        return (int) (px / context.getResources().getSystem().getDisplayMetrics().density);
//    }
//
//    public static int spToPx(float sp, Context context) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
//    }
//
//    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
//        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
//        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
//        return mDrawable;
//    }
//
//    public static boolean isValidEmail(CharSequence target) {
//        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
//    }
//
//    public static Uri getPickImageResultUri(Activity activity, Intent data) {
//        boolean isCamera = true;
//        if (data != null) {
//            String action = data.getAction();
//            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
//        }
//        return isCamera ? getCaptureImageOutputUri(activity) : data.getData();
//    }
//
//    public static Uri  getCaptureImageOutputUri(Activity activity)
//    {
//       /* Uri outputFileUri = null;
//        File tempFile = null;
//           //tempFile = File.createTempFile("image", ".jpg", new File(Objects.requireNonNull(Utility.getTempMediaDirectory(activity))));
//            tempFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
//            outputFileUri =  FileProvider.getUriForFile(getApplicationContext(),  BuildConfig.APPLICATION_ID +".fileProvider", tempFile);
//        return outputFileUri;*/
//        Uri outputFileUri = null;
////        File getImage = activity.getExternalCacheDir();
//        File getImage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        if (getImage != null) {
//            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
//        }
//        return outputFileUri;
//    }
//
//    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // CREATE A MATRIX FOR THE MANIPULATION
//        Matrix matrix = new Matrix();
//        // RESIZE THE BIT MAP
//        matrix.postScale(scaleWidth, scaleHeight);
//
//        // "RECREATE" THE NEW BITMAP
//        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
//    }
//
//    public static void hideKeyboard(Context context, View view) {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
//
//    public static Bitmap rotateBitmap(Bitmap image) {
//        int width = image.getHeight();
//        int height = image.getWidth();
//        Bitmap srcBitmap = Bitmap.createBitmap(width, height, image.getConfig());
//        for (int y = width - 1; y >= 0; y--)
//            for (int x = 0; x < height; x++)
//                srcBitmap.setPixel(width - y - 1, x, image.getPixel(x, y));
//        return srcBitmap;
//    }
//
//    public static String getFileName(Context context, Uri uri)
//    {
//        String result = null;
//        if (uri.getScheme().equals("content")) {
//            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
//            try {
//                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                }
//            } finally {
//                cursor.close();
//            }
//        }
//        if (result == null) {
//            result = uri.getPath();
//            int cut = result.lastIndexOf('/');
//            if (cut != -1) {
//                result = result.substring(cut + 1);
//            }
//        }
//        return result;
//    }
//
//
//    public static void showProgressDialog(Context context_dilog, String Title)
//    {
//        if (dialog == null && context_dilog != null)
//        {
//            ProgresBarAllBinding progresBarAllBinding = DataBindingUtil.inflate(LayoutInflater
//                    .from(context_dilog), R.layout.progres_bar_all, null, false);
//            dialog = new Dialog(context_dilog);
//            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.setCancelable(false);
//            dialog.setContentView(progresBarAllBinding.getRoot());
//        }
//
//        try {
//            if (dialog != null) {
//                if (!dialog.isShowing()) {
//                    dialog.show();
//                }
//            }
//        } catch (final Exception e) {
//            // Handle or log or ignore
//        }
//    }
//
//    public static void hideProgressDialog()
//    {
//        try {
//            if (dialog != null) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }
//        } catch (final Exception e) {
//            // Handle or log or ignore
//        } finally {
//            dialog = null;
//        }
//
//    }
//
//
//
//    public static void hideSofthKeyboaard(Context context, View view) {
//
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//
//    public static void showSofthKeyboaard(Context context, View view) {
//        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
//    }
//
//    public static void userLogoutDeleteAccount(Activity context)
//    {
//        new StorageUtil(context).clearAll();
//        Intent intent = new Intent(context, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        context.startActivity(intent);
//        context.finishAffinity();
//    }
//
//    public static String getAndroidId(Context mContext) {
//        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(mContext.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//        return android_id;
//    }
//
//    public static boolean validateEmail(String email) {
//        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
//        return emailPattern.matcher(email).matches();
//    }
//    public static String getMimeType(String url)
//    {
//        String type = null;
//        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
//        if (extension != null) {
//            MimeTypeMap mime = MimeTypeMap.getSingleton();
//            type = mime.getMimeTypeFromExtension(extension);
//        }
//        return type;
//    }
//
//    public static String parseDate(String time) {
//        String inputPattern = "dd-MM-yyyy";
//        String outputPattern = "dd MMM yyyy";
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
//        Date date;
//        String str = null;
//        try {
//            date = inputFormat.parse(time);
//            str = outputFormat.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return str;
//    }
//
//    public static Intent getOpenCameraForImageCapture(Activity activity){
//        Uri outputFileUri = getCaptureImageOutputUri(activity);
//        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        if (captureIntent.resolveActivity(activity.getPackageManager()) != null) {
//            // Create the File where the photo should go
//                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//        }
//      return captureIntent;
//    }
//
//    private static File createImageFile(Activity activity) throws IOException {
//        // Create an image file name
//        String timeStamp_ = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
//                Date());
//        String imageFileName_ = "JPEG_" + timeStamp_ + "_";
//        File storageDir_ = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image_ = File.createTempFile(
//                imageFileName_,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir_      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//         String picturePath = image_.getAbsolutePath();
//        return image_;
//    }
//
//
//
//
//
//    public static void showMessage(Activity context, String message) {
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//    }
//
//    public static Uri getImageUri(Activity activity, Bitmap inImage) {
//        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//
//
//    public static String getDateFormatFromOneToOther(String date, String your_date_format, String result_date_format) {
//        SimpleDateFormat df = new SimpleDateFormat(your_date_format, Locale.getDefault());
//        Date new_date;
//        long t = 0L;
//        SimpleDateFormat f = new SimpleDateFormat(result_date_format, Locale.getDefault());
//        Calendar calendar = Calendar.getInstance();
//        try {
//            new_date = df.parse(date);
//            t = new_date.getTime();
//            calendar.setTimeInMillis(t);
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        String s = f.format(calendar.getTime());
//        Log.d("date:- ", "" + date);
//        Log.d("your_date_format:- ", "" + your_date_format);
//        Log.d("result_date_format:- ", "" + s);
////        s = s.replace("..", ".");
//
//        return s;
//    }
//
//
//    public static Intent getPickImageChooserIntent(Activity activity)
//    {
//        Uri outputFileUri = getCaptureImageOutputUri(activity);
//        List<Intent> allIntents = new ArrayList<>();
//        PackageManager packageManager = activity.getPackageManager();
//        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//
//        for (ResolveInfo res : listCam) {
//            Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            }
//            allIntents.add(intent);
//        }
//
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//        galleryIntent.setType("image/*");
//        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
//
//        for (ResolveInfo res : listGallery)
//        {
//            Intent intent = new Intent(galleryIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            allIntents.add(intent);
//        }
//
//        Intent mainIntent;
//        if (allIntents.size() > 0) {
//            mainIntent = allIntents.get(allIntents.size() - 1);
//        } else {
//            Utils.showMessage(activity, "Impossible de trouver un service pour prendre des photos.");
//            return null;
//        }
//        for (Intent intent : allIntents) {
//            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
//                mainIntent = intent;
//                break;
//            }
//        }
//        allIntents.remove(mainIntent);
//        Intent chooserIntent = Intent.createChooser(mainIntent, "Select an options");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
//        return chooserIntent;
//    }
//
//    public static UCrop.Options getOptions(Context context) {
//        UCrop.Options options = new UCrop.Options();
//        options.setCompressionQuality(80);
//        return options;
//    }
//
//
//    public static  void sharepdf(Activity activity,View view)
//    {
//        Bitmap bitmap = getBitmapFromViewimage(activity,view);
//
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(activity, bitmap));
//        shareIntent.setType("image/jpeg");
//        activity.startActivity(Intent.createChooser(shareIntent,"Send "));
//
//    }
//
//    public static Bitmap getBitmapFromViewimage(Activity activity,View view) {
//        try {
//
//            view.setDrawingCacheEnabled(true);
//
//            view.buildDrawingCache();
//            //Define a bitmap with the same size as the view
//            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
//            //Bind a canvas to it
//            Canvas canvas = new Canvas(returnedBitmap);
//            //Get the view's background
//            Drawable bgDrawable = view.getBackground();
//            if (bgDrawable != null) {
//                //has background drawable, then draw it on the canvas
//                bgDrawable.draw(canvas);
//            } else {
//                //does not have background drawable, then draw white background on the canvas
//                canvas.drawColor(Color.WHITE);
//            }
//            // draw the view on the canvas
//            view.draw(canvas);
//            //return the bitmap
//            return returnedBitmap;
//        }catch (Exception e){
//            Toast.makeText(activity, ""+e.toString(), Toast.LENGTH_SHORT).show();
//        }
//        return null;
//    }
//
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        try {
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
//                    inImage, "", "");
//            return Uri.parse(path);
//        }catch (Exception e){
//            e.getMessage();
//        }
//        return null;
//    }
//
//    public static void setStatusBarColor(Activity activity,int colorId){
//        Window window = activity.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setStatusBarColor(colorId);
//    }
//
//    public static String getTempMediaDirectory(Context context) {
//        String state = Environment.getExternalStorageState();
//        File dir = null;
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            dir = context.getExternalCacheDir();
//        } else {
//            dir = context.getCacheDir();
//        }
//
//        if (dir != null && !dir.exists()) {
//            dir.mkdirs();
//        }
//        if (dir.exists() && dir.isDirectory()) {
//            return dir.getAbsolutePath();
//        }
//        return null;
//    }
//
//    public static void setImageDocument(Context mActivity, ImageView imgDocument, Uri documentUri){
//        String mimeType = Utils.getMimeType(documentUri.toString());
//        if(mimeType!= null &&( mimeType.equalsIgnoreCase("jpg") || mimeType.equalsIgnoreCase("jpeg") || mimeType.equalsIgnoreCase("jfif") || mimeType.equalsIgnoreCase("pjpeg")
//                ||  mimeType.equalsIgnoreCase("pjp")||  mimeType.equalsIgnoreCase("png") ||  mimeType.equalsIgnoreCase("svg") ||  mimeType.equalsIgnoreCase("webp") ||
//                mimeType.equalsIgnoreCase("image/jpg") || mimeType.equalsIgnoreCase("image/jpeg") || mimeType.equalsIgnoreCase("image/jfif") || mimeType.equalsIgnoreCase("image/pjpeg")
//                ||  mimeType.equalsIgnoreCase("image/pjp")||  mimeType.equalsIgnoreCase("image/png") ||  mimeType.equalsIgnoreCase("image/svg") ||  mimeType.equalsIgnoreCase("image/" +
//                "" +
//                "   webp"))){
//
//            Utils.setSingleImageURI(mActivity,imgDocument, documentUri);
//            imgDocument.setVisibility(View.VISIBLE);
//
//        }else{
//            String fileName = Utils.getFileName(mActivity, documentUri);
//            imgDocument.setVisibility(View.GONE);
//        }
//
//
//    }
//
//    public static void setDocuments(Context mActivity, ImageView imgDocument, TextView tvDocument, Uri documentUri){
//        String mimeType = Utils.getMimeType(documentUri.toString());
//        if(mimeType!= null &&( mimeType.equalsIgnoreCase("jpg") || mimeType.equalsIgnoreCase("jpeg") || mimeType.equalsIgnoreCase("jfif") || mimeType.equalsIgnoreCase("pjpeg")
//                ||  mimeType.equalsIgnoreCase("pjp")||  mimeType.equalsIgnoreCase("png") ||  mimeType.equalsIgnoreCase("svg") ||  mimeType.equalsIgnoreCase("webp") ||
//                mimeType.equalsIgnoreCase("image/jpg") || mimeType.equalsIgnoreCase("image/jpeg") || mimeType.equalsIgnoreCase("image/jfif") || mimeType.equalsIgnoreCase("image/pjpeg")
//                ||  mimeType.equalsIgnoreCase("image/pjp")||  mimeType.equalsIgnoreCase("image/png") ||  mimeType.equalsIgnoreCase("image/svg") ||  mimeType.equalsIgnoreCase("image/" +
//                "" +
//                "   webp"))){
//
//            Utils.setSingleImageURI(mActivity,imgDocument, documentUri);
//            tvDocument.setVisibility(View.GONE);
//            imgDocument.setVisibility(View.VISIBLE);
//
//        }else{
//            String fileName = Utils.getFileName(mActivity, documentUri);
//            tvDocument.setText(fileName);
//            tvDocument.setVisibility(View.VISIBLE);
//            imgDocument.setVisibility(View.GONE);
//        }
//    }
//
//    public static void setSingleImageURI(Context mActivity, ImageView view, Uri sourceUri) {
//        if (sourceUri != null) {
//            Glide.with(mActivity)
//                    .load(sourceUri)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.ic_profile_placeholder)
//                    .error(R.drawable.ic_profile_placeholder)
//                    .into(view);
//        }
//    }
//
//    public static boolean panCardValidation(String s){
//        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
//
//        Matcher matcher = pattern.matcher(s);
//        // Check if pattern matches
//        if (matcher.matches())
//            return true;
//
//        return false;
//    }
//
//    public static String getAppVersionName(Context mContext) {
//        try {
//            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
//            return pInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//        public static void animateSlideLeft(Context context) {
//            if (context instanceof Activity) {
//                ((Activity) context).overridePendingTransition(
//                        R.anim.animate_slide_left_enter,
//                        R.anim.animate_slide_left_exit
//                );
//            }
//        }
//
//        public static void animateSlideRight(Context context) {
//            if (context instanceof Activity) {
//                ((Activity) context).overridePendingTransition(
//                        R.anim.animate_slide_in_left,
//                        R.anim.animate_slide_out_right
//                );
//            }
//        }
//
//    public static String getDateFormatExample(String date1) {
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
//        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//
//        String formattedDate = null;
//        try {
//            Date date = inputFormat.parse(date1);
//            formattedDate = outputFormat.format(date);
//            System.out.println(formattedDate);  // Output: 09 Nov 2025
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return formattedDate;
//    }
//
//    public static File getFile(Context context, Uri uri) {
//        if (uri == null) return null;
//        try {
//            InputStream inputStream = context.getContentResolver().openInputStream(uri);
//            if (inputStream == null) return null;
//
//            File file = new File(context.getCacheDir(), getFileName(context, uri));
//            OutputStream outputStream = new FileOutputStream(file);
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//            outputStream.close();
//            inputStream.close();
//            return file;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
