package net.neurolab.musicmap.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.EventGenre;
import net.neurolab.musicmap.db.EventMusician;
import net.neurolab.musicmap.db.Genre;
import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.db.Musician;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
public class JSONAdapter {

	/**
	 * Static method for getting a JSON string representing an array, but
	 * containing only one object. As PHP (in our implementation) expects the
	 * data in array, it is not possible to use string that represent only one
	 * object, but rather array of objects.
	 * 
	 * @param jsonObject
	 *            Object containing name value pairs.
	 * @return String describing an array which contains one object with its
	 *         name value pairs. <br>
	 *         An example:<br>
	 *         <code> [{"name":"value", "surname":"survalue"}]</code>
	 * 
	 */
	/*
	 * public static String getJsonArrayString(JSONObject jsonObject) { if
	 * (jsonObject != null) { JSONArray tmp = new JSONArray();
	 * tmp.put(jsonObject); return tmp.toString(); } else return "[]"; // an
	 * empty array }
	 */

	/**
	 * Static method for converting the jsonString results containing the
	 * information about events into an ArrayList of Events.
	 * 
	 * @param jsonString
	 *            String containing a JSONArray of objects with all name value
	 *            pairs.
	 * @return ArrayList of Store objects containing the information on stores.
	 * @throws Exception
	 * @throws Exception
	 *             If conversion from string to JSONObject is not possible the
	 *             exception is raised.
	 * 
	 */

	public static void getEvents(String jsonString, ArrayList<Event> events,
			ArrayList<Location> locations, ArrayList<Musician> musicians,
			ArrayList<Genre> genres, ArrayList<EventGenre> eventGenres,
			ArrayList<EventMusician> eventMusicians) throws Exception {
		System.out.println(jsonString);

		if (!(jsonString.equals("<?xml"))) {
			JSONArray jsonArr = new JSONArray(jsonString);
			JSONObject jsonObj = null;
			int size = jsonArr.length();

			for (int i = 0; i < size; i++) {
				jsonObj = jsonArr.getJSONObject(i);

				String venue = jsonObj.getString("venue");
				String city = jsonObj.getString("city");
				String address = jsonObj.getString("address");
				Double lat = (double) 0;
				try {
					lat = jsonObj.getDouble("lat");
				} catch (Exception e) {
					System.out
							.println("This string cannot be converted to double");
				}
				Double lng = (double) 0;
				try {
					lng = jsonObj.getDouble("lng");
				} catch (Exception e) {
					System.out
							.println("This string cannot be converted to double");
				}
				Long eventId = (long) 0;
				try{
					eventId = jsonObj.getLong("EventId");
				}
				catch(Exception e){
					System.out
					.println("This string cannot be converted to long");
				}
				String name = jsonObj.getString("name");
				String desc = jsonObj.getString("desc");
				Date start = ConvertToTimestamp(jsonObj.getString("start"));
				Date lastEdited = ConvertToTimestamp((jsonObj
						.getString("lastEdited")));

				if (city.equals("")) {
					city = "-";
				}
				if (address.equals("")) {
					address = "-";
				}
				if (desc.equals("")) {
					desc = "-";
				}
				if (lastEdited == null) {
					lastEdited = ConvertToTimestamp(Calendar.getInstance(
							TimeZone.getDefault()).toString());
				}

				boolean eventExists = false;

				Event event = null;

				if (!(lat == null || lng == null)) {

					if (!(eventId == 0 || name.equals("") || start == null)) {
						System.out.println("event");

						event = new Event(eventId, name, desc, start,
								lastEdited);

						// provjera i dodavanje lokacije
						/*
						 * List<Location> loc = new Select().all()
						 * .from(Location.class).where("lat = ?", lat)
						 * .and("lng = ?", lng).execute();
						 * 
						 * if (loc.size() == 1) {
						 * event.setIdLocation(loc.get(0)); } events.add(event);
						 * eventExists = true;
						 */
					}
					System.out.println("location");

					Location location = new Location(venue, city, address, lat,
							lng);
					

					locations.add(location);
					System.out.println("addlocation");

					//event.setIdLocation(location);
					System.out.println("setlocation");
					System.out.println(event.getEventId());
					System.out.println(event.getName());
					System.out.println(event.getDescription());
					System.out.println(event.getEventTime());
					System.out.println(event.getLastUpdate());
					System.out.println(event.getIdLocation());
					System.out.println(event.getIdLocation().getAddress());
					System.out.println(event.getIdLocation().getCity());
					System.out.println(event.getIdLocation().getLat());
					System.out.println(event.getIdLocation().getLng());
					System.out.println(event.getIdLocation().getName());
					events.add(event);
					eventExists = true;

				}

				// ArrayList<String> musiciansarray1 = new ArrayList<String>();
				String musiciansString = jsonObj.getString("artists");
				JSONArray musicianArray = new JSONArray(musiciansString);
				System.out.println("musician");

				for (int j = 0; j < musicianArray.length(); j++) {
					String musician = musicianArray.getString(j);
					// musiciansarray1.add(musician);
					if (!(musician.equals(""))) {
						Musician m = new Musician(musician, "-");
						musicians.add(m);
						EventMusician em = new EventMusician(event, m);
						eventMusicians.add(em);
					}
				}

				System.out.println("genre");

				String genresString = jsonObj.getString("genre");
				JSONArray genreArray = new JSONArray(genresString);

				for (int j = 0; j < genreArray.length(); j++) {
					String genre = genreArray.getString(j);
					// musiciansarray1.add(musician);
					if (!(genre.equals(""))) {
						Genre g = new Genre(genre, "-");
						genres.add(g);
						EventGenre eg = new EventGenre(event, g);
						eventGenres.add(eg);
					}
				}

			}

		}

		System.out.println(events.size());
		System.out.println(locations.size());
		System.out.println(musicians.size());
		System.out.println(genres.size());
		System.out.println(eventGenres.size());
		System.out.println(eventMusicians.size());

	}

	/**
	 * Method converts date from string to Date by using a Calendar class.
	 * 
	 * @param jsonDate
	 *            String containing a date formated as yyyy-MM-dd
	 * @return Date object initialized to received date.
	 * @throws ParseException
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date ConvertToTimestamp(String jsonDate) {
		SimpleDateFormat dateParser = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
		Date date = null;// Calendar?
		try {
			date = dateParser.parse(jsonDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
