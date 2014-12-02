package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "EventPrice")
public class EventPrice extends Model {
	
	@Column (name = "idEvent")
	private Event idEvent;
	
	@Column (name = "description")
	private String description;
	
	@Column (name = "price")
	private String price;
	/*
	@Column (name = "event")
	private Event event;
	*/
	
	public EventPrice() {
		super();
	}

		
	public EventPrice(Event idEvent, String description, String price) {
		super();
		this.idEvent = idEvent;
		this.description = description;
		this.price = price;
	}



/*
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}*/

	public Event getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(Event idEvent) {
		this.idEvent = idEvent;
	}


	public String getDescription() {
		return description;
	}

	public String getPrice() {
		return price;
	}

	
}
