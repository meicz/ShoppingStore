package com.shoppingstore.app.formcommon.main; 
    
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.exception.CrashHandler;
import com.shoppingstore.app.formcommon.utils.BackFragmentActivity; 
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.RadioButtonEx;

public class ShopActivity extends BackFragmentActivity implements OnCheckedChangeListener { 
	private RadioGroup menuRedioGroup; 
	private FragmentEx nowFragment;
	private FragmentManagerEx fm;
	Fragment fragment;
	String lsfragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_layout);  
		
		//捕获异常
		CrashHandler crashHandler = CrashHandler.getInstance();  
		crashHandler.init(getApplicationContext());    
		
		initActivity();
	}
	
	public void initActivity(){
		menuRedioGroup = (RadioGroup)findViewById(R.id.tab_menu); 
		menuRedioGroup.setOnCheckedChangeListener(this);
		 
	    if(findViewById(R.id.activity_container) != null) { 
	    	nowFragment = new FragmentHome();  
	    	nowFragment.setPush(true);
	    	nowFragment.setFragmentName("home");
	    	fm = new FragmentManagerEx(this, R.id.activity_container);
	    	fm.add(false, false, nowFragment, FragmentHome.class.getName());
	    	GlobalData app = (GlobalData) getApplication();
	    	app.setFragment(fm);
	    	
	    	/*String name = "home";
	    	FragmentManager mFManager = getSupportFragmentManager();
	    	FragmentTransaction transaction = mFManager.beginTransaction();   
			fragment = mFManager.findFragmentByTag(name);
			transaction.add(R.id.activity_container, nowFragment, name);
			transaction.addToBackStack(name); 
			fragment = mFManager.findFragmentByTag(name);
			transaction.commit();
			int i=0;
			fragment = mFManager.findFragmentByTag(name);
			fragment=fragment;*/
	    	//addFragment(false, R.id.activity_container, nowFragment, "home");
	    	/*nowFragment = new FragmentHome(); 
	    	fm = new FragmentManagerEx(this, R.id.activity_container);
	    	fm.add(false, nowFragment, "home");
	    	GlobalData app = (GlobalData) getApplication();
	    	app.setFragment(fm);*/
            //getSupportFragmentManager().beginTransaction().add(R.id.activity_container, nowFragment, "home").commit();  
             
        }  
	    
	    RadioButtonEx butHome = (RadioButtonEx) findViewById(R.id.home);
	    butHome.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectChange(R.id.home);
			}
	    	
	    });
	    
	    RadioButtonEx butActivity = (RadioButtonEx) findViewById(R.id.activity);
	    butActivity.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectChange(R.id.activity);
			}
	    	
	    });
	    
	    RadioButtonEx butBrand = (RadioButtonEx) findViewById(R.id.brand);
	    butBrand.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectChange(R.id.brand);
			}
	    	
	    });
	    
	    RadioButtonEx butFavorite = (RadioButtonEx) findViewById(R.id.favorite);
	    butFavorite.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectChange(R.id.favorite);
			}
	    	
	    });
	    
	    RadioButtonEx butUser = (RadioButtonEx) findViewById(R.id.user);
	    butUser.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectChange(R.id.user);
			}
	    	
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shop_main, menu);
		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub 
		selectChange(checkedId);
	}
	 
	public void selectChange(int checkedId){
		FragmentManager mFManager = getSupportFragmentManager();
		switch(checkedId) {
		case R.id.home:
        	fragment = mFManager.findFragmentByTag(FragmentHome.class.getName());
        	if(fragment == null)
        		nowFragment = new FragmentHome(); 
        	else
        		nowFragment = (FragmentEx) fragment;
        	change(FragmentHome.class.getName());	            	
            break;
            
        case R.id.activity:
        	fragment = mFManager.findFragmentByTag(Fragmentactivity.class.getName());
        	if(fragment == null)
        		nowFragment = new Fragmentactivity(); 
        	else
        		nowFragment = (FragmentEx) fragment;
        	change(Fragmentactivity.class.getName());
            break;
            
        case R.id.brand:
        	fragment = mFManager.findFragmentByTag(FragmentBrand.class.getName());
        	if(fragment == null)
        		nowFragment = new FragmentBrand(); 
        	else
        		nowFragment = (FragmentEx) fragment;
        	change(FragmentBrand.class.getName());
            break;
            
        case R.id.favorite:
        	fragment = mFManager.findFragmentByTag(FragmentFavorite.class.getName());
        	if(fragment == null)
        		nowFragment = new FragmentFavorite(); 
        	else
        		nowFragment = (FragmentEx) fragment;
        	change(FragmentFavorite.class.getName());
            break;
            
        case R.id.user:
        	fragment = mFManager.findFragmentByTag(FragmentUser.class.getName());
        	if(fragment == null)
        		nowFragment = new FragmentUser(); 
        	else
        		nowFragment = (FragmentEx) fragment;
        	change(FragmentUser.class.getName());
            break;
		}
	}
	
	private void change(String nowActivity){ 
		fm.add(false, true, nowFragment, nowActivity);
		
		/*FragmentManager mFManager = getSupportFragmentManager();
		fragment = mFManager.findFragmentByTag(nowActivity);
		int cout = mFManager.getBackStackEntryCount();
		List<Fragment> l = mFManager.getFragments();
		 
		if(fragment != null)
			nowFragment = (FragmentEx) fragment;
		
    	FragmentTransaction transaction = mFManager.beginTransaction();   
		fragment = mFManager.findFragmentByTag(nowActivity);
		transaction.add(R.id.activity_container, nowFragment, nowActivity);
		if(fragment == null)
			transaction.addToBackStack(nowActivity); 
		 
		fragment = mFManager.findFragmentByTag(nowActivity);
		transaction.commit(); */
		//fm.add(false, nowFragment, nowActivity); 
		/*nowFragment.setPush(false);
    	nowFragment.setFragmentName(nowActivity);
		addFragment(false, R.id.activity_container, nowFragment, nowActivity);*/
		/*if(nowActivity != currentActivity){
			Fragment lastFragment =	getSupportFragmentManager().findFragmentByTag(currentActivity); 
			if(getSupportFragmentManager().findFragmentByTag(nowActivity) != null){ 
				Fragment fragmentNow = getSupportFragmentManager().findFragmentByTag(nowActivity);
				getSupportFragmentManager().beginTransaction()
				.show(fragmentNow)
				.hide(lastFragment)
				.commit();  
			}else{ 
				getSupportFragmentManager().beginTransaction()
				.add(R.id.activity_container, nowFragment, nowActivity) 
				.hide(lastFragment)
				.commit();
			}
			
			currentActivity = nowActivity;
		}*/
	} 
}
