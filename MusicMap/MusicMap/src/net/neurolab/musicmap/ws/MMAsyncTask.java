package net.neurolab.musicmap.ws;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


public class MMAsyncTask extends AsyncTask<Object, Void, Object[]> {
	private String serviceUrl = "http://musicmap.azurewebsites.net/api";
	//private String serviceUrl = "http://musicmap.cloudapp.net/api";
	private String apiKey = "2c9s1rwf7578307";
	
	private String httpGetRequest(String url) {
		Log.i("get url", url);
		String response = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		    StringBuilder sb = new StringBuilder();

		    
		    String line = null;
		    while ((line = reader.readLine()) != null)
		    {
		        sb.append(line + "\n");
		    }
		    
			try {
				Log.i("mm async task", "almost done");
				response = new JSONTokener(sb.toString()).nextValue().toString();
				Log.i("mm async task", response.toString());
		    } catch (JSONException e) {
		        e.printStackTrace();
		    }
			
			
			httpClient.getConnectionManager().shutdown();
			return response;
		
		} 
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch(Error e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	private String addUser(String id, String idHash, Boolean fromFacebook) {
		if (fromFacebook) {
			return this.httpGetRequest(this.serviceUrl + "/registerFacebookUser/" + id + "/" + idHash + "/" + this.apiKey);
		}
		else {
			return this.httpGetRequest(this.serviceUrl + "/registerUser/" + id + "/" + idHash + "/" + this.apiKey);
		}
		
	}
	
	private String getUserKey(String id, String idHash, Boolean fromFacebook) {
		if (fromFacebook) {
			return httpGetRequest(this.serviceUrl + "/facebookLogin/" + id + "/" + idHash + "/" + this.apiKey);
		}
		else {
			return httpGetRequest(this.serviceUrl + "/userLogin/" + id + "/" + idHash + "/" + this.apiKey);
		}
	}
	
	private String getGenres(String userKey) {
		return httpGetRequest(this.serviceUrl + "/genres/" + userKey);
	}
	
	private String getEvents(String location) {
		
		String url = this.serviceUrl + "/events/0/" + location + "/0/0/25112014/26112018/10000/0/testbero=)";
		System.out.println("asynctask");
		
		String response = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		    StringBuilder sb = new StringBuilder();

		    
		    String line = null;
		    while ((line = reader.readLine()) != null)
		    {
		        sb.append(line + "\n");
		    }
		    
			try {
		        response = new JSONTokener(sb.toString()).nextValue().toString();
		    } catch (JSONException e) {
		    	System.out.println("Havesomeproblems");
		        e.printStackTrace();
		    }
			
			httpClient.getConnectionManager().shutdown();
			//System.out.println(response);
			return response;
	}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch(Error e) {
			e.printStackTrace();
		}
		return "";
		
	}
	
	@Override
	protected Object[] doInBackground(Object... params) {
		Object result[] = new Object[] { "", false, null, null };
		result[2] = (ProgressDialog) params[2];
		result[3] = (MMAsyncResultHandler) params[3];
		
		String entity = (String) params[0];
		String action = (String) params[1];
		
		if (entity.matches("fbUser")) {
			if (action.matches("add")) {
				if (params[4] != null && params[5] != null) {
					//params[4] - id, params[5] - idHash
					result[0] = this.addUser(params[4].toString(), params[5].toString(), true);
					result[1] = true;
				}
				else {
					result[1] = false;
				}
			}	
			else if (action.matches("getKey")) {
				if (params[4] != null && params[5] != null) {
					//params[4] - id, params[5] - idHash
					//Log.i("fbUser getKey", response);
					result[0] = this.getUserKey(params[4].toString(), params[5].toString(), true);
					result[1] = true;
				}
				else {
					result[1] = false;
				}
			}	
		}
		else if (entity.matches("user")) {
			if (action.matches("add")) {
				if (params[4] != null && params[5] != null) {
					result[0] = this.addUser(params[4].toString(), params[5].toString(), false);
					result[1] = true;
				}
				else {
					result[1] = false;
				}
			}
			else if (action.matches("getKey")) {
				if (params[4] != null && params[5] != null) {
					result[0] = this.getUserKey(params[4].toString(), params[5].toString(), false);
					result[1] = true;
				}
				else {
					result[1] = false;
				}
			}	
		}
		else if (entity.matches("genres")) {
			if (action.matches("get")) {
				//4 - user key
				result[0] = this.getGenres(params[4].toString());
				result[1] = true;
				
			}	
		}
		else if (entity.matches("getEvents")) {				
				//if (params[4] != null && params[5] != null) {
					//params[4] - id | params[5] - idHash
					String response = null;
					String location = (String) params[1];
					
					response = this.getEvents(location);
					
					result[0] = response;
					result[1] = true;
				//}
				//else {
					//result[1] = false;
			//	}
				
		}
		
		return result;
	}

	@Override
	protected void onPostExecute(Object[] result) {
		if ((ProgressDialog) result[2] != null) {
			((ProgressDialog) result[2]).cancel();
		}
		if ((MMAsyncResultHandler) result[3] != null) {
			((MMAsyncResultHandler) result[3]).handleResult((String)result[0], (Boolean)result[1]);
		}
	}

}
