package com.weather.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.util.Log;

public class CustomHttpClient {

	/** The time it takes for our client to timeout */
	public static final int HTTP_TIMEOUT = 10000; //10 seconds


	/**
	 * Performs an HTTP Post request to the specified url with the
	 * specified parameters.
	 *
	 * @param url The web address to post the request to
	 * @param postParameters The parameters to send via the request
	 * @return The result of the request
	 * @throws Exception
	 */
	public static String executeHttpPost(String url) throws Exception {
		
		DownloadWebpage downloader = new DownloadWebpage();
		Log.i("CustomHttpClient","Init");
		downloader.execute(new URL(url));
		
		// Poll for result to be obtained
		while(!downloader.resultobtained);	
		String res = downloader.result;
		
		if(res == null)
			throw new Exception();
		
		Log.i("CustomHttpClient","Result obtained");
		Log.i("CustomHttpClient",res);
		return res;
	}

	private static class DownloadWebpage extends AsyncTask<URL, Integer, Long> {

		public String result=null;
		public boolean resultobtained=false;
		
		@Override
		protected Long doInBackground(URL... urls) {
			
			BufferedReader in = null;
			try	{
				URL url = urls[0]; 
				Log.i("CustomHttpClient","URL = "+url.toString());
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(HTTP_TIMEOUT);
				conn.setReadTimeout(HTTP_TIMEOUT);

				// Get the response
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();

				result = sb.toString().trim();
			} catch (IOException e) {
				Log.e("CustomHttpClient", "IOException");
				result=null;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Log.e("CustomHttpClient", e.getMessage());
					}
				}
			}
			resultobtained = true;
			return (long)0;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		@Override
		protected void onPostExecute(Long result) {
		}
	}
}
