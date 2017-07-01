package com.weebly.stevelosk.uv_buddy;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.util.Xml;

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
    private ApiTestActivity mApiTestActivity;

    public GetUV_IndexAsync (ApiTestActivity apiTestActivity) {
        mApiTestActivity = apiTestActivity;
    }

    @Override
    protected Integer doInBackground(String... zipCode) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        // Pass zipCode into Api call
        //zipCode = "98390";

        String baseUriString = "https://iaspub.epa.gov/enviro/efservice/getEnvirofactsUVHOURLY/ZIP/";
        //baseUriString += zipCode;
        baseUriString += "98390";
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
            // TODO: pass in hour currently using 5PM to test

            // find correct hour object
            String correctHourJSON = "05 PM\",\"UV_VALUE\":";
            int index = jsonReturned.indexOf(correctHourJSON);
            index = index + correctHourJSON.length();

            // get the value
            String value = jsonReturned.substring(index, index + 2);
            // pull off end bracket if result is a single digit
            if (value.substring(1, 2).equals("}")) {
                value = value.substring(0, 1);
            }

            Integer result = Integer.valueOf(value);
            return result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        Log.d(TAG, "something went wrong");
        return null;
    }


    @Override
    protected void onPostExecute(Integer result) {

        Log.i(TAG, "entered onPostExecute");

        mApiTestActivity.update(result);

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



