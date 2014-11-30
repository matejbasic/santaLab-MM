package net.neurolab.musicmap.db;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "Musician")
public class Musician extends Model{

	@Column (name = "musicianId")
	private  long musicianId;
	
	@Column (name = "name")
	private String name;
	
	@Column (name = "biography")
	private String biography;

	public Musician() {
		super();
	}

	public Musician(long musicianId, String name, String biography) {
		super();
		this.musicianId = musicianId;
		this.name = name;
		this.biography = biography;
	}

	public long getMusicianId() {
		return musicianId;
	}

	public String getName() {
		return name;
	}

	public String getBiography() {
		return biography;
	}
	
	
	public List<MusicianLink> links(){
		return getMany(MusicianLink.class, "MusicianLink");
	}
	
}