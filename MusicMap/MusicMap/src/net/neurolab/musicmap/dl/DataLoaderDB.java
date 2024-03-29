package net.neurolab.musicmap.dl;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import android.app.Activity;

import com.activeandroid.query.Select;

/**
 * 
 * @author Ljiljana
 *
 *         DataLoaderDB is a class which extends DataLoader and which is used to
 *         retrieve data from the app database.
 */
public class DataLoaderDB extends DataLoader {

	@Override
	public void LoadData(Activity activity, String location) {
		super.LoadData(activity, location);

		List<Event> eventsDb = null;

		boolean databaseQuerySuccessfull = false;

		try {
			eventsDb = new Select()
					.all()
					.from(Event.class)
					.where("lat in (select lat from location where city LIKE ?) and lng in (select lng from location where city LIKE ?)",
							location, location).execute();

			databaseQuerySuccessfull = true;
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (databaseQuerySuccessfull == true && eventsDb.size() > 0) {
			events = (ArrayList<Event>) eventsDb;
			DataLoaded();
		}
	}

}