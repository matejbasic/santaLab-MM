package net.neurolab.musicmap;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.PreferredLocation;
import net.neurolab.musicmap.db.User;
import net.neurolab.musicmap.dl.DataLoader.OnDataLoadedListener;
import net.neurolab.musicmap.fragments.FragmentTabList;
import net.neurolab.musicmap.fragments.FragmentTabMap;
import net.neurolab.musicmap.interfaces.MainView;
import net.neurolab.musicmap.ns.NotificationData;
import net.neurolab.musicmap.ns.NotificationService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

public class MainActivity extends SherlockFragmentActivity implements
		OnDataLoadedListener, MainView {

	private ActionBar mActionBar;
	private ViewPager mPager;
	private Tab tab;

	private FragmentTabMap ftm = null;
	private FragmentTabList ftl = null;

	// private Boolean dataLoaded = false;

	static Context context;
	static NotificationData<Object> notification;

	String[] menu;
	DrawerLayout dLayout;
	ListView dList;
	ArrayAdapter<String> adapter;

	SharedPreferences sharedPreferences;
	Editor editor;
	
	ArrayList<Event> events;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActiveAndroid.initialize(this);
		context = getApplicationContext();

		ftm = new FragmentTabMap();
		ftl = new FragmentTabList();


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
//KOD LOGIRANJA SPREMITI USERLOGIN - AKO SE RADI O FACEBOOK KORISNIKU ONDA U userID(User tablica) spremiti facebookId inaèe neki random broj
//NAKON SPREMANA U BAZU, STAVITI userId u shared preference POD NAZIVOM idUser 
		/*
		String userId = loadSavedPreferences();
		//obrisati - samo za probu kad nema logina 
		Log.i("user", userId);						
		if(userId.equals("NN")){
			userId="0";
		}
		//obrisati
		
		
		Long id = (long) 0;
		List<User> user = null;
		List<PreferredLocation> loc;
		ArrayList<PreferredLocation> l = null;
		ArrayList<User> u = null;

		try {
			user = new Select().from(User.class).where("facebookId = ?", userId)
					.execute();
			u = (ArrayList<User>) user;
		} catch (Exception e) {
			Log.i("getUserFromDB", e.toString());
		}
		
		if (u.size() > 0) {
			System.out.println(u.size());
			id = u.get(0).getId();
		}

		if (id != 0) {
			Log.i("mainActivity", "locations");
			try {
				loc = new Select().from(PreferredLocation.class)
						.where("idUser LIKE ?", id).execute();
				l = (ArrayList<PreferredLocation>) loc;
			} catch (Exception e) {
				Log.i("getPreferredLocationsFromDB", e.toString());
			}
			if (l.size() > 0) {				
				menu = new String[l.size()];
				for (int i = 0; i < l.size(); i++) {					
					menu[i] = l.get(i).getSingleLocation().getCity();
					System.out.println(menu[i]);
				}
			} else {
				menu = new String[] { getResources().getString(
						R.string.no_preferred_locations) };
			}
		} else {
			menu = new String[] { getResources().getString(
					R.string.no_preferred_locations) };
		}
		*/
		menu = new String[] {"Zagreb", "Oxford", "London", "Baltimore", "Rome", "Boston", "Madison"};
		dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		dList = (ListView) findViewById(R.id.left_drawer);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menu);
		dList.setAdapter(adapter);
		dList.setSelector(android.R.color.holo_blue_dark);
		dList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				dLayout.closeDrawers();
				// Bundle args = new Bundle();
				// args.putString("Menu", menu[position]);
				if (!(menu[position].equals("") && !menu[position]
						.equals(getResources().getString(
								R.string.no_preferred_locations)))) {
					savePreferences(menu[position]);	
				}
				Log.i("mainActivity", "onItemClick");
			}
		});

		Log.i("mainActivity", "on create end");
	}

	public String loadSavedPreferences() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String idUser = sharedPreferences.getString("idUser", "");		
		if (idUser.equalsIgnoreCase("")) {
			return "NN";
		} else
			return idUser;

	}

	void savePreferences(String location) {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();
		editor.putString("theLocation", location);
		editor.commit();
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
		int minutes = 5 * 60; // 5sati
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
			if (context != null) {
				notification = new NotificationData<Object>();
				notification.showNotification(context);
			}
		}

	}

	@Override
	public void navigateToSingleEvent(Long eventId) {
		// Toast.makeText(activity, children, Toast.LENGTH_SHORT).show();
		if (eventId != null) {
			Intent intent = new Intent(getApplicationContext(),
					EventActivity.class);
			intent.putExtra("eventId", eventId);

			startActivity(intent);
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// changes position of list indicator
		ftl.setIndicatorPosition();
	}

}
