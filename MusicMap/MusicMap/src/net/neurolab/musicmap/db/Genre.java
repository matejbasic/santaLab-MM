package net.neurolab.musicmap.db;



import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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
