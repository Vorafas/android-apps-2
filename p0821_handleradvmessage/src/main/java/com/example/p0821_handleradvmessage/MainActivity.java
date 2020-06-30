package com.example.p0821_handleradvmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLog";

    final int STATUS_NONE = 0;
    final int STATUS_CONNECTING = 1;
    final int STATUS_CONNECTED = 2;
    final int STATUS_DOWNLOAD_START = 3;
    final int STATUS_DOWNLOAD_FILE = 4;
    final int STATUS_DOWNLOAD_END = 5;
    final int STATUS_DOWNLOAD_NONE = 6;

    Handler handler;

    TextView tvStatus;
    ProgressBar pbDownload;
    Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        pbDownload = findViewById(R.id.pbDownload);
        btnConnect = findViewById(R.id.btnConnect);

        handler = new MyHandler(this);
        handler.sendEmptyMessage(STATUS_NONE);
    }

    void onMessage(Message msg) {
        switch (msg.what) {
            case STATUS_NONE:
                btnConnect.setEnabled(true);
                tvStatus.setText("Not connected");
                pbDownload.setVisibility(View.GONE);
                break;
            case STATUS_CONNECTING:
                btnConnect.setEnabled(false);
                tvStatus.setText("Connecting");
                break;
            case STATUS_CONNECTED:
                tvStatus.setText("Connected");
            case STATUS_DOWNLOAD_START:
                tvStatus.setText("Start download " + msg.arg1 + " files");
                pbDownload.setMax(msg.arg1);
                pbDownload.setProgress(0);
                pbDownload.setVisibility(View.VISIBLE);
                break;
            case STATUS_DOWNLOAD_FILE:
                tvStatus.setText("Downloading. Left " + msg.arg2 + " files");
                pbDownload.setProgress(msg.arg1);
                saveFile((byte[]) msg.obj);
                break;
            case STATUS_DOWNLOAD_END:
                tvStatus.setText("Download complete!");
                break;
            case STATUS_DOWNLOAD_NONE:
                tvStatus.setText("No files for download");
                break;
        }
    }

    public void onClick(View v) {
        Thread thrd = new Thread(new Runnable() {
            Message msg;
            byte[] file;
            Random rand = new Random();

            @Override
            public void run() {
                try {
                    handler.sendEmptyMessage(STATUS_CONNECTING);
                    TimeUnit.SECONDS.sleep(1);

                    handler.sendEmptyMessage(STATUS_CONNECTED);

                    TimeUnit.SECONDS.sleep(1);
                    int filesCount = rand.nextInt(5);

                    if (filesCount == 0) {
                        handler.sendEmptyMessage(STATUS_DOWNLOAD_NONE);
                        TimeUnit.MILLISECONDS.sleep(1500);
                        handler.sendEmptyMessage(STATUS_NONE);
                        return;
                    }

                    msg = handler.obtainMessage(STATUS_DOWNLOAD_START, filesCount, 0);
                    handler.sendMessage(msg);

                    for (int i = 1; i <= filesCount; i++) {
                        file = downloadFile();
                        msg = handler.obtainMessage(STATUS_DOWNLOAD_FILE, i, filesCount - i, file);
                        handler.sendMessage(msg);
                    }
                    handler.sendEmptyMessage(STATUS_DOWNLOAD_END);
                    TimeUnit.MILLISECONDS.sleep(1500);
                    handler.sendEmptyMessage(STATUS_NONE);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        thrd.start();
    }

    static class MyHandler extends Handler {
        WeakReference<MainActivity> wrActivity;

        public MyHandler(MainActivity activity) {
            wrActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = wrActivity.get();
            if (activity != null) {
                activity.onMessage(msg);
            }
        }
    }

    byte[] downloadFile() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        return new byte[1024];
    }

    void saveFile(byte[] file) {

    }
}
