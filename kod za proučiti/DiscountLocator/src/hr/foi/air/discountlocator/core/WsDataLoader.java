package hr.foi.air.discountlocator.core;

import hr.foi.air.discountlocator.db.Discount;
import hr.foi.air.discountlocator.db.Store;
import hr.foi.air.discountlocator.ws.JsonAdapter;
import hr.foi.air.discountlocator.ws.WebServiceAsyncTask;
import hr.foi.air.discountlocator.ws.WebServiceResultHandler;
import android.app.Activity;
//Data Loader je apstraktna klasa koju WsDataLoader i DbDataLoader naslje�uju

public class WsDataLoader extends DataLoader{

	private boolean storesLoaded = false;
	private boolean discountsLoaded = false;
	
	@Override
	public void LoadData(Activity activity) {
		super.LoadData(activity);
		
		WebServiceAsyncTask asyncTaskStores = new WebServiceAsyncTask();
		Object paramsStores[] = new Object[]{activity, "", "stores", "getAll", "items", null, acceptStores};
		asyncTaskStores.execute(paramsStores);
		
		//getting discounts
		WebServiceAsyncTask asyncTaskDiscount = new WebServiceAsyncTask();
		Object paramsDisounts[] = new Object[]{activity, "", "discounts", "getAll", "items", null, acceptDiscounts};
		asyncTaskDiscount.execute(paramsDisounts);
	}
	
	WebServiceResultHandler acceptStores = new WebServiceResultHandler() {
		
		@Override
		public void handleResult(String result, boolean ok) {
			//convert to List<Stores>
			if (ok)
			{
				try {
					stores = JsonAdapter.getStores(result);
					for (Store s : stores) {
						s.save();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			storesLoaded = true;
			//showLoadedData();
			DataLoaded();//iz DataLoadera - ako su podaci u�itani vra�a true... goto DataLoader.java :)
		}
	};
	
	WebServiceResultHandler acceptDiscounts = new WebServiceResultHandler() {
		
		@Override
		public void handleResult(String result, boolean ok) {
			//converto to List<Discounts>
			if (ok)
			{
				try {
					discounts = JsonAdapter.getDiscounts(result);
					// save to local database
					for (Discount d : discounts) {
						d.save();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			discountsLoaded = true;
			showLoadedData();
			
		}
	};
	
	private void showLoadedData()
	{
		//Synchronization of results
		if (storesLoaded && discountsLoaded)
		{
			bindDiscountsToStores();
			
			storesLoaded = false;
			discountsLoaded = false;
			
			DataLoaded();
		}
	}
		
	private void bindDiscountsToStores() {//dodaje objekt store pripadaju�em objektu discount
		for (Store s : stores) {
			for (Discount d : discounts) {
				if(d.getRemoteId() == s.getRemoteId()){
					d.setStore(s);
					d.save();
				}
			}
		}
	}

}
