package com.shoppingstore.app.internal;

import com.shoppingstore.app.internal.request.SplistRequest;
import com.shoppingstore.app.internal.request.UsercenterRequest;
import com.shoppingstore.app.internal.response.SplistResponse;
import com.shoppingstore.app.internal.response.UsercenterResponse;

public class test {
	public static void main(String[] args) {  
		SplistRequest req = new SplistRequest();
		req.put("category", "01");
		req.put("subcategory", "01");
		try {
			SplistResponse userRsp = req.doGet(SplistResponse.class.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/*UsercenterRequest req = new UsercenterRequest(); 
		//参数
		req.put("userName", "test");
		req.put("email", "test");
		req.put("nickName", "tes");
		req.put("Password", "tes");
		//获取数据
		UsercenterResponse userRsp = req.doPos(UsercenterResponse.class.getName());
		
		//先判断网络连接是否异常
		int rspStatus = userRsp.getResponseStatus();
		if(rspStatus < 0) {
			String error = userRsp.getResponseStatusMessage(rspStatus);
			System.out.println(error);
			return;
		}
		
		//判断返回的数据是否错误
		String status = userRsp.getStatus();
		if(!"0".equals(status)){
			String error = userRsp.getErrorMessage();
			System.out.println(error);
			return;
		}
		
		//成功
		String token = userRsp.getToken();*/
	}
}
