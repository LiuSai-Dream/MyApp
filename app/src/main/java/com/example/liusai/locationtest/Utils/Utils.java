package com.example.liusai.locationtest.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by liusai on 16/5/12.
 */
public class Utils {

    public static boolean checkPermission(Context context, String permission) {
        PackageManager packageManager = context.getPackageManager();

        if (packageManager.PERMISSION_GRANTED == packageManager.checkPermission(permission,context.getPackageName())) {
            return true;
        }

        return false;
    }

    public static String getDeviceId(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);

            // Getting the device_id
            String device_id = null;
            if (Utils.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }

            // Getting the wifi mac address
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            // If device id is null, replace it with mac
            if(TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            // If mac is null
            if(TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();

        } catch(Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * 取得手机号；只有手机号存储在sim卡上的时候才能得到，否则为空
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tm.getLine1Number();
        return phoneNumber;
    }
}
