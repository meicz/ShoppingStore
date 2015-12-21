package com.shoppingstore.app.formcommon.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.CommodityListBean;
import com.shoppingstore.app.common.bean.SxSetBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.Adapter.CommodityAdapter;
import com.shoppingstore.app.formcommon.utils.CallBackInterface;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.RadioButtonLayout;
import com.shoppingstore.app.formcommon.utils.SerializableMap;
import com.shoppingstore.app.formcommon.utils.ShopCartImageView;
import com.shoppingstore.app.internal.WebUtils;
import com.shoppingstore.app.internal.request.CxSpListRequest;
import com.shoppingstore.app.internal.request.SplistRequest;
import com.shoppingstore.app.internal.response.SplistResponse;

public class FragmentCommodityLists extends FragmentEx implements OnClickListener, CallBackInterface, PopupWindow.OnDismissListener{   
	private String mCategory; 		//大类编码
	private String mSubcategory; 	//小类编码
	private String mCallFragmentName;
	
	private PopupWindow mPopupWindow;  
	private View mPopupView;
	private ImageView imageView_Left;
	private ImageView imageView_Right;
	private TextView textView_Title;  
	private List<CommodityListBean> mCommodList = new ArrayList<CommodityListBean>();	//商品列表
	
	//排序弹出框
	private boolean isSelectStore = false;
	private PopupWindow popupStoreWindow = null; 	//分享弹出窗
	List<RadioButtonLayout> storeRadios = new ArrayList<RadioButtonLayout>();
 
	//精确查找
	private boolean isSelectFilter = false;  
	private String mIsactivity; 	//是否是活动商品
	
	private Map<String, String> mWhereMap = null;
	
	private ShopCartImageView mShopCart;
	private CommodityAdapter mCommodAdapter = null;	//商品信息适配器
	
