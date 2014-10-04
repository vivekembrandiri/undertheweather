package com.weather.undertheweather;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.utils.CustomHttpClient;
import com.weather.utils.XMLUtil;

//import com.weather.utils.CustomHttpClient;

public class Result extends ActionBarActivity {

	private static final String WOEIDQUERYSTART = "https://query.yahooapis.com/v1/public/yql?q=select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
	private static final String WOEIDQUERYEND = "%22&format=xml&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	private static final String WEATHERSTART ="http://weather.yahooapis.com/forecastrss?";
	private static final int NUMBEROFDAYS = 4;

	private String location,unit,woeid;

	private TextView locationdisplay;
	private TextView datedisplay;
	private TextView textdisplay;
	private TextView temperaturedisplay;
	private TextView otherinfodisplay;
	private ImageView tempimagedisplay;
	private WeatherData weatherdata; 
	
	private TextView nexttextdisplay[];
	private ImageView nexttempimagedisplay[];
	private TextView nextotherinfodisplay[];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		locationdisplay 		= (TextView)findViewById(R.id.result_location);
		datedisplay 			= (TextView)findViewById(R.id.result_date);
		textdisplay 			= (TextView)findViewById(R.id.result_text);
		otherinfodisplay 		= (TextView)findViewById(R.id.result_otherinfo);
		tempimagedisplay 		= (ImageView)findViewById(R.id.result_temperature_bg);
		temperaturedisplay 		= (TextView)findViewById(R.id.result_temperature);
		
		nexttextdisplay 		= new TextView[NUMBEROFDAYS];
		nexttempimagedisplay	= new ImageView[NUMBEROFDAYS];
		nextotherinfodisplay	= new TextView[NUMBEROFDAYS];
		
		for(int i=1; i<=NUMBEROFDAYS; i++) {
			int textViewId 				= getResources().getIdentifier("result_nexttext"+i, "id", getPackageName());
			int imageViewId 			= getResources().getIdentifier("result_nexttemperature"+i, "id", getPackageName());
			int otherInfoId				= getResources().getIdentifier("result_nextotherinfo"+i, "id", getPackageName());
			
			nexttextdisplay[i-1] 			= (TextView)findViewById(textViewId);
			nexttempimagedisplay[i-1] 	= (ImageView)findViewById(imageViewId);
			nextotherinfodisplay[i-1] 	= (TextView)findViewById(otherInfoId);
		}

		weatherdata = new WeatherData();

		location = "Los Angeles, CA";
		unit = "F";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			location = extras.getString("selectedlocation");
			unit = extras.getString("selectedunit");
		}

		weatherdata.setLocation(location);
		weatherdata.setUnit(unit);

		try {
			location = URLEncoder.encode(location.toLowerCase(), "UTF-8");
			Log.i("Result","location = "+location);
			Log.i("Result","unit = "+unit);

			String result = CustomHttpClient.executeHttpPost(WOEIDQUERYSTART+location+WOEIDQUERYEND);

			woeid = XMLUtil.getWoeid(result);
			Log.i("Result","woeid = "+woeid);
			setUpViews();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.e("Result","UnsupportedEncodingException");
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error connecting to server!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.e("Result",e.toString());
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			setUpViews();
		}
		return super.onOptionsItemSelected(item);
	}

	private void setUpViews() {
		
		try {
			String result = CustomHttpClient.executeHttpPost(WEATHERSTART+"w="+woeid+"&u="+unit.toLowerCase());
			XMLUtil.populateWeatherData(result,weatherdata);

			locationdisplay.setText(weatherdata.getLocation()+", "+weatherdata.getCountry());
			datedisplay.setText(weatherdata.getDate());
			textdisplay.setText(weatherdata.getText());
			temperaturedisplay.setText(weatherdata.getCurrenttemp()+"\u00b0"+weatherdata.getUnit());

			if(weatherdata.getCode() >= 32)
				tempimagedisplay.setImageResource(R.drawable.sunny);
			else
				tempimagedisplay.setImageResource(R.drawable.cloudy);

			otherinfodisplay.setText( "Wind Speed: "+weatherdata.getWindspeed()+"\n"
					+"Humidity: "+weatherdata.getHumidity()+"\n"
					+"Visibility: "+weatherdata.getVisibility()+"\n"
					+"Low: "+weatherdata.getTodaymin()+"\n"
					+"High: "+weatherdata.getTodaymax());
			
			for(int i=0; i<NUMBEROFDAYS; i++) {
				nexttextdisplay[i].setText("Forecast for "+weatherdata.getNextday(i)+", "+weatherdata.getNextdate(i));
				
				if(weatherdata.getNextcode(i) >= 32)
					nexttempimagedisplay[i].setImageResource(R.drawable.sunny);
				else
					nexttempimagedisplay[i].setImageResource(R.drawable.cloudy);
	
				nextotherinfodisplay[i].setText( weatherdata.getNexttext(i)+"\n"
						+"Low: "+weatherdata.getNextmin(i)+"\n"
						+"High: "+weatherdata.getNextmax(i));
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error connecting to server!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.e("Result",e.toString());
		}
	}
}
