package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "EventGenre")
public class EventGenre extends Model {

	@Column (name = "idEvent")
	public long idEvent;
	
	@Column (name = "idGenre")
	public long idGenre;
	
	public EventGenre(){
		super();
	}

	public EventGenre(long idEvent, long idGenre) {
		super();
		this.idEvent = idEvent;
		this.idGenre = idGenre;
	}

	public long getIdEvent() {
		return idEvent;
	}

	public long getIdGenre() {
		return idGenre;
	}
	
	
	
}