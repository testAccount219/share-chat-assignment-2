package com.sharechat.anandpandey.sharechatassignment;

import com.sharechat.anandpandey.sharechatassignment.HomeActivity.HttpRequestServiceReciever;
import com.sharechat.anandpandey.sharechatassignment.HttpRequestService;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by anandpandey on 11/06/17.
 */

public class DataFetchService extends IntentService {


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.v("Info", "Intent service called for DATA fetch and save");
        try {


        } catch (Exception e) {
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(DataFetchServiceReciever.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(broadcastIntent);
    }

    /**
     * Constructor
     */
    public DataFetchService() {
        super("DataFetchService");
    }


}