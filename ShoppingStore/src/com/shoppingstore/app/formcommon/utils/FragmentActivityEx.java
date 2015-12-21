package com.shoppingstore.app.formcommon.utils; 
 
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * ����Fragment
 * @author meicunzhi
 * 2015-10-04 22:22:50
 */
public class FragmentActivityEx extends FragmentActivity{
	private Map<String, Fragment> activityFragmentMap = null; 
	
	public void initActivityFragmentMap(){
		if(activityFragmentMap == null){
			activityFragmentMap = new HashMap<String, Fragment>();
		}
	}
	
	public void put(String key, Fragment fragment){
		initActivityFragmentMap();		
		activityFragmentMap.put(key, fragment);
	}
	
	public void put(Map fragmentMap){
		initActivityFragmentMap();		
		Iterator iter = fragmentMap.keySet().iterator();
		 while(iter.hasNext()){
			 Entry entry = (Entry) iter.next();
			 activityFragmentMap.put(entry.getKey().toString(), (Fragment)entry.getValue());
		 } 
	} 
	
	 
	public void get(String key){
		activityFragmentMap.get(key);
	}
	
	public Map getActivityFragmentMap(){
		return activityFragmentMap;
	}
}
