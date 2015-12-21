package com.shoppingstore.app.formcommon.utils;
 
import java.util.ArrayList;
import java.util.List;

import com.shoppingstore.app.R;
 
import android.app.Activity;
import android.content.Context; 
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 模拟单选
 * @author meicunzhi
 * @date 2015-11-6 下午3:19:25
 */
public class QuanSelectButton extends RadioButtonLayout {

	private TextView textView_quan;
	
	public QuanSelectButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub 
	}
 
	public void setSelectBackground(Boolean isSelect) {
		// TODO Auto-generated method stub
		if(isSelect){
			textView_quan.setBackgroundResource(R.color.login_but_bgcolor);
		}
		else{
			textView_quan.setBackgroundResource(R.color.header_text_margin_line_color);
		}
	}
 
	public void setSelectTextColor(Boolean isSelect) {
		// TODO Auto-generated method stub
		if(isSelect){
			
		}
		else{
			
		}
	} 
	
	public void setSelect(boolean isSelect) {
		setSelectBackground(isSelect);
	} 
	
	public void initUI(Context context){
		int count = this.getChildCount();
    	List<View> vs = new ArrayList<View>();
    	for(int i = 0 ; i < count; i++){
    		textView_quan = (TextView) this.getChildAt(i); 
    	} 
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		boolean isClick = false;
		 if(getSelect())
			 isClick = false;
		 else
			 isClick = true;
		 
		 setSelect(isClick);
		 setSelectBackground(isClick);
		 setSelectTextColor(isClick);
	}
}
