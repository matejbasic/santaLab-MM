package net.neurolab.musicmap;

import net.neurolab.musicmap.fragments.FragmentTabList;
import net.neurolab.musicmap.fragments.FragmentTabMap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
 
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
    @Override
    public Fragment getItem(int itemNum) {
    	
        if (itemNum == 0) {
            FragmentTabMap fragmentTabMap = new FragmentTabMap();
            return fragmentTabMap;
        }
        else {
            FragmentTabList fragmentTabList = new FragmentTabList();
            return fragmentTabList;
        }
        
    }
 
	@Override
    public int getCount() {
        return PAGE_COUNT;
    }
 
}
