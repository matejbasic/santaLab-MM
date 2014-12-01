package hr.foi.air.discountlocator;

import hr.foi.air.discountlocator.db.Discount;
import hr.foi.air.discountlocator.db.Store;
import hr.foi.air.discountlocator.fragments.DiscountDetailsFragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class DiscountsExpandableAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private LayoutInflater inflater;
	private ArrayList<Store> parentItems;
	private ArrayList<Discount> childItems;
	
	/*
	private Discount childDiscountToDelete;
	private Store storeToDelete;
	private int currentGroup;
	*/
//konstruktor
	public DiscountsExpandableAdapter(ArrayList<Store> parents,
			ArrayList<Discount> childern) {
		//u ELV imamo roditelje(StoreS) koji su vidljivi na poèetku, i djecu koja se prikazuju nakon klika na roditelje
		this.parentItems = parents;
		this.childItems = childern;
		
	}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.activity = activity;
		this.inflater = inflater;
	}
	/* u DLF,
		DiscountsExpandableAdapter adapter = new DiscountsExpandableAdapter(stores, discounts);
		adapter.setInflater( (LayoutInflater) getActivity().getSystemService(FragmentActivity.LAYOUT_INFLATER_SERVICE), getActivity());
		//zbog prikazivanja pop buttona...
		ExpandableListView expandableList = (ExpandableListView) getView().findViewById(R.id.list);
		
		if(expandableList != null) {
			expandableList.setAdapter(adapter);
		}	
		
		ListView - A view that shows items in a vertically scrolling list. The items come from the ListAdapter associated with this view.
		
		LayoutInflater - Instantiates a layout XML file into its corresponding View objects. 
						It is never used directly. Instead, use getLayoutInflater() or getSystemService(String) 
						to retrieve a standard LayoutInflater instance that is already hooked up to the current context 
						and correctly configured for the device you are running on. For example:

		LayoutInflater inflater = (LayoutInflater)context.getSystemService
		      (Context.LAYOUT_INFLATER_SERVICE);
		      
		return inflater.inflate(R.layout.fragment_list_layout, container, false);
	 */

	@SuppressLint("InflateParams")
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		TextView nameView = null;
		TextView descriptionView = null;
		TextView discountView = null;

	
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.discounts_item, null);//novi layout
			
		}
//dijelovi discounts_item.xml
		nameView = (TextView) convertView.findViewById(R.id.discount_name);
		descriptionView = (TextView) convertView.findViewById(R.id.discount_description);
		discountView = (TextView) convertView.findViewById(R.id.discount);
		
		//as childPosition represents a position of child referenced to 
		//given group, we can not use a childPosition as element position in list, but
		//we have to search for the element.
		
		Discount childDiscount = null;
		int position = 0;
		for (Discount discount : childItems) {//discount in childItems
			if (discount.getStoreId() == parentItems.get(groupPosition).getRemoteId())//groupPosition se prosljeðuje 
			{//pokušaj pronalaženja željenog popusta u listi na temelju groupPosition-a, childPositiona
				if (position == childPosition)
				{
					childDiscount = discount;
					break;
				}
				position++;
			}
		}
		//da bi ga zatim prikazali
		nameView.setText(childDiscount.getName());
		descriptionView.setText(childDiscount.getDescription());
		discountView.setText(childDiscount.getDiscount()+"");
		
		//convertView.setTag(childDiscount.getRemoteId());
		/*
		 * Sets the tag associated with this view. 
		 * A tag can be used to mark a view in its hierarchy and does not have to be unique within the hierarchy. 
		 * Tags can also be used to store data within a view without resorting to another data structure.
		 */		 
		convertView.setTag(new Object[]{childDiscount.getRemoteId(), groupPosition});
		convertView.setOnLongClickListener(new View.OnLongClickListener() {
	        @Override
	        public boolean onLongClick(final View view) {
	        	// create Yes/No dialog        	
	        	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
	        	dialogBuilder.setMessage(activity.getResources().getString(R.string.q_delete_discount))
	        		.setPositiveButton(activity.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
	        		    @Override
	        		    public void onClick(DialogInterface dialog, int which) {
	        		        switch (which){
	        		        case DialogInterface.BUTTON_POSITIVE:
	        		        	
	        		        	// remove child element from the list, and delete it from database
	        					Object[] tagData = (Object[]) view.getTag();

	        		        	Discount discountToDelete = null;
	        		        	for (Discount d : childItems) {
									if(d.getRemoteId() == (Long) tagData[0]){
										discountToDelete = d;
									}
								}
	        		        	childItems.remove(discountToDelete);
	        		        	discountToDelete.delete();
	   
	        		        	// if there are no more discounts, delete parent store 
	        		        	// (remove from list and delete from database)
	        		        	
	        		        	if(getChildrenCount((Integer)tagData[1]) == 0){
	        		        		Store s = ((Store)getGroup((Integer)tagData[1]));
	        		        		if(s.discounts().size() == 0){
	        		        			s.delete();
	        		        		}
	        		        		parentItems.remove(getGroup((Integer)tagData[1]));

	        		        	}
	        		        	
	        		        	// propagate changes
	        		        	notifyDataSetChanged();
	        		            break;
	        		        }
	        		    }
	        		})
	        		.setNegativeButton(activity.getResources().getString(R.string.no), null)
	        		.show();
	        	
	        	// return true to say "event handled" 
	            return true;
	        }
	    });
		
		convertView.setOnClickListener(onClickListener);
		return convertView;
	}
	
	
	// handle click event on dialog
	//private DialogInterface.OnClickListener 
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Object[] tagData = (Object[]) v.getTag();	
			// get the animation preference
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
			boolean animate_enabled = preferences.getBoolean("pref_animate", true);		
			
			Bundle args = new Bundle();
			args.putLong("id", (Long) tagData[0]);
			
			DiscountDetailsFragment ddf = new DiscountDetailsFragment();
			ddf.setArguments(args);
			// create a new transaction, i.e. an object which will switch between fragments 
			FragmentTransaction transaction = ((FragmentActivity) activity).getSupportFragmentManager().beginTransaction();
			// animate only if it is enabled in preferences
			if(animate_enabled){
				// set animations 
				transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right);
			}
			// replace the current list fragment with new details fragment
			transaction.replace(R.id.fragment_container, ddf);
			// add the current fragment to backstack so when you push back, it is back
			transaction.addToBackStack(null);
			// make the change
			transaction.commit();
		}
	}; 

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		TextView nameView = null;
		TextView descriptionView = null;
		//ImageView imageView = null;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.discounts_group, null);
		}
		
		nameView = (TextView) convertView.findViewById(R.id.store_name);
		descriptionView = (TextView) convertView.findViewById(R.id.store_description);
		//imageView = (ImageView) convertView.findViewById(R.id.logo);

		nameView.setText(parentItems.get(groupPosition).getName());
		descriptionView.setText(parentItems.get(groupPosition).getDescription());

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childItems.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childItems.get(childPosition).getRemoteId();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size = 0;
		
		for (Discount discount : childItems) {
			if (discount.getStoreId() == parentItems.get(groupPosition).getRemoteId())
			{
				size++;
			}
		}
		
		return size;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parentItems.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		if(parentItems == null)
			return 0;
		else 
			return parentItems.size();
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
	public long getGroupId(int groupPosition) {

		return parentItems.get(groupPosition).getRemoteId();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}
}