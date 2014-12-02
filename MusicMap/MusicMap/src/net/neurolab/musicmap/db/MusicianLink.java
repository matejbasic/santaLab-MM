package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "MusicianLink")
public class MusicianLink  extends Model{

	@Column (name = "idMusician")
	private Musician idMusician;
	
	@Column (name = "link")
	private String link;
	/*
	@Column (name = "idMusician")
	private long idMusician;
	*/	

	public MusicianLink() {
		super();
	}
	public MusicianLink(Musician idMusician, String link) {
		super();
		this.idMusician = idMusician;
		this.link = link;
	}
	public Musician getIdMusician() {
		return idMusician;
	}
	public void setIdMusician(Musician idMusician) {
		this.idMusician = idMusician;
	}
	public String getLink() {
		return link;
	}


	
	
}
