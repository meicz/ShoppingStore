package com.shoppingstore.app.formcommon.utils;

import java.util.List;
import java.util.Stack;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.global.GlobalData; 
import com.shoppingstore.app.fragment.FragmentInfo;
 
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;

public class BackFragmentActivity extends FragmentActivity implements BackFragmentInterface{
	private FragmentManager mFManager; 
	private Stack mFragmentStack;
	private FragmentEx nowFragment;
	private FragmentSelectManager mSelectFragmen; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		mFManager = getSupportFragmentManager(); 
		mFragmentStack = new Stack();
	}
	 
	/**
	 * 由FragmentEx调用触发
	 */
	@Override
	public void setNowtFragment(FragmentEx addFragment) {
		// TODO Auto-generated method stub
		nowFragment = addFragment; 
		mFragmentStack.push(nowFragment);
	}  
	
	
 
	@Override
	public void addFragment(Boolean isAddStack, int fragmentLayoutId,
			Fragment fragment, String nowFragmentName) {
		// TODO Auto-generated method stub 
		if(mSelectFragmen == null){
			mSelectFragmen = new FragmentSelectManager(this, fragmentLayoutId);
		} 
		mSelectFragmen.add(isAddStack, fragment, nowFragmentName); 
	}

	@Override
	public void popBackStack() {
		// TODO Auto-generated method stub
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {  
		GlobalData app = (GlobalData)getApplication();
    	FragmentManagerEx mSelectFragmen = app.getFragment();
    	Stack stack = mSelectFragmen.getStack(); 
    	//判断是否是空，空不处理
    	if(stack.isEmpty()){
    		return;
    	}
    	
    	//获取堆栈中最后一个
    	FragmentInfo last = (FragmentInfo) stack.peek();
    	//判断是否能按后退返回
    	if(!last.isBack()){
    		//不能后退返回
    		return;
    	}
    	
    	//能后退返回
    	last = (FragmentInfo) stack.pop();	//弹出
    	Fragment lastf = mFManager.findFragmentByTag(last.getName());   
    	if(lastf != null){
    		FragmentEx fx = (FragmentEx) lastf;
    		//检查是否能返回
    		if(!fx.onBackPressed()){
    			//return;
    		}
    	}
    	
		FragmentTransaction transaction = mFManager.beginTransaction();
		if(FragmentActivity.class.getName().equals(last.getName())){
			String y = "";
			y=y;
		}
    	 
    	//显示后面一个
		FragmentInfo now = null;
    	Fragment nowf = null;
		if(!stack.isEmpty()){ 
			now = (FragmentInfo) stack.peek();
			nowf = mFManager.findFragmentByTag(now.getName());
			if(FragmentActivity.class.getName().equals(now.getName())){
				String y = "";
				y=y;
			}
		}
    	
    	if(lastf != null && nowf != null){ 
    		transaction.hide(lastf).show(nowf);
		}
    	if(last.isBackDestroy()){ 
			//返回删除
			if(lastf != null){ 
        		transaction.remove(lastf).commit();
    		}  
		}
		else if(lastf != null && nowf == null){ 
    		transaction.hide(lastf).commit(); 
		} else if(lastf != null && nowf != null){
			transaction.commit(); 
		}
    	if(now == null){
    		mSelectFragmen.setLaseFragmentName(""); 
    	}
    	else{
    		mSelectFragmen.setLaseFragmentName(now.getName()); 
    	}
	}
    	
	
	
 
	/*@Override
	public void onBackPressed() {  
		GlobalData app = (GlobalData)getApplication();
    	FragmentManagerEx mSelectFragmen = app.getFragment();
    	Stack s = mSelectFragmen.getStack(); 
    	if (s.isEmpty()) {
			super.onBackPressed();
		} else { 
			if(!s.isEmpty()){
	    		String last = (String) mSelectFragmen.getStack().pop();
	    		String now = "";
	    		if(!s.isEmpty()){
	    			now = (String) mSelectFragmen.getStack().peek();
	    		}  
	    		Fragment lastf = mFManager.findFragmentByTag(last);   
	    		Fragment nowf = mFManager.findFragmentByTag(now);   
	    		if(lastf != null && nowf != null){
	    			FragmentTransaction transaction = mFManager.beginTransaction();
	        		transaction.hide(lastf).show(nowf).commit();	
	        		mSelectFragmen.setLaseFragmentName(now); 
	    		} 
	    		else{
	    			mSelectFragmen.setLaseFragmentName(last); 
	    		} 
			}  
		} 
    	 
		
		if (mFManager.getBackStackEntryCount() == 0) {
			super.onBackPressed();
		} else { 
			if (nowFragment != null && !nowFragment.onBackPressed()) {
				if(nowFragment.isPush())
					mFManager.popBackStack();
				if(!mFragmentStack.isEmpty()){
					mFragmentStack.pop();
					if(!mFragmentStack.isEmpty()){
						nowFragment = (FragmentEx) mFragmentStack.peek();
						mSelectFragmen.setLaseFragmentName(nowFragment.getFragmentName());
					}
					else{
						nowFragment = null;
						mSelectFragmen.setLaseFragmentName("");
					}
				} 
				else{
					nowFragment = null;
					mSelectFragmen.setLaseFragmentName("");
				}
			}  
		} 
	}*/
}
