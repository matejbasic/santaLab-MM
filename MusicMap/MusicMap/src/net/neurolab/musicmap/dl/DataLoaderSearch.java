package net.neurolab.musicmap.dl;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.EventGenre;
import net.neurolab.musicmap.db.EventMusician;
import net.neurolab.musicmap.db.Genre;
import net.neurolab.musicmap.db.Musician;
import android.app.Activity;
import net.neurolab.musicmap.db.Location;

import com.activeandroid.query.Select;

public class DataLoaderSearch extends DataLoader {

	public void setContext(Activity activity, String location) {
		super.LoadData(activity, location);
	}

	/*
	 * public void searchData(String searchTerm) { searchTerm = "%" + searchTerm
	 * + "%"; List<Event> eventsDb = null;
	 * 
	 * boolean databaseQuerySuccessfull = false; try { eventsDb = new
	 * Select().all().from(Event.class) .where("name LIKE ?", searchTerm)
	 * .or("eventTime LIKE ?", searchTerm).execute(); databaseQuerySuccessfull =
	 * true; } catch (NullPointerException e) { e.printStackTrace(); }
	 * 
	 * if (databaseQuerySuccessfull == true && eventsDb.size() > 0) { events =
	 * (ArrayList<Event>) eventsDb; }
	 * 
	 * DataLoaded(); }
	 */

	public void searchData(String event,/* String genre, String musician,*/
			String city, long dateSince, long dateUntil) {
		event = "%" + event + "%";
		//genre = "%" + genre + "%";
		//musician = "%" + musician + "%";
		city = "%" + city + "%";

		List<Event> eventsDb = null;

		boolean databaseQuerySuccessfull = false;
		try {
			/*
			eventsDb = new Select("Event.id, eventId, Event.name, description, eventTime, lastUpdate, Event.lat, Event.lng, Event.location").all().from(Event.class)
					.join(Location.class).on("Event.location = Location.id").join(EventGenre.class)
					.on("Event.id=EventGenre.idEvent").join(Genre.class).on("EventGenre.idGenre = Genre.id")
					.join(EventMusician.class).on("Event.id = EventMusician.idEvent").join(Musician.class)
					.on("EventMusician.idMusician = Musician.id").where("event.name LIKE ?", event)
					.or("eventTime >= ?", dateSince)
					.or("eventTime <= ?", dateUntil)
					.or("genre.name LIKE ?", genre)
					.or("musician.name LIKE ?", musician)
					.or("city LIKE ?", city).execute();*/
			eventsDb = new Select().all().from(Event.class).join(Location.class).on("Event.location = Location.id")
					.where("event.name LIKE ?", event)
					.and("eventTime >= ?", dateSince)
					.and("eventTime <= ?", dateUntil)
					.and("city LIKE ?", city)
					.execute();
			databaseQuerySuccessfull = true;
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (databaseQuerySuccessfull == true && eventsDb.size() > 0) {
			events = (ArrayList<Event>) eventsDb;
		}

		DataLoaded();
	}

}