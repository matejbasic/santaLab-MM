package hr.foi.air.discountlocator.ws;

import hr.foi.air.discountlocator.db.Discount;
import hr.foi.air.discountlocator.db.Store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class contains additional methods which makes it easier to use standard JSON
 * library.
 * 
 */
public class JsonAdapter {

	/**
	 * Static method for getting a JSON string representing an array, but
	 * containing only one object. As PHP (in our implementation) expects the
	 * data in array, it is not possible to use string that represent only one
	 * object, but rather array of objects.
	 * 
	 * @param jsonObject
	 *            Object containing name value pairs.
	 * @return String describing an array which contains one object with its
	 *         name value pairs. <br>
	 *         An example:<br>
	 *         <code> [{"name":"value", "surname":"survalue"}]</code>
	 * 
	 */
	public static String getJsonArrayString(JSONObject jsonObject) {
		if (jsonObject != null) {
			JSONArray tmp = new JSONArray();
			tmp.put(jsonObject);
			return tmp.toString();
		} else
			return "[]"; // an empty array
	}

	/**
	 * Static method for converting the jsonString results containing the
	 * information about stores into an ArrayList of Stores.
	 * 
	 * @param jsonString
	 *            String containing a JSONArray of objects with all name value
	 *            pairs.
	 * @return ArrayList of Store objects containing the information on stores.
	 * @throws Exception
	 *             If conversion from string to JSONObject is not possible the
	 *             exception is raised.
	 * 
	 */
	public static ArrayList<Store> getStores(String jsonString)
			throws Exception {
		ArrayList<Store> stores = new ArrayList<Store>();
		if (jsonString.length() == 0)
			jsonString = '[' + jsonString + ']';

		if (!jsonString.startsWith("["))
			jsonString = '[' + jsonString + ']';

		JSONArray jsonArr = new JSONArray(jsonString);
		int size = jsonArr.length();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			Store store = new Store(jsonObj.getLong("id"),
					jsonObj.getString("name"),
					jsonObj.getString("description"),
					jsonObj.getString("imgUrl"), jsonObj.getLong("longitude"),
					jsonObj.getLong("latitude"));
			stores.add(store);
		}

		return stores;
	}

	/**
	 * Static method for converting the jsonString results containing the
	 * information about discounts into an ArrayList of Discounts.
	 * 
	 * @param jsonString
	 *            String containing a JSONArray of objects with all name value
	 *            pairs.
	 * @return ArrayList of Store objects containing the information on stores.
	 * @throws Exception
	 *             If conversion from string to JSONObject is not possible the
	 *             exception is raised.
	 * 
	 */
	public static ArrayList<Discount> getDiscounts(String jsonString)
			throws Exception {
		ArrayList<Discount> discounts = new ArrayList<Discount>();
		if (jsonString.length() == 0)
			jsonString = '[' + jsonString + ']';

		if (!jsonString.startsWith("["))
			jsonString = '[' + jsonString + ']';

		JSONArray jsonArr = new JSONArray(jsonString);
		int size = jsonArr.length();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);

			Discount discount = new Discount(jsonObj.getLong("id"),
					jsonObj.getString("name"),
					jsonObj.getString("description"),
					jsonObj.getLong("storeId"),
					ConvertToDate(jsonObj.getString("startDate")),
					ConvertToDate(jsonObj.getString("endDate")),
					jsonObj.getInt("discount"));
			discounts.add(discount);
		}

		return discounts;
	}

	/**
	 * Method converts date from string to Date by using a Calendar class.
	 * 
	 * @param jsonDate
	 *            String containing a date formated as yyyy-MM-dd
	 * @return Date object initialized to received date.
	 */
	public static Date ConvertToDate(String jsonDate) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(jsonDate.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(jsonDate.substring(5, 7)) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(jsonDate.substring(8, 10)));
		Date date = cal.getTime();

		return date;
	}
}