package com.shoppingstore.app.formcommon.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;

public class PayOkFragment extends FragmentEx {
	 private String mItem = ""; 	//单号
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.payok_layout, container, false);
		
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		Bundle bundle = getArguments();
		if(bundle != null){
			mItem = bundle.getString("item");
		}
		
		View vl = setLeftLayout(R.id.include1, R.layout.header_back_layout);
		vl.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 popBackStack();
			 }
		}); 
		
		TextView textView_item = (TextView) getView().findViewById(R.id.textView_item); 
		textView_item.setText("您的订单号:" + mItem);
		
		Button button_dd = (Button) getView().findViewById(R.id.button_dd); 
		button_dd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bund = new Bundle();
				bund.putString("item", mItem);
				OrderDetailFragment orderDstail = new OrderDetailFragment();
				orderDstail.setArguments(bund); 
				mFragmentManagerEx.add(true, true, orderDstail, mItem+"order.");
			}
			
		});
		
		Button button_jxgw = (Button) getView().findViewById(R.id.button_jxgw); 
		button_jxgw.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		Button button_fx = (Button) getView().findViewById(R.id.button_fx);
		button_fx.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}); 
		 
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
		
		FragmentHome nowFragment = new FragmentHome();    
		mFragmentManagerEx.add(false, false, nowFragment, FragmentHome.class.getName()); 
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
            	
            }
        });  
        thread.start(); 
	}
}
