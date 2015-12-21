package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 获取个人中心首页数据请求
 * @author meicunzhi
 * @date 2015-11-10 下午3:18:33
 */
public class UsercenterRequest extends AoHanRequest{
	public UsercenterRequest(){
		setUrl(GlobalData.GURL + "api/app/usercenter");
	} 
}
