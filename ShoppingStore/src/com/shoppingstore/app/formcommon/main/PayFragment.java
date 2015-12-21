package com.shoppingstore.app.formcommon.main;
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.OrderDetailBeans;
import com.shoppingstore.app.common.bean.ShopCartItemBean;
import com.shoppingstore.app.common.bean.User;
import com.shoppingstore.app.common.bean.UserAddressBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.Adapter.ShopCartItemAdapter;
import com.shoppingstore.app.formcommon.utils.CallBackInterface;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.RadioButtonLayout;
import com.shoppingstore.app.formcommon.utils.SerializableMap;
import com.shoppingstore.app.internal.WebUtils;
import com.shoppingstore.app.internal.request.SalesdhRequest;
import com.shoppingstore.app.internal.request.ShoppingCartRequest;
import com.shoppingstore.app.internal.request.UserAddressRequest;
import com.shoppingstore.app.internal.response.SalesdhResponse;
import com.shoppingstore.app.internal.response.ShoppingCartResponse;
import com.shoppingstore.app.internal.response.UserAddressResponse;
 
/**
 * 我的购物车
 * 通过回调，获取选择的地址
 * @author meicunzhi
 * @date 2015-10-26 下午2:30:19
 */
public class PayFragment extends FragmentEx implements CallBackInterface {  
	private ShopCartItemAdapter shopCartAdapter;
	private List<ShopCartItemBean> shopCarts; 
	private double mAmount = 0;			//金额
	private String mItem = "";			//单号
	/**
	 * 获取Bundle对这些变量赋值
	 */
	private String mAddressId = ""; 	//收货地址ID	
	private String mQuan_id = "0";		//使用券的ID
	private String mUse_gold = "0";		//是否使用积分： 0不使用 1使用
	private String mPayType = "";		//付款类型
	double mQuan_amount = -1;  	//券金额
	double mUser_jf = -1;  		//使用积分
	private double myfAmount = 0;	//应该付款金额
	
	private double mTotalAmount = -1;	//实际付款金额
	//结束
	
	private ImageView imageView_imageUrl;
	private Button button_gopay;
	 
	List<RadioButtonLayout> radios = new ArrayList<RadioButtonLayout>();
		
