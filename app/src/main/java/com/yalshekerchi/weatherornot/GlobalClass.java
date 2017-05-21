package com.yalshekerchi.weatherornot;

import android.app.Application;
import com.johnhiott.darkskyandroidlib.models.DataPoint;
import java.util.ArrayList;

/**
 * Created by yalsh on 21-May-17.
 */

public class GlobalClass extends Application {
    static ArrayList<DataPoint> weatherList = new ArrayList<>();
}
