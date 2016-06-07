package com.example.liusai.locationtest.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liusai.locationtest.Utils.Constants;
import com.example.liusai.locationtest.Action.LocationCollector;
import com.example.liusai.locationtest.R;
//import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;


public class LocationActivity extends Activity implements OnClickListener{

	private static final String TAG = LocationActivity.class.getSimpleName();

	private TextView positionTextView, gpsTextView;

	private Button btn, btnMap;
	private Button btnGps, btnNet, btnGn;

	private UIHandler handler;

	private LocationCollector locationCollector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_location);

		btn = (Button) findViewById(R.id.button);
		btnGps = (Button) findViewById(R.id.gpsOnly); btnGps.setOnClickListener(this);
		btnNet = (Button) findViewById(R.id.netOnly); btnNet.setOnClickListener(this);
		btnGn = (Button) findViewById(R.id.gpsNet); btnGn.setOnClickListener(this);
		btnMap = (Button) findViewById(R.id.map);

		positionTextView = (TextView) findViewById(R.id.textView);
		gpsTextView = (TextView) findViewById(R.id.gpsStatus);
		gpsTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

		/*
		String appKey = "5735bf8667e58e74500037c3", channelID = "12";
		UMAnalyticsConfig umConfig = new UMAnalyticsConfig(this, appKey, channelID);
		MobclickAgent.startWithConfigure(umConfig);
		MobclickAgent.setDebugMode(true);
		Log.d(TAG, getDeviceInfo(this));
		*/

		handler = new UIHandler();

		locationCollector = LocationCollector.getInstance(this, handler);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LocationActivity.this, StationActivity.class);
				startActivity(intent);
			}
		});

		btnMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LocationActivity.this, MapActivity.class);
				startActivity(intent);
			}
		});

	}

	protected void onResume() {
		super.onResume();
	//	MobclickAgent.onResume(this);  //Session 统计
	}

	protected void onPause() {
		super.onPause();
	//	MobclickAgent.onPause(this);
	}

	protected void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.gpsOnly:
			//	MobclickAgent.onEvent(this, Constants.GPS_EVENT_LOCATE);
				Toast.makeText(this, "只用GPS定位", Toast.LENGTH_SHORT).show();
				locationCollector.GPSONLY = true; locationCollector.NETONLY = false; locationCollector.MIXED = false;
				locationCollector.registerProvider();
				break;

			case R.id.netOnly:
			//	MobclickAgent.onEvent(this, Constants.NETWORK_EVENT_LOCATE);
				Toast.makeText(this, "只用network定位", Toast.LENGTH_SHORT).show();
				locationCollector.GPSONLY = false; locationCollector.NETONLY = true; locationCollector.MIXED = false;
				locationCollector.registerProvider();
				break;

			case R.id.gpsNet:
			//	MobclickAgent.onEvent(this, Constants.MIX_EVENT_LOCATE);
				Toast.makeText(this, "混合定位", Toast.LENGTH_SHORT).show();
				locationCollector.GPSONLY = false; locationCollector.NETONLY = false; locationCollector.MIXED = true;
				locationCollector.registerProvider();
				break;

		}
	}

	class UIHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case Constants.SATE_UPDATE:
					gpsTextView.setText(msg.obj.toString());
					break;
				case Constants.LOCATION_UPDATE:
					positionTextView.setText(msg.obj.toString());
					break;
				case Constants.OPEN_GPS:
					openGPS(LocationActivity.this, msg.obj.toString());
			}
		}
	}

	private void openGPS(final Activity activity, final String str){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;

		builder.setMessage(str).setPositiveButton("确定",
				new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int id){
						activity.startActivity(new Intent(action));
						dialog.dismiss();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					dialog.cancel();
				}
		});

		final AlertDialog alert = builder.create();
		alert.show();
	}

}
