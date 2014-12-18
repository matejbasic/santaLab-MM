package net.neurolab.musicmap.fragments;

import java.util.ArrayList;

import net.neurolab.musicmap.MainActivity.OnDataChangedListener;
import net.neurolab.musicmap.R;
import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.dl.DataLoader;
import net.neurolab.musicmap.dl.DataLoaderDB;
import net.neurolab.musicmap.dl.DataLoaderMM;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

public class FragmentTabMap extends SherlockFragment implements OnDataChangedListener {

	private boolean mAlreadyLoaded = false;
	public boolean fromSearch = false;
	private ArrayList<Event> events;
	private GoogleMap googleMap;
	private SupportMapFragment mapFragment;
	
	private ArrayList<Markers> markers;

	public FragmentTabMap() {
		markers = new ArrayList<Markers>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.fragment_tab_map, container,
				false);
		System.out.println("OnCreateView");
		Log.i("view", view.toString());
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//System.out.println("OnViewCreated");	
		if (googleMap == null) {
			initilizeMap();

		}
		try {

			if (savedInstanceState == null && !mAlreadyLoaded) {
				mAlreadyLoaded = true;

				//System.out.println("Loadanje iz baze");
				DataLoader dl = new DataLoaderDB();
				dl.LoadData(getActivity(), "Zagreb");

				Boolean eventsExists = dl.DataLoaded();
				  
				if (!eventsExists) {
					System.out.println("Loadanje sa servisa");
					dl = new DataLoaderMM();
					dl.LoadData(getActivity(), "zagreb");
				}

				//this.events = dl.events; //? dodaje se vec u onDataChanged
				//Log.i("events size", String.valueOf(events.size()));

			}

			else {
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
		/*
		System.out.println("addMarkersToMap fja");
		Log.i("markers num", String.valueOf(markers.size()));
		Log.i("googleMap", String.valueOf(this.googleMap));
		*/
		initilizeMap();
		if (googleMap != null) {
			//Log.i("addMarkersToMap", "map!=null");
			MarkerOptions marker;
			double latitude = 0, longitude = 0;

			for (int i = 0; i < markers.size(); i++) {
				latitude = markers.get(i).getLat();
				longitude = markers.get(i).getLng();
				String title = markers.get(i).getTitle();
				//Log.i("marker vals", String.valueOf(latitude) + " " + String.valueOf(longitude) + " " + title);
				marker = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title(title);

				googleMap.addMarker(marker);
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
		Log.i("events size", String.valueOf(events.size()));
		this.events = events;
		markers.clear();
		for (int i = 0; i < events.size(); i++) {
			addMarkers(events.get(i).getLat(), events.get(i).getLng(), events
					.get(i).getIdLocation().getName());
		}
		//System.out.println("kraj ondatachanged");
		addMarkersToMap();
	}

	private void initilizeMap() {
		Log.i("initMap status", String.valueOf(googleMap));
		if (googleMap == null) {
			
			mapFragment = ((SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.map)); 
			Log.i("tempFrag", String.valueOf(mapFragment));
			if (mapFragment == null) {
				new Thread(new Runnable() {
					int i = 0;
					@Override
					public void run() {
						while (i < 10) {
							/*
							getChildFragmentManager().beginTransaction().add(R.id.map, mapFragment, "MapFragment").commit();
							getChildFragmentManager().executePendingTransactions();
							*/
							mapFragment = ((SupportMapFragment) getChildFragmentManager()
									.findFragmentById(R.id.map)); 
							Log.i("tempFrag (new T)", String.valueOf(mapFragment));
							i++;
							try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
								Log.i("new Thrad", "it's not sleep time yet");
								e.printStackTrace();
							}
						}
						
					}}).start();
				
			}
			
			googleMap = mapFragment.getMap();
			
			// check if map is created successfully or not
			if (googleMap == null) {
				Log.i("googleMap", "=null, ludilo!");
				Toast.makeText(getActivity(), "Sorry! unable to create maps",
						Toast.LENGTH_SHORT).show();
			} else {
				UiSettings mm = googleMap.getUiSettings();
				googleMap.setMyLocationEnabled(true);
			}
		}
	}
	
	@Override
	public void onPause() {
		Log.i("tabMap", "I'm paused");
		
		super.onPause();
		/*
		cameraPos = googleMap.getCameraPosition();
		googleMap = null;
		googleMap.clear();
		*/
	}
	@Override
	public void onResume() {
		Log.i("tabMap", "I'm resuming");
		
		/*
		if (cameraPos != null) {		
			googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos));		
			cameraPos = null;
		}
		*/
		super.onResume();
	}
	
	
	
}