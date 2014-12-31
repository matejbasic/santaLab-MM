package net.neurolab.musicmap.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.neurolab.musicmap.db.Event;

public class Group {
	private Date date;
	//private final List<String> children;
	private final ArrayList<Event> children;
	
	public Group(Date date) {
		this.date = date;
		//children = new ArrayList<String>();
		children = new ArrayList<Event>();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	/*
	public List<String> getChildren() {
		return children;
	}
	*/
	
	public ArrayList<Event> getChildren() {
		return children;
	}

}
