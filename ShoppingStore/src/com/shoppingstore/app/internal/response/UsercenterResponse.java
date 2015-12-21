package com.shoppingstore.app.internal.response;

import java.util.List;

import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 获取个人中心首页数据响应
 * @author meicunzhi
 * @date 2015-11-10 下午3:19:21
 */
public class UsercenterResponse extends AoHanResponse { 
	 public String getData(String name){
		 //检查状态是否正常，不正常返回空
		 if(!isAllStatus()) return "";
		 String value = super.getData(name);
		 return value;
	 } 
}
