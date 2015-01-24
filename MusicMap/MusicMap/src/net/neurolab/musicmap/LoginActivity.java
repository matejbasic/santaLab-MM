package net.neurolab.musicmap;

import java.util.HashMap;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.PreferredGenre;
import net.neurolab.musicmap.db.PreferredLocation;
import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.fragments.FragmentFacebookLogin;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

/**
 * 
 * @author Basic
 * 
 * First activity displayed to user. Checks connectivity and user data.
 * If user is not signed via Facebook or as an guest, options for login are showed.
 * Else, activity navigates to setPreferences Activity or Main Activity.
 *
 */
public class LoginActivity extends FragmentActivity implements LoginView {

	private FragmentFacebookLogin fFacebookLogin;
	private boolean userExist;
	private boolean isInstanceSaved;
	private static boolean connectionValid = false;
	private ProgressBar progressBar;
	private Button btnCheckConnection;
	private Button btnFbAuth;
	private Button btnGuest;
	private String fbId = null;
	private String idHash = null;
	private String uniqueId = null;
	private String userName = null;
	private class DependenciesTask extends AsyncTask<Context, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Context... params) {
			Context context = params[0];
			Boolean isConnected = false;
			try {
				ConnectivityManager cm = (ConnectivityManager)context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				
				NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
				
				//check connection status
				isConnected = activeNetwork != null && activeNetwork.isConnected();
				//TODO: add connection type (WiFi) to preferences
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			return isConnected;
		}

		@Override
		protected void onPostExecute(Boolean isConnected) {			
			if (isConnected) {
				checkUser();
			}
			else {
				int eventsSum = new Event().getSum();
				int usersSum = new User().getSum();
				
				if (eventsSum == 0 || usersSum == 0) {
					setNoConnectionError();
				}
				else {
					//even if not online, user can see previously downloaded events
					navigateToHome();
				}
			}
		}
	
	}
	private MMAsyncResultHandler getFbUserKeyHandler = new MMAsyncResultHandler() {
		
		@Override
		public void handleResult(String result, Boolean status) {
			
			JSONObject results = null;
			try {
				results = new JSONObject(result);
			} 
			catch (JSONException e) {
				setMMWebServiceError();
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
						navigateToPreferences();
						
					} catch (JSONException e) {
						setMMWebServiceError();
						e.printStackTrace();
					}
					
				}
				else {
					setMMWebServiceError();
				}
			}
			else {
				setMMWebServiceError();
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
				setFacebookLoginError();
				e.printStackTrace();
			}
			if (results != null) {
				try {
					if (results.has("error")) {
						if ( results.getString("error").contains("facebookId exists")) {
							//Fb user exists: get existing key from mm web service
							MMAsyncTask mmTask = new MMAsyncTask();
							Object params[] = new Object[]{"fbUser", "getKey", null, getFbUserKeyHandler, fbId, idHash};
							mmTask.execute(params);
							
						}
						else {
							setMMWebServiceError();
						}
					}
					else if(results.has("ApiKey")) {
						addUserToDB(results.getString("ApiKey"), true);
						navigateToPreferences();
					}
					else {
						setMMWebServiceError();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else {
				setMMWebServiceError();
			}
			
		}
	};
	private MMAsyncResultHandler addUserHandler = new MMAsyncResultHandler() {
		
		@Override
		public void handleResult(String result, Boolean status) {
			JSONObject results = null;
			
			try {
				results = new JSONObject(result);
			} 
			catch (JSONException e) {
				setFacebookLoginError();
				e.printStackTrace();
			}
			if (results != null) {
				try {
					if (results.has("error")) {
						
						MMAsyncTask mmTask = new MMAsyncTask();
						if (idHash == null) {
							idHash = String.valueOf(userName.hashCode());
						}
						Object params[] = new Object[]{"user", "getKey", null, getUserKeyHandler, userName, idHash};
						mmTask.execute(params);
					}
					else if(results.has("ApiKey")) {
						addUserToDB(results.getString("ApiKey"), false);
						navigateToPreferences();
					}
					else {
						setMMWebServiceError();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else {
				setMMWebServiceError();
			}
			
		}
	};
	private MMAsyncResultHandler getUserKeyHandler = new MMAsyncResultHandler() {
		
		@Override
		public void handleResult(String result, Boolean status) {
			JSONObject results = null;
			try {
				results = new JSONObject(result);
			} 
			catch (JSONException e) {
				setMMWebServiceError();
				e.printStackTrace();
			}
			if (results != null) {
				if (results.has("ApiKey")) {
					try {
						addApiKeyToUser(results.getString("ApiKey"), false);
						navigateToPreferences();
						
					} catch (JSONException e) {
						setMMWebServiceError();
						e.printStackTrace();
					}
					
				}
				else {
					newUser(uniqueId);
				}
			}
			else {
				setMMWebServiceError();
			}
			
		}
	};
	
	
	private void addUserToDB(String apiKey, Boolean fromFacebook) {
		//User user = new User(userId, firstLastName, facebookId, mmApiKey, password);
		User user = null;	
		if (fromFacebook) {
			user = new User(0, this.userName, this.fbId, apiKey, this.idHash);
		}
		else {
			user = new User(0, this.userName, "0", apiKey, this.idHash);
			
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
	private void checkUser() {
		List<User> users = new User().getAll();
		if (!users.isEmpty()) { 
			Boolean isUserValid = false;
			for (User user : users) {
				if (user.getMmApiKey() != null) {
					isUserValid = true;
					int prefGenresSum = new PreferredGenre().getSum();
					int prefLocationsSum = new PreferredLocation().getSum();
					if (prefGenresSum == 0 || prefLocationsSum == 0) {
						navigateToPreferences();
					}
					else {
						navigateToHome();
					}
				}	
			}
			if (!isUserValid) {
				setLoginButtons();
			}
		}
		else {
			setLoginButtons();
		}
	}
	
	
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
						navigateToPreferences();
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
		this.idHash = String.valueOf(uniqueId.hashCode());
		this.userName = uniqueId;
		
		MMAsyncTask mmTask = new MMAsyncTask();
		Object params[] = new Object[]{"user", "add", null, addUserHandler, this.userName, this.idHash};
		mmTask.execute(params);
	}
	
	public void checkDependencies(Context context) {
		DependenciesTask task = new DependenciesTask();
		task.execute(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// active android initialization, must be in launch activity
		ActiveAndroid.initialize(this);
		isInstanceSaved = false;
		this.userExist = false;
		
		Bundle extras = getIntent().getExtras();		
		if (extras != null) {
			String reason = extras.getString("reason");
			if ( reason == "no-key") {
				this.userExist = true;
			}		
		}
		
		if (savedInstanceState != null) {
			isInstanceSaved = true;
		}
		
		progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
		hideProgress();
		
		btnFbAuth = (Button) findViewById(R.id.authButton);	
		btnGuest = (Button) findViewById(R.id.btnLoginAsGuest);		
		btnCheckConnection = (Button) findViewById(R.id.btnCheckConnection);
		
		if (connectionValid) {
			btnGuest.setVisibility(View.VISIBLE);
			btnFbAuth.setVisibility(View.VISIBLE);
			btnCheckConnection.setVisibility(View.INVISIBLE);
			setLoginButtons();
		}
		else {
			btnGuest.setVisibility(View.INVISIBLE);
			btnFbAuth.setVisibility(View.INVISIBLE);
			btnCheckConnection.setVisibility(View.VISIBLE);
		}
		btnCheckConnection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProgress();
				btnCheckConnection.setVisibility(View.INVISIBLE);
				if (!connectionValid) {
					checkDependencies(getApplicationContext());
				}
			}
		});
		
		if (!connectionValid) {
			checkDependencies(getApplicationContext());
		}	
	}
	
	
	@Override
	public void setLoginButtons() {
		if (!connectionValid) {
			connectionValid = true;
		}
	
		hideProgress();
		btnCheckConnection.setVisibility(View.INVISIBLE);
			
		btnFbAuth.setVisibility(View.VISIBLE);
		btnGuest.setVisibility(View.VISIBLE);
			
		if (!isInstanceSaved) {
			fFacebookLogin = new FragmentFacebookLogin();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, fFacebookLogin).commit();
		} 
		else {
			fFacebookLogin = (FragmentFacebookLogin) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}		
	}
	/*
	 * (non-Javadoc)
	 * @see net.neurolab.musicmap.interfaces.LoginView#getFbFragmentData(java.util.HashMap)
	 */
	@Override
	public void getFbFragmentData(HashMap<String, String> data) {
		if (data.containsKey("id") && data.containsKey("name")) {	
			checkFbUser(data.get("name")
					.toString(), data.get("id").toString(), LoginActivity.this);	
		}
	}

	@Override
	public void showProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void setFacebookLoginError() {
		newToast(R.string.facebook_login_error, Toast.LENGTH_LONG);
	}

	@Override
	public void setMMWebServiceError() {
		newToast(R.string.mm_service_login_error, Toast.LENGTH_LONG);
	}
	@Override
	public void setUnknownError() {
		newToast(R.string.unknown_error, Toast.LENGTH_LONG);
	}
	@Override
	public void setNoConnectionError() {
		hideProgress();
		btnCheckConnection.setVisibility(View.VISIBLE);
        newToast(R.string.no_connection_error, Toast.LENGTH_SHORT);
	}

	private void newToast(int txtId, int duration) {
		LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
        ((TextView) layout.findViewById(R.id.toast_text)).setText(txtId);

        Toast toast = new Toast(getBaseContext());
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
	}
	@Override
	public void navigateToPreferences() {
		Intent intent = new Intent(LoginActivity.this,
				SetPreferencesActivity.class);
		startActivity(intent); 
		finish();
	}

	@Override
	public void navigateToHome() {
		Intent intent = new Intent(LoginActivity.this,
				MainActivity.class);
		startActivity(intent); 
		finish();
	}

	@Override
	public void checkGuest() {
		//use it as an username for guest user
		uniqueId = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
				
		List<User> users = new User().getAll();
		if (!users.isEmpty()) { //but no mm Api Key!
			Boolean noMatch = true;
			
			for (User user : users) {
				if (uniqueId.matches(user.getFirstLastName())) {
					noMatch = false;
					
					if ( user.getMmApiKey() == null) {
						getUserKey(uniqueId);
					}
							
					navigateToPreferences();
							
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
	
}
