package com.example.p0891_asynctaskcancel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    MyTask mt;
    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.tvInfo);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                mt = new MyTask();
                mt.execute();
                break;
            case R.id.btnCancel:
                cancelTask();
                break;
        }
    }

    private void cancelTask() {
        if (mt == null) {
            return;
        }
        Log.d(LOG_TAG, "cancel result: " + mt.cancel(false));
    }

    class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Begin");
            Log.d(LOG_TAG, "Begin");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 5; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    if (isCancelled()) {
                        return null;
                    }
                    Log.d(LOG_TAG, "isCanceled: " + isCancelled());
                }
            } catch (InterruptedException exc) {
                Log.d(LOG_TAG, "Interrupted");
                exc.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tvInfo.setText("End");
            Log.d(LOG_TAG, "End");
        }

        protected void onCancelled() {
            super.onCancelled();
            tvInfo.setText("Canceled");
            Log.d(LOG_TAG, "Canceled");
        }
    }
}
