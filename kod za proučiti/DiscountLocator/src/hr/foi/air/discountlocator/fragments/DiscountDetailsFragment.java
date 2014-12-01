package hr.foi.air.discountlocator.fragments;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.activeandroid.query.Select;

import hr.foi.air.discountlocator.R;
import hr.foi.air.discountlocator.db.Discount;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DiscountDetailsFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_details_layout, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		// get data from from bundle
		Bundle args = getArguments();
		long id = args.getLong("id");
		
		// use this id to get data from database and display it
		Discount d = new Select().from(Discount.class).where("remoteId == ?", id).executeSingle();
		
		// getView() gets the elements of the current fragments' view
		TextView name = ((TextView) getView().findViewById(R.id.discount_details_name));
		name.setText(d.getName() + ", " + d.getDiscount() + "%");
		
		TextView description = ((TextView) getView().findViewById(R.id.discount_details_description));
		description.setText(d.getDescription());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		
		TextView startDate = ((TextView) getView().findViewById(R.id.discount_details_start));
		startDate.setText(sdf.format(d.getStartDate()));
		
		TextView endDate = ((TextView) getView().findViewById(R.id.discount_details_end));
		endDate.setText(" - " + sdf.format(d.getEndDate()));
	}
	

	
}
