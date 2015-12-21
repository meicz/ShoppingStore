package com.shoppingstore.app.formcommon.utils;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 
import java.util.Map;

import com.shoppingstore.app.R;
import com.shoppingstore.app.internal.WebUtils;
 
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity; 
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;  
 
/**
 * 
 * @author meicunzhi
 * @date 2015-10-05 下午1:34:50
 * 图片轮播布局
 * 在XML文件中直接引用该布局，在实现JAVA文件中调用该对象，用addImage添加要轮播的图片
 * 如果想在该布局中增加按钮添加事件，可以把按钮布局（或其他布局）直接添加到该布局中
 * 动态添加的实现，是从原布局中删除该布局，然后再将该布局重新添加到动态生成的布局中（LinearLayout)
 */
public class ImageRoundView extends FrameLayout{  
	private RelativeLayout mRelativeLayout; //主布局
	private LinearLayout mDotLinearLayout; 	//圆点布局
	private ViewPager mViewPager;			//轮番视图框架
	
	private List<View> mPageView; 			//轮番视图，在轮播事件中调用
	private List<ImageView> mDotImageView; 	//轮番圆点，在轮播事件中调用
	private List<Object> mImageUrls; 		//轮番图片Url或本地资源ID
	private boolean isImageUrl = false;		//是否是网络图片
	
	private Bitmap bitmap;
	
