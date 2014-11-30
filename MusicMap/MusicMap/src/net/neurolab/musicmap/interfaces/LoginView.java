package net.neurolab.musicmap.interfaces;

import java.util.HashMap;

public interface LoginView {
	
	public void showProgress();
	public void hideProgress();
	public void setFacebookLoginError();
	public void setMMWebServiceError();
	public void navigateToSetBasicPref();
	public void getFbFragmentData(HashMap<String, String> data);
	
}