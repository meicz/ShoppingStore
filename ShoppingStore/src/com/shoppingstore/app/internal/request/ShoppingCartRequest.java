package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 购物车请求
 * @author meicunzhi
 * @date 2015-10-28 上午9:45:57
 */
public class ShoppingCartRequest extends AoHanRequest{
	public ShoppingCartRequest(){
		setUrl(GlobalData.GURL + "api/app/ShoppingCart");
	} 
	
	/**
	 * 添加商品到购物车
	 * @param responseclassName
	 * @return
	 * @throws Exception 
	 */
	public <T extends AoHanResponse> T addShopCart(String responseclassName) throws Exception {
		return doPost(responseclassName);
	}
	
	/**
	 * 获取购物信息
	 * @param responseclassName
	 * @return
	 * @throws Exception 
	 */
	public <T extends AoHanResponse> T getShopCart(String responseclassName) throws Exception {
		return doGet(responseclassName);
	}
	
	/**
	 * 删除购物车
	 * @param responseclassName
	 * @return
	 * @throws Exception 
	 */
	public <T extends AoHanResponse> T deleteShopCart(String responseclassName) throws Exception {
		return doDelete(responseclassName);
	}
	
	/**
	 * 修改购物车
	 * @param responseclassName
	 * @return
	 * @throws Exception 
	 */
	public <T extends AoHanResponse> T updateShopCart(String responseclassName) throws Exception {
		return doPut(responseclassName);
	}
}
