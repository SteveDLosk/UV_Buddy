package com.weebly.stevelosk.uv_buddy;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;

public class ApiTestActivity extends AppCompatActivity {

    private TextView testResultTextView;
    private EditText testZipCodeEditText;
    private Button testGetUV_IndexButton;
    private Button testUseLocationServiceButton;
    private final String TAG = "ApiTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);

        // create java objects from xml

        testResultTextView = (TextView) findViewById(R.id.testResultTextView);
        testZipCodeEditText = (EditText) findViewById(R.id.testZipCodeEditText);
        testGetUV_IndexButton = (Button) findViewById(R.id.testGetUVIndexButton);
        testUseLocationServiceButton = (Button) findViewById(R.id.testUseLocationService);

        // getUV index event handler
        testGetUV_IndexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApiTestActivity callingActivity = ApiTestActivity.this;
                Integer currentUV_index = -1;

                // pass in current zipcode and hour
                String zipCode = testZipCodeEditText.getText().toString();
                // TODO: get current hour
                String hour = getTime();
            //    GetUV_IndexAsync task = new GetUV_IndexAsync(
              //          callingActivity, zipCode, hour);
               // task.execute();


            }
        });

    }

    protected void update(Integer index) {
        try {
            testResultTextView.setText(index.toString());
        }
        catch (NullPointerException e) {
            testResultTextView.setText("Something went wrong");
        }
    }

    private String getTime () {
        Boolean PM = false;

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        // Format for AM / PM
        if (hour >= 12) {
            PM = true;
        }
        if (hour > 12) {
            hour -= 12;
        }

        // fix 12 AM
        if (hour == 0) {
            hour = 12;
        }

        // fix no JSON for before 4AM
        if (hour < 4 && !PM) {
            hour = 4;
        }

        // Turn to String
        String hourString = Integer.toString(hour);
        if (hour < 10) {
            hourString = "0" + hourString;
        }

        // formatting
        hourString += " ";
        if (PM) {
            hourString += "PM";
        }
        else {
            hourString += "AM";
        }

        return hourString;
    }
}
