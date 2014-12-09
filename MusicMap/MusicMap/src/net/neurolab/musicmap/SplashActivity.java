package net.neurolab.musicmap;

import net.neurolab.musicmap.interfaces.SplashView;
import net.neurolab.musicmap.presenters.Splash;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

public class SplashActivity extends Activity implements SplashView {
	private Splash presenter = null;
	private ProgressBar progressBar;
	private Button btnCheckConnection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// active android initialization, must be in launch activity
		ActiveAndroid.initialize(this);
		
		progressBar = (ProgressBar) findViewById(R.id.splashProgressBar);
		btnCheckConnection = (Button) findViewById(R.id.btnCheckConnection);
		btnCheckConnection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				progressBar.setVisibility(View.VISIBLE);
				btnCheckConnection.setVisibility(View.GONE);
				presenter.checkDependencies(getApplicationContext());
			}
		});
		
		presenter = new Splash(this);
		presenter.checkDependencies(getApplicationContext());	
	}

	@Override
	public void navigateToLogin(String reason) {
	
		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
		if (reason != null) {
			intent.putExtra("reason", reason);
		}
		startActivity(intent);
	}

	@Override
	public void setNoConnectionError() {
		progressBar.setVisibility(View.GONE);
		btnCheckConnection.setVisibility(View.VISIBLE);
		Toast.makeText(getApplicationContext(), R.string.no_connection_error, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void navigateToPreferences() {
		
		Intent intent = new Intent(SplashActivity.this, SetPreferencesActivity.class);
		startActivity(intent);
		
	}

	@Override
	public void navigateToHome() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		
	}
}
