package com.example.dell.bhatti;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener {
    public static String bundle;
    public GoogleMap map;
    double jLatitude_1,jLongitude_1, jLatitude_2, jLongitude_2;
    int jRating_1, jRating_2,id;
    String Rate_1, Rate_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map=((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                map.clear();
                map.setMyLocationEnabled(true);
                map.addMarker(new MarkerOptions().position(lng).title("You am here").snippet(String.valueOf(lng)).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myCoordinates, 15);
                map.animateCamera(yourLocation);
                new getData().execute(new JSONParser());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        LocationManager manage = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        manage.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, listener);



    }

    class getData extends AsyncTask<JSONParser, Long, JSONArray> {

        /**
         *
         */
        JSONArray ratingJArray;
        @Override
        protected JSONArray doInBackground(JSONParser... params) {
            Log.e("GETDATA","in background");
            ratingJArray = params[0].getData("http://tenzingsherpa.lixter.com/bhatti.php");
            return params[0].getData("http://tenzingsherpa.lixter.com/bhatti_location.php");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            try {
                setTextTo(ratingJArray);
                setTextTo(jsonArray);
                map.addMarker(new MarkerOptions().position(new LatLng(jLatitude_1, jLongitude_1)).title("Bhatti 1").snippet("Rating : " + String.valueOf(Rate_1)));
                map.addMarker(new MarkerOptions().position(new LatLng(jLatitude_2, jLongitude_2)).title("Bhatti 2").snippet("Rating : "+ String.valueOf(Rate_2)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setTextTo(JSONArray jsonArray) throws JSONException {

            int jsonArrayLength = jsonArray.length();
            Log.e("setText", " " + jsonArray.length());
            if (jsonArrayLength != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json;
                    try {
                        json = jsonArray.getJSONObject(i);
                        Log.e("JSONObject", String.valueOf(json));
                        id=json.getInt("Bhatti_id");
                        switch (id) {
                            case 1:
                                if (jRating_1<=0) {
                                    jRating_1 = json.getInt("rating");
                                }
                                jLatitude_1 = json.getDouble("latitude");
                                jLongitude_1 = json.getDouble("longitude");

                                break;
                            case 2:
                                if (jRating_2<=0) {
                                    jRating_2 = json.getInt("rating");
                                }
                                jLatitude_2 = json.getDouble("latitude");
                                jLongitude_2 = json.getDouble("longitude");

                                break;

                        }
                    } catch (JSONException e){

                    }
                    if (jRating_1==1){
                        Rate_1= "Bad";
                    }
                    if (jRating_1==2){
                        Rate_1= "Not Bad";
                    }else if (jRating_1==3){
                        Rate_1= "Satisfactory";
                    }else if (jRating_1==4){
                        Rate_1= "Good";
                    }else{
                        Rate_1= "Very Good";
                    }
                    if (jRating_2==1){
                        Rate_2= "Bad";
                    }else if (jRating_2==2){
                        Rate_2= "Not Bad";
                    }else if (jRating_2==3){
                        Rate_2= "Satisfactory";
                    }else if (jRating_2==4){
                        Rate_2= "Good";
                    }else{
                        Rate_2= "Very Good";
                    }
                    Log.e("map data",String.valueOf(jLatitude_1+" "+jLongitude_2 ) + " " + String.valueOf(Rate_1 + Rate_2));
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        int sendingData = id;
        Bundle bundle = new Bundle();
        if(arg0.getTitle().equals("MyHome")) // if marker source is clicked
            Toast.makeText(MainActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();
        bundle.putString("data", String.valueOf(sendingData));
        Intent intent = new Intent(MainActivity.this,review.class);
        intent.putExtras(bundle);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
