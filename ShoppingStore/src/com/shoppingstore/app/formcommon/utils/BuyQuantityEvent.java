package com.shoppingstore.app.formcommon.utils;

import java.util.HashMap;
import java.util.Map;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.ShopCartItemBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.Adapter.ShopCartItemAdapter;
import com.shoppingstore.app.formcommon.main.ShoppingCartFragment;
import com.shoppingstore.app.internal.request.ShoppingCartRequest;
import com.shoppingstore.app.internal.response.ShoppingCartResponse;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 修改购物车数量事件
 * @author meicunzhi
 * @date 2015-10-31 上午7:36:39
 */
public class BuyQuantityEvent {

	private int mMax = 0;	//允许最大值，领无限
	private int mMin = 1;	//允许最小值
	private int mQuantity = 0; 	//数量 
	private EditText editQuantity;
	private View view;
	
	private LoadingProgressDialog mLoadingProgress;
	
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {  
			switch(msg.what){
			case FragmentEx.HIDELOADINGPROGRESS: {
        		mLoadingProgress.hide();
        		break;
        	}
			case FragmentEx.SHOWLOADINGPROGRESS: {
        		mLoadingProgress.show();
        		break;
        	}
			case FragmentEx.MESSAGEHELP:{ 
				Map<String, Object> map = (Map<String, Object>) msg.obj; 
				Context context = (Context) map.get("activity");
				String errortip = (String) map.get("errortip"); 
				Toast toast = Toast.makeText(context, errortip, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show(); 
				break;
			}
			}
		}
	};
	
	public BuyQuantityEvent(View view){
		this.view = view;
	}
	
	public void initEvent(){
		initEvent(null, null, null);
	}
	
	public void initEvent(final ShoppingCartFragment shopCartFragment, final ShopCartItemBean shopCartList){
		if(view == null) return; 

		mLoadingProgress = new LoadingProgressDialog(shopCartFragment.getActivity()); 
		
		Button butAdd = (Button) view.findViewById(R.id.button_add);
		Button butSub = (Button) view.findViewById(R.id.button_sub); 
		editQuantity = (EditText) view.findViewById(R.id.editText_addquantity);
		
		//增加
		butAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub 
				String quantity = editQuantity.getText().toString().trim();
				quantity = quantity == null || quantity.equals("") ? "0" : quantity;
				mQuantity = Integer.valueOf(quantity);
				if(mMax != 0 && mQuantity + 1 > mMax) {
					mQuantity = mMax;
					return;
				}
				else
					mQuantity+=1; 
				editQuantity.setText(String.valueOf(mQuantity));
				
				new Thread(new Runnable() {  
					@Override  
					public void run() {
						try {
							mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
							ShoppingCartRequest req = new ShoppingCartRequest(); 
							req.put("id", shopCartList.getId());
							req.put("quantity", String.valueOf(mQuantity));
							ShoppingCartResponse res = req.deleteShopCart(ShoppingCartResponse.class.getName());
							if(!res.isAllStatus()){ 
								Message msg = new Message();
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("activity", shopCartFragment.getActivity());
								map.put("errortip", res.getErrorMessage());
								msg.obj = map;
								msg.what = FragmentEx.MESSAGEHELP;
								mCommodHandler.sendMessage(msg);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							mCommodHandler.sendEmptyMessage(FragmentEx.HIDELOADINGPROGRESS);
						}
						
					}
				}).start(); 
				
				if(shopCartList != null){
					shopCartList.setQuantity(String.valueOf(mQuantity)); 
					shopCartFragment.setTotalAmount(shopCartFragment.getAmount());
				}
			}
			
		});
		
		//减少
		butSub.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub  
				String quantity = editQuantity.getText().toString().trim();
				quantity = quantity == null || quantity.equals("") ? "1" : quantity;
				if(mQuantity - 1 < mMin ){
					mQuantity = mMin;
					return;
				}
				mQuantity = mQuantity - 1 <= mMin ? 1 : mQuantity - 1; 
				editQuantity.setText(String.valueOf(mQuantity));
				
				new Thread(new Runnable() {  
					@Override  
					public void run() {
						try {
							mCommodHandler.sendEmptyMessage(FragmentEx.SHOWLOADINGPROGRESS);
							ShoppingCartRequest req = new ShoppingCartRequest(); 
							req.put("id", shopCartList.getId());
							req.put("quantity", String.valueOf(mQuantity));
							ShoppingCartResponse res = req.deleteShopCart(ShoppingCartResponse.class.getName());
							if(!res.isAllStatus()){ 
								Message msg = new Message();
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("activity", shopCartFragment.getActivity());
								map.put("errortip", res.getErrorMessage());
								msg.obj = map;
								msg.what = FragmentEx.MESSAGEHELP;
								mCommodHandler.sendMessage(msg);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							mCommodHandler.sendEmptyMessage(FragmentEx.HIDELOADINGPROGRESS);
						}
						
					}
				}).start(); 
				
				if(shopCartList != null){
					shopCartList.setQuantity(String.valueOf(mQuantity)); 
					shopCartFragment.setTotalAmount(shopCartFragment.getAmount());
				}
			}
			
		}); 
	}

	public void initEvent(final ShoppingCartFragment shopCartFragment, final ShopCartItemAdapter shopCartAdapter, final ShopCartItemBean shopCartList){
		if(view == null) return;
		
		Button butAdd = (Button) view.findViewById(R.id.button_add);
		Button butSub = (Button) view.findViewById(R.id.button_sub); 
		editQuantity = (EditText) view.findViewById(R.id.editText_addquantity);
		
		//增加
		butAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub 
				String quantity = editQuantity.getText().toString().trim();
				quantity = quantity == null || quantity.equals("") ? "0" : quantity;
				mQuantity = Integer.valueOf(quantity);
				if(mMax != 0 && mQuantity + 1 > mMax) {
					mQuantity = mMax;
				}
				else
					mQuantity+=1; 
				editQuantity.setText(String.valueOf(mQuantity));
				
				if(shopCartList != null){
					shopCartList.setQuantity(String.valueOf(mQuantity)); 
					shopCartFragment.setTotalAmount(shopCartAdapter.getAmount());
				}
			}
			
		});
		
		//减少
		butSub.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub  
				String quantity = editQuantity.getText().toString().trim();
				quantity = quantity == null || quantity.equals("") ? "1" : quantity;
				mQuantity = Integer.valueOf(quantity);
				mQuantity = mQuantity - 1 <= mMin ? 1 : mQuantity - 1; 
				editQuantity.setText(String.valueOf(mQuantity));
				
				if(shopCartList != null){
					shopCartList.setQuantity(String.valueOf(mQuantity)); 
					shopCartFragment.setTotalAmount(shopCartAdapter.getAmount());
				}
			}
			
		}); 
	}
	
	public int getMax() {
		return mMax;
	}

	public void setMax(int max) {
		this.mMax = max;
	}

	public int getMin() {
		return mMin;
	}

	public void setMin(int min) {
		this.mMin = min;
	}

	public int getQuantity() {
		if(editQuantity == null)
			mQuantity = 0;
		else{
			String quantity = editQuantity.getText().toString().trim();
			quantity = quantity == null || quantity.equals("") ? "0" : quantity;
			
			mQuantity = Integer.valueOf(quantity);
			if(mMax != 0 && mQuantity > mMax) {
				mQuantity = mMax;
				editQuantity.setText(String.valueOf(mQuantity));
			}
		}
		
		return mQuantity;
	}  
}
