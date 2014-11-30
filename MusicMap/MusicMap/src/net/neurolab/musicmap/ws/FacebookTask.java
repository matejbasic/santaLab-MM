package net.neurolab.musicmap.ws;

import java.util.HashMap;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import android.util.Log;
import bolts.Task;

public class FacebookTask  {

	public Object getUserInfo(Session session) {
		final HashMap<String, String> userData = new HashMap<String, String>();
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
			
			@Override
			public void onCompleted(GraphUser user, Response response) {
				Log.i("fbOnComplete", "enter");
				if (user != null) {
					Log.i("fbUserId", user.getId().toString());
					Log.i("fbUserName", user.getName().toString());
						
						userData.put("id", user.getId().toString());
						userData.put("name", user.getUsername().toString());
				}
				
			}
			
			
			
		} );
		
		return Request.executeBatchAsync(request);
		
	}
}
