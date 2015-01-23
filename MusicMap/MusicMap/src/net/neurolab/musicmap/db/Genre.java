package net.neurolab.musicmap.db;

/**
 * 
 * Genre - a class representing database table Genre which contains informations about specific genre.
 */

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table (name = "Genre")
public class Genre extends Model {
/*
	@Column (name = "genreId")
	private long genreId;
	*/
	@Column (name = "name")
	private String name;
	
	@Column (name = "subgenres")
	private String subgenres;
	
	
	public Genre() {
		super();
	}


	public Genre(/*long genreId,*/ String name, String subgenres) {
		super();
		//this.genreId = genreId;
		this.name = name;
		this.subgenres = subgenres;
	}

	public int getSum() {
		return new Select().from(Genre.class).count();
	}
	public List<Genre> getAll() {
		return new Select().from(Genre.class).execute();
	}
	
	public void deleteAll() {
		List<Genre> genres = this.getAll();
		
		for (Genre genre : genres) {
			genre.delete();
		}
	}
	
/*
	public long getGenreId() {
		return genreId;
	}
*/

	public String getName() {
		return name;
	}


	public String getSubgenres() {
		return subgenres;
	}

	
}
