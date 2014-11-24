package net.neurolab.musicmap.ws;

import java.util.Arrays;
import java.util.List;

import android.os.StrictMode;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class FacebookWS {
	
	private List<String> readPermissions = Arrays.asList("public_profile");
	private String userId = null;
	private String userName = null;
	
	public void setLoginPermissions(LoginButton authButton) {
		authButton.setReadPermissions(readPermissions);
	}

	public void saveUserId(Session session) {
		
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
				
			@Override
			public void onCompleted(GraphUser user, Response response) {
				// If the response is successful
				
				if (user != null) {
					Log.i("fbUserId", user.getId().toString());
					Log.i("fbUserName", user.getName().toString());
					userId = user.getId().toString();
					userName = user.getName().toString();
				}
			}
		} );
		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//StrictMode.setThreadPolicy(policy);
		request.executeAndWait();
		
	}

	
}
