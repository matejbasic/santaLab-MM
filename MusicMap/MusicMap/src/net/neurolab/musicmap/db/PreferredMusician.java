package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "PreferredMusician")
public class PreferredMusician extends Model{
	
	@Column (name = "idUser")
	private long idUser;
	
	@Column (name = "idMusician")
	private long idMusician;

	public PreferredMusician() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PreferredMusician(long idUser, long idMusician) {
		super();
		this.idUser = idUser;
		this.idMusician = idMusician;
	}

	public long getIdUser() {
		return idUser;
	}

	public long getIdMusician() {
		return idMusician;
	}
	
	
}