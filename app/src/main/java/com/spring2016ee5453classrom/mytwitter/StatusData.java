package com.spring2016ee5453classrom.mytwitter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import winterwell.jtwitter.Twitter;

/**
 * Created by kkx358 on 3/29/2016.
 */
public class StatusData {

    public static final String TAG = "StatusData";

    public static final String DB_NAME = "timeline.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE = "status";

    public static final String C_ID = "_id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_USER = "user_name";
    public static final String C_TEXT = "status_text";

    Context context;
    DbHelper dbHelper;
    SQLiteDatabase db;


    public StatusData(Context context){
        this.context=context;
        dbHelper = new DbHelper(context);
    }

    public void insert(Twitter.Status status){

        //db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(C_ID,status.id);
        values.put(C_CREATED_AT,status.createdAt.getTime());
        values.put(C_USER,status.user.name);
        values.put(C_TEXT, status.text);

        //db.insert(TABLE,null,values);
        //db.insertWithOnConflict(TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(StatusProvider.CONTENT_URI_STATUS,values);
        //context.sendBroadcast(new Intent(MyTwitter.STATUS_CHANGE_ALERT));
    }

    public Cursor query(){
        /*db = dbHelper.getReadableDatabase();
        return db.query(TABLE,null,null,null,null,null,C_CREATED_AT+ " DESC",null);*/
       return context.getContentResolver().query(StatusProvider.CONTENT_URI_STATUS,null,null,null,C_CREATED_AT+" DESC");
    }

    public Cursor queryByUserName(String name){
        db = dbHelper.getReadableDatabase();
        String query = "select * from status where user_name like '%" + name + "%' order by user_name";
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }
}

class DbHelper extends SQLiteOpenHelper{

    public DbHelper(Context context) {
        super(context, StatusData.DB_NAME, null, StatusData.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(StatusData.TAG,"onCreate");
        String sql = String.format("create table %s (%s int primary key, %s long, %s text, %s text)",
                StatusData.TABLE, StatusData.C_ID, StatusData.C_CREATED_AT, StatusData.C_USER, StatusData.C_TEXT);
        //Log.d(TAG,sql);
        db.execSQL(sql);

        sql = "create table users (_ID int primary key, user text, age long)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(StatusData.TAG,"onUpgrade");
        db.execSQL("drop table if exists " + StatusData.TABLE);
        onCreate(db);
    }
}
