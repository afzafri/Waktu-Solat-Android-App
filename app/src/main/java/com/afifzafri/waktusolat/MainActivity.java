package com.afifzafri.waktusolat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // API URL
        final String url = "http://192.168.43.90/waktusolat/api.php";

        // Initialize UI elements
        // Progress Bar
        final ProgressBar loading = (ProgressBar)findViewById(R.id.loading);
        // State select box
        final Spinner selectState = (Spinner)findViewById(R.id.selectState);
        // Zone select box
        final Spinner selectZone = (Spinner)findViewById(R.id.selectZone);

        final TextView testRes = (TextView)findViewById(R.id.testRes);

        // onload, populate States spinner
        // Request States list json object response from the provided API URL.
        RequestQueue queueState = Volley.newRequestQueue(getApplicationContext()); // Instantiate the RequestQueue.
        loading.setVisibility(View.VISIBLE);// show loading progress bar
        JsonObjectRequest stateRequests = new JsonObjectRequest
                (Request.Method.GET, url+"?getStates", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // Iterate through the JSONObject and store into array, for populating spinner
                int nostates = response.length() + 1;
                // State list array
                String statelist[] = new String[nostates];
                statelist[0] = "Select state..."; // set default first element in the spinner
                for(int i=1;i<nostates;i++) {
                    try {
                        statelist[i] = response.getString(String.valueOf(i-1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Populate the spinner with Array values
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(),   android.R.layout.simple_spinner_item, statelist);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                selectState.setAdapter(stateAdapter);

                Toast.makeText(getApplicationContext(), "States list fetched.", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Toast.makeText(getApplicationContext(), "Error fetch states list.", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        });

        // Add the request to the RequestQueue.
        queueState.add(stateRequests);

        // Event listener for Select State
        selectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Check if selected value is index 0, which is no value, do nothing
                if(i != 0) {
                    loading.setVisibility(View.VISIBLE);// show loading progress bar

                    String selectedVal = adapterView.getSelectedItem().toString();

                    RequestQueue queueZone = Volley.newRequestQueue(getApplicationContext()); // Instantiate the RequestQueue.
                    loading.setVisibility(View.VISIBLE);// show loading progress bar
                    JsonObjectRequest zoneRequests = new JsonObjectRequest
                            (Request.Method.GET, url+"?stateName="+selectedVal, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    //testRes.setText(response.toString());

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
                                    loading.setVisibility(View.GONE);
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                    Toast.makeText(getApplicationContext(), "Error fetch zones list.", Toast.LENGTH_SHORT).show();
                                    loading.setVisibility(View.GONE);
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

                // Check if selected value is index 0, which is no value, do nothing
                if(i != 0) {
                    loading.setVisibility(View.VISIBLE);// show loading progress bar

                    StringWithTag zone = (StringWithTag) adapterView.getItemAtPosition(i); // access the custom class
                    String zoneID = (String) zone.key; // get the key

                    testRes.setText(zoneID);

                    loading.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
