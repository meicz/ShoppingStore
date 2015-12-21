package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 收藏请求
 * @author meicunzhi
 * @date 2015-11-13 上午10:14:23
 */
public class FavoriteRequest extends AoHanRequest{
	public FavoriteRequest(){
		setUrl(GlobalData.GURL + "api/app/myfavorite");
	} 
}
