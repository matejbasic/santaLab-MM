package net.neurolab.musicmap.db;

import java.sql.Timestamp;
import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "Event")
public class Event extends Model {
	
	@Column (name = "eventId", unique = true, index = true, notNull = true)
	private long eventId;
	
	@Column (name = "description")
	private String description;
	
	@Column (name = "eventTime")
	private Timestamp eventTime;
	
	@Column (name = "lastUpdate")
	private Timestamp lastUpdate;
	
	@Column (name = "idLocation", index = true, notNull = true)
	private long idLocation;
	
	@Column (name = "location")
	private Location location;
	
	public Event() {
		super();
	}
	
	

	public Event(long eventId, String description, Timestamp eventTime,	Timestamp lastUpdate, long idLocation) {
		super();
		this.eventId = eventId;
		this.description = description;
		this.eventTime = eventTime;
		this.lastUpdate = lastUpdate;
		this.idLocation = idLocation;
	}



	public long getEventId() {
		return eventId;
	}

	public String getDescription() {
		return description;
	}

	public Timestamp getEventTime() {
		return eventTime;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public long getIdLocation() {
		return idLocation;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
		
	
	public List<Location> locations(){
		return getMany(Location.class, "Location");
	}
	
	public List<EventPrice> prices(){
		return getMany(EventPrice.class, "EventPrice");
	}
	
	public List<Comment> comments(){
		return getMany(Comment.class, "Comment");
	}
	
}