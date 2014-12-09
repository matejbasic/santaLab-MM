package net.neurolab.musicmap.presenters;

import java.util.List;

import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.interfaces.LoginPresenter;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Log;

public class Login implements LoginPresenter {
	private LoginView loginView;
	private String fbId = null;
	private String idHash = null;
	private String userName = null;
	private String uniqueId = null;
	
	public Login(LoginView loginView) {
		this.loginView = loginView;
		
	}
	
	private void addUserToDB(String apiKey, Boolean fromFacebook) {
		//User user = new User(userId, firstLastName, facebookId, mmApiKey, password);
		User user = null;
		
		if (fromFacebook) {
			user = new User(0, this.userName, this.fbId, apiKey, this.idHash);
		}
		else {
			user = new User(0, this.userName, null, apiKey, this.idHash);
			
		}
		user.save();
	}
	private void addApiKeyToUser(String apiKey, Boolean fromFacebook) {
		User user = new User();
		if (fromFacebook) {
			user.setApiKeyToUser(apiKey, true, this.fbId);
		}
		else {
			user.setApiKeyToUser(apiKey, false, this.userName);
		}
	}
			
	private void getFbUserKey(String fbId) {
		this.fbId = fbId;
		this.idHash = String.valueOf(fbId.hashCode());
		
		//get the key and proceed to preferences
		MMAsyncTask mmTask = new MMAsyncTask();
		Object params[] = new Object[]{"fbUser", "getKey", null, getFbUserKeyHandler, this.fbId, this.idHash};
		mmTask.execute(params);
	}
	private void getUserKey(String userName) {
		this.idHash = String.valueOf(userName.hashCode());
		this.userName = userName;
		
		MMAsyncTask mmTask = new MMAsyncTask();
		Object params[] = new Object[]{"user", "getKey", null, getUserKeyHandler, this.userName, this.idHash};
		mmTask.execute(params);
	}
	
	private void newFbUser(String fbId, String userName) {
		this.idHash = String.valueOf(fbId.hashCode());
		this.fbId = fbId;
		this.userName = userName;
		
		MMAsyncTask mmTask = new MMAsyncTask();
		Object params[] = new Object[]{"fbUser", "add", null, addFbUserHandler, this.fbId, this.idHash, this.userName};
		mmTask.execute(params);
	}
	private void newUser(String uniqueId) {
		this.idHash = String.valueOf(userName);
		this.userName = uniqueId;
		//Log.i("newUser", "start");
		MMAsyncTask mmTask = new MMAsyncTask();
		Object params[] = new Object[]{"user", "add", null, addUserHandler, this.userName, this.idHash};
		mmTask.execute(params);
	}
	
	@Override
	public void checkFbUser(String userName, String fbId, Activity activity) {
		
		List<User> users = new User().getAll();
		if (!users.isEmpty()) { //if user exist
			Boolean noMatch = true;
			for (User user : users) {
				
				if ( user.getFacebookId() != null && fbId.matches(user.getFacebookId())) {
					noMatch = false;
					if ( user.getMmApiKey() == null) { //local DB contains user data but not the user key
						getFbUserKey(fbId);
					}
					else {//user data valid, proceed
						loginView.navigateToPreferences();
					}
				}
			}
			if (noMatch) {
				newFbUser(fbId, userName);
			}
		}
		else { //user doesn't exist, add new one to MM WS and local database
			newFbUser(fbId, userName);
		}
		
	}
	
	private MMAsyncResultHandler getFbUserKeyHandler = new MMAsyncResultHandler() {
		
		@Override
		public void handleResult(String result, Boolean status) {
			
			//Log.i("handler user key", String.valueOf(status));
			JSONObject results = null;
			try {
				results = new JSONObject(result);
			} 
			catch (JSONException e) {
				loginView.setMMWebServiceError();
				e.printStackTrace();
			}
			if (results != null) {
				if (results.has("ApiKey")) {
					try {
						String apiKey = results.getString("ApiKey");
						
						if (userName != null) {
							addUserToDB(apiKey, true);
						}
						else {
							addApiKeyToUser(apiKey, true);
						}
						loginView.navigateToPreferences();
						
					} catch (JSONException e) {
						loginView.setMMWebServiceError();
						e.printStackTrace();
					}
					
				}
				else {
					loginView.setMMWebServiceError();
				}
			}
			else {
				loginView.setMMWebServiceError();
			}
			
		}
	};
	
	private MMAsyncResultHandler addFbUserHandler = new MMAsyncResultHandler() {
		
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
				try {
					if (results.has("error")) {
						if ( results.getString("error").contains("facebookId exists")) {
							
							//Log.i("facebookId", "exists");
							
							//Fb user exists: get existing key from mm web service
							MMAsyncTask mmTask = new MMAsyncTask();
							Object params[] = new Object[]{"fbUser", "getKey", null, getFbUserKeyHandler, fbId, idHash};
							mmTask.execute(params);
							
						}
						else {
							loginView.setMMWebServiceError();
						}
					}
					else if(results.has("ApiKey")) {
						addUserToDB(results.getString("ApiKey"), true);
						loginView.navigateToPreferences();
					}
					else {
						loginView.setMMWebServiceError();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else {
				loginView.setMMWebServiceError();
			}
			
		}
	};

	
	@Override
	public void checkGuest(Boolean userExist, Context context) {
		
		//use it as an username for guest user
		uniqueId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		
		List<User> users = new User().getAll();
		if (!users.isEmpty()) { //but no mm Api Key!
			Boolean noMatch = true;
			//Log.i("users", "exists");
			for (User user : users) {
				if (uniqueId.matches(user.getFirstLastName())) {
					//Log.i("users", "match");
					noMatch = false;
					
					if ( user.getMmApiKey() == null) {
						getUserKey(uniqueId);
					}
					else {
						loginView.navigateToPreferences();
					}
				}
			}
			if (noMatch) {
				newUser(uniqueId);
			}
		}
		else {
			newUser(uniqueId);
		}
	}

	private MMAsyncResultHandler addUserHandler = new MMAsyncResultHandler() {
		
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
				try {
					if (results.has("error")) {
						
						MMAsyncTask mmTask = new MMAsyncTask();
						Object params[] = new Object[]{"user", "getKey", null, getUserKeyHandler, userName, idHash};
						mmTask.execute(params);
					}
					else if(results.has("ApiKey")) {
						Log.i("apiKey", results.getString("ApiKey"));
						addUserToDB(results.getString("ApiKey"), false);
						loginView.navigateToPreferences();
					}
					else {
						loginView.setMMWebServiceError();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else {
				loginView.setMMWebServiceError();
			}
			
		}
	};
	
	private MMAsyncResultHandler getUserKeyHandler = new MMAsyncResultHandler() {
		
		@Override
		public void handleResult(String result, Boolean status) {
			Log.i("user key handler", result);
			//Log.i("handler user key", String.valueOf(status));
			JSONObject results = null;
			try {
				results = new JSONObject(result);
			} 
			catch (JSONException e) {
				loginView.setMMWebServiceError();
				e.printStackTrace();
			}
			if (results != null) {
				if (results.has("ApiKey")) {
					Log.i("apiKey", "we have it");
					try {
						addApiKeyToUser(results.getString("ApiKey"), false);
						loginView.navigateToPreferences();
						
					} catch (JSONException e) {
						loginView.setMMWebServiceError();
						e.printStackTrace();
					}
					
				}
				else {
					newUser(uniqueId);
					//loginView.setMMWebServiceError();
				}
			}
			else {
				loginView.setMMWebServiceError();
			}
			
		}
	};

}
