package com.shoppingstore.app.formcommon.utils;
 
import com.shoppingstore.app.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * 模拟单选框
 * @author meicunzhi
 * @date 2015-11-6 下午3:19:25
 */
public class RadioButtonLayout extends RelativeLayout implements OnClickListener {

	private static final int[] SELECT_STATE = { R.attr.selectstate };
	private boolean mSelectState = false;
	
	public RadioButtonLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}
 

	public boolean getSelect(){
		return this.mSelectState;
	}
	
	public void setSelect(boolean isSelect) {
		super.setSelected(isSelect);
		
		if (this.mSelectState != isSelect)
		{
			mSelectState = isSelect;
			refreshDrawableState();
		}  
	} 
	
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		if (mSelectState) {
			final int[] drawableState = super
					.onCreateDrawableState(extraSpace + 1);
			mergeDrawableStates(drawableState, SELECT_STATE);
			return drawableState;
		}
		
		return super.onCreateDrawableState(extraSpace);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		 if(getSelect())
			 setSelect(false);
		 else
			 setSelect(true);
	}

}
