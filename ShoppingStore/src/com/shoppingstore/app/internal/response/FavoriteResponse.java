package com.shoppingstore.app.internal.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
  
import com.shoppingstore.app.common.bean.FavoriteBean; 
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 购物车响应
 * @author meicunzhi
 * @date 2015-10-28 上午9:46:50
 */
public class FavoriteResponse extends AoHanResponse { 
	
	/**
	 * 获取返回的用户TOKEN
	 * @return
	 */
	public String getToken(){
		 //检查状态是否正常，不正常返回空
		 if(!"0".equals(status) && responseStatus < 0) return "";
		 String token = getData("token");
		 return token;
	}
	 
	public List<FavoriteBean> getFavoriteList() throws JSONException{
		List<FavoriteBean> favoriteList = new ArrayList<FavoriteBean>();
		//检查状态是否正常，不正常返回空
		if(!"0".equals(status) && responseStatus < 0) return favoriteList;
		 
		List<JSONObject> jsons = getDatas("resultValue");
		for(int i = 0; i < jsons.size(); i++){
			JSONObject json = jsons.get(i);
			FavoriteBean favorite = new FavoriteBean();  
			favorite.setOpenid(json.getString("openid"));
			favorite.setDate(json.getString("date"));
			favorite.setItem(json.getString("item"));
			favorite.setName(json.getString("name"));
			favorite.setImageUrl(json.getString("imageUrl"));
			favorite.setPrice(json.getString("price"));
			favorite.setOrderid(json.getString("orderid"));
			favorite.setCategory(json.getString("category"));
			favorite.setSubCategory(json.getString("subCategory"));
			favorite.setActivity_id(json.getString("activity_id"));
			favorite.setId(json.getString("id")); 
			 
			favoriteList.add(favorite);
		}
		
		return favoriteList;
	}
}
