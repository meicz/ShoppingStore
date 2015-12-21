package com.shoppingstore.app.formcommon.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.FavoriteBean;
import com.shoppingstore.app.common.bean.SxSetBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.Adapter.CommodityAdapter;
import com.shoppingstore.app.formcommon.Adapter.CommodityFilterAdapter;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.RadioButtonLayout;
import com.shoppingstore.app.formcommon.utils.SerializableMap;
import com.shoppingstore.app.internal.request.SxSetRequest;
import com.shoppingstore.app.internal.response.SxSetResponse;

/**
 * 商品过滤
 * @author meicunzhi
 * @date 2015-12-5 下午7:31:05
 */
public class CommodityFilterFragment extends FragmentEx {
	 
	private String mSx;		//大类:01 男 02女
	private String mKind;	//小类:01鞋、02配饰、03皮衣、04包
	private String mType;	//1、小类  2、价格  3、折扣 4、尺寸

	private List<RadioButtonLayout> mFilterButtons = new ArrayList<RadioButtonLayout>(); 
	private int mCurrentFilterButtonId;				//当前选择对象的ID
	
	//条件map
	private Map<String, List<SxSetBean>> mSxs = new HashMap<String, List<SxSetBean>>();
	private CommodityFilterAdapter mFilterAdapter;
	/**
	 * 判断是否选择了条件
	 */
	private boolean mIsSelect = false;		
	private List<SxSetBean> mCurrentSxs;	//当前选择的对象
	 
	private int mOldButtonId = -1;			//保留以前点击的buttonid
	
