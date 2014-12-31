package net.neurolab.musicmap.ns;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.MainActivity;
import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.EventGenre;
import net.neurolab.musicmap.db.EventGenre_2;
import net.neurolab.musicmap.db.EventMusician;
import net.neurolab.musicmap.db.EventMusician_2;
import net.neurolab.musicmap.db.Genre;
import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.db.Musician;
import net.neurolab.musicmap.ws.JSONAdapter;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.activeandroid.query.Select;

public class NotificationService extends Service {

	private WakeLock mWakeLock;
	private boolean updated;
	/**
	 * Simply return null, since our Service will not be communicating with any
	 * other components. It just does its work silently.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("ONBIND");
		return null;
	}

	/**
	 * This is where we initialize. We call this when onStart/onStartCommand is
	 * called by the system. We won't do anything with the intent here, and you
	 * probably won't, either.
	 */

	private void handleIntent(Intent intent) {
		// obtain the wake lock

		System.out.println("HANDLE INTENT");

		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"MMWakelockTag");
		mWakeLock.acquire();

		// check the global background data setting
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (!cm.getActiveNetworkInfo().isAvailable()) {
			// if (!cm.getBackgroundDataSetting()) {
			System.out.println("STOP");
			stopSelf();
			return;
		}

		// do the actual work, in a separate thread
		new PollTask().execute();
	}

	private class PollTask extends AsyncTask<Void, Void, Void> {
		/**
		 * This is where YOU do YOUR work. There's nothing for me to write here
		 * you have to fill this in. Make your HTTP request(s) or whatever it is
		 * you have to do to get your updates in here, because this is run in a
		 * separate thread
		 */

		ArrayList<Event> events;

		public PollTask() {
			events = new ArrayList<Event>();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// do stuff!

			System.out.println("DO IN BACKGROUND");

			MMAsyncTask asyncTaskEvents = new MMAsyncTask();
			Object paramsEvent[] = new Object[] { "getEvents", "zagreb", null,// parametri?
					eventsHandler, null, null };
			asyncTaskEvents.execute(paramsEvent);

			return null;
		}

		MMAsyncResultHandler eventsHandler = new MMAsyncResultHandler() {

			@Override
			public void handleResult(String result, Boolean status) {
				updated = false;
				if (status) { // PROVJERITI STATUS
					try {
						 
						events = new ArrayList<Event>();
						ArrayList<Location> locations = new ArrayList<Location>();
						ArrayList<Genre> genres = new ArrayList<Genre>();
						ArrayList<Musician> musicians = new ArrayList<Musician>();
						ArrayList<EventGenre_2> eventGenres = new ArrayList<EventGenre_2>();
						ArrayList<EventMusician_2> eventMusicians = new ArrayList<EventMusician_2>();

						JSONAdapter.getEvents(result, events, locations,
								musicians, genres, eventGenres, eventMusicians);

						// System.out.println("DLMM");
						// System.out.println("DLMM");
						for (Location l : locations) {
							List<Location> locs = new Select().all()
									.from(Location.class)
									.where("lat = ?", l.getLat())
									.and("lng = ?", l.getLng()).execute();
							if (locs.size() == 0) {
								l.save();
								// System.out.println("spremljena lokacija");
							}
							// System.out.println(locations.size());
						}

						for (Musician m : musicians) {
							List<Musician> mscs = new Select().all()
									.from(Musician.class)
									.where("name = ?", m.getName()).execute();
							if (mscs.size() == 0) {
								m.save();
							}
							// System.out.println(musicians.size());
						}

						for (Genre g : genres) {
							List<Genre> gnrs = new Select().all()
									.from(Genre.class)
									.where("name = ?", g.getName()).execute();
							if (gnrs.size() == 0) {
								g.save();
							}
							// System.out.println(genres.size());
						}
						// System.out.println("events");
						for (Event e : events) {
							List<Event> evnt = new Select().all()
									.from(Event.class)
									.where("eventId = ?", e.getEventId())
									.execute();

							if (evnt.size() == 0) {
								List<Location> eventLoc = new Select()
										.from(Location.class)
										.where("lat = ?", e.getLat())
										.and("lng = ?", e.getLng()).execute();
								if (eventLoc.size() == 1) {
									e.setIdLocation(eventLoc.get(0));
									e.save();
									updated = true;
								}
							}

						}
						System.out.println(events.size());
						// System.out.println("eventgenres");
						for (EventGenre_2 eg : eventGenres) {
							List<Genre> g = null;
							List<Event> e = null;

							e = new Select().from(Event.class)
									.where("eventId = ?", eg.getIdEvent())
									.execute();
							g = new Select().from(Genre.class)
									.where("name = ?", eg.getName()).execute();

							if (e.size() == 1 && g.size() == 1) {

								EventGenre eventGenre = new EventGenre(
										e.get(0), g.get(0));
								eventGenre.save();
								/*
								 * List<EventGenre> evgn = new Select().all()
								 * .from(EventGenre.class) .where("idEvent = ?",
								 * e.get(0).getId()) .and("idGenre = ?",
								 * g.get(0).getId()) .execute();
								 * 
								 * if (evgn.size() == 0) { if (e != null && g !=
								 * null) { eg.setIdEvent(e.get(0));
								 * eg.setIdGenre(g.get(0)); eg.save();
								 * //System.out.println("spremljeni eg"); } }
								 */
								// System.out.println(eventGenres.size());
							}
						}

						System.out.println("EventMusician");
						for (EventMusician_2 em : eventMusicians) {

							List<Musician> m = null;
							List<Event> ev = null;

							ev = new Select().from(Event.class)
									.where("eventId = ?", em.getIdEvent())
									.execute();
							m = new Select().from(Musician.class)
									.where("name = ?", em.getName()).execute();
							if (ev.size() == 1 && m.size() == 1) {

								EventMusician eventMusician = new EventMusician(
										ev.get(0), m.get(0));
								eventMusician.save();
								/*
								 * List<EventMusician> evms = new Select().all()
								 * .from(EventMusician.class)
								 * .where("idEvent = ?", ev.get(0).getId())
								 * .and("idMusician = ?", m.get(0).getId())
								 * .execute();
								 * 
								 * if (evms.size() == 0) { if (ev != null && m
								 * != null) { em.setIdEvent(ev.get(0));
								 * em.setIdMusician(m.get(0)); em.save(); } }
								 */
								// System.out.println(eventMusicians.size());
							}
						}

						if(updated){
							MainActivity.sendData("updated");
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		};

		/**
		 * In here you should interpret whatever you fetched in doInBackground
		 * and push any notifications you need to the status bar, using the
		 * NotificationManager. I will not cover this here, go check the docs on
		 * NotificationManager.
		 *
		 * What you HAVE to do is call stopSelf() after you've pushed your
		 * notification(s). This will: 1) Kill the service so it doesn't waste
		 * precious resources 2) Call onDestroy() which will release the wake
		 * lock, so the device can go to sleep again and save precious battery.
		 */
		@Override
		protected void onPostExecute(Void result) {

			stopSelf();
		}

	}

	/**
	 * This is deprecated, but you have to implement it if you're planning on
	 * supporting devices with an API level lower than 5 (Android 2.0).
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		System.out.println("onSTART");
		handleIntent(intent);
	}

	/**
	 * This is called on 2.0+ (API level 5 or higher). Returning
	 * START_NOT_STICKY tells the system to not restart the service if it is
	 * killed because of poor resource (memory/cpu) conditions.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("onSTARTCOMMAND");
		handleIntent(intent);
		return START_NOT_STICKY;
	}

	/**
	 * In onDestroy() we release our wake lock. This ensures that whenever the
	 * Service stops (killed for resources, stopSelf() called, etc.), the wake
	 * lock will be released.
	 */
	public void onDestroy() {
		super.onDestroy();
		mWakeLock.release();
	}

}
