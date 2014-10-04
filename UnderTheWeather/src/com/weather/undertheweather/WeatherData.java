package com.weather.undertheweather;

public class WeatherData {
	private String location, country, unit, text, currenttemp, date, windspeed, humidity, visibility, todaymax, todaymin;
	private int code;
	private int nextcode[];
	private String nextday[], nextdate[], nextmax[], nextmin[], nexttext[];
	
	public WeatherData() {
		nextcode 	= new int[4];
		nextday		= new String[4];
		nextdate	= new String[4];
		nextmax		= new String[4];
		nextmin 	= new String[4];
		nexttext	= new String[4];
	}
	
	public int getNextcode(int index) {
		return nextcode[index];
	}
	public void setNextcode(int nextcode, int index) {
		this.nextcode[index] = nextcode;
	}
	public String getNextday(int index) {
		return nextday[index];
	}
	public void setNextday(String nextday, int index) {
		this.nextday[index] = nextday;
	}
	public String getNextdate(int index) {
		return nextdate[index];
	}
	public void setNextdate(String nextdate, int index) {
		this.nextdate[index] = nextdate;
	}
	public String getNexttext(int index) {
		return nexttext[index];
	}
	public void setNexttext(String nexttext, int index) {
		this.nexttext[index] = nexttext;
	}
	public String getNextmax(int index) {
		return nextmax[index];
	}
	public void setNextmax(String nextmax, int index) {
		this.nextmax[index] = nextmax;
	}
	public String getNextmin(int index) {
		return nextmin[index];
	}
	public void setNextmin(String nextmin, int index) {
		this.nextmin[index] = nextmin;
	}
	
	public String getTodaymax() {
		return todaymax;
	}
	public void setTodaymax(String todaymax) {
		this.todaymax = todaymax;
	}
	public String getTodaymin() {
		return todaymin;
	}
	public void setTodaymin(String todaymin) {
		this.todaymin = todaymin;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getWindspeed() {
		return windspeed;
	}
	public void setWindspeed(String windspeed) {
		this.windspeed = windspeed;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCurrenttemp() {
		return currenttemp;
	}
	public void setCurrenttemp(String currenttemp) {
		this.currenttemp = currenttemp;
	}
}
