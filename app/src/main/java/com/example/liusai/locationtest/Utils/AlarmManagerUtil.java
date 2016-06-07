package com.example.liusai.locationtest.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.liusai.locationtest.Receiver.Receiver;

import java.util.Random;

/**
 * Created by user on 2016/6/2.
 */
public class AlarmManagerUtil {

    private static final String TAG = AlarmManagerUtil.class.getSimpleName();

    public static void makeNotificationAlarm(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Trigger the receiver.
        Intent intent = new Intent(Constants.NOTIFICATION_BROADCAST_RECEIVER);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set an alarm fires in between @min minutes and @max minutes.
        int min = 5, max = 10;
        Random random = new Random();
        int minute = random.nextInt(max - min + 1) + min;

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                60 * 1000 * minute, pi
        );


        /* For test.
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        3 * 1000, pi
        );
        */

        Log.d(TAG, "NotificationAlarm set!");
    }

}
