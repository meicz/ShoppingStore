package com.shoppingstore.app.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 拦截没有捕获到的异常，由BusException统一处理
 * @author meicunzhi
 * @date 2015-11-16 下午1:53:22
 */
public class CrashHandler implements UncaughtExceptionHandler {

	private static CrashHandler mInstance; 
	private Context mContext;
	
	private CrashHandler(){
		
	}
	
	/**
	 * 同步方法，以免单例多线程环境下出现异常
	 * @return
	 */
	public synchronized static CrashHandler getInstance(){  
        if (mInstance == null){
        	mInstance = new CrashHandler();
        }
        return mInstance;
    }
	
	/**
	 * 初始化，把当前对象设置成UncaughtExceptionHandler默认处理
	 * @param context
	 */
	public void init(Context context){
		mContext = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	} 
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		Log.d("shopEx", ex.getMessage());    
		new BusException(mContext, ex);
	}
 
}
