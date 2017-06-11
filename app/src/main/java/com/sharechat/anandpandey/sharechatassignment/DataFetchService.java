package com.sharechat.anandpandey.sharechatassignment;

import com.sharechat.anandpandey.sharechatassignment.HomeActivity.DataFetchServiceReciever;
import com.sharechat.anandpandey.sharechatassignment.DAO.imageDao;
import com.sharechat.anandpandey.sharechatassignment.Data.image;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by anandpandey on 11/06/17.
 */

public class DataFetchService extends IntentService {


    public static final String RESPONSE_MESSAGE = "myResponseMessage";
    private HttpRequestServiceReciever httpRequestServiceReciever;
    private int OFFSET = -1;
    private imageDao ImageDao;

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.v("Info", "Intent service called for DATA fetch and save");
        try {
            JSONObject responseJson = getDataFromDatabase(OFFSET);
            IntentFilter filter = new IntentFilter(HttpRequestServiceReciever.PROCESS_RESPONSE);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            httpRequestServiceReciever = new HttpRequestServiceReciever();
            registerReceiver(httpRequestServiceReciever, filter);
            Intent httpIntent = new Intent(DataFetchService.this, HttpRequestService.class);
            Log.v("INFO","CALLING INTENT HTTP");
            startService(httpIntent);
            Log.v("INFO","After CALLING INTENT HTTP");

        } catch (Exception e) {

        }
    }

    /**
     * Constructor
     */
    public DataFetchService() {
        super("DataFetchService");
    }


    public class HttpRequestServiceReciever extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseString = intent.getStringExtra(HttpRequestService.RESPONSE_MESSAGE);
            JSONObject responseJson = new JSONObject();
            try {
                int i;
                responseJson = new JSONObject(responseString);
                JSONArray data = (JSONArray) responseJson.get("data");
                for(i = 0; i < data.length();  i  += 1) {

                }
            } catch (org.json.JSONException e) {
                Log.v("Error", e.toString());
            }
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(HttpRequestServiceReciever.PROCESS_RESPONSE);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(RESPONSE_MESSAGE, responseString);
            sendBroadcast(broadcastIntent);
        }

    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(httpRequestServiceReciever);
        super.onDestroy();
    }

    public JSONObject getDataFromDatabase(int offset) {
        try {
            List<image> Images = ImageDao.getAllImages(offset);
        } catch(Exception e) {
            Log.v("Error", e.toString());
        }
        return new JSONObject();
    }

}