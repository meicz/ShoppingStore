package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 申请退货
 * @author meicunzhi
 * @date 2015-12-14 上午6:02:30
 */
public class SalesdhthRequest extends AoHanRequest{
	public SalesdhthRequest(){
		setUrl(GlobalData.GURL + "api/app/salesdhth");
	} 
}
