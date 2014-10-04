package com.weather.undertheweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	private Button go;
	private Spinner locations, units;
	private String selectedlocation, selectedunit;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
    }
    
    private class LocationSpinnerActivity extends Activity implements OnItemSelectedListener {
	    @Override
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	selectedlocation = (String)parent.getItemAtPosition(pos);
	    }
	    
	    @Override
	    public void onNothingSelected(AdapterView<?> parent) {
	        // Another interface callback
	    }
	}
    
    private class UnitSpinnerActivity extends Activity implements OnItemSelectedListener {
	    @Override
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	selectedunit = (String)parent.getItemAtPosition(pos);
	    }
	    
	    @Override
	    public void onNothingSelected(AdapterView<?> parent) {
	        // Another interface callback
	    }
	}
    
    private void setUpViews() {		
		go 				= (Button)findViewById(R.id.main_button);
		locations 		= (Spinner)findViewById(R.id.main_locations);
		units 			= (Spinner)findViewById(R.id.main_units);
		
		ArrayAdapter<CharSequence> locationsadapter = ArrayAdapter.createFromResource(this, 
				R.array.locations_array, android.R.layout.simple_spinner_item);
		locationsadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locations.setAdapter(locationsadapter);
		selectedlocation = locationsadapter.getItem(0).toString();
		locations.setOnItemSelectedListener(new LocationSpinnerActivity());
		
		ArrayAdapter<CharSequence> unitsadapter = ArrayAdapter.createFromResource(this, 
				R.array.units_array, android.R.layout.simple_spinner_item);
		unitsadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		units.setAdapter(unitsadapter);
		selectedunit = unitsadapter.getItem(0).toString();
		units.setOnItemSelectedListener(new UnitSpinnerActivity());
		
		go.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				
				Toast.makeText(getApplicationContext(), "Loading, please wait...", Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(MainActivity.this, Result.class);
				intent.putExtra("selectedlocation", selectedlocation);
				intent.putExtra("selectedunit", (selectedunit.substring(0, 1)));
				startActivity(intent);
			}
		});
    }
}
