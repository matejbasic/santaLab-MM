package net.neurolab.musicmap;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;

/**
 * 
 * @author Ljiljana
 *
 *OnDataChangedListener interface is used to notify fragments that (asyncTask) data has arrived and can be shown in list or map
 */

public interface OnDataChangedListener {
	void OnDataChanged(ArrayList<Event> events);
}
