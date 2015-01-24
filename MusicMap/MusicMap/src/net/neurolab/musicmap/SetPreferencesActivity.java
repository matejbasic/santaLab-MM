package net.neurolab.musicmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import net.neurolab.musicmap.db.Genre;
import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.db.PreferredGenre;
import net.neurolab.musicmap.db.PreferredLocation;
import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.fragments.FragmentSetPreferredGenres;
import net.neurolab.musicmap.fragments.FragmentSetPreferredLocation;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.activeandroid.query.Select;

/**
 * 
 * @author Basic
 *
 * Activity for setting the preferred location and genres.
 */
public class SetPreferencesActivity extends Activity {
	private FragmentSetPreferredGenres fSetGenres;
	private FragmentSetPreferredLocation fSetLocation;
	private static boolean chooseLocation = true;
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
						List<Genre> genres = new Select().all()
								.from(Genre.class).execute();
						if (genres.size() == 0) {
							for (int i = 0; i < mmGenres.length(); i++) {
								JSONObject mmGenre = mmGenres.getJSONObject(i);
								Genre genre = new Genre(
										mmGenre.getString("Name"), null);
								genre.save();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

	}
	private class getAddressTask extends AsyncTask<Object, Void, Object[]> {
		
		private String gcFallback(String location) {
			String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
			try {
				url += URLEncoder.encode(location, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				url += location;
				e1.printStackTrace();
			}

			String response = "";
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);

			try {
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream inputStream = httpEntity.getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				try {
					response = new JSONTokener(sb.toString()).nextValue()
							.toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				httpClient.getConnectionManager().shutdown();

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Error e) {
				e.printStackTrace();
			}

			return response;
		}

		@Override
		protected Object[] doInBackground(Object... params) {
			String locationName = (String) params[0];
			Context context = (Context) params[1];

			// 0 - name, 1 - city, 2 - address, 3 - lat, 4 - lng
			Double defLatLng = -1.00;
			Object result[] = new Object[] { "", "", "", defLatLng, defLatLng };

			try {
				Geocoder gc = new Geocoder(context, Locale.getDefault());

				if (Geocoder.isPresent()) {

					List<Address> addresses = gc.getFromLocationName(
							locationName, 1);
					if (!addresses.isEmpty()) {
						Address address = addresses.get(0);

						result[0] = address.getFeatureName();
						result[1] = address.getLocality();

						StringBuilder tempAddress = new StringBuilder();
						int iMax = address.getMaxAddressLineIndex();
						for (int i = 0; i <= iMax; i++) {
							tempAddress.append(address.getAddressLine(i));
							if (i < iMax) {
								tempAddress.append(", ");
							}
						}

						result[2] = tempAddress.toString();
						if (address.hasLatitude() && address.hasLongitude()) {
							result[3] = address.getLatitude();
							result[4] = address.getLongitude();
						}

					}
				} else {
					String response = gcFallback(locationName);

					try {
						JSONObject rez = new JSONObject(response);
						JSONArray data = rez.getJSONArray("results");

						result[0] = result[1] = locationName;
						result[2] = data.getJSONObject(0)
								.getString("formatted_address").toString();

						JSONObject temp = new JSONObject(data.getString(0));
						JSONObject geometry = (JSONObject) temp.get("geometry");
						JSONObject location = (JSONObject) geometry
								.get("location");

						result[3] = Double.parseDouble(location
								.getString("lat"));
						result[4] = Double.parseDouble(location
								.getString("lng"));
					} catch (JSONException e) {
						e.printStackTrace();
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

		// 0 - name, 1 - city, 2 - address, 3 - lat, 4 - lng
		if (result[0] != null && result[1] != null && result[2] != null
				&& (Double) result[3] != -1 && (Double) result[4] != -1) {
			
			navigateToSetGenres();

			new Thread(new Runnable() {

				@Override
				public void run() {
					Location newLocation = new Location(result[0].toString(),
							result[1].toString(), result[2].toString(),
							(Double) result[3], (Double) result[4]);
					newLocation.save();
					/*
					Location location = new Select().from(Location.class).where("lat = ?", (Double) result[3])
							.and("lng = ?", (Double) result[4]).and("address = ?", 
									result[2].toString()).executeSingle();
					
					PreferredLocation newPrefLocation = new PreferredLocation(
							user, location);
					*/
					
					PreferredLocation newPrefLocation = new PreferredLocation(
							user, newLocation);
					
					newPrefLocation.save();
					savePreferences(newPrefLocation.getIdLocation().getCity());
				
					
				}
			}).start();
			
		} else {
			setNoLocationError();
		}

		locationCheck = false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		if(checkUser()) {
			setContentView(R.layout.activity_set_preferences);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			
			fSetLocation = new FragmentSetPreferredLocation();
			fSetGenres = new FragmentSetPreferredGenres();
			if (chooseLocation) {
				ft.add(R.id.activitySetPreferences, fSetLocation).show(fSetLocation).commit();
			}
			Log.i("SETPREF", "go to check genres");
			checkGenres();
			
		}
		else {		
			navigateToLogin();
		}	
	}
	
	public void savePreferences(String location) {

		//SharedPreferences sharedPreferences = PreferenceManager
		//		.getDefaultSharedPreferences(MainActivity.context);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Log.i("SetPreferences", "savePreferences");
		Editor editor = sharedPreferences.edit();
		editor.putString("theLocation", location);
		editor.commit();
	}
	
	public void checkLocation(String name) {	
		if (!locationCheck) {
			locationCheck = true;
			if (name != null && !name.isEmpty()) {
				Location location = new Location().getLocation(name);

				if (location == null) {
					// preferred location can be just a city
					// get lat, lng from google geocoding
					getAddressTask mmTask = new getAddressTask();
					mmTask.execute(new Object[] { name, getApplicationContext() });

				} else {
					// check if location is already preferred
					if (!new PreferredLocation()
							.checkPreferredLocation(location)) {
						PreferredLocation newPrefLocation = new PreferredLocation(
								user, location);
						newPrefLocation.save();

					}

					navigateToSetGenres();
					locationCheck = false;

				}
			} else {
				setNoLocationError();
				locationCheck = false;
			}
		}
	}

	
	public void setPreferedGenres(List<Genre> genres) {
		PreferredGenre prefGenre;
		for (Genre genre : genres) {
			prefGenre = new PreferredGenre(user, genre);
			prefGenre.save();
		}
		navigateToHome();
	}
	
	public void navigateToHome() {
		Intent intent = new Intent(SetPreferencesActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	public void navigateToLogin() {
		Intent intent = new Intent(SetPreferencesActivity.this, LoginActivity.class);
		intent.putExtra("reason", "no-user");
		startActivity(intent);
		finish();
	}
	
	public void navigateToSetGenres() {
		FragmentTransaction ft= getFragmentManager().beginTransaction();	
		ft.replace(R.id.activitySetPreferences, fSetGenres);	
		ft.commit();
		
		chooseLocation = false;
	}

	public void setNoLocationError() {
		fSetLocation.setNoLocationError();
		
	}
	
	public Boolean checkUser() {
		List<User> users = new User().getAll();
		if (users.isEmpty()) {
			return false;
		} else {
			user = users.get(0);
			if (user.getMmApiKey() == null) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	public void checkGenres() {
		int sumGenres = new Genre().getSum();
		if (sumGenres == 0) {
			MMAsyncTask asyncTaskEvents = new MMAsyncTask();
			Object paramsEvent[] = new Object[] { "genres", "get", null,
					new getGenresHandler(), user.getMmApiKey() };
			asyncTaskEvents.execute(paramsEvent);
		}
	}

	
}
