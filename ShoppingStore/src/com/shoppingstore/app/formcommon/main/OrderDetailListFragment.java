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
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.OrderDetailBean;
import com.shoppingstore.app.common.bean.OrderDetailBeans;
import com.shoppingstore.app.common.bean.QuanCenterBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.Adapter.OrderDetailAdapter;
import com.shoppingstore.app.formcommon.Adapter.ShopCartItemAdapter;
import com.shoppingstore.app.formcommon.utils.CallBackInterface;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.internal.WebUtils;
import com.shoppingstore.app.internal.request.SalesdhRequest;
import com.shoppingstore.app.internal.response.SalesdhResponse;

/**
 * 订单信息
 * @author meicunzhi
 * @date 2015-11-23 上午12:02:10
 */
public class OrderDetailListFragment extends FragmentEx implements CallBackInterface {
	 
	private List<OrderDetailBean> orderDetails;
	private OrderDetailAdapter orderDetailAdapter;
	private String mItem = "";		//单号类型
	GridView gridView;
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			mLoadingProgress.hide();
			
			switch (msg.what) { 
			case FragmentEx.UPDATE_VIEW: { 
				gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
				if(orderDetailAdapter == null){
					orderDetailAdapter = new OrderDetailAdapter(getActivity(), orderDetails);
					gridView.setAdapter(orderDetailAdapter);  
				}
				mLoadingProgress.hide();
			}
			case FragmentEx.UPDATE_COMMODITY_IMAGE: {
				orderDetailAdapter.notifyDataSetChanged();
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
		return inflater.inflate(R.layout.orderdetail_item_list_layout, container, false);
		
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
		textView_Title.setText("我的订单");  
		
		//隐藏首页页页脚导航
		getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		
		gridView =  (GridView) getView().findViewById(R.id.gridView1); 
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
            @Override  
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            	//将值传给回调函数去处理 
            	final OrderDetailBean orderDetail = orderDetails.get(position); 
            	RelativeLayout layout_look = (RelativeLayout) view.findViewById(R.id.layout_look);  
        		layout_look.setOnClickListener(new OnClickListener(){

        			@Override
        			public void onClick(View arg0) {
        				// TODO Auto-generated method stub 
        				String item = orderDetail.getItem();
        				Bundle bund = new Bundle();
        				bund.putString("item", item);
        				bund.putInt("position", position);
        				bund.putString(FragmentEx.CALLBACKFRAGMENTNAME, OrderDetailListFragment.class.getName());
        				OrderDetailFragment orderDstail = new OrderDetailFragment();
        				orderDstail.setArguments(bund); 
        				mFragmentManagerEx.add(true, true, orderDstail, item+"order.");
        			}
        		});
            }
            
        });
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
			isAllRefresh = true;
			loadData();
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		Bundle bundle = getArguments();
		if(bundle == null) return;
		mItem = bundle.getString("item");
		Thread thread = new Thread(new Runnable()  
        {  
            @Override  
            public void run(){
            	try{
            		mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
            		
            		//加载数据
                	if(isAllRefresh){
                		String token = GlobalData.gUser.getUserToken();
                		SalesdhRequest salesReq = new SalesdhRequest(); 
                		salesReq.put("token", token);
                		salesReq.put("item", mItem); 
                		SalesdhResponse salesRes = salesReq.doGet(SalesdhResponse.class.getName());
                		List<OrderDetailBean> orders = salesRes.getOrderDetail();
                		if(orders == null) return;
                		
                		if(orderDetails == null){
                			orderDetails = new ArrayList<OrderDetailBean>();
						}
                		
                		//显示信息
						for(int i = 0; i < orders.size(); i++){
							OrderDetailBean order = orders.get(i);
							String item = order.getItem();
							int updateRow = -1;
							for(int x = 0; x < orderDetails.size(); x++){
								OrderDetailBean oldorder = orderDetails.get(x); 
								String oldItem = oldorder.getItem();
								if(item.equals(oldItem)){
									List<OrderDetailBeans> orderBs = order.getOrderItems();
									List<OrderDetailBeans> oldorderBs = oldorder.getOrderItems();
									for(int y = 0; y < oldorderBs.size(); y++){
										OrderDetailBeans oldb = oldorderBs.get(y);
										OrderDetailBeans b = orderBs.get(y);
										Bitmap bmp = oldb.getImgBitmap();
										if(bmp != null){
											b.setImgBitmap(bmp);
										}
									}
									updateRow = x;
									break;
								}
							}
							if(updateRow < 0)
								orderDetails.add(order);
							else
								orderDetails.set(updateRow, order);
						}
						//启动更新线程
	        			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW); 
						
						isAllRefresh = false;
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

	@Override
	public boolean callBack(Object object) {
		// TODO Auto-generated method stub
		int index = Integer.valueOf(String.valueOf(object));
		OrderDetailBean order = orderDetails.get(index);
		order.setStatus("0");
    	orderDetailAdapter.notifyDataSetChanged();
    	
		return false;
	}
}
