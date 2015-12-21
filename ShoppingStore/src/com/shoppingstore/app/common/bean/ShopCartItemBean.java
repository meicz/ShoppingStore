package com.shoppingstore.app.common.bean;

import android.graphics.Bitmap;

/**
 * 购物车
 * @author meicunzhi
 * @date 2015-10-28 上午9:48:04
 */
public class ShopCartItemBean {
	private String id;			//序号
	private String item;		//商品编号(型号+'-'+颜色编码)
	private String size;		//尺码
	private String sizeName;	//尺码名称
	private String nameCN;		//中文名称
	private String imageUrl;	//图片地址 
	private String price;		//吊牌价
	private String salePrice;	//零售价
	private String quantity;	//修改的数量
	private String oldQuantity;	//从购物车中获取的原始数量，和quantity做判断是否要提交
	private String kcquantity;	//库存量
	private Bitmap bitmap;		//图片数据对象
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getSizeName() {
		return sizeName;
	}
	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	public String getNameCN() {
		return nameCN;
	}
	public void setNameCN(String nameCN) {
		this.nameCN = nameCN;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	} 
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getOldQuantity() {
		return oldQuantity;
	}
	public void setOldQuantity(String oldQuantity) {
		this.oldQuantity = oldQuantity;
	}
	public String getKcquantity() {
		return kcquantity;
	}
	public void setKcquantity(String kcquantity) {
		this.kcquantity = kcquantity;
	}  
}
