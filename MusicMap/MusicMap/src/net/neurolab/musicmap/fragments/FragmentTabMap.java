package net.neurolab.musicmap.fragments;

import java.util.ArrayList;

import net.neurolab.musicmap.MainActivity.OnDataChangedListener;
import net.neurolab.musicmap.R;
import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.db.PreferredLocation;
import net.neurolab.musicmap.dl.DataLoader;
import net.neurolab.musicmap.dl.DataLoaderDB;
import net.neurolab.musicmap.dl.DataLoaderMM;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentTabMap extends SherlockFragment implements OnDataChangedListener {

	private boolean mAlreadyLoaded = false;
	public boolean fromSearch = false;
	private ArrayList<Event> events;
	private GoogleMap gMap;
	private SupportMapFragment fSupportMap;
	private Location cPrefLocation;
	
	private ArrayList<Markers> markers;

	public FragmentTabMap() {
		markers = new ArrayList<Markers>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("tabMap", "create");
		
		return inflater.inflate(R.layout.fragment_tab_map, container, false);
	}
	
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		System.out.println("OnViewCreated");	
		if (gMap == null) {
			initMap();
		}
		try {

			if (savedInstanceState == null && !mAlreadyLoaded) {
				Log.i("tabMap", "first load");
				mAlreadyLoaded = true;
				
				cPrefLocation = new PreferredLocation().getSingleLocation();
				gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cPrefLocation.getLat(), cPrefLocation.getLng()), (float) 12.0));
				
				System.out.println("Loadanje iz baze");
				DataLoader dl = new DataLoaderDB();
				dl.LoadData(getActivity(), cPrefLocation.getCity());

				Boolean eventsExists = dl.DataLoaded();
				  
				if (!eventsExists) {
					System.out.println("Loadanje sa servisa");
					dl = new DataLoaderMM();
					dl.LoadData(getActivity(), cPrefLocation.getCity());
				}
				
				//this.events = dl.events; //? dodaje se vec u onDataChanged
				//Log.i("events size", String.valueOf(events.size()));
				
			}

			else {
				Log.i("tabMap", "n-th load");
				DataLoader dl = new DataLoaderDB();
				dl.LoadData(getActivity(), "Zagreb");
				/*
				this.events = dl.events;
				Log.i("events size", String.valueOf(events.size()));
				addMarkersToMap();
				*/
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	//ex loadData()
	public void addMarkersToMap() {
		Log.i("addMarkersToMap map", gMap.toString());
		if (gMap != null) {
			MarkerOptions marker;
			double latitude = 0, longitude = 0;
			String title = "";
			Log.i("addMtoM mark size", String.valueOf(markers.size()));
			for (int i = 0; i < markers.size(); i++) {
				latitude = markers.get(i).getLat();
				longitude = markers.get(i).getLng();
				title = markers.get(i).getTitle();
				marker = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title(title);
				
				//Log.i("marker vals", String.valueOf(latitude) + " " + String.valueOf(longitude) + " " + title);
				
				gMap.addMarker(marker);
			}
		}
	}

	public void addMarkers(double lat, double lng, String name) {
		//System.out.println("addMarkers");
		markers.add(new Markers(lat, lng, name));

	}

	@Override
	public void OnDataChanged(ArrayList<Event> events) {
		Log.i("tabMap", "onDataChanged");
		Log.i("OnDChanged events size", String.valueOf(events.size()));
		this.events = events;
		markers.clear();
		for (int i = 0; i < events.size(); i++) {
			addMarkers(events.get(i).getLat(), events.get(i).getLng(), events.get(i).getIdLocation().getName());
		}
		
		addMarkersToMap();
	}

	private void initMap() {
		Log.i("initMap status", String.valueOf(gMap));
		if (gMap == null) {
			
			fSupportMap = ((SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.map)); 
			Log.i("tempFrag", String.valueOf(fSupportMap));
			
			if (fSupportMap != null) {
				gMap = fSupportMap.getMap();
			}
			else {
				Log.i("initMap", "mapFrag is null");
			}
			
			// check if map is created successfully or not
			if (gMap == null) {
				Log.i("googleMap", "=null, ludilo!");
				Toast.makeText(getActivity(), R.string.gmap_unable_to_create_error, Toast.LENGTH_SHORT).show();
			} else {
				//UiSettings mm = gMap.getUiSettings();
				//gMap.setMyLocationEnabled(true);
			}
		}
	}
	
}