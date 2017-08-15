package com.weebly.stevelosk.uv_buddy;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutThisAppActivity extends AppCompatActivity {

    private ImageView wunderGroundImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_this_app);

        wunderGroundImg = (ImageView) findViewById(R.id.wundergroundImg);
        wunderGroundImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to Wunderground, using referral
                Uri referralUri = Uri.parse("https://www.wunderground.com/?apiref=883a4071c708e116");
                Intent navToWundergroundIntent = new Intent(Intent.ACTION_VIEW);
                navToWundergroundIntent.setData(referralUri);
                startActivity(navToWundergroundIntent);
            }
        });
    }
}
