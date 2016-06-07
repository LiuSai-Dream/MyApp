package com.example.liusai.locationtest.ContentProvider;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import org.apache.http.auth.AUTH;

/**
 * Created by user on 2016/6/2.
 */
public final class ProviderContract {

    public static final String AUTHORITY = "com.example.liusai.locationtest.provider";

    // To prevent from accidentally instantiating the contract class,
    // give it an empty constructor
    private ProviderContract() { }

    // Inner class that defines the table contents
    public static class DBEntry implements BaseColumns {

        // This class cannot be instantiated
        private DBEntry() {}

        // Database version and name
        public static final int DATABASE_VERSION = 2;
        public static final String DATABASE_NAME = "database";

        // Table "notification" related
        public static final String TABLE_NAME = "notification";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_SOURCE = "source";
        public static final String COLUMN_NAME_CREATE_DATE = "created";
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_CREATE_DATE + " DESC";

    }

    /**
     * The scheme part for this provider's URI
     */
    private static final String SCHEME = "content://";

    /**
     * Path parts for the URIs
     */
    
    /**
     * Path part for the table
     */
    private static final String PATH_TABLE = "/notification";

    /**
     * Path part for the row
     */
    private static final String PATH_ROW = "/notification/";

    /**
     * 0-relative position of a segment in the path part
     */
    public static final int NOTIFICATION_ID_PATH_POSITION = 1;

    /**
     * The content:// style URL for the table
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_TABLE);

    /**
     * The content URI for a row, Callers must append a numeric id
     * to this Uri to retrieve the specified row
     */
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ROW);

    /**
     * The content URI match pattern for a row, use this match incoming URIs
     */
    public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(
        SCHEME + AUTHORITY + PATH_ROW + "/#"
    );

    /**
     * The MIME type of {@CONTENT_URI}
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.notification";

    /**
     * The MIME subtype of {@CONTENT_URI}
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.notification";




}
