package com.sharechat.anandpandey.sharechatassignment;

import com.sharechat.anandpandey.sharechatassignment.HomeActivity.HttpRequestServiceReciever;
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

public class HttpRequestService extends IntentService {

    public static final String REQUEST_STRING = "http://35.154.249.234:5000/data";
    public static final String RESPONSE_STRING = "myResponse";
    public static final String RESPONSE_MESSAGE = "myResponseMessage";
    private String URL;
    private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;


    @Override
    protected void onHandleIntent(Intent intent) {

        String responseString = RESPONSE_STRING;
        String responseMessage;

        Log.v("Info", "Intent service called for HTTP request");
        try {

            URL = REQUEST_STRING;
            Log.v("Info", "Intent service caerrelled for HTTP request");
            HttpClient httpclient = new DefaultHttpClient();
            HttpParams params = httpclient.getParams();

            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

            HttpPost httpPost = new HttpPost(URL);
            httpPost.setHeader("Content-Type", "application/json");
            JSONObject json = new JSONObject();
            json.put("request_id", "anandp219@gmail.com");
            StringEntity requestEntity = new StringEntity(json.toString());
            httpPost.setEntity(requestEntity);
            HttpResponse response = httpclient.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseMessage = out.toString();
            }

            else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception e) {
            responseMessage = e.getMessage();
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(HttpRequestServiceReciever.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_STRING, responseString);
        broadcastIntent.putExtra(RESPONSE_MESSAGE, responseMessage);
        sendBroadcast(broadcastIntent);
    }

    /**
     * Constructor
     */
    public HttpRequestService() {
        super("HttpRequestService");
    }


}