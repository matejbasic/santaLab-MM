package net.neurolab.musicmap.fragments;

import java.util.ArrayList;
import java.util.Date;

import net.neurolab.musicmap.db.Event;

/**
 * Represents group item in list of events
 * (see EventsExpandableAdapter)
 * 
 * @author Basic
 *
 */
public class Group {
	private Date date;
	private final ArrayList<Event> children;
	
	public Group(Date date) {
		this.date = date;
		children = new ArrayList<Event>();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public ArrayList<Event> getChildren() {
		return children;
	}

}
