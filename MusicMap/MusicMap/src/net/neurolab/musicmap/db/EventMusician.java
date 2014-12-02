package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "EventMusician")
public class EventMusician extends Model {

	@Column (name = "idEvent")
	private Event idEvent;
	
	@Column (name = "idMusician")
	private Musician idMusician;

	public EventMusician() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventMusician(Event idEvent, Musician idMusician) {
		super();
		this.idEvent = idEvent;
		this.idMusician = idMusician;
	}

	public void setIdEvent(Event idEvent) {
		this.idEvent = idEvent;
	}

	public void setIdMusician(Musician idMusician) {
		this.idMusician = idMusician;
	}

	public Event getIdEvent() {
		return idEvent;
	}

	public Musician getIdMusician() {
		return idMusician;
	}
	
	
}
