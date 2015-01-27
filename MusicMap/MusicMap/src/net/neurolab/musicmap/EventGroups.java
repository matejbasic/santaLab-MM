package net.neurolab.musicmap;

import java.util.ArrayList;
/**
 * Singleton class used as an container for event details groups.
 * @author Basic
 *
 */
public class EventGroups {
	private static EventGroups eventGroups;
	public ArrayList<Object[]> listGroups = new ArrayList<Object[]>();
	
	private EventGroups() {}
	
	public void reset() {
		listGroups = new ArrayList<Object[]>();
	}
	public static EventGroups getInstance() {
		if (eventGroups == null) {
			eventGroups = new EventGroups();
		}
		return eventGroups;
	}

	
}
