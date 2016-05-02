package com.spring2016ee5453classrom.mytwitter;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class Filter extends Activity {
    Button butFilterSearch;
    EditText filterName;
    EditText filterEarliest;
    EditText filterLatest;



    public String searchForName;
    public String searchForEarliest;
    public String searchForLatest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        butFilterSearch = (Button) findViewById(R.id.butFilterSearch);
        filterName = (EditText) findViewById(R.id.etFilterName);
        filterEarliest = (EditText) findViewById(R.id.etFilterEarliest);
        filterLatest = (EditText) findViewById(R.id.etFilterLatest);




        OnClickListener buttonListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilteredTweets();
            }
        };

        butFilterSearch.setOnClickListener(buttonListener);



    }
    public void launchFilteredTweets(){
        getSearchCriteria();

        Intent intent = new Intent(getBaseContext(),FilteredTweets.class);
        intent.putExtra("filterByName", searchForName);
        intent.putExtra("filterByEarliest", searchForEarliest);
        intent.putExtra("filterByLatest", searchForLatest);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void getSearchCriteria(){
        searchForName       = filterName.getText().toString();
        searchForEarliest   = filterEarliest.getText().toString();
        searchForLatest     = filterLatest.getText().toString();

    }
}
