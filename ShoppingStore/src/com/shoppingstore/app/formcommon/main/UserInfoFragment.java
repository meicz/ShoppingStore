package com.shoppingstore.app.formcommon.main;

import java.util.ArrayList;
import java.util.List;
 
import com.shoppingstore.app.R;  
import com.shoppingstore.app.common.global.GlobalData; 
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx; 
import com.shoppingstore.app.formcommon.utils.ShopCartImageView;  
import com.shoppingstore.app.internal.request.QuanCenterRequest;
import com.shoppingstore.app.internal.request.UsercenterRequest;
import com.shoppingstore.app.internal.response.UsercenterResponse;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment; 
import android.view.LayoutInflater;
import android.view.View; 
import android.view.ViewGroup;  
import android.widget.TextView; 

public class UserInfoFragment extends FragmentEx {
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.user_info_layout, container, false);
		
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		
		View vl = setLeftLayout(R.id.header_bar, R.layout.header_back_layout);
		vl.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 popBackStack();
			 }
		}); 
		
		//添加购物车布局
		View vr = setRightLayout(R.id.header_bar, R.layout.header_shopcart_layout);
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
		
		//设置标题栏
		View vc = setCenterLayout(R.id.header_bar, R.layout.header_textview_layout);
		TextView textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText("个人信息");  
		
		//隐藏首页页页脚导航
		getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
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
		
	}

	@Override
	public void popBackStack() {
		// TODO Auto-generated method stub
		mBackHandledInterface.popBackStack();
	}
	
	@Override  
	public void onHiddenChanged(boolean hidden){
		super.onHiddenChanged(hidden);  
		if(hidden){
			//隐藏界面 
			getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.VISIBLE); 
		}
		else{
			//显示界面
			getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		Thread thread = new Thread(new Runnable()  
        {  
            @Override  
            public void run(){ 
            	
            	try {
            		
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });  
        thread.start(); 
	}
}
