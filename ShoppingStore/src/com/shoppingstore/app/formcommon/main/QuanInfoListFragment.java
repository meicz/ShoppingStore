package com.shoppingstore.app.formcommon.main;

import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.QuanCenterBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.Adapter.QuanInfoListAdapter;
import com.shoppingstore.app.formcommon.Adapter.QuanListAdapter;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.internal.request.QuanCenterRequest;
import com.shoppingstore.app.internal.response.QuanCenterResponse;

/**
 * 优惠券
 * @author meicunzhi
 * @date 2015-11-25 下午12:40:08
 */
public class QuanInfoListFragment extends FragmentEx {
	 
	List<QuanCenterBean> mQuanLists;
	private QuanListAdapter mQuanListAdapter;
	private GridView gridView;
	private String mType = "";
	private QuanInfoListAdapter mQuanInfoListAdapter;
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){ 
		@Override
		public void handleMessage(Message msg) {
			mLoadingProgress.hide();
			
			switch (msg.what) { 
			case FragmentEx.UPDATE_VIEW: {
				mQuanInfoListAdapter = new QuanInfoListAdapter(getActivity(), mQuanLists);
				gridView.setAdapter(mQuanInfoListAdapter);
			}
			case FragmentEx.HIDELOADINGPROGRESS: {
        		mLoadingProgress.hide();
        		break;
        	}
        	case FragmentEx.SHOWLOADINGPROGRESS: {
        		mLoadingProgress.show();
        		break;
        	}
			default:
				break;
			}
		}
	};
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.quan_info_list_layout, container, false);
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
		
		//设置标题栏
		View vc = setCenterLayout(R.id.header_bar, R.layout.header_textview_layout);
		TextView textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText("优惠券");
		
		gridView = (GridView) getView().findViewById(R.id.gridView1);    
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//隐藏首页页页脚导航
		getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		
		
		
		RadioButton radioButton_wsy = (RadioButton) getView().findViewById(R.id.radioButton_wsy); 
		RadioButton radioButton_ysy = (RadioButton) getView().findViewById(R.id.radioButton_ysy); 
		RadioButton radioButton_ygq = (RadioButton) getView().findViewById(R.id.radioButton_ygq); 
		
		radioButton_wsy.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mType = "1";
				loadData();
			}
			
		});
		
		radioButton_ysy.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mType = "2";
				loadData();
			}
			
		});
		
		radioButton_ygq.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mType = "3";
				loadData();
			}
			
		});
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
            	try{
            		mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
            		
            		//加载数据
            		String token = GlobalData.gUser.getUserToken();
            		QuanCenterRequest qreq = new QuanCenterRequest();
            		qreq.put("token", token);
         		  	qreq.put("type", mType);
         		  	QuanCenterResponse qres = qreq.doGet(QuanCenterResponse.class.getName());
         		  	mQuanLists = qres.getQuanCenterList();
					//启动更新线程
        			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW); 
            	}
            	catch(Exception e){
            		e.printStackTrace();
            	} finally {
					mCommodHandler.sendEmptyMessage(FragmentEx.HIDELOADINGPROGRESS);
				}
            }
        });  
        thread.start(); 
	}
}
