package com.weebly.stevelosk.uv_buddy;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private TextView UV_indexDescriptionTextView;
    private EditText enterZipCodeEditText;
    private Button getUV_withZipCodeButton;
    private Button getUV_withLocationButton;

    private Integer[] uviArray;

    private String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        UV_indexDescriptionTextView = (TextView) findViewById(R.id.UV_indexDescriptionTextView);
        enterZipCodeEditText = (EditText) findViewById(R.id.zipCodeEditText);
        getUV_withZipCodeButton = (Button) findViewById(R.id.getUV_withZipCodeButton);
        getUV_withLocationButton = (Button) findViewById(R.id.getUV_withLocationButton);

        // Button Click listener
        getUV_withZipCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity callingActivity = MainActivity.this;
                // logic flag for no reading
                Integer currentUV_index = -1;

                // pass in current zipCode and hour
                String zipCode = enterZipCodeEditText.getText().toString();

                // get current hour
                String hour = getTime();

                // commented out old async task /*
                /*
                GetUV_IndexAsync task = new GetUV_IndexAsync(
                        callingActivity, zipCode, hour);
                task.execute();
                */

                GetUV_IndexAsync2 task = new GetUV_IndexAsync2(
                        callingActivity, zipCode, hour);
                task.execute();


            }
        });
    }

    protected void updateArray(Integer[] data) {
        this.uviArray = data;
        Log.i(TAG, "Array data: ");
        for (int i = 0; i < 24; i++) {
            Log.i(TAG, uviArray[i].toString());
        }
    }

    protected void update(Integer index) {
        Log.i(TAG, "entered update");
        Log.i(TAG, String.valueOf(index));
        try {
            resultTextView.setText(index.toString());
            // Get resources
            Resources res = getResources();

            // Get description
            String[] desc_array = res.getStringArray(R.array.UV_index_description_array);
            String description = desc_array[index];
            UV_indexDescriptionTextView.setText(description);

            // set background color
            updateColor(index, res);


        }
        catch (NullPointerException e) {
            // clear old index number
            resultTextView.setText("Something\nwent\nwrong");
            // clear old index color and info
            UV_indexDescriptionTextView.setText("");
            resultTextView.setBackgroundColor(Color.WHITE);

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

    protected void reportLoading () {
        resultTextView.setText("loading");
    }

    private void updateColor (int index, Resources res) {
        // Possible index overflow
        if (index > 11) {
            index = 11;
        }
        String[] colorStringArray = res.getStringArray(R.array.UV_color_code_array);
        String colorString = colorStringArray[index];
        resultTextView.setBackgroundColor(Color.parseColor(colorString));

    }
}
