package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "FavoriteEvent")
public class FavoriteEvent extends Model{

	@Column (name = "idUser")
	private User idUser;
	
	@Column (name = "idEvent")
	private Event idEvent;

	public FavoriteEvent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FavoriteEvent(User idUser, Event idEvent) {
		super();
		this.idUser = idUser;
		this.idEvent = idEvent;
	}

	public User getIdUser() {
		return idUser;
	}

	public void setIdUser(User idUser) {
		this.idUser = idUser;
	}

	public void setIdEvent(Event idEvent) {
		this.idEvent = idEvent;
	}

	public Event getIdEvent() {
		return idEvent;
	}
	
	
}
