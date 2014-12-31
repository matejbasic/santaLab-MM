package net.neurolab.musicmap;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import net.neurolab.musicmap.R;
import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.fragments.Group;
import net.neurolab.musicmap.interfaces.MainView;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EventsExpandableAdapter extends BaseExpandableListAdapter {

	private final ArrayList<Group> groups;
	private MainView activity;
	private LayoutInflater infalter;
	private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd.MM.yyyy.", Locale.getDefault());
	
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
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final Event child = (Event) getChild(groupPosition, childPosition);
		TextView txtView = null;
		if (convertView == null) {
			convertView = this.infalter.inflate(R.layout.list_item, null);
		}
		
		txtView = (TextView) convertView.findViewById(R.id.itemTitle);
		txtView.setText(child.getName());
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
