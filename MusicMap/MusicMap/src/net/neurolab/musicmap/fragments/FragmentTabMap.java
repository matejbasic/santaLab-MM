package net.neurolab.musicmap.fragments;

import net.neurolab.musicmap.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentTabMap extends SherlockFragment {
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Get the view from fragmenttab2.xml
	        View view = inflater.inflate(R.layout.fragment_tab_map, container, false);
	        Log.i("tabMap", "created");
	        return view;
	    }
	 
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        setUserVisibleHint(true);
	    }
}
