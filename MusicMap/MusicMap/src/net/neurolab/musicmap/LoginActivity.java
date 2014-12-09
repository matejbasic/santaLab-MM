package net.neurolab.musicmap;

import java.util.HashMap;

import net.neurolab.musicmap.fragments.FragmentFacebookLogin;
import net.neurolab.musicmap.interfaces.LoginPresenter;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.presenters.Login;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

public class LoginActivity extends FragmentActivity implements LoginView {

	private FragmentFacebookLogin fFacebookLogin;
	private TextView loginAsGuest = null;
	private LoginPresenter presenter;
	private Boolean userExist = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// active android initialization, must be in launch activity
		ActiveAndroid.initialize(this);
		Bundle extras = getIntent().getExtras();
		loginAsGuest = (TextView) findViewById(R.id.txtLoginAsGuest);
		
		if (extras != null) {
			String reason = extras.getString("reason");
			if ( reason == "no-key") {
				userExist = true;
			}
			
		}
		this.presenter = new Login(this);
		
		loginAsGuest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Log.i("login as Guest", "clicked");
				presenter.checkGuest(userExist, getApplicationContext());
				
			}
		});
		
		if (savedInstanceState == null) {
			fFacebookLogin = new FragmentFacebookLogin();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, fFacebookLogin).commit();

		} else {
			fFacebookLogin = (FragmentFacebookLogin) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}

	

	@Override
	public void getFbFragmentData(HashMap<String, String> data) {
		if (data.containsKey("id") && data.containsKey("name")) {
			//Log.i("check fb user", "pre");
			presenter.checkFbUser(data.get("name")
					.toString(), data.get("id").toString(), LoginActivity.this);
			
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
		Toast.makeText(getApplicationContext(), R.string.mm_service_login_error,
				Toast.LENGTH_LONG).show();

	}
	@Override
	public void setUnknownError() {
		Toast.makeText(getApplicationContext(), R.string.unknown_error,
				Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void navigateToPreferences() {
		Intent intent = new Intent(LoginActivity.this,
				SetPreferencesActivity.class);
		startActivity(intent); 
	}

	@Override
	public void navigateToHome() {
		Intent intent = new Intent(LoginActivity.this,
				MainActivity.class);
		startActivity(intent); 
	}

}
