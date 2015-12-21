package com.shoppingstore.app.formcommon.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import org.json.JSONException;
  
import com.shoppingstore.app.R; 
import com.shoppingstore.app.areainfo.CityModel;
import com.shoppingstore.app.areainfo.DistrictModel;
import com.shoppingstore.app.areainfo.ProvinceModel;
import com.shoppingstore.app.common.bean.ShopCartItemBean;
import com.shoppingstore.app.common.bean.SizeBean;
import com.shoppingstore.app.common.bean.SpdetailBean;
import com.shoppingstore.app.common.bean.User;
import com.shoppingstore.app.common.bean.UserAddressBean;
import com.shoppingstore.app.common.global.GlobalData;  
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.formcommon.Adapter.ShopCartItemAdapter;
import com.shoppingstore.app.formcommon.Adapter.UserAddressListAdapter;
import com.shoppingstore.app.formcommon.utils.BuyQuantityEvent; 
import com.shoppingstore.app.formcommon.utils.CallBackInterface;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.ImageRoundView;
import com.shoppingstore.app.formcommon.utils.ShopCartImageView; 
import com.shoppingstore.app.formcommon.utils.SizeButton;
import com.shoppingstore.app.internal.WebUtils;
import com.shoppingstore.app.internal.request.ShoppingCartRequest;
import com.shoppingstore.app.internal.request.SpdetailRequest;
import com.shoppingstore.app.internal.request.UserAddressRequest;
import com.shoppingstore.app.internal.response.ShoppingCartResponse;
import com.shoppingstore.app.internal.response.SpdetailResponse;
import com.shoppingstore.app.internal.response.UserAddressResponse;

import android.content.Context; 
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.View.OnClickListener; 
import android.view.ViewGroup; 
import android.widget.Button; 
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;
 
/**
 * 收货地址列表
 * 通过回调来显示最新添加的地址
 * @author meicunzhi
 * @date 2015-11-8 下午10:00:02
 */
public class UserAddressListFragment extends FragmentEx implements CallBackInterface {    
	
	private List<UserAddressBean> mUserAddress;
	private UserAddressListAdapter mUserAddressListAdapter; 
	private boolean isShowButton = false;	//是否显示按钮、选择按钮
	
	private Handler mCommodHandler = new Handler(){ 
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FragmentEx.UPDATE_VIEW:{
				GridView gridView =  (GridView) getView().findViewById(R.id.gridView_addressitem);  
				gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
				if(mUserAddressListAdapter == null){
					/*mUserAddress = new ArrayList<UserAddressBean>();
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());
					mUserAddress.add(new UserAddressBean());*/
					mUserAddressListAdapter = new UserAddressListAdapter(getActivity(), gridView, mUserAddress);
					mUserAddressListAdapter.setIsShowButton(isShowButton);
					gridView.setAdapter(mUserAddressListAdapter);  
				} 
				else{
					mUserAddressListAdapter.setIsShowButton(isShowButton);
					mUserAddressListAdapter.notifyDataSetChanged();
				}
				break;
			}
			case FragmentEx.UPDATE:{
				mUserAddressListAdapter.setmUserAddressList(mUserAddress);
				mUserAddressListAdapter.notifyDataSetChanged();
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
		return inflater.inflate(R.layout.useraddress_list_layout, container, false);
		
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);    
		 
		Bundle bundle = getArguments();
		if(bundle != null){
			isShowButton = bundle.getBoolean("isShowButton");
		}
		
		View vl = setLeftLayout(R.id.address_list_header_bar, R.layout.header_back_layout);
		vl.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 popBackStack();
			 }
		}); 
		
		//添加购物车布局
		View vr = setRightLayout(R.id.address_list_header_bar, R.layout.header_shopcart_layout);
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
		View vc = setCenterLayout(R.id.address_list_header_bar, R.layout.header_textview_layout);
		TextView textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText("地址信息");  
		
		//添加地址
		Button button_add = (Button) getView().findViewById(R.id.button_add);
		button_add.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment();
				 
				 UserAddressFragment addressFragment = (UserAddressFragment) fm.findFragment(UserAddressFragment.class.getName()); 
				 if(addressFragment == null)
					 addressFragment = new UserAddressFragment(); 
				 
				 Bundle args = addressFragment.getArguments(); 
				 if(args == null){ 
					 args = new Bundle();
					 args.putString("addressId", ""); 
					 args.putString("isAddMrdz", ""); 
					 args.putString(FragmentEx.CALLBACKFRAGMENTNAME, UserAddressListFragment.class.getName()); 
					 addressFragment.setArguments(args);
				 }
				 else{  
					 args.putString("isAddMrdz", ""); 
					 args.putString(FragmentEx.CALLBACKFRAGMENTNAME, UserAddressListFragment.class.getName()); 
					 args.putString("addressId", "");  
				 }
				 fm.add(true, false, addressFragment, UserAddressFragment.class.getName());
			 }
		}); 
		 
		//隐藏首页页页脚导航
		getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		
		loadData();
		  
	}  
	
	private Drawable getDrawable(){
		ShapeDrawable bgdrawable =new ShapeDrawable(new OvalShape());
		bgdrawable.getPaint().setColor(getActivity().getResources().getColor(android.R.color.transparent));
		return bgdrawable;
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
			
			loadData();
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
	public void loadData(){
		Bundle bundle = getArguments();
		if(bundle != null){
			isShowButton = bundle.getBoolean("isShowButton");
		}
		
		Thread thread = new Thread(new Runnable(){
            @Override  
            public void run(){
            	try {
            		//重新加载数据
            		if(isAllRefresh){
            			User user = GlobalData.gUser;
            			String token = user.getUserToken();   
            			//获取所有收货地址
            			UserAddressResponse resa;
            			UserAddressRequest reqa = new UserAddressRequest();
            			reqa.put("token", token); 
            			reqa.put("mrdz", "0");
            			resa = reqa.getUserAddres(UserAddressResponse.class.getName());
            			mUserAddress = resa.getUserAddress();
            			if(resa.getStatus().equals("0") && resa.getResponseStatus() == 0){
            				//启动更新线程
            				mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW);
            			}  
            			
            			isAllRefresh = false;
                	}
                	else{
                		//刷新部分数据
                		//mUserAddressListAdapter.notifyDataSetChanged();
                	} 
            	} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
            }
        });  
        thread.start();  
	}

	@Override
	public boolean callBack(Object object) {
		// TODO Auto-generated method stub
		List<UserAddressBean> userAddList = (List<UserAddressBean>) object;
		mUserAddress.clear();
		for(int i = 0; i < userAddList.size(); i++){
			UserAddressBean userAddress = userAddList.get(i);
			mUserAddress.add(userAddress);
		}
		mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE);
		
		return true;
	}
	 
}
