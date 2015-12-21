package com.shoppingstore.app.formcommon.login; 

import java.util.List;

import com.shoppingstore.app.R;
import com.shoppingstore.app.R.id;
import com.shoppingstore.app.R.layout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 关闭注册
 * @author meicunzhi
 * @date 2015-10-06 09:18:24
 */
public class RegCloseFragment extends Fragment{  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login_reg_footer_close_layout, container, false);
		
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View close_view = getActivity().findViewById(R.id.include_reg_close);  
		close_view.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {  
				 getActivity().getSupportFragmentManager().popBackStack();
				 Fragment ref = getActivity().getSupportFragmentManager().findFragmentById(R.id.regframe);
				 FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				 fragmentTransaction.setCustomAnimations(R.anim.close, 0);   
				 fragmentTransaction.addToBackStack(null);
				 fragmentTransaction.show(ref).commit(); 
				 fragmentManager.popBackStack(); 
			 }
		});
	}
}
