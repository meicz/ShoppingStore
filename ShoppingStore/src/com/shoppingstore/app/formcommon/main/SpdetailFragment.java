package com.shoppingstore.app.formcommon.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.shoppingstore.app.R; 
import com.shoppingstore.app.common.bean.SizeBean;
import com.shoppingstore.app.common.bean.SpdetailBean;
import com.shoppingstore.app.common.bean.User;
import com.shoppingstore.app.common.global.GlobalData;  
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.formcommon.login.LoginFragment;
import com.shoppingstore.app.formcommon.utils.BuyQuantityEvent; 
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.ImageRoundView;
import com.shoppingstore.app.formcommon.utils.ShopCartImageView; 
import com.shoppingstore.app.formcommon.utils.SizeButton;
import com.shoppingstore.app.internal.WebUtils;
import com.shoppingstore.app.internal.request.FavoriteRequest;
import com.shoppingstore.app.internal.request.ShoppingCartRequest;
import com.shoppingstore.app.internal.request.SpdetailRequest;
import com.shoppingstore.app.internal.response.FavoriteResponse;
import com.shoppingstore.app.internal.response.ShoppingCartResponse;
import com.shoppingstore.app.internal.response.SpdetailResponse;

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
import android.view.WindowManager;
import android.widget.Button; 
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;
 
/**
 * 商品详情
 * @author meicunzhi
 * @date 2015-10-26 下午2:30:19
 */
public class SpdetailFragment extends FragmentEx implements PopupWindow.OnDismissListener {  
	private String item = "";			//商品编码
	private String activity_id = "";		//是否活动商品（正常商品-1，活动商品ID) 
	private String size = "";			//尺码
	 
	private SpdetailBean spdetail; 	//商品详情 
	
	private LinearLayout popLayout;
	
	private LinearLayout sizeLayout;
	private BuyQuantityEvent buyQuantity;
	private PopupWindow popupPayWindow = null; 	//支付弹出窗
	private PopupWindow popupFxWindow = null; 	//分享弹出窗
	
	private View mView;
	private TextView textView_Title;
	
	private ImageRoundView imageRoundView;
	
