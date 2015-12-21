package com.shoppingstore.app.formcommon.main;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.global.GlobalData; 
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.internal.WebUtils;
import com.shoppingstore.app.internal.request.SplistRequest;
import com.shoppingstore.app.internal.response.SplistResponse; 

public class FragmentCommodityCollection extends FragmentEx implements OnClickListener{   
	private String mSex; 		//性别
	private String mCategory; 	//大类编码
	
	private PopupWindow mPopupWindow;  
	private View mPopupView;
	private ImageView imageView_Right;
	private TextView textView_Title;
	private Button mSexMenu;
	private final String MENSTR = "Men Collection 男士专区";
	private final String WOMENSTR = "Women Collection 女士专区";
	
	private final String MEMCATEGORY = "01";
	private final String WOMEMCATEGORY = "02";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.commoditycollection_layout, container, false);
		
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 
		 
		DisplayMetrics metric = new DisplayMetrics();
		this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;  // 屏幕宽度（像素）
		int height = metric.heightPixels;  // 屏幕高度（像素）
		float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
		int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
		
		//获取传递过来的参数
		Bundle bund = getArguments();
		mSex = bund.getString("sex");
		View vc = setCenterLayout(R.id.header_menubar, R.layout.header_textview_layout);
		textView_Title = (TextView) vc.findViewById(R.id.header_text);
		if("men".equals(mSex)){ 
			textView_Title.setText(MENSTR); 
			mCategory = MEMCATEGORY;
		}
		else{
			textView_Title.setText(WOMENSTR); 
			mCategory = WOMEMCATEGORY;
		}
		//根据sex读取数据
		getSexData(mSex);
				 
		//添加控件事件
		View imageView_Left = setLeftLayout(R.id.header_menubar, R.layout.header_back_layout, null);
		imageView_Left.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {     
				 popBackStack();
			 }
		});  
		
		//设置默认向下图标
		View vr = setRightLayout(R.id.header_menubar, R.layout.header_select_commod_type_layout);
		imageView_Right = (ImageView) vr.findViewById(R.id.header_commodityselect);
		String tag = (String) imageView_Right.getTag();
		if(tag == null || "".equals(tag)){
			imageView_Right.setTag("down");
			imageView_Right.setImageResource(R.drawable.commonarrowdown2x);
		}
		
		//添加事件
		imageView_Right.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {    
				 String tag = (String) imageView_Right.getTag(); 
				 if(tag != null && "down".equals(tag)){ 
					 showDownOrUpImage("up", R.drawable.commonarrowup2x);
					 showPopUpMenu(true);
				 }
				 else{
					 showDownOrUpImage("down", R.drawable.commonarrowdown2x); 
					 showPopUpMenu(false);
				 } 
			 }
		});  
		
		ImageView xz = (ImageView)getView().findViewById(R.id.imageView_com_type_xz);  
		xz.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 String subcategory = "01";
				 String fragmentName = mSex + subcategory; 
				 
				 Bundle bu = new Bundle(); 
				 bu.putString("category", mCategory);
				 bu.putString("subcategory", subcategory);  
				 bu.putString("callFragmentName", fragmentName);
				 
				 FragmentCommodityLists men = (FragmentCommodityLists) mFragmentManagerEx.findFragment(fragmentName);
				 if(men == null){
					 men = new FragmentCommodityLists(); 
					 men.setArguments(bu);    
				 } 
				 mFragmentManagerEx.add(true, false, men, fragmentName); 
			 }
		});
		
		ImageView ps = (ImageView)getView().findViewById(R.id.imageView_com_type_ps);  
		ps.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 String subcategory = "02";
				 String fragmentName = mSex + subcategory;
				 Bundle bu = new Bundle(); 
				 bu.putString("category", mCategory);
				 bu.putString("subcategory", subcategory);
				 bu.putString("callFragmentName", fragmentName);
				 FragmentCommodityLists men = new FragmentCommodityLists(); 
				 men.setArguments(bu);    
				 mFragmentManagerEx.add(true, false, men, fragmentName);
			 }
		});
		
		ImageView py = (ImageView)getView().findViewById(R.id.imageView_com_type_py);  
		py.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 String subcategory = "03";
				 String fragmentName = mSex + subcategory;
				 Bundle bu = new Bundle(); 
				 bu.putString("category", mCategory);
				 bu.putString("subcategory", subcategory);
				 bu.putString("callFragmentName", fragmentName);
				 FragmentCommodityLists men = new FragmentCommodityLists(); 
				 men.setArguments(bu); 
				 mFragmentManagerEx.add(true, false, men, fragmentName);
			 }
		});
		
		ImageView bb = (ImageView)getView().findViewById(R.id.imageView_com_type_bb);  
		bb.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {
				 String subcategory = "04";
				 String fragmentName = mSex + subcategory;
				 Bundle bu = new Bundle(); 
				 bu.putString("category", mCategory);
				 bu.putString("subcategory", subcategory);
				 bu.putString("callFragmentName", fragmentName);
				 FragmentCommodityLists men = new FragmentCommodityLists(); 
				 men.setArguments(bu); 
				 mFragmentManagerEx.add(true, false, men, fragmentName);
			 }
		});
	/*ImageView butMen = (ImageView) getActivity().findViewById(R.id.testff);
	butMen.setOnClickListener(new View.OnClickListener(){
		 public void onClick(View v) {      
		    	FragmentManagerEx fm = mFragmentManagerEx; 
		    	CommodityListFragment men = new CommodityListFragment(); 
		    	fm.add(true, men, "commodityType");
		 }
	}); 
	
		 /*GridView gridView=(GridView)getActivity().findViewById(R.id.gridVie1w1);  
	        gridView.setAdapter(new ImageAdapter(getActivity()));  */
		/*ImageView butMen = (ImageView) getActivity().findViewById(R.id.testff);
		butMen.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {      
			    	FragmentManagerEx fm = mFragmentManagerEx; 
			    	CommodityListFragment men = new CommodityListFragment(); 
			    	fm.add(true, men, "commodityType");
			 }
		}); 
		final ImageView butMen1 = (ImageView) getActivity().findViewById(R.id.imageView1ffff);*/
		/*Thread thread=new Thread(new Runnable()  
        {  
            @Override  
            public void run()  
            {   
            	SplistRequest req = new SplistRequest();
        		req.put("category", "01");
        		req.put("subcategory", "01");
        		SplistResponse userRsp = req.doGet(SplistResponse.class.getName());
        		 List<JSONObject> ll = userRsp.getDatas();
        		
        		 try {
     				for(int i = 0; i < ll.size(); i++){
     		        	JSONObject obj1 =ll.get(i);  
     		        	String imageUrl=obj1.getString("imageUrl");
     		        	Bitmap bmp = WebUtils.doGetBitmap(imageUrl);
     		        	
     		        	//butMen1.setImageBitmap(bmp); 
     		        	String item=obj1.getString("item");
     		        	item=item;
     		        }
     			} catch (JSONException e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}  
            }  
        });  
        thread.start();  */
    }   
	
	@Override  
	public void onHiddenChanged(boolean hidden){
		super.onHiddenChanged(hidden);  
		if(hidden){
			//隐藏界面 
		}
		else{
			//显示界面
			
			//恢复默认
			/*Bundle bund = getArguments();
			String oldsex = bund.getString("sex");
			if(!oldsex.equals(mSex)){
				mSex = oldsex; 
				if(textView_Title != null){
					if("men".equals(mSex)){ 
						textView_Title.setText(MENSTR); 
						mCategory = MEMCATEGORY;
					}
					else{
						textView_Title.setText(WOMENSTR); 
						mCategory = WOMEMCATEGORY;
					}
					//根据sex读取数据
					getSexData(mCategory);
				} 
			} */
		}
	}
      
	/**
	 * 根据类别获取数据
	 * @param category
	 */
	public void getSexData(String category){
		
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
			/*View menubar = getView().findViewById(R.id.header_findbar);
			int ypos = menubar.getTop();
			ypos += mPopupWindow.getHeight() / 2 + 1; */
			
			Rect viewRect = new Rect();  
			getView().findViewById(R.id.header_findbar).getGlobalVisibleRect(viewRect);   
			mPopupWindow.showAtLocation(mPopupView, Gravity.CENTER | Gravity.TOP, viewRect.left, viewRect.top); 
			
			//mPopupWindow.showAtLocation(mPopupView, Gravity.CENTER | Gravity.TOP, 0, ypos);  
			if("men".equals(mSex)){ 
				mSexMenu.setText(WOMENSTR);
				mCategory = WOMEMCATEGORY;
			}
			else{ 
				mSexMenu.setText(MENSTR);
				mCategory = MEMCATEGORY;
			}
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
		mPopupWindow = new PopupWindow(mPopupView, LayoutParams.MATCH_PARENT, 60, true);  
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
		
		//添加菜单点击事件
		mSexMenu = (Button) mPopupView.findViewById(R.id.button_sexmenu);
		mSexMenu.setOnClickListener(this);
	}

	/**
	 * 按钮或菜单点击事件
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.button_sexmenu: 
			showDownOrUpImage("down", R.drawable.commonarrowdown2x); 
			mPopupWindow.dismiss();  
			if("men".equals(mSex)){
				textView_Title.setText(WOMENSTR); 
				mSex = "women";
			}
			else{
				textView_Title.setText(MENSTR); 
				mSex = "men";
			}
			getSexData(mSex);
			break;
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
		
	}  
}
