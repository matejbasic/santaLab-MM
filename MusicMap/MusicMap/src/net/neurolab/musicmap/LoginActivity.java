package net.neurolab.musicmap;

import java.util.HashMap;
import java.util.concurrent.Executor;

import net.neurolab.musicmap.fragments.FacebookLoginFragment;
import net.neurolab.musicmap.interfaces.LoginPresenterIntf;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.presenters.LoginPresenter;
import net.neurolab.musicmap.ws.MMAsyncTask;
import net.neurolab.musicmap.ws.MMAsyncTask;
import android.app.ActionBar;
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
		
		//active android initialization, must be in launch activity
		ActiveAndroid.initialize(this);
		
		//ActionBar actionBar = getActionBar();
		//actionBar.hide();
		
		this.presenter = new LoginPresenter(this);
		
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
	public class TempAsyncTask extends AsyncTask<Object, Void, Object[]> {

		@Override
		protected Object[] doInBackground(Object... arg0) {
			Log.i("temp task", "done");
			return null;
		}
		
	}
	@Override
	public void getFbFragmentData(HashMap<String, String> data) {
		if ( data.containsKey("id") && data.containsKey("name")) {
			
			String response = this.presenter.checkFbUser(
					data.get("name").toString(), data.get("id").toString(), LoginActivity.this);
			Log.i("loginActivity", response);
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
		Toast.makeText(getApplicationContext(), R.string.facebook_login_error, Toast.LENGTH_LONG).show();
		
	}



	@Override
	public void setMMWebServiceError() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void navigateToSetBasicPref() {
		// TODO Auto-generated method stub
		
	}
	
}
