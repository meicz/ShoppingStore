package com.shoppingstore.app.formcommon.Adapter;

import java.util.List;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.CommodityListBean;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.internal.WebUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 显示商品列表信息
 * @author meicunzhi
 * @date 2015-10-23 23:40:29
 */

public class CommodityAdapter extends BaseAdapter{

	private Context mContext;  
	private List<CommodityListBean> mCommodList;
	
	private Handler mCommodHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) { 
        	switch (msg.what) {
        	case FragmentEx.UPDATE_VIEW: { 
        		int position = msg.arg1;
        		ImageView imageView_imageUrl = (ImageView) msg.obj;
        		imageView_imageUrl.setImageBitmap(mCommodList.get(position).getBitmap());
        		Log.d("shopex", "显示图片：" + String.valueOf(position));
        		break;
        	} 
        	default:
        		break;
        	}
        }
	};
	
	public CommodityAdapter(Context context, List<CommodityListBean> commodList){
		this.mContext = context;
		this.mCommodList = commodList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCommodList.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mCommodList.get(position);  
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
		View view = inflater.inflate(R.layout.commodity_layout, null, false);  
		
		TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
		TextView textView_price = (TextView) view.findViewById(R.id.textView_price);
		TextView textView_tagPrice = (TextView) view.findViewById(R.id.textView_tagPrice);  
		final ImageView imageView_imageUrl = (ImageView) view.findViewById(R.id.imageView_imageUrl);  
		ImageView imageView_tj = (ImageView) view.findViewById(R.id.imageView_tj);  
		ImageView imageView_ysq = (ImageView) view.findViewById(R.id.imageView_ysq);  
		
		//设置商品
		if(!mCommodList.isEmpty()) {
			final CommodityListBean com = mCommodList.get(position);
			textView_name.setText(com.getName());
			textView_price.setText(FragmentEx.parseMoney(com.getPrice()));
			textView_tagPrice.setText(FragmentEx.parseMoney(com.getTagPrice())); 
			textView_tagPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
			
			int quantity = "".equals(com.getQuantity()) ? 0 : Integer.valueOf(com.getQuantity());
			if(quantity <= 0){
				imageView_tj.setVisibility(View.GONE);
				imageView_ysq.setVisibility(View.VISIBLE);
			}
			else{
				String recommend = com.getRecommend();
				if("1".equals(recommend)){
					imageView_tj.setVisibility(View.VISIBLE);
					imageView_ysq.setVisibility(View.GONE);
				}
				else{
					imageView_tj.setVisibility(View.GONE);
					imageView_ysq.setVisibility(View.GONE);
				}
			}
			final String imgurl = com.getImageUrl(); 
			Bitmap bmp = com.getBitmap();
			if(bmp == null){
				Log.d("shopex", com.getItem() + "-" + com.getName() + "网络加载图片");
				Thread thread = new Thread(new Runnable() {  
					@Override  
					public void run() { 
						try { 
							Log.d("shopex", com.getItem() + "-" + com.getName() + "网络加载图片线程开始：" + String.valueOf(position));
							Bitmap bmp = WebUtils.doGetBitmap(imgurl);  
							com.setBitmap(bmp);
							Message msg = new Message();
							msg.arg1 = position;
							msg.obj = imageView_imageUrl;
							msg.what = FragmentEx.UPDATE_VIEW; 
							mCommodHandler.sendMessage(msg);
							Log.d("shopex", com.getItem() + "-" + com.getName() + "网络加载图片线程结束：" + String.valueOf(position));
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				});
				thread.start();
			}
			else{
				Log.d("shopex", com.getItem() + "-" + com.getName() + "从对象中获取"); 
				imageView_imageUrl.setImageBitmap(bmp); 
			}
		}
		
		return view; 
	}

}
