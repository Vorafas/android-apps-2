package com.example.p0951servicebackpendingintent;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    private final String LOG_TAG = "myLogs";
    private ExecutorService executorService;

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService onCreate");
        executorService = Executors.newFixedThreadPool(2);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MyService onDestroy");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService onStartCommand");

        int time = intent.getIntExtra(MainActivity.PARAM_TIME, 1);
        PendingIntent pendingIntent = intent.getParcelableExtra(MainActivity.PARAM_PINTENT);

        MyRun myRun = new MyRun(time, startId, pendingIntent);
        executorService.execute(myRun);

        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyRun implements Runnable {
        private int time, startId;

        private PendingIntent pendingIntent;

        public MyRun(int time, int startId, PendingIntent pendingIntent) {
            this.time = time;
            this.startId = startId;
            this.pendingIntent = pendingIntent;

            Log.d(LOG_TAG, "MyRun#" + startId + " create");
        }

        public void run() {
            Log.d(LOG_TAG, "MyRun#" + startId + " start, time = " + time);
            try {
                // сообщаем о старте задачи
                pendingIntent.send(MainActivity.STATUS_START);

                // Начинаем выполнение задачи
                TimeUnit.SECONDS.sleep(time);

                // сообщение об окончание задачи
                Intent intent = new Intent().putExtra(MainActivity.PARAM_RESULT, time * 100);
                pendingIntent.send(MyService.this, MainActivity.STATUS_FINISH, intent);
            } catch (InterruptedException | PendingIntent.CanceledException exc) {
                exc.printStackTrace();
            }
            stop();
        }

        private void stop() {
            Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelfResult("
                    + startId + ") = " + stopSelfResult(startId));
        }
    }
}
