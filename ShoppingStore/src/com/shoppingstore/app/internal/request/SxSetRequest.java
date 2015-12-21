package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 获取商品列表请求
 * @author meicunzhi
 * @date 2015-10-20 15:25:50
 */
public class SxSetRequest extends AoHanRequest{
	public SxSetRequest(){
		setUrl(GlobalData.GURL + "api/app/sxset");
	}
}
