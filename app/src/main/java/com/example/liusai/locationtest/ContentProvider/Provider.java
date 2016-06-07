package com.example.liusai.locationtest.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by user on 2016/6/2.
 */
public class Provider extends ContentProvider {

    private static final String TAG = Provider.class.getSimpleName();

    /**
     * Constants used by UriMatcher to act_map action to int.
     */
    // Incoming URI matches the Notification table pattern
    private static final int TABLE = 1;

    // Incoming URI matches the Notification row pattern
    private static final int ROW = 2;

    // A UriMatcher instance
    private static final UriMatcher sUriMatcher;

    // The database openHelper
    private DatabaseOpenHelper mOpenHelper;

    // A projection act_map used to select columns from the database
    private static HashMap<String, String> sNotifProjectionMap;

    /**
     * A block that instantiates and sets static objects
     */
    static {
        /**
         * Creates and initializes the URI matcher
         */
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add a pattern that routes URIs terminated with "notification"
        sUriMatcher.addURI(ProviderContract.AUTHORITY, "notification", TABLE);

        // Add a pattern that routes URIs terminated with "notification" plus an integer
        sUriMatcher.addURI(ProviderContract.AUTHORITY, "notification/#", ROW);

        /**
         * Creates and initializes a projection act_map that return all columns
         */
        sNotifProjectionMap = new HashMap<String, String>();

        sNotifProjectionMap.put(ProviderContract.DBEntry._ID, ProviderContract.DBEntry._ID);

        sNotifProjectionMap.put(ProviderContract.DBEntry.COLUMN_NAME_TITLE, ProviderContract.DBEntry.COLUMN_NAME_TITLE);

        sNotifProjectionMap.put(ProviderContract.DBEntry.COLUMN_NAME_CONTENT, ProviderContract.DBEntry.COLUMN_NAME_CONTENT);

        sNotifProjectionMap.put(ProviderContract.DBEntry.COLUMN_NAME_SOURCE, ProviderContract.DBEntry.COLUMN_NAME_SOURCE);

        sNotifProjectionMap.put(ProviderContract.DBEntry.COLUMN_NAME_CREATE_DATE, ProviderContract.DBEntry.COLUMN_NAME_CREATE_DATE);


    }


    @Override
    public boolean onCreate() {

        // Creates a new helper object. Notes that the database itself isn't
        // opened until tries to access it. and it's only created if it doesn't already exist.
        mOpenHelper = new DatabaseOpenHelper(getContext());

        // Assumes that any failures will be reported by a thrown exception
        return true;
    }

    @Override
    public Cursor query(Uri uri,String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        // Construct a new query builder and sets its table name
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(ProviderContract.DBEntry.TABLE_NAME);
        qb.setProjectionMap(sNotifProjectionMap);

        // SQLiteDatabase and return cursor
        Cursor cursor;
        SQLiteDatabase db = null;

        /**
         * Choose the projection and adjust the "where" clause based on URI pattern
         */
        switch (sUriMatcher.match(uri)) {

            case TABLE:
                break;

            case ROW:
                // Append where clause
                qb.appendWhere(ProviderContract.DBEntry._ID + " = " +
                uri.getPathSegments().get(ProviderContract.NOTIFICATION_ID_PATH_POSITION)
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String orderBy;
        // if no sort order is specified, uses the default
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = ProviderContract.DBEntry.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        try {
            db = mOpenHelper.getReadableDatabase();

            cursor = qb.query(
                    db,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    orderBy
            );

            // Tells the cursor what URI to watch, so it knows when its source data changed
            cursor.setNotificationUri(getContext().getContentResolver(), uri);

        } catch (NullPointerException e) {
            throw new NullPointerException("Fails to attain DB");

        } catch (SQLException e) {
            throw new SQLException("Unable to get readableDatabase()");
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {

            case TABLE:
                return ProviderContract.CONTENT_TYPE;

            case ROW:
                return ProviderContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

    }


    @Override
    synchronized public Uri insert(Uri uri, ContentValues initialValues) {

        // Validates the incoming URI. Only the full provider URI is allowed for insert

        if (sUriMatcher.match(uri) != TABLE) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        SQLiteDatabase db = null;


        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        Long now = System.currentTimeMillis();

        if (!values.containsKey(ProviderContract.DBEntry.COLUMN_NAME_SOURCE)) {
            values.put(ProviderContract.DBEntry.COLUMN_NAME_SOURCE, "Unknown source");
        }

        if (!values.containsKey(ProviderContract.DBEntry.COLUMN_NAME_TITLE)) {
            values.put(ProviderContract.DBEntry.COLUMN_NAME_TITLE, "title not found");
        }

        if (!values.containsKey(ProviderContract.DBEntry.COLUMN_NAME_CREATE_DATE)) {
            values.put(ProviderContract.DBEntry.COLUMN_NAME_CREATE_DATE, now);
        }

        try {
            db = mOpenHelper.getWritableDatabase();
        } catch (SQLException e) {
            throw new SQLException("Unable to get writeableDatabase()");
        }

        db.beginTransaction();
        Long rowId = db.insert(ProviderContract.DBEntry.TABLE_NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();

        if(rowId > 0) {
            Uri notifiUri = ContentUris.withAppendedId(ProviderContract.CONTENT_ID_URI_BASE, rowId);

            // Notifies observers registered against this provider that the data changed
            getContext().getContentResolver().notifyChange(notifiUri, null);

            return notifiUri;
        }
        throw new SQLException("Failed to insert row into " + uri);


    }


    @Override
    synchronized public int delete(Uri uri, String where, String[] whereArgs) {

        SQLiteDatabase db = null;

        try {
            db = mOpenHelper.getWritableDatabase();
        } catch (SQLException e) {
            throw new SQLException("Unable to get writeableDatabase()");
        }

        String finalWhere;

        int count;

        switch (sUriMatcher.match(uri)) {

            case TABLE:
                count = db.delete(ProviderContract.DBEntry.TABLE_NAME,
                        where,
                        whereArgs);

                Log.d(TAG, "Delete table uri is " + uri);
                Log.d(TAG, "The row number delete " + count);
                break;

            case ROW:
                finalWhere = ProviderContract.DBEntry._ID +
                        " = " +
                        uri.getPathSegments().get(ProviderContract.NOTIFICATION_ID_PATH_POSITION);

                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }

                db.beginTransaction();
                count = db.delete(ProviderContract.DBEntry.TABLE_NAME,
                        finalWhere, whereArgs);
                db.setTransactionSuccessful();
                db.endTransaction();

                Log.d(TAG, "Delete row uri is " + uri);
                Log.d(TAG, "The row number delete " + count);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {

        return 0;
    }


    // Class about SQLiteOpenHelper
    static class DatabaseOpenHelper extends SQLiteOpenHelper {

        // Database creation statement.
        private static final String DATABASE_CREATION = "CREATE TABLE " +
                ProviderContract.DBEntry.TABLE_NAME + " ( " +
                ProviderContract.DBEntry._ID + " INTEGER PRIMARY KEY, " +
                ProviderContract.DBEntry.COLUMN_NAME_TITLE + " TEXT, " +
                ProviderContract.DBEntry.COLUMN_NAME_CONTENT + " TEXT, " +
                ProviderContract.DBEntry.COLUMN_NAME_SOURCE + " TEXT, " +
                ProviderContract.DBEntry.COLUMN_NAME_CREATE_DATE + " INTEGER" +
                " );"  ;

        DatabaseOpenHelper(Context context) {
            super(context, ProviderContract.DBEntry.DATABASE_NAME, null, ProviderContract.DBEntry.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }


}
