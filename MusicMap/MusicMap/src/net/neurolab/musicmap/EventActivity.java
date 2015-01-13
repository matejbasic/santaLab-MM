package net.neurolab.musicmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.EventMusician;
import net.neurolab.musicmap.db.EventPrice;
import net.neurolab.musicmap.db.Musician;
import net.neurolab.musicmap.ws.FlickrAsyncResultHandler;
import net.neurolab.musicmap.ws.FlickrAsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.squareup.picasso.Picasso;

public class EventActivity extends YouTubeFailureRecoveryActivity implements OnInitializedListener {
	private Event event;
	private SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd.MM.yy.", Locale.getDefault());
	private ImageView imgView;
	private boolean mainImgFetched = false;
	private ArrayList<String> musicianImgAvailable = new ArrayList<String>();
	private ExpandableListView listView;
	
	private ImageView shareEvent;
	
	private class mainImgHandler implements FlickrAsyncResultHandler {

		@Override
		public void handleResult(String imgUrl) {
			if (imgUrl != null && !imgUrl.isEmpty()) {
				Picasso.with(getApplicationContext()).load(imgUrl).into(imgView);
			}
			else {
				if (!musicianImgAvailable.isEmpty()) {
					FlickrAsyncTask fTask = new FlickrAsyncTask();
					fTask.execute(new Object[]{musicianImgAvailable.get(0), new mainImgHandler()});
					musicianImgAvailable.remove(0);
				}	
			}
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		
		shareEvent = (ImageView) findViewById(R.id.shareEvent);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			
			long eventId = extras.getLong("eventId");
			event = new Event().getById(eventId);		
			if (event != null) {
				//SET HEADER
				//event header title
				if (event.getName() != null && !event.getName().isEmpty()) {
					TextView txtName = (TextView) findViewById(R.id.eventName);
					txtName.setText(event.getName());
				}
				//event location
				if (event.getIdLocation().getName() != null && !event.getIdLocation().getName().isEmpty()) {
					TextView txtLocation = (TextView) findViewById(R.id.eventLocation);
					txtLocation.setText(event.getIdLocation().getName());
				}
				//event start date/time
				if (event.getEventTime() != null) {
					TextView txtTime = (TextView) findViewById(R.id.eventTime);
					txtTime.setText(sdf.format(event.getEventTime()));
				}
				
				//event price
				TextView txtPrice = (TextView) findViewById(R.id.eventPrice);
				try {
					List<EventPrice> prices = event.prices();
					if (prices != null) {
						for (EventPrice price : prices) {
							if (!price.getPrice().equals("-")) {
								txtPrice.append(price.getPrice() + "\n");
							}
						}
					}
				}
				catch(Exception e) {
					Log.e("fEvent prices exc", e.toString());
				}
				catch (Error e) {
					Log.e("fEvent prices err", e.toString());
				}
				
				//SET BODY
				
				//0 - group type, 1 - group header, 2 - item content, 3 - is fragment initialized, 4 - player hash, 5 - videoId
				EventGroups.getInstance().listGroups.add(new Object[]{"event-details", "Event details", Html.fromHtml(event.getDescription())});
				
				//event artist info
				List<Musician> eventMusicians = (List<Musician>) new EventMusician().getMusiciansByEvent(event);			
				imgView = (ImageView)findViewById(R.id.eventMainImage);
				
				for (Musician musician : eventMusicians) {
					if (!mainImgFetched) {
						FlickrAsyncTask fTask = new FlickrAsyncTask();
						fTask.execute(new Object[]{musician.getName(), new mainImgHandler()});
						mainImgFetched = true;
					}
					else {
						musicianImgAvailable.add(musician.getName());
					}
					EventGroups.getInstance().listGroups.add(new Object[]{"musician-bio", musician.getName(), musician.getBiography(), false, null, ""});
				}
				
				//expandable list
				listView = (ExpandableListView) findViewById(R.id.eventDetailsList);
				
				if (listView != null) {
					EventDetailsExpandableAdapter adapter = new EventDetailsExpandableAdapter(this, listView);
					listView.setAdapter(adapter);
				}
				else {
					Log.i("EventDetailsList", "null");
				}
			}
			else {
				Log.i("event", "null!");
			}
		}
	
		//share kod
		shareEvent.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.setType("text/plain");
				sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this event: "+event.getName()+" at "+event.getIdLocation().getAddress()+" on "+event.getEventTime());
				startActivity(Intent.createChooser(sendIntent, "Tell Your Friends"));	
			}
		});	
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (listView != null) {
			listView.setIndicatorBounds(listView.getRight() - 40, listView.getWidth());
		}
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		Log.i("Youtube player", "failed to init");
		Toast.makeText(getApplicationContext(), R.string.yt_player_no_app_error, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onInitializationSuccess(Provider provider, YouTubePlayer player,
			boolean wasRestored) {
		if (!wasRestored) {
			for (Object[] group : EventGroups.getInstance().listGroups) {
				if (group.length >= 3) {
					try {
						if (Integer.parseInt(group[4].toString()) == provider.hashCode()) {
							if (!group[5].toString().isEmpty()) {
								player.cueVideo(group[5].toString());
							}
						}
					}
					catch(Exception exc) {
						exc.getStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		//return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubePlayerFragment);
		return null;
	}
	
	@Override
	protected void onDestroy() {
		EventGroups.getInstance().listGroups.clear();
		super.onDestroy();
	}
	
}
