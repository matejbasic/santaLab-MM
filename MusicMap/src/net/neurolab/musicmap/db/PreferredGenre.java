package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "PreferredGenre")
public class PreferredGenre extends Model{

	@Column (name = "idUser")
	private long idUser;
	
	@Column (name = "idGenre")
	private long idGenre;

	public PreferredGenre() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PreferredGenre(long idUser, long idGenre) {
		super();
		this.idUser = idUser;
		this.idGenre = idGenre;
	}

	public long getIdUser() {
		return idUser;
	}

	public long getIdGenre() {
		return idGenre;
	}
	
	
	
}
