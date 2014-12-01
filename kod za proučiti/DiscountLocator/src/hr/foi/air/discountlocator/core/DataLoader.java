package hr.foi.air.discountlocator.core;

import hr.foi.air.discountlocator.db.Discount;
import hr.foi.air.discountlocator.db.Store;

import java.util.ArrayList;

import android.app.Activity;

public abstract class DataLoader {
	//DbDataLoader extends DataLoader
	public ArrayList<Store> stores;//DbDataLoader - dohva�anje podataka u stores
	public ArrayList<Discount> discounts;//DbDataLoader - dohva�anje podataka u discounts
	OnDataLoadedListener dataLoadedListener;
	
	public void LoadData(Activity activity){
		if(dataLoadedListener == null)
			dataLoadedListener = (OnDataLoadedListener) activity; //dodjela tipa activityu, budu�i da main activity ionako implementira ondataloaded listener
	}
	
	public boolean DataLoaded(){
		if(stores == null || discounts == null){
			return false;
		}
		else{
			dataLoadedListener.OnDataLoaded(stores, discounts);
			return true;
		}
	}
	
	//Main activity implementira OnDataLoadedListener iz DataLoader.java(su�elje) - �to zna�i da, kad su stores i discounts napunjeni, u jednom dataloaderu se poziva 
	//funkcija DataLoaded koja poziva dataLoadedListener.OnDataLoaded(stores, discounts); i javlja mu da su podaci u�itani, nakon �ega main activity zna da
	//sad mo�e raditi sa tim podacima (prikazati ih)
	
	public interface OnDataLoadedListener{
		public void OnDataLoaded(ArrayList<Store> stores, ArrayList<Discount> discounts);
	}
	
}