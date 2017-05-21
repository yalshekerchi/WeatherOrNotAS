package com.yalshekerchi.weatherornot;

//Support Libraries
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//Utility Libraries
import android.view.View;
import android.util.Log;

//Widget Libraries

//Card View Libraries
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.johnhiott.darkskyandroidlib.models.DataPoint;

import java.util.ArrayList;

public class DaySelectionActivity extends AppCompatActivity {
    //MainActivity Variables
    double valLatitude;
    double valLongitude;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "DaySelectionActivity";

    final ArrayList<DataPoint> weatherList = AppData.getInstance().getWeatherList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_selection);

        //Get Variables passed from MainActivity
        Bundle extras = getIntent().getExtras();
        valLatitude = extras.getDouble("valLatitude");


        Log.i(LOG_TAG, " Clicked on Item " + weatherList.size());

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 20; index++) {
            DataObject obj = new DataObject("Some Primary Text " + index,
                    "Secondary " + index);
            results.add(index, obj);
        }
        return results;
    }
}