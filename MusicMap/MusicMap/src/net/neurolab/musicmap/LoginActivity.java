package net.neurolab.musicmap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.fragments.FacebookLoginFragment;
import net.neurolab.musicmap.interfaces.LoginPresenterIntf;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.presenters.LoginPresenter;
import net.neurolab.musicmap.ws.JSONAdapter;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

public class LoginActivity extends FragmentActivity implements LoginView {

	private FacebookLoginFragment facebookLoginFragment;
	private LoginPresenterIntf presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// active android initialization, must be in launch activity
		ActiveAndroid.initialize(this);

		// ActionBar actionBar = getActionBar();
		// actionBar.hide();

		this.presenter = new LoginPresenter(this);

		if (savedInstanceState == null) {
			facebookLoginFragment = new FacebookLoginFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, facebookLoginFragment).commit();

		} else {
			facebookLoginFragment = (FacebookLoginFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}

	public class TempAsyncTask extends AsyncTask<Object, Void, Object[]> {

		@Override
		protected Object[] doInBackground(Object... arg0) {
			Log.i("temp task", "done");
			return null;
		}

	}

	@Override
	public void getFbFragmentData(HashMap<String, String> data) {
		if (data.containsKey("id") && data.containsKey("name")) {

			String response = this.presenter.checkFbUser(data.get("name")
					.toString(), data.get("id").toString(), LoginActivity.this);
			Log.i("loginActivity", response);
			if (response.matches("valid")) {
				this.navigateToSetPrefLocation();
			}
		}
	}

	@Override
	public void showProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFacebookLoginError() {
		Toast.makeText(getApplicationContext(), R.string.facebook_login_error,
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void setMMWebServiceError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToSetPrefLocation() {
		Intent intent = new Intent(LoginActivity.this,
				SetPrefLocationActivity.class);
		startActivity(intent);
	}

	@Override
	public void navigateToHome() {

	}

}
