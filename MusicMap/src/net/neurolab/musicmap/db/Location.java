package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "Location")
public class Location extends Model {
	
	@Column (name = "locationId", unique = true, index = true, notNull = true)
	private String locationId;
	
	@Column (name = "name")
	private String name;
	
	@Column (name = "address")
	private String address;
	
	@Column (name = "lat")
	private double lat;
	
	@Column (name = "lng")
	private double lng;
	
	public Location() {
		super();
	}
	
	
	
	public Location(String locationId, String name, String address, double lat, double lng) {
		super();
		this.locationId = locationId;
		this.name = name;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
	}



	public String getLocationId() {
		return locationId;
	}
	
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	
	
	
	
}
