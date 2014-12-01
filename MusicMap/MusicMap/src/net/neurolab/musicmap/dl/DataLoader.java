package net.neurolab.musicmap.dl;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.Location;
import android.app.Activity;
import android.provider.MediaStore.Audio.Artists;


public abstract class DataLoader {
	public ArrayList<Event> events;
	public ArrayList<Location> locations;
	//public ArrayList<Artists> artists;
	//public ArrayList<Genres> genres;
	//...
	
	//!!! razmisliti o Dohva�anju povezanih podataka tipa lokacije, glazbenici, �anrovi, cijene i sl
	
	OnDataLoadedListener dataLoadedListener;
	
	public void LoadData(Activity activity){
		if(dataLoadedListener == null)
			dataLoadedListener = (OnDataLoadedListener) activity; //dodjela tipa activityu, budu�i da main activity implementira ondataloaded listener
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
	
	//Main activity implementira OnDataLoadedListener iz DataLoader.java(su�elje) - �to zna�i da, kad su stores i discounts napunjeni, u jednom dataloaderu se poziva 
	//funkcija DataLoaded koja poziva dataLoadedListener.OnDataLoaded(stores, discounts); i javlja mu da su podaci u�itani, nakon �ega main activity zna da
	//sad mo�e raditi sa tim podacima (prikazati ih)
	
	public interface OnDataLoadedListener{
		public void OnDataLoaded(ArrayList<Event> events);
	}
	
}

