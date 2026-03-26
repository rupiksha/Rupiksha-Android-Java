package com.app.rupiksha.activities;

import static com.app.rupiksha.constant.AppConstants.REQUEST_LOCATION_PERMISSION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.BankAdapter;
import com.app.rupiksha.adapters.DeviceAdapter;
import com.app.rupiksha.apipresenter.AEPSVerifyOtpPresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityAepsKycVerifyBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IAEPSVerifyOtpView;
import com.app.rupiksha.models.BankModel;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.DeviceModel;
import com.app.rupiksha.models.Opts;
import com.app.rupiksha.models.PidData;
import com.app.rupiksha.models.PidOptions;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.GPSTracker;
import com.app.rupiksha.utils.Utils;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AepsKycVerifyActivity extends AppCompatActivity implements IAEPSVerifyOtpView, View.OnClickListener, GPSTracker.GPSTrackerInterface {

    ActivityAepsKycVerifyBinding binding;
    Activity activity;
    Context context;
    String title="",mobilenumber,adharnumber,otpvalue;
    boolean type=false;
    AEPSVerifyOtpPresenter presenter;
    private List<DeviceModel> devicelist=new ArrayList<>();
    private List<BankModel> banklist=new ArrayList<>();
    private GPSTracker gps;
    private String stringLatitude = "0.0";
    private String stringLongitude = "0.0";
    private Location mLastLocation = null;

    DeviceAdapter roleAdapter;
    BankAdapter bankAdapter;
    private ArrayList<String> positions=new ArrayList<>();
    int selectdevice = 0;
    int bankid=0;
    boolean isAppInstalled = false;
    String submit_with = "";
    String result_response = "";
    EditText ed_xml_rsp;
    private PidData pidData = null;
    private Serializer serializer = new Persister();
    String dc, dpId, mc, mi, rdsId, rdsVer, name, value, Hmac, qScore, datatype, data_data, errInfo="Success", ci, amount, Skey;
    String otp = "", errCode, fCount, fType, nmPoints;
    String transaction = "";
    private int fingerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_aeps_services);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_aeps_kyc_verify);
        activity= AepsKycVerifyActivity.this;
        context= AepsKycVerifyActivity.this;
        presenter= new AEPSVerifyOtpPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.title_aeps_service));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");

        if (getIntent().hasExtra("type"))
            type = getIntent().getBooleanExtra("type",false);

        if (checkLocationPermission()) {
            if (gps == null)
                gps = new GPSTracker(activity, this);
            mLastLocation = gps.getLocation();
            if (gps.canGetLocation() && mLastLocation != null) {
                StorageUtil.getSharedPref(this).edit()
                        .putString(AppConstants.KEY_LAST_LAT, "" + mLastLocation.getLatitude())
                        .putString(AppConstants.KEY_LAST_LNG, "" + mLastLocation.getLongitude())
                        .apply();
                // getUserStatus();
            } else {
                gps.showSettingsAlert();
            }
        }
        getDeviceList();

        setUpdevice();

        getVerfyOtp();

        binding.btnsubmit.setOnClickListener(this);
        binding.btnVerifyOTP.setOnClickListener(this);


    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                checkValidation2();
