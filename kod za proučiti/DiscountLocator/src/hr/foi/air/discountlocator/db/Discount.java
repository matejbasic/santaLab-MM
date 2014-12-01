package hr.foi.air.discountlocator.db;

import java.util.Date;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Entity class representing a discount item. 
 * Each discount is connected to one and only one store 
 * and can have zero, one or more discounted articles.
 * @see Store
 * @see DiscountArticle
 * @see Article
 */
@Table (name = "Discount")
public class Discount extends Model{
	@Column (name = "remoteId")
	private long remoteId;
	@Column (name = "name", index = true)
	private String name;
	@Column (name = "description")
	private String description;
	//----
	//used  from server - this is remoteStoreId
	@Column (name = "storeId")
	private long storeId;
	//used for local database
	@Column (name = "store")
	private Store store;
	//----
	@Column (name = "startDate")
	private Date startDate;
	@Column (name = "endDate")
	private Date endDate;
	@Column (name = "discount")
	private int discount;
	
	public Discount() {
		super();
	}

	public Discount(long remoteId, String name, String description, long storeId, Date startDate, Date endDate, int discount) {
		super();
		this.remoteId = remoteId;
		this.name = name;
		this.description = description;
		this.storeId = storeId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.discount = discount;
	}
	public Store getStore(){
		return store;
	}
	
	public void setStore(Store store){
		this.store = store;
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

	public long getStoreId() {
		return storeId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getDiscount() {
		return discount;
	}
}
