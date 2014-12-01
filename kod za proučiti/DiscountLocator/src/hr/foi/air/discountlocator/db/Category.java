package hr.foi.air.discountlocator.db;

/**
 * Entity class representing a category item. 
 * Each category can be connected to zero, one or more stores.
 * @see Store
 * @see StoreCategory
 */
public class Category {
	private long id;
	private String name;
	private long subcategoryOf;
	
	public Category()
	{
		super();
	}

	public Category(long id, String name, long subcategoryOf) {
		super();
		this.id = id;
		this.name = name;
		this.subcategoryOf = subcategoryOf;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getSubcategoryOf() {
		return subcategoryOf;
	}
}
