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
import com.shoppingstore.app.common.bean.SizeBean;
import com.shoppingstore.app.common.bean.SpdetailBean;
import com.shoppingstore.app.common.bean.User;
import com.shoppingstore.app.common.bean.UserAddressBean;
import com.shoppingstore.app.common.global.GlobalData;  
import com.shoppingstore.app.exception.BusException;
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
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.View.OnClickListener; 
import android.view.ViewGroup; 
import android.view.inputmethod.InputMethodManager;
import android.widget.Button; 
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;
 
/**
 * 收货地址
 * 添加、修改数据
 * 判断mAddressId是否为空，为空就是添加，不为空就是修改
 * @author meicunzhi
 * @date 2015-11-8 上午10:16:41
 */
public class UserAddressFragment extends AreaFragment implements OnWheelChangedListener {    
	 
	private PopupWindow popupWindow = null;
	
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	
	private LinearLayout popLayout; 
		
	private EditText editText_Shrname;
	private EditText editText_LinkPhone; 
	private EditText editText_address;
	private EditText editText_postCode; 
	private TextView editText_area; 
	
	private String mAddressId = "";	//要修改的地址ID
	private int mPosition;
	
	private String isAddMrdz = ""; 	//判断是否是从我的购物车跳转过来的，不为空的表示是是的。为空表示不是
	
	private UserAddressBean mUserAddress;	//用户信息
	
