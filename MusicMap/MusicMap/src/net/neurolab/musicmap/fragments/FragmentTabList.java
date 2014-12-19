package net.neurolab.musicmap.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

public class FragmentTabList extends SherlockFragment implements OnDataChangedListener {
	private ArrayList<Group> groups = new ArrayList<Group>();
	private ArrayList<Event> events;
	private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd.MM.yyyy.", Locale.getDefault());
	private ExpandableListView listView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	private void createDebugData() {
		/*
		 * for (int j = 0; j < 5; j++) {
			Group group = new Group("2" + String.valueOf(j) + ".10.2014");
			for (int i = 0; i < 5; i++) {
				group.getChildren().add("Sub item no. " + i);
			}
			groups.append(j, group);
		}
		*/
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Get the view from fragmenttab1.xml
		 	View view = inflater.inflate(R.layout.fragment_tab_list, container, false);
		
		 	createDebugData();
			listView = (ExpandableListView) view.findViewById(R.id.listView);
			if (listView != null) {
				EventsExpandableAdapter adapter = new EventsExpandableAdapter(getActivity(), groups);
				listView.setAdapter(adapter);
			}
			else {
				Log.i("tabList", "listView = null");
			}
	        //Log.i("tabList", "created");
	        return view;
	}
	
	private SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.getDefault());
	private SimpleDateFormat sdfMonth = new SimpleDateFormat("MM", Locale.getDefault());
	private SimpleDateFormat sdfDay = new SimpleDateFormat("dd", Locale.getDefault());

	//return values: 0 (dates are equal), < 0 (d2 is less than d1), > 0 (d2 is greater)
	//util.Date methods are deprecated
	//need better date type, this is ugly!
	private int compareDates(Date d1, Date d2) {
		
		if (Integer.parseInt(sdfYear.format(d1)) != Integer.parseInt(sdfYear.format(d2))) {
			return Integer.parseInt(sdfYear.format(d2)) - Integer.parseInt(sdfYear.format(d1));
		}
		else if (Integer.parseInt(sdfMonth.format(d1)) != Integer.parseInt(sdfMonth.format(d2))) {
			return Integer.parseInt(sdfMonth.format(d2)) - Integer.parseInt(sdfMonth.format(d1));
		}
		else if (Integer.parseInt(sdfDay.format(d1)) != Integer.parseInt(sdfDay.format(d2))) {
			return Integer.parseInt(sdfDay.format(d2)) - Integer.parseInt(sdfDay.format(d1));
		}
		
		return 0;
		
	}
	
	private int getGroupIndex(Date date) {
		int compDate = -1;
		int i = 0;
		//Log.i("date", sdf.format(date));
		for (Group group : groups) {
			compDate = compareDates(date, group.getDate());
			
			Log.i("date comparison",  sdf.format(date) + " " + sdf.format(group.getDate()) + " " + String.valueOf(compDate));
			if (compDate > 0) {
				Group newGroup = new Group(date);
				groups.add(i, newGroup);
				return i;
			}
			else if (compDate == 0) {
				return i;
			}
			else {
				i++;
			}
		}
		Group newGroup = new Group(date);
		groups.add(i, newGroup);
		return i;
	}
	
	@Override
	public void OnDataChanged(ArrayList<Event> events) {
		Log.i("tabList", "On data changed");
		this.events = events;
		
		// delete when data loading is corrected
		this.groups = new ArrayList<Group>();
		
		int i = -1;
		Date eDate = null;
		for (Event event : this.events) {
			eDate = event.getEventTime();
			if (eDate != null) {
				//Log.i("list event", sdf.format(eDate) + " " + event.getName());
				i = getGroupIndex(eDate);
				groups.get(i).getChildren().add(event.getName());
			}
		}
		
		/*
		//dirty hack :/
		
		if (listView == null) {
			listView = (ExpandableListView) view.findViewById(R.id.listView);
		}
		EventsExpandableAdapter adapter = new EventsExpandableAdapter(getActivity(), groups);
		listView.setAdapter(adapter);
		*/
		
		
	}
	 
	 @Override
	 public void onSaveInstanceState(Bundle outState) {
		 super.onSaveInstanceState(outState);
		 //setUserVisibleHint(true);
	 }
	 
}
