package net.neurolab.musicmap.fragments;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import net.neurolab.musicmap.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

public class EventsExpandableAdapter extends BaseExpandableListAdapter {

	private final ArrayList<Group> groups;
	private Activity activity;
	private LayoutInflater infalter;
	private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd.MM.yyyy.", Locale.getDefault());
	
	public EventsExpandableAdapter(Activity activity, ArrayList<Group> groups) {
		this.activity = activity;
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
	public Object getChild(int groupPosition, int childPosition) {
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

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		
		
		if (convertView == null) {
			convertView = infalter.inflate(R.layout.list_group, null);
		}
		Group group = (Group) getGroup(groupPosition);
		
		((CheckedTextView) convertView).setText(sdf.format(group.getDate()));
	
		//((CheckedTextView) convertView).setChecked(isExpanded);
		((CheckedTextView) convertView).setChecked(true);
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final String children = (String) getChild(groupPosition, childPosition);
		TextView txtView = null;
		if (convertView == null) {
			convertView = this.infalter.inflate(R.layout.list_item, null);
		}
		
		txtView = (TextView) convertView.findViewById(R.id.itemTitle);
		txtView.setText(children);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(activity, children, Toast.LENGTH_SHORT).show();
			}
		});
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
