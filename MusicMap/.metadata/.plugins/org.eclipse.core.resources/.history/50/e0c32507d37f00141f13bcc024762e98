package net.neurolab.musicmap;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.fragments.FragmentSetPreferedGenres;
import net.neurolab.musicmap.fragments.FragmentSetPreferedLocation;
import net.neurolab.musicmap.interfaces.SetPreferencesView;
import net.neurolab.musicmap.ws.MMAsyncTask;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SetPreferencesActivity extends Activity implements SetPreferencesView {
	private FragmentSetPreferedGenres fSetGenres = null;
	private FragmentSetPreferedLocation fsetLocation = null;
	private Boolean locationCheck = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_preferences);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		
		fsetLocation = new FragmentSetPreferedLocation();
		fSetGenres = new FragmentSetPreferedGenres();
		
		ft.add(R.id.activitySetPreferences, fsetLocation).show(fsetLocation).commit();
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_pref_location, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void saveLocation(String name) {
		if (!locationCheck) {
			locationCheck = true;
			Log.i("prefences activity", name);
			if(name != null && !name.isEmpty()) {
				Log.i("name", "valid");
				List<Location> locations = new Location().getLocation(name);

				Log.i("locations size", String.valueOf(locations.size()));
				
				if(locations.isEmpty()) {
					//?
					//preferred location can be just a city?
					//get lat, lng from google geocoding
					
					Location newLocation = new Location(name, name, "", 0, 0);
					
					LatLngFromAddress mmTask = new LatLngFromAddress();
					Object params[] = new Object[]{name, getApplicationContext()};
					mmTask.execute(params);
					
					//newLocation.save();
					
					//it would be better to get active user id from application storage!
					List<User> users = new User().getAll();
					
					Log.i("users size", String.valueOf(users.size()));
					
					if (users.isEmpty()) {
						this.navigateToLogin();
					}
					else {
						User user = users.get(0);
						long userId = user.getId();
						Log.i("userId", String.valueOf(userId));
					}
					//PreferredLocation newPrefLocation = new PreferredLocation(idUser, idLocation);
					
				}
				
				FragmentTransaction ft= getFragmentManager().beginTransaction();
				ft.replace(R.id.activitySetPreferences, fSetGenres);
				ft.addToBackStack(null);
				ft.commit();
			}
			else {
				this.setNoLocationError();
			}
		}
		locationCheck = false;
	}

	@Override
	public void setNoLocationError() {
		Toast.makeText(getApplicationContext(), R.string.no_location_error, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void navigateToLogin() {
		Intent intent = new Intent(SetPreferencesActivity.this, LoginActivity.class);
		intent.putExtra("reason", "no-user");
		
		startActivity(intent);
	}
	
	private class LatLngFromAddress extends AsyncTask<Object, Void, Object[]> {
		
		@Override
		protected Object[] doInBackground(Object... params) {
			String locationName = (String) params[0];
			Context context = (Context) params[1];
			
			try {
				Geocoder gc = new Geocoder(context, Locale.getDefault());
				List<Address> addresses = gc.getFromLocationName(locationName,1);
				Address address = addresses.get(0);
				Log.i("address", address.toString());
			} catch (IOException e) {
				Log.i("geoCoding", "fail");
				e.printStackTrace();
			}
			return null;
		}
		
		
		
	}
}
