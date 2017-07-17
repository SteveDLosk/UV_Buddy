package com.weebly.stevelosk.uv_buddy;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by steve on 7/7/2017.
 */

public class GetUV_IndexAsync2 extends AsyncTask <String, Void, Integer> {

    private String TAG = "GetUV_IndexAsync2";
    private iAsyncCalling mActivity;
    private String mZipCode;
    private Context mContext;
    private Boolean errors;

    public GetUV_IndexAsync2 (Context context, iAsyncCalling callingActivity, String zipCode) {
        mActivity = callingActivity;
        mZipCode = zipCode;
        mContext = context;
    }

    @Override
    protected void onPreExecute () {

        mActivity.reportLoading();
    }
    @Override
    protected Integer doInBackground(String... zipCode) {

        // flag for errors
        Boolean errors = false;

        // testing for a valid zipcode before sending query
        if (mZipCode.length() < 5) {
            // indicates error
            return -1;
        }

        // objects to handle data collection
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String key = "d4b92a82e8213ee0";
        String queryByZip = "/conditions/q/zip/" + mZipCode + ".json";
        String baseUriString = "http://api.wunderground.com/api/"; // + key +
        String fullQueryString = baseUriString + key + queryByZip;

        String jsonReturned = "";

       // http://api.wunderground.com/api/d4b92a82e8213ee0/conditions/q/CA/San_Francisco.json
        // get the json
        try {
            URL url = new URL(fullQueryString);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

            jsonReturned = buffer.toString();
            Log.i(TAG, "Here is the JSON:");
            Log.i(TAG, jsonReturned);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        UV_Json_Parse uv_json_parse = new UV_Json_Parse(jsonReturned);
        return uv_json_parse.getCurrentUV();
    }


    @Override
    protected void onPostExecute(Integer result) {
        Log.i(TAG, "entered onPostExecute");
        mActivity.update(result);
    }

}
