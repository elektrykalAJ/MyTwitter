package com.spring2016ee5453classrom.mytwitter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

public class StatusProvider extends ContentProvider {


    private static final String AUTHORITY = "content://com.spring2016ee5453classroom.mytwitter.provider";
    public static final Uri CONTENT_URI_STATUS = Uri.parse(AUTHORITY+"/"+StatusData.TABLE);
    public static final Uri CONTENT_URI_USER = Uri.parse(AUTHORITY+"/"+"users");
    private static final String TAG = "StatusProvider";

    DbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            db=dbHelper.getWritableDatabase();
            //Log.d(TAG,"got a valid db");
            db.insertWithOnConflict(uri.getLastPathSegment(),null,values,SQLiteDatabase.CONFLICT_IGNORE);
        } catch (SQLiteException e) {
            Log.d(TAG,e.getMessage());
        } catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        getContext().sendBroadcast(new Intent(MyTwitter.STATUS_CHANGE_ALERT));
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //throw new UnsupportedOperationException("Not yet implemented");
        db = dbHelper.getReadableDatabase();
        return db.query(uri.getLastPathSegment(),projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
