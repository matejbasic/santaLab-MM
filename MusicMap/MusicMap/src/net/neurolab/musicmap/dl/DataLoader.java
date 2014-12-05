package net.neurolab.musicmap.dl;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;

import android.app.Activity;


public abstract class DataLoader {
	public ArrayList<Event> events;
	// public ArrayList<Location> locations;
	// public ArrayList<Musician> musicians;
	// public ArrayList<Genre> genres;
	// ...

	OnDataLoadedListener dataLoadedListener;

	public void LoadData(Activity activity, String location) {
		if (dataLoadedListener == null)
			dataLoadedListener = (OnDataLoadedListener) activity;
	}

	public boolean DataLoaded() {
		if (events == null) {
			System.out.println("(events == null)");
			return false;
		} else {			
			/*
			for (int i = 0; i < events.size(); i++) {
				System.out.println(events.get(i).getName());
			}
			*/
			dataLoadedListener.OnDataLoaded(events);
			System.out.println("kraj");
			return true;
		}
	}

	public interface OnDataLoadedListener {
		public void OnDataLoaded(ArrayList<Event> events);
	}

}
