package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 获取商品详情请求
 * @author meicunzhi
 * @date 2015-10-25 11:05:50
 */
public class SpdetailRequest extends AoHanRequest{
	public SpdetailRequest(){
		setUrl(GlobalData.GURL + "api/app/spdetail");
	} 
}
