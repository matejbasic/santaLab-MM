package net.neurolab.musicmap;

import net.neurolab.musicmap.fragments.FragmentTabList;

import net.neurolab.musicmap.googlemaps.FragmentTabMap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    FragmentTabMap fragmentTabMap;
    FragmentTabList fragmentTabList;
 
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
    @Override
    public Fragment getItem(int itemNum) {
    	
        if (itemNum == 0) {
        	return getTabMap();
        }
        else {
        	return getTabList();
        }
        
    }
    
    public FragmentTabMap getTabMap() {
    	if (fragmentTabMap == null) {
    		fragmentTabMap = new FragmentTabMap(); 		
    	}
    	return fragmentTabMap;
    }
    
    public FragmentTabList getTabList() {
    	if(fragmentTabList == null) {
    		fragmentTabList = new FragmentTabList();
    	}
    	return fragmentTabList;
    }
    
    public void setTabList(FragmentTabList fragmentTabList) {
    	this.fragmentTabList = fragmentTabList;   	
    }
    public void setTabMap(FragmentTabMap fragmentTabMap) {
    	this.fragmentTabMap = fragmentTabMap;   	
    }
    
	@Override
    public int getCount() {
        return PAGE_COUNT;
    }
 
}
