package com.example.saveinstancestate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final String TAG = "myLogs";
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");


        // myObj = (MyObject) getLastNonConfigurationInstance();
    }

    protected void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);
        cnt = saveInstanceState.getInt("count");
        Log.d(TAG, "onRestoreInstanceState");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count", cnt);
        Log.d(TAG, "onSaveInstanceState");
    }

//    public Object onRetainNonConfigurationInstance() {
//        return myObj;
//    }

    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public void onClick(View v) {
        Toast.makeText(this, "COUNT = " + ++cnt, Toast.LENGTH_SHORT).show();
    }
}
