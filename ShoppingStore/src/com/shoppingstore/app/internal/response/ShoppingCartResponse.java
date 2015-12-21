package com.shoppingstore.app.internal.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
  
import com.shoppingstore.app.common.bean.ShopCartItemBean; 
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 购物车响应
 * @author meicunzhi
 * @date 2015-10-28 上午9:46:50
 */
public class ShoppingCartResponse extends AoHanResponse { 
	
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
	
	/**
	 * 获取购物车总的总数量
	 * @return
	 */
	public int getQuantity(){
		 //检查状态是否正常，不正常返回空
		 if(!"0".equals(status) && responseStatus < 0) return 0;
		 String squt = getData("quantity");
		 int quantity = squt == null || "".equals(squt) ? 0 : Integer.valueOf(squt);
		 return quantity;
	} 
	
	public List<ShopCartItemBean> getCommodityList() throws JSONException{
		List<ShopCartItemBean> comList = new ArrayList<ShopCartItemBean>();
		//检查状态是否正常，不正常返回空
		if(!"0".equals(status) && responseStatus < 0) return comList;
		 
		List<JSONObject> jsons = getDatas("resultValue");
		for(int i = 0; i < jsons.size(); i++){
			JSONObject json = jsons.get(i);
			ShopCartItemBean shopCart = new ShopCartItemBean(); 
			shopCart.setId(json.getString("id"));
			shopCart.setItem(json.getString("item"));
			shopCart.setSize(json.getString("size"));
			shopCart.setSizeName(json.getString("sizename"));
			shopCart.setNameCN(json.getString("nameCN"));
			shopCart.setPrice(json.getString("price"));
			shopCart.setSalePrice(json.getString("salePrice"));
			shopCart.setQuantity(json.getString("quantity"));
			shopCart.setOldQuantity(json.getString("quantity"));
			shopCart.setKcquantity(json.getString("kcquantity"));
			shopCart.setImageUrl(json.getString("imageUrl"));
			
			comList.add(shopCart);
		}
		
		return comList;
	}
}
