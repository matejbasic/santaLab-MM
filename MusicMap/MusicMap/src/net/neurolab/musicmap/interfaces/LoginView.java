package net.neurolab.musicmap.interfaces;

import java.util.HashMap;

/**
 * Interface for LoginActivity
 * @author Basic
 *
 */
public interface LoginView {
	/**
	 * Shows progress bar.
	 */
	public void showProgress();
	/**
	 * Hides progress bar.
	 */
	public void hideProgress();
	/**
	 * Shows message to inform user that something went wrong
	 * with login via facebook.
	 */
	public void setFacebookLoginError();
	/**
	 * Shows message to inform user that something went wrong
	 * with MusicMap Web Service.
	 */
	public void setMMWebServiceError();
	/**
	 * Shows message to inform user that something went wrong. <br/>
	 * Default error message.
	 */
	public void setUnknownError();
	/**
	 * Navigates to SetPreferencesActivity.
	 */
	public void navigateToPreferences();
	/**
	 * Navigates to MainActivity.
	 */
	public void navigateToHome();
	/**
	 * Gets data after facebook login.
	 * @param data: HashMap<String, String>, facebook data
	 */
	public void getFbFragmentData(HashMap<String, String> data);
	/**
	 * Shows message to inform user that there is no connection
	 * to the Internet.
	 */
	public void setNoConnectionError();
	/**
	 * Sets buttons and appropriate options for login.
	 */
	public void setLoginButtons();
	/**
	 * Checks guests data and proceeds to other activites.
	 */
	public void checkGuest();
}
