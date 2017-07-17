package com.weebly.stevelosk.uv_buddy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class NewAlarmActivity extends AppCompatActivity implements iAsyncCalling {

    private TextView mCurrentIndexTextView;
    private Spinner mSelectIndexSpinner;
    private Button mSetAlarmButton;
    private String mZipCode;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        mContext = getApplicationContext();

        mCurrentIndexTextView = (TextView) findViewById(R.id.setAlarmCurrentIndexTextView);
        mSelectIndexSpinner = (Spinner) findViewById(R.id.setAlarmSelectSpinner);
        mSetAlarmButton = (Button) findViewById(R.id.setAlarmButton);

        // Bring in zipcode
        Intent callingIntent = getIntent();
        mZipCode = callingIntent.getStringExtra("zipCode");

        // get current index
        if (mZipCode != null) {
            GetUV_IndexAsync2 task = new GetUV_IndexAsync2(
                    mContext, this, mZipCode);
            task.execute();
        }
    }

    @Override
    public void update(Integer result) {

        mCurrentIndexTextView.setText(result.toString());
    }

    @Override
    public void reportLoading() {
        mCurrentIndexTextView.setText(R.string.loading);
    }
}
