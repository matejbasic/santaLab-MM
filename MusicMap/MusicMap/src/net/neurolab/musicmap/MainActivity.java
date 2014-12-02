package net.neurolab.musicmap;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.db.Location;
import net.neurolab.musicmap.dl.DataLoader.OnDataLoadedListener;
import net.neurolab.musicmap.ws.JSONAdapter;
import net.neurolab.musicmap.ws.MMAsyncResultHandler;
import net.neurolab.musicmap.ws.MMAsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;


public class MainActivity extends SherlockFragmentActivity implements OnDataLoadedListener {

	ActionBar mActionBar;
	ViewPager mPager;
	Tab tab;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.activity_main);
        
        //********************************************************************************
        
		MMAsyncTask asyncTaskStores = new MMAsyncTask();
		Object params[] = new Object[] { "getEvents", "add", null,
				handleResult, null, null };
		asyncTaskStores.execute(params); 
		
		//************************************************************************************
 
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

	private MMAsyncResultHandler handleResult = new MMAsyncResultHandler() {

		@Override
		public void handleResult(String result, Boolean status) {
			System.out.println(status.toString());

			ArrayList<Event> events = new ArrayList<Event>();
			ArrayList<Location> locations = new ArrayList<Location>();
			
			try {
				JSONAdapter.getEvents(result, events, locations);
				
			} catch (Exception e) {
				System.out.println(locations.size());
				e.printStackTrace();
			}
			
			
			for (int i = 0; i < locations.size(); i++) {
				System.out.println(locations.get(i).getLat());
			}

			/*
			 * ArrayList<Location> locations=null;
			 * 
			 * try { locations=JSONAdapter.getLocations(result);
			 * System.out.println(locations.size());
			 * //System.out.println(events.size()); } catch (Exception e) { //
			 * TODO Auto-generated catch block
			 * System.out.println("haveSomeProblems"); e.printStackTrace(); }
			 * 
			 * for(int i=0; i<locations.size(); i++){
			 * System.out.println(locations.get(i).getName());
			 * System.out.println(locations.get(i).getAddress()); }
			 */
			/*
			 * ArrayList<Event> events=null;
			 * 
			 * try { events=JSONAdapter.getEvents(result);
			 * System.out.println(events.size());
			 * //System.out.println(events.size()); } catch (Exception e) { //
			 * TODO Auto-generated catch block
			 * System.out.println("haveSomeProblems"); e.printStackTrace(); }
			 * 
			 * for(int i=0; i<events.size(); i++){
			 * System.out.println(events.get(i).getEventId());
			 * System.out.println(events.get(i).getEventTime()); }
			 */

			// String jsonStr =
			// "[{\"No\":\"1\",\"Name\":\"ABC\"},{\"No\":\"2\",\"Name\":\"PQR\"},{\"No\":\"3\",\"Name\":\"XYZ\"}]";
			// JSONAdapter js = new JSONAdapter();
			/*
			 * try { //results = new JSONObject(result); JSONArray array = new
			 * JSONArray(result);
			 * 
			 * for(int i=0; i<array.length(); i++){ JSONObject jsonObj =
			 * array.getJSONObject(i);
			 * System.out.println(jsonObj.getString("EventId"));
			 * System.out.println(jsonObj.getString("name"));
			 * 
			 * try { System.out.println(js.ConvertToTimestamp(jsonObj.getString(
			 * "lastEdited"))); } catch (ParseException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } } } catch
			 * (JSONException e) { e.printStackTrace(); }
			 */
			// "lastEdited\":\"2014-11-26T19:25:55.507\"

			// js.ConvertToTimestamp("lastEdited\":\"2014-11-26T19:25:55.507\");

			// if(results!=null) Toast.makeText(getApplicationContext(),
			// "not null", Toast.LENGTH_LONG).show();
			// else Toast.makeText(getApplicationContext(), "null",
			// Toast.LENGTH_LONG).show();
			// Toast.makeText(getApplicationContext(), tmp[0].toString(),
			// Toast.LENGTH_LONG).show();
			// Toast.makeText(getApplicationContext(), result,
			// Toast.LENGTH_LONG).show();
		}

	};
	
	@Override
	public void OnDataLoaded(ArrayList<Event> events) {
		// data updated: (either from database, search or web service)
		// raise the event for GoogleMapsFragment ;) so it updates the ExpandableListView   !!!!!!!!!!!!!!
		// this is because DataLoader accepts Activity as an argument, and not Fragment
		// therefore MainActivity acts as a data collector for Fragments
		//if(dlf != null) dlf.OnDataChanged(stores, discounts);
		//if(dialog != null)
			//dialog.cancel();
	}
	
	//*****************************************************************************************
	/*
	public interface OnDataChangedListener{//su�elje -> implementirano u nekom fragmentu (npr po�etnom koji prikazuje kartu)
		void OnDataChanged(ArrayList<Event> events);
	}*/
}
