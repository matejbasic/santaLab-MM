package net.neurolab.musicmap.presenters;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import net.neurolab.musicmap.SetPreferencesActivity;
import net.neurolab.musicmap.db.Genre;
import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.db.PreferredGenre;
import net.neurolab.musicmap.db.PreferredLocation;
import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.interfaces.SetPreferencesPresenter;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

public class SetPreferences implements SetPreferencesPresenter {

	private SetPreferencesActivity view;
	private User user;
	private Boolean locationCheck = false;
	
	private class getGenresHandler implements MMAsyncResultHandler {

		@Override
		public void handleResult(final String result, Boolean status) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						JSONArray mmGenres = new JSONArray(result);
						
						for (int i = 0; i < mmGenres.length(); i++) {
							JSONObject mmGenre = mmGenres.getJSONObject(i);
							Genre genre = new Genre(mmGenre.getString("Name"), null);
							genre.save();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();		
		}
		
	}
	
	private class getAddressTask extends AsyncTask<Object, Void, Object[]> {
		
		@Override
		protected Object[] doInBackground(Object... params) {
			String locationName = (String) params[0];
			Context context = (Context) params[1];
			
			//0 - name, 1 - city, 2 - address, 3 - lat, 4 - lng
			Double defLatLng = -1.00;
			Object result[] = new Object[] { "", "", "", defLatLng, defLatLng };

			
			try {
				Geocoder gc = new Geocoder(context, Locale.getDefault());
				List<Address> addresses = gc.getFromLocationName(locationName, 1);
				//Log.i("addresses", addresses.toString());
				if (!addresses.isEmpty()) {
					Address address = addresses.get(0);
				
					//Log.i("address", address.toString());
					
					result[0] = address.getFeatureName();
					result[1] = address.getLocality();
					
					StringBuilder tempAddress = new StringBuilder();
					int iMax = address.getMaxAddressLineIndex();
					for (int i = 0; i <= iMax; i++) {
						tempAddress.append(address.getAddressLine(i));
						if ( i < iMax) {
							tempAddress.append(", ");
						}
					}
					
					result[2] = tempAddress.toString();
					if ( address.hasLatitude() && address.hasLongitude()) {
						result[3] = address.getLatitude();
						result[4] = address.getLongitude();
					}
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
	
		@Override
		protected void onPostExecute(Object[] result) {
			getAddressHandler(result);
		}
	
	}
	
	private void getAddressHandler(final Object[] result) {

		//0 - name, 1 - city, 2 - address, 3 - lat, 4 - lng
		if (result[0] != null && result[1] != null && result[2] != null && (Double)result[3] != -1 && (Double)result[4] != -1) {
			view.navigateToSetGenres();
				
			//maybe not the best option(?) for bg work
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Location newLocation = new Location(result[0].toString(), result[1].toString(), 
							result[2].toString(), (Double)result[3], (Double)result[4]);
					newLocation.save();
						
					PreferredLocation newPrefLocation = new PreferredLocation(user, newLocation);
					newPrefLocation.save();
				}
			}).start();
		}
		else {
			view.setNoLocationError();
		}

		locationCheck = false;				
	}
	
	public SetPreferences(SetPreferencesActivity view) {
		super();
		this.view = view;
	}
	
	@Override
	public Boolean checkUser() {
		List<User> users = new User().getAll();
		if (users.isEmpty()) {
			return false;
		}
		else {
			user = users.get(0);
			if (user.getMmApiKey() == null) {
				return false;
			}
			else {
				return true;
			}
		}
	}
	
	@Override
	public void checkGenres() {
		int sumGenres = new Genre().getSum();
		if (sumGenres == 0) {
			MMAsyncTask asyncTaskEvents = new MMAsyncTask();
			Object paramsEvent[] = new Object[] { "genres", "get", null, new getGenresHandler(), user.getMmApiKey()};
			asyncTaskEvents.execute(paramsEvent);
		}
	}

	@Override
	public void setLocation(String name, Context context) {
		if (!locationCheck) {
			locationCheck = true;
			
			if(name != null && !name.isEmpty()) {
				Location location = new Location().getLocation(name);
				//Log.i("location", String.valueOf(location));
				
				if(location == null) {
					//preferred location can be just a city
					//get lat, lng from google geocoding
					getAddressTask mmTask = new getAddressTask();
					mmTask.execute(new Object[]{name, context});
					
				}
				else {
					//check if location is already preferred
					if (!new PreferredLocation().checkPreferredLocation(location)) {
						PreferredLocation newPrefLocation = new PreferredLocation(user, location);
						newPrefLocation.save();
					}
					
					view.navigateToSetGenres();
					locationCheck = false;
					
				}
			}
			else {
				view.setNoLocationError();
				locationCheck = false;
			}
		}
	}

	@Override
	public void setPreferedGenres(List<Genre> genres) {
		PreferredGenre prefGenre;
		for (Genre genre : genres) {
			prefGenre = new PreferredGenre(user, genre);
			prefGenre.save();
		}
		view.navigateToHome();
	}
}
