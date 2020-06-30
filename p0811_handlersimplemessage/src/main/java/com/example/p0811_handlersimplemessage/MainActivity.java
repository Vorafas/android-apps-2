package com.example.p0811_handlersimplemessage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    final int STATUS_NONE = 0;
    final int STATUS_CONNECTING = 1;
    final int STATUS_CONNECTED = 2;

    Handler handler;

    TextView tvStatus;
    ProgressBar pbConnect;
    Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        pbConnect = findViewById(R.id.pbConnect);
        btnConnect = findViewById(R.id.btnConnect);

        handler = new MyHandler(this);
        handler.sendEmptyMessage(STATUS_NONE);
    }

    void checkStatus(Message msg) {
        switch (msg.what) {
            case STATUS_NONE:
                btnConnect.setEnabled(true);
                tvStatus.setText("Not connected");
                break;
            case STATUS_CONNECTING:
                btnConnect.setEnabled(false);
                pbConnect.setVisibility(View.VISIBLE);
                tvStatus.setText("Connecting");
                break;
            case STATUS_CONNECTED:
                pbConnect.setVisibility(View.GONE);
                tvStatus.setText("Connected");
                break;
        }
    }

    public void onClick(View v) {
        Thread thrd = new Thread(() -> {
            try {
                handler.sendEmptyMessage(STATUS_CONNECTING);
                TimeUnit.SECONDS.sleep(2);

                handler.sendEmptyMessage(STATUS_CONNECTED);
                TimeUnit.SECONDS.sleep(3);

                handler.sendEmptyMessage(STATUS_NONE);
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        });

        thrd.start();
    }

    static class MyHandler extends Handler {
        WeakReference<MainActivity> wrActivity;

        public MyHandler(MainActivity activity) {
            wrActivity = new WeakReference<MainActivity>(activity);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = wrActivity.get();
            if (activity != null) {
                activity.checkStatus(msg);
            }
        }
    }
}
