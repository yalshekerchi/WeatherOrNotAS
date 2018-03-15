package com.yalshekerchi.weatherornot;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by yalsh on 22-May-17.
 */

public class PlaceRecyclerViewAdapter extends
        RecyclerView.Adapter<PlaceRecyclerViewAdapter.PlaceLocationHolder> {

    //Declare member variables
    public static final String TAG = "PRViewAdapter";
    private ArrayList<Place> placeLocationList;

    public static class PlaceLocationHolder extends RecyclerView.ViewHolder {
        CardView weatherCardView;
        TextView txtPlaceName;
        TextView txtPlaceRating;
        TextView txtPlaceAddress;

        PlaceLocationHolder(View itemView) {
            super(itemView);
            weatherCardView = (CardView)itemView.findViewById(R.id.weatherCardView);
            txtPlaceName = (TextView)itemView.findViewById(R.id.txtPlaceName);
            txtPlaceRating = (TextView)itemView.findViewById(R.id.txtPlaceRating);
            txtPlaceAddress = (TextView)itemView.findViewById(R.id.txtPlaceAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                Context context = v.getContext();

                // item clicked
                Log.d(TAG, "Item Clicked");
                }
            });
        }
    }

    PlaceRecyclerViewAdapter(ArrayList<Place> placeLocationList){
        this.placeLocationList = placeLocationList;
    }

    @Override
    public int getItemCount() {
        return placeLocationList.size();
    }

    @Override
    public PlaceLocationHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_card_view_row, viewGroup, false);
        return new PlaceLocationHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceLocationHolder placeLocationHolder, int position) {
        placeLocationHolder.txtPlaceName.setText(placeLocationList.get(position).getName());
        placeLocationHolder.txtPlaceAddress.setText(placeLocationList.get(position).getAddress());
        double placeRating = placeLocationList.get(position).getRating();
        if (placeRating == -1)
        {
            placeLocationHolder.txtPlaceRating.setText("-");
        }
        else
        {
            placeLocationHolder.txtPlaceRating.setText("" + placeLocationList.get(position).getRating());
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addItem(Place place, int index) {
        placeLocationList.add(index, place);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        placeLocationList.remove(index);
        notifyItemRemoved(index);
    }

}