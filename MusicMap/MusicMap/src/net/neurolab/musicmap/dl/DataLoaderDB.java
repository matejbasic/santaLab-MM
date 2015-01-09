package net.neurolab.musicmap.dl;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.Location;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;

public class DataLoaderDB extends DataLoader {

	@Override
	public void LoadData(Activity activity, String location) {
		super.LoadData(activity, location);

		List<Event> eventsDb = null;

		boolean databaseQuerySuccessfull = false;

		List<Location> loc = new Select().from(Location.class)
				.where("city LIKE ?", location).execute();
		ArrayList<Location> l = (ArrayList<Location>) loc;
		if (l.size() > 0) {
			try {
				eventsDb = new Select()
						.all()
						.from(Event.class)
						.where("lat=? and lng=?", l.get(0).getLat(),
								l.get(0).getLng()).execute();
				databaseQuerySuccessfull = true;
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		} else {
			try {
				eventsDb = new Select().all().from(Event.class).execute();
				databaseQuerySuccessfull = false;
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		System.out.println(eventsDb.size()); 
		if (databaseQuerySuccessfull == true && eventsDb.size() > 0) {
			// Toast.makeText(activity, "Loading local data",
			// Toast.LENGTH_SHORT).show();
			Log.i("dataLoaderDB", "query successfull");
			events = (ArrayList<Event>) eventsDb;

			DataLoaded();

		}
	}

}