	private List<RadioButtonLayout> radios = new ArrayList<RadioButtonLayout>();
	 
	
	OnClickListener butClick = new View.OnClickListener(){ 
		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub 
			RadioButtonLayout b = (RadioButtonLayout) but;
			b.setSelect(true); 
			
			if(but.getId() == R.id.button_sort){
				//打开排序查询窗口
				if(popupStoreWindow == null){
					initPopStoreWindow();
				}
				backgroundAlpha(0.5f);
				Rect viewRect = new Rect();  
				getView().findViewById(R.id.gridView1).getGlobalVisibleRect(viewRect); 
				popupStoreWindow.showAtLocation(getView(), Gravity.LEFT|Gravity.TOP, viewRect.left, viewRect.top); 
			}
			else if(but.getId() == R.id.button_filter){
				//打开精准查询窗口 				
				CommodityFilterFragment comFilter = (CommodityFilterFragment) mFragmentManagerEx.findFragment(CommodityFilterFragment.class.getName());
				if(comFilter == null){
					comFilter = new CommodityFilterFragment();   
				} 
				Bundle bundle = comFilter.getArguments();
				SerializableMap serMap; 
				if(bundle == null){ 
					bundle= new Bundle(); 
					serMap = new SerializableMap();
					serMap.put("sx", mCategory);
					serMap.put("kind", mSubcategory);  
					bundle.putSerializable("find", serMap);
					bundle.putString(FragmentEx.CALLBACKFRAGMENTNAME, mCallFragmentName); 
					comFilter.setArguments(bundle);
				}
				else{
					serMap = (SerializableMap) bundle.get("find");
					serMap.put("sx", mCategory);
					serMap.put("kind", mSubcategory); 
				} 
				mFragmentManagerEx.add(true, false, comFilter, CommodityFilterFragment.class.getName()); 
			} 
		} 
	};  
	
	//排序按钮事件
	OnClickListener butStoreClick = new View.OnClickListener(){ 
		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub 
			//设置选中
			for(int i = 0; i < storeRadios.size(); i++){
				RadioButtonLayout b = (RadioButtonLayout) storeRadios.get(i);
				if(b.equals(but)){ 
					if(b.getSelect()){ 
						b.setSelect(false); 
						isSelectStore = false;
						//执行查询
						mWhereMap.put("sortField", "");
						isAllRefresh = true;
						loadData();
					}
					else{ 
						b.setSelect(true);  
						isSelectStore = true;  
						
						/*//删除精准查询条件
						mWhereMap.put("spxl", "");
						mWhereMap.put("price", "");
						mWhereMap.put("discount", "");
						mWhereMap.put("size", "");*/
						
						//执行查询
						if(b.getId() == R.id.button_new){
							mWhereMap.put("sortField", "new");
						} else if(b.getId() == R.id.button_pric){
							mWhereMap.put("sortField", "price");
						} else if(b.getId() == R.id.button_pricedesc){
							mWhereMap.put("sortField", "pricedesc");
						} else if(b.getId() == R.id.button_rx){
							mWhereMap.put("sortField", "hot");
						} else if(b.getId() == R.id.button_hot){
							mWhereMap.put("sortField", "hot");
						} 
						isAllRefresh = true;
						loadData();
						/*//清除精准条件
						isSelectFilter = false;
						clearFilter();*/
					} 
				}
				else{ 
					b.setSelect(false);
				}
			}
			
			//隐藏窗口
			popupStoreWindow.dismiss();
		} 
	};  
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	mLoadingProgress.hide();
        	
        	switch (msg.what) {
        	case FragmentEx.UPDATE_VIEW: {
        		GridView gridView =  (GridView) getView().findViewById(R.id.gridView1);
        		gridView.setOnTouchListener(new OnTouchListener(){

					@Override
					public boolean onTouch(View arg0, MotionEvent event) {
						// TODO Auto-generated method stub
						Log.d("shopex", String.valueOf(event.getAction()));
						return false;
					}
        			
        		});
        		if(mCommodAdapter == null){
        			mCommodAdapter = new CommodityAdapter(getActivity(), mCommodList);
        			gridView.setAdapter(mCommodAdapter);   
        		}
        		else{
        			mCommodAdapter.notifyDataSetChanged();
        		}
        		break;
        	}
        	case FragmentEx.UPDATE_COMMODITY_IMAGE: {
        		mCommodAdapter.notifyDataSetChanged();
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
		return inflater.inflate(R.layout.commodity_list_layout, container, false);
		
	} 
	
	/**
	 * 初始化排序弹出窗口
	 */
	protected void initPopStoreWindow() {
		// TODO Auto-generated method stub 
		//获取排序按钮
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout find_store_layout = (LinearLayout) inflater.inflate(R.layout.find_store_layout, null, false);
		for(int i = 0; i < find_store_layout.getChildCount(); i++){
			View view = find_store_layout.getChildAt(i);
			if(view instanceof RadioButtonLayout){
				RadioButtonLayout f = (RadioButtonLayout) view;
				f.setOnClickListener(butStoreClick);
				storeRadios.add(f);
			}
		}
		Rect viewRect = new Rect();  
		getView().findViewById(R.id.gridView1).getGlobalVisibleRect(viewRect); 
		Rect footerRect = new Rect();
		getActivity().findViewById(R.id.inclue_footer_menu_layout).getGlobalVisibleRect(footerRect); 
		popupStoreWindow = new PopupWindow(find_store_layout, LayoutParams.WRAP_CONTENT, footerRect.top - viewRect.top, true);
		popupStoreWindow.setFocusable(true);  
		popupStoreWindow.setTouchable(true);
		popupStoreWindow.setOutsideTouchable(true);   
		popupStoreWindow.setBackgroundDrawable(getDrawable());
		backgroundAlpha(0.5f);
		popupStoreWindow.setOnDismissListener(this);   
	}
	
	public void backgroundAlpha(float bgAlpha){
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0  
		getActivity().getWindow().setAttributes(lp);  
    }  
	
	private Drawable getDrawable(){
		ShapeDrawable bgdrawable =new ShapeDrawable(new OvalShape());
		bgdrawable.getPaint().setColor(getActivity().getResources().getColor(android.R.color.transparent));
		return bgdrawable;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);   
		
		//初始化查询参数
		mWhereMap = new HashMap<String, String>();
		mWhereMap.put("sortField", "");
		mWhereMap.put("spxl", "");
		mWhereMap.put("price", "");
		mWhereMap.put("discount", "");
		mWhereMap.put("size", "");
		mWhereMap.put("pager", "1");
		mWhereMap.put("pageSize", "1000"); 
		
		//获取传递过来的参数
		Bundle bund = getArguments();
		mCategory = bund.getString("category");
		mSubcategory = bund.getString("subcategory");
		mIsactivity = bund.getString("isactivity");  
		mCallFragmentName = bund.getString("callFragmentName");  
		
		//设置标题栏
		View vc = setCenterLayout(R.id.header_menubar, R.layout.header_textview_layout);
		textView_Title = (TextView) vc.findViewById(R.id.header_text);  
		if(mIsactivity == null || "no".equals(mIsactivity) || "".equals(mIsactivity)){
			setTitle();
		}
		
		//设置左边栏
		View imageView_Left = setLeftLayout(R.id.header_menubar, R.layout.header_list_layout, null);
		imageView_Left.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 
			 }
		});  
		 
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
				CommodityListBean com = mCommodList.get(position);
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
		
		RadioButtonLayout button_sort = (RadioButtonLayout) getView().findViewById(R.id.button_sort);   
		RadioButtonLayout button_filter = (RadioButtonLayout) getView().findViewById(R.id.button_filter);  
		radios.add(button_sort);  
		radios.add(button_filter);  
		for(int i = 0; i < radios.size(); i++){
			radios.get(i).setOnClickListener(butClick);
		} 
		
		loadData();
    }   
	
	//设置标题
	private void setTitle() {
		// TODO Auto-generated method stub
		if(textView_Title != null){
			String title = "";
			if("01".equals(mCategory))
				title = "男士";
			else
				title = "女士";
			
			if("01".equals(mSubcategory)){
				title += "鞋履";
			}else if("02".equals(mSubcategory)){
				title += "配饰";
			}else if("03".equals(mSubcategory)){
				title += "皮衣";
			}else if("04".equals(mSubcategory)){
				title += "包";
			}
			
			textView_Title.setText(title);
		}
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
	
	private class ImageAdapter extends BaseAdapter{  
        private Context mContext;  
  
        public ImageAdapter(Context context) {  
            this.mContext=context;  
        }  
  
        @Override  
        public int getCount() {  
            return mCommodList.size();  
        }  
  
        @Override  
        public Object getItem(int position) {  
            return mCommodList.get(position);  
        }  
  
        @Override  
        public long getItemId(int position) {  
            // TODO Auto-generated method stub  
            return position;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            //定义一个ImageView,显示在GridView里 
        	LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    		View view = inflater.inflate(R.layout.commodity_layout, null, false);  
    		
    		TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
    		TextView textView_price = (TextView) view.findViewById(R.id.textView_price);
    		TextView textView_tagPrice = (TextView) view.findViewById(R.id.textView_tagPrice);  
    		ImageView imageView_imageUrl = (ImageView) view.findViewById(R.id.imageView_imageUrl);  
    		
    		//设置商品
    		CommodityListBean com = mCommodList.get(position);
    		textView_name.setText(com.getName());
    		textView_price.setText(com.getPrice());
    		textView_tagPrice.setText(com.getTagPrice()); 
    		imageView_imageUrl.setImageBitmap(com.getBitmap());
    		
    		return view; 
        }   
    }

	@Override
	public void loadData() {
		// TODO Auto-generated method stub 
		Bundle bund = getArguments();
		mCategory = bund.getString("category");
		mSubcategory = bund.getString("subcategory");
		mIsactivity = bund.getString("isactivity"); 
		final String item = bund.getString("item");
		//获取商品列表请求线程 
		Thread thread = new Thread(new Runnable() {  
			@Override  
			public void run() { 
				try {
					mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
					
					if(isAllRefresh) {
						List<CommodityListBean> commodList = new ArrayList<CommodityListBean>();
						//加载普通商品
						if(mIsactivity == null || "no".equals(mIsactivity) || "".equals(mIsactivity)){
							SplistRequest req = new SplistRequest();
							req.put("category", mCategory);
							req.put("subcategory", mSubcategory);
							//判断是否有高级查询条件
							if(!mWhereMap.isEmpty()){
								mCommodList.clear();
								Iterator it = mWhereMap.entrySet().iterator();
								while(it.hasNext()){
									Entry e = (Entry)it.next();
									String name = (String) e.getKey();
									String value = (String) e.getValue();
									req.put(name, value);
								} 
							}
							SplistResponse userRsp = req.doGet(SplistResponse.class.getName()); 
							commodList = userRsp.getCommodityList(); 
						}
						
						//加载活动商品
						if("yes".equals(mIsactivity)){
							CxSpListRequest cxreq = new CxSpListRequest();
							cxreq.put("item", item);
							SplistResponse userRsp = cxreq.doGet(SplistResponse.class.getName()); 
							commodList = userRsp.getCommodityList(); 
						}
						
						//获取信息
						for(int i = 0; i < commodList.size(); i++){
							CommodityListBean com = commodList.get(i);
							if(i == mCommodList.size()){
								mCommodList.add(com);
							}
							else{
								Bitmap bmp = mCommodList.get(i).getBitmap();
								com.setBitmap(bmp);
								mCommodList.set(i, com);    
							} 
						} 
						//启动线程更新
						mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_VIEW);
						 
						isAllRefresh = false; 
					} 
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					mCommodHandler.sendEmptyMessage(FragmentEx.HIDELOADINGPROGRESS);
				}
			}  
		});  
		
		//加载数据
		if(isAllRefresh){
			thread.start();  
		}
		else{
			//刷新部分数据
		}  
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		backgroundAlpha(1f);
		
		setSelectFlag();
	}
	
	/**
	 * 设置排序或精准查询选中状态
	 */
	private void setSelectFlag() {
		// TODO Auto-generated method stub
		for(int i = 0; i < radios.size(); i++){
			RadioButtonLayout b = (RadioButtonLayout) radios.get(i);
			if(b.getId() == R.id.button_sort){
				b.setSelect(isSelectStore); 
			}
			else if(b.getId() == R.id.button_filter){
				b.setSelect(isSelectFilter); 
			}  
		}
	}

	//清除排序条件
	public void clearStore(){
		for(int i = 0; i < radios.size(); i++){
			RadioButtonLayout b = (RadioButtonLayout) radios.get(i);
			if(b.getId() == R.id.button_sort){
				b.setSelect(false); 
			}
		}
		
		for(int i = 0; i < storeRadios.size(); i++){
			RadioButtonLayout b = (RadioButtonLayout) storeRadios.get(i);
			b.setSelect(false); 
		}
	}
	
	//清除精准条件
	public void clearFilter(){
		for(int i = 0; i < radios.size(); i++){
			RadioButtonLayout b = (RadioButtonLayout) radios.get(i);
			if(b.getId() == R.id.button_filter){
				b.setSelect(false); 
			}
		} 
		
		CommodityFilterFragment comFilter = (CommodityFilterFragment) mFragmentManagerEx.findFragment(CommodityFilterFragment.class.getName());
		if(comFilter != null){
			comFilter.setSelect(false);
		}
	}

	/**
	 * 处理精准查询条件
	 */
	@Override
	public boolean callBack(Object object) {
		// TODO Auto-generated method stub 
		//打开精准查询窗口
		Map<String, Object> mFilterMap = (Map<String, Object>) object;
		if(mFilterMap == null){
			setSelectFlag();
			return false;
		} 
		
		//判断是否选择了条件
		isSelectFilter = (Boolean) mFilterMap.get("isselect");
		Map<String, List<SxSetBean>> mapSxs = (Map<String, List<SxSetBean>>) mFilterMap.get("sex");
		if(mapSxs == null){
			setSelectFlag();
			return false;
		} 
		
		/*if(isSelectFilter){*/
			/*//排序条件删除
			mWhereMap.put("sortField", "");*/
			
			boolean isselect = false;
			//执行查询 
			Iterator it = mapSxs.entrySet().iterator();
			while(it.hasNext()){
				Entry e = (Entry)it.next();
				String name = (String) e.getKey();
				List<SxSetBean> sxs = (List<SxSetBean>) e.getValue();
				
				String value = "";
				for(int i = 0; i < sxs.size(); i++){
					SxSetBean sx = sxs.get(i);
					if(sx.isSelect()){
						isselect = true;
						
						if("".equals(value)){
							value = sx.getId();
						}
						else{
							value += "," +sx.getId();
						}
					}
				}
				
				mWhereMap.put(name, value);
				/*if(!"".equals(value)){
					mWhereMap.put(name, value);
				}*/
			}
			
			isAllRefresh = true;
			loadData();
			
			//全部没选
			if(!isselect){
				isSelectFilter = false;
				clearFilter();
			}
			/*//取消排序条件选中状态
			isSelectStore = false;
			clearStore();*/
		/*}
		else{
			setSelectFlag();
		}*/
		
		return false;
	}
}
