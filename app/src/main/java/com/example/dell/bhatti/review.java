package com.example.dell.bhatti;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.dell.bhatti.MainActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Intent.getIntent;

/**
 * Created by Dell on 6/24/2015.
 */
public class review extends Activity {
    String message;
    String id_1;
    String id_2;
    int jrating;
    String jtext;
        protected void onCreate(Bundle savedInstanceState, Intent getIntent) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_review);
            Intent intent = getIntent;
            String message = intent.getStringExtra(MainActivity.bundle);
            new getData().execute(new JSONParser());
        }


        class getData extends AsyncTask<JSONParser, Long, JSONArray> {

            /**
             *
             */
            JSONArray ratingJArray;

            @Override
            protected JSONArray doInBackground(JSONParser... params) {
                Log.e("GETDATA", "in background");
                ratingJArray = params[0].getData("http://tenzingsherpa.lixter.com/bhatti.php");
                return params[0].getData("http://tenzingsherpa.lixter.com/bhatti_review.php");
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                super.onPostExecute(jsonArray);
                try {
                    setTextTo(ratingJArray);
                    setTextTo(jsonArray);
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

                            id_1 = message;
                            id_2 = String.valueOf(json.getInt("Bhatti_id"));
                            if (id_1 == id_2) {
                                jtext = String.valueOf(json.get("review"));
                                TextView score = (TextView) findViewById(R.id.title1);
                                score.setText(id_2);
                                score = (TextView) findViewById(R.id.title2);
                                score.setText(jtext);
                            }

                        } catch (JSONException e) {

                        }
                    }
                }
            }
        }
    }

