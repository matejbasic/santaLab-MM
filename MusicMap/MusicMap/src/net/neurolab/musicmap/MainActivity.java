package net.neurolab.musicmap;

import java.util.ArrayList;

import net.neurolab.musicmap.db.Event;
import net.neurolab.musicmap.dl.DataLoader.OnDataLoadedListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements OnDataLoadedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
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
