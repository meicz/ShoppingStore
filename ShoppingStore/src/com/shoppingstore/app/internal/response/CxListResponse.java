package com.shoppingstore.app.internal.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.shoppingstore.app.common.bean.CommodityListBean;
import com.shoppingstore.app.common.bean.CxBean;
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 促销活动响应
 * @author meicunzhi
 * @date 2015-11-11 下午10:48:29
 */
public class CxListResponse extends AoHanResponse {  
	
	public List<CxBean> getCxList() throws JSONException{
		List<CxBean> cxList = new ArrayList<CxBean>();
		//检查状态是否正常，不正常返回空
		if(!"0".equals(status) && responseStatus < 0) return cxList;
		 
		List<JSONObject> jsons = getDatas("resultValue");
		for(int i = 0; i < jsons.size(); i++){
			JSONObject json = jsons.get(i);
			CxBean cx = new CxBean();
			cx.setItem(json.getString("item"));
			cx.setTitleCN(json.getString("titleCN"));
			cx.setTitleEN(json.getString("titleEN"));
			cx.setMem(json.getString("mem"));
			cx.setImgUrl(json.getString("imgUrl")); 
			
			cxList.add(cx);
		}
		
		return cxList;
	}
}
