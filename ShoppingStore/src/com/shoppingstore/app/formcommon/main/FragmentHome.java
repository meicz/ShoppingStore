package com.shoppingstore.app.formcommon.main;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.global.GlobalData; 
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.ImageRoundView;
import com.shoppingstore.app.formcommon.utils.ShopCartImageView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup; 
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FragmentHome extends FragmentEx {  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.home_layout, container, false);
		
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 
		ImageRoundView imageRoundView = (ImageRoundView)getActivity().findViewById(R.id.imageRoundView);
		imageRoundView.addImage(R.drawable.homead2x);
		imageRoundView.addImage(R.drawable.loginbg2x);
		imageRoundView.initUI(this.getActivity()); 
		 
		try {
			View vl = setLeftLayout(R.id.include1, R.layout.header_list_layout, this.getClass().getMethod("dd", null));
			vl.setOnClickListener(new View.OnClickListener(){
				 public void onClick(View v) {     
					 /*Intent intent = new Intent();
					 intent.setClass(FragmentHome.this.getActivity(), ShopActivity.class);
		             startActivity(intent);*/
				 }
			}); 
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//添加购物车布局
		View vr = setRightLayout(R.id.include1, R.layout.header_shopcart_layout);
		vr.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {  
				 ShoppingCartFragment shopCartFragment = new ShoppingCartFragment(); 
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment();
				 fm.add(true, true, shopCartFragment, ShoppingCartFragment.class.getName());
			 }
		});  
		//从布局中获取购物车
		mShopCart = (ShopCartImageView) vr.findViewById(R.id.shopCart_ImageView); 
		mShopCart.setmQuantity(GlobalData.gShopCartQty);
		
		View vc = setCenterLayout(R.id.include1, R.layout.header_logo_layout);
		vc.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
			 }
		}); 
		 
		//男士专区
		ImageView butMen = (ImageView) getActivity().findViewById(R.id.imageView_Men);
		butMen.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {     
				 /*Intent intent = new Intent();
				 intent.setClass(FragmentHome.this.getActivity(), FragmentCommodityCollection.class);
	             startActivity(intent);*/
				 /*Fragment lastFragment = FragmentHome.this.getActivity().getSupportFragmentManager().findFragmentByTag("home"); 
				 FragmentHome.this.getActivity().getSupportFragmentManager().beginTransaction()
					.add(R.id.activity_container, new FragmentCommodityCollection(), "xxxxx") 
					.hide(lastFragment)
					.commit();*/ 
				  
			    	Bundle bu = new Bundle(); 
			    	bu.putString("sex", "men");
			    	FragmentCommodityCollection men = new FragmentCommodityCollection();
			    	men.setPush(true);
			    	men.setFragmentName("mencommodity");
			    	men.setArguments(bu); 
			    	//addFragment(true, R.id.activity_container, men, "mencommodity");
			    	GlobalData app = (GlobalData) getActivity().getApplication();
			    	FragmentManagerEx fm = app.getFragment();
			    	fm.add(true, true, men, "mencommodity");
			 }
		});
		
		//女士专区
		ImageView butWomen = (ImageView) getActivity().findViewById(R.id.imageView_Women);
		butWomen.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {      
		    	Bundle bu = new Bundle(); 
		    	bu.putString("sex", "women");
		    	FragmentCommodityCollection women = new FragmentCommodityCollection();
		    	women.setPush(true);
		    	women.setFragmentName("womencommodity");
		    	women.setArguments(bu); 
		    	//addFragment(true, R.id.activity_container, women, "womencommodity");
		    	GlobalData app = (GlobalData) getActivity().getApplication();
		    	FragmentManagerEx fm = app.getFragment();
		    	fm.add(true, true, women, "womencommodity");
			}
		});  
	}
	
	public void dd(){
		int i=0;
		i=i;
	}

	@Override  
	public void onHiddenChanged(boolean hidden){
		super.onHiddenChanged(hidden);  
		if(hidden){
			//隐藏界面 
		}
		else{
			//显示界面
			mShopCart.setmQuantity(GlobalData.gShopCartQty);
		}
	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addFragment(Boolean isAddStack, int fragmentLayoutId,
			Fragment fragment, String nowFragmentName) {
		// TODO Auto-generated method stub  
		mBackHandledInterface.addFragment(isAddStack, fragmentLayoutId, fragment, nowFragmentName);
	}

	@Override
	public void popBackStack() {
		// TODO Auto-generated method stub
		mBackHandledInterface.popBackStack(); 
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	} 
}
