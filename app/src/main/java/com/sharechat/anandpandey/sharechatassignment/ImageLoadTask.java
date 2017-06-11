package com.sharechat.anandpandey.sharechatassignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anandpandey on 11/06/17.
 */

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;
    private String PROFILE_IMAGE = "profile_image";
    private String IMAGE = "image";
    private int SCALED_WIDTH = 128;
    private int SCALED_HEIGHT = 128;
    private String imageType;

    public ImageLoadTask(String url, ImageView imageView, String imageType) {
        this.url = url;
        this.imageView = imageView;
        this.imageType = imageType;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            if(imageType.equals(PROFILE_IMAGE)) {
                myBitmap = Bitmap.createScaledBitmap(myBitmap, SCALED_WIDTH, SCALED_WIDTH, true);
            }
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

}