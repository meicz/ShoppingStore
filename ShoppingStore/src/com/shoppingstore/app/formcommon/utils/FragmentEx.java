package com.shoppingstore.app.formcommon.utils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.global.GlobalData; 
import com.shoppingstore.app.formcommon.utils.BackFragmentInterface;

/**
 * 提供页头按钮设置的方法,模拟Fragment进出堆栈
 * 想要实现这些效果的可以继承该类
 * @author meicunzhi
 * @date 2015-10-05 16:10:20
 */
public abstract class FragmentEx  extends Fragment implements Serializable { 
	protected FragmentManagerEx mFragmentManagerEx; 
	protected RelativeLayout mLeftLayout;	//头部导航左布局
	protected RelativeLayout mRightLayout;	//头部导航右布局
	protected RelativeLayout mCenterLayout;	//头部导航中间布局
	protected ShopCartImageView mShopCart = null;	//购物车
	protected static LoadingProgressDialog mLoadingProgress = null; 
	
	protected String mErrorMsg = "";		//错误信息
	
	protected BackFragmentInterface mBackHandledInterface;
	protected String mFragmentName = ""; 
	protected boolean isPush = false; 
	
	public final static int UPDATE_VIEW = 0;	//更新控件
	public final static int UPDATE_COMMODITY_IMAGE = 1;	//显示图片
	public final static int UPDATE_COMMODITY_SIZE = 2;	//更新尺码
	public final static int UPDATE_SHOPCARTQUANTITY = 3; //更新购物车数量
	public final static int OPEN_WINDOW = 4; //打开窗口
	public final static int CLOSE_WINDOW = 5; //关闭窗口
	public final static int MESSAGEHELP = 6; //消息提示
	public final static int UPDATE_AMOUNT =7;	//更新金额
	public final static int UPDATE_DELETE =8;	//删除控件
	public final static int UPDATE = 9;	//更新
	public final static int GOPAY = 10;	//支付
	public final static int CLEAR_SHOPCART = 11;	//清空购物车
	public final static int SHOWLOADINGPROGRESS = 12;	//显示加载
	public final static int HIDELOADINGPROGRESS = 13;	//隐藏加载
	public final static int HIDE = 14;	//隐藏
	public final static int SHOW = 15;	//显示
	
	/**
	 * isAllRefresh = true，全部重新加载数据，加载成功设置为false
	 * isAllRefresh = false，表示加载数据全部成功，可以控制部分数据的刷新
	 */
	protected boolean isAllRefresh = true;
	/**
	 * 回调参数名称，实现基类中自动检测加载回调函数
	 */
	public final static String CALLBACKFRAGMENTNAME = "callBackFragmentName";
	
	/**
	 * 要回调的Fragment,由基类根据CALLBACKFRAGMENTNAME在创建Fragment或显示时自动获取 
	 */
	protected CallBackInterface mCallBackFragment;	
	
	/**
	 * 数据加载  
	 */
	public abstract void loadData();
	
