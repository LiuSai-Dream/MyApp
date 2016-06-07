package com.example.liusai.locationtest.Utils;

import com.example.liusai.locationtest.Entity.LatLngAddrEntity;

public final class Constants {
	private Constants(){
		throw new UnsupportedOperationException("Not Support Operation");
	}

	public static final int SATE_UPDATE = 0X0010;
	public static final int LOCATION_UPDATE = 0X0020;
	public static final int OPEN_GPS = 0X0030;

	private static final float RADIUS = 300;

	//地址初始化后续可从配置文件中读取
	public static final LatLngAddrEntity[] ADDRESSES = {new LatLngAddrEntity("华远街-置地星座", 39.908232, 116.364894, RADIUS), new LatLngAddrEntity("魏公村", 39.958049, 116.314767, RADIUS)};

	public static final String NOTIFICATION_BROADCAST_RECEIVER = "com.example.liusai.locationtest.NOTIFICATION";

	public static final String ACTION_PROXIMITY_ALERT = "com.example.liusai.locationtest.PROXIMITYALERT";
}
