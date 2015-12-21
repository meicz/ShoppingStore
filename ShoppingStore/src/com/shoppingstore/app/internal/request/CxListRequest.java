package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;

/**
 * 促销活动请求
 * @author meicunzhi
 * @date 2015-11-11 下午10:47:51
 */
public class CxListRequest extends AoHanRequest{
	public CxListRequest(){
		setUrl(GlobalData.GURL + "api/app/cxlist");
	} 
}
