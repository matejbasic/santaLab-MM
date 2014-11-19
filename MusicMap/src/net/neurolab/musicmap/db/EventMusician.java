package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "EventMusician")
public class EventMusician extends Model {

	@Column (name = "idEvent")
	private long idEvent;
	
	@Column (name = "idMusician")
	private long idMusician;

	public EventMusician() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventMusician(long idEvent, long idMusician) {
		super();
		this.idEvent = idEvent;
		this.idMusician = idMusician;
	}

	public long getIdEvent() {
		return idEvent;
	}

	public long getIdMusician() {
		return idMusician;
	}
	
	
}
