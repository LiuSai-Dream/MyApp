package com.example.liusai.locationtest.Action;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.example.liusai.locationtest.Utils.Constants;
import com.example.liusai.locationtest.Entity.LatLngAddrEntity;
import com.example.liusai.locationtest.Exception.GeocoderNotImpException;
import com.example.liusai.locationtest.Utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 2016/5/13.
 */
public class LocationCollector implements GpsStatus.Listener, LocationListener{

	private Context context = null;
	private Handler handler = null;

	private LocationManager locationManager = null;

	private List<String> providerList = new ArrayList<String>();

	private static volatile LocationCollector INSTANCE = null;
	private static final String TAG = "LocationCollector";
	public static boolean GPSONLY = false, NETONLY = false, MIXED = false;
	private static long FIX_TIME_ELAPSE = 60 * 1000000000L;


	// Used to save act_location information
	private StringBuilder sb = new StringBuilder();


	/**
	 * 实例化LocationUtil，中添加gps监听接口，获取卫星信息。
	 * @param context 不能为空
	 *
	 */
	private LocationCollector(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}
	/**
	 * 单例模式
	 * @param context 不能为空
	 * @param handler 不能为空
	 * @return 返回实例
	 */
	public static LocationCollector getInstance(Context context, Handler handler) {
		if (INSTANCE == null) {
			synchronized (LocationCollector.class) {
				if (INSTANCE == null) {
					INSTANCE = new LocationCollector(context, handler);
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * 向LocationManager 注册位置变化监听器，注册GPS状态等操作
	 */
	void locationManagerHelper(LocationManager locationManager, boolean isRegister){
		if (locationManager != null){
			if (isRegister) {
				//	registerProvider();
				locationManager.addGpsStatusListener(this);
			} else {
				locationManager.removeGpsStatusListener(this);
			}
		}
	}

	/**
	 * 注册临近提醒
	 * @param
     */
	 void addProximityAlerts(){
		if(locationManager != null){

			PendingIntent pi = null;

			// Add proximity alert for all addresses
			for(int i = 0; i< Constants.ADDRESSES.length; ++i){

				LatLngAddrEntity lla = Constants.ADDRESSES[i];

				Intent intent = new Intent();
				intent.setAction(Constants.ACTION_PROXIMITY_ALERT);
				Bundle bundle = new Bundle();
				bundle.putString("address", lla.getAddress());
				intent.putExtras(bundle);

				pi = PendingIntent.getBroadcast(context, i, intent, 0);

				locationManager.addProximityAlert(lla.getLatitude(), lla.getLongitude(), lla.getRadius(), -1, pi);
			}
		}
	}

	/**
	 * 删除临近警告
	 * @param locationManager
     */
	void deleteProximityAlerts(LocationManager locationManager){
		if(locationManager != null){
			PendingIntent pi = null;
			for(int i=0; i<Constants.ADDRESSES.length; ++i){
				LatLngAddrEntity lla = Constants.ADDRESSES[i];
				Intent intent = new Intent();
				intent.setAction(Constants.ACTION_PROXIMITY_ALERT);
				Bundle bundle = new Bundle();
				bundle.putString("address", lla.getAddress());
				intent.putExtras(bundle);
				pi = PendingIntent.getBroadcast(context, i, intent, 0);

				locationManager.removeProximityAlert(pi);
			}
		}
	}

	/**
	 * GPS卫星的监听接口
	 */
	@Override
	public void onGpsStatusChanged(int event) {
		StringBuilder msb = new StringBuilder();
		GpsStatus gpsStatus = locationManager.getGpsStatus(null);

		if (gpsStatus != null) {
			Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
			Iterator<GpsSatellite> sat = satellites.iterator();
			int i=0;

			while(sat.hasNext()){
				GpsSatellite satellite = sat.next();
				msb.append((i++) + ":   " + satellite.getPrn() + ",  " + satellite.usedInFix() + ",  " + satellite.getSnr() + ",  " + satellite.getAzimuth() + ",  " + satellite.getElevation()+ "\n");
			}
		} else {
			msb.append("无卫星信息 \n");
		}
		sendMessage(Constants.SATE_UPDATE, msb.toString());
	}


	/**
	 * 根据主界面中的按钮选择相应的方式，并且注册相关接口
	 */
	public void registerProvider() {

		if (Utils.checkPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) && Utils.checkPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

			if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				sendMessage(Constants.OPEN_GPS, "请打开GPS");
			}

			providerList.clear();
			if (GPSONLY) {
				providerList.add(LocationManager.GPS_PROVIDER);
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
			} else if (NETONLY) {
				providerList.add(LocationManager.NETWORK_PROVIDER);
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
			} else if (MIXED) {

				//为所有的提供器(不管有没有开启)注册监听
				providerList = locationManager.getProviders(false);
				for(String provider : providerList){
					locationManager.requestLocationUpdates(provider, 10000, 10, this);
				}
			}
			locationManagerHelper(locationManager, true);
		}
	}

	/**
	 * 去除注册的接口
	 */
	public void unRegisterProvider(){
		unRegisterProvider();
		locationManagerHelper(locationManager, false);
	}

	/**
	 * 根据经纬度获取地址
	 * @param latitude 纬度
	 * @param longtiude 经度
	 * @return 地址
	 */
	private String getAddress(double latitude, double longtiude) {

		String address = null;
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		List<Address> addressList;

		try {
			if (Geocoder.isPresent()) {
				Log.d(TAG, "Geocoder ready");
			} else {
				sb.insert(0, "Geocoder未实现\n" );
				throw new GeocoderNotImpException();
			}

			addressList = geocoder.getFromLocation(latitude, longtiude, 1);
			String street = addressList.get(0).getAddressLine(0);
			String city = addressList.get(0).getLocality();
			address = city + " " + street + "\n";

			Log.d(TAG, "get act_location from Geocoder " + address);
		} catch (GeocoderNotImpException e){
			Log.d(TAG, "Geocoder unready");
		} catch (Exception e) {
			sb.insert(0, "根据经纬度获取地址失败\n");
			Log.e(TAG, "Fail to get act_location with latitude");
		}
		return address;
	}

	/**
	 * 通过位置监听器的改变封装位置信息
	 */
	private void showLocation(Location location){
		String currentPosition = null;
		if (location == null) {
			currentPosition = "无法定位";
		} else {
			String address = getAddress(location.getLatitude(), location.getLongitude());
			currentPosition = "使用的provider： " + location.getProvider() + "\n纬度： " + location.getLatitude()
					+ "\n经度： " + location.getLongitude() + "\n当前的位置： " + address;
		}
		sb.insert(0, currentPosition + "\n");
	}

	/*
     * (non-Javadoc) 监听位置变化，当定位的时间和当前的时间大于30秒的时候，该位置不算数。 添加临近位置提醒
     * @see android.act_location.LocationListener#onLocationChanged(android.act_location.LocationActivity)
     */
	@Override
	public void onLocationChanged(Location location) {
		long fixTime = location.getElapsedRealtimeNanos();
		long now = SystemClock.elapsedRealtimeNanos();

		Log.d(TAG, "Time Gap is " + (now - fixTime) + " nanoseconds");
		if (now - fixTime > FIX_TIME_ELAPSE){
			return;
		}

		Log.d(TAG, "Location changed!");
		sb.insert(0, "provider "+ location.getProvider() +" 位置发生了改变\n\n");

		addProximityAlerts();
		showLocation(location);
		sendMessage(Constants.LOCATION_UPDATE, sb.toString());
	}

	@Override
	public void onProviderDisabled(String prov) {
		sb.insert(0, prov + " 提供器关闭\n\n");
		sendMessage(Constants.LOCATION_UPDATE, sb.toString());
		Log.d(TAG, "Provider closed!");
	}


	@Override
	public void onProviderEnabled(String prov) {
		sb.insert(0, prov + " 提供器开启\n\n");
		sendMessage(Constants.LOCATION_UPDATE, sb.toString());
		Log.d(TAG, "Provider started!");
	}


	@Override
	public void onStatusChanged(String prov, int arg1, Bundle arg2) {
		sb.insert(0, prov + " 状态发生了改变\n\n");
		sendMessage(Constants.LOCATION_UPDATE, sb.toString());
		Log.d(TAG, "Provider status changed!");
	}

	/**
	 * 向handler发送消息，UI展示
	 * @param what 消息编号
	 * @param string 消息字符串
	 */
	private void sendMessage(int what, String string){
		Message msg = new Message();
		msg.what = what;
		msg.obj = string;

		handler.sendMessage(msg);
	}

}
