package com.example.ic13;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PlacesAdapter extends ArrayAdapter<Place> {

    public PlaceInterface pi;

    public PlacesAdapter(@NonNull Context context, int resource, @NonNull List<Place> objects) {
        super(context, resource, objects);

        if (pi == null) {
            pi = (PlacesAdapter.PlaceInterface) context;
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Place place = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.place_item, parent, false);
        }

        TextView tv_tripName_trips = convertView.findViewById(R.id.tv_place);
        tv_tripName_trips.setText(place.name);

        ImageView iv_icon = convertView.findViewById(R.id.imageView);
        Picasso.get().load(place.icon).into(iv_icon);

        Button btn_select_place = convertView.findViewById(R.id.btn_select_place);
        btn_select_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo", "select places button clicked");
                Intent intentBackToMain = new Intent(getContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("addPlace", place);
                intentBackToMain.putExtra("bundlePlace", bundle);
                pi.passIntentToMainFromPlaces(intentBackToMain);
            }
        });

        return convertView;
    }

    public interface PlaceInterface {

        void passIntentToMainFromPlaces(Intent intent);
    }
}

