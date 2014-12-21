package net.neurolab.musicmap;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.dl.DataLoader.OnDataLoadedListener;
import net.neurolab.musicmap.fragments.FragmentTabList;
import net.neurolab.musicmap.fragments.FragmentTabMap;
import net.neurolab.musicmap.ns.NotificationData;
import net.neurolab.musicmap.ns.NotificationService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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

public class MainActivity extends SherlockFragmentActivity implements
		OnDataLoadedListener {

	private ActionBar mActionBar;
	private ViewPager mPager;
	private Tab tab;

	private FragmentTabMap ftm = null;
	private FragmentTabList ftl = null;

	private OnDataChangedListener dataChangedList = null;
	private OnDataChangedListener dataChangedMap = null;

	private Boolean dataLoaded = false;
	
	static Context context;
	static NotificationData<Object> notification;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActiveAndroid.initialize(this);
		context = getApplicationContext();
		
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
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				mPager.setCurrentItem(tab.getPosition());
			}
		};

		tab = mActionBar.newTab().setText(R.string.map)
				.setTabListener(tabListener);
		mActionBar.addTab(tab);

		tab = mActionBar.newTab().setText(R.string.list)
				.setTabListener(tabListener);
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
		if (ftm != null) {
			ftm.OnDataChanged(events);
		}
		if (ftl != null) {
			ftl.OnDataChanged(events);
		}
	}

	public interface OnDataChangedListener {
		void OnDataChanged(ArrayList<Event> events);

	}

	public void onResume() {

		super.onResume();

		System.out.println("onResume GLAVNE AKTIVNOSTI");

		/*
		 * SharedPreferences prefs = PreferenceManager
		 * .getDefaultSharedPreferences(this); int minutes =
		 * prefs.getInt("interval");
		 */
		int minutes = 5*60;//5sati
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent i = new Intent(this, NotificationService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		// PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0,
		// i, 0);
		am.cancel(pi);
		// by my own convention, minutes <= 0 means notifications are disabled
		if (minutes > 0) {
			am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime() + minutes * 60 * 1000,
					minutes * 60 * 1000, pi);
		}

	}

	public void notifyMe() {
		
	}

	public static void sendData(String str) {
		if (str.equalsIgnoreCase("updated")) {
			System.out.println(str);			
			 // create object
			notification = new NotificationData<Object>();
			notification.showNotification(context);
		}

	}

}
