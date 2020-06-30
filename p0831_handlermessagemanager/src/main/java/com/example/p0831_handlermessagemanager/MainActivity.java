package com.example.p0831_handlermessagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    Handler handler;
    Handler.Callback handlerCallback = (Message msg) -> {
        Log.d(LOG_TAG, "what = " + msg.what);
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(handlerCallback);
        sendMessage();
    }

    void sendMessage() {
        Log.d(LOG_TAG, "send messages");

        handler.sendEmptyMessageDelayed(1, 1000);
        handler.sendEmptyMessageDelayed(2, 2000);
        handler.sendEmptyMessageDelayed(3, 3000);
        handler.sendEmptyMessageDelayed(2, 4000);
        handler.sendEmptyMessageDelayed(5, 5000);
        handler.sendEmptyMessageDelayed(2, 6000);
        handler.sendEmptyMessageDelayed(7, 7000);

        handler.removeMessages(2);
    }
}
