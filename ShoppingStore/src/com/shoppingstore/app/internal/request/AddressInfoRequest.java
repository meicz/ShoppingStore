package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 获取省市区信息请求
 * @author meicunzhi
 * @date 2015-11-7 上午12:37:54
 */
public class AddressInfoRequest extends AoHanRequest{
	public AddressInfoRequest(){
		setUrl(GlobalData.GURL + "api/app/addressinfo");
	} 
}
