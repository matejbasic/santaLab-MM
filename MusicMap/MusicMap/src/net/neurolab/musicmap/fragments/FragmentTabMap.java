package net.neurolab.musicmap.fragments;

import java.util.ArrayList;

import net.neurolab.musicmap.MainActivity.OnDataChangedListener;
import net.neurolab.musicmap.R;
import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.dl.DataLoader;
import net.neurolab.musicmap.dl.DataLoaderDB;
import net.neurolab.musicmap.dl.DataLoaderMM;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentTabMap extends SherlockFragment implements
		OnDataChangedListener {
	/*
	 * public int position; public String name;
	 */

	private static FragmentTabMap instance;
	private boolean mAlreadyLoaded = false;
	public boolean fromSearch = false;
	private ArrayList<Event> events;
	private GoogleMap googleMap;
	private ArrayList<Markers> markers;

	private FragmentTabMap() {
		markers = new ArrayList<Markers>();
	}

	public static FragmentTabMap getInstance() {
		if (instance == null)
			instance = new FragmentTabMap();
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.fragment_tab_map, container,
				false);
		System.out.println("OnCreate");

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

		System.out.println("OnViewCreated");

		if (googleMap == null)
			initilizeMap();

		try {

			if (savedInstanceState == null && !mAlreadyLoaded) {
				mAlreadyLoaded = true;

				System.out.println("Loadanje iz baze");
				DataLoader dl = new DataLoaderDB();
				dl.LoadData(getActivity(), "Zagreb");

				if (!dl.DataLoaded()) {
					System.out.println("Loadanje sa servisa");
					dl = new DataLoaderMM();
					dl.LoadData(getActivity(), "zagreb");
				}

				this.events = dl.events;
				System.out.println(events.size());

			}

			else {
				loadData();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

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

	public void loadData() {
		System.out.println(googleMap);
		System.out.println("LoadData fja");
		System.out.println(markers.size());
		if (googleMap != null) {
			MarkerOptions marker;
			double latitude = 0, longitude = 0;

			for (int i = 0; i < markers.size(); i++) {
				latitude = markers.get(i).getLat();
				longitude = markers.get(i).getLng();
				String title = markers.get(i).getTitle();

				marker = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title(title);

				googleMap.addMarker(marker);
				System.out.println("Dodan marker");
			}
		}
	}

	public void addMarkers(double lat, double lng, String name) {
		System.out.println("addMarkers");
		markers.add(new Markers(lat, lng, name));

	}

	@Override
	public void OnDataChanged(ArrayList<Event> events) {

		System.out.println("OnDataChangedfja");
		System.out.println(events.size());
		this.events = events;
		markers.clear();
		for (int i = 0; i < events.size(); i++) {
			addMarkers(events.get(i).getLat(), events.get(i).getLng(), events
					.get(i).getIdLocation().getName());
		}
		System.out.println("kraj ondatachanged");
		loadData();
	}

	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			/*
			 * MapFragment mMapFragment =
			 * (com.google.android.gms.maps.MapFragment) getActivity()
			 * .getFragmentManager().findFragmentById(R.id.map); googleMap =
			 * mMapFragment.getMap();
			 */
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
	/*
	 * public void onResume(){ initilizeMap(); loadData(); }
	 */
	/*
	 * public void OnDestroy(){ mAlreadyLoaded = false; }
	 */
}