package hr.foi.air.discountlocator.core;

import hr.foi.air.discountlocator.MainActivity;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class PositionProvider implements LocationListener {

	private int INTERVAL = 30000;
	private int ACCURACY = 20;

	public PositionProvider() {
	};

	public Location getLatestCoordinates(Activity activity) {
		Location location = null;
		LocationManager manager = (LocationManager) activity.getSystemService(MainActivity.LOCATION_SERVICE);
		boolean isLocationEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (isLocationEnabled) {
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, ACCURACY, this);
			location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		return location;
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
