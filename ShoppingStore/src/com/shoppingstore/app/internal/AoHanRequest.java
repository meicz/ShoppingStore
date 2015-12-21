package com.shoppingstore.app.internal;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.exception.BusException;

/**
 * 发送请求，所有请求接口继承该类
 * 该类继承了MAP，要传递给接口的参数用put添加
 * @author meicunzhi
 * @date 2015-10-21 上午9:26:02
 */
public class AoHanRequest extends HashMap<String, String> implements IRequest, Serializable{  
	private String url;  
	
	@Override
	public void setUrl(String url) {
		// TODO Auto-generated method stub
		this.url = url;
	}

	@Override
	public <T extends AoHanResponse> T doPost(String responseclassName) throws Exception {
		// TODO Auto-generated method stub 
		T tRsp = null;
		try {
			if(!isNetworkAvailable(GlobalData.gContext)){
				throw new BusException("", "网络异常或数据加载失败");
			}
			
			tRsp = (T) Class.forName(responseclassName).newInstance();
			String resultvalue = WebUtils.doPost(url, this); 
			if("".equals(resultvalue)){
				tRsp.setResponseStatus(-1);	
			}
			else{
				tRsp.setResponseStatus(0);
				boolean isok = tRsp.convertStringToJson(resultvalue);
				if(!isok){
					tRsp.setResponseStatus(-2);
				}
				else{
					if(tRsp.getStatus().equals("-1")){
						throw new BusException(tRsp.getStatus(), tRsp.getErrorMessage());
					}
				}
			}  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			if(e instanceof BusException){
				BusException busEx = (BusException) e;
				busEx.showException();
				throw busEx;
			} 
			else{ 
				BusException busEx = new BusException(e);
				busEx.showException();
				throw busEx;
			}
		} 
		
		return tRsp;
	}

	@Override
	public <T extends AoHanResponse> T doGet(String responseclassName) throws Exception {
		// TODO Auto-generated method stub
		T tRsp = null;
		try {
			if(!isNetworkAvailable(GlobalData.gContext)){
				throw new BusException("", "网络异常或数据加载失败");
			}
			
			tRsp = (T) Class.forName(responseclassName).newInstance();
			String resultvalue = WebUtils.doGet(url, this); 
			if("".equals(resultvalue)){
				tRsp.setResponseStatus(-1);	
			}
			else{
				tRsp.setResponseStatus(0);
				boolean isok = tRsp.convertStringToJson(resultvalue);
				if(!isok){
					tRsp.setResponseStatus(-2);
				}	
				else{
					if(tRsp.getStatus().equals("-1")){
						throw new BusException(tRsp.getStatus(), tRsp.getErrorMessage());
					}
				}
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
			
			if(e instanceof BusException){
				BusException busEx = (BusException) e;
				busEx.showException();
				throw busEx;
			} 
			else{ 
				BusException busEx = new BusException(e);
				busEx.showException();
				throw busEx;
			}
		} 
		
		return tRsp;
	}

	@Override
	public <T extends AoHanResponse> T doDelete(String responseclassName) throws Exception {
		// TODO Auto-generated method stub
		T tRsp = null;
		try {
			if(!isNetworkAvailable(GlobalData.gContext)){
				throw new BusException("", "网络异常或数据加载失败");
			}
			
			tRsp = (T) Class.forName(responseclassName).newInstance();
			String resultvalue = WebUtils.doDelete(url, this); 
			if("".equals(resultvalue)){
				tRsp.setResponseStatus(-1);	
			}
			else{
				tRsp.setResponseStatus(0);
				boolean isok = tRsp.convertStringToJson(resultvalue);
				if(!isok){
					tRsp.setResponseStatus(-2);
				}
				else{
					if(tRsp.getStatus().equals("-1")){
						throw new BusException(tRsp.getStatus(), tRsp.getErrorMessage());
					}
				}
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
			
			if(e instanceof BusException){
				BusException busEx = (BusException) e;
				busEx.showException();
				throw busEx;
			} 
			else{ 
				BusException busEx = new BusException(e);
				busEx.showException();
				throw busEx;
			}
		} 
		
		return tRsp;
	}

	@Override
	public <T extends AoHanResponse> T doPut(String responseclassName) throws Exception {
		// TODO Auto-generated method stub
		T tRsp = null;
		try {
			if(!isNetworkAvailable(GlobalData.gContext)){
				throw new BusException("", "网络异常或数据加载失败");
			}
			
			tRsp = (T) Class.forName(responseclassName).newInstance();
			String resultvalue = WebUtils.doPut(url, this); 
			if("".equals(resultvalue)){
				tRsp.setResponseStatus(-1);	
			}
			else{
				tRsp.setResponseStatus(0);
				boolean isok = tRsp.convertStringToJson(resultvalue);
				if(!isok){
					tRsp.setResponseStatus(-2);
				}	
				else{
					if(tRsp.getStatus().equals("-1")){
						throw new BusException(tRsp.getStatus(), tRsp.getErrorMessage());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
			
			if(e instanceof BusException){
				BusException busEx = (BusException) e;
				busEx.showException();
				throw busEx;
			} 
			else{ 
				BusException busEx = new BusException(e);
				busEx.showException();
				throw busEx;
			}
		}
		
		return tRsp;
	} 
	
	/**
	 * 判断网络连接是否正常
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {   
        ConnectivityManager cm = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (cm == null) {   
        } 
        else {
        	//如果仅仅是用来判断网络连接
        	//则可以使用 cm.getActiveNetworkInfo().isAvailable();    
            NetworkInfo[] info = cm.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
                        return true;   
                    }   
                }   
            }   
        }   
        return false;   
    }
}
