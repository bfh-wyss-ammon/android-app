package bfh.ch.androidapp.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import bfh.ch.androidapp.database.GpsDbHelper;

/**
 * Created by Pascal on 09.04.2017.
 */

public class MyService extends Service implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String Tag = "MyService";
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 50;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 50;
    private GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    private Location mLastLocation;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private PendingResult<Status> mStaus;
    private SQLiteDatabase sqLiteDatabase;
    private long mSessionId;

    @Override
    public void onCreate() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        GpsDbHelper gpsDbHelper = new GpsDbHelper(getBaseContext());
        sqLiteDatabase = gpsDbHelper.getWritableDatabase();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues initialValues = new ContentValues();
        initialValues.put(GpsDbHelper.SessionEntry.COLUMN_NAME_START, dateFormat.format( new Date()));
        mSessionId = sqLiteDatabase.insert(GpsDbHelper.SessionEntry.TABLE_NAME, null, initialValues);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        //todo update
        //SELECT last_insert_rowid()
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues initialValues = new ContentValues();
        initialValues.put(GpsDbHelper.SessionEntry.COLUMN_NAME_END, dateFormat.format( new Date()));
        long r = sqLiteDatabase.update(GpsDbHelper.SessionEntry.TABLE_NAME, initialValues, "_id="+mSessionId, null);

        sqLiteDatabase.close();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onConnected(@Nullable Bundle bundle) {
        mStaus = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "google api connection failed. check permissions!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(Tag, "GPS:" + location.getLongitude() + "/" + location.getLatitude());
        ContentValues values = new ContentValues();
        values.put(GpsDbHelper.GpsEntry.COLUMN_NAME_SESSION_TITLE, mSessionId);
        values.put(GpsDbHelper.GpsEntry.COLUMN_NAME_LONGITUDE, location.getLongitude());
        values.put(GpsDbHelper.GpsEntry.COLUMN_NAME_LATITUDE, location.getLatitude());
        long newRowId = sqLiteDatabase.insert(GpsDbHelper.GpsEntry.TABLE_NAME, null, values);
    }
}
