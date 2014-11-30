package net.neurolab.musicmap.db;

import java.sql.Timestamp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name = "Info")
public class Info extends Model {
	
	@Column (name = "lastModified")
	private Timestamp lastModified;
}