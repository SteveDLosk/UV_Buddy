package com.weebly.stevelosk.uv_buddy;

import android.net.Uri;
import android.os.AsyncTask;

import java.net.URI;

/**
 * Retrieved UV index from web API
 * Created by steve on 6/28/2017.
 */

public class GetUV_IndexAsync extends AsyncTask <String, Void, Integer> {
    @Override
    protected Integer doInBackground(String... zipCode) {

        // Pass zipCode into Api call
        String baseUriString = "https://iaspub.epa.gov/enviro/efservice/getEnvirofactsUVDAILY/ZIP/";
        baseUriString += zipCode;
        Uri apiCall = Uri.parse(baseUriString);

        // submit Api call
        

        // get call back

        // exctract UV index as an Integer


        return null;
    }
}
