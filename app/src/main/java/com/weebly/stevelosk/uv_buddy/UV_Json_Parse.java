package com.weebly.stevelosk.uv_buddy;


import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by steve on 7/7/2017.
 */
public class UV_Json_Parse {

    private String json;

    // stamp for when data was last downloaded.  Used to prevent excessive API calls
    private long timeStamp;
    private final long hour = 1000 * 60 * 60;

    // stores up to 24 hours' forecast UV index
    private Integer[] UVindexByHour;

    public UV_Json_Parse (String jsonInput) {

        // timeStamp the data
        this.timeStamp = System.currentTimeMillis();
        this.json = jsonInput;

        /*
        // initialize data array.  Use -1 to indicate no data for that hour
        UVindexByHour = new Integer[24];
        for (int i = 0; i < 24; i++) {
            UVindexByHour[i] = -1;
        }

        // load the Json data into the array
        setUVindexByHour (this.UVindexByHour, jsonInput);
        */
    }

    private void setUVindexByHour (Integer[] array, String json) {
        // pulls data from the json and loads it into the array

        // variables for counting through the json
        int index = 0;
        int count = 0;
        int currentHour;
        String hourNumAsString;
        String uviAsString;

        while (count < 24) {
            index = json.indexOf("\"hour\": ");
            // move up to the actual hour in the json
            index += 8;
            hourNumAsString = json.substring(index, index+2);

            // solve single digit number issues
            if (hourNumAsString.substring(1,2).equals('"')) {
                hourNumAsString = hourNumAsString.substring(0,1);

            }
            currentHour = Integer.parseInt(hourNumAsString);

            // get the index of that hour
            index = json.indexOf("uvi\": \"");
            // move up to the actual uvi in the json
            index += 7;
            uviAsString = json.substring(index, index + 2);

            // solve single digit number issues
            if (uviAsString.substring(1,2).equals('"')) {
                uviAsString = uviAsString.substring(0,1);

            }

            // load data into array
            array[currentHour] = Integer.parseInt(uviAsString);

            // clear out processed json
            json = json.substring(index);

            // loop over again
            count++;
        }

    }

    public Integer[] getDataArray () {
        return UVindexByHour;
    }

    public long getDataAge () {
        return System.currentTimeMillis() - timeStamp;
    }

    public Integer getCurrentUV () {
        // sample String:  "UV":"0"
        int index = json.indexOf("UV\":\"");

        // move index to the start of the actual value
        index += 5;

        // index for the closing "
        int index2 = json.indexOf('"', index + 1);

        String currentUV = json.substring(index, index2);

        double uvIndexDouble = Double.parseDouble(currentUV);

        int uvIndexInt = (int) Math.round(uvIndexDouble);
        return uvIndexInt;
    }
}
