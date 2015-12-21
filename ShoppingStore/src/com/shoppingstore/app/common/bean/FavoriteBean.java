package com.shoppingstore.app.common.bean;

import android.graphics.Bitmap;

/**
 * 商品列表bean
 * @author mei
 * @date 2015-10-24 13:30:50
 */
public class FavoriteBean {
	private String openid;
	private String date;
	private String item;		//商品编号(型号+'-'+颜色编码)
	private String name;		//中文名称
	private String imageUrl;	//图片
	private String tagPrice;	//商品零售价
	private String price;		//销售价格
	private String orderid;
	private String category;
	private String subCategory;
	private String activity_id;	//活动ID,正常商品默认 -1 
	private String id;
	private Bitmap bitmap;	//图片数据对象
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getTagPrice() {
		return tagPrice;
	}
	public void setTagPrice(String tagPrice) {
		this.tagPrice = tagPrice;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	 
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}  
	
}
