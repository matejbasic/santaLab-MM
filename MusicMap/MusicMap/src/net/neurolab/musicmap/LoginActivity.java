package net.neurolab.musicmap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.fragments.FacebookLoginFragment;
import net.neurolab.musicmap.interfaces.LoginPresenterIntf;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.presenters.LoginPresenter;
import net.neurolab.musicmap.ws.JSONAdapter;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

public class LoginActivity extends FragmentActivity implements LoginView {

	private FacebookLoginFragment facebookLoginFragment;
	private LoginPresenterIntf presenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//active android initialization, must be in launch activity
		ActiveAndroid.initialize(this);
		
		//*********************************************************************************************
		
		MMAsyncTask asyncTaskStores = new MMAsyncTask();
		Object params[] = new Object[]{"getEvents", "add", null, handleResult, null, null};
		asyncTaskStores.execute(params);
			
		
			
		//*********************************************************************************************
		
		//ActionBar actionBar = getActionBar();
		//actionBar.hide();
		
		this.presenter = new LoginPresenter(this);
		
		if (savedInstanceState == null) {
			facebookLoginFragment = new FacebookLoginFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(android.R.id.content, facebookLoginFragment)
			.commit();
			
		}
		else {
			facebookLoginFragment = (FacebookLoginFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}
	
	//*****************************************
private MMAsyncResultHandler handleResult = new MMAsyncResultHandler() {
		
		@Override
		public void handleResult(String result, Boolean status) {
			JSONObject results = null;
			Log.i("getEventsHandler", result);
			
			
			ArrayList<String> nesto=null;				
			
			try {
				nesto=JSONAdapter.getTheArtists(result);
					System.out.println(nesto.size());
				//System.out.println(events.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("haveSomeProblems");
				e.printStackTrace();
			}

			 for(int i=0; i<nesto.size(); i++){			        
			        System.out.println(nesto.get(i).toString());					        	        
			 }
			
			
			/*
			ArrayList<Location> locations=null;				
			
			try {
				locations=JSONAdapter.getLocations(result);
					System.out.println(locations.size());
				//System.out.println(events.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("haveSomeProblems");
				e.printStackTrace();
			}

			 for(int i=0; i<locations.size(); i++){			        
			        System.out.println(locations.get(i).getName());		
			        System.out.println(locations.get(i).getAddress());			        
			 }
			*/
			/*
			ArrayList<Event> events=null;				
			
			try {
				events=JSONAdapter.getEvents(result);
					System.out.println(events.size());
				//System.out.println(events.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("haveSomeProblems");
				e.printStackTrace();
			}

			 for(int i=0; i<events.size(); i++){			        
			        System.out.println(events.get(i).getEventId());		
			        System.out.println(events.get(i).getEventTime());			        
			 }*/
			
			//String jsonStr = "[{\"No\":\"1\",\"Name\":\"ABC\"},{\"No\":\"2\",\"Name\":\"PQR\"},{\"No\":\"3\",\"Name\":\"XYZ\"}]";
			//JSONAdapter js  = new JSONAdapter();
			/*
			try {
				//results = new JSONObject(result);
				   JSONArray array = new JSONArray(result); 

				    for(int i=0; i<array.length(); i++){
				        JSONObject jsonObj  = array.getJSONObject(i);
				        System.out.println(jsonObj.getString("EventId"));
				        System.out.println(jsonObj.getString("name"));
				       
				        try {
							System.out.println(js.ConvertToTimestamp(jsonObj.getString("lastEdited")));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
			} 
			catch (JSONException e) {				
				e.printStackTrace();
			}			*/
			//"lastEdited\":\"2014-11-26T19:25:55.507\"
			
			//js.ConvertToTimestamp("lastEdited\":\"2014-11-26T19:25:55.507\");
		
				
		
			
						 
			//if(results!=null) Toast.makeText(getApplicationContext(), "not null", Toast.LENGTH_LONG).show();	
			//else Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG).show();	
			//Toast.makeText(getApplicationContext(), tmp[0].toString(), Toast.LENGTH_LONG).show();	
			//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();	
		}
		
	};
	//************************************************
	public class TempAsyncTask extends AsyncTask<Object, Void, Object[]> {

		@Override
		protected Object[] doInBackground(Object... arg0) {
			Log.i("temp task", "done");
			return null;
		}
		
	}
	@Override
	public void getFbFragmentData(HashMap<String, String> data) {
		if ( data.containsKey("id") && data.containsKey("name")) {
			
			String response = this.presenter.checkFbUser(
					data.get("name").toString(), data.get("id").toString(), LoginActivity.this);
			Log.i("loginActivity", response);
			if (response.matches("valid")) {
				this.navigateToSetPrefLocation();
			}
		}
	}

	
	@Override
	public void showProgress() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideProgress() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFacebookLoginError() {
		Toast.makeText(getApplicationContext(), R.string.facebook_login_error, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void setMMWebServiceError() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void navigateToSetPrefLocation() {
		Intent intent = new Intent(LoginActivity.this, SetPrefLocationActivity.class);
		startActivity(intent);
	}

	@Override
	public void navigateToHome() {
		
	}
	
}
