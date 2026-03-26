package com.app.rupiksha.constant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Patterns;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ConstantMethod {

    public static String HH_MM_A = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";


    /********************************* Get  Time UTC *****************************************************/
    public static String getTimeUTC(String formet) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(formet);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(calendar.getTime());
    }

    /********************************* Get  Time UTC *****************************************************/
    public static Date getTimeStringtoDate(String datestart) {
        SimpleDateFormat sdf = new SimpleDateFormat(HH_MM_A);
        Date date = null;
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = sdf.parse(datestart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /****************************************/
    /********************************* Get Current Time*****************************************************/
    public static String getConvertUTCTimeString(Date tdate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a");
        String dateTime;
        dateTime = dateFormat.format(tdate);
        return dateTime;
    }


    /********************************* Get Current Time*****************************************************/
    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /********************************* Set Shared Preference Data *****************************************************/
    public static void SetPreferenceData(String key, String value, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /********************************* Get Shared Preference Data *****************************************************/
    public static String GetPreferenceData(String key, String defaultvalue, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "" + defaultvalue;
        String Getvalue = sharedPref.getString(key, defaultValue);
        return Getvalue;
    }

    /********************************* Get Current Time*****************************************************/
    public static Date getStringConvertDate(String stdate) {
        String dtStart = "2010-10-15T09:27:37Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(stdate);
            System.out.println(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /********************************* Get Current Time*****************************************************/
    public static String getDateConvertString(Date tdate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateTime = "";
        dateTime = dateFormat.format(tdate);
        return dateTime;
    }

    public static long getDayCount(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        long diff = -1;
        try {
            Date dateStart = dateFormat.parse(start);
            Date dateEnd = dateFormat.parse(end);

            //time is always 00:00:00, so rounding should help to ignore the missing hour when going from winter to summer time, as well as the extra hour in the other direction
            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
        } catch (Exception e) {
            //handle the exception according to your own situation
        }
        return diff;
    }

    /********************************* Get Current Time*****************************************************/
    public static String getDateMinus1Day() {
        Calendar c = Calendar.getInstance();
        c.setTime(ConstantMethod.getStringConvertDate("" + ConstantMethod.getDateTime()));
        c.add(Calendar.DATE, -1);
        Date currentDatePlusOne = c.getTime();
        String date_minus_st = ConstantMethod.getDateConvertString(currentDatePlusOne);
        return date_minus_st;
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }



    public static boolean validatePassword(String password)
    {
        return !(TextUtils.isEmpty(password) || password.length() < 8);
    }

}
