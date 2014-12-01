package hr.foi.air.discountlocator.core;

import hr.foi.air.discountlocator.db.Discount;
import hr.foi.air.discountlocator.db.Store;

import java.util.ArrayList;

import android.app.Activity;

public abstract class DataLoader {
	//DbDataLoader extends DataLoader
	public ArrayList<Store> stores;//DbDataLoader - dohvaæanje podataka u stores
	public ArrayList<Discount> discounts;//DbDataLoader - dohvaæanje podataka u discounts
	OnDataLoadedListener dataLoadedListener;
	
	public void LoadData(Activity activity){
		if(dataLoadedListener == null)
			dataLoadedListener = (OnDataLoadedListener) activity; //dodjela tipa activityu, buduæi da main activity ionako implementira ondataloaded listener
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
	
	//Main activity implementira OnDataLoadedListener iz DataLoader.java(suèelje) - što znaèi da, kad su stores i discounts napunjeni, u jednom dataloaderu se poziva 
	//funkcija DataLoaded koja poziva dataLoadedListener.OnDataLoaded(stores, discounts); i javlja mu da su podaci uèitani, nakon èega main activity zna da
	//sad može raditi sa tim podacima (prikazati ih)
	
	public interface OnDataLoadedListener{
		public void OnDataLoaded(ArrayList<Store> stores, ArrayList<Discount> discounts);
	}
	
}