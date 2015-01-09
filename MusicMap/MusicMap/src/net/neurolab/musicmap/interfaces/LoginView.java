package net.neurolab.musicmap.interfaces;

import java.util.HashMap;

import android.content.Context;

public interface LoginView {
	
	public void showProgress();
	public void hideProgress();
	public void setFacebookLoginError();
	public void setMMWebServiceError();
	public void setUnknownError();
	public void navigateToPreferences();
	public void navigateToHome();
	public void getFbFragmentData(HashMap<String, String> data);
	public void setNoConnectionError();
	public void setLoginButtons();
}
