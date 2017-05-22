package com.yalshekerchi.weatherornot;

//Support Libraries

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.johnhiott.darkskyandroidlib.models.DataPoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//Utility Libraries
//Widget Libraries
//Card View Libraries

public class DaySelectionActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    // RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "DaySelectionActivity";

    // webservices API
    private static final String placesAPIKey = "AIzaSyB6EemTJi9vn84xwhv-ukq3aLqZR7ZTl9E";

    private static final int TEMP_RADIUS = 1000;

    final ArrayList<DataPoint> weatherList = AppData.getInstance().getWeatherList();
    double latitude = AppData.getInstance().getLatitude();
    double longitude = AppData.getInstance().getLongitude();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_selection);


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // All your networking logic
                // should be here
                for (int i = 0; i < weatherList.size(); i++) {
                    Log.d(LOG_TAG, "LIST NUMBER: " + i);

                    //Initialize new ArrayList in map
                    AppData.getInstance().getResultMap().put(i, new ArrayList<String>());

                    //Get weather icon and types associated with it
                    String icon = weatherList.get(i).getIcon();
                    String[] vals = AppData.getInstance().getIconMap().get(icon);

                    //Iterate through each type
                    for (int j = 0; j < vals.length; j++) {
                        Log.d(LOG_TAG, "TYPE: " + vals[j]);

                        //Get ArrayList of place-ids
                        ArrayList<String> idList = null;
                        try {
                            idList = getPlaceIDs(latitude, longitude, TEMP_RADIUS, vals[j]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Remove duplicates and all ids into resultList
                        ArrayList<String> resultList = AppData.getInstance().getResultMap().get(i);
                        resultList.removeAll(idList);
                        resultList.addAll(idList);
                    }

                    Log.d(LOG_TAG, "Array: " +  AppData.getInstance().getResultMap().get(i).toString());
                }
            }
        });

        //Setup RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.weatherRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        //Setup RecyclerView Layout Manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Initialize RecyclerView
        mAdapter = new WeatherRecyclerViewAdapter(AppData.getInstance().getWeatherList());
        mRecyclerView.setAdapter(mAdapter);

        // Code to Add an item with default animation
        //((WeatherRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((WeatherRecyclerViewAdapter) mAdapter).deleteItem(index);
    }

    private ArrayList<String> createFromJSON( JsonReader jsonReader ) throws IOException {
        ArrayList<String> ids = new ArrayList<String>();

        jsonReader.beginObject();
        while( jsonReader.hasNext() ){
            final String name = jsonReader.nextName();
            final boolean isNull = (jsonReader.peek() == JsonToken.NULL);

            if( name.equals( "results" ) && !isNull ) {
                jsonReader.beginArray();

                while( jsonReader.hasNext() ) {
                    jsonReader.beginObject();

                    while( jsonReader.hasNext() ) {
                        final String innerName = jsonReader.nextName();
                        final boolean isInnerNull = (jsonReader.peek() == JsonToken.NULL);

                        if( innerName.equals( "place_id" ) && !isInnerNull ) {
                            String ID = jsonReader.nextString();
                            Log.d(LOG_TAG, "PLACE_ID: " + ID);
                            ids.add(ID);
                        }
                        else jsonReader.skipValue();
                    }
                    jsonReader.endObject();
                }
                jsonReader.endArray();
            }
            else jsonReader.skipValue();
        }
        jsonReader.endObject();
        return ids;
    }

    private ArrayList<String> getPlaceIDs(double latitude, double longitude, int radius, String type) throws IOException {
        final String AMPERSAND = "&";
        HttpURLConnection connection = null;
        ArrayList<String> idList = null;
        /*
        try{
            InputStream inputstream = getAssets().open("response.json");
            InputStreamReader responseBodyReader = new InputStreamReader(inputstream, "UTF-8");
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            idList = createFromJSON(jsonReader);
            jsonReader.close();
        } catch (Exception e){
            //pass
        }
        */


        try {
            // set query string
            String urlStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            urlStr += "key=" + placesAPIKey + AMPERSAND;
            urlStr += "location=" + latitude + "," + longitude + AMPERSAND;
            urlStr += "radius=" + radius + AMPERSAND;
            urlStr += "type=" + type;
            // open connection
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                // Success
                // parse
                InputStream responseBody = connection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                idList = createFromJSON(jsonReader);
                jsonReader.close();
            } else {
                // Error handling code goes here
                Log.e(LOG_TAG, "" + connection.getResponseCode());
            }
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return idList;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}