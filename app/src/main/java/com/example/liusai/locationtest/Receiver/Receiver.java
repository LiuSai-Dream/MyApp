package com.example.liusai.locationtest.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.liusai.locationtest.Activity.MainActivity;
import com.example.liusai.locationtest.R;
import com.example.liusai.locationtest.Utils.AlarmManagerUtil;
import com.example.liusai.locationtest.Utils.Constants;
import com.example.liusai.locationtest.Utils.NotificationUtil;

/**
 * Created by user on 2016/5/31.
 */
public class Receiver extends BroadcastReceiver {

    private static final String TAG = Receiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction().toString();
        Log.i(TAG, action);

        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {

            Toast.makeText(context, "启动成功，设置提醒成功", Toast.LENGTH_SHORT).show();

            // Setting the alarm again.
            AlarmManagerUtil.makeNotificationAlarm(context);

        } else if (action.equals(Constants.NOTIFICATION_BROADCAST_RECEIVER)) {

            // Better to fetch title and content in another thread... ie.CursorLoader
            NotificationUtil.makeNotification(context, R.drawable.ic_launcher, "消息来啦...", "消息在远方", 2);

            // Setting the alarm again.
            AlarmManagerUtil.makeNotificationAlarm(context);

        } else if (action.equals(Constants.ACTION_PROXIMITY_ALERT)){

            // Getting the state of entering or leaving
            final String key = LocationManager.KEY_PROXIMITY_ENTERING;
            final Boolean entering = intent.getBooleanExtra(key, false);
            Bundle bundle = intent.getExtras();

            Log.d(TAG,"Receiving the Proximity alert...");

            if(bundle != null) {
                if (entering) {
                    Toast.makeText(context, "进入区域：" + bundle.getString("address"), Toast.LENGTH_LONG).show();

                    NotificationUtil.makeProximityAlertNotification(context, R.drawable.ic_launcher,"位置提醒","进入区域：" + bundle.getString("address"), 3);
                } else {
                    Toast.makeText(context, "离开区域：" + bundle.getString("address"), Toast.LENGTH_LONG).show();

                    NotificationUtil.makeProximityAlertNotification(context, R.drawable.ic_launcher,"位置提醒","离开区域：" + bundle.getString("address"), 4);
                }
            }

        }

    }




}
