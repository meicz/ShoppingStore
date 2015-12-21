package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 取消订单
 * @author meicunzhi
 * @date 2015-11-10 下午1:57:51
 */
public class SalesdhRequest extends AoHanRequest{
	public SalesdhRequest(){
		setUrl(GlobalData.GURL + "api/app/salesdh");
	} 
}
