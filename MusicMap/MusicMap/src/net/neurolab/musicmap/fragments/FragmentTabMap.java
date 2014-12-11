package net.neurolab.musicmap.fragments;

import java.util.ArrayList;

import net.neurolab.musicmap.MainActivity.OnDataChangedListener;
import net.neurolab.musicmap.R;
import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.dl.DataLoader;
import net.neurolab.musicmap.dl.DataLoaderDB;
import net.neurolab.musicmap.dl.DataLoaderMM;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentTabMap extends SherlockFragment implements
		OnDataChangedListener {
	/*
	 * public int position; public String name;
	 */

	private boolean mAlreadyLoaded = false;
	public boolean fromSearch = false;
	private ArrayList<Event> events;
	private GoogleMap googleMap;

	public FragmentTabMap() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.fragment_tab_map, container,
				false);

		try {
			// Loading map
			initilizeMap();

			if (savedInstanceState == null && !mAlreadyLoaded) {
				mAlreadyLoaded = true;
				DataLoader dl = new DataLoaderDB();
				dl.LoadData(getActivity(), "Zagreb");

				if (!dl.DataLoaded()) {
					// check if it is allowed to use web services if so, get the
					// data
					dl = new DataLoaderMM();
					dl.LoadData(getActivity(), "zagreb");
				}

				// if (dl.events != null) {// obrisati
				this.events = dl.events;
				// loadData(this.events);// obrisati
				// }

			} else {
				loadData(this.events);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}

	// it is VERY important to use the right event to load data
	// because you first display the list (even if it is empty)
	// then later on you get the data, so if you are to late
	// it will not be shown:
	// http://developer.android.com/guide/components/fragments.html
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// if the data is already loaded then skip this part
		// if you don't skip it, then you will always reload the data in
		// the ExpandableListView, e.g. if you do a search, and check the
		// result data in DiscountDetailsFragment, when you press back, you
		// reload the data and loose the result.

		// just to display the current position
		// this can be reused to collect only shops from the preferred radius
		// PositionProvider positionProvider = new
		// PositionProvider(getActivity());
		// Location location = positionProvider.getLatestCoordinates();

	}

	public void loadData(ArrayList<Event> events) {

		if (googleMap != null) {
			MarkerOptions marker;
			double latitude = 0, longitude = 0;

			for (int i = 0; i < events.size(); i++) {
				latitude = events.get(i).getLat();
				longitude = events.get(i).getLng();
				String title = events.get(i).getIdLocation().getName();

				marker = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title(title);

				googleMap.addMarker(marker);
			}
		}
	}

	@Override
	public void OnDataChanged(ArrayList<Event> events) {

		// if (events != null) {
		this.events = events;
		System.out.println(googleMap);
		loadData(events);
		// }
	}

	private void initilizeMap() {
		if (googleMap == null) {
			// googleMap = ((SupportMapFragment) getChildFragmentManager()
			// .findFragmentById(R.id.map)).getMap();

			MapFragment mMapFragment = (com.google.android.gms.maps.MapFragment) getActivity()
					.getFragmentManager().findFragmentById(R.id.map);
			googleMap = mMapFragment.getMap();

			System.out.println("inicijalizacija mape");
			System.out.println(googleMap);

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getActivity(), "Sorry! unable to create maps",
						Toast.LENGTH_SHORT).show();
			} else {
				UiSettings mm = googleMap.getUiSettings();
				googleMap.setMyLocationEnabled(true);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		initilizeMap();
	}
/*
	@Override
	public void onDestroy() {
		super.onDestroy();
		MapFragment mMapFragment = (com.google.android.gms.maps.MapFragment) getActivity()
				.getFragmentManager().findFragmentById(R.id.map);
		if (mMapFragment != null) {
			getActivity().getFragmentManager().beginTransaction()
					.remove(mMapFragment).commit();
		}
	}
*/
}