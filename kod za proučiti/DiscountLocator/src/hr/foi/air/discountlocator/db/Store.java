package hr.foi.air.discountlocator.db;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Entity class representing a store item. 
 * Each store can have one or more discounts and is connected to one or more categories.
 * @see Discount
 * @see Category
 * @see StoreCategory
 */

@Table (name = "Store")
public class Store extends Model{
	@Column (name = "remoteId")
	private long remoteId;
	@Column (name = "name", index = true)
	private String name;
	@Column (name = "description")
	private String description;
	@Column (name = "imgUrl")
	private String imgUrl;
	@Column (name = "longitude")
	private long longitude;
	@Column (name = "latitude")
	private long latitude;
	
	public Store() {
		super();
	}
	
	public Store(long remoteId, String name, String description, String imgUrl,
			long longitude, long latitude) {
		super();
		this.remoteId = remoteId;
		this.name = name;
		this.description = description;
		this.imgUrl = imgUrl;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public List<Discount> discounts(){
		return getMany(Discount.class, "Store");
	}

	public long getRemoteId() {
		return remoteId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public long getLongitude() {
		return longitude;
	}

	public long getLatitude() {
		return latitude;
	}
}
