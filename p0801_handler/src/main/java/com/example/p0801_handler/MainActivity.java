package com.example.p0801_handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    Handler handle;
    TextView tvInfo;
    Button btnStart;
    ProgressBar pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.tvInfo);
        pgBar = findViewById(R.id.pgBar);
        btnStart = findViewById(R.id.btnStart);
        handle = new MyHandler(this);
    }

    void someMethod(Message msg) {
        tvInfo.setText("Закачено файлов: " + msg.what);
        if (msg.what == 10) {
            btnStart.setEnabled(true);
            pgBar.setVisibility(View.INVISIBLE);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                btnStart.setEnabled(false);
                pgBar.setVisibility(View.VISIBLE);
                Thread thrd = new Thread(() -> {
                    for (int i = 0; i <= 10; i++) {
                        downloadFile();
                        handle.sendEmptyMessage(i);
                        Log.d(LOG_TAG, "Закачено файлов: " + i);
                    }
                });
                thrd.start();
                break;
            case R.id.btnTest:
                pgBar.setVisibility(View.VISIBLE);
                Log.d(LOG_TAG, "test");
                break;
        }
    }

    void downloadFile() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        }
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
                activity.someMethod(msg);
            }
        }
    }
}
