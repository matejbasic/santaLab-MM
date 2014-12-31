package net.neurolab.musicmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.EventMusician;
import net.neurolab.musicmap.db.EventPrice;
import net.neurolab.musicmap.db.Musician;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class EventActivity extends Activity {
	private Event event;
	private SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd.MM.yy.", Locale.getDefault());
	private ImageView imgView;
	private boolean mainImgFetched = false;
	
	private class FlickrTask extends AsyncTask<Object, Void, String> {
		private String apiKey = "c08c4a39214b8bdd635718dc3caa22b0";
		private String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
		private String tags = "band%2C+music%2C+group%2C+artist%2C+concert%2C+glazba%2C+zurka%2C+koncert%2C+svirka";
		private String searchParams = "sort=relevance&per_page=1&page=1&format=json&nojsoncallback=1";
		private ImageView orgImgView;
		@Override
		protected String doInBackground(Object... params) {
			orgImgView = (ImageView)params[1];
			String artist;
			try {
				artist = URLEncoder.encode(params[0].toString(), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				artist = params[0].toString();
				e1.printStackTrace();
			}
			
			url += "&api_key=" + apiKey + "&tags=" + tags + "&text=" + artist + "&" + searchParams;
			
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
			    while ((line = reader.readLine()) != null) {
			        sb.append(line + "\n");
			    }
			    
				try {
					response = new JSONTokener(sb.toString()).nextValue().toString();
					
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
			
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			JSONObject results = null;
			try {
				results = new JSONObject(result);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			if (results != null) {
				if (results.has("photos")) {
					try {
						JSONObject photos = new JSONObject(results.getString("photos"));
						
						Log.i("photos", photos.toString());
						int total = photos.getInt("total");
						if (total > 0) {
							JSONObject photo = (JSONObject)(photos.getJSONArray("photo")).getJSONObject(0);
							
							Log.i("photo", photo.toString());
							
							int farmId = photo.getInt("farm");
							int serverId = photo.getInt("server");			
							String id = photo.getString("id");
							String secret = photo.getString("secret");
							
							//is format always .jpg?
							//https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
							String returnUrl = "https://farm"+ farmId + ".staticflickr.com/" + serverId + "/" + id + "_"+ secret + ".jpg";
							
							Picasso.with(getApplicationContext()).load(returnUrl).into(orgImgView);
						}
						else {
							mainImgFetched = false;
						}
						
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
					
				}
			}
		}
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_event);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			long eventId = extras.getLong("eventId");
			Log.i("eventId", String.valueOf(eventId));
			
			event = new Event().getById(eventId);
			if (event != null) {

				if (event.getName() != null && !event.getName().isEmpty()) {
					TextView txtName = (TextView) findViewById(R.id.eventName);
					txtName.setText(event.getName());
				}
				
				if (event.getDescription() != null && !event.getDescription().isEmpty()) {
					TextView txtDesc = (TextView) findViewById(R.id.eventDesc);
					txtDesc.setText(Html.fromHtml(event.getDescription()));
				}
				
				if (event.getIdLocation().getName() != null && !event.getIdLocation().getName().isEmpty()) {
					TextView txtLocation = (TextView) findViewById(R.id.eventLocation);
					txtLocation.setText(event.getIdLocation().getName() + ", " + event.getIdLocation().getCity());
				}
				if (event.getEventTime() != null) {
					TextView txtTime = (TextView) findViewById(R.id.eventTime);
					txtTime.setText(sdf.format(event.getEventTime()));
				}
				
				
				TextView txtPrice = (TextView) findViewById(R.id.eventPrice);
				try {
					List<EventPrice> prices = event.prices();
					Log.i("fEvent", prices.toString());
					if (prices != null) {
						Log.i("eventPrices", event.getName() + String.valueOf(prices));
						for (EventPrice price : prices) {
							if (!price.getPrice().equals("-")) {
								txtPrice.append(price.getPrice() + "\n");
							}
						}
						Log.i("fEvent", "txtPrice added");
					}
				}
				catch(Exception e) {
					Log.e("fEvent prices err", e.toString());
				}
				catch (Error e) {
					Log.e("fEvent prices err", e.toString());
				}
				
				
				List<Musician> eventMusicians = (List<Musician>) new EventMusician().getMusiciansByEvent(event);
				
				LinearLayout musiciansContainer = (LinearLayout) findViewById(R.id.eventMusicians);
				
				for (Musician musician : eventMusicians) {
					imgView = (ImageView)findViewById(R.id.eventMainImage);
					if (!mainImgFetched) {
						FlickrTask fTask = new FlickrTask();
						fTask.execute(new Object[]{musician.getName(), imgView});
						mainImgFetched = true;
					}
					TextView musicianName = new TextView(getApplicationContext());
					musicianName.setText(musician.getName());
					musiciansContainer.addView(musicianName);
					if (!musician.getBiography().isEmpty() && musician.getBiography() != "-") {
						TextView musicianBio = new TextView(getApplicationContext());
						musicianBio.setText(musician.getBiography());
						musiciansContainer.addView(musicianBio);
					}
				}
				
			}
			else {
				Log.i("event", "is null!");
			}
		}
	
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onPostCreate(savedInstanceState);
	}
	
	
	
}
