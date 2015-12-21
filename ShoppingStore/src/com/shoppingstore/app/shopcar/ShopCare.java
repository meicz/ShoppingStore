package com.shoppingstore.app.shopcar;
 
import java.io.InputStream;

import com.shoppingstore.app.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

public class ShopCare extends View{

	public ShopCare(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ShopCare(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub 
    }
    public ShopCare(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    	// TODO Auto-generated constructor stub 
    	initUI(context);
    }
    
    private void initUI(Context context){
    	/*LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.shopcar_layout, this, true);*/
    }
    
    @Override
    protected void onDraw(Canvas canvas){
        // TODO Auto-generated method stub  
        super.onDraw(canvas);
        Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG  
                | Paint.DEV_KERN_TEXT_FLAG);  
        countPaint.setColor(Color.BLUE);  
        countPaint.setTextSize(20f);  
        countPaint.setTypeface(Typeface.DEFAULT_BOLD);  
        countPaint.setTextAlign(Paint.Align.CENTER);  
       
        
        ImageView imageView;  
        imageView=new ImageView(getContext());  
        imageView.setLayoutParams(new GridView.LayoutParams(285, 285));  
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);  
        imageView.setPadding(8, 8, 8, 8);  
        imageView.setImageResource(R.drawable.commonshopcart2x);  
        imageView.setDrawingCacheEnabled(true);
        
        Bitmap bmp = ((BitmapDrawable) ((ImageView) imageView).getDrawable()).getBitmap(); 
        canvas.drawBitmap(bmp, 0, 0, null);
        imageView.setDrawingCacheEnabled(false);
      /*  
        Bitmap bitmap=null;                         //Bitmap对象  
        bitmap=((BitmapDrawable)getResources().getDrawable(R.drawable.commonshopcart2x)).getBitmap();  
        canvas.drawBitmap(bitmap, 0, 0, null);    
        canvas.drawText("sssss", 50, 50, countPaint); */
    }  
}
