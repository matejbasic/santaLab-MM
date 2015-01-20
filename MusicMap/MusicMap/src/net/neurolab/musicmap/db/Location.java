package net.neurolab.musicmap.db;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table (name = "Location")
public class Location extends Model {
	/*
	@Column (name = "locationId", unique = true, index = true, notNull = true)
	private long locationId;*/
	
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
	

	public Location(String name, String city, String address, double lat, double lng) {
		super();		
		this.name = name;
		this.city = city;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
	}


	public int getSum() {
		return new Select().from(Location.class).count();
	}

	public Location getLocation(String name) {
		
		StringBuilder temp = new StringBuilder(name);
		temp.setCharAt(0, Character.toUpperCase(temp.charAt(0)));
		name = temp.toString();
		return new Select().from(Location.class).where("name = ?", name).executeSingle();
	}
	
	public Location getLocationByLatLng(long lat, long lng, String address){
		return new Select().from(Location.class).where("lat = ?", lat).and("lng = ?", lng).and("address = ?", address).executeSingle();
				
	}
	
	public List<Location> getAll() {
		return new Select().from(Location.class).execute();
	}
	
/*
	public long getLocationId() {
		return locationId;
	}*/
	
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
