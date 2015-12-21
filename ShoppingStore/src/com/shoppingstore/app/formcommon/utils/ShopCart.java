package com.shoppingstore.app.formcommon.utils;

import java.util.List; 

import com.shoppingstore.app.common.bean.ShopCartItemBean;

/**
 * 管理购物车信息
 * @author meicunzhi
 * @2015-10-25 7:53:19
 */
public class ShopCart {
	private List<ShopCartItemBean> shopCarts;
	private ShopCartImageView userShopCart;
	
	public ShopCart(ShopCartImageView userShopCart){
		
	}
 
	public void setShopCart(List<ShopCartItemBean> shopCart) {
		this.shopCarts = shopCart;
	}

	public ShopCartImageView getUserShopCart() {
		return userShopCart;
	}

	public void setUserShopCart(ShopCartImageView userShopCart) {
		this.userShopCart = userShopCart;
	}

	/**
	 * 添加购物车
	 * @param shopcart
	 * @return 成功true
	 */
	public boolean addShopCart(ShopCartItemBean shopcart){ 
		boolean isok = false;
		int row = findShopCart(shopcart);
		if(row == 0){
			shopCarts.add(shopcart);
		}else if(row > 0){
			isok = updateShopCart(row, shopcart);
		}
		
		return isok;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean updateShopCart(ShopCartItemBean shopcart){
		int row = findShopCart(shopcart);
		return updateShopCart(row, shopcart);
	}
	
	/**
	 * 更新购物车中的商品数量
	 * @param row
	 * @param shopcart
	 * @return
	 */
	private boolean updateShopCart(int row, ShopCartItemBean shopcart){
		if(row < 1) return false;
		
		int addQuantity = Integer.valueOf(shopcart.getQuantity());
		
		ShopCartItemBean sc = shopCarts.get(row);
		int quantity = Integer.valueOf(sc.getQuantity()) + addQuantity;
		sc.setQuantity(String.valueOf(quantity));
		shopCarts.set(row, sc);
		
		return true;
	}
	
	/**
	 * 查找项目是否存在
	 * @param shopcart
	 * @return 返回0不存在，大于0存在，-1为空 
	 */
	public int findShopCart(ShopCartItemBean shopcart){
		if(shopcart == null) return -1;
		
		String addItem = shopcart.getItem();
		String addSize = shopcart.getSize();
		
		int findRow = 0;
		for(int i = 0; i < shopCarts.size(); i++){
			ShopCartItemBean value = shopCarts.get(i);
			String item = value.getItem();
			String size = value.getSize();
			if(addItem.equals(item) && addSize.equals(size)){
				findRow = i;
				break;
			}
		}
		
		return findRow;
	}
}
