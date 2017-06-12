package com.sharechat.anandpandey.sharechatassignment;

import com.google.gson.Gson;
import com.sharechat.anandpandey.sharechatassignment.HomeActivity.DataFetchServiceReciever;
import com.sharechat.anandpandey.sharechatassignment.DAO.imageDao;
import com.sharechat.anandpandey.sharechatassignment.DAO.userDao;
import com.sharechat.anandpandey.sharechatassignment.Data.image;
import com.sharechat.anandpandey.sharechatassignment.Data.user;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anandpandey on 11/06/17.
 */

public class DataFetchService extends IntentService {

    public static final String REQUEST_STRING = "http://35.154.249.234:5000/data";
    public static final String RESPONSE_STRING = "myResponse";
    private String URL;
    private int LAST_POST_FETCHED = 0;
    private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;
    public static final String RESPONSE_MESSAGE = "myResponseMessage";
    private int OFFSET = 0;
    private imageDao ImageDao = new imageDao(DataFetchService.this);
    private userDao UserDao = new userDao(DataFetchService.this);

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.v("Info", "Intent service called for DATA fetch and save");
        try {
            OFFSET = intent.getIntExtra(HomeActivity.LAST_POST_FETCHED, 0);
            LAST_POST_FETCHED = OFFSET;
            JSONObject responseJson = getDataFromDatabase(OFFSET);
            if(responseJson == null) {
                fetchDataAndPutItInDatabase();
                responseJson = getDataFromDatabase(OFFSET);
            }
            if(responseJson == null) {
                responseJson = new JSONObject();
                responseJson.put("data",new JSONArray());
            }

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(DataFetchServiceReciever.PROCESS_RESPONSE);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(RESPONSE_MESSAGE, responseJson.toString());
            broadcastIntent.putExtra(HomeActivity.LAST_POST_FETCHED, LAST_POST_FETCHED);
            sendBroadcast(broadcastIntent);

        } catch (Exception e) {
        }
    }

    public void fetchDataAndPutItInDatabase() {
        String responseMessage = null;
        try {
            URL = REQUEST_STRING;
            Log.v("Info", "HTTP CALL");
            HttpClient httpclient = new DefaultHttpClient();
            HttpParams params = httpclient.getParams();

            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

            HttpPost httpPost = new HttpPost(URL);
            httpPost.setHeader("Content-Type", "application/json");
            JSONObject json = new JSONObject();
            json.put("request_id", "anandp219@gmail.com");
            json.put("id_offset", OFFSET);
            StringEntity requestEntity = new StringEntity(json.toString());
            httpPost.setEntity(requestEntity);
            HttpResponse response = httpclient.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseMessage = out.toString();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
        }
        JSONObject responseJson = new JSONObject();
        try {
            int i;
            ImageDao.open();
            UserDao.open();
            responseJson = new JSONObject(responseMessage);
            JSONArray data = (JSONArray) responseJson.get("data");
            for(i = 0; i < data.length();  i  += 1) {
                try {
                    JSONObject obj = (JSONObject) data.get(i);
                    if(obj.get("type").equals("image")) {
                        ImageDao.createImage(new image(
                                obj.getInt("id"),
                                obj.getString("author_name"),
                                obj.getString("url"),
                                obj.getInt("postedOn"),
                                obj.getString("type")
                        ));
                    } else {
                        UserDao.createUser(new user(
                                obj.getInt("id"),
                                obj.getString("type"),
                                obj.getString("author_name"),
                                obj.getString("author_dob"),
                                obj.getString("author_gender"),
                                obj.getString("author_status"),
                                obj.getString("profile_url"),
                                obj.getString("author_contact")
                        ));
                    }
                } catch (Exception e) {
                    Log.v("Error", e.toString());
                }
            }

        } catch (org.json.JSONException e) {
            Log.v("Error", e.toString());
        } finally {
            ImageDao.close();
            UserDao.close();
        }
    }

    /**
     * Constructor
     */
    public DataFetchService() {
        super("DataFetchService");
    }

    public JSONObject getDataFromDatabase(int offset) {

        List<image> Images = new ArrayList<image>();
        List<user> Users = new ArrayList<user>();

        try {

            ImageDao.open();
            UserDao.open();
            Images = ImageDao.getAllImages(offset);
            Users = UserDao.getAllUsers(offset);

        } catch(Exception e) {

            ImageDao.createTable();
            Log.v("Error", e.toString());
        }
        finally {
            ImageDao.close();
            UserDao.close();
        }

        if(Images.size() == 0  && Users.size() == 0) {
            return null;
        }

        JSONObject result = null;

        try {

            Gson gson = new Gson();
            JSONArray resultArray = new JSONArray();

            for(int i = 0;  i<Images.size();  i += 1) {
                if(Images.get(i).getId() > LAST_POST_FETCHED) {
                    LAST_POST_FETCHED = (int)Images.get(i).getId();
                }
                resultArray.put(i, gson.toJson(Images.get(i)));
            }

            int NO_OF_IMAGES = Images.size();

            for(int i = 0;  i<Users.size();  i += 1) {
                if(Users.get(i).getId() > LAST_POST_FETCHED) {
                    LAST_POST_FETCHED = (int)Users.get(i).getId();
                }
                resultArray.put(NO_OF_IMAGES + i, gson.toJson(Users.get(i)));
            }


            result = new JSONObject();
            result.put("data", resultArray);

        } catch(Exception e) {

            Log.v("Error", e.toString());

        }

        return result;
    }


}