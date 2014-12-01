package hr.foi.air.discountlocator;

import hr.foi.air.discountlocator.core.DataLoader.OnDataLoadedListener;
import hr.foi.air.discountlocator.db.Discount;
import hr.foi.air.discountlocator.db.Store;
import hr.foi.air.discountlocator.fragments.DiscountListFragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.activeandroid.ActiveAndroid;
//Main activity implementira OnDataLoadedListener iz DataLoader.java(suèelje) - što znaèi da, kad su stores i discounts napunjeni, u jednom dataloaderu se poziva 
//funkcija DataLoaded koja poziva dataLoadedListener.OnDataLoaded(stores, discounts); i javlja mu da su podaci uèitani, nakon èega main activity zna da
//sad može raditi sa tim podacima (poslati ih dalje)
public class MainActivity extends FragmentActivity implements OnDataLoadedListener{

	private ProgressDialog dialog = null;
	DiscountListFragment dlf = null;//fragment za uèitavanje podataka u listu
	OnDataChangedListener dataChanged = null;//eventListener za poèenu stranicu (prikaz podataka)

	@Override
	protected void onCreate(Bundle savedInstanceState) {//d
		super.onCreate(savedInstanceState);//d
		setContentView(R.layout.main_fragment_layout);//layout mainActivity-a je main_fragment_layout koji sadrži sljedeæe:
				/*
				<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
				    android:id="@+id/fragment_container"
				    android:layout_width="match_parent"
				    android:layout_height="match_parent" />
				    */
/*
 * FrameLayout služi za zauzimanje dijela ekrana za prikaz (samo) jedne stavke.
 * Generalno, FrameLayout trebao bi sadržavati samo jedan child view, jer je teško organizirati nekoliko pogleda
 * skalabilno razlièitim velièinama ekrana bez njihova preklapanja.
 * Moguæe je dodati ih više, no onda je potrebno kontrolirati njihove pozicije korištenjem layout_gravity atributa.
 */
        ActiveAndroid.initialize(this);//inicijalizacija ActiveAndroida (orm, baza !)

        // start the dialog
        //show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable)
        //getResources() - Return a Resources instance for your application's package.
        dialog = ProgressDialog.show(this, "", getResources().getString(R.string.dialog_please_wait), true, true); 
        
        // create a new fragment to display the list
        dlf = new DiscountListFragment();
        // DataListFragment also implements OnDataChangedListener from MainActivity
        // here we are "hooking-up" this listener in DiscountListFragment
        dataChanged = (OnDataChangedListener) dlf;
        /*
        *DiscountListFragment:
        public void OnDataChanged(ArrayList<Store> stores, ArrayList<Discount> discounts) {
			this.stores = stores;
			this.discounts = discounts;
			loadData(stores, discounts);		
		}
         */
        // show the main fragment
        // Add the fragment to the 'fragment_container' FrameLayout
	    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, dlf).commit();
		/*
		 * getSupportFragmentManager() - Return the FragmentManager for interacting with fragments associated with this activity.
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {//argument!!
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();//dohvati id stavke menia
		if (id == R.id.action_settings) {//ako je to id settings stavke
			// load the activity which displays the PreferenceFragment with 
			// users settings and preferences
		    Intent settingsActivity = new Intent(this, SettingsActivity.class);
			this.startActivity(settingsActivity);//otvori novu aktivnost SettingsActivity klase (onCreate)
			return true; //izvršeno
		}
		else if(id == R.id.action_search){//ako je to id stavke search
			SearchDialog sd = new SearchDialog(this);
			sd.show();//prikaži search dialog
			return true;//izvršeno
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void OnDataLoaded(ArrayList<Store> stores, ArrayList<Discount> discounts) {
		// data updated: (either from database, search or web service)
		// raise the event for DiscountListFragment so it updates the ExpandableListView   !!!!!!!!!!!!!!
		// this is because DataLoader accepts Activity as an argument, and not Fragment
		// therefore MainActivity acts as a data collector for Fragments
		if(dlf != null) dlf.OnDataChanged(stores, discounts);
		if(dialog != null)
			dialog.cancel();
	}
	
	/*
	 * In order to reuse the Fragment UI components, you should build each as a completely self-contained, 
	 * modular component that defines its own layout and behavior. 
	 * Once you have defined these reusable Fragments, you can associate them with an Activity 
	 * and connect them with the application logic to realize the overall composite UI.
	 * 
	 * Often you will want one Fragment to communicate with another, for example to change the content based on a user event. 
	 * All Fragment-to-Fragment communication is done through the associated Activity. 
	 * Two Fragments should never communicate directly.
	 * 
	 * To allow a Fragment to communicate up to its Activity, you can define an interface in the Fragment class 
	 * and implement it within the Activity. The Fragment captures the interface implementation 
	 * during its onAttach() lifecycle method and can then call the Interface methods 
	 * in order to communicate with the Activity.
	 */
	// this listener is used for delivering data to DiscountListFragment
	public interface OnDataChangedListener{//suèelje -> implementirano u DiscountListFragment!
		void OnDataChanged(ArrayList<Store> stores, ArrayList<Discount> discounts);
	}
	
	// just a simple "swipe gesture" recognizer
	// when you swipe from LEFT to RIGHT it will press back button
	float x1 = 0, x2 = 0;
	
	public boolean onTouchEvent(MotionEvent touchevent) {		
		// check if swipe is enabled in the preferences by the user
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean swipe_enabled = preferences.getBoolean("pref_backswipe", true);		
		if(!swipe_enabled) return false;
		
		switch (touchevent.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				x1 = touchevent.getX();
				break;
			}
			case MotionEvent.ACTION_UP: {
				x2 = touchevent.getX();
				if (x1 < x2) {
					onBackPressed();
				}
			}
		}
		return false;
	}
}
