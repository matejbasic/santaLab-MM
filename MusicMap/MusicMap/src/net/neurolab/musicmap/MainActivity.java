package net.neurolab.musicmap;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.dl.DataLoader;
import net.neurolab.musicmap.dl.DataLoaderDB;
import net.neurolab.musicmap.dl.DataLoaderMM;
import net.neurolab.musicmap.dl.DataLoader.OnDataLoadedListener;
import net.neurolab.musicmap.fragments.FragmentTabList;
import net.neurolab.musicmap.fragments.FragmentTabMap;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.ActiveAndroid;
import com.google.android.gms.internal.lt;


public class MainActivity extends SherlockFragmentActivity implements OnDataLoadedListener {

	private ActionBar mActionBar;
	private ViewPager mPager;
	private Tab tab;
	
	private FragmentTabMap ftm = null;
	private FragmentTabList ftl = null;	
	
	private OnDataChangedListener dataChangedList = null;
	private OnDataChangedListener dataChangedMap = null;
	
	private Boolean dataLoaded = false;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActiveAndroid.initialize(this);
        
        ftm = new FragmentTabMap();
        ftl = new FragmentTabList();
        dataChangedList = (OnDataChangedListener) ftl;
        dataChangedMap = (OnDataChangedListener) ftm;
        
        Log.i("fragments", "created");
        
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
        
        viewPagerAdapter.setTabMap(ftm);
        viewPagerAdapter.setTabList(ftl);
        mPager.setAdapter(viewPagerAdapter);
        
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        	
        	@Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
        	    mPager.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
 
            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {
            	mPager.setCurrentItem(tab.getPosition());
            }
        };
 
        tab = mActionBar.newTab().setText(R.string.map).setTabListener(tabListener);
        mActionBar.addTab(tab);
 
        tab = mActionBar.newTab().setText(R.string.list).setTabListener(tabListener);
        mActionBar.addTab(tab);
     
        Log.i("mainActivity", "on create end");
    }
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
	
		if (id == R.id.action_settings) {
		    Intent settingsActivity = new Intent(this, SettingsActivity.class);
			this.startActivity(settingsActivity);
		}
			return true;
		
	}
	
	
	
	@Override
	public void OnDataLoaded(ArrayList<Event> events) {		
		Log.i("mainActivitiy", "onDataLoaded");
		if(ftm != null) {
			ftm.OnDataChanged(events);
		}
		if (ftl != null) {
			ftl.OnDataChanged(events);
		}
	}
	
	public interface OnDataChangedListener{
		void OnDataChanged(ArrayList<Event> events);

	}
	
	
}
