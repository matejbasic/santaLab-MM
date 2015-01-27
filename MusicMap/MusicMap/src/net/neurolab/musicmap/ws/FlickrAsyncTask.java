package net.neurolab.musicmap.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;

/**
 * Downloads image(.jpg) for given musician.
 * @author Basic
 *
 */
public class FlickrAsyncTask extends AsyncTask<Object, Void, Object[]> {
	private String apiKey = "c08c4a39214b8bdd635718dc3caa22b0";
	private String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
	private String tags = "band%2C+music%2C+group%2C+artist%2C+concert%2C+glazba%2C+zurka%2C+koncert%2C+svirka";
	private String searchParams = "sort=relevance&per_page=1&page=1&format=json&nojsoncallback=1";
	
	@Override
	protected Object[] doInBackground(Object... params) {
		Object result[] = new Object[] {"", null};
		result[1] = (FlickrAsyncResultHandler) params[1];
	
		String artist;
		try {
			artist = URLEncoder.encode(params[0].toString(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			artist = params[0].toString();
			e1.printStackTrace();
		}
		
		url += "&api_key=" + apiKey + "&tags=" + tags + "&text=" + artist + "&" + searchParams;
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		    StringBuilder sb = new StringBuilder();

		    
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		    
			try {
				result[0] = new JSONTokener(sb.toString()).nextValue().toString();
				
		    } catch (JSONException e) {
		        e.printStackTrace();
		    }
			
			httpClient.getConnectionManager().shutdown();
		
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
		
		return result;
	}
	
	@Override
	protected void onPostExecute(Object[] result) {
		JSONObject results = null;
		String returnUrl = "";
		try {
			results = new JSONObject((String)result[0]);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		if (results != null) {
			if (results.has("photos")) {
				try {
					JSONObject photos = new JSONObject(results.getString("photos"));
					
					int total = photos.getInt("total");
					if (total > 0) {
						JSONObject photo = (JSONObject)(photos.getJSONArray("photo")).getJSONObject(0);
						
						int farmId = photo.getInt("farm");
						int serverId = photo.getInt("server");			
						String id = photo.getString("id");
						String secret = photo.getString("secret");
					
						//https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
						returnUrl = "https://farm"+ farmId + ".staticflickr.com/" + serverId + "/" + id + "_"+ secret + ".jpg";				
					}				
				} 
				catch (JSONException e) {	
					e.printStackTrace();
				}
				
				((FlickrAsyncResultHandler) result[1]).handleResult(returnUrl);	
			}
		}
	}
}
