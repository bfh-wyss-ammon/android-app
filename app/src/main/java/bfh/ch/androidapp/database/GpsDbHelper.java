package bfh.ch.androidapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class GpsDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_GPS_TABLE =
            "CREATE TABLE " + GpsEntry.TABLE_NAME + " (" +
                    GpsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    GpsEntry.COLUMN_NAME_SESSION_TITLE + " INTEGER," +
                    GpsEntry.COLUMN_NAME_LONGITUDE + " TEXT," +
                    GpsEntry.COLUMN_NAME_LATITUDE + " TEXT)";

    private static final String SQL_DELETE_GPS_TABLE =
            "DROP TABLE IF EXISTS " + GpsEntry.TABLE_NAME;

    private static final String SQL_CREATE_SESSION_TABLE =
            "CREATE TABLE " + SessionEntry.TABLE_NAME + " (" +
                    SessionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    SessionEntry.COLUMN_NAME_START + " TEXT," +
                    SessionEntry.COLUMN_NAME_END + " TEXT)";

    private static final String SQL_DELETE_SESSION_TABLE =
            "DROP TABLE IF EXISTS " + SessionEntry.TABLE_NAME;

    public static class GpsEntry implements BaseColumns {
        public static final String TABLE_NAME = "gps";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_SESSION_TITLE = "session";
        public static final String COLUMN_NAME_ID = "_id";
    }

    public static class SessionEntry implements BaseColumns {
        public static final String TABLE_NAME = "session";
        public static final String COLUMN_NAME_START = "start";
        public static final String COLUMN_NAME_END = "end";
        public static final String COLUMN_NAME_ID = "_id";
    }

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "gps_data2.db";

    public GpsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_GPS_TABLE);
        db.execSQL(SQL_CREATE_SESSION_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_GPS_TABLE);
        db.execSQL(SQL_DELETE_SESSION_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
