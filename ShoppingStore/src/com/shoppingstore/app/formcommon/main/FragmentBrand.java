package com.shoppingstore.app.formcommon.main;
 
import java.util.ArrayList;
import java.util.List; 

import org.json.JSONException;

import com.shoppingstore.app.R; 
import com.shoppingstore.app.common.bean.BrandBean;
import com.shoppingstore.app.common.bean.ShopCartItemBean;  
import com.shoppingstore.app.common.bean.User;
import com.shoppingstore.app.common.global.GlobalData;  
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.formcommon.Adapter.BrandAdapter;
import com.shoppingstore.app.formcommon.Adapter.CxAdapter;
import com.shoppingstore.app.formcommon.Adapter.ShopCartItemAdapter; 
import com.shoppingstore.app.formcommon.utils.FragmentEx;   
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.ShopCartImageView;
import com.shoppingstore.app.internal.WebUtils; 
import com.shoppingstore.app.internal.request.BrandRequest;
import com.shoppingstore.app.internal.request.CxListRequest;
import com.shoppingstore.app.internal.request.ShoppingCartRequest; 
import com.shoppingstore.app.internal.response.BrandResponse;
import com.shoppingstore.app.internal.response.CxListResponse;
import com.shoppingstore.app.internal.response.ShoppingCartResponse; 
 
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment; 
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.ViewGroup;  
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;  
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView; 
 
/**
 * 促销活动
 * @author meicunzhi
 * @date 2015-11-12 上午4:36:45
 */
public class FragmentBrand extends FragmentEx {  
	private List<BrandBean> mBrands; 
	private BrandAdapter mBrandAdapter;
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			mLoadingProgress.hide();
			
			switch (msg.what) { 
			case FragmentEx.UPDATE_VIEW: {
				//显示购物车页面 
				ListView listView =  (ListView) getView().findViewById(R.id.listView_brandlist);  
				if(mBrandAdapter == null){
					mBrandAdapter = new BrandAdapter(getActivity(), mBrands);
					listView.setAdapter(mBrandAdapter);  
				}
				else{
					mBrandAdapter.setBrandList(mBrands);
					mBrandAdapter.notifyDataSetChanged();
				}  
				 
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
		return inflater.inflate(R.layout.brand_layout, container, false);
		
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
		
		//添加购物车布局
		View vr = setRightLayout(R.id.header_bar, R.layout.header_shopcart_layout);
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
		View vc = setCenterLayout(R.id.header_bar, R.layout.header_textview_layout);
		TextView textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText("品牌文化"); 
		 
		//获取商品详情请求线程
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
		Thread thread = new Thread(new Runnable(){
            @Override  
            public void run(){
            	mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
            	BrandRequest req = new BrandRequest(); 
            	try {
            		//加载数据
            		if(isAllRefresh){
            			BrandResponse res = req.doGet(BrandResponse.class.getName());
    					List<BrandBean> brands = res.getBrandList();
    					if(brands == null) return;
    					if(mBrands == null){
    						mBrands = new ArrayList<BrandBean>();
    					}
    					//显示信息
    					for(int i = 0; i < brands.size(); i++){
    						BrandBean cx = brands.get(i); 
    						String item = cx.getItem(); 
    						int updateRow = -1;
    						for(int x = 0; x < mBrands.size(); x++){
    							BrandBean oldcx = mBrands.get(x); 
    							String oldItem = oldcx.getItem();
    							if(item.equals(oldItem)){
    								cx.setBitmap(oldcx.getBitmap()); 
    								updateRow = x;
    								break;
    							}
    						}
    						if(updateRow < 0)
    							mBrands.add(cx);
    						else
    							mBrands.set(updateRow, cx);  
    					} 
    					//启动更新线程
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
