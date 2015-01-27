package net.neurolab.musicmap;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.fragments.Group;
import net.neurolab.musicmap.interfaces.MainView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

/**
 * Adapter that implements controls for list of events.<br/>
 * Extends BaseExpandableListAdapter.
 * @author Basic
 *
 */
public class EventsExpandableAdapter extends BaseExpandableListAdapter {

	private final ArrayList<Group> groups;
	private MainView activity;
	private LayoutInflater infalter;
	private SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd.MM.yy.", Locale.getDefault());
	
	public EventsExpandableAdapter(Activity activity, ArrayList<Group> groups) {
		this.activity = (MainView)activity;
		this.groups = groups;
		this.infalter = activity.getLayoutInflater();
	}
	
	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}
	
	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).getChildren().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public Event getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getChildren().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		
		
		if (convertView == null) {
			convertView = infalter.inflate(R.layout.events_list_group, null);
		}
		Group group = (Group) getGroup(groupPosition);
		
		((CheckedTextView) convertView).setText(sdf.format(group.getDate()));
		((CheckedTextView) convertView).setChecked(isExpanded);
		
		return convertView;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final Event child = (Event) getChild(groupPosition, childPosition);
		
		if (convertView == null) {
			convertView = this.infalter.inflate(R.layout.events_list_item, null);
		}
		
		String cityName = child.getIdLocation().getCity();
		StringBuilder sb = new StringBuilder();
		sb.append(cityName);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0))); 
		cityName = sb.toString();
		
		((TextView) convertView.findViewById(R.id.itemTitle)).setText(child.getName());
		((TextView) convertView.findViewById(R.id.itemLocation)).setText(child.getIdLocation().getName()
				+ ", " + cityName);
		
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.navigateToSingleEvent(child.getId());
			}
		});
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
