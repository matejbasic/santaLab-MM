package net.neurolab.musicmap.fragments;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.neurolab.musicmap.R;
import net.neurolab.musicmap.interfaces.LoginView;
import net.neurolab.musicmap.ws.FacebookAsyncTask;
import net.neurolab.musicmap.ws.FacebookResultHandler;
import android.content.Intent;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class FragmentFacebookLogin extends Fragment {
	
	private UiLifecycleHelper uiHelper;
	private LoginButton authButton;
	private List<String> readPermissions = Arrays.asList("public_profile");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		View view = inflater.inflate(R.layout.activity_login, container, false);
		try {
			authButton = (LoginButton) view.findViewById(R.id.authButton);
			authButton.setFragment(this);
			authButton.setReadPermissions(readPermissions);
			
		}
		catch(Exception exception) {
			Toast.makeText(getActivity(), R.string.facebook_login_failed, Toast.LENGTH_SHORT).show();
		}
		return view;
	}
	FacebookResultHandler getUserBasicData = new FacebookResultHandler() {
		@Override
		public void handleResults(HashMap<String, String> data) {
			
			if (data.containsKey("id") && data.containsKey("name")) {
				//pass data to parent container/activity
				LoginView parent = (LoginView)getActivity();
				parent.getFbFragmentData(data);
			}
			else {
				Toast.makeText(getActivity(), R.string.facebook_login_failed, Toast.LENGTH_LONG).show();
			}
		}
	};
	
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			
			FacebookAsyncTask fbTask = new FacebookAsyncTask();
			Object params[] = new Object[]{session, "getUserData", null, null, getUserBasicData};
			fbTask.execute(params);
			
		}
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
			
		}
	};
	
	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if ( session != null && (session.isOpened() || session.isClosed()) ) {
			//onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override 
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}
	
	@Override 
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
}

