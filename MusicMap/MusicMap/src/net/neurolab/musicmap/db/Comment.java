package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "Comment")
public class Comment extends Model {
	
	@Column (name = "commentId")
	private long commentId;
	/*
	@Column (name = "idEvent")
	private long idEvent;
	*/
	
	@Column (name = "idEvent")
	private Event idEvent;
	
	
	@Column (name = "event")
	private Event event;
	
	public Comment(){
		super();
	}

	public Comment(long commentId, Event idEvent) {
		super();
		this.commentId = commentId;
		this.idEvent = idEvent;
	}

	public Event getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(Event idEvent) {
		this.idEvent = idEvent;
	}

	public long getCommentId() {
		return commentId;
	}

	public Event getEvent() {
		return event;
	}
	
	
}
