package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "PreferredLocation")
public class PreferredLocation extends Model{
	
	@Column (name = "idUser")
	private long idUser;
	
	@Column (name = "idLocation")
	private long idLocation;

	public PreferredLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PreferredLocation(long idUser, long idLocation) {
		super();
		this.idUser = idUser;
		this.idLocation = idLocation;
	}

	public long getIdUser() {
		return idUser;
	}

	public long getIdLocation() {
		return idLocation;
	}
	
	
}
