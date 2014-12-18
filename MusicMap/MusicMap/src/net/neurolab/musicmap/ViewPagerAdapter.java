package net.neurolab.musicmap;

import net.neurolab.musicmap.fragments.FragmentTabList;
import net.neurolab.musicmap.fragments.FragmentTabMap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
 
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
            //FragmentTabMap fragmentTabMap = FragmentTabMap.getInstance();
        	//return fragmentTabMap;
        	return getTabMap();
        }
        else {
            //FragmentTabList fragmentTabList = new FragmentTabList();
        	//return fragmentTabList;
        	return getTabList();
        }
        
    }
    
    public FragmentTabMap getTabMap() {
    	Log.i("vPagerAdapter getTabMap", String.valueOf(fragmentTabMap));
    	if (fragmentTabMap == null) {
    		fragmentTabMap = new FragmentTabMap(); 		
    	}
    	return fragmentTabMap;
    }
    
    public FragmentTabList getTabList() {
    	Log.i("vPagerAdapter getTabList", String.valueOf(fragmentTabList));
    	if(fragmentTabList == null) {
    		fragmentTabList = new FragmentTabList();
    	}
    	return fragmentTabList;
    }
    
    public void setTabList(FragmentTabList fragmentTabList) {
    	Log.i("vPagerAdapter setTabList", String.valueOf(fragmentTabList));
    	this.fragmentTabList = fragmentTabList;   	
    }
    public void setTabMap(FragmentTabMap fragmentTabMap) {
    	Log.i("vPagerAdapter setTabMap", String.valueOf(fragmentTabMap));
    	this.fragmentTabMap = fragmentTabMap;   	
    }
    
	@Override
    public int getCount() {
        return PAGE_COUNT;
    }
 
}
