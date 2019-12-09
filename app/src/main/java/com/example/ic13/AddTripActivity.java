package com.example.ic13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AddTripActivity extends AppCompatActivity {

    public EditText et_enteredCity;
    public EditText et_tripName;
    public ListView lv_city;
    public Button btn_search;
    public Button btn_addTrip;
    public String TAG = "demo";
    public ArrayList<String> cities;
    public ArrayAdapter<Trip> adapter;
    public String selectedCity;
    public String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setTitle("Add Trip");

        et_tripName = findViewById(R.id.et_tripName);
        et_enteredCity = findViewById(R.id.et_city);
        btn_search = findViewById(R.id.btn_search);
        btn_addTrip = findViewById(R.id.btn_addTrip);
        lv_city = findViewById(R.id.lv_cities);

        if(isConnected()){

            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new GetCitiesAsync().execute("https://maps.googleapis.com/maps/api/place/autocomplete/json?types=(cities)&input=" + et_enteredCity.getText().toString() + "&key=AIzaSyB2XoCw_Z71dSqGoTPrwJNbGzTGcSNNBDA");
                }
            });
        }else{
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_LONG).show();
        }

    }

    private class GetCitiesAsync extends AsyncTask<String, Void, ArrayList<Trip>> {

        @Override
        protected ArrayList<Trip> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Trip> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                Log.d("demo", "url: " + url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                Log.d(TAG, "response code: " + connection.getResponseCode());
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String  json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject root = new JSONObject(json);
                    JSONArray predictions = root.getJSONArray("predictions");
                    Log.d(TAG, "predictions " + predictions);
                    for (int i=0; i<predictions.length(); i++){

                        JSONObject predictionsJSONObject = predictions.getJSONObject(i);
                        Trip trip = new Trip();
                       // trip.tripName = et_tripName.getText().toString();
                        trip.city = predictionsJSONObject.getString("description");
                        trip.placeId = predictionsJSONObject.getString("place_id");
                        result.add(trip);
                        Log.d(TAG, "predictionsJSONObject: " + predictionsJSONObject);
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<Trip> result) {
            Log.d(TAG, "onPostExecute: RESULT" + result.toString());
           adapter =  new CityAdapter(AddTripActivity.this, R.layout.city_item, result);
            lv_city.setAdapter(adapter);

            lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedCity = result.get(i).city;
                    et_enteredCity.setText(result.get(i).city);
                    placeId = result.get(i).placeId;
                }
            });
            btn_addTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(et_tripName.getText().toString().equals("")){
                        Toast.makeText(AddTripActivity.this, "Please enter a trip name!", Toast.LENGTH_LONG).show();
                    }else{
                        Trip trip = new Trip();
                        trip.tripName = et_tripName.getText().toString();
                        trip.city = selectedCity;
                        trip.placeId = placeId;
                        Intent intentToMain = new Intent(AddTripActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("addTrip", trip);
                        intentToMain.putExtra("bundleTrip", bundle);
                        setResult(AddTripActivity.RESULT_OK, intentToMain);
                        finish();
                    }

                }
            });
        }


    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}