	OnClickListener butClick = new View.OnClickListener(){ 
		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub 
			//设置选中
			for(int i = 0; i < radios.size(); i++){
				RadioButtonLayout b = (RadioButtonLayout) radios.get(i);
				if(b.equals(but)){ 
					/*if(b.getSelect()){ 
						b.setSelect(false); 
					}
					else{ 
						b.setSelect(true); 
					}*/
					
					//付款类型
					mPayType = (String) b.getTag();
					b.setSelect(true); 
				}
				else{ 
					b.setSelect(false);
				}
			}
				
		} 
	};  
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			mLoadingProgress.hide();
			
			switch (msg.what) { 
			case FragmentEx.UPDATE_VIEW: { 
				/*GridView gridView =  (GridView) getView().findViewById(R.id.gridView_shopcartitem);  
				if(shopCartAdapter == null){
					shopCartAdapter = new ShopCartItemAdapter(null, getActivity(), shopCarts);
					shopCartAdapter.setShowbutton(false);
					gridView.setAdapter(shopCartAdapter);   
				} 
				else{
					shopCartAdapter.setShopCartList(shopCarts);
					shopCartAdapter.notifyDataSetChanged();
				}*/
				
				LinearLayout linearlayout_commoditybar = (LinearLayout) getView().findViewById(R.id.linearlayout_commoditybar);	 
				//先清空以前界面上的内容
    			linearlayout_commoditybar.removeAllViews();
				LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
				for(int i = 0; i < shopCarts.size(); i++){
					View view = inflater.inflate(R.layout.commodity_item_layout, null, false); 
					final ImageView imageView_imageUrl = (ImageView) view.findViewById(R.id.imageView_imageUrl);
					TextView textView_commoditycode = (TextView) view.findViewById(R.id.textView_commoditycode);
					TextView textView_size = (TextView) view.findViewById(R.id.textView_size);
					TextView textView_quantity = (TextView) view.findViewById(R.id.textView_quantity);
					TextView textView_amount = (TextView) view.findViewById(R.id.textView_amount);
							
					final ShopCartItemBean shopCart = shopCarts.get(i);
					textView_commoditycode.setText(shopCart.getNameCN());
					textView_size.setText(shopCart.getSizeName());
					textView_quantity.setText(shopCart.getQuantity());
					textView_amount.setText("￥" + shopCart.getSalePrice()); 
					
					//加载图片资源 
        			Bitmap bitmap = shopCart.getBitmap();
        			if(bitmap == null){
        				new Thread(new Runnable() {  
        					@Override  
        					public void run() { 
        						String imgurl = shopCart.getImageUrl();
        						Bitmap bitmap = WebUtils.doGetBitmap(imgurl);
        						shopCart.setBitmap(bitmap);
        						
        						Message msg = new Message(); 
    							Map<String, Object> imgMap = new HashMap<String, Object>();
    							imgMap.put("imageBitmap", bitmap);
    							imgMap.put("imageView", imageView_imageUrl);
    							msg.obj = imgMap; 
    							msg.what = FragmentEx.UPDATE_COMMODITY_IMAGE;  
    							mCommodHandler.sendMessage(msg); 
        					}
        				}).start(); 
        			}
        			else
        				imageView_imageUrl.setImageBitmap(bitmap);  
					
					linearlayout_commoditybar.addView(view, i);
				}
				
				//显示统计金额
				setTotalAmount(parseMoney(mTotalAmount));
				
				//解禁支付按钮
				if(button_gopay != null) {
					button_gopay.setEnabled(true);
				}
				
				TextView textView_yhq = (TextView) getView().findViewById(R.id.textView_yhq);
				TextView textView_jf = (TextView) getView().findViewById(R.id.textView_jf);
				 
        		break;
			} 
			case FragmentEx.UPDATE_COMMODITY_IMAGE: {
				if(getView() == null){
					return;
				}
				
				Map<String, Object> imgMap = (Map<String, Object>) msg.obj; 
        		ImageView imageView_imageUrl = (ImageView) imgMap.get("imageView");
        		Bitmap bmp = (Bitmap) imgMap.get("imageBitmap");
        		imageView_imageUrl.setImageBitmap(bmp); 
				
        		break;
			}
			case FragmentEx.UPDATE: { 
				TextView textView_phone = (TextView) getView().findViewById(R.id.textView_phone);
				TextView textView_address = (TextView) getView().findViewById(R.id.textView_address);
				   
				UserAddressBean addr = (UserAddressBean) msg.obj;
				mAddressId = addr.getId();
				textView_phone.setText(addr.getShrname() + "	" + addr.getLinkPhone());
				String address = addr.getAddress();
				String post = addr.getPostCode();
				if(!"".equals(post)){
					address += "," + post;
				}
				textView_address.setText(address);
				
				//显示统计金额
				setTotalAmount(parseMoney(mTotalAmount));
				
				break;
			}
			case FragmentEx.GOPAY: { 
				//打开支付宝
				GlobalData app = (GlobalData) getActivity().getApplication();
				FragmentManagerEx fm = app.getFragment();
				
				Bundle bundle = new Bundle();
				bundle.putString("item", mItem);
				bundle.putDouble("fkje", mAmount);
				bundle.putString("tofragment", FragmentHome.class.getName());
				PayConfirmFragment payConfirm = new PayConfirmFragment();  
				payConfirm.setArguments(bundle);
				fm.add(true, true, payConfirm, PayConfirmFragment.class.getName());  
				 
				button_gopay.setEnabled(true);
				
				break;
			}
			case FragmentEx.CLEAR_SHOPCART: { 
				//情况购物车信息
				LinearLayout linearlayout_commoditybar = (LinearLayout) getView().findViewById(R.id.linearlayout_commoditybar);	  
    			linearlayout_commoditybar.removeAllViews(); 
    			setTotalAmount(parseMoney(0));
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
			case 999: {
				button_gopay.setEnabled(true);
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
		return inflater.inflate(R.layout.pay_layout, container, false);
	}  
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		View vl = setLeftLayout(R.id.shopcart_header_bar, R.layout.header_back_layout);
		vl.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 popBackStack();
			 }
		});  
		
		//设置标题栏
		View vc = setCenterLayout(R.id.shopcart_header_bar, R.layout.header_textview_layout);
		TextView textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText("我的购物袋"); 
		 
		button_gopay = (Button) getView().findViewById(R.id.button_gopay);
		button_gopay.setEnabled(false);
		button_gopay.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 button_gopay.setEnabled(false);
				 
				 if("".equals(mAddressId)){
					 button_gopay.setEnabled(true);
					 Toast toast = Toast.makeText(getActivity(), "请选择收货地址!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show();  
					 return;
				 }
				 
				 if("".equals(mPayType)){
					 button_gopay.setEnabled(true);
					 Toast toast = Toast.makeText(getActivity(), "请选择付款方式!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 if(mTotalAmount < 0){
					 button_gopay.setEnabled(true);
					 Toast toast = Toast.makeText(getActivity(), "数据还未加载成功，请稍后支付!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 } 
				 
				 //提交订单
				 Thread thread = new Thread(new Runnable(){
			            @Override  
			            public void run(){ 
			            	
							try {
								if("".equals(mAddressId)){
									return;
								}
								
								String token = GlobalData.gUser.getUserToken();
								SalesdhRequest salesReq = new SalesdhRequest(); 
								salesReq.put("token", token);
								salesReq.put("address_id", mAddressId);
								salesReq.put("quan_id", mQuan_id);
								salesReq.put("use_gold", mUse_gold);
								SalesdhResponse salesRes = salesReq.doPost(SalesdhResponse.class.getName());
								if(salesRes.isAllStatus()){ 
									mItem = salesRes.getItem();
									String fkje = salesRes.getData("fkje");
									if(fkje == null || "".equals(fkje)){
										fkje = "0";
									}
									mAmount = Double.valueOf(fkje);
									//1.打开支付宝
									mCommodHandler.sendEmptyMessage(FragmentEx.GOPAY);  
									
									//2.将购物车数量清0
									clearData(); 
									
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								mCommodHandler.sendEmptyMessage(999);  
							} 
			            } 
						
			        });  
			        thread.start();   
				 
				 //pay();
				 /*SalesdhRequest req = new SalesdhRequest();
				 String token = GlobalData.gUser.getUserToken();
				 req.put("token", token);
				 req.put("address_id", mAddressId);
				 req.put("quan_id", "-1");
				 SalesdhResponse res = req.doPost(SalesdhResponse.class.getName());*/
			 }
		});  
		
		RadioButtonLayout zfb = (RadioButtonLayout) getView().findViewById(R.id.pay_zfb);  
		zfb.setTag("zfb");
		radios.add(zfb);
		RadioButtonLayout wx = (RadioButtonLayout) getView().findViewById(R.id.pay_wx); 
		wx.setTag("wx");
		radios.add(wx);
		RadioButtonLayout zxzf = (RadioButtonLayout) getView().findViewById(R.id.pay_zxzf); 
		zxzf.setTag("zxzf");
		radios.add(zxzf);
		RadioButtonLayout hdfk = (RadioButtonLayout) getView().findViewById(R.id.pay_hdfk); 
		hdfk.setTag("hdfk");
		radios.add(hdfk);
		for(int i = 0; i < radios.size(); i++){
			radios.get(i).setOnClickListener(butClick);
		}  
		
		//收货地址
		ImageView imageView_butaddress = (ImageView)getView().findViewById(R.id.imageView_address_edit);
		imageView_butaddress.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				GlobalData app = (GlobalData) getActivity().getApplication();
				FragmentManagerEx fm = app.getFragment();
				UserAddressListFragment addressListFragment = (UserAddressListFragment) fm.findFragment(UserAddressListFragment.class.getName());
				if(addressListFragment == null)
					addressListFragment = new UserAddressListFragment(); 
				
				Bundle bundle = addressListFragment.getArguments();
				if(bundle == null){
					bundle = new Bundle(); 
					bundle.putBoolean("isShowButton", true);
					bundle.putString(FragmentEx.CALLBACKFRAGMENTNAME, PayFragment.class.getName()); 
					addressListFragment.setArguments(bundle);
				}
				else{
					bundle.putBoolean("isShowButton", true);
					bundle.putString(FragmentEx.CALLBACKFRAGMENTNAME, PayFragment.class.getName()); 
				}
				
				fm.add(true, false, addressListFragment, UserAddressListFragment.class.getName()); 
			}
		});
		
		//隐藏首页页页脚导航
		getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		
		//获取商品详情请求线程
		loadData();  
				
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
			
			
			if(GlobalData.gShopCartQty == 0){
				clearData();
				
				//跳转到购物车界面
				ShoppingCartFragment shopCart = (ShoppingCartFragment) mFragmentManagerEx.findFragment(ShoppingCartFragment.class.getName());
				if(shopCart != null){
					mFragmentManagerEx.add(true, true, shopCart, ShoppingCartFragment.class.getName());
				}
				
				return;
			}
			
			//读取数据 
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
	
	/**
	 * 请求购物车数据
	 */
	@Override
	public void loadData(){
		
		//禁用支付按钮
		if(button_gopay != null) {
			button_gopay.setEnabled(false);
		}
		
		Bundle bundle = getArguments();
		if(bundle != null){ 
			mAddressId = bundle.getString("addressId");
			mQuan_id = bundle.getString("quan_id");
			mUse_gold = bundle.getString("use_gold"); 
			mTotalAmount = bundle.getDouble("totalamount");
			myfAmount = bundle.getDouble("yfamount");
			mQuan_amount = bundle.getDouble("quan_amount");  
			mUser_jf = bundle.getDouble("user_jf");  
			
			SerializableMap map = (SerializableMap) bundle.getSerializable("addressobj");
			if(map != null){
				UserAddressBean userAdd = (UserAddressBean) map.get("addressobj");
				if(userAdd != null)
					callBack(userAdd); 
			} 
		}
		
		Thread thread = new Thread(new Runnable(){
            @Override  
            public void run(){ 
            	
				try {
					mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
					
					if(isAllRefresh){
						//获取用户TOKEN
		            	User user = GlobalData.gUser;
		            	String token = user.getUserToken(); 
		            	
						/*//获取默认收货地址
	        			UserAddressRequest reqAdd = new UserAddressRequest();
	        			reqAdd.put("token", token); 
	        			reqAdd.put("mrdz", "1");
	        			UserAddressResponse resAdd = reqAdd.doGet(UserAddressResponse.class.getName());
	        			List<UserAddressBean> userAdd = resAdd.getUserAddress();
	        			if(userAdd.size() > 0 && "".equals(mAddressId)){
	        				CallBack(userAdd.get(0));
	        			} */
	        			
						//获取购物车 
						ShoppingCartRequest req = new ShoppingCartRequest();
		            	req.put("token", token);
		            	ShoppingCartResponse res = req.getShopCart(ShoppingCartResponse.class.getName());
						List<ShopCartItemBean> carts = res.getCommodityList();
						if(carts == null || carts.isEmpty()){
							//清空购物车
							clearData(); 
							return;
						}
						
						if(shopCarts == null){
							shopCarts = new ArrayList<ShopCartItemBean>();
						}
						
						//显示购物车的商品信息
						for(int i = 0; i < carts.size(); i++){
							ShopCartItemBean cart = carts.get(i); 
							String id = cart.getId(); 
							int updateRow = -1;
							for(int x = 0; x < shopCarts.size(); x++){
								ShopCartItemBean oldcart = shopCarts.get(x); 
								String oldId = oldcart.getId();
								if(id.equals(oldId)){
									cart.setBitmap(oldcart.getBitmap());
									updateRow = x;
									break;
								}
							}
							if(updateRow < 0)
								shopCarts.add(cart);
							else
								shopCarts.set(updateRow, cart); 
							
							//统计金额
							double salePrice = Double.valueOf(cart.getSalePrice());
							int quantity = Integer.valueOf(cart.getQuantity());
							mAmount += salePrice * quantity;
						} 
						//启动更新线程
	        			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW);
	        			 
	        			isAllRefresh = false;
					}
					else{
						//控制局部刷新
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					mCommodHandler.sendEmptyMessage(FragmentEx.HIDELOADINGPROGRESS);
				}  
            }
        });  
        thread.start();  
	
	}
	
	//小计金额
	public void setTotalAmount(String anount){
		String samount = String.valueOf(anount);
			
		TextView textView_xj = (TextView) getView().findViewById(R.id.textView_xj);
		TextView textView_yf1 = (TextView) getView().findViewById(R.id.textView_yf1);
		TextView textView_totalamount = (TextView) getView().findViewById(R.id.textView_totalamount );
		TextView textView_yhq  = (TextView) getView().findViewById(R.id.textView_yhq);
		TextView textView_jf  = (TextView) getView().findViewById(R.id.textView_jf ); 
		
		textView_xj.setText("商品小计：￥" + samount);
		textView_yf1.setText("=应付：￥" + samount);
		textView_totalamount.setText("￥" + samount); 
		if(mQuan_amount > 0){
			textView_yhq.setText("-优惠券：" + mQuan_amount);
			textView_yhq.setVisibility(View.VISIBLE); 
		}
		else{
			textView_yhq.setVisibility(View.GONE);
		}
		
		if(mUser_jf > 0){
			if(myfAmount - mQuan_amount <= 0){
				textView_jf.setText("-积分抵扣金额：0");
			}
			else{
				mUser_jf = mUser_jf - Math.abs(myfAmount - mQuan_amount - mUser_jf);
				textView_jf.setText("-积分抵扣金额：" + parseMoney(mUser_jf));
				textView_jf.setVisibility(View.VISIBLE); 
			} 
		}
		else{
			textView_jf.setVisibility(View.GONE);
		}
	}
		 
	@Override
	public boolean callBack(Object object) {
		// TODO Auto-generated method stub 
		Message msg = new Message();
		msg.obj = object;
		msg.what = FragmentEx.UPDATE; 
		mCommodHandler.sendMessage(msg);
		
		return true;
	}
	
	/**
	 * 清空数据
	 */
	private void clearData() {
		// TODO Auto-generated method stub
		//订单提交成功，将购物车数量清0
		GlobalData.gShopCartQty = 0;
		shopCarts.clear(); 
		mAddressId = ""; 	//收货地址ID	
		mQuan_id = "0";		//使用券的ID
		mUse_gold = "0";	//是否使用积分： 0不使用 1使用
		mPayType = "";		//付款类型
		mTotalAmount = -1;	//实际付款金额
		
		//删除控件值
		mCommodHandler.sendEmptyMessage(FragmentEx.CLEAR_SHOPCART);
	}
}
