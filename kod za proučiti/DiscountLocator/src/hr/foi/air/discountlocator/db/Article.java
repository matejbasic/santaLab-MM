package hr.foi.air.discountlocator.db;

/**
 * Entity class representing an article (an item in the store). 
 * Each article can be connected to zero, one or more discounts.
 * @see Discount
 * @see DiscountArtickle
 */
public class Article {
	private long id;
	private String name;
	private double price;
	private String imgUrl;
	
	public Article()
	{
		super();
	}

	public Article(long id, String name, double price, String imgUrl) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.imgUrl = imgUrl;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getImgUrl() {
		return imgUrl;
	}
}
