package com.shoppingstore.app.formcommon.utils;

import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shoppingstore.app.fragment.FragmentInfo;

/**
 * 管理Fragment
 * @author meicunzhi
 * @date 2015-10-11 上午 07:45:10
 *
 */
public class FragmentManagerEx {
	private FragmentActivity mFActivity;
	private FragmentManager mFManager; 
	private Context mContext; 
	private int mFActivityID; 
	private String mLaseFragmentName = "";
	private Stack mStack;
	Fragment ff;
	public String lastName = "";
	
	public FragmentManagerEx(Context context, int activityID){
		mContext = context;
		mFActivityID = activityID;
		mStack = new Stack();
		
		if(context != null){
			mFActivity = (FragmentActivity) context;
			mFManager = mFActivity.getSupportFragmentManager();
		}
	} 
	
	/**
	 * 添加进堆栈
	 * @param fragment
	 */
	public void pushStack(Object fragment){
		mStack.push(fragment);
	} 
	
	public void pushStack(String fragmentName){
		if(isAdd(fragmentName)){
			if(mStack.search(fragmentName) < 0){
				mStack.push(fragmentName);
			}
			else{
				mStack.remove(fragmentName);
				mStack.push(fragmentName);
			}
		}
		else{
			mStack.push(fragmentName);
			/*if(!mStack.isEmpty()){
				mLaseFragmentName = (String) mStack.peek();
				mStack.pop();
				if(!mStack.isEmpty())
					mLaseFragmentName = (String) mStack.peek();
				else
					mLaseFragmentName = "";
			} */
		}
	} 
	
	public void add(Boolean isBack, boolean isBackDestroy, Fragment fragment, String nowFragmentName){  
		//名称为空或和最后添加的相同就返回
		if(nowFragmentName == null || "".equals(nowFragmentName)) return; 
		if(nowFragmentName.equals(mLaseFragmentName)) { 
			return; 
		}
		
		//设置信息
		FragmentInfo fminfo = new FragmentInfo();
		fminfo.setName(nowFragmentName);
		fminfo.setBack(isBack);
		fminfo.setBackDestroy(isBackDestroy);
		
		//检查要添加的fragment和当前存在的fragment信息
		FragmentTransaction transaction = mFManager.beginTransaction();
		Fragment lastFragment =	mFManager.findFragmentByTag(mLaseFragmentName);  
		Fragment nowFragment = mFManager.findFragmentByTag(nowFragmentName);
		//判断要添加的fragment是否存在
		if(nowFragment == null){
			//判断有没有当前活动的Fragment，有的话要隐藏该Fragment再添加新的
			if(lastFragment == null){
				//添加新的
				transaction.add(mFActivityID, fragment, nowFragmentName);
			}
			else{
				//隐藏之前的再添加新的
				transaction.hide(lastFragment).add(mFActivityID, fragment, nowFragmentName);
			} 
		}
		else{
			if(lastFragment != null){
				transaction.hide(lastFragment).show(nowFragment);  
			}
			else{
				transaction.show(nowFragment);
			}
		} 
		
		//判断nowFragment是添加还是切换位置
		if(nowFragment == null){
			//将新添加的Fragment入堆栈
			pushStack(fminfo); 
		}
		else if(nowFragment != null && lastFragment != null) {
			//交换Fragment在堆栈中的位置
			switchStack(fminfo);
		}
		
		//将新添加的设置为当前活动的
		mLaseFragmentName = nowFragmentName;
		
		//提交更新
		transaction.commit();  
	}
	
