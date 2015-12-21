package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 获取商品列表请求
 * @author meicunzhi
 * @date 2015-10-20 15:25:50
 */
public class SplistRequest extends AoHanRequest{
	public SplistRequest(){
		setUrl(GlobalData.GURL + "api/app/splist");
	} 
}
