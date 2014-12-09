package net.neurolab.musicmap.interfaces;

import android.app.Activity;
import android.content.Context;

public interface LoginPresenter {
	
	public void checkFbUser(String userName, String fbId, Activity activity);

	public void checkGuest(Boolean userExist, Context context);
}
