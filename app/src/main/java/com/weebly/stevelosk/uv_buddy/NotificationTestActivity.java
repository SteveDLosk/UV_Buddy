package com.weebly.stevelosk.uv_buddy;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.content.Context.ALARM_SERVICE;

public class NotificationTestActivity extends BroadcastReceiver implements iAsyncCalling {

    private String zipCode;
    private Integer uvIndex;
    private String TAG = "NotifTestActivity";
    private Context mContext;

    private int notificationID;
    private NotificationManager notificationManager;
    private Notification.Builder builder;
    private Intent callingIntent;

    // evaluation logic
    private boolean needsToBeLower;
    private int targetIndex;


    @Override
    public void onReceive(Context context, Intent intent) {

        // Saves the application context, get the data, and sets the logic
        mContext = context;
        targetIndex = intent.getIntExtra("targetIndex", -1);
        needsToBeLower = intent.getBooleanExtra("needsToBeLower", false);
        zipCode = intent.getStringExtra("zipCode");
        callingIntent = intent;


        // This checks the index.  The result is passed to update.  From update, if the
        // index meets the logic requirements, a notification is created and fired.
        checkIndex();
    }

    private void createNotification () {
        builder =
                new Notification.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_alarm_black_24dp)
                .setAutoCancel(true)
                .setContentTitle("Notification")
                .setContentText("The UV index in " + zipCode + " is " + uvIndex);

        notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());
    }

    private void checkIndex () {

        iAsyncCalling callingActivity = NotificationTestActivity.this;

        GetUV_IndexAsync2 task = new GetUV_IndexAsync2 (mContext, callingActivity, zipCode);
        task.execute();

    }

    @Override
    public void update(Integer result) {
        uvIndex = result;

        // logic for whether to alert the user with a notification
        // compares result to targetIndex, and whether it should be greater or less


         if (result <= targetIndex && needsToBeLower  ||
                 result >= targetIndex && !needsToBeLower) {

            createNotification();
                // Cancel alarm
                cancelAlarm();

         }

    }

    private void cancelAlarm () {
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 1, callingIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    @Override
    public void reportLoading() {

    }


}
