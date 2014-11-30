package net.neurolab.musicmap.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

public class MMService extends Service {
	private final IBinder mBinder = new LocalBinder();
	private final String serviceUrl = "http://musicmap.cloudapp.net/api";
	private final String apiKey = "2c9s1rwf7578307";
	
	@Override
	public void onCreate() {
		Log.i("service", "created");
		super.onCreate();
	}
	
	public String addFbUser(String id, String idHash) {
		String result = "";
		String url = "";
		HttpClient httpclient = new DefaultHttpClient();
		
		//registerFacebookUser/{facebookId}/{password}/{appKey}
		//HttpGet httpget = new HttpGet("http://musicmap.cloudapp.net/api/events/0/zagreb/0/0/30112014/05122014/10/0/testbero=%29");
		
		
		
		url = serviceUrl + "/registerFacebookUser/"+ id + "/" + idHash + "/" + apiKey;
		Log.i("addFbUser", url);
		
		HttpGet httpget = new HttpGet(url);
		try {
			ResponseHandler<String> handler = new BasicResponseHandler();
			result = httpclient.execute(httpget, handler);
			
			httpclient.getConnectionManager().shutdown();
			return result;
		} 
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch(Error e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return "error";
	}
	
	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		Log.i("service", "start cmd");
		final String action = intent.getStringExtra("action");
		if (action.contains("addFbUser")) {
			String id = intent.getStringExtra("id");
			String idHash = intent.getStringExtra("idHash");
			String result = addFbUser(id, idHash);
			Log.i("calling results GET", result.toString());
		}
		new Thread(new Runnable() {
			public void run() {
				
			 }
		 }).start();
		
		
		return Service.START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	public class LocalBinder extends Binder {
		public MMService getService() {
			return MMService.this;
		}
	}

}
