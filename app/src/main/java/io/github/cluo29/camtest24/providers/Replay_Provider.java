package io.github.cluo29.camtest24.providers;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

/**
 * Created by chul3 on 25/10/2017.
 */

public class Replay_Provider extends ContentProvider {

    public static final int DATABASE_VERSION = 2;

    public static String AUTHORITY = "io.github.cluo29.camtest24.provider.replay";


    // ContentProvider query paths
    private static final int SENSOR_DEV = 1;
    private static final int SENSOR_DEV_ID = 2;

    public static final class Replay_Info implements BaseColumns {
        private Replay_Info() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + Replay_Provider.AUTHORITY + "/replay");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.camtest24.replay";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.camtest24.replay";

        public static final String _ID = "_id";
        public static final String TIMESTAMP = "timestamp";
        public static final String DAY = "day";
        public static final String MONTH = "month";
        public static final String YEAR = "year";
        public static final String REPLAYNAME = "replayname";
        public static final String APPNAME = "appname";
        public static final String VIDEO = "video";
        public static final String ORIENTATION = "orientation";
        public static final String MODE = "mode";
        public static final String STATUS = "status"; //Running, Scheduled, Pending
    }

    public static String DATABASE_NAME = "replay.db";

    public static final String[] DATABASE_TABLES = { "replay" };

    public static final String[] TABLES_FIELDS = {
            Replay_Info._ID + " integer primary key autoincrement,"
                    + Replay_Info.TIMESTAMP + " real default 0,"
                    + Replay_Info.DAY + " real default 0,"
                    + Replay_Info.MONTH + " text default '',"
                    + Replay_Info.YEAR + " real default 0,"
                    + Replay_Info.REPLAYNAME + " text default '',"
                    + Replay_Info.APPNAME + " text default '',"
                    + Replay_Info.VIDEO + " text default '',"
                    + Replay_Info.ORIENTATION + " text default '',"
                    + Replay_Info.MODE + " text default '',"
                    + Replay_Info.STATUS + " text default ''"
    };

    private static UriMatcher sUriMatcher = null;
    private static HashMap<String, String> sensorMap = null;
    private static DatabaseHelper databaseHelper = null;
    private static SQLiteDatabase database = null;


    private boolean initializeDB() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper( getContext(), DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS );
        }
        if( databaseHelper != null && ( database == null || ! database.isOpen() )) {
            database = databaseHelper.getWritableDatabase();
        }
        return( database != null && databaseHelper != null);
    }

    public static void resetDB( Context c ) {
        File db = new File(DATABASE_NAME);
        db.delete();
        databaseHelper = new DatabaseHelper( c, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS);
        if( databaseHelper != null ) {
            database = databaseHelper.getWritableDatabase();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if( ! initializeDB() ) {

            return 0;
        }

        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case SENSOR_DEV:
                database.beginTransaction();
                count = database.delete(DATABASE_TABLES[0], selection,
                        selectionArgs);
                database.setTransactionSuccessful();
                database.endTransaction();
                break;
            default:

                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case SENSOR_DEV:
                return Replay_Info.CONTENT_TYPE;
            case SENSOR_DEV_ID:
                return Replay_Info.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if( ! initializeDB() ) {
            return null;
        }

        ContentValues values = (initialValues != null) ? new ContentValues(
                initialValues) : new ContentValues();

        switch (sUriMatcher.match(uri)) {
            case SENSOR_DEV:
                database.beginTransaction();
                long accel_id = database.insertWithOnConflict(DATABASE_TABLES[0],
                        Replay_Info.TIMESTAMP, values, SQLiteDatabase.CONFLICT_IGNORE);
                database.setTransactionSuccessful();
                database.endTransaction();
                if (accel_id > 0) {
                    Uri accelUri = ContentUris.withAppendedId(
                            Replay_Info.CONTENT_URI, accel_id);
                    getContext().getContentResolver().notifyChange(accelUri, null);
                    return accelUri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if( ! initializeDB() ) {
            return 0;
        }

        int count = 0;
        switch ( sUriMatcher.match(uri) ) {
            case SENSOR_DEV:
                database.beginTransaction();
                for (ContentValues v : values) {
                    long id;
                    try {
                        id = database.insertOrThrow( DATABASE_TABLES[0], Replay_Info.TIMESTAMP, v );
                    } catch ( SQLException e ) {
                        id = database.replace( DATABASE_TABLES[0], Replay_Info.TIMESTAMP, v );
                    }
                    if( id <= 0 ) {
                        Log.w("Light.TAG", "Failed to insert/replace row into " + uri);
                    } else {
                        count++;
                    }
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public boolean onCreate() {

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Replay_Provider.AUTHORITY, DATABASE_TABLES[0],
                SENSOR_DEV);
        sUriMatcher.addURI(Replay_Provider.AUTHORITY, DATABASE_TABLES[0] + "/#",
                SENSOR_DEV_ID);


        sensorMap = new HashMap<String, String>();
        sensorMap.put(Replay_Info._ID, Replay_Info._ID);
        sensorMap.put(Replay_Info.TIMESTAMP, Replay_Info.TIMESTAMP);
        sensorMap.put(Replay_Info.DAY, Replay_Info.DAY);
        sensorMap.put(Replay_Info.MONTH, Replay_Info.MONTH);
        sensorMap.put(Replay_Info.YEAR, Replay_Info.YEAR);
        sensorMap.put(Replay_Info.REPLAYNAME, Replay_Info.REPLAYNAME);
        sensorMap.put(Replay_Info.APPNAME, Replay_Info.APPNAME);
        sensorMap.put(Replay_Info.VIDEO, Replay_Info.VIDEO);
        sensorMap.put(Replay_Info.ORIENTATION, Replay_Info.ORIENTATION);
        sensorMap.put(Replay_Info.MODE, Replay_Info.MODE);
        sensorMap.put(Replay_Info.STATUS, Replay_Info.STATUS);


        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        if( ! initializeDB() ) {
            return null;
        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case SENSOR_DEV:
                qb.setTables(DATABASE_TABLES[0]);
                qb.setProjectionMap(sensorMap);
                break;
            default:

                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            Cursor c = qb.query(database, projection, selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } catch (IllegalStateException e) {

            Log.e("Aware.TAG", e.getMessage());

            return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        if( ! initializeDB() ) {

            return 0;
        }

        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case SENSOR_DEV:
                database.beginTransaction();
                count = database.update(DATABASE_TABLES[0], values, selection,
                        selectionArgs);
                database.setTransactionSuccessful();
                database.endTransaction();
                break;

            default:

                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


}