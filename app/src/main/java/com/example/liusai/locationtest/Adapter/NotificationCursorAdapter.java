package com.example.liusai.locationtest.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.liusai.locationtest.ContentProvider.ProviderContract;
import com.example.liusai.locationtest.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The method Override seems to be different from Normal Adapter
 *
 * Created by user on 2016/6/4.
 */
public class NotificationCursorAdapter extends CursorAdapter {

    public NotificationCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    // The newView method is used to inflate a new View and return it,
    // don't bind any data to the view at this point
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvSource = (TextView) view.findViewById(R.id.item_notification_source);
        TextView tvTitle = (TextView) view.findViewById(R.id.item_notification_title);
        TextView tvContent = (TextView) view.findViewById(R.id.item_notification_content);
        TextView tvCreated = (TextView) view.findViewById(R.id.item_notification_created);

        tvSource.setText("Source: " + cursor.getString(cursor.getColumnIndexOrThrow(ProviderContract.DBEntry.COLUMN_NAME_SOURCE)));
        tvTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(ProviderContract.DBEntry.COLUMN_NAME_TITLE)));
        tvContent.setText(cursor.getString(cursor.getColumnIndexOrThrow(ProviderContract.DBEntry.COLUMN_NAME_CONTENT)));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        tvCreated.setText( sdf.format(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(ProviderContract.DBEntry.COLUMN_NAME_CREATE_DATE)))) );

    }











}
