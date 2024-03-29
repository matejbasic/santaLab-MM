package net.neurolab.musicmap.googlemaps;

import net.neurolab.musicmap.OnDataChangedListener;
import net.neurolab.musicmap.MapFragmentInterface;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 
 * @author Ljiljana
 *
 *         FragmentTabMap is a Fragment class which is used to show a map and
 *         event markers. It implements OnDataChangedListener which tells it
 *         when data has arrived and can be loaded. It also implements
 *         MapFramgentInterface which connects it with main project (MusicMap
 *         app).
 */
public class FragmentTabMap extends SherlockFragment implements
		OnDataChangedListener, MapFragmentInterface {

	public boolean fromSearch = false;
	private ArrayList<Event> events;
	private GoogleMap gMap;
	private SupportMapFragment fSupportMap;

	private ArrayList<Markers> markers;

	/*
	 * private String previousLocation = "previousLocation"; private
	 * List<String> previousLocations = new ArrayList<String>();
	 * SharedPreferences sharedPreferences;
	 */

	public FragmentTabMap() {
		markers = new ArrayList<Markers>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tab_map, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (gMap == null) {
			initMap();
		}

		// onClickListener for infoWindow - goto Event
		gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {

				for (Markers tempMarker : markers) {
					if (tempMarker.getTitle().matches(marker.getTitle())) {
						// MainView activity = (MainView)getActivity();
						// activity.navigateToSingleEvent(tempMarker.getEventId());
						break;
					}
				}

			}
		});

		/*
		 * String theLocation = loadSavedPreferences();
		 * 
		 * if (!theLocation.equalsIgnoreCase(previousLocation) &&
		 * !isAlreadyLoaded(theLocation)) { Log.i("tabMap", "first load");
		 * previousLocation = theLocation; System.out.println(previousLocation);
		 * 
		 * try{ previousLocations.add(theLocation);
		 * System.out.println("blabla");} catch(Exception e){
		 * System.out.println(e); } try {
		 * System.out.println("Loadanje iz baze"); DataLoader dl = new
		 * DataLoaderDB(); dl.LoadData(getActivity(), theLocation);
		 * 
		 * Boolean eventsExists = dl.DataLoaded();
		 * 
		 * if (!eventsExists) { System.out.println("Loadanje sa servisa"); dl =
		 * new DataLoaderMM(); dl.LoadData(getActivity(), theLocation); }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } }
		 */
	}

	/*
	 * public boolean isAlreadyLoaded(String loc) { for (String l :
	 * previousLocations) { if (loc.equalsIgnoreCase(l)) { return true; } }
	 * return false; }
	 * 
	 * public String loadSavedPreferences() { sharedPreferences =
	 * PreferenceManager .getDefaultSharedPreferences(getActivity()); String
	 * theLocation = sharedPreferences.getString("theLocation", ""); if
	 * (theLocation.equalsIgnoreCase("") ||
	 * theLocation.equalsIgnoreCase(getResources() .getString(
	 * R.string.no_preferred_locations))) { return "Zagreb"; } else return
	 * theLocation;
	 * 
	 * }
	 */

	// ex loadData()
	public void addMarkersToMap() {
		if (gMap != null) {
			MarkerOptions markerOpt;
			double latitude = 0, longitude = 0;
			String title = "";

			for (int i = 0; i < markers.size(); i++) {
				latitude = markers.get(i).getLat();
				longitude = markers.get(i).getLng();
				title = markers.get(i).getTitle();
				markerOpt = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title(title);

				gMap.addMarker(markerOpt);
			}
		}
	}

	public void addMarkers(double lat, double lng, String desc) {
		markers.add(new Markers(lat, lng, desc));
	}

	@Override
	public void OnDataChanged(ArrayList<Event> events) {
		this.events = events;

		markers.clear();
		for (int i = 0; i < events.size(); i++) {
			Log.i("tabMap", events.get(i).getIdLocation().getName());
			addMarkers(events.get(i).getLat(), events.get(i).getLng(), events
					.get(i).getName()
					+ ", "
					+ events.get(i).getIdLocation().getName());
			// events.get(i).getId();???
		}

		addMarkersToMap();
	}

	private void initMap() {
		if (gMap == null) {

			fSupportMap = ((SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.map));

			if (fSupportMap != null) {
				gMap = fSupportMap.getMap();
			} else {
				Log.i("initMap", "mapFrag is null");
			}

			// check if map is created successfully or not
			if (gMap == null) {
				Toast.makeText(getActivity(),
						R.string.gmap_unable_to_create_error,
						Toast.LENGTH_SHORT).show();
			} else {
				// UiSettings mm = gMap.getUiSettings();
				// gMap.setMyLocationEnabled(true);
			}
		}
	}

	public void refresh() {
		this.onViewCreated(getView(), null);
	}

	@Override
	public Fragment getFragment() {
		return this;
	}

}

/*
 * 
 * public void addMarkersToMap() { if (gMap != null) { MarkerOptions markerOpt;
 * double latitude = 0, longitude = 0; String title = "";
 * 
 * for (int i = 0; i < markers.size(); i++) { latitude =
 * markers.get(i).getLat(); longitude = markers.get(i).getLng(); title =
 * markers.get(i).getTitle(); markerOpt = new MarkerOptions().position( new
 * LatLng(latitude, longitude)).title(title);
 * 
 * gMap.addMarker(markerOpt); } } }
 * 
 * public void addMarkers(double lat, double lng, String desc, long eventId) {
 * markers.add(new Markers(lat, lng, desc, eventId)); }
 * 
 * @Override public void OnDataChanged(ArrayList<Event> events) { this.events =
 * events;
 * 
 * markers.clear(); for (int i = 0; i < events.size(); i++) { Log.i("tabMap",
 * events.get(i).getIdLocation().getName()); addMarkers(events.get(i).getLat(),
 * events.get(i).getLng(), events .get(i).getName() + ", " +
 * events.get(i).getIdLocation().getName(), events.get(i).getId()); }
 * 
 * addMarkersToMap(); }
 */