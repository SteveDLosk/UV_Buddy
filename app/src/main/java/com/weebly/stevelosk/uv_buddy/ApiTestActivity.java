package com.weebly.stevelosk.uv_buddy;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ApiTestActivity extends AppCompatActivity {

    private TextView testResultTextView;
    private EditText testZipCodeEditText;
    private Button testGetUV_IndexButton;
    private Button testUseLocationServiceButton;

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
                GetUV_IndexAsync task = new GetUV_IndexAsync(callingActivity);
                task.execute();


            }
        });

    }

    protected void update(Integer index) {
        testResultTextView.setText(index.toString());
    }
}
