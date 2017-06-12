package com.sharechat.anandpandey.sharechatassignment.DAO;

import com.sharechat.anandpandey.sharechatassignment.MySQLiteHelper;
import com.sharechat.anandpandey.sharechatassignment.Data.image;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anandpandey on 12/06/17.
 */

public class imageDao {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.AUTHOR_NAME, MySQLiteHelper.URL, MySQLiteHelper.POSTED_ON, MySQLiteHelper.TYPE };

    public imageDao(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createTable() {
        dbHelper.createTable(database);
    }

    public image createImage(image Image) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ID, Image.getId());
        values.put(MySQLiteHelper.AUTHOR_NAME, Image.getAuthorName());
        values.put(MySQLiteHelper.URL, Image.getUrl());
        values.put(MySQLiteHelper.POSTED_ON, Image.getPostedOn());
        values.put(MySQLiteHelper.TYPE, Image.getType());

        long insertId = database.insert(MySQLiteHelper.IMAGES, null, values);
        return Image;
    }

    public void deleteImage(image Image) {
        long id = Image.getId();
        System.out.println("Image deleted with id: " + id);
        database.delete(MySQLiteHelper.IMAGES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<image> getAllImages(int Offset) {
        List<image> images = new ArrayList<image>();

        Cursor cursor = database.query(MySQLiteHelper.IMAGES,
                allColumns, MySQLiteHelper.COLUMN_ID + " > " + Offset, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            image Image = cursorToImage(cursor);
            images.add(Image);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return images;
    }

    private image cursorToImage(Cursor cursor) {
        image Image = new image();
        Image.setId(cursor.getLong(0));
        Image.setAuthorName(cursor.getString(1));
        Image.setUrl(cursor.getString(2));
        Image.setPostedOn(cursor.getInt(3));
        Image.setType(cursor.getString(4));
        return Image;
    }
}
