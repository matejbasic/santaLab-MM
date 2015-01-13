package net.neurolab.musicmap.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import net.neurolab.musicmap.EventsExpandableAdapter;
import net.neurolab.musicmap.MainActivity.OnDataChangedListener;
import net.neurolab.musicmap.R;
import net.neurolab.musicmap.db.Event;
import android.os.Bundle;
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

		listView = (ExpandableListView) view.findViewById(R.id.listView);
		if (listView != null) {
			EventsExpandableAdapter adapter = new EventsExpandableAdapter(
					getActivity(), groups);
			listView.setAdapter(adapter);
		} else {
			Log.i("tabList", "listView = null");
		}
		// Log.i("tabList", "created");
		return view;
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
		// now MainActivity no longer changes the list
		// Fragment is in charge for setting the data and changing the expandable list	
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
			//Log.i("tabList", "listView = null");
		}
		
		
	}
	
	@Override
	public void OnDataChanged(ArrayList<Event> events) {
		// Log.i("tabList", "On data changed");
		this.events = events;

		this.groups = new ArrayList<Group>();

		int i = -1;
		Date eDate = null;
		for (Event event : this.events) {
			eDate = event.getEventTime();
			if (eDate != null) {
				// Log.i("list event", sdf.format(eDate) + " " +
				// event.getName());
				i = getGroupIndex(eDate);
				groups.get(i).getChildren().add(event);
			}
		}
		loadData(groups);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// setUserVisibleHint(true);
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
