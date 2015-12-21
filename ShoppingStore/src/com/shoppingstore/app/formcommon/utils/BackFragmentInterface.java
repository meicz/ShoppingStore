package com.shoppingstore.app.formcommon.utils;
 
import android.support.v4.app.Fragment;

public interface BackFragmentInterface {
	public void setNowtFragment(FragmentEx addFragment); 
	public void addFragment(Boolean isAddStack, int fragmentLayoutId, Fragment fragment, String nowFragmentName);  
	public void popBackStack();
}