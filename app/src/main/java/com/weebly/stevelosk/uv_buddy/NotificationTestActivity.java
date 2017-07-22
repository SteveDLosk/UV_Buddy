package com.weebly.stevelosk.uv_buddy;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class NotificationTestActivity extends AppCompatActivity implements iAsyncCalling {

    private String zipCode = "98391";
    private Integer uvIndex;
    private String TAG = "NotifTestActivity";

    private int notificationID;
    private NotificationManager notificationManager;
    private Notification.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_notification_test);

        checkIndex();

    }

    private void createNotification () {
        Log.i(TAG, "entered createNotification");
        builder =
                new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_alarm_black_24dp)
                .setAutoCancel(true)
                .setContentTitle("Notification")
                .setContentText("The UV index in " + zipCode + " is " + uvIndex);

        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());
    }

    private void checkIndex () {

        Log.i(TAG, "entered checkIndex");
        Context mContex = getApplicationContext();
        iAsyncCalling callingActivity = NotificationTestActivity.this;

        GetUV_IndexAsync2 task = new GetUV_IndexAsync2 (mContex, callingActivity, zipCode);
        task.execute();

    }

    @Override
    public void update(Integer result) {
        Log.i(TAG, "entered update");
        uvIndex = result;

        //TODO: logic for whether to alert the user with a notification
        // if (shouldAlert) {...
        createNotification();
        // }

    }

    @Override
    public void reportLoading() {

    }
}
