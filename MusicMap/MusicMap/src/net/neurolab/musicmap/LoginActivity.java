package net.neurolab.musicmap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class LoginActivity extends FragmentActivity {

	private FacebookLoginFragment facebookLoginFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		if (savedInstanceState == null) {
			facebookLoginFragment = new FacebookLoginFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(android.R.id.content, facebookLoginFragment)
			.commit();
		}
		else {
			facebookLoginFragment = (FacebookLoginFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
}
