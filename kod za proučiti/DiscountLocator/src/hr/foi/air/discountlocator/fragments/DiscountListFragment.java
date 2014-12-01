package hr.foi.air.discountlocator.fragments;

import hr.foi.air.discountlocator.DiscountsExpandableAdapter;
import hr.foi.air.discountlocator.MainActivity.OnDataChangedListener;
import hr.foi.air.discountlocator.R;
import hr.foi.air.discountlocator.core.DataLoader;
import hr.foi.air.discountlocator.core.DbDataLoader;
import hr.foi.air.discountlocator.core.PositionProvider;
import hr.foi.air.discountlocator.core.WsDataLoader;
import hr.foi.air.discountlocator.db.Discount;
import hr.foi.air.discountlocator.db.Store;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class DiscountListFragment extends Fragment implements OnDataChangedListener{//update-a ExpendableListView!!!
	//fragment implementira OnDataChangedListener(iz MainActivity - interface!)

	private boolean mAlreadyLoaded = false;
	private ArrayList<Store> stores;
	private ArrayList<Discount> discounts;
	public boolean fromSearch = false;
	 
	
	@Override
	//onCreateView(LayoutInflater, ViewGroup, Bundle) creates and returns the view hierarchy associated with the fragment.
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list_layout, container, false);
		/*
		 * LayoutInflater Instantiates a layout XML file into its corresponding View objects. 
		 * 
		 * public View inflate (XmlPullParser parser, ViewGroup root, boolean attachToRoot)
		 * Inflate a new view hierarchy from the specified XML node. Throws InflateException if there is an error.
		 */
	}
	

	// it is VERY important to use the right event to load data
	// because you first display the list (even if it is empty)
	// then later on you get the data, so if you are to late
	// it will not be shown: http://developer.android.com/guide/components/fragments.html
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// if the data is already loaded then skip this part
		// if you don't skip it, then you will always reload the data in 
		// the ExpandableListView, e.g. if you do a search, and check the 
		// result data in DiscountDetailsFragment, when you press back, you 
		// reload the data and loose the result. 
		if (savedInstanceState == null && !mAlreadyLoaded) {
			mAlreadyLoaded = true;
			DataLoader dl = new DbDataLoader();
			dl.LoadData(getActivity());//poziva se LoadData iz DbDataLoadera
			
			if (!dl.DataLoaded()) {//ako podaci nisu uèitani
				// check if it is allowed to use web services if so, get the data
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
				boolean webservice_enabled = preferences.getBoolean("pref_allow_web", true);	
				if(webservice_enabled){
					dl = new WsDataLoader();
					dl.LoadData(getActivity());
				}
				else{
					Toast.makeText(getActivity(), "Local database is empty. Get from web disabled.", Toast.LENGTH_LONG).show();
				}
			}
			
			this.stores = dl.stores;
			this.discounts = dl.discounts;
		}
		else{
			loadData(this.stores, this.discounts);
		}
		// just to display the current position
		// this can be reused to collect only shops from the preferred radius
		PositionProvider positionProvider = new PositionProvider();
		Location location = positionProvider.getLatestCoordinates(getActivity());

		if(location != null ){
			Toast.makeText(getActivity(), "Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getActivity(), "GPS is off", Toast.LENGTH_SHORT).show();
		}
		
	}

	public void loadData(ArrayList<Store> stores, ArrayList<Discount> discounts){
		// now MainActivity no longer changes the list
		// Fragment is in charge for setting the data and changing the expandable list
		DiscountsExpandableAdapter adapter = new DiscountsExpandableAdapter(stores, discounts);
		adapter.setInflater( (LayoutInflater) getActivity().getSystemService(FragmentActivity.LAYOUT_INFLATER_SERVICE), getActivity());
		ExpandableListView expandableList = (ExpandableListView) getView().findViewById(R.id.list);
		//return inflater.inflate(R.layout.fragment_list_layout, container, false);
		if(expandableList != null) {
			expandableList.setAdapter(adapter);
		}	
	}

	@Override
	public void OnDataChanged(ArrayList<Store> stores, ArrayList<Discount> discounts) {
		this.stores = stores;
		this.discounts = discounts;
		loadData(stores, discounts);		
	}


}
