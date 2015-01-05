package net.neurolab.musicmap;

import net.neurolab.musicmap.ws.YouTubeDataManager;
import net.neurolab.musicmap.ws.YouTubeDataResultHandler;
import net.neurolab.musicmap.ws.YouTubeDevKey;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class EventDetailsExpandableAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private LayoutInflater inflater;
	private int lastExpandedGroupPosition = -1;
	private ExpandableListView listView;
	private YouTubeDataManager ytDataManager;
	
	public EventDetailsExpandableAdapter(Activity activity, ExpandableListView listView) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
		//this.groups = groups;
		this.listView = listView;
	}
	
	@Override
	public int getGroupCount() {
		//return groups.size();
		return EventGroups.getInstance().listGroups.size();
	}
	
	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
		Object[] group = EventGroups.getInstance().listGroups.get(groupPosition);
		Log.i("adapter", String.valueOf(groupPosition) + " collapsed");
		try {
			group[3] = true;
			EventGroups.getInstance().listGroups.remove(groupPosition);
			EventGroups.getInstance().listGroups.add(groupPosition, group);
		}
		catch(Exception exc) {}
	}
	
	@Override
	public void onGroupExpanded(int groupPosition) {		
		if (groupPosition != lastExpandedGroupPosition) {
			listView.collapseGroup(lastExpandedGroupPosition);
		}
		super.onGroupExpanded(groupPosition);
		lastExpandedGroupPosition = groupPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return EventGroups.getInstance().listGroups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
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
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.event_details_list_group, null); //parent OR null?
		}
		
		Object[] group = EventGroups.getInstance().listGroups.get(groupPosition);
		if (group[0] == "event-details") {
			
			((CheckedTextView) convertView).setText(group[1].toString());
			//((CheckedTextView) convertView).setChecked(false);	
		}
		else if (group[0] == "musician-bio") {
			((CheckedTextView) convertView).setText("About " + group[1].toString());
		}
		
		return convertView;
	}

	private void setEventDetails(LinearLayout ll, View convertView, Object[] group) {
		TextView txtDesc = new TextView(convertView.getContext());
		txtDesc.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		if (!group[2].toString().isEmpty() && group[2].toString().trim().length() > 1) {
			txtDesc.setText(group[2].toString());
		}
		else {
			txtDesc.setText(R.string.eventDescDef);
		}
		
		ll.addView(txtDesc);
	}
	
	private void getYoutubeData(final LinearLayout ll, final Object[] group, final int groupPosition) {
		if(ytDataManager == null) {
			ytDataManager = new YouTubeDataManager();
		}
		//musician name = group[1].toString()
		ytDataManager.setDataTask(group[1].toString(), new YouTubeDataResultHandler() {
			
			@Override
			public void handleResult(String videoId) {
				if (!videoId.isEmpty()) {
					setYoutubePlayer(ll, group, groupPosition, videoId);
				}
				
			}
		});
	}
	
	private void setYoutubePlayer(LinearLayout ll, Object[] group, int groupPosition, String videoId) {
		//add youtube video
		if (!(Boolean)group[3]) {
			YouTubePlayerFragment fPlayer = new YouTubePlayerFragment();
			
			FragmentManager fm = activity.getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			ft.replace(ll.getId(), fPlayer);
			ft.commit();
					
			fPlayer.initialize(YouTubeDevKey.DEVELOPER_KEY, (OnInitializedListener) activity);
					
			group[3] = true;
			group[4] = fPlayer.hashCode();
			group[5] = videoId;
								
			EventGroups.getInstance().listGroups.remove(groupPosition);
			EventGroups.getInstance().listGroups.add(groupPosition, group);
		}
	}
	
	private void setAboutMusician(LinearLayout ll, View convertView, Object[] group, int groupPosition) {
		TextView txtDesc = new TextView(convertView.getContext());
		txtDesc.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		txtDesc.setPadding(0, 8, 0, 16);
		
		if (!group[2].toString().isEmpty() && group[2].toString().length() > 1) {
			txtDesc.setText(group[2].toString());
		}
		else {
			txtDesc.setText(R.string.no_bio_info);
		}
		
		ll.addView(txtDesc);
		
		
		getYoutubeData(ll, group, groupPosition);
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.event_details_list_item, null);
		}
		
		Object[] group = EventGroups.getInstance().listGroups.get(groupPosition);
		LinearLayout ll = (LinearLayout)convertView.findViewById(R.id.eventDetailsItem);
		ll.removeAllViews();
		
		if ( group[0] == "event-details") {
			setEventDetails(ll, convertView, group);
		}
		else if ( group[0] == "musician-bio") {
			setAboutMusician(ll, convertView, group, groupPosition);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
}