package com.shoppingstore.app.internal.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.shoppingstore.app.common.bean.CommodityListBean;
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 获取商品列表 响应
 * @author meicunzhi
 * @date 2015-10-20 15:28:10
 */
public class SplistResponse extends AoHanResponse { 
	
	/**
	 * 获取商品列表
	 * @return
	 * @throws JSONException 
	 */
	public List<CommodityListBean> getCommodityList() throws JSONException{
		List<CommodityListBean> comList = new ArrayList<CommodityListBean>();
		//检查状态是否正常，不正常返回空
		if(!"0".equals(status) && responseStatus < 0) return comList;
		 
		List<JSONObject> jsons = getDatas("resultValue");
		for(int i = 0; i < jsons.size(); i++){
			JSONObject json = jsons.get(i);
			CommodityListBean com = new CommodityListBean();
			com.setItem(json.getString("item"));
			com.setName(json.getString("name"));
			com.setImageUrl(json.getString("imageUrl"));
			com.setTagPrice(json.getString("tagPrice"));
			com.setPrice(json.getString("price"));
			com.setActivity_id(json.getString("activity_id"));
			com.setId(json.getString("id"));
			com.setDisItem(json.getString("disItem"));
			com.setRecommend(json.getString("recommend"));
			com.setQuantity(json.getString("quantity"));
			
			comList.add(com);
		}
		
		return comList;
	}
}
