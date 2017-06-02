package bfh.ch.androidapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import bfh.ch.androidapp.database.GpsDbHelper;
import bfh.ch.androidapp.services.MyService;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private TextView mMessageView;
    private boolean mHasGrand;
    private Intent mServiceIntent;
    private boolean mTrackingOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.mButton = (Button) findViewById(R.id.checkInButton);
        this.mMessageView = (TextView) findViewById(R.id.messageView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        } else {
            mHasGrand = true;
        }


        this.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHasGrand) {
                    if (mTrackingOn) {
                        stopService(mServiceIntent);
                        mMessageView.setText("checked-out");
                        mButton.setText(R.string.check_in);
                    } else {
                        mServiceIntent = new Intent(getApplicationContext(), MyService.class);
                        startService(mServiceIntent);
                        mButton.setText(R.string.check_out);
                        mMessageView.setText("checked-in");
                    }
                    mTrackingOn = !mTrackingOn;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mmain, menu);
        return true;
    }

    protected void onStop() {
        if (mHasGrand) {
            //stopService(mServiceIntent);
        }
        super.onStop();
    }

    public boolean onOptionsItemSelected(MenuItem item ){
        switch( item.getItemId() ){
            case R.id.chronik:
                show_chronic();
                break;
            case R.id.performance:
                show_performance();
                break;
            default:
                Toast.makeText(this, "That's not an option, moron!", Toast.LENGTH_LONG).show();
                return false;
        }
        return true;
    }

    private void show_performance() {
        Intent i = new Intent(this, PerformanceActivity.class);
        startActivity(i);
    }

    private void show_chronic() {
        Intent i = new Intent(this, ChronikActivity.class);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mHasGrand = true;
                }
                return;
            }
        }
    }
}