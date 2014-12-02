package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "PreferredMusician")
public class PreferredMusician extends Model{
	
	@Column (name = "idUser")
	private User idUser;
	
	@Column (name = "idMusician")
	private Musician idMusician;

	public PreferredMusician() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PreferredMusician(User idUser, Musician idMusician) {
		super();
		this.idUser = idUser;
		this.idMusician = idMusician;
	}

	public User getIdUser() {
		return idUser;
	}

	public Musician getIdMusician() {
		return idMusician;
	}

	public void setIdUser(User idUser) {
		this.idUser = idUser;
	}

	public void setIdMusician(Musician idMusician) {
		this.idMusician = idMusician;
	}
	
	
}
