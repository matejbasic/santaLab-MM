package net.neurolab.musicmap.db;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


/**
 * 
 * @author Ljiljana
 * EventMusician - a class representing database table EventMusician which contains event and musician id.
 */
@Table (name = "EventMusician")
public class EventMusician extends Model {

	@Column (name = "idEvent")
	private Event idEvent;
	
	@Column (name = "idMusician")
	private Musician idMusician;

	public EventMusician() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public List<EventMusician> getAll() {
		return new Select().from(EventMusician.class).execute();
	}
	public List<Musician> getMusiciansByEvent(Event event) {
		Log.i("getMusiciansByEvent", "start");
		//List<Musician> temp =  new Select("idMusician").from(EventMusician.class).where("idEvent = ?", event.getEventId()).execute();
		//return temp;
		
		//is there a better way to do this?
		List<EventMusician> ems = new EventMusician().getAll();
		
		ArrayList<Musician> temp = new ArrayList<Musician>();
		for (EventMusician em : ems) {
			if(em.getIdEvent().getId() == event.getId() && !temp.contains(em.getIdMusician())) {
				temp.add(em.getIdMusician());			
			}
		}
		
		return temp; 
	}

	public EventMusician(Event idEvent, Musician idMusician) {
		super();
		this.idEvent = idEvent;
		this.idMusician = idMusician;
	}

	public void setIdEvent(Event idEvent) {
		this.idEvent = idEvent;
	}

	public void setIdMusician(Musician idMusician) {
		this.idMusician = idMusician;
	}

	public Event getIdEvent() {
		return idEvent;
	}

	public Musician getIdMusician() {
		return idMusician;
	}
	
	
}
