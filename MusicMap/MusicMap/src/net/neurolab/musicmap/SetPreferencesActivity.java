package net.neurolab.musicmap;

import java.util.List;

import net.neurolab.musicmap.db.Genre;
import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.fragments.FragmentSetPreferedGenres;
import net.neurolab.musicmap.fragments.FragmentSetPreferedLocation;
import net.neurolab.musicmap.interfaces.SetPreferencesPresenter;
import net.neurolab.musicmap.interfaces.SetPreferencesView;
import net.neurolab.musicmap.presenters.SetPreferences;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class SetPreferencesActivity extends Activity implements SetPreferencesView {
	private FragmentSetPreferedGenres fSetGenres;
	private FragmentSetPreferedLocation fSetLocation;
	private SetPreferencesPresenter presenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		presenter = new SetPreferences(this);
		
		List<User> users = new User().getAll();
		Log.i("users", users.toString());
		
		if(presenter.checkUser()) {
			setContentView(R.layout.activity_set_preferences);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			
			fSetLocation = new FragmentSetPreferedLocation();
			fSetGenres = new FragmentSetPreferedGenres();
			
			ft.add(R.id.activitySetPreferences, fSetLocation).show(fSetLocation).commit();
			
			presenter.checkGenres();
			
		}
		else {
			navigateToLogin();
		}	
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
			Intent settingsActivity = new Intent(this, SettingsActivity.class);
			this.startActivity(settingsActivity);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void checkLocation(String name) {	
		presenter.setLocation(name, getApplicationContext());	
	}
	
	@Override
	public void setPreferedGenres(List<Genre> genres) {
		presenter.setPreferedGenres(genres);
	}
	
	@Override
	public void navigateToHome() {
		Intent intent = new Intent(SetPreferencesActivity.this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void navigateToLogin() {
		Intent intent = new Intent(SetPreferencesActivity.this, LoginActivity.class);
		intent.putExtra("reason", "no-user");
		startActivity(intent);
	}
	
	@Override
	public void navigateToSetGenres() {
		FragmentTransaction ft= getFragmentManager().beginTransaction();
		ft.replace(R.id.activitySetPreferences, fSetGenres);
		ft.addToBackStack(null);
		ft.commit();
		
	}

	@Override
	public void setNoLocationError() {
		fSetLocation.setNoLocationError();
		
	}
}
