package com.shoppingstore.app;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView; 

 
public class TitleView extends View implements View.OnClickListener {
 
	public TitleView(Context context) {
		this(context, null);
	}

	public TitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle); 
 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	 
    public void draw(Canvas canvas) {
        super.draw(canvas);
	}
	 
}
