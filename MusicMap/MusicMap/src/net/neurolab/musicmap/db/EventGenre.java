package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "EventGenre")
public class EventGenre extends Model {

	@Column (name = "idEvent")
	public Event idEvent;
	
	@Column (name = "idGenre")
	public Genre idGenre;
	
	public EventGenre(){
		super();
	}

	public EventGenre(Event idEvent, Genre idGenre) {
		super();
		this.idEvent = idEvent;
		this.idGenre = idGenre;
	}

	public void setIdEvent(Event idEvent) {
		this.idEvent = idEvent;
	}

	public void setIdGenre(Genre idGenre) {
		this.idGenre = idGenre;
	}

	public Event getIdEvent() {
		return idEvent;
	}

	public Genre getIdGenre() {
		return idGenre;
	}
	
	
	
}
