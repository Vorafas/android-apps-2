package com.example.p0841_handlerrunnable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ProgressBar pbCount;
    TextView tvInfo;
    CheckBox chbInfo;

    int count;

    final String LOG_TAG = "myLogs";
    final int max = 100;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        pbCount = findViewById(R.id.pbCount);
        pbCount.setMax(max);
        pbCount.setProgress(0);

        tvInfo = findViewById(R.id.tvInfo);

        chbInfo = findViewById(R.id.chbInfo);
        chbInfo.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                tvInfo.setVisibility(View.VISIBLE);
                handler.post(showInfo);
            } else {
                tvInfo.setVisibility(View.GONE);
                handler.removeCallbacks(showInfo);
            }
        });

        Thread thrd = new Thread(() -> {
            try {
                for (count = 1; count < max; count++) {
                    TimeUnit.MILLISECONDS.sleep(100);
                    handler.post(updateProgress);
                }
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        });
        thrd.start();
    }

    Runnable updateProgress = () -> {
        pbCount.setProgress(count);
    };

    Runnable showInfo = () -> {
        Log.d(LOG_TAG, "showInfo");
        tvInfo.setText("Count = " + count);
        handler.postDelayed(this.showInfo, 1000);
    };
}
