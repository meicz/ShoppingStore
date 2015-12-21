package com.shoppingstore.app.formcommon.main;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.OrderDetailBean;
import com.shoppingstore.app.common.bean.OrderDetailBeans;
import com.shoppingstore.app.common.bean.QuanCenterBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.Adapter.OrderDetailAdapter;
import com.shoppingstore.app.formcommon.Adapter.QuanListAdapter;
import com.shoppingstore.app.formcommon.Adapter.ShopCartItemAdapter;
import com.shoppingstore.app.formcommon.utils.CallBackInterface;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.internal.WebUtils;
import com.shoppingstore.app.internal.request.QuanCenterRequest;
import com.shoppingstore.app.internal.request.SalesdhRequest;
import com.shoppingstore.app.internal.response.QuanCenterResponse;
import com.shoppingstore.app.internal.response.SalesdhResponse;

/**
 * 优惠券
 * @author meicunzhi
 * @date 2015-11-25 下午12:40:08
 */
public class QuanSelectListFragment extends FragmentEx {
	 
	List<QuanCenterBean> mQuanLists;
	private QuanListAdapter mQuanListAdapter;
	private GridView gridView;
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
		private Handler mCommodHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				mLoadingProgress.hide();
				
				switch (msg.what) { 
				case FragmentEx.UPDATE_VIEW: { 
					if(gridView == null)
						gridView =  (GridView) getView().findViewById(R.id.gridView1);   
					if(mQuanListAdapter == null){
						mQuanListAdapter = new QuanListAdapter(getActivity(), mQuanLists);
						gridView.setAdapter(mQuanListAdapter);  
					} 
					else{
						mQuanListAdapter.setmQuanList(mQuanLists);
						mQuanListAdapter.notifyDataSetChanged();
					}
				}
				case FragmentEx.UPDATE_COMMODITY_IMAGE: {
					mQuanListAdapter.notifyDataSetChanged();
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
		return inflater.inflate(R.layout.quan_select_list_layout, container, false);
		
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
		textView_Title.setText("选择优惠券");
		
		gridView = (GridView) getView().findViewById(R.id.gridView1);   
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
            @Override  
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	//将值传给回调函数去处理
            	QuanCenterBean quanList = mQuanLists.get(position); 
            	ShoppingCartFragment callFragment = (ShoppingCartFragment) mFragmentManagerEx.findFragment(ShoppingCartFragment.class.getName());
            	if(callFragment.callBack(quanList))
            		popBackStack();
            }
            
        });
		
		LinearLayout layout_noyhq = (LinearLayout) getView().findViewById(R.id.layout_noyhq);   
		layout_noyhq.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 ShoppingCartFragment callFragment = (ShoppingCartFragment) mFragmentManagerEx.findFragment(ShoppingCartFragment.class.getName());
				 callFragment.callBack(null);
				 popBackStack();
			 }
		});  
		
		//隐藏首页页页脚导航
		getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		
		//加载数据
		loadData();
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
			
			//加载数据 
			loadData();
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
                	if(isAllRefresh){
                		String token = GlobalData.gUser.getUserToken();
                		QuanCenterRequest qreq = new QuanCenterRequest();
                		qreq.put("token", token);
             		  	qreq.put("type", "1");
             		  	QuanCenterResponse qres = qreq.doGet(QuanCenterResponse.class.getName());
             		  	mQuanLists = qres.getQuanCenterList();
						//启动更新线程
	        			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW); 
                	}
                	else{
                		//刷新数据
                	}
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
