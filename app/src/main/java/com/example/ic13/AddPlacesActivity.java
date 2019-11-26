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

public class AddPlacesActivity extends AppCompatActivity {

    public Bundle extrasFromMain;
    public String TAG = "demo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);

        if (isConnected()) {

            extrasFromMain = getIntent().getExtras().getBundle("bundlePlaces");
           String placeId = (String) extrasFromMain.getSerializable("addPlace");
            new GetDataAsync().execute("https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyB2XoCw_Z71dSqGoTPrwJNbGzTGcSNNBDA&placeId=" + placeId);

        } else {
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
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
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject root = new JSONObject(json);
                    JSONArray predictions = root.getJSONArray("result");
                    Log.d(TAG, "predictionsJSONObject: " + predictions);
                    for (int i = 0; i < predictions.length(); i++) {

                        JSONObject predictionsJSONObject = predictions.getJSONObject(i);

                        result.add(predictionsJSONObject.getString("description"));

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
        protected void onPostExecute(final ArrayList<String> result) {

            new GetPlacesAsync().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyB2XoCw_Z71dSqGoTPrwJNbGzTGcSNNBDA&location=" + "&radius=1000");
        }


    }

    private class GetPlacesAsync extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<String> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                Log.d("demo", "url: " + url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject root = new JSONObject(json);
                    JSONArray predictions = root.getJSONArray("predictions");

                    for (int i = 0; i < predictions.length(); i++) {

                        JSONObject predictionsJSONObject = predictions.getJSONObject(i);

                        result.add(predictionsJSONObject.getString("description"));
                        //Log.d(TAG, "predictionsJSONObject: " + predictionsJSONObject);
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
        protected void onPostExecute(final ArrayList<String> result) {

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

