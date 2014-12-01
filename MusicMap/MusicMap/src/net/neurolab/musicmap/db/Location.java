package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "Location")
public class Location extends Model {
	
	@Column (name = "locationId", unique = true, index = true, notNull = true)
	private long locationId;
	
	@Column (name = "name")
	private String name;
	
	@Column (name = "city")
	private String city;
	
	@Column (name = "address")
	private String address;
	
	@Column (name = "lat")
	private double lat;
	
	@Column (name = "lng")
	private double lng;
	
	public Location() {
		super();
	}
	

	public Location(long locationId, String name, String city, String address, double lat, double lng) {
		super();
		this.locationId = locationId;
		this.name = name;
		this.city = city;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
	}




	public long getLocationId() {
		return locationId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCity() {
		return city;
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
