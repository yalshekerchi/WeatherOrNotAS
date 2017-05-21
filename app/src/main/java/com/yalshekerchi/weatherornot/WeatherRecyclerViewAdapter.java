package com.yalshekerchi.weatherornot;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnhiott.darkskyandroidlib.models.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherRecyclerViewAdapter extends
        RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherDayHolder> {

    //Declare member variables
    private ArrayList<DataPoint> weatherDayList;
    public static final String TAG = "WEAVER_";

    public static class WeatherDayHolder extends RecyclerView.ViewHolder {
        CardView weatherCardView;
        TextView txtTempHigh;
        TextView txtTempLow;
        TextView txtDate;
        TextView txtWeatherDescription;
        ImageView imgWeatherIcon;

        WeatherDayHolder(View itemView) {
            super(itemView);
            weatherCardView = (CardView)itemView.findViewById(R.id.weatherCardView);
            txtTempHigh = (TextView)itemView.findViewById(R.id.txtTempHigh);
            txtTempLow = (TextView)itemView.findViewById(R.id.txtTempLow);
            txtDate = (TextView)itemView.findViewById(R.id.txtDate);
            txtWeatherDescription = (TextView)itemView.findViewById(R.id.txtWeatherDescription) ;
            imgWeatherIcon = (ImageView)itemView.findViewById(R.id.imgWeatherIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    Log.d(TAG, "Item Clicked");
                }
            });
        }
    }

    WeatherRecyclerViewAdapter(ArrayList<DataPoint> weatherList){
        this.weatherDayList = weatherList;
    }

    @Override
    public int getItemCount() {
        return weatherDayList.size();
    }

    @Override
    public WeatherDayHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_row, viewGroup, false);
        return new WeatherDayHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherDayHolder weatherDayHolder, int position) {
        int tempHigh = (int)(weatherDayList.get(position).getApparentTemperatureMax() + 0.5);
        int tempLow = (int)(weatherDayList.get(position).getApparentTemperatureMin() + 0.5);
        weatherDayHolder.txtTempHigh.setText(String.valueOf(tempHigh) + "\u00B0");
        weatherDayHolder.txtTempLow.setText(String.valueOf(tempLow) + "\u00B0");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM d yyyy", Locale.getDefault());
        Date dateFormat = new java.util.Date(weatherDayList.get(position).getTime() * 1000L);
        weatherDayHolder.txtDate.setText(simpleDateFormat.format(dateFormat));

        String weatherDesc = weatherDayList.get(position).getSummary();
        weatherDayHolder.txtWeatherDescription.setText(weatherDesc);

        String iconVal = weatherDayList.get(position).getIcon();
        if (iconVal.equals("clear-day")) {
            weatherDayHolder.imgWeatherIcon.setImageResource(R.drawable.ic_sun);
        } else if (iconVal.equals("clear-night")) {
            weatherDayHolder.imgWeatherIcon.setImageResource(R.drawable.ic_moon);
        } else if (iconVal.equals("rain")) {
            weatherDayHolder.imgWeatherIcon.setImageResource(R.drawable.ic_cloud_rain);
        } else if (iconVal.equals("snow")) {
            weatherDayHolder.imgWeatherIcon.setImageResource(R.drawable.ic_cloud_snow);
        } else if (iconVal.equals("sleet")) {
            weatherDayHolder.imgWeatherIcon.setImageResource(R.drawable.ic_cloud_hail);
        } else if (iconVal.equals("wind")) {
            weatherDayHolder.imgWeatherIcon.setImageResource(R.drawable.ic_cloud_wind);
        } else if (iconVal.equals("fog")) {
            weatherDayHolder.imgWeatherIcon.setImageResource(R.drawable.ic_cloud_fog);
        } else if (iconVal.equals("cloudy")) {
            weatherDayHolder.imgWeatherIcon.setImageResource(R.drawable.ic_cloud);
        } else if (iconVal.equals("partly-cloudy-day")) {
            weatherDayHolder.imgWeatherIcon.setImageResource(R.drawable.ic_cloud_sun);
        } else if (iconVal.equals("partly-cloudy-night")) {
            weatherDayHolder.imgWeatherIcon.setImageResource(R.drawable.ic_cloud_moon);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}