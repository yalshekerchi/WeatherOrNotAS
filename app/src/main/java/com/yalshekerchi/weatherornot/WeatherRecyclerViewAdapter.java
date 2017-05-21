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

import java.util.ArrayList;

public class WeatherRecyclerViewAdapter extends
        RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherDayHolder> {

    //Declare member variables
    private ArrayList<DataPoint> weatherDayList;
    public static final String TAG = "WEAVER_";

    public static class WeatherDayHolder extends RecyclerView.ViewHolder {
        CardView weatherCardView;
        TextView txtWeather1;
        TextView txtWeather2;
        ImageView imgWeatherIcon;

        WeatherDayHolder(View itemView) {
            super(itemView);
            weatherCardView = (CardView)itemView.findViewById(R.id.weatherCardView);
            txtWeather1 = (TextView)itemView.findViewById(R.id.txtWeather1);
            txtWeather2 = (TextView)itemView.findViewById(R.id.txtWeather2);
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
        WeatherDayHolder dataObjectHolder = new WeatherDayHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(WeatherDayHolder weatherDayHolder, int position) {
        weatherDayHolder.txtWeather1.setText(String.valueOf(weatherDayList.get(position).getApparentTemperatureMax()));
        weatherDayHolder.txtWeather2.setText(String.valueOf(weatherDayList.get(position).getApparentTemperatureMin()));
        //weatherDayHolder.personPhoto.setImageResource(weatherDayList.get(position).photoId);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}