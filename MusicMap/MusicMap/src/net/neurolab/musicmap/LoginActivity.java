package net.neurolab.musicmap;

import java.util.HashMap;

import net.neurolab.musicmap.fragments.FragmentFacebookLogin;
import net.neurolab.musicmap.interfaces.LoginPresenter;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.presenters.Login;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

public class LoginActivity extends FragmentActivity implements LoginView {

	private FragmentFacebookLogin fFacebookLogin;
	private LoginPresenter presenter;
	private boolean userExist = false;
	private boolean isInstanceSaved = false;
	private ProgressBar progressBar;
	private Button btnCheckConnection;
	private Button btnFbAuth;
	private Button btnGuest;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// active android initialization, must be in launch activity
		ActiveAndroid.initialize(this);
		
		Bundle extras = getIntent().getExtras();		
		if (extras != null) {
			String reason = extras.getString("reason");
			if ( reason == "no-key") {
				userExist = true;
			}		
		}
		
		if (savedInstanceState != null) {
			isInstanceSaved = true;
		}
		
		progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
		btnFbAuth = (Button) findViewById(R.id.authButton);
		
		btnGuest = (Button) findViewById(R.id.btnLoginAsGuest);	
		
		btnCheckConnection = (Button) findViewById(R.id.btnCheckConnection);
		btnCheckConnection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProgress();
				btnCheckConnection.setVisibility(View.INVISIBLE);
				presenter.checkDependencies(getApplicationContext());
			}
		});
		
		presenter = new Login(this);
		presenter.checkDependencies(getApplicationContext());	
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		/*
		LayoutParams params = new LayoutParams(btnFbAuth.getWidth(), (int)(btnFbAuth.getHeight()*0.95));
		btnGuest.setLayoutParams(params);
		//btnGuest.setWidth(btnFbAuth.getWidth());
		//btnGuest.setHeight((int)(btnFbAuth.getHeight()*0.8));
		btnCheckConnection.setLayoutParams(params);
		//btnCheckConnection.setWidth(btnFbAuth.getWidth());
		//btnCheckConnection.setHeight((int)(btnFbAuth.getHeight()*0.85));
		
		Log.i("auth W", String.valueOf(btnFbAuth.getWidth()));
		Log.i("auth H", String.valueOf(btnFbAuth.getHeight()));
		Log.i("guest W", String.valueOf(btnGuest.getWidth()));
		Log.i("guest H", String.valueOf(btnGuest.getHeight()));
		*/
		
	}

	@Override
	public void setLoginButtons() {
		
		btnGuest.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				presenter.checkGuest(userExist, getApplicationContext());
			}
		});		
		
		hideProgress();
		btnCheckConnection.setVisibility(View.INVISIBLE);
		
		btnFbAuth.setVisibility(View.VISIBLE);
		btnGuest.setVisibility(View.VISIBLE);
		
		if (isInstanceSaved) {
			fFacebookLogin = new FragmentFacebookLogin();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, fFacebookLogin).commit();
		} 
		else {
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
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progressBar.setVisibility(View.INVISIBLE);
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
	public void setNoConnectionError() {
		hideProgress();
		btnCheckConnection.setVisibility(View.VISIBLE);
		Toast.makeText(getApplicationContext(), R.string.no_connection_error,
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
