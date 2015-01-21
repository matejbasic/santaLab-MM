package net.neurolab.musicmap.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.neurolab.musicmap.EventsExpandableAdapter;
import net.neurolab.musicmap.OnDataChangedListener;
import net.neurolab.musicmap.R;
import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.dl.DataLoader;
import net.neurolab.musicmap.dl.DataLoaderDB;
import net.neurolab.musicmap.dl.DataLoaderMM;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentTabList extends SherlockFragment implements
		OnDataChangedListener {
	private ArrayList<Group> groups = new ArrayList<Group>();
	private ArrayList<Event> events;
	private ExpandableListView listView = null;
	
	private String previousLocation = "previousLocation";
	private List<String> previousLocations = new ArrayList<String>();
	SharedPreferences sharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab1.xml
		View view = inflater.inflate(R.layout.fragment_tab_list, container,
				false);
		
		
		
		String theLocation = loadSavedPreferences();
		
		if (!theLocation.equalsIgnoreCase(previousLocation) && !isAlreadyLoaded(theLocation)) {
			Log.i("tabList", "first load");
			previousLocation = theLocation;
			System.out.println(previousLocation);
			
			try{
			previousLocations.add(theLocation);
			System.out.println("blabla");}
			catch(Exception e){
				System.out.println(e);
			}
			try {
				
				/*
				 * if (l.size() > 0) { location = l.get(0);
				 * gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new
				 * LatLng(l .get(0).getLat(), l.get(0).getLng()), (float)
				 * 12.0)); }
				 */
				/*
				 * gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				 * cPrefLocation.getLat(), cPrefLocation.getLng()), (float)
				 * 12.0));
				 */

				System.out.println("Loadanje iz baze");
				DataLoader dl = new DataLoaderDB();
				dl.LoadData(getActivity(), theLocation);

				Boolean eventsExists = dl.DataLoaded();

				if (!eventsExists) {
					System.out.println("Loadanje sa servisa");
					dl = new DataLoaderMM();
					dl.LoadData(getActivity(), theLocation);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
		
		
		listView = (ExpandableListView) view.findViewById(R.id.listView);
		if (listView != null) {
			EventsExpandableAdapter adapter = new EventsExpandableAdapter(
					getActivity(), groups);
			listView.setAdapter(adapter);
		} else {
			Log.i("tabList", "listView = null");
		}
		return view;
	}
	
	
	public boolean isAlreadyLoaded(String loc) {
		for (String l : previousLocations) {
			if (loc.equalsIgnoreCase(l)) {
				return true;
			}
		}
		return false;
	}

	public String loadSavedPreferences() {
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String theLocation = sharedPreferences.getString("theLocation", "");
		if (theLocation.equalsIgnoreCase("")
				|| theLocation.equalsIgnoreCase(getResources()
						.getString(
								R.string.no_preferred_locations))) {
			return "Zagreb";
		} else
			return theLocation;

	}
	

	private SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy",
			Locale.getDefault());
	private SimpleDateFormat sdfMonth = new SimpleDateFormat("MM",
			Locale.getDefault());
	private SimpleDateFormat sdfDay = new SimpleDateFormat("dd",
			Locale.getDefault());

	// return values: 0 (dates are equal), < 0 (d2 is less than d1), > 0 (d2 is
	// greater)
	// util.Date methods are deprecated
	// need better date type, this is ugly!
	private int compareDates(Date d1, Date d2) {

		if (Integer.parseInt(sdfYear.format(d1)) != Integer.parseInt(sdfYear
				.format(d2))) {
			return Integer.parseInt(sdfYear.format(d2))
					- Integer.parseInt(sdfYear.format(d1));
		} else if (Integer.parseInt(sdfMonth.format(d1)) != Integer
				.parseInt(sdfMonth.format(d2))) {
			return Integer.parseInt(sdfMonth.format(d2))
					- Integer.parseInt(sdfMonth.format(d1));
		} else if (Integer.parseInt(sdfDay.format(d1)) != Integer
				.parseInt(sdfDay.format(d2))) {
			return Integer.parseInt(sdfDay.format(d2))
					- Integer.parseInt(sdfDay.format(d1));
		}

		return 0;
	}

	private int getGroupIndex(Date date) {
		int compDate = -1;
		int i = 0;

		for (Group group : groups) {
			compDate = compareDates(date, group.getDate());
			if (compDate > 0) {
				Group newGroup = new Group(date);
				groups.add(i, newGroup);
				return i;
			} else if (compDate == 0) {
				return i;
			} else {
				i++;
			}
		}
		Group newGroup = new Group(date);
		groups.add(i, newGroup);
		return i;
	}


	public void loadData(ArrayList<Group> groups){
		
		try{
			listView = (ExpandableListView) getView().findViewById(R.id.listView);
		}
		catch(Exception e){
			Log.i("loadDataTabList", e.toString());
		}

		if (listView != null) {
			EventsExpandableAdapter adapter = new EventsExpandableAdapter(
					getActivity(), groups);
			
			listView.setAdapter(adapter);
			
		} 
		else {
			Log.i("tabList", "listView = null");
		}
		
		
	}
	
	@Override
	public void OnDataChanged(ArrayList<Event> events) {
		this.events = events;

		this.groups = new ArrayList<Group>();

		int i = -1;
		Date eDate = null;
		for (Event event : this.events) {
			eDate = event.getEventTime();
			if (eDate != null) {
				i = getGroupIndex(eDate);
				groups.get(i).getChildren().add(event);
			}
		}
		loadData(groups);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void setIndicatorPosition() {
		if (listView != null) {
			listView.setIndicatorBounds(listView.getRight() - 40,
					listView.getWidth());
		}

	}

	public void refresh() {
		this.onViewCreated(getView(), null);
	}
}
