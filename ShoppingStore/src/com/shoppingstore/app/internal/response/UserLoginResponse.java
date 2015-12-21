package com.shoppingstore.app.internal.response;

import java.util.List;

import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 用户登录响应
 * @author meicunzhi
 * @date 2015-10-28 上午10:29:09
 */
public class UserLoginResponse extends AoHanResponse { 
	 public String getToken(){
		 //检查状态是否正常，不正常返回空
		 if(!"0".equals(status) && responseStatus < 0) return "";
		 String token = getData("sessionId");
		 return token;
	 }
	 
	 public String getGwcQuantity(){
		 //检查状态是否正常，不正常返回空
		 if(!"0".equals(status) && responseStatus < 0) return "";
		 String token = getData("gwcQty");
		 return token;
	 }
}
