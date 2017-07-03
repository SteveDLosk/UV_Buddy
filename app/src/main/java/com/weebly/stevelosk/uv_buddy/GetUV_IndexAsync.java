package com.weebly.stevelosk.uv_buddy;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.entries;

/**
 * Retrieved UV index from web API
 * Created by steve on 6/28/2017.
 */

public class GetUV_IndexAsync extends AsyncTask <String, Void, Integer> {

    private String TAG = "GetUV_IndexAsync";
    private MainActivity mActivity;
    private String mZipCode;
    private String mHour;
    private Context mContext;
    private Boolean errors;

    public GetUV_IndexAsync (MainActivity mainActivity, String zipCode, String hour) {
        mActivity = mainActivity;
        mZipCode = zipCode;
        mHour = hour;
        mContext = mActivity.getApplicationContext();
    }

    @Override
    protected void onPreExecute () {

        mActivity.reportLoading();
    }
    @Override
    protected Integer doInBackground(String... zipCode) {

        // flag to catch problems
        errors = false;

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        // Pass zipCode into Api call
        //zipCode = "98390";

        String baseUriString = "https://iaspub.epa.gov/enviro/efservice/getEnvirofactsUVHOURLY/ZIP/";
        //baseUriString += zipCode;
        baseUriString += mZipCode;
        baseUriString += "/JSON";

        Log.i(TAG, baseUriString);

        // Get JSON

        try {
            URL url = new URL(baseUriString);
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

            String jsonReturned = buffer.toString();
            Log.i(TAG, "Here is the JSON:");
            Log.i(TAG, jsonReturned);

            // get UV index integer from json

            // find correct hour object
            String correctHourJSON = mHour + "\",\"UV_VALUE\":";
            int index = jsonReturned.indexOf(correctHourJSON);
            index = index + correctHourJSON.length();

            // get the value
            String value = jsonReturned.substring(index, index + 2);
            // pull off end bracket if result is a single digit
            if (value.substring(1, 2).equals("}")) {
                value = value.substring(0, 1);
            }


            try {
                Integer result = Integer.valueOf(value);
                return result;
            }
            catch (java.lang.NumberFormatException e) {
                e.printStackTrace();
                reportProblem();
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
            reportProblem();
        } catch (IOException e) {
            e.printStackTrace();
            reportProblem();
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            reportProblem();
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
        Log.d(TAG, "something went wrong");
        return null;
    }


    @Override
    protected void onPostExecute(Integer result) {

        Log.i(TAG, "entered onPostExecute");

        mActivity.update(result);

        if (errors) {
            reportProblem();
        }

    }

    private void reportProblem () {
        // Apparently I have to do this
        Log.i(TAG, "entered reportProblem");


    }

    private void reportProblem (Exception e) {
        Log.i(TAG, e.getMessage().toString());
    }


    class EPA_JSONParser {

        // sample result [{"ZIP_CODE":20050,"UV_INDEX":9,"UV_ALERT":0}]

        // sample API call https://iaspub.epa.gov/enviro/efservice/getEnvirofactsUVHOURLY/ZIP/98390/JSON

        public Integer getJsonUV_Index(InputStream in, Integer hour) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            try {

                Integer result = -1; // flag for an error

                // get correct hour
                while (reader.hasNext()) {
                    String hourString = reader.nextName();
                    if (hourString.equals("ORDER")) {
                        int currentHour = reader.nextInt();
                        if (currentHour != hour)
                            continue;
                    }

                    // find UV_Index in correct JSON object

                    String name = reader.nextName();
                    if (name.equals("UV_VALUE")) {
                        result = reader.nextInt();
                    }
                }
                return result;
            } finally {
                reader.close();
            }
        }
    }
}



