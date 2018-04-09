package com.afifzafri.waktusolat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        // Progress Bar
        final ProgressBar loading = (ProgressBar)findViewById(R.id.loading);
        // State select box
        Spinner selectState = (Spinner)findViewById(R.id.selectState);
        // Zone select box
        Spinner selectZone = (Spinner)findViewById(R.id.selectZone);

        // onload, populate States spinner
        // test array
        String statelist[] = {"Select state...", "Perlis","Kedah","Pulau Pinang","Perak"};

        // Populate the spinner with Array values
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, statelist);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        selectState.setAdapter(stateAdapter);

        selectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Check if selected value is index 0, which is no value, do nothing
                int selectedIndex = adapterView.getSelectedItemPosition();
                if(selectedIndex != 0) {
                    loading.setVisibility(View.VISIBLE);// show loading progress bar

                    String selectedVal = adapterView.getSelectedItem().toString();
                    Toast.makeText(getApplicationContext(), "Selected: "+selectedVal+ " & Index: "+selectedIndex, Toast.LENGTH_SHORT).show();

                    loading.setVisibility(View.GONE); // hide
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // onload, populate Zone spinner
        // test array
        String zonelist[] = {"Select zone..."};

        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, zonelist);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        selectZone.setAdapter(zoneAdapter);
    }
}
