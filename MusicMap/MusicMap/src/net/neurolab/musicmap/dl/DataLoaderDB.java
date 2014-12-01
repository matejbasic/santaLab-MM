package net.neurolab.musicmap.dl;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.Location;
import android.app.Activity;
import android.widget.Toast;

import com.activeandroid.query.Select;


public class DataLoaderDB extends DataLoader{
	
	@Override
	public void LoadData(Activity activity) {//overriden DataLoader
		super.LoadData(activity);
		
		List<Event> eventsDB = null ;
		//List<Location> locationsDB = null;
				
		// keep in mind that we need two calls instead of one
		// we actually never binded stores and discounts in database
		// we are using local infrastructure to display them (DiscountExpandableAdapter)
		//********************************************************************************
		// while playing with database, make sure you change version in the manifest file
		//********************************************************************************
		//:D :D i guess
		
		boolean databaseQuerySuccessfull = false;
		
		try{
			eventsDB = new Select().all().from(Event.class).execute();
			//locationsDB = new Select().all().from(Location.class).execute();
			
			databaseQuerySuccessfull = true;
		}catch (NullPointerException e){
			e.printStackTrace();
		}
		
		if(databaseQuerySuccessfull == true && events.size() > 0 ){
			Toast.makeText(activity, "Loading local data", Toast.LENGTH_SHORT).show();
			
			events = (ArrayList<Event>) eventsDB;
			//locations = (ArrayList<Location>) locationsDB;
	
			DataLoaded();					
			/*
			 * DataLoader method
			 * 	public boolean DataLoaded(){
					if(stores == null || discounts == null){
						return false;
					}
					else{
						dataLoadedListener.OnDataLoaded(stores, discounts);
						return true;
					}
				}
			 */
		}
	}

}