package com.example.p0911_asynctaskrotate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    MyTask mt;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "create MainActivity: " + this.hashCode());

        tv = findViewById(R.id.tv);

        mt = (MyTask) getLastNonConfigurationInstance();
        if (mt == null) {
            mt = new MyTask();
            mt.execute();
        }
        mt.link(this);
        Log.d(LOG_TAG, "create MyTask: " + mt.hashCode());
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        mt.unLink();
        return mt;
    }

    static class MyTask extends AsyncTask<String, Integer, Void> {
        MainActivity activity;
        String str = "";

        void link(MainActivity activity) {
            this.activity = activity;
            activity.tv.setText(str);
        }

        void unLink() {
            activity = null;
        }

        protected Void doInBackground(String... params) {
            try {
                for (int i = 1; i <= 10; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    publishProgress(i);
                    Log.d(activity.LOG_TAG, "i = " + i + ", MyTask: "
                            + this.hashCode() + ", MainActivity: "
                            + activity.hashCode());
                }
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (activity == null) {
                str = "i = " + values[0];
            } else {
                activity.tv.setText("i = " + values[0]);
            }
        }
    }
}
