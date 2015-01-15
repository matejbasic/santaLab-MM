package net.neurolab.musicmap;

import java.util.List;

import net.neurolab.musicmap.db.Genre;
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

public class SetPreferencesActivity extends Activity implements SetPreferencesView {
	private FragmentSetPreferedGenres fSetGenres;
	private FragmentSetPreferedLocation fSetLocation;
	private SetPreferencesPresenter presenter;
	private static boolean chooseLocation = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		presenter = new SetPreferences(this);
		
		if(presenter.checkUser()) {
			setContentView(R.layout.activity_set_preferences);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			
			fSetLocation = new FragmentSetPreferedLocation();
			fSetGenres = new FragmentSetPreferedGenres();
			if (chooseLocation) {
				ft.add(R.id.activitySetPreferences, fSetLocation).show(fSetLocation).commit();
			}
			Log.i("SETPREF", "go to check genres");
			presenter.checkGenres();
			
		}
		else {
			
			navigateToLogin();
		}	
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
		ft.commit();
		
		chooseLocation = false;
	}

	@Override
	public void setNoLocationError() {
		fSetLocation.setNoLocationError();
		
	}
}
