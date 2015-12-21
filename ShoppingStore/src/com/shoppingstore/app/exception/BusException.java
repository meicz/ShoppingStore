package com.shoppingstore.app.exception;

import com.shoppingstore.app.common.global.GlobalData;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 异常统一处理类
 * exCode=9999是未处理异常
 * @author meicunzhi
 * @date 2015-11-16 下午2:52:46
 */
public class BusException extends RuntimeException {
	
	protected String exCode;
	protected String exMsg;
	protected Throwable exThrowable;
	protected Context mContext = null;
	
	public BusException(Context context, Throwable ex) {
		exThrowable = ex; 
		this.mContext = context;
		
		showException();
	}
	
	public BusException(Throwable ex) {
		exThrowable = ex;  
		this.mContext = GlobalData.gContext; 
	}
	
	public BusException(String exCode, String exMsg) {
		super();
		this.exCode = exCode;
		this.exMsg = exMsg;  
		this.mContext = GlobalData.gContext; 
	}
	 
	public BusException(String exCode, String exMsg, Throwable ex) {
		super(ex);
		this.exCode = exCode;
		this.exMsg = exMsg;
		this.exThrowable = ex;  
		this.mContext = GlobalData.gContext; 
	} 
	 
	public String getExceptionMsg(){
		String msg = exMsg;
		if(msg == null || "".equals(msg)){
			msg = exThrowable.toString();
		}
		
		if(exCode == null || "".equals(exCode)){
			exCode = "9999";
		} 
		 
		String value = "错误代码：" + exCode + "\r\n错误消息：" + msg;
		return value;
	}
	
	public void showException() {  
		if(exThrowable != null) 
			exThrowable.printStackTrace();
		
        //使用Toast来显示异常信息  
        new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();  
                Toast toast = Toast.makeText(mContext, getExceptionMsg(), Toast.LENGTH_LONG); 
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();  
                Looper.loop();  
            }  
        }.start();   
 }
}
