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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.OrderDetailBean;
import com.shoppingstore.app.common.bean.OrderDetailBeans;
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
 * 订单详情信息
 * @author meicunzhi
 * @date 2015-11-23 上午12:02:10
 */
public class OrderDetailFragment extends FragmentEx implements CallBackInterface {
	private Button button_cancelorder;
	private Button button_gopay;
	
	private OrderDetailBean mOrderDetail; 
	private String mItem = "";		//单号
	private double mAmount = 0; 	//付款金额 
	private double mFkje = 0;		//实际付款金额
	
	private LinearLayout mLinearlayout_commoditybar;
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件 
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			mLoadingProgress.hide();
			
			switch (msg.what) { 
			case FragmentEx.UPDATE_VIEW: { 
				if(mOrderDetail == null) return;
				
				TextView textView_orderitem = (TextView) getView().findViewById(R.id.textView_orderitem);
				TextView textView_date = (TextView) getView().findViewById(R.id.textView_date);
				TextView textView_address = (TextView) getView().findViewById(R.id.textView_address);
				textView_orderitem.setText(mOrderDetail.getItem());
				textView_date.setText("订单日期：" + mOrderDetail.getDate()); 
				String address = mOrderDetail.getConsigneeName() + "		" + mOrderDetail.getLinkPhone() + "\n\n" + 
						mOrderDetail.getProvince() + " " +  mOrderDetail.getCity() + " " + mOrderDetail.getCounty() + " " + mOrderDetail.getAddress();
				textView_address.setText(address);
				
				mLinearlayout_commoditybar = (LinearLayout) getView().findViewById(R.id.linearlayout_commoditybar);	 
				LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
				List<OrderDetailBeans> orderBs = mOrderDetail.getOrderItems();  
				mLinearlayout_commoditybar.removeAllViews();
				for(int i = 0; i < orderBs.size(); i++){
					final OrderDetailBeans b = orderBs.get(i);
					View view = inflater.inflate(R.layout.orderdetail_view_item_layout, null, false);
					final ImageView imageView_imageUrl = (ImageView) view.findViewById(R.id.imageView_imageUrl);
					TextView textView_commoditycode = (TextView) view.findViewById(R.id.textView_commoditycode);
					TextView textView_size = (TextView) view.findViewById(R.id.textView_size);
					TextView textView_quantity = (TextView) view.findViewById(R.id.textView_quantity);
					TextView textView_amount = (TextView) view.findViewById(R.id.textView_amount);
					textView_commoditycode.setText(b.getCommodityCode());
					textView_size.setText(b.getSize());
					textView_quantity.setText(b.getQuantity());
					textView_amount.setText("￥" + b.getAmount());
					
					//加载图片资源 
        			Bitmap bitmap = b.getImgBitmap();
        			if(bitmap == null){
        				new Thread(new Runnable() {  
        					@Override  
        					public void run() { 
        						String imgurl = b.getImgUrl();
        						Bitmap bitmap = WebUtils.doGetBitmap(imgurl);
        						b.setImgBitmap(bitmap);
        						
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
					
					final TextView textView_th = (TextView) view.findViewById(R.id.textView_th);
					final String allowreturn = b.getAllowreturn();
					final String thid = b.getId();
					textView_th.setTag(allowreturn + "," + thid);
					if("0".equals(allowreturn)){
						String status = mOrderDetail.getStatus();
						if("0".equals(status)){
							textView_th.setText("已取消");
							
							button_cancelorder.setVisibility(View.GONE);
							button_gopay.setVisibility(View.GONE);
						} else if("2".equals(status)){
							textView_th.setText("待付款");
						} 
					} else if("1".equals(allowreturn) || "2".equals(allowreturn) || "3".equals(allowreturn)){
						button_cancelorder.setVisibility(View.GONE);
						button_gopay.setVisibility(View.GONE);
						TextView textView_totalamount  = (TextView) getView().findViewById(R.id.textView_totalamount );
						textView_totalamount.setVisibility(View.GONE);
						
						final int index = i;
						textView_th.setText("退货");
						textView_th.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								String tag = (String) textView_th.getTag();
								Bundle bundle = new Bundle();
								bundle.putString("id", tag.split(",")[0]);
								bundle.putInt("index", index);
								bundle.putString("allowreturn", tag.split(",")[1]);
								bundle.putString(FragmentEx.CALLBACKFRAGMENTNAME, mItem + "order.");
								SalesdhThFragment th = new SalesdhThFragment();
								th.setArguments(bundle);
								mFragmentManagerEx.add(true, false, th, SalesdhThFragment.class.getName()); 
							}
							
						});
					} 
					
					mLinearlayout_commoditybar.addView(view, i); 
				}
				
				TextView textView_yhq = (TextView) getView().findViewById(R.id.textView_yhq);
				TextView textView_jf = (TextView) getView().findViewById(R.id.textView_jf);
				double damount = 0;
				
				//优惠券
				String samount = mOrderDetail.getQuanje(); 
				if("".equals(samount)){
					samount = "0";
				}
				damount = Double.valueOf(samount);
				if(damount < 1){
					textView_yhq.setVisibility(View.GONE);
				}
				else{
					mFkje -= damount;
					textView_yhq.setText("-优惠券：" + samount);
					textView_yhq.setVisibility(View.VISIBLE);
				}
				
				//积分
				samount = mOrderDetail.getSygold(); 
				if("".equals(samount)){
					samount = "0";
				}
				damount = Double.valueOf(samount);
				if(damount < 1){ 
					textView_jf.setVisibility(View.GONE);
				}
				else{
					mFkje -= damount;
					textView_jf.setText("-积分抵扣金额：" + mOrderDetail.getSygold());
					textView_jf.setVisibility(View.VISIBLE);
				}
				
				//显示统计金额
				setTotalAmount(parseMoney(String.valueOf(mFkje)));
				
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
			case FragmentEx.UPDATE:{
				Bundle bundle = getArguments();
				if(bundle != null){
					int index = bundle.getInt("position");
					mCallBackFragment.callBack(index);
				} 
				popBackStack();
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
			case 999:{
				int index = msg.arg1;
				String tip = (String) msg.obj;
				TextView textView_th = (TextView) mLinearlayout_commoditybar.getChildAt(index).findViewById(R.id.textView_th); 
				textView_th.setText(tip);
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
		return inflater.inflate(R.layout.orderdetail_view_layout, container, false);
		
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		
		Bundle bundle = this.getArguments();
		if(bundle != null){
			mItem = bundle.getString("item");
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
		textView_Title.setText("订单详情");  
		
		button_cancelorder = (Button) getView().findViewById(R.id.button_cancelorder);
		button_cancelorder.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 button_cancelorder.setEnabled(false);
				 try{
					 Thread thread = new Thread(new Runnable() {  
						 @Override  
						 public void run(){ 
							 try {
								 String token = GlobalData.gUser.getUserToken();
								 SalesdhRequest req = new SalesdhRequest();
								 req.put("Token", token);
								 req.put("Item", mItem);
								 SalesdhResponse res = req.doPut(SalesdhResponse.class.getName());
								 if(res.isAllStatus()){ 
									 Message msg = new Message();
									 msg.what = FragmentEx.UPDATE;
									 mCommodHandler.sendMessage(msg); 
								 } 
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								button_cancelorder.setEnabled(true);
							} 
						 }
					 });
					 thread.start();
				 }
				 catch(Exception e){
					 
				 } finally {
					 button_cancelorder.setEnabled(true);
				 } 
			 }
		});
		
		//立即支付
		button_gopay = (Button) getView().findViewById(R.id.button_gopay); 
		button_gopay.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 //打开支付宝
				 Bundle bundle = new Bundle();
				 bundle.putString("item", mItem);
				 if(mFkje < 0)
					 mFkje = 0;
				 bundle.putDouble("fkje", Double.valueOf(mFkje));
					
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment();
				 PayConfirmFragment payConfirm = new PayConfirmFragment();
				 payConfirm.setArguments(bundle);
				 fm.add(true, true, payConfirm, PayConfirmFragment.class.getName());   
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
            	mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
            	
            	try{
            		//加载数据
                	if(isAllRefresh){
                		String token = GlobalData.gUser.getUserToken();
                		SalesdhRequest salesReq = new SalesdhRequest(); 
                		salesReq.put("token", token);
                		salesReq.put("item", mItem); 
                		SalesdhResponse salesRes = salesReq.doGet(SalesdhResponse.class.getName());
                		List<OrderDetailBean> orders = salesRes.getOrderDetail();
                		if(orders == null) return;
                		
                		if(mOrderDetail == null){
                			mOrderDetail = orders.get(0);  
                			mFkje = Double.valueOf(mOrderDetail.getFkje());
                			mAmount = mFkje;
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
	
	//小计金额
	public void setTotalAmount(String anount){
		String samount = String.valueOf(anount);
				
		TextView textView_xj = (TextView) getView().findViewById(R.id.textView_xj);
		TextView textView_yf1 = (TextView) getView().findViewById(R.id.textView_yf1);
		TextView textView_totalamount  = (TextView) getView().findViewById(R.id.textView_totalamount );
		textView_xj.setText("商品小计：" + String.valueOf(mAmount));
		textView_yf1.setText("=应付：" + samount);
		textView_totalamount.setText(samount);
	}
	
	@Override
	public boolean callBack(Object object) {
		// TODO Auto-generated method stub
		Map<String, Object> map = (Map<String, Object>) object;  
		Message msg = new Message();
    	msg.arg1 = Integer.valueOf(String.valueOf(map.get("index")));
    	msg.obj = map.get("tip");
    	msg.what = 999;
    	mCommodHandler.sendMessage(msg);
    	
		return false;
	}
}
