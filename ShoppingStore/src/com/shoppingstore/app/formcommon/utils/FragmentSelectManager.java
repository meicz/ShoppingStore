package com.shoppingstore.app.formcommon.utils;

import java.util.Stack;

import com.shoppingstore.app.R; 

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

/**
 * 管理Fragment
 * @author meicunzhi
 * @date 2015-10-11 上午 07:45:10
 *
 */
public class FragmentSelectManager {
	private FragmentActivity mFActivity;
	private FragmentManager mFManager;  
	private int mFActivityID; 
	private String mLaseFragmentName = ""; 
	
	public FragmentSelectManager(Context context, int activityID){ 
		mFActivityID = activityID; 
		if(context != null){
			mFActivity = (FragmentActivity) context;
			mFManager = mFActivity.getSupportFragmentManager();   
			 
		}
	} 
	 
	public void add(Boolean isAddStack, Fragment fragment, String nowFragmentName){ 
		if(nowFragmentName == null || "".equals(nowFragmentName)) return; 
		if(nowFragmentName.equals(mLaseFragmentName)) return; 
		
		FragmentTransaction transaction = mFManager.beginTransaction();
		
		Fragment lastFragment =	mFManager.findFragmentByTag(mLaseFragmentName);  
		Fragment nowFragment = mFManager.findFragmentByTag(nowFragmentName);
		if(nowFragment != null && !"".equals(mLaseFragmentName)){  
			transaction.show(nowFragment).hide(lastFragment);   
		}else{
			if(lastFragment != null)
				transaction.add(mFActivityID, fragment, nowFragmentName).hide(lastFragment);
			else
				transaction.add(mFActivityID, fragment, nowFragmentName);  
		}  
		
		if(isAddStack){
			transaction.addToBackStack(nowFragmentName); 
		}  
		mLaseFragmentName = nowFragmentName; 
		transaction.commit(); 
	}
	
	public boolean isAdd(String fragmentName){
		Fragment fragment =	mFManager.findFragmentByTag(fragmentName);   
		return fragment == null ? false : fragment.isAdded();
	}
	
	public boolean isHidden(String fragmentName){
		Fragment fragment =	mFManager.findFragmentByTag(fragmentName);   
		return fragment == null ? false : fragment.isHidden();
	}
	
	public boolean isVisible(String fragmentName){
		Fragment fragment =	mFManager.findFragmentByTag(fragmentName);   
		return fragment == null ? false : fragment.isVisible();
	}
	
	public boolean isDetached(String fragmentName){
		Fragment fragment =	mFManager.findFragmentByTag(fragmentName);   
		return fragment == null ? false : fragment.isDetached();
	}
	
	public void replace(Boolean isPop, int srcID, Fragment fragment){
		replace(isPop, srcID, fragment, null); 
	}
	
	public void replace(Boolean isPop, int srcID, Fragment fragment, String tagName){ 
		FragmentTransaction transaction = mFManager.beginTransaction();
		transaction.replace(srcID, fragment);
		if(isPop){
			//transaction.addToBackStack(tagName);
		}
		transaction.commit();
	}
	
	public void popBackStack(){ 
		mFManager.popBackStack(); 
	}

	public String getLaseFragmentName() {
		return mLaseFragmentName;
	}

	public void setLaseFragmentName(String laseFragmentName) {
		this.mLaseFragmentName = laseFragmentName;
	} 
}
