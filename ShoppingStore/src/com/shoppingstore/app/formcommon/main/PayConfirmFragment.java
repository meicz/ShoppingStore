package com.shoppingstore.app.formcommon.main;
  
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList; 
import java.util.Date;
import java.util.List;  

import com.alipay.android.app.sdk.AliPay;  
import com.shoppingstore.app.R;   
import com.shoppingstore.app.alipay.sdk.pay.Keys;
import com.shoppingstore.app.alipay.sdk.pay.Result;
import com.shoppingstore.app.alipay.sdk.pay.Rsa;
import com.shoppingstore.app.common.bean.ShopCartItemBean;   
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.Adapter.ShopCartItemAdapter; 
import com.shoppingstore.app.formcommon.utils.CallBackInterface;
import com.shoppingstore.app.formcommon.utils.FragmentEx;    
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.RadioButtonLayout;  

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle; 
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;  
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.ViewGroup;  
import android.view.View.OnClickListener;
import android.widget.Button; 
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;  
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
 
/**
 * 我的购物车
 * 通过回调，获取选择的地址
 * @author meicunzhi
 * @date 2015-10-26 下午2:30:19
 */
public class PayConfirmFragment extends FragmentEx implements CallBackInterface {   
	private double mAmount = 0;			//金额
	private String mAddressId = ""; 	//收货地址ID	
	private String paytype = "";		//付款类型
	private String mItem = "";			//单号
	private String  mToFragment = "";
	
	private static final int RQF_PAY = 1; 
	private static final int RQF_LOGIN = 2;
	
	private PopupWindow popupWindow = null;
	private LinearLayout popLayout; 
	private boolean ispayok = false;
	
	List<RadioButtonLayout> radios = new ArrayList<RadioButtonLayout>();
		
