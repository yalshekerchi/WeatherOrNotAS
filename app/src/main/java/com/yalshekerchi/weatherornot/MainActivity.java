package com.yalshekerchi.weatherornot;

//Support Libraries

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.DataPoint;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//Utility Libraries
//Widget Libraries
//Location Libraries
//Geolocator Libraries


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "MainActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    //GPS Constant Permission
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    /* GPS */
    private Location mLastLocation;
    double valLatitude;
    double valLongitude;
    String valAddress;

    //Geocoder - GeoLocation to Address
    Geocoder geocoder;
    List<Address> addresses;

    //Google Play Services API
    private GoogleApiClient mGoogleApiClient;

    //Dark Sky API Key
    java.lang.String DARK_SKY_API_KEY = "033525848b492ba1fc38edb5da6d0947";

    //UI Elements Variables
    TextView txtAddress;
    TextView txtLatitude;
    TextView txtLongitude;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "OnCreate");

        //Setup Dark Sky API
        ForecastApi.create(DARK_SKY_API_KEY);

        //Setup Variables
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        btnNext = (Button) findViewById(R.id.btnNext);

        //Detect Button Clicks
        btnNext.setOnClickListener(this);

        //Disable Next Button
        btnNext.setEnabled(false);

        buildGoogleApiClient();
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnNext) {
            Intent intent = new Intent(getApplicationContext(), DaySelectionActivity.class);
            intent.putExtra("valLatitude", valLatitude);
            intent.putExtra("valLongitude", valLongitude);
            startActivity(intent);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            valLatitude = mLastLocation.getLatitude();
            valLongitude = mLastLocation.getLongitude();
            valAddress = getCompleteAddressString(valLatitude, valLongitude);

            txtLatitude.setText("Latitude:" + String.valueOf(valLatitude));
            txtLongitude.setText("Longitude:" + String.valueOf(valLongitude));
            txtAddress.setText(valAddress);

            if (btnNext.isEnabled() == false) {
                getWeatherResponse();
            }
            //Enable Next Button
            btnNext.setEnabled(true);
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                strAdd = "My Current location address: No Address returned!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            strAdd = "My Current location address: Cannot get Address!";
        }
        return strAdd;
    }

    private void getWeatherResponse() {
        RequestBuilder weather = new RequestBuilder();
        final ArrayList<DataPoint> weatherList = new ArrayList<DataPoint>();

        Request request = new Request();
        request.setLat(String.valueOf(valLatitude));
        request.setLng(String.valueOf(valLongitude));
        request.setUnits(Request.Units.AUTO);
        request.setLanguage(Request.Language.ENGLISH);
        request.addExcludeBlock(Request.Block.CURRENTLY);
        request.addExcludeBlock(Request.Block.HOURLY);
        request.addExcludeBlock(Request.Block.MINUTELY);
        request.addExcludeBlock(Request.Block.ALERTS);

        weather.getWeather(request, new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                Log.d(TAG, "Temp: " + weatherResponse.getDaily().getSummary());

                int numDays = weatherResponse.getDaily().getData().size();
                for (int i = 0; i < numDays; i++)
                {
                    weatherList.add(weatherResponse.getDaily().getData().get(i));
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "Error while calling: " + retrofitError.getUrl());
                Log.d(TAG, retrofitError.toString());
            }
        });
        AppData.getInstance().setWeatherList(weatherList);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
}