	/**
	 * 后退
	 * 由回退事件触发
	 * @return
	 */
	public abstract boolean onBackPressed();
	public abstract void addFragment(Boolean isAddStack, int fragmentLayoutId, Fragment fragment, String nowFragmentName);
	public abstract void popBackStack();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!(getActivity() instanceof BackFragmentInterface)) {
			throw new ClassCastException(
					"Hosting Activity must implement BackHandledInterface");
		} else {
			this.mBackHandledInterface = (BackFragmentInterface) getActivity();
		}
		
		if(mLoadingProgress == null)
			mLoadingProgress = new LoadingProgressDialog(getActivity());
	}

	/**
	 * 触发BackFragmentActivity的setNowtFragment,添加当前到栈顶
	 */
	@Override
	public void onStart() {
		super.onStart(); 
		mBackHandledInterface.setNowtFragment(this);
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments(); 
		if(bundle != null){
			//设置回调的fragment
			String fragmentName = bundle.getString(CALLBACKFRAGMENTNAME);
			if(fragmentName != null && !"".equals(fragmentName)){
				GlobalData app = (GlobalData) getActivity().getApplication();
				FragmentManagerEx fm = app.getFragment();
				mCallBackFragment = (CallBackInterface) fm.findFragment(fragmentName);
			}
		} 
				
		GlobalData app = (GlobalData) this.getActivity().getApplication();
		mFragmentManagerEx = app.getFragment(); 
	}
	
	/**
	 * 添加头部左边按钮
	 * @param layoutHeaderid 头部布局ID
	 * @param addLeftLayoutid 要添加到头部布局中的左边布局ID
	 * @return
	 */
	public View setLeftLayout(int layoutHeaderid, int addLeftLayoutid){ 
		return setLeftLayout(layoutHeaderid, R.id.imageView_ComLeft, addLeftLayoutid, null);
	} 
	
	/**
	 * 添加头部左边按钮
	 * @param layoutHeaderid 头部布局ID
	 * @param addLeftLayoutid 要添加到头部布局中的左边布局ID
	 * @param callback 要回调的方法
	 * @return
	 */
	public View setLeftLayout(int layoutHeaderid, int addLeftLayoutid, Method callback){ 
		/*mLeftLayout = getAddHeaderLayout(layoutHeaderid, R.id.textView_ComTitle, addLeftLayoutid, callback);
		return mLeftLayout.getFocusedChild();*/
		return setLeftLayout(layoutHeaderid, R.id.imageView_ComLeft, addLeftLayoutid, callback);
	} 
	
	/**
	 * 添加头部左边按钮
	 * @param layoutHeaderid 头部布局ID
	 * @param layoutHeaderControlid 头部布局中左边布局的ID
	 * @param addLeftLayoutid 要添加到头部布局中的左边布局ID
	 * @param callback 要回调的方法
	 * @return
	 */
	private View setLeftLayout(int layoutHeaderid, int layoutHeaderControlid, int addLeftLayoutid, final Method callback){
		View head = getView().findViewById(layoutHeaderid); 
		mLeftLayout = (RelativeLayout) head.findViewById(layoutHeaderControlid);
		if(mLeftLayout == null) return null; 
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		View view = inflater.inflate(addLeftLayoutid, null, false);  
		mLeftLayout.addView(view);
		if(callback != null){
			view.setOnClickListener(new View.OnClickListener(){
				 public void onClick(View v) {
					 int i=0;
					 i=i;
					 try {
						callback.invoke(this, null);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			});
		}
		
		return view;
	} 
	
	/**
	 * 添加头部右边按钮
	 * @param layoutHeaderid 头部布局ID
	 * @param addRightLayoutid 要添加到头部布局中的左边布局ID
	 * @return
	 */
	public View setRightLayout(int layoutHeaderid, int addRightLayoutid){ 
		return setRightLayout(layoutHeaderid, R.id.imageView_ComRight, addRightLayoutid, null);
	} 
	
	/**
	 * 添加头部右边按钮
	 * @param layoutHeaderid 头部布局ID
	 * @param addRightLayoutid 要添加到头部布局中的左边布局ID
	 * @param callback 要回调的方法
	 * @return
	 */
	public View setRightLayout(int layoutHeaderid, int addRightLayoutid, Method callback){ 
		/*mRightLayout = getAddHeaderLayout(layoutHeaderid, R.id.textView_ComTitle, addRightLayoutid, callback);
		return mRightLayout.getFocusedChild();*/
		return setLeftLayout(layoutHeaderid, R.id.imageView_ComRight, addRightLayoutid, callback);
	} 
	
	/**
	 * 添加头部右边按钮
	 * @param layoutHeaderid 头部布局ID
	 * @param layoutHeaderControlid 头部布局中左边布局的ID
	 * @param addRightLayoutid 要添加到头部布局中的左边布局ID
	 * @param callback 要回调的方法
	 * @return
	 */
	private View setRightLayout(int layoutHeaderid, int layoutHeaderControlid, int addRightLayoutid, final Method callback){
		View head = getView().findViewById(layoutHeaderid); 
		mRightLayout = (RelativeLayout) head.findViewById(layoutHeaderControlid);
		if(mRightLayout == null) return null; 
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		View view = inflater.inflate(addRightLayoutid, null, false);  
		mRightLayout.addView(view);
		if(callback != null){
			view.setOnClickListener(new View.OnClickListener(){
				 public void onClick(View v) {
					 int i=0;
					 i=i;
					 try {
						callback.invoke(this, null);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			});
		}
		
		return view;
	} 
	 
	/**
	 * 添加头部中间按钮
	 * @param layoutHeaderid 头部布局ID
	 * @param addCenterLayoutid 要添加到头部布局中的左边布局ID
	 * @return
	 */
	public View setCenterLayout(int layoutHeaderid, int addCenterLayoutid){ 
		return setCenterLayout(layoutHeaderid, R.id.textView_ComTitle, addCenterLayoutid, null);
	} 
	
	/**
	 * 添加头部中间按钮
	 * @param layoutHeaderid 头部布局ID
	 * @param addCenterLayoutid 要添加到头部布局中的左边布局ID
	 * @param callback 要回调的方法
	 * @return
	 */
	public View setCenterLayout(int layoutHeaderid, int addCenterLayoutid, Method callback){ 
		//mCenterLayout = getAddHeaderLayout(layoutHeaderid, R.id.textView_ComTitle, addCenterLayoutid, callback);
		//return mCenterLayout.getFocusedChild();
		return setCenterLayout(layoutHeaderid, R.id.textView_ComTitle, addCenterLayoutid, callback);
	}  
	
	/**
	 * 添加头部中间按钮
	 * @param layoutHeaderid 头部布局ID
	 * @param layoutHeaderControlid 头部布局中左边布局的ID
	 * @param addCenterLayoutid 要添加到头部布局中的左边布局ID
	 * @param callback 要回调的方法
	 * @return
	 */
	private View setCenterLayout(int layoutHeaderid, int layoutHeaderControlid, int addCenterLayoutid, final Method callback){
		View head = getView().findViewById(layoutHeaderid); 
		mCenterLayout = (RelativeLayout) head.findViewById(layoutHeaderControlid);
		if(mCenterLayout == null) return null; 
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		View view = inflater.inflate(addCenterLayoutid, null, false);  
		mCenterLayout.addView(view); 
		
		if(callback != null){
			view.setOnClickListener(new View.OnClickListener(){
				 public void onClick(View v) {
					 int i=0;
					 i=i;
					 try {
						callback.invoke(this, null);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			});
		}
		
		return view;
	} 
	 
	/**
	 * 添加头部中间按钮
	 * @param layoutHeaderid 头部布局ID
	 * @param layoutHeaderControlid 头部布局中左边布局的ID
	 * @param addCenterLayoutid 要添加到头部布局中的左边布局ID
	 * @param callback 要回调的方法
	 * @return
	 */
	private RelativeLayout getAddHeaderLayout(int layoutHeaderid, int layoutHeaderControlid, int addLayoutid, final Method callback){
		View head = getView().findViewById(layoutHeaderid); 
		RelativeLayout layout =  (RelativeLayout) head.findViewById(layoutHeaderControlid);
		if(layout == null) return null; 
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		View view = inflater.inflate(addLayoutid, null, false);  
		layout.addView(view);
		if(callback != null){
			view.setOnClickListener(new View.OnClickListener(){
				 public void onClick(View v) {
					 int i=0;
					 i=i;
					 try {
						callback.invoke(this, null);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			});
		}
		
		return layout;
	} 
	
	/**
	 * 初始化弹出菜单
	 * @param menuid
	 * @param width
	 * @param height
	 * @param focusable
	 * @return
	 */
	protected PopupWindow initPopUpMenu(int menuid, int width, int height, boolean focusable){
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//获取菜单布局文件，生成视图 
		View popupView = inflater.inflate(menuid, null, false);  
        //创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度  
		final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);  
        //设置动画效果  
        //popupWindow.setAnimationStyle(R.style.AnimationFade);  
        //点击其他地方消失  
		popupView.setOnTouchListener(new OnTouchListener() {   
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {  
					popupWindow.dismiss();  
                }  
                return false;   
			}  
        });  
		
		return popupWindow;
	}
	
	public String getFragmentName() {
		return mFragmentName;
	}
	public void setFragmentName(String FragmentName) {
		this.mFragmentName = FragmentName;
	}
	
	public boolean isPush() {
		return isPush;
	}
	public void setPush(boolean isPush) {
		this.isPush = isPush;
	} 
	
	/**
	 * 设置购物车数量
	 * @param quantity
	 */
	public void setShoCartQuantity(){
		if(mShopCart == null) return; 
		mShopCart.setmQuantity(GlobalData.gShopCartQty);
		
	}
	
	public static String parseMoney(double price){ 
		BigDecimal bd = new BigDecimal(price); 
		String format = ",###,##0.00";
		/*if(bd.doubleValue() > 1)
			format = ",###,###";*/
		return parseMoney(format, bd);
	}
	
	public static String parseMoney(String price){
		if(price == null || "".equals(price) || "null".equals(price))
			price = "0";
		BigDecimal bd = new BigDecimal(price); 
		String format = ",###,##0.00";
		/*if(bd.doubleValue() > 1)
			format = ",###,###";*/
		return "￥" + parseMoney(format, bd);
	}
	
	public static String parseMoney(String format, BigDecimal bd){ 
		DecimalFormat df = new DecimalFormat(format);
		return df.format(bd);
	}
	
	@Override  
	public void onHiddenChanged(boolean hidden){
		super.onHiddenChanged(hidden);  
		
		if(hidden){
			//隐藏界面 
		}
		else{
			//显示界面 
			Bundle bundle = getArguments(); 
			if(bundle != null){
				//设置回调的fragment
				String fragmentName = bundle.getString(CALLBACKFRAGMENTNAME);
				if(fragmentName != null && !"".equals(fragmentName)){
					GlobalData app = (GlobalData) getActivity().getApplication();
					FragmentManagerEx fm = app.getFragment();
					mCallBackFragment = (CallBackInterface) fm.findFragment(fragmentName);
				}
			}  
		}
	}
	
	public void deleteFragments(List<String> fragmentNames){
		mFragmentManagerEx.deleteFragments(fragmentNames);
	}
	
	public void outLoad(){
		mFragmentManagerEx.outLoad();
	}
}
