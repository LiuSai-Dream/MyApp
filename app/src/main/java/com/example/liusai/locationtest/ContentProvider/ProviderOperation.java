package com.example.liusai.locationtest.ContentProvider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.liusai.locationtest.Adapter.NotificationCursorAdapter;

/**
 * Created by user on 2016/6/2.
 */
public class ProviderOperation {

    private static final String TAG = ProviderOperation.class.getSimpleName();

    static String[] froms = new String[] {"f1","f2","f3","f4"};
    static String[] titles = new String[] {"t1","t2","t3","t4"};
    static String[] contents = new String[] {"c1","c2","c3","c4"};
    static Long[] time = new Long[] {System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis()};

    private Context mContext;
    private ContentResolver contentResolver;

    String[] projections = {ProviderContract.DBEntry._ID,
            ProviderContract.DBEntry.COLUMN_NAME_TITLE,
            ProviderContract.DBEntry.COLUMN_NAME_CONTENT,
            ProviderContract.DBEntry.COLUMN_NAME_SOURCE,
            ProviderContract.DBEntry.COLUMN_NAME_CREATE_DATE
    };


    public ProviderOperation(Context context) {
        mContext = context;

        contentResolver = mContext.getContentResolver() ;
    }

    public void insert() {
        Log.d(TAG, "通过ContentResolver插入数据");
        Uri uri = ProviderContract.CONTENT_URI;
        for (int i = 0; i < froms.length; ++i) {

            ContentValues values = new ContentValues();
            values.put(ProviderContract.DBEntry.COLUMN_NAME_TITLE, titles[i]);
            values.put(ProviderContract.DBEntry.COLUMN_NAME_CONTENT, contents[i]);
            values.put(ProviderContract.DBEntry.COLUMN_NAME_SOURCE, froms[i]);
            values.put(ProviderContract.DBEntry.COLUMN_NAME_CREATE_DATE, time[i]);

            contentResolver.insert(uri, values);

        }

    }

    public void delete(int id) {

        Uri uri = ProviderContract.CONTENT_URI;

        // If id >= 0, meaning delete one row
        if (id >= 0) {
            uri = ContentUris.withAppendedId(uri, id);
        }

        contentResolver.delete(uri, null, null);

    }

    public Cursor query() {

        Log.d(TAG, "通过ContentResolver查询数据");

        Uri uri = ProviderContract.CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, projections, null, null, null);

        return cursor;
    }

    public void display(ListView listView) {

        Cursor cursor = query();

        if (cursor == null) {
            Log.d(TAG, "查询到的结果为null.");
        } else {

            String[] listColumns = {};

            NotificationCursorAdapter notificationCursorAdapter = new NotificationCursorAdapter(mContext, cursor, 0);

            listView.setAdapter(notificationCursorAdapter);


            Log.d(TAG, "查询结果的数量 " + cursor.getCount());

            Log.d(TAG, "_ID    TITLE   CONTENT   SOURCE    DATE");
            while(cursor.moveToNext()) {
                Log.d(TAG, cursor.getInt(0) + " " +
                        cursor.getString(1) + " " +
                        cursor.getString(2) + " " +
                        cursor.getString(3) + " " +
                        cursor.getInt(4)
                );
            }
        }

    }


}
