package net.neurolab.musicmap.db;


public class EventMusician_2 {

	private long idEvent;

	private String name;

	public long getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(long idEvent) {
		this.idEvent = idEvent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EventMusician_2() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventMusician_2(long idEvent, String name) {
		super();
		this.idEvent = idEvent;
		this.name = name;
	}
	
	
}
