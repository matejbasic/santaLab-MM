package net.neurolab.musicmap.presenters;

import java.util.List;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.PreferredGenre;
import net.neurolab.musicmap.db.PreferredLocation;
import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.interfaces.SplashPresenter;
import net.neurolab.musicmap.interfaces.SplashView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author MB
 *
 */
public class Splash implements SplashPresenter {
	private SplashView splashView;
	
	private Boolean isConnected = false;

	
	public Splash(SplashView splashView) {
		this.splashView = splashView;
	}

	public void resolveCheck() {
		
		this.splashView.setNoConnectionError();
	}
	
	private class DependenciesTask extends AsyncTask<Context, Void, Void> {

		@Override
		protected Void doInBackground(Context... params) {
			Context context = params[0];
			try {
				ConnectivityManager cm = (ConnectivityManager)context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				
				NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
				
				//check connection status & type
				isConnected = activeNetwork != null && activeNetwork.isConnected();

				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			if (isConnected) {
				checkUser();
			}
			else {
				
				int eventsSum = new Event().getSum();
				int usersSum = new User().getSum();
				
				if (eventsSum == 0 || usersSum == 0) {
					splashView.setNoConnectionError();
				}
				else {
					splashView.navigateToHome();
				}
			}

		
		}
	
	
	
	}
	private void checkUser() {
		List<User> users = new User().getAll();
		Log.i("checkuser", "start");
		if (!users.isEmpty()) { 
			Boolean isUserValid = false;
			for (User user : users) {
				if ( user.getMmApiKey() != null) { //local DB contains user data but not the user key
					isUserValid = true;
					int prefGenresSum = new PreferredGenre().getSum();
					int prefLocationsSum = new PreferredLocation().getSum();
					if (prefGenresSum == 0 || prefLocationsSum == 0) {
						splashView.navigateToPreferences();
					}
					else {
						splashView.navigateToHome();
					}
				}	
			}
			if (!isUserValid) {
				splashView.navigateToLogin("no-key");
			}
		}
		else {
			splashView.navigateToLogin("no-user");
		}
	}
	@Override
	public void checkDependencies(Context context) {
		
		DependenciesTask task = new DependenciesTask();
		Context params = context;
		task.execute(params);
	}

	
}
