package net.neurolab.musicmap.fragments;

import net.neurolab.musicmap.R;
import net.neurolab.musicmap.SetPreferencesActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class FragmentSetPreferedLocation extends Fragment {
	private EditText inputLocation;
	private ProgressBar progressBar;
	private Button next;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_prefered_location, container, false);
		progressBar = (ProgressBar)view.findViewById(R.id.pLocationProgressBar);
		inputLocation = (EditText)view.findViewById(R.id.inputPreferedLocation);
		inputLocation.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	
				String location = v.getText().toString();
				return checkLocation(location);
			}
		});
		
		next = (Button) view.findViewById(R.id.btnPrefNext);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String location = inputLocation.getText().toString();
				
				if (location == null) {
					EditText iLocationTemp = (EditText)view.findViewById(R.id.inputPreferedLocation);
					location = iLocationTemp.getText().toString();
				}
				checkLocation(location);	
			}
		});
		
		return view;
	}

	private boolean checkLocation(String location) {
		if (!location.isEmpty()) {
			((SetPreferencesActivity)getActivity()).checkLocation(location);
		
			inputLocation.setEnabled(false);
			progressBar.setVisibility(View.VISIBLE);
			return true;
		}
		else {
			setNoLocationError();
			return false;
		}
	}
	
	public void setNoLocationError() {
		progressBar.setVisibility(View.INVISIBLE);
		inputLocation.setEnabled(true);
		Toast.makeText(getActivity(), R.string.no_location_error, Toast.LENGTH_LONG).show();
		
	}
}
