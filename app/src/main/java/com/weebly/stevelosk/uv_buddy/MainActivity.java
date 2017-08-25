package com.weebly.stevelosk.uv_buddy;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements iAsyncCalling {

    private TextView resultTextView;
    private TextView UV_indexDescriptionTextView;
    private EditText enterZipCodeEditText;
    private Button getUV_withZipCodeButton;
    private Button getUV_withLocationButton;
    private Toolbar mActionBar;

    private Integer[] uviArray;

    private String TAG = "Main Activity";
    private Resources res;

    private String mZipCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mActionBar);

        res = getResources();

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        UV_indexDescriptionTextView = (TextView) findViewById(R.id.UV_indexDescriptionTextView);
        enterZipCodeEditText = (EditText) findViewById(R.id.zipCodeEditText);
        getUV_withZipCodeButton = (Button) findViewById(R.id.getUV_withZipCodeButton);
        getUV_withLocationButton = (Button) findViewById(R.id.getUV_withLocationButton);

        mActionBar.inflateMenu(R.menu.menu);

        // Button Click listener
        getUV_withZipCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity callingActivity = MainActivity.this;
                // logic flag for no reading
                Integer currentUV_index = -1;

                // pass in current zipCode and hour
                mZipCode = enterZipCodeEditText.getText().toString();

                // get current hour
                String hour = getTime();

                // commented out old async task /*
                /*
                GetUV_IndexAsync task = new GetUV_IndexAsync(
                        callingActivity, zipCode, hour);
                task.execute();
                */

                GetUV_IndexAsync2 task = new GetUV_IndexAsync2(
                        getApplicationContext(), callingActivity, mZipCode);
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

    public void update(Integer index) {

        if (index == -1) {
            // got here because there was an error
            reportErrorToUser();

        } else {
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


            } catch (NullPointerException e) {
                reportErrorToUser();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showEpaScale:
                // User chose the "Settings" item, show the app settings UI...
                // Launch EPA scale

                String uriString = res.getString(R.string.epaScaleUri);
                Uri webUri = Uri.parse(uriString);
                Intent navToEpaIntent = new Intent(Intent.ACTION_VIEW);
                navToEpaIntent.setData(webUri);
                startActivity(navToEpaIntent);

                return true;

            case R.id.setAlarmIcon:
                Intent setAlarmActionIntent = new Intent(this, NewAlarmActivity.class);
                setAlarmActionIntent.putExtra("zipCode", mZipCode);
                startActivity(setAlarmActionIntent);
                return true;

            case R.id.showAboutThisApp:
                Intent showAboutIntent = new Intent(this, AboutThisAppActivity.class);
                startActivity(showAboutIntent);
                return true;

            default:
                return true;
        }
    }

    private void reportErrorToUser() {
        // clear old index number
        resultTextView.setText("Something\nwent\nwrong");
        // clear old index color and info
        UV_indexDescriptionTextView.setText("");
        resultTextView.setBackgroundColor(Color.WHITE);

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

    public void reportLoading () {
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
