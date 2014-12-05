package net.neurolab.musicmap.dl;

import java.util.List;

import net.neurolab.musicmap.db.Event;
import android.app.Activity;

import com.activeandroid.query.Select;

public class DataLoaderSearch extends DataLoader {

	public void setContext(Activity activity) {
		super.LoadData(activity);
	}

	public void searchData(String searchTerm) {
		// add % for LIKE clause
		searchTerm = "%" + searchTerm + "%";
		List<Event> eventsFromDb = null;

		// check database
		boolean databaseQuerySuccessfull = false;
		try {
			eventsFromDb = new Select().all().from(Event.class)
					.where("name LIKE ?", searchTerm)
					.or("eventTime LIKE ?", searchTerm).execute();
			databaseQuerySuccessfull = true;
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		/*
		 * if(databaseQuerySuccessfull == true && eventsFromDb.size() > 0 ){ }
		 */
		DataLoaded();
	}

}