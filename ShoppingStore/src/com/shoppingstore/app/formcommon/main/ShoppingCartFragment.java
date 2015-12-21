package com.shoppingstore.app.formcommon.main;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.QuanCenterBean;
import com.shoppingstore.app.common.bean.ShopCartItemBean;
import com.shoppingstore.app.common.bean.User;
import com.shoppingstore.app.common.bean.UserAddressBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.formcommon.Adapter.ShopCartItemAdapter;
import com.shoppingstore.app.formcommon.utils.BuyQuantityEvent;
import com.shoppingstore.app.formcommon.utils.CallBackInterface;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.RadioButtonLayout;
import com.shoppingstore.app.formcommon.utils.SerializableMap;
import com.shoppingstore.app.internal.WebUtils;
import com.shoppingstore.app.internal.request.ShoppingCartRequest;
import com.shoppingstore.app.internal.request.UserAddressRequest;
import com.shoppingstore.app.internal.request.UsercenterRequest;
import com.shoppingstore.app.internal.response.ShoppingCartResponse;
import com.shoppingstore.app.internal.response.UserAddressResponse;
import com.shoppingstore.app.internal.response.UsercenterResponse;
 
/**
 * 我的购物车
 * @author meicunzhi
 * @date 2015-10-26 下午2:30:19
 */
public class ShoppingCartFragment extends FragmentEx implements CallBackInterface {  
	private List<ShopCartItemBean> shopCarts; 
	private ShopCartItemAdapter shopCartAdapter; 
	private double mOlgold = -1; 	//用户可用积分
	private double mUseJf = 0;			//使用的积分金额
	private double mQuanAmount = 0; 	//用户可用券金额
	private String mQuanId = "";		//使用的优惠券ID 
	
	private double myfAmount = 0;		//应付金额
	private double mTotalAmount = 0; 	//实际付款总金额 
	
	private RadioButtonLayout pay_syjf; 
	private RelativeLayout layout_yhq;
	private RelativeLayout layout_yhqinfo; 
	private ImageView imageView_imageUrl;
	private TextView textView_jf;
	
	public final static int GOPAY = 100;		//付款
	public final static int GOADDRESS = 101;	//地址
	
	private View mView;
	
