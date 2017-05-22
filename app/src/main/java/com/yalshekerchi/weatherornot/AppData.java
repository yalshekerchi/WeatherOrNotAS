package com.yalshekerchi.weatherornot;

import com.google.android.gms.location.places.Place;
import com.johnhiott.darkskyandroidlib.models.DataPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yalsh on 21-May-17.
 */

public class AppData {
    private double latitude;
    private double longitude;
    private ArrayList<DataPoint> weatherList;
    private Map<Integer, Integer> schedule;
    private Map<String, String[]> iconMap = new HashMap<String, String[]>();
    private Map<Integer, ArrayList<String>> resultMap = new HashMap<Integer, ArrayList<String>>();
    private Map<Integer, ArrayList<Place>> placeMap = new HashMap<Integer, ArrayList<Place>>();

    private static final AppData appdata = new AppData();

    {
        // iconMap initialization
        iconMap.put("clear-day", new String[] {"amusement_park", "zoo", "campground", "park", "stadium" } );
        iconMap.put("clear-night", iconMap.get("clear-day"));
        iconMap.put("partly-cloudy-day", new String[] { "night_club", "restaurant", "rv_park", "cafe", "florist"});
        iconMap.put("partly-cloudy-night", iconMap.get("partly-cloudy-day"));
        iconMap.put("cloudy-day", new String[] { "night_club", "restaurant", "rv_park", "cafe", "florist"});
        iconMap.put("cloudy-night", iconMap.get("cloudy-day"));
        iconMap.put("rain", new String[] { "art_gallery", "bowling_alley", "movie_theater", "aquarium", "bar" });
        iconMap.put("snow", iconMap.get("rain"));
        iconMap.put("sleet", iconMap.get("rain"));
        iconMap.put("wind", new String[] { "library", "museum", "shopping_mall", "bookstore", "gym" });
        iconMap.put("fog", iconMap.get("wind"));
    }


    public static AppData getInstance() {return appdata;}

    public ArrayList<DataPoint> getWeatherList() {
        return weatherList;
    }
    public void setWeatherList(ArrayList<DataPoint> weatherList) {
        this.weatherList = weatherList;
    }

    public Map<Integer, Integer> getSchedule() {
        return schedule;
    }
    public void setSchedule(Map<Integer, Integer> schedule) {
        this.schedule = schedule;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Map<String, String[]> getIconMap() {
        return this.iconMap;
    }
    public void setIconMap(Map<String, String[]> iconMap) {
        this.iconMap = iconMap;
    }

    public Map<Integer, ArrayList<String>> getResultMap() {
        return resultMap;
    }
    public void setResultMap(Map<Integer, ArrayList<String>> resultMap) {
        this.resultMap = resultMap;
    }

    public Map<Integer, ArrayList<Place>> getPlaceMap() {
        return placeMap;
    }
    public void setPlaceMap(Map<Integer, ArrayList<Place>> placeMap) {
        this.placeMap = placeMap;
    }
}
