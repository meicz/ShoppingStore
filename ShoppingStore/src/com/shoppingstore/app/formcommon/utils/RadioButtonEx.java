package com.shoppingstore.app.formcommon.utils;

import com.shoppingstore.app.R;
 
import android.content.Context; 
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet; 
import android.view.View;
import android.widget.RadioButton;

/**
 * 自绘单选框，主要解决图片无法设置大小
 * 1、在attrs.xml文件中配置自定义样式
 * 2、设置drawableSize大小
 * 3、attrs.xml配置如下
 * <declare-styleable name="RadioButtonEx">
	    <attr name="drawableSize" format="dimension"/>
	    <attr name="drawableTop" format="string"/>
	    <attr name="drawableBottom" format="string"/>
	    <attr name="drawableRight" format="string"/>
	    <attr name="drawableLeft" format="string"/> 
	</declare-styleable>
	
 * @author meicunzhi
 * @date 2015-10-22 20:54:08
 *
 */
public class RadioButtonEx extends RadioButton { 
    private int mDrawableSize; 	
    private TypedArray mTypedArr;
    public RadioButtonEx(Context context) {
        this(context, null, 0);
    }
 
    public RadioButtonEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
 
    public RadioButtonEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub 
        mTypedArr = context.obtainStyledAttributes(attrs, R.styleable.RadioButtonEx); 
       
        selectRadio();
 
        this.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectRadio();
			}
        	
        });
    }
 
    public void selectRadio(){
    	Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null; 
		
        int count = mTypedArr.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = mTypedArr.getIndex(i); 
            switch (attr) {
            case R.styleable.RadioButtonEx_drawableSize:
                mDrawableSize = mTypedArr.getDimensionPixelSize(R.styleable.RadioButtonEx_drawableSize, 50); 
                break;
                
            case R.styleable.RadioButtonEx_drawableTop:
                drawableTop = mTypedArr.getDrawable(attr);
                break;
                
            case R.styleable.RadioButtonEx_drawableBottom:
                drawableRight = mTypedArr.getDrawable(attr);
                break;
                
            case R.styleable.RadioButtonEx_drawableRight:
                drawableBottom = mTypedArr.getDrawable(attr);
                break;
                
            case R.styleable.RadioButtonEx_drawableLeft:
                drawableLeft = mTypedArr.getDrawable(attr); 
                break;
                
            default :
                    break;
            }
        }
        
        //typedArr.recycle();
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }
    
    @Override
    protected void onDraw(Canvas canvas){
        // TODO Auto-generated method stub  
        super.onDraw(canvas);
       /* Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG  
                | Paint.DEV_KERN_TEXT_FLAG);  
        countPaint.setColor(Color.BLUE);  
        countPaint.setTextSize(20f);  
        countPaint.setTypeface(Typeface.DEFAULT_BOLD);  
        countPaint.setTextAlign(Paint.Align.CENTER);  
        canvas.drawText("sssss", 50, 50, countPaint);*/
        
    }  
    
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left,
            Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        
        if (right != null) {
            right.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        
        if (top != null) {
            top.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        
        setCompoundDrawables(left, top, right, bottom);
    }
 
}