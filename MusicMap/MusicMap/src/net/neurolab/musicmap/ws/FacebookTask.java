package net.neurolab.musicmap.ws;

import java.util.HashMap;

import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class FacebookTask  {

	public Object getUserInfo(Session session) {
		final HashMap<String, String> userData = new HashMap<String, String>();
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
			
			@Override
			public void onCompleted(GraphUser user, Response response) {
				Log.i("fbOnComplete", "enter");
				if (user != null) {
					userData.put("id", user.getId().toString());
					userData.put("name", user.getUsername().toString());
				}
				
			}
			
			
			
		} );
		
		return Request.executeBatchAsync(request);
		
	}
}
