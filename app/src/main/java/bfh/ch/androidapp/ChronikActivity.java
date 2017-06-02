package bfh.ch.androidapp;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import bfh.ch.androidapp.database.GpsDbHelper;

/**
 * Created by Pascal on 17.04.2017.
 */

public class ChronikActivity extends Activity {

    private SimpleCursorAdapter adapter;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronik);

        String[] projection = {
                GpsDbHelper.GpsEntry.COLUMN_NAME_SESSION_TITLE,
                GpsDbHelper.GpsEntry.COLUMN_NAME_LATITUDE,
                GpsDbHelper.GpsEntry.COLUMN_NAME_LONGITUDE,
                GpsDbHelper.GpsEntry.COLUMN_NAME_ID
        };

        GpsDbHelper gpsDbHelper = new GpsDbHelper(getBaseContext());
        db = gpsDbHelper.getWritableDatabase();

        Cursor cursor = db.query(
                GpsDbHelper.GpsEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        String[] from = new String[] { GpsDbHelper.GpsEntry.COLUMN_NAME_LATITUDE, GpsDbHelper.GpsEntry.COLUMN_NAME_LONGITUDE,  GpsDbHelper.GpsEntry.COLUMN_NAME_SESSION_TITLE };
        int[] to = new int[] { R.id.llatitude, R.id.llongitude, R.id.session};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.row_chronic,
                cursor,
                from, to, 0);

        ListView listView = (ListView) findViewById(R.id.chronicList);
        listView.setAdapter(adapter);
    }
}
