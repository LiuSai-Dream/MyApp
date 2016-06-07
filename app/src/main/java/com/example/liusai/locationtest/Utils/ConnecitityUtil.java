package com.example.liusai.locationtest.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.liusai.locationtest.R;

/**
 * tools about network
 */

public class ConnecitityUtil {

	/**
	 * 得到网络信息
	 * @param context
	 * @return
     */
	public static NetworkInfo getNetworkInfo(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	/**
	 * 是否联网
	 * @param context
	 * @return
     */
	public static boolean isConnected(Context context){
		NetworkInfo info = ConnecitityUtil.getNetworkInfo(context);
		return (info != null && info.isConnected());
	}

	/**
	 * 是否wifi联网
	 * @param context
	 * @return
     */
	public static boolean isConnectedWifi(Context context){
		NetworkInfo info = ConnecitityUtil.getNetworkInfo(context);
		return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
	}

	/**
	 * 是否移动数据联网
	 * @param context
     * @return
     */
	public static boolean isConnectedMobile(Context context){
		NetworkInfo info = ConnecitityUtil.getNetworkInfo(context);
		return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
	}

	/**
	 * 网速够不够快
	 * @param context
	 * @return
     */
	public static boolean isConnectedFast(Context context){
		NetworkInfo info = ConnecitityUtil.getNetworkInfo(context);
		return (info != null && info.isConnected() && ConnecitityUtil.isConnectionFast(info.getType(),info.getSubtype()));
	}

	/**
	 * 网速工具类
	 * @param type
	 * @param subType
     * @return
     */
	public static boolean isConnectionFast(int type, int subType){
		if(type==ConnectivityManager.TYPE_WIFI){
			return true;
		}else if(type==ConnectivityManager.TYPE_MOBILE){
			switch(subType){
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					return false; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return false; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return false; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					return true; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return true; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return false; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					return true; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					return true; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					return true; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return true; // ~ 400-7000 kbps

				case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
					return true; // ~ 1-2 Mbps
				case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
					return true; // ~ 5 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
					return true; // ~ 10-20 Mbps
				case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
					return false; // ~25 kbps
				case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
					return true; // ~ 10+ Mbps
				// Unknown
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				default:
					return false;
			}
		}else{
			return false;
		}
	}

}