	private String token = "";
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {  
			
			Log.d("shopex", "： handleMessage开始" );
			try{
			switch (msg.what) { 
			case FragmentEx.UPDATE_VIEW: {
				if(getView() == null){
					return;
				}
				//显示购物车页面
    			if(shopCarts.isEmpty()){
    				getView().findViewById(R.id.empty_shopcart).setVisibility(View.VISIBLE);
    				getView().findViewById(R.id.my_shopcart).setVisibility(View.GONE);
    			}
    			else{
    				getView().findViewById(R.id.empty_shopcart).setVisibility(View.GONE);
    				getView().findViewById(R.id.my_shopcart).setVisibility(View.VISIBLE);
    			}
    			
    			final LinearLayout linearlayout_commoditybar = (LinearLayout) getView().findViewById(R.id.linearlayout_commoditybar);
    			
    			//先清空以前界面上的内容
    			linearlayout_commoditybar.removeAllViews();
    			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    			for(int i = 0; i < shopCarts.size(); i++){
    				final ShopCartItemBean com = shopCarts.get(i);
    				
    				final View view = inflater.inflate(R.layout.shopcartitem_layout, null, false);   
        			View buy_quantity_fragment = view.findViewById(R.id.buy_quantity_fragment);
        			View imageView_shopcart_delete = view.findViewById(R.id.imageView_shopcart_delete);
        			TextView textView_quantity = (TextView) view.findViewById(R.id.textView_quantity);
        			
        			BuyQuantityEvent byEvent = new BuyQuantityEvent(view);
        			byEvent.setMax(Integer.valueOf(com.getKcquantity() + 10));
        			byEvent.initEvent(ShoppingCartFragment.this, com);
        			final int deleteindex = i;
        			imageView_shopcart_delete.setOnClickListener(new OnClickListener(){

        				@Override
        				public void onClick(View arg0) {
        					// TODO Auto-generated method stub
        					new Thread(new Runnable() {  
            					@Override  
            					public void run() {
            						mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS); 
            						try {
            							ShoppingCartRequest req = new ShoppingCartRequest();
            							req.put("id", com.getId());
										ShoppingCartResponse res = req.deleteShopCart(ShoppingCartResponse.class.getName());
										if(res.isAllStatus()){
											GlobalData.gShopCartQty -= 1;  
											shopCarts.remove(deleteindex);
											Message msg = new Message();
											Map<String, Object> map = new HashMap<String, Object>();
											map.put("deletelayout", linearlayout_commoditybar);
											map.put("deleteview", view); 
											msg.obj = map;
											msg.arg1 = FragmentEx.UPDATE_DELETE;
											mCommodHandler.sendMessage(msg); 
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} finally {
										mCommodHandler.sendEmptyMessage(FragmentEx.HIDELOADINGPROGRESS);
									}
            						
            					}
            				}).start(); 
        				}
        				
        			});
        			/*Button butAdd = (Button) view.findViewById(R.id.button_add);
        			Button butSub = (Button) view.findViewById(R.id.button_sub); */
        			 
        			final ImageView imageView_imageUrl = (ImageView) view.findViewById(R.id.imageView_imageUrl);
        			TextView textView_commodityname = (TextView) view.findViewById(R.id.textView_commodityname);
        			TextView textView_size = (TextView) view.findViewById(R.id.textView_size);
        			TextView textView_price = (TextView) view.findViewById(R.id.textView_price);  
        			EditText editText_addquantity = (EditText) view.findViewById(R.id.editText_addquantity);  
        			TextView textView_tip = (TextView) view.findViewById(R.id.textView_tip);
        			if(Integer.valueOf(com.getKcquantity()) <= 0){
        				textView_tip.setVisibility(View.VISIBLE);
        			}
        			else{
        				textView_tip.setVisibility(View.GONE);
        			}
        			textView_commodityname.setText(com.getNameCN());
        			double salePrice = Double.valueOf(com.getSalePrice());
        			textView_price.setText("￥" + salePrice);
        			textView_size.setText(com.getSizeName());   
        			editText_addquantity.setText(com.getQuantity());
        			textView_quantity.setText(com.getQuantity());
        			
        			//加载图片资源 
        			Bitmap bitmap = com.getBitmap();
        			if(bitmap == null){
        				new Thread(new Runnable() {  
        					@Override  
        					public void run() { 
        						String imgurl = com.getImageUrl();
        						Bitmap bitmap = WebUtils.doGetBitmap(imgurl);
        						com.setBitmap(bitmap);
        						
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
				setTotalAmount(getAmount());
				
        		break;
			}
			case FragmentEx.UPDATE: {
				//shopCartAdapter.notifyDataSetChanged();
				textView_jf.setText("使用积分（可用积分" + String.valueOf(mOlgold) + "）");
				
        		break;
			}
			case FragmentEx.UPDATE_COMMODITY_IMAGE: {
				//shopCartAdapter.notifyDataSetChanged();
				if(getView() == null){
					return;
				}
				
				Map<String, Object> imgMap = (Map<String, Object>) msg.obj; 
        		ImageView imageView_imageUrl = (ImageView) imgMap.get("imageView");
        		Bitmap bmp = (Bitmap) imgMap.get("imageBitmap");
        		imageView_imageUrl.setImageBitmap(bmp); 
				
        		break;
			}
			case GOPAY: {  
				UserAddressBean userAddress = (UserAddressBean) msg.obj;
				PayFragment payFragment = (PayFragment) mFragmentManagerEx.findFragment(PayFragment.class.getName()); 
				if(payFragment == null)
					payFragment = new PayFragment();  
				Bundle bundle = payFragment.getArguments();  
				if(bundle == null){ 
					bundle = new Bundle(); 
					payFragment.setArguments(bundle);
				}
				bundle.putString("addressId", userAddress.getId());  
				bundle.putDouble("yfamount", myfAmount); 
				bundle.putDouble("totalamount", mTotalAmount); 
				SerializableMap map = new SerializableMap();
				map.put("addressobj", userAddress);
				bundle.putSerializable("addressobj", map); 	
				bundle.putString("paytype", "支付宝");  
				
				//使用券
				if(mQuanAmount > 0){
					bundle.putString("quan_id", mQuanId);  
				}
				else{
					bundle.putString("quan_id", "");  
				}
				
				if(mUseJf > 0){
					bundle.putString("use_gold", "1");  
				}
				else{
					bundle.putString("use_gold", "0");  
				} 

				bundle.putDouble("quan_amount", mQuanAmount);  
				bundle.putDouble("user_jf", mUseJf);  
				mFragmentManagerEx.add(true, false, payFragment, PayFragment.class.getName());
				
        		break;
			}
			case GOADDRESS: {  
				UserAddressFragment addressFragment = (UserAddressFragment) mFragmentManagerEx.findFragment(UserAddressFragment.class.getName()); 
				if(addressFragment == null)
					addressFragment = new UserAddressFragment();  
				Bundle args = addressFragment.getArguments(); 
				if(args == null){ 
					args = new Bundle();
					args.putString("addressId", ""); 
					args.putString("isAddMrdz", "isAddMrdz");
					addressFragment.setArguments(args);
				}
				else{  
					args.putString("isAddMrdz", "isAddMrdz");
					args.putString("addressId", "");  
				}
				mFragmentManagerEx.add(true, false, addressFragment, UserAddressFragment.class.getName());
				
				break;
			}
			case FragmentEx.CLEAR_SHOPCART: {
				getView().findViewById(R.id.empty_shopcart).setVisibility(View.VISIBLE); 
				getView().findViewById(R.id.my_shopcart).setVisibility(View.GONE); 
				LinearLayout linearlayout_commoditybar = (LinearLayout) getView().findViewById(R.id.linearlayout_commoditybar); 
    			linearlayout_commoditybar.removeAllViews();
    			
    			pay_syjf.setSelect(false);  
    			layout_yhq.setVisibility(View.VISIBLE); 
    			layout_yhqinfo.setVisibility(View.GONE);  
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
			case FragmentEx.UPDATE_DELETE: {
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				LinearLayout deletelayout = (LinearLayout) map.get("deletelayout");
				View deleteview = (View) map.get("deleteview"); 
				deletelayout.removeView(deleteview);
				break;
			}
			default:
				break;
			}
			}catch(Exception e){
				e.printStackTrace();

				mLoadingProgress.hide();
			}
			
			Log.d("shopex", "： handleMessage结束" );
		}
	};
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.shopcar_layout, container, false);
		return mView;
		
	}  
	
	public View getView(){
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		
		//判断token是否为空，为空跳转到注册登陆窗口
		User user = GlobalData.gUser;
		token = user.getUserToken();  
		if(token == null || "".equals(token)){
			getView().findViewById(R.id.empty_shopcart).setVisibility(View.VISIBLE); 
			getView().findViewById(R.id.my_shopcart).setVisibility(View.GONE); 
		}
		else{
			//获取商品详情请求线程
			loadData();
		}
		
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
		
		Button button_backhome = (Button) getView().findViewById(R.id.button_backhome);
		button_backhome.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 FragmentHome home = new FragmentHome();
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment();
				 fm.add(true, false, home, FragmentHome.class.getName());
			 }
		});  
		 
		//立即结账
		Button button_gobuy = (Button) getView().findViewById(R.id.button_gobuy);
		button_gobuy.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 boolean ispay = true;
				 for(int i = 0; i < shopCarts.size(); i++){
					 ShopCartItemBean cart = shopCarts.get(i);
					 if(Integer.valueOf(cart.getKcquantity()) <= 0){
						 ispay = false;
						 break;
					 }
				 }
				 if(!ispay){
					 Toast toast = Toast.makeText(getActivity(), "请删除库存不足的商品再进行结账", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
					
				 //重新检查商品的库存判断是否是0  
				 Thread thread = new Thread(new Runnable(){
					 @Override  
					 public void run(){ 
			            	
						 try {
							 mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
							 
							 //判断是否存在默认地址，没有就要求先输入地址信息
							 UserAddressRequest reqAdd = new UserAddressRequest();
							 reqAdd.put("token", token); 
							 reqAdd.put("mrdz", "1");
							 UserAddressResponse resAdd = reqAdd.doGet(UserAddressResponse.class.getName());
							 if(resAdd.isAllStatus()){
								 List<UserAddressBean> userAdd = resAdd.getUserAddress();
								 if(userAdd.size() > 0){
									 //打开付款界面 
									 CallBackPay(userAdd.get(0));
								 }
								 else{
									 //打开输入地址界面
									 mCommodHandler.sendEmptyMessage(GOADDRESS); 
								 }
							 }
							 else{
								 //打开输入地址界面
								 mCommodHandler.sendEmptyMessage(GOADDRESS); 
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
		});  
		 
		pay_syjf = (RadioButtonLayout) getView().findViewById(R.id.pay_syjf);  		
		pay_syjf.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 if(mOlgold <= 0) {
					 pay_syjf.setSelect(false);  
					 return;
				 }
				 
				 if(pay_syjf.getSelect()){ 
					 pay_syjf.setSelect(false);  
					 mUseJf = 0; 
					 setTotalAmount(getAmount());
				}
				else{  
					mUseJf = mOlgold;
					pay_syjf.setSelect(true);  
					setTotalAmount(getAmount());
				}
			 }
		}); 
		
		OnClickListener yhqClick = new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				QuanSelectListFragment quanSelect = new QuanSelectListFragment();
				Bundle bundle = new Bundle(); 
				GlobalData app = (GlobalData) getActivity().getApplication();
				FragmentManagerEx fm = app.getFragment();
				fm.add(true, false, quanSelect, QuanSelectListFragment.class.getName());
			}
			
		};
		layout_yhq = (RelativeLayout) getView().findViewById(R.id.layout_yhq);
		layout_yhqinfo = (RelativeLayout) getView().findViewById(R.id.layout_yhqinfo);
		layout_yhq.setOnClickListener(yhqClick);  
		layout_yhqinfo.setOnClickListener(yhqClick);  
		
		//积分控件
		textView_jf = (TextView) getView().findViewById(R.id.textView_jf);
		
		//隐藏首页页页脚导航
		getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		    
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
				return;
			}
			
