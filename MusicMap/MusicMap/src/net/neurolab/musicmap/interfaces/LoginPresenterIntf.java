package net.neurolab.musicmap.interfaces;

import android.app.Activity;
import android.content.Context;

public interface LoginPresenterIntf {
	public String checkFbUser(String userName, String fbId, Activity activity);
	public void checkUserData();
	public void saveFbUserToWS(String id, Context context);
}
