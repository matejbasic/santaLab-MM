package net.neurolab.musicmap;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;

public interface OnDataChangedListener {
	void OnDataChanged(ArrayList<Event> events);
}
