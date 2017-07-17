package com.weebly.stevelosk.uv_buddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class NewAlarmActivity extends AppCompatActivity {

    private TextView mCurrentIndexTextView;
    private Spinner mSelectIndexSpinner;
    private Button mSetAlarmButton;
    private String mZipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        mCurrentIndexTextView = (TextView) findViewById(R.id.setAlarmCurrentIndexTextView);
        mSelectIndexSpinner = (Spinner) findViewById(R.id.setAlarmSelectSpinner);
        mSetAlarmButton = (Button) findViewById(R.id.setAlarmButton);

        // Bring in zipcode
        Intent callingIntent = getIntent();
        mZipCode = callingIntent.getStringExtra("zipCode");

        // get current index
        GetUV_IndexAsync2 task = new GetUV_IndexAsync2(
                this, mZipCode);
        task.execute();
    }
}
