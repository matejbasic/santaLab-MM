package net.neurolab.musicmap.fragments;

import net.neurolab.musicmap.R;
import net.neurolab.musicmap.SetPreferencesActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FragmentSetPreferedLocation extends Fragment {
	private EditText inputLocation = null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_prefered_location, container, false);
		
		inputLocation = (EditText)view.findViewById(R.id.inputPreferedLocation);
		inputLocation.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				inputLocation.setHint("");
				return false;
			}
		});
		inputLocation.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				inputLocation.setHint("");
				
			}
		});
		inputLocation.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//String location = inputLocation.getText().toString().trim();
				String location = v.getText().toString();
				((SetPreferencesActivity)getActivity()).saveLocation(location);
				return true;
			}
		});
		
		
		return view;
	}
}
