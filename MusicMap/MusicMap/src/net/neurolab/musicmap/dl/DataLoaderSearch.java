package net.neurolab.musicmap.dl;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import android.app.Activity;
import android.util.Log;

import com.activeandroid.query.Select;

public class DataLoaderSearch extends DataLoader {

	public void setContext(Activity activity, String location) {
		super.LoadData(activity, location);
	}

	public void searchData(String searchTerm) {
		searchTerm = "%" + searchTerm + "%";
		List<Event> eventsDb = null;

		boolean databaseQuerySuccessfull = false;
		try {
			eventsDb = new Select().all().from(Event.class)
					.where("name LIKE ?", searchTerm)
					.or("eventTime LIKE ?", searchTerm).execute();
			databaseQuerySuccessfull = true;			
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (databaseQuerySuccessfull == true && eventsDb.size() > 0) {
			events = (ArrayList<Event>) eventsDb;			
		}
		
		System.out.println(eventsDb.size());
		
		Log.i("DataLoaderSearch-", searchTerm);
		DataLoaded();
	}

}