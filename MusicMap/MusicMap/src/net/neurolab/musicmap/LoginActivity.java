package net.neurolab.musicmap;

import java.util.HashMap;

import net.neurolab.musicmap.fragments.FragmentFacebookLogin;
import net.neurolab.musicmap.interfaces.LoginPresenter;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.presenters.Login;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnWindowFocusChangeListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

public class LoginActivity extends FragmentActivity implements LoginView {

	private FragmentFacebookLogin fFacebookLogin;
	private LoginPresenter presenter;
	private boolean userExist;
	private boolean isInstanceSaved;
	private static boolean connectionValid = false;
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
		
		isInstanceSaved = false;
		userExist = false;
		
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
		hideProgress();
		btnFbAuth = (Button) findViewById(R.id.authButton);
		btnFbAuth.setVisibility(View.INVISIBLE);
		
		btnGuest = (Button) findViewById(R.id.btnLoginAsGuest);	
		btnGuest.setVisibility(View.INVISIBLE);
		
		btnCheckConnection = (Button) findViewById(R.id.btnCheckConnection);
		btnCheckConnection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProgress();
				btnCheckConnection.setVisibility(View.INVISIBLE);
				if (!connectionValid) {
					presenter.checkDependencies(getApplicationContext());
				}
			}
		});
		
		presenter = new Login(this);
		if (!connectionValid) {
			presenter.checkDependencies(getApplicationContext());
		}
		
		btnGuest.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				presenter.checkGuest(userExist, getApplicationContext());
				Log.i("btnGuest", "click");
			}
		});	
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onResume() {
		Log.i("connectionStatus", String.valueOf(connectionValid));
		if (connectionValid) {
			btnFbAuth.setVisibility(View.VISIBLE);
			btnGuest.setVisibility(View.VISIBLE);
			
			hideProgress();
			btnCheckConnection.setVisibility(View.INVISIBLE);
		}
		else {
			btnCheckConnection.setVisibility(View.VISIBLE);
			
			btnFbAuth.setVisibility(View.INVISIBLE);
			btnGuest.setVisibility(View.INVISIBLE);
		}
		super.onResume();
	}


	@Override
	public void setLoginButtons() {
		if (!connectionValid) {
			connectionValid = true;
				
			
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
	}

	@Override
	public void getFbFragmentData(HashMap<String, String> data) {
		if (data.containsKey("id") && data.containsKey("name")) {
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
		newToast(R.string.facebook_login_error, Toast.LENGTH_LONG);
	}

	@Override
	public void setMMWebServiceError() {
		newToast(R.string.mm_service_login_error, Toast.LENGTH_LONG);
	}
	@Override
	public void setUnknownError() {
		newToast(R.string.unknown_error, Toast.LENGTH_LONG);
	}
	@Override
	public void setNoConnectionError() {
		hideProgress();
		btnCheckConnection.setVisibility(View.VISIBLE);
        newToast(R.string.no_connection_error, Toast.LENGTH_SHORT);
	}

	private void newToast(int txtId, int duration) {
		LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
        ((TextView) layout.findViewById(R.id.toast_text)).setText(txtId);

        Toast toast = new Toast(getBaseContext());
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
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
