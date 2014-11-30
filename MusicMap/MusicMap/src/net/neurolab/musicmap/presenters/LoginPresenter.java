package net.neurolab.musicmap.presenters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.interfaces.LoginPresenterIntf;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;
import net.neurolab.musicmap.ws.MMService;
import net.neurolab.musicmap.ws.MMService.LocalBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class LoginPresenter implements LoginPresenterIntf {
	private LoginView loginView;
	
	MMService mmService = new MMService();
	
	public LoginPresenter(LoginView loginView) {
		this.loginView = loginView;
		
	}

	
	@Override
	public void checkUserData() {}
	
	@Override
	public void saveFbUserToWS(String id, final Context context) {
		
		id += "d3ag"; //DEBUG purpose: delete when done!
		//"{\"ApiKey\":\"zGqwh5PQZxSKlezE6KdT\"}"

		String idHash = String.valueOf(id.hashCode());

		final Intent intent = new Intent(context, MMService.class);
		intent.putExtra("action", "addFbUser");
		intent.putExtra("id", id);
		intent.putExtra("idHash", idHash);
		
		String res = mmService.addFbUser(id, idHash);
		Log.i("mmService - rez", res);
		
		final ServiceConnection serviceConnection = new ServiceConnection() {

	        @Override
	        public void onServiceConnected(ComponentName className, IBinder service) {
	            // We've bound to LocalService, cast the IBinder and get LocalService instance
	        	LocalBinder binder = (LocalBinder) service;
	        	mmService = binder.getService();
	            //mService = binder.getService();
	            //mBound = true;
	        }

	        @Override
	        public void onServiceDisconnected(ComponentName arg0) {
	            //mBound = false;
	        }

	    };

		
		new Thread(new Runnable() {
			public void run() {
				try {
					//context.startService(intent);
					context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	
	}

	@Override
	public String checkFbUser(String userName, String fbId, Activity activity) {
		User users = new User();
		List<User> usersList = users.getAll();
		for (User userData : usersList) {
			
			if ( fbId.matches(userData.getFacebookId())) {
				
				if ( userData.getMmApiKey() == null) {
					Log.i("presenter","active");
					String id = fbId;
					String idHash = String.valueOf(id.hashCode());
					MMAsyncTask mmTask = new MMAsyncTask();
					Object params[] = new Object[]{"fbUser", "add", null, checkFbUserHandler, id, idHash};
					mmTask.execute(params);
					
				}
				else {
					return "valid";
				}
			}
		}
		return "";
	}
	
	private MMAsyncResultHandler checkFbUserHandler = new MMAsyncResultHandler() {
		
		@Override
		public void handleResult(String result, Boolean status) {
			JSONObject results = null;
			
			
			
			try {
				results = new JSONObject(result);
			} 
			catch (JSONException e) {
				loginView.setFacebookLoginError();
				e.printStackTrace();
			}
			if (results != null) {
				Log.i("resultH", results.toString());
			}
			
		}
	};
}
