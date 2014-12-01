package hr.foi.air.discountlocator.core;

import hr.foi.air.discountlocator.db.Discount;
import hr.foi.air.discountlocator.db.Store;

import java.util.ArrayList;
import java.util.List;

import com.activeandroid.query.Select;

import android.app.Activity;
import android.widget.Toast;

//Data Loader je apstraktna klasa koju WsDataLoader i DbDataLoader nasljeðuju

public class DbDataLoader extends DataLoader{
	
	@Override
	public void LoadData(Activity activity) {//overriden DataLoader
		super.LoadData(activity);
		
		List<Store> storesFromDb = null ;
		List<Discount> discountsFromDb = null;
		
		// keep in mind that we need two calls instead of one
		// we actually never binded stores and discounts in database
		// we are using local infrastructure to display them (DiscountExpandableAdapter)
		// while playing with database, make sure you change version in the manifest file
		boolean databaseQuerySuccessfull = false;
		
		try{
			storesFromDb = new Select().all().from(Store.class).execute();
			discountsFromDb = new Select().all().from(Discount.class).execute();
			
			databaseQuerySuccessfull = true;
		}catch (NullPointerException e){
			e.printStackTrace();
		}
		
		if(databaseQuerySuccessfull == true && storesFromDb.size() > 0 ){
			Toast.makeText(activity, "Loading local data", Toast.LENGTH_SHORT).show();
			
			stores = (ArrayList<Store>) storesFromDb;
			discounts = (ArrayList<Discount>) discountsFromDb;
	
			DataLoaded();
			/*
			 * metoda iz DataLoader-a
			 * 	public boolean DataLoaded(){
					if(stores == null || discounts == null){
						return false;
					}
					else{
						dataLoadedListener.OnDataLoaded(stores, discounts);
						return true;
					}
				}
			 */
		}
	}

}
