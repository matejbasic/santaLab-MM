package net.neurolab.musicmap.db;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table (name = "PreferredLocation")

/** 
 * 
 * @author Ljiljana
 *
 * PreferredLocation - a class that represents a database table PreferredLocation which contains user and location id.
 */

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
	
	public Location getSingleLocation() {
		PreferredLocation prefLocation = new Select().from(PreferredLocation.class).executeSingle();
		return prefLocation.getIdLocation();
	}
	
	public Boolean checkPreferredLocation(Location location) {
		List<PreferredLocation> temp = new Select().from(PreferredLocation.class).where("idLocation = ?", location).execute();
		
		if (temp != null) {
			return true;
		}
		else {
			return false;
		}
	}
	public List<PreferredLocation> getAll() {
		return new Select().from(PreferredLocation.class).execute();
	}
	
	public int getSum() {
		return new Select().from(PreferredLocation.class).count();
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
