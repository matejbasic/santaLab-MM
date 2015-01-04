package net.neurolab.musicmap;

import java.util.ArrayList;

public class EventGroups {
	private static EventGroups eventGroups;
	public ArrayList<Object[]> listGroups = new ArrayList<Object[]>();
	
	private EventGroups() {

	}
	
	public static EventGroups getInstance() {
		if (eventGroups == null) {
			eventGroups = new EventGroups();
		}
		return eventGroups;
	}

	
}