	/**
	 * 删除
	 * @param fragmentNames
	 */
	public void deleteFragments(List<String> fragmentNames){  
    	//判断是否是空，空不处理
    	if(mStack.isEmpty()){
    		return;
    	}
    	FragmentTransaction transaction = mFManager.beginTransaction();
    	for(int i = 0; i < fragmentNames.size(); i++){
    		String name = fragmentNames.get(i);
    		for(int x = 0; x < mStack.size(); x++){
    			FragmentInfo now = (FragmentInfo) mStack.get(x);
    			if(name.equals(now.getName())){ 
    				Fragment nowf = mFManager.findFragmentByTag(name);
    				if(nowf != null){
    					mStack.remove(x);
    					transaction.remove(nowf);
    				} 
    			}
    		} 
    	}
    	
    	if(mStack.isEmpty()){
    		return;
    	}
    	FragmentInfo now = (FragmentInfo) mStack.peek();
    	Fragment nowf = mFManager.findFragmentByTag(now.getName());
    	if(nowf == null){
    		mLaseFragmentName = "";
    	}
    	else{
    		transaction.show(nowf).commit();
    		mLaseFragmentName = now.getName();
    	}
    	
    	/*//判断要删除的是否是第一个
    	FragmentInfo last = (FragmentInfo) mStack.peek(); 
    	if(fragmentName.equals(last.getName())){ 
    		Fragment lastf = mFManager.findFragmentByTag(last.getName());   
    		if(lastf == null) return;
    		
    		FragmentInfo now = (FragmentInfo) mStack.pop();	//弹出
    		Fragment nowf = mFManager.findFragmentByTag(now.getName());
    		if(nowf == null){
    			transaction.remove(lastf).commit();
    		}
    		else{
    			transaction.remove(lastf).show(nowf).commit();
    		}
    	}
    	else{
    		
    	} */
	}
	
	 
	public void outLoad(){  
    	//判断是否是空，空不处理
    	if(mStack.isEmpty()){
    		return;
    	} 
    	
    	FragmentTransaction transaction = mFManager.beginTransaction();
    	while(!mStack.isEmpty()){
    		FragmentInfo now = (FragmentInfo) mStack.pop();
			Fragment nowf = mFManager.findFragmentByTag(now.getName());
			if(nowf != null){ 
				transaction.remove(nowf);
			} 
    	} 
    	transaction.commit();
	}
	
	/**
	 * 交换在堆栈中的位置
	 * 移除后再添加到堆栈
	 * @param fminfo
	 */
	private void switchStack(FragmentInfo fminfo) {
		// TODO Auto-generated method stub
		mStack.remove(fminfo);	
		mStack.push(fminfo);
	}

	public void addold(Boolean isAddStack, Fragment fragment, String nowFragmentName){ 
		if(nowFragmentName == null || "".equals(nowFragmentName)) return; 
		if(nowFragmentName.equals(mLaseFragmentName)) { 
			return; 
		}
		
		FragmentTransaction transaction = mFManager.beginTransaction();
		Fragment lastFragment =	mFManager.findFragmentByTag(mLaseFragmentName);  
		Fragment nowFragment = mFManager.findFragmentByTag(nowFragmentName);
		if(nowFragment == null){
			if(nowFragment == null && lastFragment == null){
				transaction.add(mFActivityID, fragment, nowFragmentName);
			}
			else{
				transaction.hide(lastFragment).add(mFActivityID, fragment, nowFragmentName);
			} 
		}
		else{
			if(lastFragment != null){
				transaction.hide(lastFragment).show(nowFragment);  
			}
		} 
		if(isAddStack){
			pushStack(nowFragmentName);
			/*transaction.addToBackStack(nowFragmentName); 
			int cout = mFManager.getBackStackEntryCount();
			List<Fragment> l = mFManager.getFragments(); */
		}
		else{
			lastName = nowFragmentName;
		}
		
		mLaseFragmentName = nowFragmentName;
		transaction.commit(); 
		/*if(nowFragment != null && !"".equals(mLaseFragmentName)){  
			transaction.show(nowFragment).hide(lastFragment);  
			mFManager.popBackStack();
			transaction.add(mFActivityID, new FragmentBrand(), mLaseFragmentName); 
		}else{
			if(lastFragment != null)
				transaction.add(mFActivityID, fragment, nowFragmentName).hide(lastFragment);
			else
				transaction.add(mFActivityID, fragment, nowFragmentName); 
			if(isAddStack){
				transaction.addToBackStack(nowFragmentName); 
			}
			ff = fragment;
		}  
		 
		mLaseFragmentName = nowFragmentName;
		
		//set(mLaseFragmentName);
		transaction.commit(); */
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
	
	public Fragment findFragment(String fragmentName){
		return mFManager.findFragmentByTag(fragmentName);  
	}
	
	public void popBackStack(){
		FragmentTransaction transaction = mFManager.beginTransaction();
		mFManager.popBackStack();
		transaction.commit(); 
	}

	public String getLaseFragmentName() {
		return mLaseFragmentName;
	}

	public void setLaseFragmentName(String laseFragmentName) {
		this.mLaseFragmentName = laseFragmentName;
	}

	public Stack getStack() {
		return mStack;
	} 
}
