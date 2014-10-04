package com.weather.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.weather.undertheweather.WeatherData;

import android.util.Log;

public class XMLUtil {
	
	private static final int NUMBEROFDAYS = 4;
	
	private static Document convertStringToDocument(String src) {
		Document document = null;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			document = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e) {
			Log.e("XMLUtil","ParserConfigurationException");
		} catch (SAXException e) {
			Log.e("XMLUtil","SAXException");
		} catch (IOException e) {
			Log.e("XMLUtil","IOException");
		} catch (Exception e) {
			Log.e("XMLUtil",e.toString());
		}
		return document;
	}
	
	public static String getWoeid(String src) {
		String woeid = "";
		
		Document doc = convertStringToDocument(src);
		woeid = doc.getElementsByTagName("woeid").item(0).getTextContent();
		Log.i("XMLUtils","WOEID = "+woeid);
		
		return woeid;
	}
	
	public static void populateWeatherData(String src, WeatherData weatherdata) {		
		Document doc = convertStringToDocument(src);
		
		//<yweather:location city="Los Angeles" region="CA" country="United States"/>
		Node locationNode = doc.getElementsByTagName("yweather:location").item(0);
		weatherdata.setCountry(locationNode.getAttributes().getNamedItem("country").getNodeValue());
		
		//<yweather:condition text="Fair" code="34" temp="96" date="Fri, 03 Oct 2014 1:46 pm PDT"/>
		Node currentConditionNode = doc.getElementsByTagName("yweather:condition").item(0);
		weatherdata.setCode(Integer.parseInt(currentConditionNode.getAttributes().getNamedItem("code").getNodeValue()));
		weatherdata.setText(currentConditionNode.getAttributes().getNamedItem("text").getNodeValue());
		weatherdata.setCurrenttemp(currentConditionNode.getAttributes().getNamedItem("temp").getNodeValue());
		weatherdata.setDate(doc.getElementsByTagName("pubDate").item(0).getTextContent());
		
		//<yweather:units temperature="F" distance="mi" pressure="in" speed="mph"/>
		//<yweather:wind chill="96" direction="280" speed="6"/>
		//<yweather:atmosphere humidity="8" visibility="10" pressure="29.96" rising="2"/>
		Node weatherUnits = doc.getElementsByTagName("yweather:units").item(0);
		Node wind = doc.getElementsByTagName("yweather:wind").item(0);
		Node atmosphere = doc.getElementsByTagName("yweather:atmosphere").item(0);
		
		weatherdata.setWindspeed(wind.getAttributes().getNamedItem("speed").getNodeValue()
				+" "+weatherUnits.getAttributes().getNamedItem("speed").getNodeValue());
		weatherdata.setHumidity(atmosphere.getAttributes().getNamedItem("humidity").getNodeValue()+" %");
		weatherdata.setVisibility(atmosphere.getAttributes().getNamedItem("visibility").getNodeValue()
				+" "+weatherUnits.getAttributes().getNamedItem("distance").getNodeValue());
		
		//<yweather:forecast day="Fri" date="3 Oct 2014" low="70" high="94" text="Mostly Sunny" code="34"/>
		Node forecast = doc.getElementsByTagName("yweather:forecast").item(0);
		weatherdata.setTodaymin(forecast.getAttributes().getNamedItem("low").getNodeValue()+"\u00b0"+weatherdata.getUnit());
		weatherdata.setTodaymax(forecast.getAttributes().getNamedItem("high").getNodeValue()+"\u00b0"+weatherdata.getUnit());
		
		//<yweather:forecast day="Sat" date="4 Oct 2014" low="69" high="97" text="Partly Cloudy" code="30"/>
		for(int i=0; i<NUMBEROFDAYS; i++) {
			Node forecastnext = doc.getElementsByTagName("yweather:forecast").item(i+1);
			weatherdata.setNextcode(Integer.parseInt(forecastnext.getAttributes().getNamedItem("code").getNodeValue()), i);
			weatherdata.setNextday(forecastnext.getAttributes().getNamedItem("day").getNodeValue(), i);
			weatherdata.setNextdate(forecastnext.getAttributes().getNamedItem("date").getNodeValue(), i);
			weatherdata.setNextmin((forecastnext.getAttributes().getNamedItem("low").getNodeValue()+"\u00b0"+weatherdata.getUnit()), i);
			weatherdata.setNextmax((forecastnext.getAttributes().getNamedItem("high").getNodeValue()+"\u00b0"+weatherdata.getUnit()), i);
			weatherdata.setNexttext(forecastnext.getAttributes().getNamedItem("text").getNodeValue(), i);
		}
		
		Log.i("XMLUtils","COUNTRY = "+weatherdata.getCountry());
		Log.i("XMLUtils","TEXT = "+weatherdata.getText());
		Log.i("XMLUtils","CURRENT TEMP = "+weatherdata.getCurrenttemp());
		Log.i("XMLUtils","DATE = "+weatherdata.getDate());
		Log.i("XMLUtils","WIND SPEED = "+weatherdata.getWindspeed());
		Log.i("XMLUtils","HUMIDITY = "+weatherdata.getHumidity());
		Log.i("XMLUtils","VISIBILITY = "+weatherdata.getVisibility());
		Log.i("XMLUtils","CODE = "+weatherdata.getCode());
		Log.i("XMLUtils","TODAY MIN = "+weatherdata.getTodaymin());
		Log.i("XMLUtils","TODAY MAX = "+weatherdata.getTodaymax());
	}
}
