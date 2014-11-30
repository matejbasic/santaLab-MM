package net.neurolab.musicmap.db;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table (name = "User")
public class User extends Model{
	
	@Column (name = "userId")
	private long userId;
	
	@Column (name = "firstLastName")
	private String firstLastName;
	
	@Column (name = "facebookId")
	private String facebookId;
	
	@Column (name = "mmApiKey")
	private String mmApiKey;
	
	@Column (name = "password")
	private String password;

	public User() {
		super();
		
	}

	public User(long userId, String firstLastName, String facebookId, String mmApiKey, String password) {
		super();
		this.userId = userId;
		this.firstLastName = firstLastName;
		this.facebookId = facebookId;
		this.mmApiKey = mmApiKey;
		this.password = password;
	}
	
	public List<User> getAll() {
		return new Select().from(User.class).execute();
	}
	
	/**
	 * todo: delete before it spreads :D
	 * @author Deng
	 */
	public void DeleteAll() {
		List<User> users = this.getAll();
		for (User user : users) {
			user.delete();
		}
	}
	
	public long getUserId() {
		return userId;
	}

	
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFirstLastName() {
		return firstLastName;
	}

	public void setFirstLastName(String firstLastName) {
		this.firstLastName = firstLastName;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getMmApiKey() {
		return mmApiKey;
	}

	public void setMmApiKey(String mmApiKey) {
		this.mmApiKey = mmApiKey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
