package com.shoppingstore.app.formcommon.main;
 
import java.util.ArrayList;
import java.util.List; 

import org.json.JSONException;

import com.shoppingstore.app.R; 
import com.shoppingstore.app.common.bean.CxBean;
import com.shoppingstore.app.common.bean.ShopCartItemBean;  
import com.shoppingstore.app.common.bean.User;
import com.shoppingstore.app.common.global.GlobalData;  
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.formcommon.Adapter.CxAdapter;
import com.shoppingstore.app.formcommon.Adapter.ShopCartItemAdapter; 
import com.shoppingstore.app.formcommon.utils.FragmentEx;   
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.internal.WebUtils; 
import com.shoppingstore.app.internal.request.CxListRequest;
import com.shoppingstore.app.internal.request.ShoppingCartRequest; 
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
import android.widget.TextView; 
 
/**
 * 促销活动
 * @author meicunzhi
 * @date 2015-11-12 上午4:36:45
 */
public class CxFragment extends FragmentEx {  
	private List<CxBean> mCxs; 
	private CxAdapter mCxAdapter;
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) { 
			case FragmentEx.UPDATE_VIEW: {
				//显示购物车页面
    			if(mCxs.size() == 0){
    				getView().findViewById(R.id.empty_shopcart).setVisibility(View.VISIBLE); 
    				getView().findViewById(R.id.my_shopcart).setVisibility(View.GONE); 
    			}
    			else{
    				getView().findViewById(R.id.empty_shopcart).setVisibility(View.GONE); 
    				getView().findViewById(R.id.my_shopcart).setVisibility(View.VISIBLE); 
    			}
    			
				GridView gridView =  (GridView) getView().findViewById(R.id.listView1);  
				if(mCxAdapter == null){
					mCxAdapter = new CxAdapter(getActivity(), mCxs);
					gridView.setAdapter(mCxAdapter);  
				}
				else{
					mCxAdapter.setCxList(mCxs);
					mCxAdapter.notifyDataSetChanged();
				}  
				 
        		break;
			}
			case FragmentEx.UPDATE_COMMODITY_IMAGE: {
				mCxAdapter.notifyDataSetChanged();
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
		return inflater.inflate(R.layout.cxlist_layout, container, false);
		
	}  
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		//获取商品详情请求线程
		loadData(); 
		
		View vl = setLeftLayout(R.id.header_bar, R.layout.header_back_layout);
		vl.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 popBackStack();
			 }
		});  
		
		//设置标题栏
		View vc = setCenterLayout(R.id.header_bar, R.layout.header_textview_layout);
		TextView textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText("活动"); 
		
		Button button_backhome = (Button) getView().findViewById(R.id.button_backhome);
		button_backhome.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 FragmentHome home = new FragmentHome();
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment();
				 fm.add(false, false, home, FragmentHome.class.getName());
			 }
		});  
		 
		Button button_gobuy = (Button) getView().findViewById(R.id.button_gobuy);
		button_gobuy.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 PayFragment pay = new PayFragment();
				 GlobalData app = (GlobalData) getActivity().getApplication();
				 FragmentManagerEx fm = app.getFragment();
				 fm.add(true, false, pay, PayFragment.class.getName());
			 }
		});  
		
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
				try {
					//重新加载
					if(isAllRefresh){ 
						CxListRequest req = new CxListRequest(); 
		            	CxListResponse res;
						res = req.doGet(CxListResponse.class.getName());
						List<CxBean> cxs = res.getCxList();
						if(cxs == null) return;
						if(mCxs == null){
							mCxs = new ArrayList<CxBean>();
						}
						//显示信息
						for(int i = 0; i < cxs.size(); i++){
							CxBean cx = cxs.get(i); 
							String item = cx.getItem(); 
							int updateRow = -1;
							for(int x = 0; x < mCxs.size(); x++){
								CxBean oldcx = mCxs.get(x); 
								String oldItem = oldcx.getItem();
								if(item.equals(oldItem)){
									cx.setBitmap(oldcx.getBitmap()); 
									updateRow = x;
									break;
								}
							}
							if(updateRow < 0)
								mCxs.add(cx);
							else
								mCxs.set(updateRow, cx);  
						} 
						//启动更新线程
	        			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW);
	        			
	        			//显示图片
	        			for(int i = 0; i < mCxs.size(); i++){
	        				CxBean cx = mCxs.get(i);
							String imgurl = cx.getImgUrl();
							//获取图片资源
							if(cx.getBitmap() == null){
								Bitmap bmp = WebUtils.doGetBitmap(imgurl);  
		        				cx.setBitmap(bmp);
		        				mCxs.set(i, cx);    
							} 
	        				//启动线程
	            			mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_COMMODITY_IMAGE);
						}  
	        			
	        			isAllRefresh = false;
					}
					else{
						//刷新部分数据
					}					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	 
            }
        });  
        thread.start();  
	}
	 
}