//
//                break;
//            case R.id.btnVerifyOTP:
//                checkValidation();
//
//                break;
//        }
        if (id == R.id.btnsubmit){
            checkValidation2();
        } else if (id == R.id.btnVerifyOTP) {
            checkValidation();
        }

    }

    private void checkValidation()
    {
        otpvalue = Objects.requireNonNull(binding.etotp.getText()).toString();
      if(TextUtils.isEmpty(otpvalue)){
            binding.etotp.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_otp));
        }else{

          doVerfyOtp();
        }
    }

    private void checkValidation2()
    {
        if (selectdevice==0) {
            binding.etdevice.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_device));
        }else{
            doDeviceTask();
        }
    }

    private void doVerfyOtp()
    {
        stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
        stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);

        if (NetworkAlertUtility.isInternetConnection2(context)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("otp", String.valueOf(otpvalue))
                    .addFormDataPart("lat", String.valueOf(stringLatitude))
                    .addFormDataPart("log", String.valueOf(stringLongitude))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getAepsOtpVerify(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }
    }

    private void doDeviceTask() {
        if (selectdevice!=0)
        {
            try {
                try {
                    String pidOption = getPIDOptions();

                    if (pidOption != null) {
                        Log.e("PidOptions", pidOption);
                        Intent intent2 = new Intent();
                        intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                        intent2.putExtra("PID_OPTIONS", pidOption);
                        startActivityForResult(intent2, 2);
                    }
                } catch (Exception e) {
                    Log.e("Eror", e.toString());
                }
            } catch (Exception e) {
                Log.e("Eor", e.toString());
            }
        } else {

            String pidOptString = "<PidOptions><Opts fCount=\"1\" fType=\"2\" iCount=\"0\" pCount=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"20000\" otp=\"\" posh=\"LEFT_INDEX\" env=\"P\" wadh=\"\" /> <Demo></Demo> <CustOpts> <Param name=\"Param1\" value=\"\" /> </CustOpts> </PidOptions>";
            Intent intent2 = new Intent();
            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
            intent2.putExtra("PID_OPTIONS", pidOptString);
            startActivityForResult(intent2, 101);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                    String pidData = b.getString("PID_DATA"); // in this varaible you will get Pid data
                    result_response = pidData;
                    //  Toast.makeText(ICICIAEPSActivity.this,pidData, Toast.LENGTH_LONG).show();
                    String dnc = b.getString("DNC", ""); // you will get value in this variable when your finger
                    // textView2.setText(dnc);
                    String dnr = b.getString("DNR", ""); // you will get value in this variable when
                    // textView2.setText(dnr);
                    //bal_enquiry();
                    setText("pid data morpho = " + pidData);

                    if (submit_with.equalsIgnoreCase("xml_data"))
                    {
                        parseXML(ed_xml_rsp.getText().toString());
                    } else {
                        parseXML(pidData);
                    }
                }
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                try {
                    if (data != null)
                    {
                        Log.e("Biomerticdata", String.valueOf(data));
                        String result = data.getStringExtra("PID_DATA");
                        if (result != null) {
                            pidData = serializer.read(PidData.class, result);
                            setText(result);


                            result_response = result;

                            parseXML(result);

                            //updateType();
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", "Error while deserialze pid data", e);
                }
            }


        }
    }

    private void parseXML(String data) {
        try {

            try {
                FileOutputStream fileout = openFileOutput("responsemantra.xml", MODE_PRIVATE);
                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                outputWriter.write(data);
                outputWriter.close();
                Log.e("dssdfgs",""+data);
                //display file saved message


            } catch (Exception e) {
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


            /*read*/

            // FileInputStream fis = null;
            // InputStreamReader isr = null;

            FileInputStream fileIn = openFileInput("responsemantra.xml");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[fileIn.available()];
            InputRead.read(inputBuffer);

            data = new String(inputBuffer);

            InputRead.close();
            fileIn.close();

            /*
             * Converting the String data to XML format so
             * that the DOM parser understands it as an XML input.
             */

            InputStream is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

            DocumentBuilderFactory dbf;
            DocumentBuilder db;
            NodeList items = null;
            Document doc;

            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(is);


            Element element = doc.getDocumentElement();
            String xpathExpression = "/PidData/DeviceInfo";
            boolean b = checkIfNodeExists(doc, xpathExpression);


            if (b == true) {
                NodeList nl = element.getElementsByTagName("DeviceInfo");

                for (int i = 0; i < nl.getLength(); i++) {
                    Node currentItem = nl.item(i);
                    dc = currentItem.getAttributes().getNamedItem("dc").getNodeValue();
                    dpId = currentItem.getAttributes().getNamedItem("dpId").getNodeValue();
                    mc = currentItem.getAttributes().getNamedItem("mc").getNodeValue();
                    mi = currentItem.getAttributes().getNamedItem("mi").getNodeValue();
                    rdsId = currentItem.getAttributes().getNamedItem("rdsId").getNodeValue();
                    rdsVer = currentItem.getAttributes().getNamedItem("rdsVer").getNodeValue();

                }

                NodeList n2 = element.getElementsByTagName("Param");
                for (int i = 0; i < 1; i++) {
                    Node currentItem = n2.item(i);
                    name = currentItem.getAttributes().getNamedItem("name").getNodeValue();
                    value = currentItem.getAttributes().getNamedItem("value").getNodeValue();

                }


                NodeList currentItem = doc.getElementsByTagName("Hmac");
                Element e = (Element) currentItem.item(0);
                Hmac = e.getTextContent();


                NodeList n3 = element.getElementsByTagName("Resp");
                for (int i = 0; i < 1; i++) {
                    Node currentItem1 = n3.item(i);
                    errCode = currentItem1.getAttributes().getNamedItem("errCode").getNodeValue();
                    fCount = currentItem1.getAttributes().getNamedItem("fCount").getNodeValue();
                    fType = currentItem1.getAttributes().getNamedItem("fType").getNodeValue();
                    nmPoints = currentItem1.getAttributes().getNamedItem("nmPoints").getNodeValue();
                    qScore = currentItem1.getAttributes().getNamedItem("qScore").getNodeValue();


                }


                NodeList currentItemSkey1 = doc.getElementsByTagName("Data");
                for (int i = 0; i < 1; i++) {
                    Node currentItem1 = currentItemSkey1.item(i);
                    datatype = currentItem1.getAttributes().getNamedItem("type").getNodeValue();
                    String types = currentItem1.getAttributes().getNamedItem("type").getNodeValue();
                }
                Element e1 = (Element) currentItemSkey1.item(0);
                data_data = e1.getTextContent();


                NodeList currentItemSkey = doc.getElementsByTagName("Skey");

                for (int i = 0; i < 1; i++) {
                    Node currentItem1 = currentItemSkey.item(i);
                    ci = currentItem1.getAttributes().getNamedItem("ci").getNodeValue();
                }

                Element eSkey = (Element) currentItemSkey.item(0);
                Skey = eSkey.getTextContent();


                //setText("errorcode=" + errCode + "fcount=" + fCount + "fType=" + fType + "nmPoints=" + nmPoints);


                if (transaction.equalsIgnoreCase("")) {
                     fingureVerify();

                   // Toast.makeText(activity, "kyc done", Toast.LENGTH_SHORT).show();
                    //  adhaarpay();

                }


                element.normalize();
            } else {
                NodeList n3 = element.getElementsByTagName("Resp");
                for (int i = 0; i < 1; i++) {
                    Node currentItem1 = n3.item(i);
                    errInfo = currentItem1.getAttributes().getNamedItem("errInfo").getNodeValue();
                }

                Toast.makeText(this, ""+errInfo, Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            String s = e.getMessage();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    private void fingureVerify() {

        stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
        stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);

        if (NetworkAlertUtility.isInternetConnection2(context)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("dsrno", value)
                    .addFormDataPart("pidType", datatype)
                    .addFormDataPart("pidData", data_data)
                    .addFormDataPart("ci", ci)
                    .addFormDataPart("dc", dc)
                    .addFormDataPart("dpId", dpId)
                    .addFormDataPart("errorCode", errCode)
                    .addFormDataPart("fCount", fCount)
                    .addFormDataPart("fType", fType)
                    .addFormDataPart("hmac", Hmac)
                    .addFormDataPart("mc", mc)
                    .addFormDataPart("mi", mi)
                    .addFormDataPart("nmPoints", nmPoints)
                    .addFormDataPart("qScore", qScore)
                    .addFormDataPart("rdsId", rdsId)
                    .addFormDataPart("rdsVer", rdsVer)
                    .addFormDataPart("sessionKey", Skey)
                    .addFormDataPart("errorInfo", "Success")
                    .addFormDataPart("lat", String.valueOf(stringLatitude))
                    .addFormDataPart("log", String.valueOf(stringLongitude))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getBiometricVerify(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }

    }

    private static boolean checkIfNodeExists(Document document, String xpathExpression) throws Exception {
        boolean matches = false;

        // Create XPathFactory object
        XPathFactory xpathFactory = XPathFactory.newInstance();

        // Create XPath object
        XPath xpath = xpathFactory.newXPath();

        try {
            // Create XPathExpression object
            XPathExpression expr = xpath.compile(xpathExpression);

            // Evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            if (nodes != null && nodes.getLength() > 0) {
                matches = true;
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return matches;
    }

    private void setText(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
               // tv_data.setText(message);
            }
        });
    }

    private String generateTXN() {
        try {
            Date tempDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.ENGLISH);
            String dTTXN = formatter.format(tempDate);
//            return "UKC:public:" + dTTXN;
            return dTTXN;
        } catch (Exception e) {
            Log.e("generateTXN.Error", e.toString());
            return "";
        }
    }

    private String getAuthURL(String UID) {
        String url = "http://developer.uidai.gov.in/auth/";
//        String url = "http://developer.uidai.gov.in/uidauthserver/";
        url += "public/" + UID.charAt(0) + "/" + UID.charAt(1) + "/";
        url += "MG41KIrkk5moCkcO8w-2fc01-P7I5S-6X2-X7luVcDgZyOa2LXs3ELI"; //ASA
        return url;
    }

    @SuppressLint("SetTextI18n")
    public void onResetClicked() {
        fingerCount = 0;

        pidData = null;
        positions.clear();
        positions = new ArrayList<>();
    }

    private void getVerfyOtp() {
        stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
        stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);

        if (NetworkAlertUtility.isInternetConnection2(context)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            String latitude = stringLatitude;
            String longitude = stringLongitude;
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getAepsOtp(activity, headers,latitude,longitude,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }
    }

    private void getDeviceList() {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("demo", "")
                    .build();
            //=======================================
            presenter.getDeviceList(activity,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setUpdevice() {
        binding.etdevice.setThreshold(100);
        roleAdapter = new DeviceAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, devicelist, Gravity.CENTER);
        binding.etdevice.setAdapter(roleAdapter);
        binding.etdevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = devicelist.get(position).getName();
                selectdevice=devicelist.get(position).getId();

                binding.etdevice.setText(name);
                binding.etdevice.clearFocus();
                showtext();
            }
        });
    }

   /* public void showtext() {
        if(selectdevice==1) {
            isAppInstalled = appInstalledOrNot("com.mantra.rdservice");
        }else if (selectdevice==2) {
            isAppInstalled = appInstalledOrNot("com.scl.rdservice");
        }else if (selectdevice==3) {
            isAppInstalled = appInstalledOrNot("com.acpl.registersdk");
        }else if (selectdevice==4) {
            isAppInstalled = appInstalledOrNot("co.aratek.asix_gms.rdservice");
        }  else{
            isAppInstalled = appInstalledOrNot("com.mantra.rdservice");
        }
        if(!isAppInstalled) {
            if (selectdevice == 1) {
                String mainText = "Connect Your Mantra MFS100 V54 Device and download MFS100 RD Service. Click Here";
                String s1 = "Click Here";
                SpannableString spannableString = new SpannableString(mainText);
                int startPositionS1 = mainText.indexOf(s1);
                int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse("market://details?id=" + "com.mantra.rdservice");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "com.mantra.rdservice")));
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                        ds.setColor(linkColor);
                        ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                        ds.setUnderlineText(false);
                    }
                }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                binding.tvtext.setVisibility(View.VISIBLE);
                binding.tvtext.setText(spannableString);
                binding.tvtext.setMovementMethod(LinkMovementMethod.getInstance());

            } else if (selectdevice == 2) {

                String mainText = "Connect Your Morpho MSO 1300 E3 Device and download Morpho SCL RDService. Click Here";
                String s1 = "Click Here";
                SpannableString spannableString = new SpannableString(mainText);
                int startPositionS1 = mainText.indexOf(s1);
                int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse("market://details?id=" + "com.scl.rdservice");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "com.scl.rdservice")));
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                        ds.setColor(linkColor);
                        ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                        ds.setUnderlineText(false);
                    }
                }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                binding.tvtext.setVisibility(View.VISIBLE);
                binding.tvtext.setText(spannableString);
                binding.tvtext.setMovementMethod(LinkMovementMethod.getInstance());
            } else if (selectdevice == 3) {

                String mainText = "Connect Your ACPL FM220 Registered Device and download ACPL Device RDService. Click Here";
                String s1 = "Click Here";
                SpannableString spannableString = new SpannableString(mainText);
                int startPositionS1 = mainText.indexOf(s1);
                int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse("market://details?id=" + "com.acpl.registersdk");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.acpl.registersdk")));
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.acpl.registersdk")));
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                        ds.setColor(linkColor);
                        ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                        ds.setUnderlineText(false);
                    }
                }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                binding.tvtext.setVisibility(View.VISIBLE);
                binding.tvtext.setText(spannableString);
                binding.tvtext.setMovementMethod(LinkMovementMethod.getInstance());


            } else if (selectdevice == 5) {

                String mainText = "Connect Your Aratek A600 Registered Device and download Aratek Device RDService. Click Here";
                String s1 = "Click Here";
                SpannableString spannableString = new SpannableString(mainText);
                int startPositionS1 = mainText.indexOf(s1);
                int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse("market://details?id=" + "com.acpl.registersdk");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.acpl.registersdk")));
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                        ds.setColor(linkColor);
                        ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                        ds.setUnderlineText(false);
                    }
                }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                binding.tvtext.setVisibility(View.VISIBLE);
                binding.tvtext.setText(spannableString);
                binding.tvtext.setMovementMethod(LinkMovementMethod.getInstance());


            }

        }
    }*/


    public void showtext()
    {
        if(selectdevice==1) {
            isAppInstalled = appInstalledOrNot("com.mantra.mfs110.rdservice");

        }else if (selectdevice==2) {
            isAppInstalled = appInstalledOrNot("com.idemia.l1rdservice");
        }else if (selectdevice==3) {
            isAppInstalled = appInstalledOrNot("com.acpl.registersdk_l1");
        }else if (selectdevice==4) {
            isAppInstalled = appInstalledOrNot("com.secugen.rdservice");
        }else if (selectdevice==6) {
            isAppInstalled = appInstalledOrNot("in.co.precisionit.innaitaadhaar");
        } else if (selectdevice==10) {
            isAppInstalled = appInstalledOrNot("in.gov.uidai.facerd");
        } else{
            isAppInstalled = appInstalledOrNot("com.mantra.rdservice");
        }
        if(!isAppInstalled) {
            if (selectdevice == 1) {
                String mainText = "Connect Your Mantra MFS100 V54 Device and download MFS100 RD Service. Click Here";
                String s1 = "Click Here";
                SpannableString spannableString = new SpannableString(mainText);
                int startPositionS1 = mainText.indexOf(s1);
                int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse("market://details?id=" + "com.mantra.mfs110.rdservice");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "com.mantra.mfs110.rdservice")));
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                        ds.setColor(linkColor);
                        ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                        ds.setUnderlineText(false);
                    }
                }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                binding.tvtext.setVisibility(View.VISIBLE);
                binding.tvtext.setText(spannableString);
                binding.tvtext.setMovementMethod(LinkMovementMethod.getInstance());

            } else if (selectdevice == 2) {

                String mainText = "Connect Your Morpho MSO 1300 E3 Device and download Morpho SCL RDService. Click Here";
                String s1 = "Click Here";
                SpannableString spannableString = new SpannableString(mainText);
                int startPositionS1 = mainText.indexOf(s1);
                int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse("market://details?id=" + "com.idemia.l1rdservice");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "com.idemia.l1rdservice")));
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                        ds.setColor(linkColor);
                        ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                        ds.setUnderlineText(false);
                    }
                }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                binding.tvtext.setVisibility(View.VISIBLE);
                binding.tvtext.setText(spannableString);
                binding.tvtext.setMovementMethod(LinkMovementMethod.getInstance());
            } else if (selectdevice == 3) {

                String mainText = "Connect Your ACPL L1 RD service Registered Device and download ACPL L1 RD Service. Click Here";
                String s1 = "Click Here";
                SpannableString spannableString = new SpannableString(mainText);
                int startPositionS1 = mainText.indexOf(s1);
                int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse("market://details?id=" + "com.acpl.registersdk_l1");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.acpl.registersdk_l1")));
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.acpl.registersdk_l1")));
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                        ds.setColor(linkColor);
                        ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                        ds.setUnderlineText(false);
                    }
                }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                binding.tvtext.setVisibility(View.VISIBLE);
                binding.tvtext.setText(spannableString);
                binding.tvtext.setMovementMethod(LinkMovementMethod.getInstance());


            }else if (selectdevice == 4) {

                String mainText = "Connect Your SecuGen Device and download SecuGen SCL RDService. Click Here";
                String s1 = "Click Here";
                SpannableString spannableString = new SpannableString(mainText);
                int startPositionS1 = mainText.indexOf(s1);
                int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse("market://details?id=" + "com.secugen.rdservice");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "com.secugen.rdservice")));
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                        ds.setColor(linkColor);
                        ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                        ds.setUnderlineText(false);
                    }
                }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                binding.tvtext.setVisibility(View.VISIBLE);
                binding.tvtext.setText(spannableString);
                binding.tvtext.setMovementMethod(LinkMovementMethod.getInstance());
            }else if (selectdevice == 6) {

                String mainText = "Connect Your precision Registered Device and download PB1000 L1 RDService. Click Here";
                String s1 = "Click Here";
                SpannableString spannableString = new SpannableString(mainText);
                int startPositionS1 = mainText.indexOf(s1);
                int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse("market://details?id=" + "in.co.precisionit.innaitaadhaar");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "in.co.precisionit.innaitaadhaar")));
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                        ds.setColor(linkColor);
                        ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                        ds.setUnderlineText(false);
                    }
                }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                binding.tvtext.setVisibility(View.VISIBLE);
                binding.tvtext.setText(spannableString);
                binding.tvtext.setMovementMethod(LinkMovementMethod.getInstance());


            } else if (selectdevice == 10) {

                String mainText = "Connect Your Aratek A600 Registered Device and download Aratek Device RDService. Click Here";
                String s1 = "Click Here";
                SpannableString spannableString = new SpannableString(mainText);
                int startPositionS1 = mainText.indexOf(s1);
                int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Uri uri = Uri.parse("market://details?id=" + "in.gov.uidai.facerd");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "in.gov.uidai.facerd")));
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                        ds.setColor(linkColor);
                        ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                        ds.setUnderlineText(false);
                    }
                }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                binding.tvtext.setVisibility(View.VISIBLE);
                binding.tvtext.setText(spannableString);
                binding.tvtext.setMovementMethod(LinkMovementMethod.getInstance());


            }

        }
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private String getPIDOptions() {
        try {

            int fingerCount = 1;
            int fingerType = 2;
            int fingerFormat = 0;
            String pidVer = "2.0";
            String timeOut = "10000";
            String posh = "UNKNOWN";
            if (positions.size() > 0) {
                Log.e("zdsd",""+positions.size());
                posh = positions.toString().replace("[", "").replace("]", "").replaceAll("[\\s+]", "");
            }

            Opts opts = new Opts();
            opts.wadh = "E0jzJ/P8UopUHAieZn8CKqS4WPMi5ZSYXgfnlfkWjrc=";
            opts.fCount = String.valueOf(fingerCount);
            opts.fType = String.valueOf(fingerType);
            opts.iCount = "0";
            opts.iType = "0";
            opts.pCount = "0";
            opts.pType = "0";
            opts.format = String.valueOf(fingerFormat);
            opts.pidVer = pidVer;
            opts.timeout = timeOut;
//            opts.otp = "123456";

            opts.posh = posh;
            opts.env = "P";

            PidOptions pidOptions = new PidOptions();
            pidOptions.ver = "1.0";
            pidOptions.Opts = opts;

            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(pidOptions, writer);
            return writer.toString();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return null;
    }

    @Override
    public void onDeviceListSuccess(BaseResponse body) {
        if(body!=null){
            if(devicelist.size()>0)
                devicelist.clear();

            devicelist.addAll(body.getData().getRdname());

        }
    }

    @Override
    public void onVerifyOtpSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,""+body.getMessage());
            binding.tilotp.setVisibility(View.GONE);
            binding.btnVerifyOTP.setVisibility(View.GONE);

            binding.tildevice.setVisibility(View.VISIBLE);
            binding.btnsubmit.setVisibility(View.VISIBLE);
           /* startActivity(new Intent(activity, AepsServicesActivity.class)
                    .putExtra("title",title)
                    .putExtra("type",type));*/
        }
    }

    @Override
    public void onGetOtpSuccess(BaseResponse body) {
       if(body!=null){
           new CustomToastNotification(activity,""+body.getMessage());

       }
    }

    @Override
    public void onBiometricAuthenticationSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,""+body.getMessage());
            startActivity(new Intent(context, AepsServicesActivity.class)
                    .putExtra("title", title)
                    .putExtra("type", type));
            finish();
        }
    }

    @Override
    public Context getContext()
    {
        return null;
    }

    @Override
    public void enableLoadingBar(boolean enable, String s) {
        if (enable)
            Utils.showProgressDialog(activity,"");
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
    public void onErrorToast(String reason)
    {
        new CustomToastNotification(this, reason);
    }


    @Override
    public void onUserLocationChanged(Location location) {
        mLastLocation = location;
        if (gps.canGetLocation()) {
            StorageUtil.getSharedPref(this).edit()
                    .putString(AppConstants.KEY_LAST_LAT, "" + mLastLocation.getLatitude())
                    .putString(AppConstants.KEY_LAST_LNG, "" + mLastLocation.getLongitude())
                    .apply();

            Log.e("lat", String.valueOf(mLastLocation.getLatitude()));
            Log.e("log", String.valueOf(mLastLocation.getLongitude()));

        } else {
            gps.showSettingsAlert();
        }
    }
}