	private Handler mCommodHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) { 
        	switch (msg.what) {
        	case FragmentEx.UPDATE_VIEW: { 
        		Map<String, Object> imgMap = (Map<String, Object>) msg.obj; 
        		ImageView imageView_imageUrl = (ImageView) imgMap.get("imageView");
        		Bitmap bmp = (Bitmap) imgMap.get("imageBitmap");
        		imageView_imageUrl.setImageBitmap(bmp); 
        	}
        	default:
        		break;
        	}
        }
	};
	
	public ImageRoundView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub 
	}
 
    public ImageRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub 
    }
    public ImageRoundView(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    	// TODO Auto-generated constructor stub
    	initData();
    	initUI(context);
    }
    
    //初始化
    private void initData(){
    	mImageUrls = new ArrayList<Object>();
    	mPageView = new ArrayList<View>(); 
    	mDotImageView = new ArrayList<ImageView>(); 
    }
    
    /**
     * 添加轮番图片，Url或资源ID
     * @param imageUrl
     */
    public void addImage(Object imageUrl){
    	if(mImageUrls == null){
    		initData();
    	}
    	
    	mImageUrls.add(imageUrl);
    }
    
    /**
     * 获取轮番图片数量
     * @return
     */
    public int getImageCount(){
    	return mImageUrls == null ? 0 : mImageUrls.size();
    }
    
    /**
     * 初始化布局
     * @param context
     */
    public void initUI(Context context){
    	this.setBackgroundResource(R.color.header_top_line_color);
    	if(mImageUrls == null || mImageUrls.size() == 0)
    		return;
    	
    	//初始化主布局，其它动态添加的布局添加到该布局中
    	mRelativeLayout = new RelativeLayout(context);
    	mRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
    	
    	//圆点布局
    	mDotLinearLayout = new LinearLayout(context);
    	RelativeLayout.LayoutParams rp = new  RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);  
    	mDotLinearLayout.setLayoutParams(rp); 
    	mDotLinearLayout.setPadding(50, 50, 50, 50); 
    	mDotLinearLayout.setGravity(Gravity.CENTER); 
    	 
    	// 添加显示的图片
        for (int i = 0; i < mImageUrls.size(); i++) { 
        	//添加图片视图
        	final ImageView view =  new ImageView(context); 
        	view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        	final String imageName = String.valueOf(mImageUrls.get(i));
        	if(imageName == null || "".equals(imageName)){
        		
        	}
        	
        	if(isImageUrl){
        		//Bitmap bit = (Bitmap) mImageUrls.get(i);
        		//view.setImageBitmap(bit);
        		view.setBackgroundResource(R.drawable.spbg);
        		Thread thread = new Thread(new Runnable() {  
					@Override  
					public void run() { 
						try {  
							Bitmap bmp = WebUtils.doGetBitmap(imageName);   
							if(bitmap == null) bitmap = bmp;
							
							Message msg = new Message(); 
							Map<String, Object> imgMap = new HashMap<String, Object>();
							imgMap.put("imageBitmap", bmp);
							imgMap.put("imageView", view);
							msg.obj = imgMap; 
							msg.what = FragmentEx.UPDATE_VIEW;  
							mCommodHandler.sendMessage(msg);
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				});
				thread.start();
        	}
        	else{
        		int srcid = (Integer) mImageUrls.get(i);
        		view.setBackgroundResource(srcid); 
        	}
        	
        	mPageView.add(view);
        	
        	//添加圆点视图
        	ImageView dotImgView  = new ImageView(context);   
        	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);     //圆点大小
        	dotImgView.setLayoutParams(lp);    
        	//圆点左右之间的间距
        	lp.leftMargin = 8;
        	lp.rightMargin = 8;
        	if(isImageUrl){ 
        		//第一个默认选中
            	if (i == 0) {  
            		dotImgView.setBackgroundResource(R.drawable.spdateil_dotfocused);  
                } else {  
                	dotImgView.setBackgroundResource(R.drawable.spdetail_dotunfocused);  
                } 
        	}
        	else{
        		//第一个默认选中
            	if (i == 0) {  
            		dotImgView.setBackgroundResource(R.drawable.dotfocused);  
                } else {  
                	dotImgView.setBackgroundResource(R.drawable.dotunfocused);  
                } 
        	}
        	
        	//添加到布和List中，List用于轮播笤俑
        	mDotLinearLayout.addView(dotImgView); 
        	mDotImageView.add(dotImgView);
		} 
    	
        mViewPager = new ViewPager(context);
    	mViewPager.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    	mViewPager.setFocusable(true); 
    	mViewPager.setAdapter(mPagerAdapter);
    	mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
    	
    	//获取本来存在布局中的其它控件（在XML文件中添加的）
    	int count = this.getChildCount();
    	List<View> vs = new ArrayList<View>();
    	for(int i = 0 ; i < count; i++){
    		View v = this.getChildAt(i);
    		vs.add(v); 
    		this.removeView(v);
    	}
    	
    	mRelativeLayout.addView(mViewPager); 
    	mRelativeLayout.addView(mDotLinearLayout);   
    	
    	LinearLayout buttonLine = new LinearLayout(context);
    	//存放获取本来存在的布局
    	RelativeLayout.LayoutParams rvlp = new  RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); 
    	rvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);   
    	rvlp.addRule(RelativeLayout.BELOW, mDotLinearLayout.getId());  
    	rvlp.bottomMargin = 120;	
    	buttonLine.setLayoutParams(rvlp);   
    	buttonLine.setGravity(Gravity.CENTER);  
    	for(int i = 0 ; i < vs.size(); i++){
    		View v = vs.get(i);
    		buttonLine.addView(v);
    	}
    	if(vs.size() > 0){
    		mRelativeLayout.addView(buttonLine); 
    	}
    	
    	addView(mRelativeLayout);   
    }
    
    //数据适配器
    PagerAdapter mPagerAdapter = new PagerAdapter(){

		@Override
		//获取当前窗体界面数
		public int getCount() {
			// TODO Auto-generated method stub
			return mPageView.size();
		}

		@Override
		//断是否由对象生成界面
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		//是从ViewGroup中移出当前View
		 public void destroyItem(View arg0, int arg1, Object arg2) {  
	            ((ViewPager) arg0).removeView(mPageView.get(arg1));  
	        }  
		
		//返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
		public Object instantiateItem(View arg0, int arg1){
			((ViewPager)arg0).addView(mPageView.get(arg1));
			return mPageView.get(arg1);				
		} 
    };
    
  //pageView监听器
    class GuidePageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		} 
		
		@Override
		//如果切换了，就把当前的点点设置为选中背景，其他设置未选中背景
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			for(int i = 0; i < mDotImageView.size(); i++){ 
				if(isImageUrl){  
	            	mDotImageView.get(i).setBackgroundResource(R.drawable.spdateil_dotfocused);
					if(i != arg0)
						mDotImageView.get(i).setBackgroundResource(R.drawable.spdetail_dotunfocused);  
	        	}
	        	else{ 
	        		mDotImageView.get(i).setBackgroundResource(R.drawable.dotfocused);
					if(i != arg0)
						mDotImageView.get(i).setBackgroundResource(R.drawable.dotunfocused);  
	        	} 
			}
			
		}
    	
    }

	public boolean isImageUrl() {
		return isImageUrl;
	}

	public void setBitmap(boolean isImageUrl) {
		this.isImageUrl = isImageUrl;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	} 
	
	
}
