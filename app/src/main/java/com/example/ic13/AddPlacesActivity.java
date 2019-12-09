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

public class AddPlacesActivity extends AppCompatActivity implements PlacesAdapter.PlaceInterface {

    public Bundle extrasFromMain;
    public String TAG = "demo";
    public String latitude;
    public String longitude;
    public ListView lv_places;
    public PlacesAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);
        setTitle("Add Places");

        lv_places = findViewById(R.id.lv_places);



        if (isConnected()) {

            extrasFromMain = getIntent().getExtras().getBundle("bundlePlace");
            String placeId = (String) extrasFromMain.getSerializable("addPlace");
            Log.d(TAG, "Place Id: " + placeId);
            new GetDataAsync().execute("https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyB2XoCw_Z71dSqGoTPrwJNbGzTGcSNNBDA&placeid=" + placeId);

        } else {
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void passIntentToMainFromPlaces(Intent intent) {

        setResult( 5, intent);
    }

    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<String> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                Log.d("demo", "url: " + url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                Log.d(TAG, "connection " + connection.getResponseCode());
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject root = new JSONObject(json);
                    JSONObject resultJSON = root.getJSONObject("result");
                    JSONObject geometryJSON = resultJSON.getJSONObject("geometry");
                    JSONObject locationJSON = geometryJSON.getJSONObject("location");
                    Log.d(TAG, "locationJSON: " + locationJSON);
                    latitude = locationJSON.getString("lat");
                    longitude = locationJSON.getString("lng");
                    Log.d(TAG, "latitudeJSON: " + latitude);
                    Log.d(TAG, "longitudeJSON " + longitude);
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
        protected void onPostExecute(final ArrayList<String> result) {

             new GetPlacesAsync().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyB2XoCw_Z71dSqGoTPrwJNbGzTGcSNNBDA&location="+ latitude +"," + longitude + "&radius=1000");
        }


    }

    private class GetPlacesAsync extends AsyncTask<String, Void, ArrayList<Place>> {

        @Override
        protected ArrayList<Place> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Place> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                Log.d("demo", "url: " + url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject root = new JSONObject(json);
                    JSONArray resultJSON = root.getJSONArray("results");
                    Log.d(TAG, "resultJSON " + resultJSON.toString());
                    for (int i = 0; i < resultJSON.length(); i++) {
                        JSONObject nameJSONObject = resultJSON.getJSONObject(i);
                        Place place = new Place();
                        place.name = nameJSONObject.getString("name");
                        place.icon = nameJSONObject.getString("icon");
                       JSONObject geoObject =  nameJSONObject.getJSONObject("geometry");
                       JSONObject locObject = geoObject.getJSONObject("location");
                        place.latitude = locObject.getString("lat");
                        place.longitude = locObject.getString("lng");

                        Log.d(TAG, "geometry: " +  place.toString());

                        result.add( place);

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
        protected void onPostExecute(final ArrayList<Place> result) {

            adapter = new PlacesAdapter( AddPlacesActivity.this,R.layout.place_item, result);
            lv_places.setAdapter(adapter);

            lv_places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    result.get(i); // the selected place.
                    Log.d(TAG, "onItemClick: " + result.get(i));
                    Intent intentBackToMain = new Intent(AddPlacesActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectedPlace",  result.get(i));
                    intentBackToMain.putExtra("bundledPlace", bundle);
                    setResult(6, intentBackToMain);
                    finish();

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

