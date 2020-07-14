package com.example.p0951servicebackpendingintent;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "myLogs";

    private final int TASK1_CODE = 1;
    private final int TASK2_CODE = 2;
    private final int TASK3_CODE = 3;

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    public final static String PARAM_TIME = "time";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";

    private TextView tvTask1, tvTask2, tvTask3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTask1 = findViewById(R.id.tvTask1);
        tvTask2 = findViewById(R.id.tvTask2);
        tvTask3 = findViewById(R.id.tvTask3);
        tvTask1.setText("Task1");
        tvTask2.setText("Task2");
        tvTask3.setText("Task3");
    }

    public void onClickStart(View view) {
        PendingIntent pendingIntent;
        Intent intent;
        Intent emptyIntent = new Intent();

        // Creating PendingIntent for Task1
        pendingIntent = createPendingResult(TASK1_CODE, emptyIntent, 0);
        // Создаем Intent для вызова сервиса, кладем туда параметр времени
        // и созданный PendingIntent
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 7)
                .putExtra(PARAM_PINTENT, pendingIntent);
        // Стуртуем сервис
        startService(intent);

        pendingIntent = createPendingResult(TASK2_CODE, emptyIntent, 0);
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 4)
                .putExtra(PARAM_PINTENT, pendingIntent);
        startService(intent);

        pendingIntent = createPendingResult(TASK3_CODE, emptyIntent, 0);
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 6)
                .putExtra(PARAM_PINTENT, pendingIntent);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode = " + requestCode + ", resultCode = " + resultCode);

        if (resultCode == STATUS_START) {
            switch (requestCode) {
                case TASK1_CODE:
                    tvTask1.setText("Task1 start");
                    break;
                case TASK2_CODE:
                    tvTask2.setText("Task2 start");
                    break;
                case TASK3_CODE:
                    tvTask3.setText("Task3 start");
                    break;
            }
        }

        if (resultCode == STATUS_FINISH) {
            int result = data.getIntExtra(PARAM_RESULT, 0);
            switch (requestCode) {
                case TASK1_CODE:
                    tvTask1.setText("Task1 finish, result = " + result);
                    break;
                case TASK2_CODE:
                    tvTask2.setText("Task2 finish, result = " + result);
                    break;
                case TASK3_CODE:
                    tvTask3.setText("Task3 finish, result = " + result);
            }
        }
    }
}
