package net.neurolab.musicmap.dl;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.Location;
import android.app.Activity;
import android.provider.MediaStore.Audio.Artists;


public abstract class DataLoader {
	public ArrayList<Event> events;
//	public ArrayList<Location> locations;
//	public ArrayList<Musician> musicians;
//	public ArrayList<Genre> genres;
	//...
		
	OnDataLoadedListener dataLoadedListener;
	
	public void LoadData(Activity activity){
		if(dataLoadedListener == null)
			dataLoadedListener = (OnDataLoadedListener) activity; 
		}
	
	public boolean DataLoaded(){
		if(events == null){
			return false;
		}
		else{
			dataLoadedListener.OnDataLoaded(events);
			return true;
		}
	}
	
	public interface OnDataLoadedListener{
		public void OnDataLoaded(ArrayList<Event> events);
	}
	
}

