package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 优惠券请求
 * @author meicunzhi
 * @date 2015-11-13 下午3:57:13
 */
public class QuanCenterRequest extends AoHanRequest{
	public QuanCenterRequest(){
		setUrl(GlobalData.GURL + "api/app/quancenter");
	} 
}
