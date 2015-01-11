package net.neurolab.musicmap.fragments;

import java.util.ArrayList;
import java.util.List;

import net.neurolab.musicmap.R;
import net.neurolab.musicmap.SetPreferencesActivity;
import net.neurolab.musicmap.db.Genre;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class FragmentSetPreferedGenres extends Fragment {
	
	private Button btnSelectAll;
	private Boolean selectAll = false;
	private Button btnDone;
	private List<CheckBox> checkBoxes;
	private List<Genre> genres;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_prefered_genres, container, false);	
		TableLayout table = (TableLayout) view.findViewById(R.id.genresTable);
		checkBoxes = new ArrayList<CheckBox>();
		
		genres = new Genre().getAll();
		for (Genre genre : genres) {
			//Log.i("genre", genre.getName());
			
			TableRow row = new TableRow(getActivity());
			TableRow.LayoutParams lParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
			row.setLayoutParams(lParams);
			
			CheckBox checkBox = new CheckBox(getActivity());
			checkBox.setText(genre.getName());
			
			checkBoxes.add(checkBox);
			row.addView(checkBox);
			
			table.addView(row);
		}
		
		//set relLayout height to screen height
		WindowManager wm = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenHeight = size.y;
		
		Log.i("screenH", String.valueOf(screenHeight));
		
		RelativeLayout gContainer = (RelativeLayout) view.findViewById(R.id.genresContainer);
		Log.i("gContainer H", String.valueOf(gContainer.getHeight()));
		Log.i("measure", "end");
		gContainer.setMinimumHeight((int)(screenHeight*0.9));
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		btnSelectAll = (Button) view.findViewById(R.id.btnSelectAll);
		btnDone = (Button) view.findViewById(R.id.btnDone);
		
		btnSelectAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectAll = !selectAll;
				if (selectAll) {
					btnSelectAll.setText(R.string.unselect_all);
				}
				else {
					btnSelectAll.setText(R.string.select_all);
				}
				for (CheckBox checkBox : checkBoxes) {
					checkBox.setChecked(selectAll);
				}
				
			}
		});
		
		btnDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int i = 0;
				List<Genre> tempGenres = new ArrayList<Genre>();
				for (CheckBox checkBox : checkBoxes) {
					if (checkBox.isChecked()) {
						tempGenres.add(genres.get(i));
					}
					i++;
				}
				((SetPreferencesActivity)getActivity()).setPreferedGenres(tempGenres);

			}
		});
		
		super.onViewCreated(view, savedInstanceState);
	}
}
