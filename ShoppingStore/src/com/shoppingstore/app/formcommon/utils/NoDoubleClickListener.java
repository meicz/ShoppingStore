package com.shoppingstore.app.formcommon.utils;

import java.util.Calendar;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 防止按钮点击多次
 * @date 2015-11-28 上午8:09:29
 */
public abstract class NoDoubleClickListener implements OnClickListener {
	public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public abstract void onNoDoubleClick(View v);
    
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        } 
    }   
}
