package com.afifzafri.waktusolat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // onload, populate States spinner
        // test array
        String statelist[] = {"Select state...", "Perlis","Kedah","Pulau Pinang","Perak"};
        Spinner selectState = (Spinner)findViewById(R.id.selectState);
        // Populate the spinner with Array values
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, statelist);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        selectState.setAdapter(stateAdapter);

        // onload, populate Zone spinner
        // test array
        String zonelist[] = {"Select zone..."};
        Spinner selectZone = (Spinner)findViewById(R.id.selectZone);

        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, zonelist);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        selectZone.setAdapter(zoneAdapter);
    }
}
