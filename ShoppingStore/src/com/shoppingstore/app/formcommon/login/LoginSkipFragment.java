package com.shoppingstore.app.formcommon.login; 

import java.util.List;

import com.shoppingstore.app.R;
import com.shoppingstore.app.R.id;
import com.shoppingstore.app.R.layout; 
import com.shoppingstore.app.formcommon.main.ShopActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 跳过登录进入首页
 * @author meicunzhi
 * @date 2015-10-05 09:10:24
 */
public class LoginSkipFragment extends Fragment{  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login_skip_footer_layout, container, false);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState == null){
			TextView text_view = (TextView) getView().findViewById(R.id.textView_userName);
			ImageView skip_view = (ImageView) getView().findViewById(R.id.imageview_skip);  
			RelativeLayout layout_skip = (RelativeLayout) getView().findViewById(R.id.layout_skip); 
			
			OnClickListener click = new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();  
					 intent.setClass(LoginSkipFragment.this.getActivity(), ShopActivity.class);
		             startActivity(intent);  
		             LoginSkipFragment.this.getActivity().finish();
				}
				
			};
			text_view.setOnClickListener(click);
			skip_view.setOnClickListener(click);
			layout_skip.setOnClickListener(click);
		} 
	}
}
