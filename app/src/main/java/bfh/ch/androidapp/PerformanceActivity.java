package bfh.ch.androidapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.SecureRandom;

import project.two.crypto.GroupSignHelper;
import project.two.crypto.GroupSignMemberKey;
import project.two.crypto.GroupSignPublicKey;
import project.two.crypto.GroupSignSignature;

/**
 * Created by Pascal on 23.05.2017.
 */

public class PerformanceActivity extends Activity {
    private Button mButton1;
    private Button mButton10;
    private Button mButton15;
    private Button mButton20;
    private Button mButton30;
    private Button mButton100;

    private GroupSignPublicKey vk;
    private GroupSignMemberKey sk;
    private SecureRandom rand;
    private Handler mHandler = new Handler();
    private String publicKey;
    private String privateKey;

    private long start;
    private long stop;
    private byte[] message;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        this.mButton1 = (Button) findViewById(R.id.SimulateOne);
        this.mButton10 = (Button) findViewById(R.id.SimulateThen);
        this.mButton15 = (Button) findViewById(R.id.Simulate15);
        this.mButton20 = (Button) findViewById(R.id.Simulate20);
        this.mButton30 = (Button) findViewById(R.id.Simulate30);
        this.mButton100 = (Button) findViewById(R.id.SimulateHundred);

        this.publicKey = getString(R.string.GroupSignPublicKey);
        this.privateKey = getString(R.string.GroupSignMemberKey);
        this.message = getString(R.string.DemoData).getBytes();

        this.mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                // Start lengthy operation in a background thread
                getOpThread(1).start();

            }
        });

        this.mButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                // Start lengthy operation in a background thread
                getOpThread(10).start();

            }
        });

        this.mButton15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                // Start lengthy operation in a background thread
                getOpThread(15).start();

            }
        });

        this.mButton20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                // Start lengthy operation in a background thread
                getOpThread(20).start();

            }
        });
        this.mButton30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                // Start lengthy operation in a background thread
                getOpThread(30).start();

            }
        });
        this.mButton100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                // Start lengthy operation in a background thread
                getOpThread(100).start();

            }
        });
    }

    private Thread getOpThread(int repeat) {
        return  new Thread(new Runnable() {
            public void run() {
                Message msg = handler.obtainMessage();
                Bundle data = new Bundle();
                data.putString("message", "start");
                msg.setData(data);
                handler.sendMessage(msg);

                rand = new SecureRandom();
                Gson gson = new Gson();
                vk = gson.fromJson(publicKey, GroupSignPublicKey.class);
                sk = gson.fromJson(privateKey, GroupSignMemberKey.class);


                start = System.currentTimeMillis();
                for(int i = 0; i < repeat; i++) {
                    GroupSignSignature gss = GroupSignHelper.sign(rand, message, sk, vk);
                }
                stop = System.currentTimeMillis();


                msg = handlerDone.obtainMessage();
                data = new Bundle();
                data.putString("message", "done");
                msg.setData(data);
                handlerDone.sendMessage(msg);
            }
        });
    }

    private final Handler handlerDone = new Handler() {
        public void handleMessage(Message msg) {
            float elapsedTime = (stop - start);
            Log.v("Time Meassure","S:" + (elapsedTime / 1000));
            Toast.makeText(getApplicationContext(), "ZeitMessung:" + (elapsedTime / 1000), Toast.LENGTH_LONG).show();
        }
    };
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), msg.getData().getString("message"), Toast.LENGTH_LONG).show();
        }
    };
}
