package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "PreferredLocation")
public class PreferredLocation extends Model{
	
	@Column (name = "idUser")
	private User idUser;
	
	@Column (name = "idLocation")
	private Location idLocation;

	public PreferredLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PreferredLocation(User idUser, Location idLocation) {
		super();
		this.idUser = idUser;
		this.idLocation = idLocation;
	}

	public User getIdUser() {
		return idUser;
	}

	public Location getIdLocation() {
		return idLocation;
	}

	public void setIdUser(User idUser) {
		this.idUser = idUser;
	}

	public void setIdLocation(Location idLocation) {
		this.idLocation = idLocation;
	}
	
	
}
