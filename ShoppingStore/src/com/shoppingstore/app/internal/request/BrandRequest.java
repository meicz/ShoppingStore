package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 品牌文化请求
 * @author meicunzhi
 * @date 2015-11-13 上午1:25:37
 */
public class BrandRequest extends AoHanRequest{
	public BrandRequest(){
		setUrl(GlobalData.GURL + "api/app/brand");
	} 
}
