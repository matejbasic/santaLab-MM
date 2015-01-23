package net.neurolab.musicmap;

import android.support.v4.app.Fragment;

/**
 * @author Ljiljana
 * MapFragmentInterface interface is the one that MapFragments have to implement in order to be shown in MM app. It is used for communication between MM app and external map projects.
 */
public interface MapFragmentInterface {
	public Fragment getFragment();
}
