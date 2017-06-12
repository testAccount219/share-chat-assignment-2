package com.sharechat.anandpandey.sharechatassignment.DAO;

import com.sharechat.anandpandey.sharechatassignment.MySQLiteHelper;
import com.sharechat.anandpandey.sharechatassignment.Data.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anandpandey on 12/06/17.
 */

public class userDao {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.AUTHOR_NAME,
            MySQLiteHelper.AUTHOR_CONTACT,
            MySQLiteHelper.AUTHOR_DOB,
            MySQLiteHelper.AUTHOR_STATUS,
            MySQLiteHelper.AUTHOR_GENDER,
            MySQLiteHelper.PROFILE_URL,
            MySQLiteHelper.TYPE};

    public userDao(Context context) {
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

    public user createUser(user User) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ID, User.getId());
        values.put(MySQLiteHelper.AUTHOR_NAME, User.getAuthorName());
        values.put(MySQLiteHelper.AUTHOR_DOB, User.getAuthorDob());
        values.put(MySQLiteHelper.AUTHOR_CONTACT, User.getAuthorContact());
        values.put(MySQLiteHelper.AUTHOR_STATUS, User.getAuthorStatus());
        values.put(MySQLiteHelper.AUTHOR_GENDER, User.getAuthorGender());
        values.put(MySQLiteHelper.PROFILE_URL, User.getProfileUrl());
        values.put(MySQLiteHelper.TYPE, User.getType());

        long insertId = database.insert(MySQLiteHelper.USERS, null, values);
        return User;
    }

    public void deleteUser(user User) {
        long id = User.getId();
        System.out.println("user deleted with id: " + id);
        database.delete(MySQLiteHelper.USERS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<user> getAllUsers(int Offset) {
        List<user> Users = new ArrayList<user>();

        Cursor cursor = database.query(MySQLiteHelper.USERS,
                allColumns, MySQLiteHelper.COLUMN_ID + " > " + Offset, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user User = cursorToUser(cursor);
            Users.add(User);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Users;
    }

    private user cursorToUser(Cursor cursor) {
        user User = new user();
        User.setId(cursor.getLong(0));
        User.setAuthorName(cursor.getString(1));
        User.setAuthorContact(cursor.getString(2));
        User.setAuthorDob(cursor.getString(3));
        User.setAuthorStatus(cursor.getString(4));
        User.setAuthorGender(cursor.getString(5));
        User.setProfileUrl(cursor.getString(6));
        User.setType(cursor.getString(7));
        return User;
    }
}
