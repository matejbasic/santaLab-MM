package net.neurolab.musicmap;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.dl.DataLoader.OnDataLoadedListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class MainActivity extends SherlockFragmentActivity implements OnDataLoadedListener {

	ActionBar mActionBar;
	ViewPager mPager;
	Tab tab;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.activity_main);
     
        
        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mPager = (ViewPager) findViewById(R.id.pager);
        FragmentManager fm = getSupportFragmentManager();
 
        ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                
                mActionBar.setSelectedNavigationItem(position);
            }
        };
 
        mPager.setOnPageChangeListener(ViewPagerListener);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fm);
        mPager.setAdapter(viewPagerAdapter);
        
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        	
            
        	@Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
        	    mPager.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            	
            	
            }
 
            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {
            	mPager.setCurrentItem(tab.getPosition());
            }
        };
 
        tab = mActionBar.newTab().setText("Map").setTabListener(tabListener);
        mActionBar.addTab(tab);
 
        tab = mActionBar.newTab().setText("List").setTabListener(tabListener);
        mActionBar.addTab(tab);
 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
	
		if (id == R.id.action_settings) {
			// load the activity which displays the PreferenceFragment with 
			// users settings and preferences
		    Intent settingsActivity = new Intent(this, SettingsActivity.class);
			this.startActivity(settingsActivity);
		}
			return true;
		
	}
	
	@Override
	public void OnDataLoaded(ArrayList<Event> events) {
		
		//System.out.println("wooohooo");
		//System.out.println(events.size());
		
		// data updated: (either from database, search or web service)
		// raise the event for GoogleMapsFragment ;) so it updates the ExpandableListView   !!!!!!!!!!!!!!
		// this is because DataLoader accepts Activity as an argument, and not Fragment
		// therefore MainActivity acts as a data collector for Fragments
		//if(dlf != null) dlf.OnDataChanged(stores, discounts);
		//if(dialog != null)
			//dialog.cancel();
	}
	
	//*****************************************************************************************
	
	public interface OnDataChangedListener{
		void OnDataChanged(ArrayList<Event> events);
	}
	

}
