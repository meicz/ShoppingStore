package com.shoppingstore.app.formcommon.main;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.FavoriteBean;
import com.shoppingstore.app.common.global.GlobalData; 
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.formcommon.Adapter.CommodityAdapter;
import com.shoppingstore.app.formcommon.Adapter.FavoriteAdapter;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.ShopCartImageView;
import com.shoppingstore.app.internal.WebUtils;
import com.shoppingstore.app.internal.request.CxSpListRequest;
import com.shoppingstore.app.internal.request.FavoriteRequest;
import com.shoppingstore.app.internal.request.SpdetailRequest;
import com.shoppingstore.app.internal.request.SplistRequest;
import com.shoppingstore.app.internal.response.FavoriteResponse;
import com.shoppingstore.app.internal.response.SpdetailResponse;
import com.shoppingstore.app.internal.response.SplistResponse; 

public class FragmentFavorite extends FragmentEx implements OnClickListener{     
	
	private PopupWindow mPopupWindow;  
	private View mPopupView;
	private ImageView imageView_Left;
	private ImageView imageView_Right;
	private TextView textView_Title;  
	private List<FavoriteBean> mFavoriteList;	//商品列表
	 
	private ImageView imageView_imageUrl;
	private ShopCartImageView mShopCart;
	private FavoriteAdapter mFavoriteAdapter = null;	//商品信息适配器
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	mLoadingProgress.hide();
        	
        	switch (msg.what) {
        	case FragmentEx.UPDATE_VIEW: {
        		GridView gridView =  (GridView) getView().findViewById(R.id.gridView1);  
        		if(mFavoriteAdapter == null){
        			mFavoriteAdapter = new FavoriteAdapter(getActivity(), mFavoriteList); 
        		}
        		gridView.setAdapter(mFavoriteAdapter);  
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
		return inflater.inflate(R.layout.favorite_list_layout, container, false);
		
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);   
		//获取传递过来的参数 
		//设置标题栏
		View vc = setCenterLayout(R.id.header_menubar, R.layout.header_textview_layout);
		textView_Title = (TextView) vc.findViewById(R.id.header_text);   
		textView_Title.setText("我的收藏夹");
		 
		//添加购物车布局
		View vr = setRightLayout(R.id.header_menubar, R.layout.header_shopcart_layout);
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
				
		final GridView gridView =  (GridView) getView().findViewById(R.id.gridView1); 
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));  
		gridView.setOnItemClickListener(new OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				// TODO Auto-generated method stub
				FavoriteBean com = mFavoriteList.get(position);
				String item = com.getItem();
				String activity_id = com.getActivity_id();
				
				Bundle bu = new Bundle(); 
		    	bu.putString("item", item);
		    	bu.putString("activity_id", activity_id);
		    	SpdetailFragment spdetail = new SpdetailFragment();
		    	spdetail.setArguments(bu);
		    	GlobalData app = (GlobalData) getActivity().getApplication();
		    	FragmentManagerEx fm = app.getFragment();
		    	fm.add(true, false, spdetail, "spdetail" + item);
			}
		});  
		
		loadData();
    }  

	@Override  
	public void onHiddenChanged(boolean hidden){
		super.onHiddenChanged(hidden);  
		if(hidden){
			//隐藏界面 
		}
		else{
			//显示界面
			mShopCart.setmQuantity(GlobalData.gShopCartQty);
			loadData();
		}
	}
	
	/**
	 * 显示按钮状态
	 * @param imgv
	 * @param tagName
	 * @param resid
	 */
	private void showDownOrUpImage(String tagName, int resid){
		imageView_Right.setTag(tagName);
		imageView_Right.setImageResource(resid);
	}

	/**
	 * 根据性别显示、隐藏菜单
	 * @param sex
	 * @param isSHow
	 */
	private void showPopUpMenu(boolean isSHow){
		if(mPopupWindow == null){
			initPopUpMenu();
		} 
		if(isSHow){
			
		}
		else{
			mPopupWindow.dismiss();
		} 
	}
 
	/**
	 * 初始化菜单
	 */
	private void initPopUpMenu(){
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//获取菜单布局文件，生成视图 
		mPopupView = inflater.inflate(R.layout.sexmenu_layout, null, false);  
        //创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度  
		mPopupWindow = new PopupWindow(mPopupView, LayoutParams.MATCH_PARENT, 50, true);  
        //设置动画效果  
        //popupWindow.setAnimationStyle(R.style.AnimationFade);  
        //点击其他地方消失  
		mPopupView.setOnTouchListener(new OnTouchListener() {   
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (mPopupWindow != null && mPopupWindow.isShowing()) {  
					showDownOrUpImage("down", R.drawable.commonarrowdown2x); 
					mPopupWindow.dismiss();  
                }  
                return false;   
			}  
        });   
	}

	/**
	 * 按钮或菜单点击事件
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 
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
		setPush(isAddStack);
		setFragmentName(nowFragmentName); 
	}
	
	@Override
	public void popBackStack() {
		// TODO Auto-generated method stub
		mBackHandledInterface.popBackStack();
	} 
	 
	@Override
	public void loadData() {
		// TODO Auto-generated method stub 
		//获取商品列表请求线程 
		Thread thread = new Thread(new Runnable() {  
			@Override  
			public void run() { 
				try {
					mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
					
					if(isAllRefresh){
						//加载普通商品
						String token = GlobalData.gUser.getUserToken();
						FavoriteRequest req = new FavoriteRequest(); 
						req.put("token", token);
						FavoriteResponse res = req.doGet(FavoriteResponse.class.getName()); 
						mFavoriteList = res.getFavoriteList();
						
						//获取信息
						for(int i = 0; i < mFavoriteList.size(); i++){
							FavoriteBean com = mFavoriteList.get(i);
							mFavoriteList.set(i, com);    
						} 
						//启动线程更新
						mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW);
						 
						isAllRefresh = false;
					}
					else{
						//刷新部分数据
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
}
