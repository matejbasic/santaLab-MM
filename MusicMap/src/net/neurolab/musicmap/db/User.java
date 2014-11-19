package net.neurolab.musicmap.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "User")
public class User extends Model{
	
	@Column (name = "userId")
	private long userId;
	
	@Column (name = "firstLastName")
	private String firstLastName;
	
	@Column (name = "email")
	private String email;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(long userId, String firstLastName, String email) {
		super();
		this.userId = userId;
		this.firstLastName = firstLastName;
		this.email = email;
	}

	public long getUserId() {
		return userId;
	}

	public String getFirstLastName() {
		return firstLastName;
	}

	public String getEmail() {
		return email;
	}
	
	
}
