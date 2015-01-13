package net.neurolab.musicmap.dl;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.EventGenre;
import net.neurolab.musicmap.db.EventGenre_2;
import net.neurolab.musicmap.db.EventMusician;
import net.neurolab.musicmap.db.EventMusician_2;
import net.neurolab.musicmap.db.Genre;
import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.db.Musician;
import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.ws.JSONAdapter;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;
import android.app.Activity;
import android.util.Log;

import com.activeandroid.query.Select;
public class DataLoaderMM extends DataLoader {

	@Override
	public void LoadData(Activity activity, String location) {
		super.LoadData(activity, location);
		
		MMAsyncTask asyncTaskEvents = new MMAsyncTask();
		List<User> users = new User().getAll();
		if (!users.isEmpty()) {
			for (User user : users) {
				if (!user.getMmApiKey().isEmpty()) {
					Log.i("cUser", user.getMmApiKey());
					
					Object paramsEvent[] = new Object[] { "user", "getEvents", null,
							eventsHandler, location, user.getMmApiKey() };
					asyncTaskEvents.execute(paramsEvent);
					break;
				}
			}
			
		}
	}

	MMAsyncResultHandler eventsHandler = new MMAsyncResultHandler() {
		
		
		@Override
		public void handleResult(String result, Boolean status) {
			if (status) { // PROVJERITI STATUS
				try {
					events = new ArrayList<Event>();					
					ArrayList<Location> locations = new ArrayList<Location>();
					ArrayList<Genre> genres = new ArrayList<Genre>();
					ArrayList<Musician> musicians = new ArrayList<Musician>();
					ArrayList<EventGenre_2> eventGenres = new ArrayList<EventGenre_2>();
					ArrayList<EventMusician_2> eventMusicians = new ArrayList<EventMusician_2>();
					
					JSONAdapter.getEvents(result, events, locations, musicians, genres, eventGenres, eventMusicians);
				
					for (Location l : locations) {
						List<Location> locs = new Select().all()
								.from(Location.class)
								.where("lat = ?", l.getLat())
								.and("lng = ?", l.getLng()).execute();
						if (locs.size() == 0) {
							l.save();
								
						}
					}

					for (Musician m : musicians) {
						List<Musician> mscs = new Select().all()
								.from(Musician.class)
								.where("name = ?", m.getName()).execute();
						if (mscs.size() == 0) {
							m.save();
						}
					}

					for (Genre g : genres) {
						List<Genre> gnrs = new Select().all().from(Genre.class)
								.where("name = ?", g.getName()).execute();
						if (gnrs.size() == 0) {
							g.save();
						}
					}
					
					for (Event e : events) {
						List<Event> evnt = new Select().all().from(Event.class)
								.where("eventId = ?", e.getEventId()).execute();
						
						if (evnt.size() == 0) {
							List<Location> eventLoc = new Select().from(Location.class).where("lat = ?", e.getLat() )
									.and("lng = ?", e.getLng()).execute();
							if(eventLoc.size()==1){
								e.setIdLocation(eventLoc.get(0));
								e.save();
								//System.out.println(e.getName());								
								}
						}
						
					}
					
					for (EventGenre_2 eg : eventGenres) {
						List<Genre> g = null;
						List<Event> e = null;

						e = new Select()
								.from(Event.class)
								.where("eventId = ?",
										eg.getIdEvent()).execute();
						g = new Select().from(Genre.class)
								.where("name = ?", eg.getName())
								.execute();

						if (e.size() == 1 && g.size() == 1) {
							
							EventGenre eventGenre = new EventGenre(e.get(0), g.get(0));
							eventGenre.save();
						}
					}
					
					for (EventMusician_2 em : eventMusicians) {

						List<Musician> m = null;
						List<Event> ev = null;

						ev = new Select()
								.from(Event.class)
								.where("eventId = ?",
										em.getIdEvent()).execute();
						m = new Select()
								.from(Musician.class)
								.where("name = ?", em.getName())
								.execute();
						if (ev.size() == 1 && m.size() == 1) {
							
							EventMusician eventMusician = new EventMusician(ev.get(0), m.get(0));
							eventMusician.save();

						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//eventsLoaded = true;
			DataLoaded();
			//showLoadedData();
		}
	};

}