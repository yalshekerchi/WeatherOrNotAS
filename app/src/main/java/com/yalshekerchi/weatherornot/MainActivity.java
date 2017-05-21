package com.yalshekerchi.weatherornot;

//Support Libraries
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//Utility Libraries
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.view.View;
import android.Manifest;
import android.util.Log;

//Widget Libraries
import android.widget.Toast;
import android.widget.TextView;
import android.widget.NumberPicker;
import android.widget.Button;

//Location Libraries
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.johnhiott.darkskyandroidlib.ForecastApi;

//Geolocator Libraries
import android.location.Geocoder;
import android.location.Address;

import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.DataPoint;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final String TAG = "WEAVER_";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    //GPS Constant Permission=
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    //Position
    private static final int LOCATION_REFRESH_TIME = 1000;  // 1s
    private static final int LOCATION_REFRESH_DISTANCE = 50; // 50m

    /* GPS */
    private Location mLastLocation;
    public LocationManager mLocationManager;
    int updates;
    double valLatitude;
    double valLongitude;
    String valAddress;

    //Geocoder - GeoLocation to Address
    Geocoder geocoder;
    List<Address> addresses;

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
        updates = 0;

        //Handle Location Permissions
        handlePermissionsAndGetLocation();

        //Setup Dark Sky API
        ForecastApi.create(DARK_SKY_API_KEY);

        //Setup Variables
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);

        //Disable Next Button
        //btnNext.setEnabled(false);

        //Detect Button Clicks
        //btnNext.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onClick(View v)
    {
        //if (v == btnNext)
        //{
        //    Intent intent = new Intent(getApplicationContext(), DaySelectionActivity.class);
        //    intent.putExtra("valLatitude", valLatitude);
        //    intent.putExtra("valLongitude", valLongitude);
        //    startActivity(intent);
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Accepted
                    getLocation();
                } else {
                    // Denied
                    Toast.makeText(MainActivity.this, "LOCATION Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void handlePermissionsAndGetLocation() {
        Log.v(TAG, "handlePermissionsAndGetLocation");
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        getLocation();//if already has permission
    }

    protected void getLocation() {
        Log.v(TAG, "GetLocation");
        int LOCATION_REFRESH_TIME = 1000;
        int LOCATION_REFRESH_DISTANCE = 5;

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Log.v("WEAVER_", "Has permission");
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        } else {
            Log.v("WEAVER_", "Does not have permission");
        }

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            System.out.println("onLocationChanged");
            mLastLocation = location;

            valLatitude = location.getLatitude();
            valLongitude = location.getLongitude();
            valAddress = getCompleteAddressString(valLatitude, valLongitude);

            txtLatitude.setText("Latitude:" + String.valueOf(valLatitude)) ;
            txtLongitude.setText("Longitude:" + String.valueOf(valLongitude));
            txtAddress.setText(valAddress);

            //Enable Next Button
            //btnNext.setEnabled(true);

            //Get Weather
            getWeatherResponse();
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
    };

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

    private ArrayList<DataPoint> getWeatherResponse() {
        final ArrayList<DataPoint> weatherList = new ArrayList<>();

        RequestBuilder weather = new RequestBuilder();

        Request request = new Request();
        request.setLat(String.valueOf(valLatitude));
        request.setLng(String.valueOf(valLongitude));
        request.setUnits(Request.Units.AUTO);
        request.setLanguage(Request.Language.ENGLISH);
        request.addExcludeBlock(Request.Block.CURRENTLY);
        request.addExcludeBlock(Request.Block.HOURLY);
        request.addExcludeBlock(Request.Block.MINUTELY);
        request.addExcludeBlock(Request.Block.ALERTS);
        request.addExcludeBlock(Request.Block.FLAGS);

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

        return weatherList;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}