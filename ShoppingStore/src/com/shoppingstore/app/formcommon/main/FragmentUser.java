package com.shoppingstore.app.formcommon.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.shoppingstore.app.R;  
import com.shoppingstore.app.ShopMain;
import com.shoppingstore.app.common.bean.QuanCenterBean;
import com.shoppingstore.app.common.global.GlobalData;  
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx; 
import com.shoppingstore.app.formcommon.utils.ShopCartImageView; 
import com.shoppingstore.app.internal.request.QuanCenterRequest; 
import com.shoppingstore.app.internal.request.UsercenterRequest;
import com.shoppingstore.app.internal.response.QuanCenterResponse; 
import com.shoppingstore.app.internal.response.UsercenterResponse;
 
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;  
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup; 
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class FragmentUser extends FragmentEx {  
	private TextView textView_quan;
	private TextView textView_olgold;
	private TextView textView_name;
	private TextView textView_card;
	
	private String mUserName = ""; 	//用户名
	private String mQuanQty = "0"; 	//券数量
	private String mOlgold = "0.00";	//积分
	private Handler mCommodHandler = new Handler(){ 
		@Override
		public void handleMessage(Message msg) {
			mLoadingProgress.hide();
			
			switch (msg.what) {
			case FragmentEx.UPDATE_VIEW:{   
				textView_quan.setText(mQuanQty);
				textView_olgold.setText(mOlgold);
				textView_name.setText(mUserName);
				break;
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
		return inflater.inflate(R.layout.user_layout, container, false);
		
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		
		View vl = setLeftLayout(R.id.include1, R.layout.header_list_layout);
		vl.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {  
			 }
		}); 
		
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
		
		//设置标题栏
		View vc = setCenterLayout(R.id.include1, R.layout.header_textview_layout);
		TextView textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText("我的乐途惠"); 
		
		OnClickListener userInfoAddRess = new OnClickListener(){
			public void onClick(View v) {
				GlobalData app = (GlobalData) getActivity().getApplication();
				FragmentManagerEx fm = app.getFragment();
				UserAddressListFragment addressListFragment = (UserAddressListFragment) fm.findFragment(UserAddressListFragment.class.getName());
				if(addressListFragment == null)
					addressListFragment = new UserAddressListFragment(); 
					
				Bundle bundle = addressListFragment.getArguments();
				if(bundle == null){
					bundle = new Bundle(); 
					bundle.putBoolean("isShowButton", false);
					addressListFragment.setArguments(bundle);
				}
				else{
					bundle.putBoolean("isShowButton", false);
				}  
				fm.add(true, false, addressListFragment, UserAddressListFragment.class.getName()); 
			}
		};		 
		RelativeLayout imageView_address = (RelativeLayout) getView().findViewById(R.id.imageView_address);
		TextView imageView_address1 = (TextView) getView().findViewById(R.id.imageView_address1);
		ImageView imageView_address2 = (ImageView) getView().findViewById(R.id.imageView_address2);
		imageView_address.setOnClickListener(userInfoAddRess); 
		imageView_address1.setOnClickListener(userInfoAddRess); 
		imageView_address2.setOnClickListener(userInfoAddRess); 
		
		OnClickListener userInfoClickEvent = new OnClickListener(){
			public void onClick(View v) {  
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment(); 
				 UserInfoFragment userinfo = new UserInfoFragment();
				 fm.add(true, false, userinfo, UserInfoFragment.class.getName());  
			 }
		};
		RelativeLayout imageView_userinfo = (RelativeLayout) getView().findViewById(R.id.imageView_userinfo);
		TextView imageView_userinfo1 = (TextView) getView().findViewById(R.id.imageView_userinfo1);
		ImageView imageView_userinfo2 = (ImageView) getView().findViewById(R.id.imageView_userinfo2);
		imageView_userinfo.setOnClickListener(userInfoClickEvent); 
		imageView_userinfo1.setOnClickListener(userInfoClickEvent); 
		imageView_userinfo2.setOnClickListener(userInfoClickEvent); 		
		
		
		OnClickListener userPassWordClickEvent = new OnClickListener(){
			public void onClick(View v) {  
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment(); 
				 UserPassWordFragment userpass = new UserPassWordFragment();
				 fm.add(true, false, userpass, UserPassWordFragment.class.getName());  
			 }
		};
		RelativeLayout textView_password = (RelativeLayout) getView().findViewById(R.id.textView_password);
		TextView textView_password1 = (TextView) getView().findViewById(R.id.textView_password1);
		ImageView textView_password2 = (ImageView) getView().findViewById(R.id.textView_password2);
		textView_password.setOnClickListener(userPassWordClickEvent); 
		textView_password1.setOnClickListener(userPassWordClickEvent); 
		textView_password2.setOnClickListener(userPassWordClickEvent); 
		
		OnClickListener userYhqClickEvent = new OnClickListener(){
			public void onClick(View v) {  
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment(); 
				 UserYhqFragment useryhq = new UserYhqFragment();
				 fm.add(true, false, useryhq, UserYhqFragment.class.getName());  
			 }
		};
		RelativeLayout textView_yhq = (RelativeLayout) getView().findViewById(R.id.textView_yhq);
		TextView textView_yhq1 = (TextView) getView().findViewById(R.id.textView_yhq1);
		ImageView textView_yhq2 = (ImageView) getView().findViewById(R.id.textView_yhq2);
		textView_yhq.setOnClickListener(userYhqClickEvent); 
		textView_yhq1.setOnClickListener(userYhqClickEvent); 
		textView_yhq2.setOnClickListener(userYhqClickEvent);  
		
		OnClickListener userOrderClickEvent = new OnClickListener(){
			public void onClick(View v) {  
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment(); 
				 OrderDetailListFragment orderList = new OrderDetailListFragment();
				 Bundle bundle = new Bundle();
				 bundle.putString("item", "1");
				 orderList.setArguments(bundle);
				 fm.add(true, false, orderList, OrderDetailListFragment.class.getName());  
			 }
		};
		RelativeLayout layout_order = (RelativeLayout) getView().findViewById(R.id.layout_order);
		TextView textView_order = (TextView) getView().findViewById(R.id.textView_order);
		ImageView imageView_order = (ImageView) getView().findViewById(R.id.imageView_order);
		layout_order.setOnClickListener(userOrderClickEvent); 
		textView_order.setOnClickListener(userOrderClickEvent); 
		imageView_order.setOnClickListener(userOrderClickEvent);  
		
		textView_quan = (TextView) getView().findViewById(R.id.textView_quan);
		textView_olgold = (TextView) getView().findViewById(R.id.textView_olgold);
		textView_name = (TextView) getView().findViewById(R.id.textView_name);
		textView_card = (TextView) getView().findViewById(R.id.textView_card);
		
		textView_quan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
		textView_olgold.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
		
		ImageView imageView_wzf = (ImageView) getView().findViewById(R.id.imageView_wzf);
		imageView_wzf.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GlobalData app = (GlobalData) getActivity().getApplication();
				FragmentManagerEx fm = app.getFragment(); 
				OrderDetailListFragment orderList = new OrderDetailListFragment();
				Bundle bundle = new Bundle();
				bundle.putString("item", "0");
				orderList.setArguments(bundle);
				fm.add(true, false, orderList, OrderDetailListFragment.class.getName() + "0");  
			}
			
		});
		
		
		//优惠券
		OnClickListener userQuanClickEvent = new OnClickListener(){
			public void onClick(View v) {  
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment(); 
				 QuanInfoListFragment orderList = new QuanInfoListFragment(); 
				 fm.add(true, true, orderList, QuanInfoListFragment.class.getName());  
			 }
		};
		TextView textView_quanbar = (TextView) getView().findViewById(R.id.textView_quanbar);
		TextView textView_ky = (TextView) getView().findViewById(R.id.textView_ky);
		textView_quanbar.setOnClickListener(userQuanClickEvent);
		textView_quan.setOnClickListener(userQuanClickEvent);
		textView_ky.setOnClickListener(userQuanClickEvent);
		
		Button button_backhome = (Button) getView().findViewById(R.id.button_backhome);
		button_backhome.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				outLoad();
				Intent intentpay = new Intent();  
				intentpay.setClass(getActivity(), ShopMain.class);
				startActivity(intentpay);   
				getActivity().finish();
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
		
	}
	
	@Override  
	public void onHiddenChanged(boolean hidden){
		super.onHiddenChanged(hidden);  
		if(hidden){
			//隐藏界面 
		}
		else{
			//显示界面 
			//读取数据
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
            	mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
            	
            	//获取可用优惠券数量 
				try {
					//加载数据
					if(isAllRefresh) {
						String token = GlobalData.gUser.getUserToken();
		        		UsercenterRequest userReq = new UsercenterRequest();
		        		userReq.put("token", token);
						UsercenterResponse userRes;
						userRes = userReq.doGet(UsercenterResponse.class.getName());
						mUserName = userRes.getData("name");
						mQuanQty = userRes.getData("quanQty");
						mOlgold = userRes.getData("olgold");
						if("".equals(mQuanQty))
							mQuanQty = "0"; 
						if("".equals(mOlgold))
							mOlgold = "0.00";  
						
						Message msg = new Message();
						msg.what = FragmentEx.UPDATE_VIEW; 
						mCommodHandler.sendMessage(msg);
						
						isAllRefresh = false;
					}
					else{
						//刷新数据	
					} 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					mCommodHandler.sendEmptyMessage(FragmentEx.HIDELOADINGPROGRESS);
				} 
				
            	/*String token = GlobalData.gUser.getUserToken();
            	QuanCenterRequest req = new QuanCenterRequest();  
            	req.put("token", token);
            	req.put("type", "1"); 
            	QuanCenterResponse res;
				try {
					res = req.doGet(QuanCenterResponse.class.getName());
					Message msg = new Message();
	            	msg.what = FragmentEx.UPDATE_VIEW;
	            	msg.obj = res.getQuanCenterList().size();
	            	mCommodHandler.sendMessage(msg);
	            	List<QuanCenterBean> quans = res.getQuanCenterList();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
            	
            }
        });  
        thread.start(); 
	}
}
