package com.sharechat.anandpandey.sharechatassignment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String IMAGES = "images";
    public static final String IMAGE = "image";
    public static final String COLUMN_ID = "_id";
    public static final String AUTHOR_NAME = "author_name";
    public static final String URL = "url";
    public static final String POSTED_ON = "posted_on";


    private static final String DATABASE_NAME = "sharechat";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_IMAGES = "create table "
            + IMAGES + "( " + COLUMN_ID
            + " integer primary key, " + AUTHOR_NAME
            + " TEXT not null " + URL
            + " text NOT NULL " + POSTED_ON
            + " integer not null )";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + IMAGES);
        onCreate(db);
    }

}