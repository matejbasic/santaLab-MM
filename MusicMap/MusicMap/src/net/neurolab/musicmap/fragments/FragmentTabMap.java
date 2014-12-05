package net.neurolab.musicmap.fragments;

import net.neurolab.musicmap.R;
import net.neurolab.musicmap.dl.DataLoader;
import net.neurolab.musicmap.dl.DataLoaderDB;
import net.neurolab.musicmap.dl.DataLoaderSearch;
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
		View view = inflater.inflate(R.layout.fragment_tab_map, container,
				false);
		Log.i("tabMap", "created");

		// DataLoader dl = new DataLoaderMM();
		// DataLoader dl = new DataLoaderDB();
		// dl.LoadData(getActivity(), "Zagreb");

		DataLoaderSearch dl = new DataLoaderSearch();
		dl.LoadData(getActivity(), null);
		((DataLoaderSearch) dl).searchData("a");
		/*
		 * FWIW, Class.newInstance() will fail if the class to be instantiated
		 * is primitive (e.g. "int" or "float"), is an interface, is an array,
		 * or is abstract. These four items are identified in the rather obscure
		 * "newInstance failed: p0 i0 [0 a1" message, which in this case means
		 * "newInstance failed because it was asked to instantiate an abstract class"
		 * .
		 */

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}
}
