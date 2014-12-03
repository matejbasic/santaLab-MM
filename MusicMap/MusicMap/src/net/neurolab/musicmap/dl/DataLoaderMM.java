package net.neurolab.musicmap.dl;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.EventGenre;
import net.neurolab.musicmap.db.EventMusician;
import net.neurolab.musicmap.db.Genre;
import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.db.Musician;
import net.neurolab.musicmap.ws.JSONAdapter;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;
import android.app.Activity;

import com.activeandroid.query.Select;
public class DataLoaderMM extends DataLoader {

	private boolean eventsLoaded = false;
	private boolean locationsLoaded = false;

	// ...

	@Override
	public void LoadData(Activity activity) {
		super.LoadData(activity);

		MMAsyncTask asyncTaskEvents = new MMAsyncTask();
		Object paramsEvent[] = new Object[] { "getEvents", null, null,//parametri?
				eventsHandler, null, null };
		asyncTaskEvents.execute(paramsEvent);
	}

	MMAsyncResultHandler eventsHandler = new MMAsyncResultHandler() {
		
		
		@Override
		public void handleResult(String result, Boolean status) {
			if (status) { // PROVJERITI STATUS
				try {
					//ArrayList<Event> events = new ArrayList<Event>();
					ArrayList<Location> locations = new ArrayList<Location>();
					ArrayList<Genre> genres = new ArrayList<Genre>();
					ArrayList<Musician> musicians = new ArrayList<Musician>();
					ArrayList<EventGenre> eventGenres = new ArrayList<EventGenre>();
					ArrayList<EventMusician> eventMusicians = new ArrayList<EventMusician>();
					
					JSONAdapter.getEvents(result, events, locations, musicians, genres, eventGenres, eventMusicians);
				
					System.out.println("DLMM");
					for (Location l : locations) {	
						List<Location> locs = new Select().all().from(Location.class).where("lat = ?", l.getLat() ).or("lng = ?", l.getLng() ).execute();
						if (locs.size() == 0){
							l.save();
						}
						System.out.println(locations.size());
					}
					
					for (Musician m : musicians) {
						List<Musician> mscs = new Select().all().from(Musician.class).where("name = ?", m.getName()).execute();
						if (mscs.size() == 0){
							m.save();
						}
						System.out.println(musicians.size());
					}
					
					for (Genre g : genres) {
						List<Genre> gnrs = new Select().all().from(Genre.class).where("name = ?", g.getName()).execute();
						if (gnrs.size() == 0){
							g.save();
						}
						System.out.println(genres.size());
					}					
					
					for (Event e : events) {
						Event evnt = (Event) new Select().from(Event.class).where("eventId = ?", e.getEventId()).execute();
						if( evnt == null){
						e.save();
						}
						System.out.println(events.size());
					}
					
					for (EventGenre eg : eventGenres){
						Genre g = null;
						Event e = null;
						
						e = (Event) new Select().from(Event.class).where("eventId = ?", eg.getIdEvent().getEventId()).execute();
						g = (Genre) new Select().from(Genre.class).where("name = ?", eg.getIdGenre().getName()).execute();
						/*
						for( int i=0; i<events.size();i++)
						{
							if(events.get(i).getEventId() == eg.idEvent.getEventId()) {
								e = events.get(i);
							}							
						}
						for( int i=0; i<genres.size();i++)
						{						
							if(genres.get(i).getName() == eg.idGenre.getName()) {
								g = genres.get(i);
							}
						}*/						
						//eg.idEvent = e;
						//eg.idGenre = g;
						if(e != null && g != null){
							eg.setIdEvent(e);
							eg.setIdGenre(g);
							eg.save();
						}
						System.out.println(eventGenres.size());
					}
					for (EventMusician em : eventMusicians){
						Musician m = null;
						Event e = null;
						
						e = (Event) new Select().from(Event.class).where("eventId = ?", em.getIdEvent().getEventId()).execute();
						m = (Musician) new Select().from(Musician.class).where("name = ?", em.getIdMusician().getName()).execute();
						
						if(e != null && m != null){
							em.setIdEvent(e);
							em.setIdMusician(m);
							em.save();
						}
						System.out.println(eventMusicians.size());
					}
					
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
			eventsLoaded = true;
			DataLoaded();
			//showLoadedData();
		}
	};

	/*
	private void showLoadedData() {
		// Synchronization of results
		if (eventsLoaded) {
			bindLocationsToEvents();

			eventsLoaded = false;

			DataLoaded();
		}
	}
	*/
/*
	private void bindLocationsToEvents() {// join
		for (Location l : locations) {
			for (Event e : events) {
				if (true ) {
					// e.setLocation(l);
				//	e.save();
				}
			}
		}
	}*/
}