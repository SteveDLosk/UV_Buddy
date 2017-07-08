package com.weebly.stevelosk.uv_buddy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by steve on 7/7/2017.
 */

public class GetUV_IndexAsync2 extends AsyncTask <String, Void, Integer[]> {

    private String TAG = "GetUV_IndexAsync2";
    private MainActivity mActivity;
    private String mZipCode;
    private String mHour;
    private Context mContext;
    private Boolean errors;

    public GetUV_IndexAsync2 (MainActivity mainActivity, String zipCode, String hour) {
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
    protected Integer[] doInBackground(String... zipCode) {

        // flag for errors
        Boolean errors = false;

        String json = "";
        UV_Json_Parse uv_json_parse = new UV_Json_Parse(json);
        return uv_json_parse.getDataArray();
    }


    @Override
    protected void onPostExecute(Integer[] result) {
        Log.i(TAG, "entered onPostExecute");
        mActivity.updateArray(result);
    }

}
