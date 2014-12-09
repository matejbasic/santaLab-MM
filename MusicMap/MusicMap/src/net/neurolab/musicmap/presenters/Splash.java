package net.neurolab.musicmap.presenters;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.Genre;
import net.neurolab.musicmap.db.PreferredGenre;
import net.neurolab.musicmap.db.PreferredLocation;
import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.interfaces.SplashPresenter;
import net.neurolab.musicmap.interfaces.SplashView;
import net.neurolab.musicmap.ws.MMAsyncTask;
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
				
				//check needed data - preferences
				checkPreferences();
				//check user
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
	
	private void checkPreferences() {
		
		Log.i("checkPref", "start");
		//debug
		new Genre().deleteAll();
		
		int sumGenres = new Genre().getSum();
	
		if (sumGenres == 0) {
			
			//ALERT - change to MM web service call when list of genres become available 
			
			MMAsyncTask asyncTaskEvents = new MMAsyncTask();
			Object paramsEvent[] = new Object[] { "genres", "get", null, null, null };
			asyncTaskEvents.execute(paramsEvent);
			
			ArrayList<String> genres = new ArrayList<String>();
			genres.add("Blues");
			genres.add("Classical");
			genres.add("Electronic");
			genres.add("Folk, World & Country");
			genres.add("Funk/Soul");
			genres.add("Hip Hop");
			genres.add("Jazz");
			genres.add("Latin");
			genres.add("Pop");
			genres.add("Reggae");
			genres.add("Rock");
			genres.add("Metal");
			Log.i("list genres", String.valueOf(genres));
			for (String genreName : genres) {
				Genre genre = new Genre(genreName, null);
				genre.save();
			}
			
			List<Genre> genresDb = new Genre().getAll();
			for (Genre genre : genresDb) {
				Log.i("genre", String.valueOf(genre));
			}
			
		}
		
	}
	@Override
	public void checkDependencies(Context context) {
		
		DependenciesTask task = new DependenciesTask();
		Context params = context;
		task.execute(params);
	}

	
}
