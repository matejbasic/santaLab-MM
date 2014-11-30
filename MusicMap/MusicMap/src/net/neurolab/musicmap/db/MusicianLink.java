package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "MusicianLink")
public class MusicianLink  extends Model{

	@Column (name = "link")
	private String link;
	
	@Column (name = "idMusician")
	private long idMusician;
	
	@Column (name = "musician")
	private Musician musician;

	public MusicianLink() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Musician getMusician() {
		return musician;
	}

	public void setMusician(Musician musician) {
		this.musician = musician;
	}

	public String getLink() {
		return link;
	}

	public long getIdMusician() {
		return idMusician;
	}
	
	
}