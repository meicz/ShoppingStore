package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 用户登录请求
 * @author meicunzhi
 * @date 2015-10-28 上午10:28:49
 */
public class CxSpListRequest extends AoHanRequest{
	public CxSpListRequest(){
		setUrl(GlobalData.GURL + "api/app/cxsplist");
	} 
}
