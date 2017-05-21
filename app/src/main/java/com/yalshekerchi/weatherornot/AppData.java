package com.yalshekerchi.weatherornot;

import com.johnhiott.darkskyandroidlib.models.DataPoint;

import java.util.ArrayList;

/**
 * Created by yalsh on 21-May-17.
 */

public class AppData {
    private ArrayList<DataPoint> weatherList;
    private static final AppData appdata = new AppData();

    public static AppData getInstance() { return appdata; }
    public ArrayList<DataPoint> getWeatherList() {
        return weatherList;
    }
    public void setWeatherList(ArrayList<DataPoint> weatherList) {
        this.weatherList = weatherList;
    }
}
