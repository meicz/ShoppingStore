package com.shoppingstore.app.formcommon.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import com.shoppingstore.app.R;  
import com.shoppingstore.app.common.global.GlobalData; 
import com.shoppingstore.app.formcommon.Adapter.SalesdhThAdapter;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx; 
import com.shoppingstore.app.formcommon.utils.ShopCartImageView;  
import com.shoppingstore.app.internal.request.SalesdhRequest;
import com.shoppingstore.app.internal.request.SalesdhthRequest;
import com.shoppingstore.app.internal.response.SalesdhResponse;
import com.shoppingstore.app.internal.response.SalesdhthResponse;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment; 
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View; 
import android.view.ViewGroup;   
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView; 
import android.widget.Toast;

public class SalesdhThFragment extends FragmentEx {
	 private static final String[] arrModel = {"质量问题，货品存在瑕疵（划痕、胶水等）", "穿着效果与想象不符", "与描述不符", "尺码不合适", "未在指定日期内发货", "气味太重", "7天无理由退货"};
	 private ArrayAdapter<String> mSalesdhThAdapter; 
	 private Spinner mSpinner;
	 
	 private String thid = "";
	 private String allowreturn = "";
	 private int index = -1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.salesdhth_layout, container, false);
		
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		
		Bundle bundle = getArguments();
		if(bundle != null){
			thid = (String) bundle.get("id");
			allowreturn = (String) bundle.get("allowreturn");
			index = bundle.getInt("index");
		} 
		
		View vl = setLeftLayout(R.id.header_bar, R.layout.header_back_layout);
		vl.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 popBackStack();
			 }
		}); 
		  
		//设置标题栏
		View vc = setCenterLayout(R.id.header_bar, R.layout.header_textview_layout);
		TextView textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText("退货申请");    
		
		Button button_backsales = (Button) getView().findViewById(R.id.button_backsales);  
		button_backsales.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 if(index == -1) return; 
				 
				 final String sqthmem = mSpinner.getSelectedItem().toString();
				 if("".equals(sqthmem)){
					 Toast toast = Toast.makeText(getActivity(), "请选中退货原因", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 EditText edit_kdcode = (EditText) getView().findViewById(R.id.edit_kdcode); 
				 final String kdcode = edit_kdcode.getText().toString().trim();
				 if("3".equals(allowreturn)){ 
					 if("".equals(kdcode)){
						 Toast toast = Toast.makeText(getActivity(), "请输入快递单号", Toast.LENGTH_LONG);
						 toast.setGravity(Gravity.CENTER, 0, 0);
						 toast.show(); 
						 return;
					 }
				 }
				 
				 
				 final String tk = kdcode;
				 Thread thread = new Thread(new Runnable() {  
					 @Override  
					 public void run(){ 
						 try {
							 SalesdhthResponse res = new SalesdhthResponse();
							 SalesdhthRequest req = new SalesdhthRequest();
							 req.put("Token", GlobalData.gUser.getUserToken());
							 req.put("id", thid);
							 req.put("sqthmem", sqthmem);
							 if("3".equals(allowreturn)){
								 req.put("thexpressno", kdcode);
								 res = req.doPost(SalesdhthResponse.class.getName());
							 }
							 else{
								 res = req.doPut(SalesdhthResponse.class.getName());
							 }
							 if(res.isAllStatus()){
								 String tip = "";
								 if("".equals(tk)){
									 tip = "等待退款";
								 }
								 else{
									 tip = "等待退11111款";
								 }
								 String mm = res.getErrorMessage();
								 Map<String, Object> map = new HashMap<String, Object>();
								 map.put("index", index);
								 map.put("tip", tip);
								 mCallBackFragment.callBack(map); 
							 } 
						 } catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
					 }
				 });
				 thread.start();
				 
				 //mCallBackFragment.callBack(index);
			 }
		}); 
		
		LinearLayout layout_kdbar = (LinearLayout) getView().findViewById(R.id.layout_kdbar);  
		if("2".equals(allowreturn)){
			layout_kdbar.setVisibility(View.VISIBLE);
		} else if("3".equals(allowreturn)){
			layout_kdbar.setVisibility(View.VISIBLE);
		}
		
		mSpinner = (Spinner) getView().findViewById(R.id.spinner_sqthmem);  
		mSalesdhThAdapter = new SalesdhThAdapter(getActivity(), arrModel);
		mSpinner.setAdapter(mSalesdhThAdapter);
		
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
            	
            }
        });  
        thread.start(); 
	}
}
