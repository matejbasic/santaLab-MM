package net.neurolab.musicmap.dl;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import android.app.Activity;
import android.widget.Toast;

import com.activeandroid.query.Select;

public class DataLoaderDB extends DataLoader {

	@Override
	public void LoadData(Activity activity) {
		super.LoadData(activity);

		List<Event> eventsDb = null;

		boolean databaseQuerySuccessfull = false;

		try {
			eventsDb = new Select().all().from(Event.class).execute();
			databaseQuerySuccessfull = true;
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (databaseQuerySuccessfull == true && eventsDb.size() > 0) {
			Toast.makeText(activity, "Loading local data", Toast.LENGTH_SHORT)
					.show();

			events = (ArrayList<Event>) eventsDb;

			DataLoaded();

		}
	}

}