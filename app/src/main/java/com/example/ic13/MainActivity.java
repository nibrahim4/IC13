package com.example.ic13;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public Button btn_addTrip;
    public int REQ_CODE = 5;
    public Bundle extrasFromAddTrip;
    public static final String SIGNUP_KEY = "bundleTrip";
    public String TAG = "demo";
    public ListView lv_trips;
    public ArrayList<Trip> trips = new ArrayList<>();
    public TripAdapter adapter;
    public Button btn_addPlaces;
    public String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Trips");

        lv_trips = findViewById(R.id.lv_trips);

        btn_addTrip = findViewById(R.id.btn_add);


        //if(trips.size() > 0){
          adapter = new TripAdapter(this, R.layout.trip_item, trips);
            lv_trips.setAdapter(adapter);
        //}


        btn_addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToAdd = new Intent(MainActivity.this, AddTripActivity.class);
                startActivityForResult(intentToAdd, 5);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
                extrasFromAddTrip = data.getExtras().getBundle(SIGNUP_KEY);

               Trip createdTrip = (Trip) extrasFromAddTrip.getSerializable("addTrip");
                placeId = createdTrip.placeId;
                Log.d(TAG, "createdTrip: " + createdTrip.placeId);
               trips.add(createdTrip);

               // adapter.setNotifyOnChange(true);
                adapter.notifyDataSetChanged();
            }
        }
    }



}
