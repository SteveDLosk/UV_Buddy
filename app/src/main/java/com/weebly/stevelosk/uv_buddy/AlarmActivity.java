package com.weebly.stevelosk.uv_buddy;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

import java.util.Calendar;

/**
 * Created by steve on 7/16/2017.
 * Schedules a recurring UV index check
 */

public class AlarmActivity extends Activity {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Context mContext;
    private String mZipcode;
    private Calendar mCalandar;

    // instance variables for index data
    private boolean hasHitTargetIndex;
    private boolean alertWhenLowEnough;
    private boolean alertWhenTooHigh;
    private Integer mTargetIndex;
    private Integer mCurrentIndex;

    private final long HALFHOUR = 30 * 60 * 1000;

    public AlarmActivity (Context appContext, int targetIndex, int mCurrentIndex, String zipCode) {
        mContext = appContext;
        mZipcode = zipCode;
        mTargetIndex = targetIndex;
        alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mCalandar = Calendar.getInstance();

        // logic
        hasHitTargetIndex = false;
        alertWhenLowEnough = false;
        alertWhenTooHigh = false;

        if (mCurrentIndex < targetIndex) {
            alertWhenTooHigh = true;
        }
        else if (mCurrentIndex > targetIndex) {
            alertWhenTooHigh = false;
        }


       // Intent intent = new Intent(mContext, new AlarmReceiver.class(mZipcode, mCurrentIndex));
       // alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

        /*
        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);
        */

        // setRepeating() lets you specify a precise custom interval--

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, mCalandar.getTimeInMillis(),
                HALFHOUR, alarmIntent);
    }

    private void update (Integer result) {

        mCurrentIndex = result;

        if (mCurrentIndex <= mTargetIndex && alertWhenLowEnough) {
            hasHitTargetIndex = true;
        }
        else if (mCurrentIndex >= mTargetIndex && alertWhenTooHigh) {
            hasHitTargetIndex = true;
        }

    }
}
