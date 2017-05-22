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
import com.google.android.gms.location.places.Places;
import com.johnhiott.darkskyandroidlib.models.DataPoint;

import org.json.JSONObject;

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
    double latitude;
    double longitude;
    // RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "DaySelectionActivity";

    // webservices API
    private static final String placesAPIKey = "AIzaSyDf0oPYWc5ePlr-CfyuAv75AbB7uHRdpSg";

    final ArrayList<DataPoint> weatherList = AppData.getInstance().getWeatherList();

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_selection);

        // setup google API client
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getDouble("valLatitude");
        longitude = bundle.getDouble("valLongitude");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // All your networking logic
                // should be here
                JSONObject json = getPlaces();
                Log.d(LOG_TAG, ""+json);
            }
        });

        //Setup RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.weatherRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        //Setup RecyclerView Layout Manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new WeatherRecyclerViewAdapter(AppData.getInstance().getWeatherList());
        mRecyclerView.setAdapter(mAdapter);

        // Code to Add an item with default animation
        //((WeatherRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((WeatherRecyclerViewAdapter) mAdapter).deleteItem(index);
    }

    public void createFromJSON( JsonReader jsonReader ) throws IOException {
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
                            Log.d(LOG_TAG, "PLACE_ID: " + jsonReader.nextString());
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
    }

    private JSONObject getPlaces() {
        final String AMPERSAND = "&";
        HttpURLConnection connection = null;
        JSONObject data = null;
        try {
            // set query string
            String urlStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            urlStr += "key=AIzaSyDf0oPYWc5ePlr-CfyuAv75AbB7uHRdpSg" + AMPERSAND;
            urlStr += "location=" + latitude + "," + longitude + AMPERSAND;
            urlStr += "radius=1000";
            // open connection
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            // TODO: arraylist

            if (connection.getResponseCode() == 200) {
                // Success
                // parse
                InputStream responseBody = connection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                createFromJSON(jsonReader);
                jsonReader.close();
            } else {
                // Error handling code goes here
                Log.d(LOG_TAG, "HTTP RESPONSE ! OK");
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
        return data;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");

        // Disconnect the Google API client and stop any ongoing discovery or advertising. When the
        // GoogleAPIClient is disconnected, any connected peers will get an onDisconnected callback.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // get current place

    // find places using elements from list


    /*@Override
    protected void onResume() {
        super.onResume();
        ((WeatherRecyclerViewAdapter) mAdapter).setOnItemClickListener(new WeatherRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }*/
}