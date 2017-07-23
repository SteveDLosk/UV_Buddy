package com.weebly.stevelosk.uv_buddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class NewAlarmActivity extends AppCompatActivity implements iAsyncCalling {

    private final String TAG = "NewAlarmActivity";
    private TextView mCurrentIndexTextView;
    private Spinner mSelectIndexSpinner;
    private Button mSetAlarmButton;
    private String mZipCode;
    private Context mContext;

    // alarm stuff
    private AlarmManager mAlarmManager;
    private PendingIntent pendingIntent;
    private final long TWO_MINUTES = 2 * 60 * 1000;
    private final long TWENTY_MINUTES = 20 * 60 * 1000;
    private final long HALF_HOUR = 30 * 60 * 1000;
    private final long HOUR = 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        mContext = getApplicationContext();

        mCurrentIndexTextView = (TextView) findViewById(R.id.setAlarmCurrentIndexTextView);
        mSelectIndexSpinner = (Spinner) findViewById(R.id.setAlarmSelectSpinner);
        mSetAlarmButton = (Button) findViewById(R.id.setAlarmButton);

        // Get current UV index, this is a point of reference for the alarm parameters
        // Bring in zipcode
        Intent callingIntent = getIntent();
        mZipCode = callingIntent.getStringExtra("zipCode");

        checkUV_Index();

        // Button handler to set alarm
        mSetAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scheduleAlarm();

            }
        });
    }

    private void scheduleAlarm()
    {

        // The time at which the alarm will be scheduled. Here the alarm is scheduled for 1 day from the current time.
        // We fetch the current time in milliseconds and add time
        // i.e. 24*60*60*1000 = 86,400,000 milliseconds in a day.
        Long time = new GregorianCalendar().getTimeInMillis()+TWO_MINUTES;

        // Create an Intent and set the class that will execute when the Alarm triggers. Here we have
        // specified AlarmReceiver in the Intent. The onReceive() method of this class will execute when the broadcast from your alarm is received.
        Intent intentAlarm = new Intent(this, NotificationTestActivity.class);

        // Get the Alarm Service.
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,
                TWENTY_MINUTES, PendingIntent.getBroadcast(this, 1, intentAlarm,
                        PendingIntent.FLAG_UPDATE_CURRENT));

        // TODO: set repeating?
        Toast.makeText(this, "Alarm Scheduled for two minutes", Toast.LENGTH_LONG).show();
    }

    @Override
    public void update(Integer result) {

        mCurrentIndexTextView.setText(result.toString());

    }

    private void checkUV_Index () {
        // get current index
        if (mZipCode != null) {
            GetUV_IndexAsync2 task = new GetUV_IndexAsync2(
                    mContext, this, mZipCode);
            task.execute();
        }
    }

    @Override
    public void reportLoading() {
        mCurrentIndexTextView.setText(R.string.loading);
    }
}
