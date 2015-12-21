package com.shoppingstore.app.formcommon.Adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.CommodityListBean;
import com.shoppingstore.app.common.bean.OrderDetailBean;
import com.shoppingstore.app.common.bean.OrderDetailBeans;
import com.shoppingstore.app.common.bean.ShopCartItemBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.formcommon.main.OrderDetailFragment;
import com.shoppingstore.app.formcommon.main.OrderDetailListFragment;
import com.shoppingstore.app.formcommon.main.ShoppingCartFragment;
import com.shoppingstore.app.formcommon.utils.BuyQuantityEvent;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.internal.WebUtils;
import com.shoppingstore.app.internal.request.ShoppingCartRequest;
import com.shoppingstore.app.internal.response.ShoppingCartResponse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 显示订单信息
 * @author meicunzhi
 * @date 2015-10-30 上午6:46:05
 */

public class OrderDetailAdapter extends BaseAdapter{ 
	
	private List<OrderDetailBean> orderDetails;
	private Context mContext;
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) { 
        	switch (msg.what) {
        	case FragmentEx.UPDATE_VIEW: { 
        		Map<String, Object> imgMap = (Map<String, Object>) msg.obj; 
        		ImageView imageView_imageUrl1 = (ImageView) imgMap.get("imageView1");
        		if(imageView_imageUrl1 != null){
        			Bitmap bmp = (Bitmap) imgMap.get("imageBitmap1");
        			imageView_imageUrl1.setImageBitmap(bmp); 
        		}
        		
        		ImageView imageView_imageUrl2 = (ImageView) imgMap.get("imageView2");
        		if(imageView_imageUrl2 != null){
        			Bitmap bmp = (Bitmap) imgMap.get("imageBitmap2");
        			imageView_imageUrl2.setImageBitmap(bmp); 
        		}
        	}
        	default:
        		break;
        	}
        }
	};
		
	public OrderDetailAdapter(Context context, List<OrderDetailBean> orderDetails){ 
		this.orderDetails = orderDetails;
		this.mContext = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orderDetails.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return orderDetails.get(position);  
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
		View view = inflater.inflate(R.layout.orderdetail_item_layout, null, false);  
		 
		TextView textView_orderitem = (TextView) view.findViewById(R.id.textView_orderitem); 
		TextView textView_date = (TextView) view.findViewById(R.id.textView_date); 
		TextView textView_amount = (TextView) view.findViewById(R.id.textView_amount); 
		TextView textView_status = (TextView) view.findViewById(R.id.textView_status); 
		TextView textView_quantity = (TextView) view.findViewById(R.id.textView_quantity);  
		final ImageView imageView_imageUrl1 = (ImageView) view.findViewById(R.id.imageView_imageUrl1); 
		final ImageView imageView_imageUrl2 = (ImageView) view.findViewById(R.id.imageView_imageUrl2);  
		
		final OrderDetailBean orderDetail = orderDetails.get(position); 
		textView_orderitem.setText(orderDetail.getItem());
		textView_date.setText(orderDetail.getDate());
		String status = orderDetail.getStatus();
		if(orderDetail.getItem().equals("1400022909")){
			orderDetail.getItem();
		}
		if("0".equals(status)){
			status = "已取消订单";
		} else if("2".equals(status)){
			status = "已提交,等待付款";
		} else if("3".equals(status)){
			status = "仓库正在拣货";
		} else {
			status = "";
		} 
		
		textView_quantity.setText(String.valueOf("共" + orderDetail.getOrderItems().size() + "件"));
		 
		List<OrderDetailBeans> orderDetas = orderDetail.getOrderItems();
		for(int i = 0; i < orderDetas.size(); i++){ 
			final OrderDetailBeans imgdeta = orderDetas.get(i);
			if(imgdeta.getThstatus().equals("1")){
				status = "待退款";
			}
			
			if(imgdeta.getChstatus().equals("1")){
				status = "已出货";
			}
			
			if(imgdeta.getSqth().equals("1")){
				status = "待退款";
			}
			else if(imgdeta.getSqth().equals("2")){
				status = "已退款";
			}
			Bitmap bm = imgdeta.getImgBitmap();
			if(bm != null){ 
				if(i == 0){
					imageView_imageUrl1.setImageBitmap(bm);
				}
				else if(i == 1){
					imageView_imageUrl2.setImageBitmap(bm);
				} else{
					break;
				}
			}
			else{
				final Message msg = new Message(); 
				final Map<String, Object> imgMap = new HashMap<String, Object>();
				final int index = i;
				Thread thread = new Thread(new Runnable() {  
					@Override  
					public void run() { 
						try {   
							Bitmap bmp = WebUtils.doGetBitmap(imgdeta.getImgUrl());   
							imgdeta.setImgBitmap(bmp); 
							
							if(index == 0){
								imgMap.put("imageBitmap1", bmp);
								imgMap.put("imageView1", imageView_imageUrl1); 
							} 
							
							if(index == 1){
								imgMap.put("imageBitmap2", bmp);
								imgMap.put("imageView2", imageView_imageUrl2); 
							} 
							
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
		}
		
		textView_status.setText(status);
		if(orderDetas.size() == 1){
			imageView_imageUrl2.setVisibility(View.GONE); 
		}
		
		textView_amount.setText(getAmount(orderDetas));  
		
		return view; 
	} 
	
	public String getAmount(List<OrderDetailBeans> orderDetas) {
		double amount = 0; 
		for(int i = 0; i < orderDetas.size(); i++){ 
			OrderDetailBeans imgdeta = orderDetas.get(i);
			amount+=Double.valueOf(imgdeta.getAmount());
		}
		
		return FragmentEx.parseMoney(String.valueOf(amount));
	} 
}
