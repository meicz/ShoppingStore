package com.shoppingstore.app.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List; 
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shoppingstore.app.exception.BusException;

/**
 * 请求响应，所有接口响应继承该类
 * @author meicunzhi
 * @date 2015-10-21 上午10:35:12
 */
public class AoHanResponse implements Serializable{
	protected String status;		//请求接口数据状态，0成功，-1失败
	protected String message;		//请求接口状态信息，ok成功，其它信息是报错误内容 
	protected int responseStatus;	//网络请求状态，0=网络正常、-1网络异常、 -2接受数据转换JSON异常、-3反射错误, -4无此参数 
	private Map<String, JSONArray> jsonMap; 	//存储json数组
	
	/**
	 * 请求接口数据状态，0成功，-1失败
	 * @return
	 */
	public String getStatus() {
		return status;
	}
	 
	public void setStatus(String status) {
		this.status = status;
	}
	 
	/**
	 * 请求接口状态信息，ok成功，其它信息是报错误内容
	 * @return
	 */
	public String getErrorMessage() {
		return message;
	}
	 
	public void setErrorMessage(String message) {
		this.message = message;
	}
	  
	/**
	 * 网络请求状态，0=网络正常、-1网络异常、 -2接受数据转换JSON异常、-3反射错误, -4无此参数
	 * @return
	 */
	public int getResponseStatus() {
		return responseStatus;
	}
	 
	public void setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
	} 
	
	/**
	 * 判断请求状态和接口返回状态是否正常
	 * @return
	 */
	public boolean isAllStatus(){
		if(responseStatus == 0 && "0".equals(status)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 将字符串转换成JSON对象
	 * @param resultString
	 * @return
	 */ 
	public boolean convertStringToJson(String resultValue){  
		JSONObject jsonObject = null;
		if(resultValue != null)
			resultValue = resultValue.replace("null", "\b");
		
		try {
			jsonMap = new HashMap<String, JSONArray>();
			//判断请求是否正常
			if(responseStatus < 0) return false;
			
			jsonObject = new JSONObject(resultValue);
			 
			status = jsonObject.getString("status"); 
			if(status.equals("-1")){ 
				//接口返回的失败消息
				message = jsonObject.getString("message"); 
				resultValue = "";
			}
			else{ 				
				resultValue = jsonObject.getString("resultValue"); 
				if(resultValue == null || "".equals(resultValue) || "null".equals(resultValue)){
					resultValue = "";
					return false;
				}
				//转换成JSON数组
				if("[".equals(resultValue.substring(0, 1))){
					jsonMap.put("resultValue", new JSONArray(resultValue));
				}
				else{
					JSONObject addObj = new JSONObject();
					//转换成JSON对象
					JSONObject jsObj = new JSONObject(resultValue);
					Iterator its = jsObj.keys();
					String key = "";
					while(its.hasNext()){
						try {
							key = (String) its.next();
							resultValue = jsObj.getString(key); 
							jsonMap.put(key, new JSONArray(resultValue));
						}
						catch (Exception e) {
							// TODO Auto-generated catch block  
							addObj.put(key, resultValue);
						} 
						
					}
					jsonMap.put("resultValue", new JSONArray("[" + addObj.toString() + "]"));
				}
				
				//jsonArr = new JSONArray(resultValue); 
				message = "ok";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
			
			try {
				message = jsonObject.getString("message");
				resultValue = "";
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				if(status.equals("-1000")){
					BusException busEx = new BusException("", message);
					throw busEx; 
				}
				else{
					if(status.equals("-1")){
						BusException busEx = new BusException("01", "转换JSON字符串错误", e);
						throw busEx; 
					} 
				} 
			} 
			
			if(status.equals("-1000")){
				BusException busEx = new BusException("", message);
				throw busEx; 
			}
			else{
				if(status.equals("-1")){
					BusException busEx = new BusException("01", "转换JSON字符串错误", e);
					throw busEx; 
				} 
			} 
		} 
		
		return true;
	}
	
	/**
	 * 获取单个数据
	 * @param name
	 * @return
	 * @throws JSONException 
	 */
	public String getData(String name){  
		String value = "";
		try {
			JSONArray jsonArr = jsonMap.get("resultValue");
			for(int i = 0; i < jsonArr.length(); i++){
				JSONObject obj1 = (JSONObject) jsonArr.get(i);
				value = obj1.getString(name); 
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			BusException busEx = new BusException("01", "转换JSON字符串错误", e);
			throw busEx; 
		} 
       
        return value;
	}
	
	/**
	 * 获取JSON数组
	 * @param jsonKeyName，要获取的KEY
	 * @return
	 */
	public List<JSONObject> getDatas(String jsonKeyName){  
		List<JSONObject> lists = new ArrayList<JSONObject>();
		try {
			JSONArray jsonArr = jsonMap.get(jsonKeyName);
			if(jsonArr == null) return lists;
			
			for(int i = 0; i < jsonArr.length(); i++){
	        	JSONObject obj1 = (JSONObject) jsonArr.get(i); 
	        	lists.add(obj1);
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			BusException busEx = new BusException("01", "转换JSON字符串错误", e);
			throw busEx; 
		}  
        
        return lists;
	}
	
	public String getResponseStatusMessage(int responseStatus){
		String msg = "";
		switch(responseStatus){
		case 0:
			msg = "网络正常";
			break;
			
		case -1:
			msg = "网络异常";
			break;
			
		case -2:
			msg = "接受数据转换JSON异常";
			break;
			
		case -3:
			msg = "反射错误";
			break;
			
		case -4:
			msg = "获取参数错误，无此参数";
			break;
		}
		return msg;
	}
}
