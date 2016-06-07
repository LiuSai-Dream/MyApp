package com.example.liusai.locationtest.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.liusai.locationtest.Activity.MainActivity;
import com.example.liusai.locationtest.R;

/**
 * Created by user on 2016/6/1.
 */
public class NotificationUtil {

    public static void makeNotification(Context context, int smallIcon, String title,
                                        String content, int id) {

        // Building the Notification builder.
        Notification.Builder mBuilder = new Notification.Builder(context)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(content);

        // Setting the intent.
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent,
                 PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        // Setting the vibrate interval.
        long[] vibrate = new long[]{1000l, 500l};
        mBuilder.setVibrate(vibrate);

        // Notification appear on lock screen.
        // To modify ...
        mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        mBuilder.addAction(R.drawable.ic_launcher, "Previous", pendingIntent);
        mBuilder.addAction(R.drawable.ic_launcher, "Pause", pendingIntent);
        mBuilder.addAction(R.drawable.ic_launcher, "Next", pendingIntent);
        mBuilder.setStyle(new Notification.MediaStyle().setShowActionsInCompactView(1));

        // Ready to notify the notification.
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setAutoCancel(true);
        notificationManager.notify(id, mBuilder.build());
    }


    public static void makeProximityAlertNotification(Context context, int smallIcon, String title,
                String content, int id) {

        // Building the Notification builder.
        Notification.Builder mBuilder = new Notification.Builder(context)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(content);

        // Setting the intent.
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        // Setting the vibrate interval.
        long[] vibrate = new long[]{1000l, 500l};
        mBuilder.setVibrate(vibrate);

        // Ready to notify the notification.
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setAutoCancel(true);
        notificationManager.notify(id, mBuilder.build());
    }

}

/*
    Sample code for constructing regular notification.

I   Intent intent = new Intent(context, MapActivity.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(MapActivity.class);
    stackBuilder.addNextIntent(intent);
    PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
 */

/*
    Sample code for Seting the InboxStyle.

    Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
    String[] events = new String[]{"展开后的内容1", "展开后的内容2", "展开后的内容3"};
    inboxStyle.setBigContentTitle("这是展开后的标题（与原来的标题不同）：");
    for (int i = 0; i < events.length; ++i) {
        inboxStyle.addLine(events[i]);
    }
    mBuilder.setStyle(inboxStyle);
*/