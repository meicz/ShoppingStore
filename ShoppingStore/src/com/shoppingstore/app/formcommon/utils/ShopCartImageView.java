package com.shoppingstore.app.formcommon.utils;

import com.shoppingstore.app.common.global.GlobalData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @购物车，扩充ImageView在中间位置显示数量
 * @author meicunzhi
 * @date 2015-10-23 23:50:28
 */
public class ShopCartImageView extends ImageView{
	private String mQuantity = "";
	private Paint mPaint;
	private int mWidth = 0;
	private int mHeight = 0;
	private int mFontWidth = 0;
	private int mFontHeight = 0;
	
	public ShopCartImageView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public ShopCartImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ShopCartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		init();
	}
	
	public void init(){
		//获取购物车数量
		mQuantity = String.valueOf(GlobalData.gShopCartQty);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG  
                | Paint.DEV_KERN_TEXT_FLAG);  
		mPaint.setColor(Color.WHITE);  
		//根据比率设置字符大小
		mPaint.setTextSize(Math.round(20f * GlobalData.GRATIO));  
		mPaint.setTextAlign(Paint.Align.CENTER);     
	}

	public String getmQuantity() {
		return mQuantity;
	}

	public void setmQuantity(int quantity) {
		this.mQuantity = String.valueOf(quantity); 
		this.invalidate();
	}

	@Override
    protected void onDraw(Canvas canvas){
        // TODO Auto-generated method stub  
        super.onDraw(canvas);  
        
        //获取字符串高和宽
        if(mFontHeight == 0 && !"".equals(mQuantity)){
        	Rect rect = new Rect();
        	mPaint.getTextBounds(mQuantity, 0, mQuantity.length(), rect);
        	mFontHeight = rect.height();
        	mFontWidth = rect.width();
        } 
        
        mWidth = getWidth(); 
		mHeight = getHeight(); 
        if(!"".equals(mQuantity) && !"0".equals(mQuantity) ){
        	canvas.drawText(mQuantity, mWidth / 2, mHeight / 2 + mFontHeight - 2, mPaint);
        }
    }  
}
