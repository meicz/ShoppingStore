package com.shoppingstore.app.internal.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
 
import com.shoppingstore.app.common.bean.QuanCenterBean;
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 优惠券响应
 * @author meicunzhi
 * @date 2015-11-13 下午3:57:51
 */
public class QuanCenterResponse extends AoHanResponse {  
	
	public List<QuanCenterBean> getQuanCenterList() throws JSONException{
		List<QuanCenterBean> quanList = new ArrayList<QuanCenterBean>();;
		//检查状态是否正常，不正常返回空
		if(!"0".equals(status) && responseStatus < 0) return quanList; 
		
		List<JSONObject> jsons = getDatas("resultValue");
		for(int i = 0; i < jsons.size(); i++){
			JSONObject json = jsons.get(i);
			QuanCenterBean quan = new QuanCenterBean();
			quan.setId(json.getString("id"));
			quan.setAmount(json.getString("amount"));
			quan.setDate(json.getString("date"));
			quan.setType(json.getString("type"));
			quan.setCdate(json.getString("cdate"));
			quan.setMem(json.getString("mem")); 
			quanList.add(quan);
		}
		
		return quanList;
	}
}
