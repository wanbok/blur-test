package com.mintech.blur;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class CommonUtil {

	public static void moveView(View v, float scale) {
		LayoutParams params = (LayoutParams)v.getLayoutParams();
		v.setLayoutParams(params);
	}

	public static void scaleTwiceHeight(View v) {
		LayoutParams params = (LayoutParams)v.getLayoutParams();
		params.height = params.height * 2;
		v.setLayoutParams(params);
	}

	public static void stretchImageViewByScale(ImageView iv, float w, float h) {
		FrameLayout.LayoutParams pastParams = (FrameLayout.LayoutParams)iv.getLayoutParams();
		iv.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		iv.getLayoutParams().width = (int)(pastParams.width * w);
		iv.getLayoutParams().height = (int)(pastParams.height * h);
	}

	public static ArrayList<JSONObject> sortedListFromJSONArray(JSONArray jsonArray, final String key) {
		// Change JSONArray to ArrayList. Because JSONArray can't be sorted. 
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();      
		if (jsonArray != null) { 
			int len = jsonArray.length();
			try {
				for (int i=0;i<len;i++) list.add((JSONObject) jsonArray.get(i));
			} catch (JSONException e) {
				// TODO: handle exception
			}
		} 

		// Sort JSON Array by id
		Collections.sort( list, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject a, JSONObject b) {
				String valA = new String();
				String valB = new String();

				try {
					valA = a.getString(key);
					valB = b.getString(key);
				} 
				catch (JSONException e) {
					Log.e("MainActivity", "JSONException in combineJSONArrays sort section", e);
				}

				int comp = valA.compareTo(valB);

				if(comp > 0)
					return 1;
				if(comp < 0)
					return -1;
				return 0;
			}
		});

		return list;
	}
	
	public static InputStream fetch(String address) throws MalformedURLException, IOException {
	    HttpGet httpRequest = new HttpGet(URI.create(address) );
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
	    HttpEntity entity = response.getEntity();
	    BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
	    InputStream instream = bufHttpEntity.getContent();
	    return instream;
	}
}
