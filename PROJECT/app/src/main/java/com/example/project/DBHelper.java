package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME="APP";
    public static final int VER=1;
//    private static final String CREATE_SERVER_TABLE = "CREATE TABLE Server ("
//            + "S_user TEXT,"
//            + "S_name TEXT,"
//            + "S_user TEXT,"
//            + "S_url TEXT PRIMARY KEY)";

    private static final String CREATE_ANALYSIS_ALERT_TABLE = "CREATE TABLE Analysis_Alert ("
            + "Alert_NO INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "S_url TEXT,"
            + "Hostname TEXT,"
            + "Item TEXT,"
            + "Operator TEXT,"
            + "Value REAL,"
            + "Severity TEXT,"
            + "Alert_text TEXT,"
            + "Date TEXT)";

    private static final String CREATE_ALERT_CONFIG_TABLE = "CREATE TABLE Alert_Config ("
            + "Alert_Config_NO INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "S_url TEXT,"
            + "Hostname TEXT,"
            + "Item TEXT,"
            + "Operator TEXT,"
            + "Value REAL,"
            + "Severity TEXT)";







    public DBHelper(@Nullable Context context)
    {
        super(context, DBNAME, null, VER);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE Server (S_name TEXT,S_url TEXT PRIMARY KEY, S_user TEXT, S_pass TEXT)";
        db.execSQL(query);
        db.execSQL(CREATE_ALERT_CONFIG_TABLE);
        db.execSQL(CREATE_ANALYSIS_ALERT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query="drop table if exists Server";
        db.execSQL(query);
    }


    public Cursor getServer()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        String query="Select * from Server";
        Cursor cursor  = DB.rawQuery(query, null);
        return cursor;
    }

    public Cursor getAlert_config(String Surl,String hostname)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        String query="Select Item, Operator, Value, Severity from Alert_Config WHERE S_url = ? AND Hostname = ?";
        String[] selectionArgs = { Surl,hostname };
        Cursor cursor  = DB.rawQuery(query, selectionArgs);
        return cursor;
    }

    public Cursor getAna_alert(String Surl) {
        SQLiteDatabase DB = this.getWritableDatabase();
        String query = "SELECT Hostname, Alert_text, Severity, Date FROM Analysis_Alert WHERE S_url = ?";
        String[] selectionArgs = { Surl };
        Cursor cursor = DB.rawQuery(query, selectionArgs);
        return cursor;
    }

    public Cursor getAna_alert2(String Surl, String hostname) {
        SQLiteDatabase DB = this.getWritableDatabase();
        String query = "SELECT Hostname, Alert_text, Severity, Date FROM Analysis_Alert WHERE S_url = ? AND Hostname = ?";
        String[] selectionArgs = { Surl, hostname };
        Cursor cursor = DB.rawQuery(query, selectionArgs);
        return cursor;
    }





    public void updateServer(String old_S_url, String S_name, String new_S_url, String S_user, String S_pass) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            // Delete the existing record
            db.delete("Server", "S_url = ?", new String[] { old_S_url });

            // Insert the new record
            ContentValues values = new ContentValues();
            values.put("S_name", S_name);
            values.put("S_url", new_S_url);
            values.put("S_user", S_user);
            values.put("S_pass", S_pass);
            db.insert("Server", null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            // Handle exceptions here
        } finally {
            db.endTransaction();
        }

        db.close();
    }

    public void insertAnalysisAlert(String Surl, String hostname, String item, String operator, float value, String severity, String alert_text, String date) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.delete("Analysis_Alert", "S_url = ? AND Hostname = ? AND Item = ? AND Operator = ? AND Value = ? AND Severity = ?",
                new String[]{Surl, hostname, item, operator, String.valueOf(value), severity});

        ContentValues values = new ContentValues();
        values.put("S_url", Surl);
        values.put("Hostname", hostname);
        values.put("Item", item);
        values.put("Operator", operator);
        values.put("Value", value);
        values.put("Severity", severity);
        values.put("Alert_text", alert_text);
        values.put("Date", date);

        db.insert("Analysis_Alert", null, values);

        db.close();
    }



//    public void deleteAnalysisAlert(String alert_type, String S_url, String hostname) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete("Analysis_Alert", "Alert_type = ? AND S_url = ? AND Hostname = ?", new String[]{alert_type, S_url, hostname});
//        db.close();
//    }



//    public void deleteAllTables() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS Server");
//        db.close();
//    }
//
//    public void insertServer(String S_name, String S_url, String S_user, String S_pass) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("S_name", S_name);
//        values.put("S_url", S_url);
//        values.put("S_user", S_user);
//        values.put("S_pass", S_pass);
//        db.insert("Server", null, values);
//        db.close();
//    }



}
