package com.spring2016ee5453classrom.mytwitter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FilteredTweets extends AppCompatActivity {

    public String searchForName;
    public String searchForEaliest;
    public String searchForLatest;

    public TextView tvSearchedName;
    public TextView tvSearchedEarliest;
    public TextView tvSearchedLatest;

    public ListView listFiltered;
    SimpleCursorAdapter adapter;
    Cursor cursor;
    String FROM[] = {StatusData.C_USER,StatusData.C_CREATED_AT,StatusData.C_TEXT};
    int TO[] = {R.id.text_user,R.id.text_createdat,R.id.text_status};
    TimelineReceiver receiver=null;

    static final SimpleCursorAdapter.ViewBinder VIEW_BINDER = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

            if (view.getId()!=R.id.text_createdat)
                return false;

            long timeInMilli = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timeInMilli);
            ((TextView)view).setText(relativeTime);
            return true;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_tweets);
//        getActionBar().setDisplayHomeAsUpEnabled(true);





        tvSearchedName      = (TextView)findViewById(R.id.tvSearchedName);
        tvSearchedEarliest  = (TextView) findViewById(R.id.tvSearchedEarliest);
        tvSearchedLatest    = (TextView) findViewById(R.id.tvSearchLatest);
        Bundle searchCriteria = getIntent().getExtras();

        if(searchCriteria != null){
            searchForName       = searchCriteria.getString("filterByName");
            searchForEaliest    = searchCriteria.getString("filterByEarliest");
            searchForLatest     = searchCriteria.getString("filterByLatest");


            tvSearchedName.setText(searchForName);
            tvSearchedEarliest.setText(searchForEaliest);
            tvSearchedLatest.setText(searchForLatest);
        }

        cursor = ((MyTwitter) getApplication()).statusData.queryByUserName(searchForName);
//        cursor = ((MyTwitter) getApplication()).statusData.query();
        listFiltered = (ListView) findViewById(R.id.listFiltered);
        adapter = new SimpleCursorAdapter(this,R.layout.row,cursor,FROM,TO,0);
        adapter.setViewBinder(VIEW_BINDER);
        listFiltered.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (receiver==null)
            receiver = new TimelineReceiver();

        registerReceiver(receiver,new IntentFilter(MyTwitter.STATUS_CHANGE_ALERT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    class TimelineReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            cursor = ((MyTwitter) getApplication()).statusData.query();
            adapter.changeCursor(cursor);

        }
    }

}