	private TextView textView_item;
	private Button button_dd;
	private Button button_jxgw;
	private Button button_fx;
	
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
					} */
					//付款类型
					paytype = (String) b.getTag();
					b.setSelect(true); 
				}
				else{ 
					b.setSelect(false);
				}
			}
				
		} 
	}; 
		 
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:
			case RQF_LOGIN: { 
				//popBackStack();
				/*PayOkFragment payok = new PayOkFragment();
				Bundle bundle = new Bundle();
				bundle.putString("item", mItem);
				payok.setArguments(bundle);
				mFragmentManagerEx.add(true, true, payok, PayOkFragment.class.getName());*/
				if("9000".equals(result.getResultCode())){  
					ispayok = true; 
					if(popupWindow == null){
						initPopupWindow();
					} 
					popupWindow.showAtLocation(getView(), Gravity.CENTER|Gravity.BOTTOM, 0, 0);
					mItem = "";
					mAmount = 9999999; 
				}
				else{ 
					ispayok = false; 
					Toast toast = Toast.makeText(getActivity(), result.getResult(), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} 
				
				break;
			} 
			default:
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pay_confirm_layout, container, false);
		
	}  
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		Bundle bundle = getArguments();
		mItem = bundle.getString("item");
		mAmount = bundle.getDouble("fkje");
		mToFragment = bundle.getString("tofragment");
		
		if(popupWindow == null){
			initPopupWindow();
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
		textView_Title.setText("确认支付"); 
		 
		Button button_gopay = (Button) getView().findViewById(R.id.button_gopay);
		button_gopay.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 if("".equals(mItem.trim())){
					 Toast toast = Toast.makeText(getActivity(), "无此交易", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show();
					 return;
				 }
				 
				 String info = getNewOrderInfo();
				 String sign = Rsa.sign(info, Keys.PRIVATE);
				 sign = URLEncoder.encode(sign);
				 info += "&sign=\"" + sign + "\"&" + getSignType(); 

				 final String orderInfo = info;
				 new Thread() {
					 public void run() {
						 AliPay alipay = new AliPay(getActivity(), mHandler);
							
						 //设置为沙箱模式，不设置默认为线上环境
						 //alipay.setSandBox(true);

						 String result = alipay.pay(orderInfo);
 
						 Message msg = new Message();
						 msg.what = RQF_PAY;
						 msg.obj = result;
						 mHandler.sendMessage(msg);
					 }
				 }.start(); 
			 }
		});  
		
		RadioButtonLayout zfb = (RadioButtonLayout) getView().findViewById(R.id.pay_zfb);  
		zfb.setTag("zfb");
		zfb.setSelect(true);
		  
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
		
		/*
		
		PayOkFragment payok = new PayOkFragment();
		Bundle bundle = new Bundle();
		bundle.putString("item", mItem);
		payok.setArguments(bundle);
		mFragmentManagerEx.add(true, true, payok, PayOkFragment.class.getName());*/
	} 
	
	/**
	 * 请求购物车数据
	 */
	@Override
	public void loadData(){

		Thread thread = new Thread(new Runnable(){
            @Override  
            public void run(){ 
            	
            }
        });  
        thread.start();  
	
	} 
	 
	@Override
	public boolean callBack(Object object) {
		// TODO Auto-generated method stub 
		
		return true;
	}
	
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(mItem);	//getOutTradeNo()
		sb.append("\"&subject=\"");
		sb.append("商品");
		sb.append("\"&body=\"");
		sb.append("商品");
		sb.append("\"&total_fee=\"");
		sb.append(String.valueOf(mAmount));
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(GlobalData.GURL + "PayView/AlipayHandle.aspx"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}
	
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15); 
		return key;
	}
	 
	private String trustLogin(String partnerId, String appUserId) {
		StringBuilder sb = new StringBuilder();
		sb.append("app_name=\"mc\"&biz_type=\"trust_login\"&partner=\"");
		sb.append(partnerId);
		Log.d("TAG", "UserID = " + appUserId);
		if (!TextUtils.isEmpty(appUserId)) {
			appUserId = appUserId.replace("\"", "");
			sb.append("\"&app_id=\"");
			sb.append(appUserId);
		}
		sb.append("\"");

		String info = sb.toString();

		// 请求信息签名
		String sign = Rsa.sign(info, Keys.PRIVATE);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		info += "&sign=\"" + sign + "\"&" + getSignType();

		return info;
	}
	
	private void initPopupWindow() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popLayout = (LinearLayout) inflater.inflate(R.layout.payok_layout, null, false);
		popupWindow = new PopupWindow(popLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setFocusable(true);  
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);    
		
		textView_item = (TextView) popLayout.findViewById(R.id.textView_item);
		button_dd = (Button) popLayout.findViewById(R.id.button_dd);
		button_jxgw = (Button) popLayout.findViewById(R.id.button_jxgw);
		textView_item.setText("您的订单号:" + mItem);
		button_dd.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 List<String> fragmentNames = new ArrayList<String>();
				 fragmentNames.add(PayConfirmFragment.class.getName());
				 fragmentNames.add(PayFragment.class.getName());
				 fragmentNames.add(OrderDetailFragment.class.getName());
				 deleteFragments(fragmentNames);
				 popupWindow.dismiss();  
				 
				 Bundle bundle = new Bundle();
				 bundle.putString("item", mItem); 
				 OrderDetailFragment orderDstail = new OrderDetailFragment();
				 orderDstail.setArguments(bundle);
				 mFragmentManagerEx.add(true, true, orderDstail, mItem+"order.");
			 }
		});  
		
		button_jxgw.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 List<String> fragmentNames = new ArrayList<String>();
				 fragmentNames.add(PayConfirmFragment.class.getName());
				 fragmentNames.add(PayFragment.class.getName());
				 fragmentNames.add(OrderDetailFragment.class.getName());
				 deleteFragments(fragmentNames);
				 popupWindow.dismiss();  
				 
				 FragmentHome home = new FragmentHome(); 
				 mFragmentManagerEx.add(false, true, home, FragmentHome.class.getName());
			 }
		});  
		
		RelativeLayout imageView_ComLeft = (RelativeLayout) popLayout.findViewById(R.id.imageView_ComLeft);
		imageView_ComLeft.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 if(ispayok){
					 List<String> fragmentNames = new ArrayList<String>();
					 fragmentNames.add(PayConfirmFragment.class.getName());
					 fragmentNames.add(PayFragment.class.getName());
					 fragmentNames.add(OrderDetailFragment.class.getName());
					 deleteFragments(fragmentNames); 
					 popupWindow.dismiss(); 

					 if(FragmentHome.class.getName().equals(mToFragment)){
						 FragmentHome home = new FragmentHome(); 
						 mFragmentManagerEx.add(false, true, home, FragmentHome.class.getName());
					 } 
				 }
				 else{
					 popupWindow.dismiss();
				 }
			 }
		});  
	}
	
	private Drawable getDrawable(){
		ShapeDrawable bgdrawable =new ShapeDrawable(new OvalShape());
		bgdrawable.getPaint().setColor(getActivity().getResources().getColor(android.R.color.transparent));
		return bgdrawable;
		}
}
