package com.example.ic13;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CityAdapter extends ArrayAdapter<Trip> {
    public CityAdapter(@NonNull Context context, int resource, @NonNull List<Trip> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Trip trip = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.city_item, parent, false);
        }

        TextView tv_description_city = convertView.findViewById(R.id.tv_description);
        tv_description_city.setText(trip.city);

        return convertView;
    }
}