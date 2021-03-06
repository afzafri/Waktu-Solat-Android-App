package com.afifzafri.waktusolat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        // Progress Bar
        final FrameLayout loadingFrame = (FrameLayout)findViewById(R.id.loadingFrame);
        // State select box
        final Spinner selectState = (Spinner)findViewById(R.id.selectState);
        // Zone select box
        final Spinner selectZone = (Spinner)findViewById(R.id.selectZone);
        // Output area
        final TableLayout outputArea = (TableLayout)findViewById(R.id.outputArea);

        // onload, populate States spinner
        // Request States list json object response from the provided API URL.
        RequestQueue queueState = Volley.newRequestQueue(getApplicationContext()); // Instantiate the RequestQueue.
        loadingFrame.setVisibility(View.VISIBLE);// show loading progress bar
        JsonObjectRequest stateRequests = new JsonObjectRequest
                (Request.Method.GET, AppHelper.API+"?getStates", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // Iterate through the JSONObject and store into array, for populating spinner
                // State list ArrayList
                ArrayList<String> statelist = new ArrayList<String>();
                statelist.add("Select state..."); // set default first element in the spinner

                Iterator<String> iter = response.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        Object value = response.get(key);
                        statelist.add(value.toString());
                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }

                // Populate the spinner with Array values
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(),   android.R.layout.simple_spinner_item, statelist);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                selectState.setAdapter(stateAdapter);

                Toast.makeText(getApplicationContext(), "States list fetched.", Toast.LENGTH_SHORT).show();
                loadingFrame.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Toast.makeText(getApplicationContext(), "Error fetch states list.", Toast.LENGTH_SHORT).show();
                loadingFrame.setVisibility(View.GONE);
            }
        });

        // Add the request to the RequestQueue.
        queueState.add(stateRequests);

        // Event listener for Select State
        selectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                outputArea.setVisibility(View.GONE);// clear the output area
                // Check if selected value is index 0, which is no value, do nothing
                if(i != 0) {
                    loadingFrame.setVisibility(View.VISIBLE);// show loading progress bar

                    String selectedVal = adapterView.getSelectedItem().toString();

                    RequestQueue queueZone = Volley.newRequestQueue(getApplicationContext()); // Instantiate the RequestQueue.
                    loadingFrame.setVisibility(View.VISIBLE);// show loading progress bar
                    JsonObjectRequest zoneRequests = new JsonObjectRequest
                            (Request.Method.GET, AppHelper.API+"?stateName="+selectedVal, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    // Zone ID Array
                                    ArrayList<StringWithTag> zonelist = new ArrayList<StringWithTag>();
                                    zonelist.add(new StringWithTag(null, "Select zone...")); // set default first element in the spinner

                                    Iterator<String> iter = response.keys();
                                    while (iter.hasNext()) {
                                        String key = iter.next();
                                        try {
                                            Object value = response.get(key);
                                            zonelist.add(new StringWithTag(key, value.toString()));
                                        } catch (JSONException e) {
                                            // Something went wrong!
                                        }
                                    }

                                    // Populate the spinner with Array values
                                    ArrayAdapter<StringWithTag> zoneAdapter = new ArrayAdapter<StringWithTag>(getApplicationContext(),   android.R.layout.simple_spinner_item, zonelist);
                                    zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                    selectZone.setAdapter(zoneAdapter);

                                    Toast.makeText(getApplicationContext(), "Zones list fetched.", Toast.LENGTH_SHORT).show();
                                    loadingFrame.setVisibility(View.GONE);
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                    Toast.makeText(getApplicationContext(), "Error fetch zones list.", Toast.LENGTH_SHORT).show();
                                    loadingFrame.setVisibility(View.GONE);
                                }
                            });

                    // Add the request to the RequestQueue.
                    queueZone.add(zoneRequests);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Event listener for Select Zone
        selectZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                outputArea.setVisibility(View.GONE);// clear the output area
                // Check if selected value is index 0, which is no value, do nothing
                if(i != 0) {
                    StringWithTag zone = (StringWithTag) adapterView.getItemAtPosition(i); // access the custom class
                    String zoneID = (String) zone.key; // get the key

                    RequestQueue queueWaktuSolat = Volley.newRequestQueue(getApplicationContext()); // Instantiate the RequestQueue.
                    loadingFrame.setVisibility(View.VISIBLE);// show loading progress bar
                    JsonObjectRequest waktuSolatRequests = new JsonObjectRequest
                            (Request.Method.GET, AppHelper.API+"?zon="+zoneID, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        // Initialize TextView variables for the output
                                        TextView outImsak = (TextView)findViewById(R.id.outImsak);
                                        TextView outSubuh = (TextView)findViewById(R.id.outSubuh);
                                        TextView outSyuruk = (TextView)findViewById(R.id.outSyuruk);
                                        TextView outZohor = (TextView)findViewById(R.id.outZohor);
                                        TextView outAsar = (TextView)findViewById(R.id.outAsar);
                                        TextView outMaghrib = (TextView)findViewById(R.id.outMaghrib);
                                        TextView outIsyak = (TextView)findViewById(R.id.outIsyak);
                                        TextView outDateTime = (TextView)findViewById(R.id.outDateTime);

                                        // Parse the json get store the data
                                        String imsak = response.getString("waktu_imsak");
                                        String subuh = response.getString("waktu_subuh");
                                        String syuruk = response.getString("waktu_syuruk");
                                        String zohor = response.getString("waktu_zohor");
                                        String asar = response.getString("waktu_asar");
                                        String maghrib = response.getString("waktu_maghrib");
                                        String isyak = response.getString("waktu_isyak");
                                        String tarikhmasa = response.getString("tarikh_masa");

                                        // Set the data to output
                                        outImsak.setText(imsak);
                                        outSubuh.setText(subuh);
                                        outSyuruk.setText(syuruk);
                                        outZohor.setText(zohor);
                                        outAsar.setText(asar);
                                        outMaghrib.setText(maghrib);
                                        outIsyak.setText(isyak);
                                        outDateTime.setText(tarikhmasa);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Toast.makeText(getApplicationContext(), "Waktu Solat fetched.", Toast.LENGTH_SHORT).show();
                                    loadingFrame.setVisibility(View.GONE);
                                    outputArea.setVisibility(View.VISIBLE);
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                    Toast.makeText(getApplicationContext(), "Error fetch Waktu Solat.", Toast.LENGTH_SHORT).show();
                                    loadingFrame.setVisibility(View.GONE);
                                }
                            });

                    // Add the request to the RequestQueue.
                    queueWaktuSolat.add(waktuSolatRequests);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}