			//读取数据,实时查询数据 
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
		
		Thread thread = new Thread(new Runnable(){
            @Override  
            public void run(){
            	try {
            		mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
            		
            		User user = GlobalData.gUser;
            		String token = user.getUserToken();
            		ShoppingCartRequest req = new ShoppingCartRequest();
            		req.put("token", token);
            		ShoppingCartResponse res; 
					if(isAllRefresh){
						res = req.getShopCart(ShoppingCartResponse.class.getName()); 
	            		List<ShopCartItemBean> carts = res.getCommodityList();
	            		if(carts == null || carts.isEmpty()){
	            			//清空数据 
	            			clearData(); 
	            			
							return;
						}
	            		
						if(shopCarts == null){
							shopCarts = new ArrayList<ShopCartItemBean>();
						}
						//显示信息
						for(int i = 0; i < carts.size(); i++){
							Log.d("shopex", carts.size() + "： 获取信息" + i);
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
						} 
						
						/*shopCarts.clear();
						for(int i = 0; i < carts.size(); i++){
							shopCarts.add(carts.get(i)); 
						} */
						//启动更新线程
	        			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW); 
	        			
	        			//获取用户积分
	        			UsercenterRequest userReq = new UsercenterRequest();
	        			userReq.put("token", token); 
	        			UsercenterResponse userRes = userReq.doGet(UsercenterResponse.class.getName());
	        			if(userRes.isAllStatus()){
	        				String olgold = userRes.getData("olgold");
	        				if("".equals(olgold)) olgold = "0";
	        				mOlgold = Double.valueOf(olgold);
	        			} 
	        			
	        			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE);
	        			Log.d("shopex", shopCarts.size() + "： 获取积分");
	        			
	        			isAllRefresh = false;
					}
					else{
						
					} 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if(e instanceof BusException){
		        		BusException busEx = (BusException) e;
		        		busEx.showException();
		        	} 
					else{ 
						e.printStackTrace();
					}
				} finally {
					mCommodHandler.sendEmptyMessage(FragmentEx.HIDELOADINGPROGRESS);
				} 
            }
        });  
        thread.start();  
	}
	
	protected void clearData() {
		// TODO Auto-generated method stub
		//清空数据 
		mTotalAmount = 0;
		mUseJf = 0;
		mOlgold = -1;
		mQuanAmount = 0;
		mQuanId = ""; 
		
		if(shopCarts != null){
			shopCarts.clear();
		}
		
		mCommodHandler.sendEmptyMessage(FragmentEx.CLEAR_SHOPCART); 
	}
	
	public void deleteShopCart(int position){
		shopCarts.remove(position);
	}
	
	//小计金额
	public void setTotalAmount(String anount){ 
		TextView xj = (TextView) getView().findViewById(R.id.textView_xj);
		TextView totalamount = (TextView) getView().findViewById(R.id.textView_totalamount);
		xj.setText(anount);
		totalamount.setText(anount);
	}
	
	public String getAmount() {
		myfAmount = 0;
		if(shopCarts == null) return "0";
		
		for(int i = 0; i < shopCarts.size(); i++){
			ShopCartItemBean com = shopCarts.get(i);
			double salePrice = Double.valueOf(com.getSalePrice());
			int quantity = Integer.valueOf(com.getQuantity());
			myfAmount += salePrice * quantity; 
		}
		
		//总金额 = 实付 -（减优惠券 + 积分）
		mTotalAmount = myfAmount - (mQuanAmount + mUseJf);  
		 
		if(mTotalAmount < 0)
			mTotalAmount = 0;
		
		return FragmentEx.parseMoney(mTotalAmount);
	}
	
	@Override
	public boolean callBack(Object object) {
		// TODO Auto-generated method stub
		if(object == null){  
			layout_yhqinfo.setVisibility(View.GONE); 
			layout_yhq.setVisibility(View.VISIBLE);
			mQuanAmount = 0;
			mQuanId = ""; 
			setTotalAmount(getAmount()); 
			return false; 
		}
		
		//获取选中的优惠券信息
		layout_yhqinfo.setVisibility(View.VISIBLE); 
		layout_yhq.setVisibility(View.GONE);
		QuanCenterBean quanList = (QuanCenterBean) object;
		TextView textView_quantype = (TextView) getView().findViewById(R.id.textView_quantype);
		TextView textView_amount = (TextView) getView().findViewById(R.id.textView_amount); 
		textView_quantype.setText(quanList.getType() + ":");
		textView_amount.setText(quanList.getAmount());
		 
		mQuanAmount = Double.valueOf(quanList.getAmount());
		mQuanId = quanList.getId();
		setTotalAmount(getAmount()); 
		
		return true;
	}
	
	/**
	 * 跳转到支付的界面
	 * @param object
	 * @return
	 */
	public boolean CallBackPay(Object object) {
		// TODO Auto-generated method stub
		if(object == null) return false; 
		UserAddressBean userAddress = (UserAddressBean) object;
		Message msg = new Message();
		msg.obj = userAddress;
		msg.what = GOPAY;
		mCommodHandler.sendMessage(msg); 
		
		return true;
	}
}
