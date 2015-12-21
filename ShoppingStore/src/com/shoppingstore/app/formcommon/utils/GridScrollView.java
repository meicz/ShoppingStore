package com.shoppingstore.app.formcommon.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;

/**
 * 解决GridView放在ScrollView里面显示不全
 * @author meicunzhi
 * @date 2015-11-9 上午11:01:42
 */
public class GridScrollView extends GridView{  
    public GridScrollView(Context context, AttributeSet attrs) {   
    	super(context, attrs);   
    }   
     
    public GridScrollView(Context context) {   
    	super(context);   
    }   
     
    public GridScrollView(Context context, AttributeSet attrs, int defStyle) {   
    	super(context, attrs, defStyle);   
    }   
     
    @Override   
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
    	int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);   
    	super.onMeasure(widthMeasureSpec, expandSpec);   
    }   
}  