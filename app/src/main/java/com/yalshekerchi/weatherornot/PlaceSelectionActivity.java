package com.yalshekerchi.weatherornot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

public class PlaceSelectionActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "PlaceSelectionActivity";

    //Google Play Services API
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_selection);

        //Get Variables passed from PlaceSelectionActivity
        Bundle extras = getIntent().getExtras();
        int position = extras.getInt("position");
        Log.d(LOG_TAG, "POSITION: " + position);

        //Setup RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.placeRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        //Setup RecyclerView Layout Manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Build Google API Client
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }

        //Initialize RecyclerView
        mAdapter = new PlaceRecyclerViewAdapter(new ArrayList<Place>());
        mRecyclerView.setAdapter(mAdapter);

        //Get Place objects using list of place-ids
        ArrayList<String> idList = AppData.getInstance().getResultMap().get(position);
        getPlaces(idList);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    private ArrayList<Place> getPlaces(ArrayList<String> idList){
        final ArrayList<Place> placeList = new ArrayList<Place>();

        for (int i = 0; i < idList.size(); i++){
            String placeID = idList.get(i);
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeID)
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                final Place myPlace = places.get(0);
                                Log.i(LOG_TAG, "Place found: " + myPlace.getName());
                                ((PlaceRecyclerViewAdapter) mAdapter).addItem(myPlace, mAdapter.getItemCount());

                                placeList.add(myPlace);
                            } else {
                                Log.e(LOG_TAG, "Place not found");
                            }
                        }
                    });
        }
        return placeList;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(LOG_TAG, "GoogleApiClient connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "GoogleApiClient connection suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
}
