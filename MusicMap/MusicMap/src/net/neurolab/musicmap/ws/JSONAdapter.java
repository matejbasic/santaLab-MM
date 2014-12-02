package net.neurolab.musicmap.ws;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	/*
	public static ArrayList<Event> getEvents(String jsonString)
			throws Exception {
		ArrayList<Event> events = new ArrayList<Event>();

		JSONArray jsonArr = new JSONArray(jsonString);
		int size = jsonArr.length();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);

			Event event = new Event(jsonObj.getLong("EventId"),// eventId
					jsonObj.getString("name"),// name
					jsonObj.getString("desc"),// description - TODO //
												// PARSIRATI!!!!!!!!!!!!!!!!!!!!!!
					ConvertToTimestamp(jsonObj.getString("start")),// eventTime
																	// TODO
																	// ConvertToTimestamp
					ConvertToTimestamp(jsonObj.getString("lastEdited"))// lastUpdate

			// jsonObj.getLong(null)// locationId (DataLoaderWS)
			);
			events.add(event);
		}

		return events;
	}
	public static ArrayList<Location> getLocations(String jsonString)
			throws Exception {
		ArrayList<Location> locations = new ArrayList<Location>();

		JSONArray jsonArr = new JSONArray(jsonString);
		int size = jsonArr.length();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);

			Location location = new Location(
					jsonObj.getString("venue"),// name
					jsonObj.getString("city"),// name
					jsonObj.getString("address"),// address - TODO
													// PARSIRATI!!!!!!!!!!!!!!!!!!!!!!
													// :D
					jsonObj.getDouble("lat"),// latitude (DataLoaderWS)
					jsonObj.getDouble("lng")// latitude (DataLoaderWS)

			
			);
			locations.add(location);
		}

		return locations;
	}

	public static ArrayList<String> getTheDescription(String jsonString)
			throws Exception {
		ArrayList<String> theRestOfTheDataBulk = new ArrayList<String>();

		JSONArray jsonArr = new JSONArray(jsonString);
		int size = jsonArr.length();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);

			String theRestOfTheData = jsonObj.getString("desc"); // PARSIRATI U
																	// DLWS

			theRestOfTheDataBulk.add(theRestOfTheData);
		}

		return theRestOfTheDataBulk;
	}

	// parsirano
	public static ArrayList<String> getTheArtists(String jsonString)
			throws Exception {
		ArrayList<String> theRestOfTheDataBulk = new ArrayList<String>();

		JSONArray jsonArr = new JSONArray(jsonString);
		int size = jsonArr.length();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			String theRestOfTheData = jsonObj.getString("artists");
			JSONArray theRestOfTheDataArray = new JSONArray(theRestOfTheData);

			for (int j = 0; j < theRestOfTheDataArray.length(); j++) {
				String artist = theRestOfTheDataArray.getString(j);
				theRestOfTheDataBulk.add(artist);
			}
		}

		return theRestOfTheDataBulk;
	}
*/
	
	public static void getEvents(String jsonString, ArrayList<Event> events, ArrayList<Location> locations/*, ArrayList<Musician> musicians*/) throws Exception
			 {
		//ArrayList<Event> eventsArr = new ArrayList<Event>();
	//	ArrayList<Location> locationsArr = new ArrayList<Location>();
		System.out.println("blabla");
		
		JSONArray jsonArr = new JSONArray(jsonString);
		int size = jsonArr.length();
		
		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			
			System.out.println(jsonObj.getString("venue"));
			System.out.println(jsonObj.getString("city"));
			System.out.println(jsonObj.getString("address"));
			System.out.println(jsonObj.getString("lat"));
			System.out.println(jsonObj.getString("lng"));
			
			Location location = new Location(
					jsonObj.getString("venue"),
					jsonObj.getString("city"),
					jsonObj.getString("address"),
					jsonObj.getDouble("lat"),
					jsonObj.getDouble("lng")
			);
			
			System.out.println(location.getName());
			location.save();
			locations.add(location);
/*
			Event event = new Event(
					jsonObj.getLong("EventId"),// eventId			
					jsonObj.getString("name"),// name
					jsonObj.getString("desc"),// description - TODO // PARSIRATI!!!!!!!!!!!					
					ConvertToTimestamp(jsonObj.getString("start")),
					ConvertToTimestamp(jsonObj.getString("lastEdited"))// lastUpdate					
			// jsonObj.getLong(null)// locationId (DataLoaderWS)
			);		
			events.add(event);
			System.out.println(events.size());*/
			System.out.println(i);
			
		}
	
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
