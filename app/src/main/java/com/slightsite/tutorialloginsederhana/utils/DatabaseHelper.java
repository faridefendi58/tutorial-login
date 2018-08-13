package com.slightsite.tutorialloginsederhana.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements Database {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = DatabaseContents.DATABASE.toString();


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + DatabaseContents.TABLE_USERS + "("
                + "_id INTEGER PRIMARY KEY,"
                + "name TEXT(100),"
                + "email TEXT(32),"
                + "password TEXT(256),"
                + "phone TEXT(32),"
                + "status INTEGER DEFAULT 1,"
                + "date_added DATETIME"
                + ");");

        Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_USERS + " Successfully.");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContents.TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    @Override
    public List<Object> select(String queryString) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            List<Object> list = new ArrayList<Object>();
            Cursor cursor = database.rawQuery(queryString, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        ContentValues content = new ContentValues();
                        String[] columnNames = cursor.getColumnNames();
                        for (String columnName : columnNames) {
                            content.put(columnName, cursor.getString(cursor
                                    .getColumnIndex(columnName)));
                        }
                        list.add(content);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            database.close();
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int insert(String tableName, Object content) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();

            int id = (int) database.insert(tableName, null,
                    (ContentValues) content);

            database.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean update(String tableName, Object content) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues cont = (ContentValues) content;
            // this array will always contains only one element.
            String[] array = new String[]{cont.get("_id")+""};
            database.update(tableName, cont, " _id = ?", array);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String tableName, int id) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            database.delete(tableName, " _id = ?", new String[]{id+""});
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean execute(String queryString) {
        try{
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL(queryString);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
