package com.shoppingstore.app.formcommon.Adapter;

import java.util.List;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.CommodityListBean;
import com.shoppingstore.app.common.bean.ShopCartItemBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.formcommon.main.ShoppingCartFragment;
import com.shoppingstore.app.formcommon.utils.BuyQuantityEvent;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.internal.request.ShoppingCartRequest;
import com.shoppingstore.app.internal.response.ShoppingCartResponse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 显示购物车商品信息
 * @author meicunzhi
 * @date 2015-10-30 上午6:46:05
 */

public class ShopCartItemAdapter extends BaseAdapter{

	private Context mContext;  
	private List<ShopCartItemBean> shopCartList; 
	private ShoppingCartFragment shopCartFragment;
	private int mPosition; 
	private boolean mIsshowbutton = true;
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){ 
			case FragmentEx.UPDATE_DELETE:{
				shopCartList.remove(mPosition);  
				notifyDataSetChanged();
				
				//统计总金额
				shopCartFragment.setTotalAmount(getAmount());
				break;
			}
			case FragmentEx.UPDATE_AMOUNT:{
				//shopCartFragment.setTotalAmount(String.valueOf(mAmount));
				shopCartFragment.setTotalAmount(getAmount());
				break;
			}
			}
		}
	};
		
	public ShopCartItemAdapter(ShoppingCartFragment shopCartFragment, Context context, List<ShopCartItemBean> commodList){
		this.mContext = context;
		this.shopCartList = commodList;
		this.shopCartFragment = shopCartFragment;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shopCartList.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return shopCartList.get(position);  
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
		View view = inflater.inflate(R.layout.shopcartitem_layout, null, false);  
		
		View buy_quantity_fragment = view.findViewById(R.id.buy_quantity_fragment);
		View imageView_shopcart_delete = view.findViewById(R.id.imageView_shopcart_delete);
		TextView textView_quantity = (TextView) view.findViewById(R.id.textView_quantity);
		TextView textView_tip = (TextView) view.findViewById(R.id.textView_tip);
		
		if(mIsshowbutton){
			buy_quantity_fragment.setVisibility(View.VISIBLE); 
			imageView_shopcart_delete.setVisibility(View.VISIBLE); 
			textView_quantity.setVisibility(View.GONE); 
		}
		else{
			buy_quantity_fragment.setVisibility(View.GONE); 
			imageView_shopcart_delete.setVisibility(View.GONE); 
			textView_quantity.setVisibility(View.VISIBLE); 
		}
		//设置购物车加减、删除事件
		if(shopCartFragment != null){
			final BuyQuantityEvent byEvent = new BuyQuantityEvent(view);
			byEvent.initEvent(shopCartFragment, ShopCartItemAdapter.this, shopCartList.get(position)); 
			Button butAdd = (Button) view.findViewById(R.id.button_add);
			Button butSub = (Button) view.findViewById(R.id.button_sub); 
			/*butAdd.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub 
					ShopCartItemBean com = shopCartList.get(position);
					//修改了数量统计总金额
					int quantity = byEvent.getQuantity(); 
					shopCartList.set(position, com);
					//统计总金额
					shopCartFragment.setTotalAmount(getAmount());
				}
				
			});  
			*/
		}
		
		ImageView imageView_imageUrl = (ImageView) view.findViewById(R.id.imageView_imageUrl);
		TextView textView_commodityname = (TextView) view.findViewById(R.id.textView_commodityname);
		TextView textView_size = (TextView) view.findViewById(R.id.textView_size);
		TextView textView_price = (TextView) view.findViewById(R.id.textView_price);  
		EditText editText_addquantity = (EditText) view.findViewById(R.id.editText_addquantity);  
		//设置商品
		final ShopCartItemBean com = shopCartList.get(position);
		textView_commodityname.setText(com.getNameCN());
		double salePrice = Double.valueOf(com.getSalePrice());
		/*int quantity = Integer.valueOf(com.getQuantity());
		mAmount += salePrice * quantity; */
		
		textView_price.setText("￥" + salePrice);
		textView_size.setText(com.getSizeName());   
		editText_addquantity.setText(com.getQuantity());
		textView_quantity.setText(com.getQuantity());
		Bitmap bitmap = com.getBitmap();
		imageView_imageUrl.setImageBitmap(bitmap); 
		if(Integer.valueOf(com.getKcquantity()) <= 0){
			textView_tip.setVisibility(View.GONE);
		}
		else{
			textView_tip.setVisibility(View.VISIBLE);
		}
		ImageView butDelete = (ImageView) view.findViewById(R.id.imageView_shopcart_delete);
		//删除购物车，提交请求
		butDelete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub   
				new Thread(new Runnable(){
		            @Override   
					public void run(){
						String id = com.getId();
						String token = GlobalData.gUser.getUserToken();
						ShoppingCartRequest req = new ShoppingCartRequest();
						req.put("id", id);
						req.put("token", token);
						ShoppingCartResponse res;
						try {
							res = req.deleteShopCart(ShoppingCartResponse.class.getName());
							if("0".equals(res.getStatus()) && res.getResponseStatus() == 0){
								//判断是否删除成功，成功了重新更新购物车数量
								GlobalData.gShopCartQty -= 1; 
								mPosition = position;
								mCommodHandler.sendEmptyMessage(FragmentEx.UPDATE_DELETE); 
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							if(e instanceof BusException){
				        		BusException busEx = (BusException) e;
				        		busEx.showException();
				        	} 
							else{ 
								e.printStackTrace();
							}
						} 
					}
				}).start(); 
			}
					
		});
		 
		return view; 
	}
	public List<ShopCartItemBean> getShopCartList() {
		return shopCartList;
	}
	public void setShopCartList(List<ShopCartItemBean> shopCartList) {
		this.shopCartList = shopCartList;
	}
	
	public String getAmount() {
		double amount = 0;
		for(int i = 0; i < shopCartList.size(); i++){
			ShopCartItemBean com = shopCartList.get(i);
			double salePrice = Double.valueOf(com.getSalePrice());
			int quantity = Integer.valueOf(com.getQuantity());
			amount += salePrice * quantity; 
		}
		return FragmentEx.parseMoney(amount);
	}
	
	public void setShowbutton(boolean isShowButton) {
		this.mIsshowbutton = isShowButton;
	}	
}
