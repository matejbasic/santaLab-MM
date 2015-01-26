package net.neurolab.musicmap.ws;


import java.util.HashMap;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

/**
 * 
 * params[0] -> Session(com.facebook.Session)<br>
 * params[1] -> String action<br>
 * params[2] -> String(?) Data<br>
 * params[3] -> ProgressDialog<br>
 * params[4] .> FacebookResultHandler<br>
 * 
 */

public class FacebookAsyncTask extends AsyncTask<Object, Void, Object[]> {
	
	private HashMap<String, String> data = new HashMap<String, String>();
	
	private Object saveUserId(Session session, FacebookResultHandler handler) {
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
		
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					String userId = user.getId().toString();
					String userName = user.getName().toString();
					if (userName != null && !userName.isEmpty()) {
						data.put("name", userName);
					}
					if (userId != null && !userId.isEmpty()) {
						data.put("id", userId);
						
					}
				}
				
			}
			
			
		} );
		
		return request;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected Object[] doInBackground(Object... params) {
		Object result[] = new Object[] {false, "", null, null, null};

		Session session = (Session)params[0];
		String action = (String)params[1];
		FacebookResultHandler handler = (FacebookResultHandler) params[4];
		
		result[2] = (ProgressDialog) params[3];
		result[3] = (FacebookResultHandler) params[4];
		
		if ( action == "getUserData") {
			Request req = (Request) saveUserId(session, handler);
			req.executeAndWait();
			
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(Object[] result) {
		
		if ((ProgressDialog) result[2] != null) {
			((ProgressDialog) result[2]).cancel();
		}
		if ((FacebookResultHandler) result[3] != null) {
			((FacebookResultHandler) result[3]).handleResults(data);
		}
		
	}

	
}
