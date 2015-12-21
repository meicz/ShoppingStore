package com.shoppingstore.app.formcommon.Adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.FavoriteBean;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.internal.WebUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 显示商品列表信息
 * @author meicunzhi
 * @date 2015-10-23 23:40:29
 */

public class FavoriteAdapter extends BaseAdapter{

	private Context mContext;  
	private List<FavoriteBean> mFavoriteList;
	
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
	
	public FavoriteAdapter(Context context, List<FavoriteBean> favoriteList){
		this.mContext = context;
		this.mFavoriteList = favoriteList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFavoriteList.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mFavoriteList.get(position);  
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//定义一个ImageView,显示在GridView里 
    	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		View view = inflater.inflate(R.layout.favorite_layout, null, false);  
		
		TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
		TextView textView_price = (TextView) view.findViewById(R.id.textView_price);
		TextView textView_tagPrice = (TextView) view.findViewById(R.id.textView_tagPrice);  
		final ImageView imageView_imageUrl = (ImageView) view.findViewById(R.id.imageView_imageUrl);  
		
		//设置商品
		final FavoriteBean com = mFavoriteList.get(position);
		textView_name.setText(com.getName());
		textView_price.setText(FragmentEx.parseMoney(com.getPrice()));
		textView_tagPrice.setText(FragmentEx.parseMoney(com.getTagPrice())); 
		textView_tagPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
		
		Bitmap bitmap = com.getBitmap();
		if(bitmap == null){
			Thread thread = new Thread(new Runnable() {  
				@Override  
				public void run() { 
					try {  
						String imageName = com.getImageUrl();
						Bitmap bmp = WebUtils.doGetBitmap(imageName);   
						com.setBitmap(bmp);
						
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
		else
			imageView_imageUrl.setImageBitmap(bitmap); 
		return view; 
	}

}
