package net.neurolab.musicmap.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Group {
	private Date date;
	private final List<String> children;
	
	public Group(Date date) {
		this.date = date;
		children = new ArrayList<String>();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getChildren() {
		return children;
	}

}