	/**
	 * 筛选条件
	 */
	private Map<String, Object> mFilterMap = new HashMap<String, Object>();
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) { 
			mLoadingProgress.hide();
			
			switch (msg.what) {
			case FragmentEx.UPDATE_VIEW: {
				GridView gridView =  (GridView) getView().findViewById(R.id.gridView1);  
				if(mFilterAdapter == null){
					mFilterAdapter = new CommodityFilterAdapter(getActivity(), mCurrentSxs);
					gridView.setAdapter(mFilterAdapter);    
				} 
				else{
					mFilterAdapter.setCommodList(mCurrentSxs);
					mFilterAdapter.notifyDataSetChanged();
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
	
	//排序按钮事件
	OnClickListener mButFilterClick = new View.OnClickListener(){ 
		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub 
			//设置选中
			for(int i = 0; i < mFilterButtons.size(); i++){
				RadioButtonLayout b = (RadioButtonLayout) mFilterButtons.get(i);
				if(b.equals(but)){ 
					if(b.getSelect()){ 
						b.setSelect(false);  
						if(mCurrentSxs != null){  
							//设置是否有选中状态
							setSelectFlag(b.getId(), mCurrentSxs);
						}
					}
					else{ 
						//选中
						b.setSelect(true);   
						if(b.getId() == R.id.button_spxl){
							mType = "1";
						} else if(b.getId() == R.id.button_price){
							mType = "2";
						} else if(b.getId() == R.id.button_discount){
							mType = "3";
						} else if(b.getId() == R.id.button_size){
							mType = "4";
						}
						
						//设置以前点击按钮的选中状态，设置好之后用当前选的按钮Id替换以前的
						if(mCurrentSxs != null){  
							setSelectFlag(mOldButtonId, mCurrentSxs);
						}
						mOldButtonId = b.getId();
						
						//首次先获取数据
						mCurrentFilterButtonId = b.getId();
						mCurrentSxs = mSxs.get(buttonIdToName(mCurrentFilterButtonId));
						if(mCurrentSxs == null || mCurrentSxs.isEmpty()){
							loadData(); 
						}
						else{
							//刷新GridView
							mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW);
						}  
					} 
				}
				else{ 
					b.setSelect(false);   
				}
			} 
		} 
	};  
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.find_layout, container, false);
		
	} 
	
	/**
	 * 设置过滤条件按钮选中状态
	 * @param id
	 * @param isSelect
	 */
	protected void setSelectFlag(int id, List<SxSetBean> currentSxs) {
		// TODO Auto-generated method stub
		RadioButtonLayout dd = (RadioButtonLayout) getView().findViewById(id);
		ImageView imge = (ImageView) dd.findViewById(R.id.imageView1);
		
		//判断是否有选中的项，有就设置选中状态
		boolean falg = false; 
		for(int i = 0; i < currentSxs.size(); i++){
			SxSetBean sx = currentSxs.get(i);
			if(sx.isSelect()){
				falg = true;
				break;
			}
		}
		if(falg){
			imge.setBackgroundResource(R.drawable.commondot2x); 
		}
		else{
			imge.setBackgroundResource(0);
		} 
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
		View vr = setRightLayout(R.id.header_bar, R.layout.header_close_layout);
		vr.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 
				 popBackStack();
			 }
		}); 
		  
		//设置标题栏
		View vc = setCenterLayout(R.id.header_bar, R.layout.header_textview_layout);
		TextView textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		textView_Title.setText("精准筛选");  
		
		//动态添加筛选按钮和事件
		LinearLayout filterlayout1 = (LinearLayout) getView().findViewById(R.id.layout_filter1);
		for(int i = 0; i < filterlayout1.getChildCount(); i++){
			View view = filterlayout1.getChildAt(i);
			if(view instanceof RadioButtonLayout){
				RadioButtonLayout but = (RadioButtonLayout) view;
				but.setOnClickListener(mButFilterClick);
				mFilterButtons.add(but);
				
				mSxs.put(buttonIdToName(but.getId()), new ArrayList<SxSetBean>()); 
			}
		}
		 
		//清除条件
		Button button_clear = (Button) getView().findViewById(R.id.button_clear);
		button_clear.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) { 
				mIsSelect = false;
				Iterator it = mSxs.entrySet().iterator();
				while(it.hasNext()){
					Entry e = (Entry)it.next();
					String name = (String) e.getKey();
					List<SxSetBean> sxs = (List<SxSetBean>) e.getValue();
					for(int x = 0; x < sxs.size(); x++){
						SxSetBean sx = sxs.get(x);
						sx.setSelect(false);
					}
					mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW); ;
				}
				
				for(int i = 0; i < mFilterButtons.size(); i++){
					mFilterButtons.get(i).setSelect(false);
					ImageView imge = (ImageView) mFilterButtons.get(i).findViewById(R.id.imageView1);
					imge.setBackgroundResource(0);
				}
				popBackStack();
			}
		}); 
		
		//确认
		Button button_ok = (Button) getView().findViewById(R.id.button_ok);
		button_ok.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) { 
				mIsSelect = true;
				popBackStack();
			}
		}); 
		//隐藏首页页页脚导航
		getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE);  
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
			mFilterMap.put("isselect", mIsSelect);
			mFilterMap.put("sex", mSxs);
			if(mCallBackFragment != null)
				mCallBackFragment.callBack(mFilterMap);
		}
		else{
			//显示界面
			getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE);   
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub 
		Bundle bundle = getArguments();
		if(bundle == null){
			return;
		}
		
		SerializableMap serMap = (SerializableMap) bundle.getSerializable("find");
		if(serMap == null){
			return;
		}
		mSx = (String) serMap.get("sx");
		mKind = (String) serMap.get("kind"); 
		
		Thread thread = new Thread(new Runnable()  
        {  
            @Override  
            public void run(){
            	mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
            	SxSetRequest req = new SxSetRequest();
            	req.put("sx", mSx);
            	req.put("kind", mKind);
            	req.put("type", mType);
            	try {
            		SxSetResponse res = req.doGet(SxSetResponse.class.getName()); 
            		//尺码和其它类型返回的结构不一样，分开获取
            		if("4".equals(mType)){
            			mCurrentSxs = res.getSizeSxs();
            		}
            		else{
            			mCurrentSxs = res.getSxs();
            		}
            		mSxs.put(buttonIdToName(mCurrentFilterButtonId), mCurrentSxs);
            		mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					mCommodHandler.sendEmptyMessage(FragmentEx.HIDELOADINGPROGRESS);
				}
            }
        });  
        thread.start(); 
	}

	public boolean isSelect() {
		return mIsSelect;
	}

	public void setSelect(boolean isSelect) {
		this.mIsSelect = isSelect;
	} 
	
	
	public String buttonIdToName(int buttonid){
		String name = "";
		
		switch(buttonid){
		case R.id.button_spxl:
			name = "spxl";
			break;
		case R.id.button_price:
			name = "price";
			break;
			
		case R.id.button_discount:
			name = "discount";
			break;
			
		case R.id.button_size:
			name = "size";
			break;			
		}
		
		return name;
	}
}
