package com.shoppingstore.app.formcommon.Adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shoppingstore.app.R; 
import com.shoppingstore.app.common.bean.BrandBean; 
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.main.FragmentCommodityLists;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.internal.WebUtils;

import android.content.Context; 
import android.graphics.Bitmap; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

/**
 * 促销活动适配器
 * @author meicunzhi
 * @date 2015-11-12 上午4:51:01
 */
public class BrandAdapter extends BaseAdapter{

	private Context mContext;  
	private List<BrandBean> mBrandList;
	
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
	
	public BrandAdapter(Context context, List<BrandBean> brandList){
		this.mContext = context;
		this.mBrandList = brandList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBrandList.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mBrandList.get(position);  
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//定义一个ImageView,显示在GridView里 
    	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		View view = inflater.inflate(R.layout.branditem_layout, null, false);  
		
		TextView textView_titleen = (TextView) view.findViewById(R.id.textView_titleen);
		TextView textView_titlecn = (TextView) view.findViewById(R.id.textView_titlecn);
		TextView textView_mem = (TextView) view.findViewById(R.id.textView_mem);  
		final ImageView imageView_imageUrl = (ImageView) view.findViewById(R.id.imageView_imageUrl);  
		LinearLayout layout_bar = (LinearLayout) view.findViewById(R.id.layout_bar);  
		//layout_bar.getBackground().setAlpha(99);
		//设置商品
		final BrandBean brand = mBrandList.get(position);
		String nameen = brand.getTitleEN();
		String namecn = brand.getTitleCN();
		if(nameen.equals("null")){
			nameen = "";
		}if(namecn.equals("null")){
			namecn = "";
		}
		textView_titleen.setText(nameen);
		textView_titlecn.setText(namecn);
		textView_mem.setText(brand.getMem());  
		
		Bitmap bmp = brand.getBitmap();
		if(bmp == null){
			Thread thread = new Thread(new Runnable() {  
				@Override  
				public void run() { 
					try {  
						String imageName = brand.getImgUrl();
						Bitmap bmp = WebUtils.doGetBitmap(imageName);   
						brand.setBitmap(bmp);
						
						Message msg = new Message(); 
						Map<String, Object> imgMap = new HashMap<String, Object>();
						imgMap.put("imageBitmap", bmp);
						imgMap.put("imageView", imageView_imageUrl);
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
			imageView_imageUrl.setImageBitmap(bmp);
		} 
		
		OnClickListener click = new OnClickListener(){ 
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*BrandBean cx = mBrandList.get(position);
				String item = cx.getItem();
				Bundle bundle = new Bundle();
				bundle.putString("isactivity", "yes");	//是否是活动，yes=是 no=否
				bundle.putString("item", item);
				
				GlobalData app = (GlobalData) ((FragmentActivity)mContext).getApplication();
				FragmentManagerEx fm = app.getFragment();
				FragmentCommodityLists comlist = new FragmentCommodityLists();
				comlist.setArguments(bundle); 
				fm.add(true, comlist, "cx"+item);*/
			}
			
		};
		
		imageView_imageUrl.setOnClickListener(click);
		layout_bar.setOnClickListener(click);
		
		return view; 
	}

	public void setBrandList(List<BrandBean> brandList){
		this.mBrandList = brandList;
	}
}
