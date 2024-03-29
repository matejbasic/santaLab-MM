package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * 
 * @author Ljiljana
 *
 * PreferredGenre - a class that represents a database table PreferredGenre which contains user and genre id.
 */

@Table (name = "PreferredGenre")
public class PreferredGenre extends Model{

	@Column (name = "idUser")
	private User idUser;
	
	@Column (name = "idGenre")
	private Genre idGenre;

	public PreferredGenre() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PreferredGenre(User idUser, Genre idGenre) {
		super();
		this.idUser = idUser;
		this.idGenre = idGenre;
	}
	
	
	public int getSum() {
		return new Select().from(PreferredGenre.class).count();
	}
	

	public User getIdUser() {
		return idUser;
	}

	public Genre getIdGenre() {
		return idGenre;
	}

	public void setIdUser(User idUser) {
		this.idUser = idUser;
	}

	public void setIdGenre(Genre idGenre) {
		this.idGenre = idGenre;
	}
	
	
	
}