	private Handler mCommodHandler = new Handler(){ 
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FragmentEx.UPDATE_VIEW:{
				clearData();
				
				break;
			}
			case FragmentEx.UPDATE:{
				//修改，设置值
				if(mUserAddress == null) return;
				
				mCurrentProviceName = mUserAddress.getProvince();
				mCurrentProviceCode = mUserAddress.getProvinceCode();				
				mCurrentCityName = mUserAddress.getCity();
				mCurrentCityCode = mUserAddress.getCityCode();
				mCurrentDistrictName = mUserAddress.getCounty();
				mCurrentDistrictCode = mUserAddress.getCountyCode();  
				
				editText_Shrname.setText(mUserAddress.getShrname());
				editText_LinkPhone.setText(mUserAddress.getLinkPhone());
				editText_address.setText(mUserAddress.getAddress());
				editText_postCode.setText(mUserAddress.getPostCode()); 
				editText_area.setText(mCurrentProviceName + "		" + mCurrentCityName + "		" + mCurrentDistrictName); 
				
				Button button_addAddress = (Button) getView().findViewById(R.id.button_addAddress);
				if(mAddressId != null && !"".equals(mAddressId)){
					button_addAddress.setText("修改地址");
				} 
				
				//设置地址信息
				for(int i = 0; i < mPprovinceList.size(); i++){
					String proviceCode = mPprovinceList.get(i).getProvince().getCode();
					if(proviceCode.equals(mCurrentProviceCode)){
						mViewProvince.setCurrentItem(i);
						
						List<CityModel> citys = mPprovinceList.get(i).getCityList();
						for(int x = 0; x < citys.size(); x++){
							String cityCode = citys.get(x).getCity().getCode();
							if(cityCode.equals(mCurrentCityCode)){
								mViewCity.setCurrentItem(x);								
								List<DistrictModel> districts = citys.get(x).getDistrictList();
								for(int y = 0; y < districts.size(); y++){ 
									String districtsCode = districts.get(y).getDistrict().getCode();
									if(districtsCode.equals(mCurrentDistrictCode)){
										mViewDistrict.setCurrentItem(y);
										break;
									}
								}
								
								break;
							}
						}
						
						break;
					} 
				} 
				break;
			} 
			case FragmentEx.GOPAY:{ 
				popBackStack();
				UserAddressBean userAdd = (UserAddressBean) msg.obj;
				ShoppingCartFragment shopCart = (ShoppingCartFragment) mFragmentManagerEx.findFragment(ShoppingCartFragment.class.getName());
				shopCart.CallBackPay(userAdd);
			}
			default:
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.useraddress_layout, container, false);
		
	}  
	
	protected void clearData() {
		// TODO Auto-generated method stub
		editText_Shrname.setText("");
		editText_LinkPhone.setText("");
		editText_address.setText("");
		editText_postCode.setText("");
		editText_Shrname.setText("");
		editText_area.setText("");
		
		mCurrentProviceName = "";
		mCurrentProviceCode = "";				
		mCurrentCityName = "";
		mCurrentCityCode = "";
		mCurrentDistrictName = "";
		mCurrentDistrictCode ="";  
		
		mViewProvince.setCurrentItem(0);
		mViewCity.setCurrentItem(0);
		mViewDistrict.setCurrentItem(0);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);    
		
		View vl = setLeftLayout(R.id.address_header_bar, R.layout.header_back_layout);
		vl.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 popBackStack();
			 }
		}); 
		
		//初始化地区信息
		if(popupWindow == null){
			initPopupWindow();
		} 
		
		//添加购物车布局
		View vr = setRightLayout(R.id.address_header_bar, R.layout.header_shopcart_layout);
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
		View vc = setCenterLayout(R.id.address_header_bar, R.layout.header_textview_layout);
		TextView textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText("编辑地址");  
		
		//添加地址按钮事件
		Button button_addAddress = (Button) getView().findViewById(R.id.button_addAddress); 
		button_addAddress.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 final String shrname = editText_Shrname.getText().toString().trim();
				 if("".equals(shrname)) {
					 Toast toast = Toast.makeText(getActivity(), "请输入收货人!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 final String linkPhone = editText_LinkPhone.getText().toString().trim();
				 if("".equals(linkPhone)) {
					 Toast toast = Toast.makeText(getActivity(), "请输入手机号码!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 if(mCurrentProviceName.equals("") || mCurrentCityName.equals("") || mCurrentDistrictName.equals("")){
					 Toast toast = Toast.makeText(getActivity(), "请选择所在地区!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 final String address = editText_address.getText().toString().trim();
				 if("".equals(address)) {
					 Toast toast = Toast.makeText(getActivity(), "请输入详细地址!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 final String postCode = editText_postCode.getText().toString();
				 if("".equals(postCode)) {
					 Toast toast = Toast.makeText(getActivity(), "请输入邮政编码!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 //启动线程提交数据
				 new Thread(){  
					 @Override  
					 public void run(){ 
						try {
							
							UserAddressRequest reqa = new UserAddressRequest();
							reqa.put("token", GlobalData.gUser.getUserToken());
							reqa.put("Shrname", shrname);
							reqa.put("LinkPhone", linkPhone);
							reqa.put("province", mCurrentProviceName);
							reqa.put("provinceCode", mCurrentProviceCode);
							reqa.put("city", mCurrentCityName);
							reqa.put("cityCode", mCurrentCityCode);
							reqa.put("county", mCurrentDistrictName);
							reqa.put("countyCode", mCurrentDistrictCode);
							reqa.put("address", address);
							reqa.put("postCode", postCode);  
							UserAddressResponse res;
							if("".equals(mAddressId)){
								//添加，新添加的地址为默认地址
								res = reqa.addUserAddres(UserAddressResponse.class.getName());
							}
							else{
								//修改
								reqa.put("id", mAddressId);
								reqa.put("mrdz", mUserAddress.getMrdz()); 
								res = reqa.updateUserAddres(UserAddressResponse.class.getName());
							} 
							//请求成功
							if(res.isAllStatus()){
								//判断是否是从“我的购物车”跳转过来的，不为空的表示是的。为空表示不是
								List<UserAddressBean> userAdd = res.getUserAddress(); 
								Bundle bundle = getArguments();
								if(bundle != null){
									isAddMrdz = bundle.getString("isAddMrdz");
									if(isAddMrdz == null) isAddMrdz = "";
								} 
								if("".equals(isAddMrdz)){  
									if(mCallBackFragment != null){ 
										mCallBackFragment.callBack(userAdd);
										popBackStack();
									}
								}
								else{
									//跳回到“我的购物车”中去处理
									Message msg = new Message();
									msg.what = FragmentEx.GOPAY;
									msg.obj = userAdd.get(0);
									mCommodHandler.sendMessage(msg);
								} 
							} 
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
					 }
				 }.start(); 
			 }
		}); 
		
		editText_Shrname = (EditText) getView().findViewById(R.id.editText_Shrname); 
		editText_LinkPhone = (EditText) getView().findViewById(R.id.editText_LinkPhone);
		editText_address = (EditText) getView().findViewById(R.id.editText_address);
		editText_postCode = (EditText) getView().findViewById(R.id.editText_postCode);  
		editText_area = (TextView) getView().findViewById(R.id.editText_area);
		editText_area.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 if(popupWindow == null){
					 initPopupWindow();
				 } 
				 popupWindow.showAtLocation(getView(), Gravity.CENTER|Gravity.BOTTOM, 0, 0);
			 }
		}); 
		
		//隐藏首页页页脚导航
		getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		  
		//加载数据
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
			 
			Bundle bundle = getArguments();  
			mAddressId = bundle.getString("addressId"); 
			mPosition = bundle.getInt("position");
			
			//加载数据
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
	
	private void setUpViews() {
		mViewProvince = (WheelView) popLayout.findViewById(R.id.id_province);
		mViewCity = (WheelView) popLayout.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) popLayout.findViewById(R.id.id_district); 
	}
	
	private void setUpListener() {
    	// 添加change事件
    	mViewProvince.addChangingListener(this);
    	// 添加change事件
    	mViewCity.addChangingListener(this);
    	// 添加change事件
    	mViewDistrict.addChangingListener(this); 
    }
	
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7); 
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas(); 
		mViewProvince.setCurrentItem(0);
		mViewCity.setCurrentItem(0);
		mViewDistrict.setCurrentItem(0);
	}
	
	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
	
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			if(mDistrictDatasMap != null && !mDistrictDatasMap.isEmpty()){
				if("".equals(mCurrentCityName)) return;
				mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			} 
		}
	}
	 
	private void initPopupWindow() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popLayout = (LinearLayout) inflater.inflate(R.layout.area_layout, null, false);
		popupWindow = new PopupWindow(popLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setFocusable(true);  
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);   
		popupWindow.setBackgroundDrawable(getDrawable());
			 
		RelativeLayout layout_tm = (RelativeLayout) popLayout.findViewById(R.id.layout_tm);
		layout_tm.getBackground().setAlpha(99);
		
		TextView textView_cancel = (TextView) popLayout.findViewById(R.id.textView_cancel);
		TextView textView_ok = (TextView) popLayout.findViewById(R.id.textView_ok);
		textView_ok.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 getSelectedArea();
				 popupWindow.dismiss();
			 }
		}); 
		
		textView_cancel.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 popupWindow.dismiss();
			 }
		}); 
		
		//初始化地域
		setUpViews();
		setUpListener();
		setUpData();
	}
	
	private void getSelectedArea() {
		int pCurrent = mViewProvince.getCurrentItem();
		ProvinceModel province = mPprovinceList.get(pCurrent); 
		mCurrentProviceName = province.getProvince().getName();
		mCurrentProviceCode = province.getProvince().getCode();
		
		pCurrent = mViewCity.getCurrentItem();
		CityModel city = null;
		if(province.getCityList() != null && !province.getCityList().isEmpty()){
			city = province.getCityList().get(pCurrent);
			mCurrentCityName = city.getCity().getName();
			mCurrentCityCode = city.getCity().getCode();
		}
		else{
			mCurrentCityName = "";
			mCurrentCityCode = "";
		}
		
		
		pCurrent = mViewDistrict.getCurrentItem();
		DistrictModel distrinct = null;
		if(city.getDistrictList() != null && !city.getDistrictList().isEmpty()){
			distrinct = city.getDistrictList().get(pCurrent); 
			mCurrentDistrictName = distrinct.getDistrict().getName();
			mCurrentDistrictCode = distrinct.getDistrict().getCode();
		}
		else{
			mCurrentDistrictName = "";
			mCurrentDistrictCode = "";
		}
		
		 
		TextView editText_area = (TextView) getView().findViewById(R.id.editText_area);
		editText_area.setText(mCurrentProviceName + "		" + mCurrentCityName + "		" + mCurrentDistrictName); 
	}
	
	public void setUserAddress(UserAddressBean userAddress) {
		this.mUserAddress = userAddress;
	}
	
	@Override
	public void loadData() {
		Bundle bundle = getArguments();  
		if(bundle == null) return;
		
		//地址id不为空就是修改
		mAddressId = bundle.getString("addressId"); 
		mPosition = bundle.getInt("position");
		if(mAddressId == null || "".equals(mAddressId)){
			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW);
		} 
		else
			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE);
		
		Button button_addAddress = (Button) getView().findViewById(R.id.button_addAddress);
		if(mAddressId != null && !"".equals(mAddressId)){
			button_addAddress.setText("修改地址");
		}
		else{
			button_addAddress.setText("添加地址");
		}
	}
}
