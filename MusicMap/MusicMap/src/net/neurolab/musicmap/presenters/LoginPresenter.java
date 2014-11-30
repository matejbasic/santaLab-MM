package net.neurolab.musicmap.presenters;

import java.util.List;

import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.interfaces.LoginPresenterIntf;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class LoginPresenter implements LoginPresenterIntf {
	private LoginView loginView;
	private String fbId = null;
	private String idHash = null;
	private String userName = null;
	
	public LoginPresenter(LoginView loginView) {
		this.loginView = loginView;
		
	}
	@Override
	public void checkUserData() {}
	
	public void addFbUser(String apiKey) {
		//{"ApiKey":"1OLxYJT1ueCMIZ0Nquf7"}
		//User user = new User(userId, firstLastName, facebookId, mmApiKey, password);
		User user = new User(0, this.userName, this.fbId, apiKey, this.idHash);
		user.save();
		Log.i("addFbUser", "user saved");
	}
	
	@Override
	public String checkFbUser(String userName, String fbId, Activity activity) {
		User users = new User();
		
		//users.DeleteAll(); //DEBUG - delete when done
		
		List<User> usersList = users.getAll();
		if (!usersList.isEmpty()) { //if user exist
			
			fbId += "d11m"; //DEBUG
			Log.i("fbId actual", fbId);
			
			for (User userData : usersList) {
				Log.i("userKey", userData.getMmApiKey());
				Log.i("userFbId", userData.getFacebookId());
				if ( fbId.matches(userData.getFacebookId())) {
					if ( userData.getMmApiKey() == null) { //local DB contains user data but not the user key
						
						String idHash = String.valueOf(fbId.hashCode());
						MMAsyncTask mmTask = new MMAsyncTask();
						Object params[] = new Object[]{"fbUser", "add", null, checkFbUserHandler, fbId, idHash};
						mmTask.execute(params);
					}
					else {
						return "valid";
					}
				}
			}
		}
		else { //user doesn't exist, add new one to MM WS and local database
			
			//fbId += "d11m"; //DEBUG
			
			this.idHash = String.valueOf(fbId.hashCode());
			this.fbId = fbId;
			this.userName = userName;
			MMAsyncTask mmTask = new MMAsyncTask();
			Object params[] = new Object[]{"fbUser", "add", null, checkFbUserHandler, this.fbId, this.idHash};
			mmTask.execute(params);
		}
		
		return "a";
	}
	
	private MMAsyncResultHandler checkFbUserHandler = new MMAsyncResultHandler() {
		
		@Override
		public void handleResult(String result, Boolean status) {
			JSONObject results = null;
			Log.i("fbUserHandler", result);
			try {
				results = new JSONObject(result);
			} 
			catch (JSONException e) {
				loginView.setFacebookLoginError();
				e.printStackTrace();
			}
			if (results != null) {
				try {
					if (results.has("error")) {
						String error = results.getString("error");
						if ( error.contains("facebookId exists")) {
							Log.i("facebookId", "exists");
							//delete user from mm WS
							//add user to mm WS
							//get user api key
							//save user api key and proceed
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				try {
					if ( results.has("ApiKey")) {
						String apiKey = results.getString("ApiKey");
						//add user data to local database
						addFbUser(apiKey);
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}
	};

	@Override
	public void saveFbUserToWS(String id, Context context) {
		// TODO Auto-generated method stub
		
	}
}
