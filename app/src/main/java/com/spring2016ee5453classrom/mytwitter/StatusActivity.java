package com.spring2016ee5453classrom.mytwitter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

public class StatusActivity extends AppCompatActivity implements LocationListener {

    public static final String TAG = "StatusActivity";

    LocationManager locationManager;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //finish();
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null)
            Log.d(TAG, "Location: " + location.toString());
        else
            Log.d(TAG, "null location object");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1000, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    public void onClick(View v) {
        final String status_text = ((EditText) findViewById(R.id.text_status)).getText().toString();
        Log.d(TAG, status_text);
        //suspect
        //twitter.setStatus(status_text);

     /*   new Thread(){
            public void run(){
                try {
                    final Twitter twitter = new Twitter("student", "password");
                    twitter.setAPIRootUrl("http://yamba.newcircle.com/api");
                    twitter.setStatus(status_text);
                    Toast.makeText(StatusActivity.this, "Posted!", Toast.LENGTH_SHORT).show();
                }
                catch (TwitterException e){
                    Log.d(TAG,e.getMessage());
                    Toast.makeText(StatusActivity.this,"Failed to post!: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }.start();*/
        if (status_text.contains("\n")){
            //multiple tweets
            //String[] statuses = new

            new PostToTwitter().execute((status_text.split("\n")));

        }
        else
        {
            //single tweet
            new PostToTwitter().execute(status_text);

        }

        new Thread() {
            public void run() {
                try {
                    /*Twitter twitter = new Twitter("student", "password");
                    twitter.setAPIRootUrl("http://yamba.newcircle.com/api");*/
                    List<Twitter.Status> statuses = ((MyTwitter)getApplication()).getTwitter().getHomeTimeline();
                    for (Twitter.Status status:statuses){
                        Log.d(TAG,status.getText());
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
        Log.d(TAG,"Location: "+location.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    class PostToTwitter extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... statuses) {
            try {
                /*Twitter twitter = new Twitter("student", "password");
                twitter.setAPIRootUrl("http://yamba.newcircle.com/api");*/
                //twitter.setStatus(statuses[0]);
                for (int i=0;i<statuses.length;i++) {
                    ((MyTwitter)getApplication()).getTwitter().setStatus(statuses[i]);
                    publishProgress("Posting tweet: "+statuses[i]);
                }
                return "Posted!";
            } catch (TwitterException e) {
                //e.printStackTrace();
                return "Failed to post: " + e.getMessage();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(StatusActivity.this, values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(StatusActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}