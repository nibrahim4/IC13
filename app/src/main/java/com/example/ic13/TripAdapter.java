package com.example.ic13;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TripAdapter extends ArrayAdapter<Trip> {

    public TripInterface ti;

    public TripAdapter(@NonNull Context context, int resource, @NonNull List<Trip> objects) {
        super(context, resource, objects);
        ti = (TripAdapter.TripInterface) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Trip trip = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trip_item, parent, false);
        }

        TextView tv_tripName_trips = convertView.findViewById(R.id.tv_description);
        tv_tripName_trips.setText(trip.tripName);

        TextView tv_city_trips = convertView.findViewById(R.id.tv_city_trips);
        tv_city_trips.setText(trip.city);

        Button btn_addPlaces = convertView.findViewById(R.id.btn_addPlace);

        btn_addPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToAddPlaces = new Intent(getContext(), AddPlacesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("addPlace", trip.placeId);
                intentToAddPlaces.putExtra("bundlePlace", bundle);
                ti.passIntentToMain(intentToAddPlaces);
            }
        });
        return convertView;
    }

    public interface TripInterface {

        void passIntentToMain(Intent intent);
    }
}

