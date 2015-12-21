package com.shoppingstore.app.formcommon.utils;
 
import com.shoppingstore.app.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 尺码选择按钮
 * @author meicunzhi
 * @date 2015-11-2 下午8:53:46
 */
public class SizeButton extends Button { 
	private boolean isSelect; 	//是否选择 
	private int quantity = 0;
	public SizeButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		isSelect = false; 
	}
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}  
}