	OnClickListener butSizeClick = new View.OnClickListener(){

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub 
			//判断是否禁用按钮，是退出
			SizeButton clickbut = (SizeButton) but;
			if(clickbut.getQuantity() <= 0) return;
			
			//设置选中
			for(int i = 0; i < sizeLayout.getChildCount(); i++){
				if(sizeLayout.getChildAt(i) instanceof SizeButton){
					SizeButton b = (SizeButton) sizeLayout.getChildAt(i);
					if(b.getQuantity() <= 0){
						continue;
					}
					if(b.equals(but)){
						if(b.isSelect()){
							b.setBackgroundResource(R.drawable.merchandiseitemsizecommon2x);
							b.setSelect(false);
							size = "";
						}
						else{
							b.setBackgroundResource(R.drawable.merchandiseitemsizeselected2x);
							b.setSelect(true);
							size = (String) but.getTag();
						} 
					}
					else{
						b.setBackgroundResource(R.drawable.merchandiseitemsizecommon2x);
						b.setSelect(false);
					}
				}
			}
			
		} 
	};
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) { 
			if(spdetail == null) {
				mLoadingProgress.hide();
				return;
			}
			
			switch (msg.what) { 
			case FragmentEx.UPDATE_COMMODITY_IMAGE: {
				/*//添加轮播图片
				ImageRoundView imageRoundView = (ImageRoundView)getView().findViewById(R.id.spdetail_imageRoundView);
				for(int i = 0; i < bitmap.length; i++){ 
					imageRoundView.addImage(bitmap[i]);
				} 
				int w = imageRoundView.getMeasuredWidth();
				int h = imageRoundView.getMeasuredHeight();
				imageRoundView.setBitmap(true);
				imageRoundView.initUI(getActivity());  */
				break;
			} 
			case FragmentEx.UPDATE_COMMODITY_SIZE: {
				textView_Title.setText(spdetail.getNameCN());
				TextView textView_namecn = (TextView) getView().findViewById(R.id.textView_namecn);
				TextView textView_nameen = (TextView) getView().findViewById(R.id.textView_nameen);
				TextView textView_salePrice = (TextView) getView().findViewById(R.id.textView_salePrice);
				TextView textView_price = (TextView) getView().findViewById(R.id.textView_price);
				TextView textView_detailScription = (TextView) getView().findViewById(R.id.textView_detailScription);  
				textView_namecn.setText(spdetail.getNameCN());
				textView_nameen.setText(spdetail.getNameEN());
				textView_salePrice.setText(FragmentEx.parseMoney(spdetail.getSalePrice()));
				textView_price.setText(FragmentEx.parseMoney(spdetail.getPrice()));
				textView_detailScription.setText(spdetail.getDetailScription());
				textView_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
				
				//动态添加尺码组按钮 
				//要添加按钮的主布局
				sizeLayout = (LinearLayout) getView().findViewById(R.id.sizebuttons);
				
				//按钮的布局，设置右边间距
				LinearLayout.LayoutParams sizeButtonLayout = new LinearLayout.LayoutParams(150, LayoutParams.MATCH_PARENT); 
				sizeButtonLayout.rightMargin = 20;
				List<SizeBean> sizes = spdetail.getSizeArraycolorid();
				for(int i = 0; i < sizes.size(); i++){
					SizeBean size = sizes.get(i);
					SizeButton butSize = new SizeButton(sizeLayout.getContext());
					butSize.setText(size.getName());
					butSize.setTag(size.getCode()); 
					butSize.setLayoutParams(sizeButtonLayout);
					/*
					butSize.setFocusable(true);
					butSize.setFocusableInTouchMode(true);
					butSize.requestFocus();
					butSize.requestFocusFromTouch();*/
					butSize.requestFocus();
					butSize.requestFocusFromTouch();
					butSize.setOnClickListener(butSizeClick); 
					butSize.setTextColor(getResources().getColor(R.color.spdetail_footer_margin_line_color));
					int qty = size.getQuantity() == null || size.getQuantity().equals("0") ? 0 : Integer.valueOf(size.getQuantity());
					butSize.setQuantity(qty);
					if(qty > 0 )
						butSize.setBackgroundResource(R.drawable.merchandiseitemsizecommon2x);
					else
						butSize.setBackgroundResource(R.drawable.merchandiseitemsizedisable2x);
					sizeLayout.addView(butSize); 
				}
				
				//设置图片
				List picmaps = spdetail.getPictues();
    			imageRoundView = (ImageRoundView)getView().findViewById(R.id.spdetail_imageRoundView);
    			for(int i = 0; i < picmaps.size(); i++){
    				Map picmap = (Map) picmaps.get(i);
    				String imageUrl = (String) picmap.get("imageUrl");
					imageRoundView.addImage(imageUrl);
				} 
				int w = imageRoundView.getMeasuredWidth();
				int h = imageRoundView.getMeasuredHeight();
				imageRoundView.setBitmap(true);
				imageRoundView.initUI(getActivity());
				break;
			}
			case FragmentEx.UPDATE_SHOPCARTQUANTITY:{
				setShoCartQuantity();
				break;
			}
			case FragmentEx.OPEN_WINDOW:{
				//打开支付窗口
    			if(popupPayWindow == null){
   					 popupPayWindow = new PopupWindow(popLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
   					 popupPayWindow.setFocusable(true);  
   					 popupPayWindow.setTouchable(true);
   					 popupPayWindow.setOutsideTouchable(true);   
   					 popupPayWindow.setBackgroundDrawable(getDrawable());   
   				} 
   				
   				Rect viewRect = new Rect();  
   				getView().findViewById(R.id.spdetail_one).getGlobalVisibleRect(viewRect);  
   				ImageView img = (ImageView) popLayout.findViewById(R.id.imageView_singlepay);
   				img.setImageBitmap(imageRoundView.getBitmap());
   				popupPayWindow.showAtLocation(getView().findViewById(R.id.spdetail_one), Gravity.CENTER|Gravity.TOP, viewRect.left, viewRect.top);
   				
   				//启动定时器，4秒后关闭
   				final Timer timer = new Timer();
   				TimerTask task = new TimerTask(){
   					@Override
   					public void run(){
   						mCommodHandler.sendEmptyMessage(FragmentEx.CLOSE_WINDOW);
   					}
   				};
   				timer.schedule(task, 4000);
				break;
			}		
			case FragmentEx.CLOSE_WINDOW:{ 
				if(popupPayWindow != null)
					popupPayWindow.dismiss();
				break;
			}
			case FragmentEx.MESSAGEHELP:{ 
				Toast toast = Toast.makeText(getActivity(), mErrorMsg, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show(); 
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
		mView = inflater.inflate(R.layout.spdetail_layout, container, false);
		return mView;
		
	}  
	
	public View getView(){
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);    
		//设置购物车加减、删除事件
		buyQuantity = new BuyQuantityEvent(getView().findViewById(R.id.buy_quantity_fragment));
		buyQuantity.initEvent();
		
		//获取传递过来的参数
		Bundle bund = getArguments();
		item = bund.getString("item");
		activity_id = bund.getString("activity_id");
		 
		View vl = setLeftLayout(R.id.include1, R.layout.header_back_layout);
		vl.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 popBackStack();
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
		textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText(""); 
		 
		//隐藏首页页页脚导航
		getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		
		//添加尺码组左右方向按钮事件
		final HorizontalScrollView hr = (HorizontalScrollView) getView().findViewById(R.id.horizontalScrollView1);
		ImageView size_left = (ImageView) getView().findViewById(R.id.imageView_size_left);
		size_left.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				hr.arrowScroll(View.FOCUS_LEFT);
			} 
		});
		
		ImageView size_right = (ImageView) getView().findViewById(R.id.imageView_size_right);
		size_right.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				hr.arrowScroll(View.FOCUS_RIGHT);
			} 
		});
		
		//加入购物袋按钮事件
		Button butAddShopCart = (Button) getView().findViewById(R.id.button_addshopcart);
		butAddShopCart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				User user = GlobalData.gUser;
				final String token = user.getUserToken(); 
				
				//判断token是否为空，为空跳转到注册登陆窗口
				if(token == null || "".equals(token)){
					LoginFragment login = new LoginFragment(); 
					GlobalData app = (GlobalData) getActivity().getApplication();
					FragmentManagerEx fm = app.getFragment();
					fm.add(true, false, login, LoginFragment.class.getName()); 
					return;
				}
				
				final int quantity = buyQuantity.getQuantity();
				if(quantity < 1) {
					Toast toast = Toast.makeText(getActivity(), "请输入数量!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show(); 
					return;
				}
				
				if("".equals(size)) {
					Toast toast = Toast.makeText(getActivity(), "请选择尺码!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show(); 
					return;
				}
				
				new Thread(){  
					@Override  
					public void run(){ 
						mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
						
						ShoppingCartRequest req = new ShoppingCartRequest();
						req.put("token", token);  
						req.put("item", item);
						req.put("activity_id", activity_id);
						req.put("size", size); 
						req.put("quantity", String.valueOf(quantity)); 
						ShoppingCartResponse res;
						try {
							res = req.addShopCart(ShoppingCartResponse.class.getName());
							//请求成功
							if(res.getResponseStatus() == 0 && "0".equals(res.getStatus())){
								//启动更新购物车线程
								GlobalData.gShopCartQty = res.getQuantity(); 
			        			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_SHOPCARTQUANTITY);  
			        			mCommodHandler.sendEmptyMessage(FragmentEx.OPEN_WINDOW);   
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
				}.start();  
			}
			
		});
		
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//获取支付窗口布局，加载事件 
		popLayout = (LinearLayout) inflater.inflate(R.layout.singlepay_layout, null, false);
		Button butSingPay = (Button) popLayout.findViewById(R.id.but_sing_pay);
		butSingPay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(popupPayWindow != null){
					popupPayWindow.dismiss();
				}
				ShoppingCartFragment shopCartFragment = new ShoppingCartFragment(); 
				GlobalData app = (GlobalData) getActivity().getApplication();
				FragmentManagerEx fm = app.getFragment();
				fm.add(true, true, shopCartFragment, ShoppingCartFragment.class.getName()); 
			} 
		}); 
		
		Button button_fx = (Button) getView().findViewById(R.id.button_fx);
		button_fx.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(popupFxWindow == null){
					initPopupWindow();
				} 
				backgroundAlpha(0.5f);
				popupFxWindow.showAtLocation(getView(), Gravity.CENTER|Gravity.BOTTOM, 0, 0); 
			}
			
		}); 
		
		Button button_xyqd = (Button) getView().findViewById(R.id.button_xyqd);
		button_xyqd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Thread thread = new Thread(new Runnable() {  
					@Override  
					public void run() {  
						mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
						String item = spdetail.getCode();
						String activity_id = spdetail.getActivity_id();
						String token = GlobalData.gUser.getUserToken();
						FavoriteRequest req = new FavoriteRequest(); 
						req.put("token", token);
						req.put("activity_id", activity_id);
						req.put("item", item);
						FavoriteResponse res;
						try {
							res = req.doPost(FavoriteResponse.class.getName());
							if(res.getResponseStatus() < 0){
								mErrorMsg = res.getResponseStatusMessage(res.getResponseStatus());
								mCommodHandler.sendEmptyMessage(FragmentEx.MESSAGEHELP); 
								return;
							}
							
							if(!"0".equals(res.getStatus())){
								mErrorMsg = res.getErrorMessage();
								mCommodHandler.sendEmptyMessage(FragmentEx.MESSAGEHELP); 
								return;
							}
							
							//请求成功
							if(res.getResponseStatus() == 0 && "0".equals(res.getStatus())){ 
								mErrorMsg = "收藏成功！";
								if(!res.getErrorMessage().equals("ok")){
									mErrorMsg = res.getErrorMessage();
								}
								mCommodHandler.sendEmptyMessage(FragmentEx.MESSAGEHELP);
								return;
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
			
		}); 
		
		final TextView textView_detailScription = (TextView) getView().findViewById(R.id.textView_detailScription);
		final TextView textView_show = (TextView) getView().findViewById(R.id.textView_show);
		textView_show.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String show = (String) textView_show.getText();
				if("+".equals(show)){
					textView_show.setText("-");
					textView_detailScription.setVisibility(View.VISIBLE);
				}
				else{
					textView_show.setText("+");
					textView_detailScription.setVisibility(View.GONE);
				}
			}
			
		}); 
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
			//显示购物车数量
			mShopCart.setmQuantity(GlobalData.gShopCartQty); 
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
	
	public void backgroundAlpha(float bgAlpha){
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0  
		getActivity().getWindow().setAttributes(lp);  
    }  
	
	private void initPopupWindow() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout popLayout = (LinearLayout) inflater.inflate(R.layout.fx_layout, null, false);
		popupFxWindow = new PopupWindow(popLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupFxWindow.setFocusable(true);  
		popupFxWindow.setTouchable(true);
		popupFxWindow.setOutsideTouchable(true);   
		popupFxWindow.setBackgroundDrawable(getDrawable());
		backgroundAlpha(0.5f);
		popupFxWindow.setOnDismissListener(this);  
		 
		Button button_cancel = (Button) popLayout.findViewById(R.id.button_cancel); 
		button_cancel.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 popupFxWindow.dismiss();
			 }
		});  
	}
	
	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		backgroundAlpha(1f);
	}
	
	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		mLoadingProgress.show();
		
		Thread thread = new Thread(new Runnable(){
			@Override  
			public void run(){   
				try { 
					//重新加载数据
					if(isAllRefresh){
						SpdetailRequest req = new SpdetailRequest();
		        		req.put("item", item);
		        		req.put("Activity_id", activity_id);
		        		SpdetailResponse spRsp;
		        		spRsp = req.doGet(SpdetailResponse.class.getName()); 

	        			spdetail = spRsp.getSpdetail(); 
	        			if(spdetail == null){
	        				new BusException("", "获取商品明细错误！");
	        				return;
	        			}
	        			//启动更新图片控件 线程
	        			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_COMMODITY_SIZE);
	        			 
	        			/*List picmaps = spdetail.getPictues();
	        			ImageRoundView imageRoundView = (ImageRoundView)getView().findViewById(R.id.spdetail_imageRoundView);
	        			for(int i = 0; i < picmaps.size(); i++){
	        				Map picmap = (Map) picmaps.get(i);
	        				String imageUrl = (String) picmap.get("imageUrl");
	    					imageRoundView.addImage(imageUrl);
	    				} 
	    				int w = imageRoundView.getMeasuredWidth();
	    				int h = imageRoundView.getMeasuredHeight();
	    				imageRoundView.setBitmap(true);
	    				imageRoundView.initUI(getActivity()); */
	        			/*List picmaps = spdetail.getPictues();
	        			bitmap = new Bitmap[picmaps.size()];
	        			for(int i = 0; i < picmaps.size(); i++){
	        				Map picmap = (Map) picmaps.get(i);
	        				String imageUrl = (String) picmap.get("imageUrl");
	        				bitmap[i] = WebUtils.doGetBitmap(imageUrl);  
	        			}
	        			
	        			//启动更新图片控件 线程
	        			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_COMMODITY_IMAGE); */
	        			
	        			isAllRefresh = false;
					}
					else{
						//刷新数据
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					mCommodHandler.sendEmptyMessage(FragmentEx.HIDELOADINGPROGRESS);
				}
            }  
        });  
        thread.start();  
	}
}
