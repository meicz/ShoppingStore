package com.shoppingstore.app.common.bean;

import android.graphics.Bitmap;

/**
 * 订单详情信息
 * @author meicunzhi
 * @date 2015-11-22 下午6:02:09
 */
public class OrderDetailBeans {
	private String id;
	private String item; 	//订单号
	private String spItem; 	//商品编号(型号+'-'+颜色编码)
	private String commodityCode; 	//型号
	private String name; 	//中文品名
	private String color; 	//颜色
	private String colorName; 	//颜色名称
	private String size; 	//尺码
	private String sizeName; 	//尺码名称 
	private String price; 	//吊牌价
	private String discount; 	//折扣
	private String amount; 	//销售价
	private String quantity; 	//数量
	private String imgUrl; 	//图片路径
	private Bitmap imgBitmap; 	//图片数据
	private String allowreturn; 	//否允许客人退货 0不显示退货按钮 1申请退货 2直接退货
	private String thstatus; 	//退货状态  0未退货  1已退货
	private String chstatus; 	//出货状态  0未出货  1已出货
	private String sqth; 	//0未申请   1已申请  2客服同意
	
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
	public String getSpItem() {
		return spItem;
	}
	public void setSpItem(String spItem) {
		this.spItem = spItem;
	}
	public String getCommodityCode() {
		return commodityCode;
	}
	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getAllowreturn() {
		return allowreturn;
	}
	public void setAllowreturn(String allowreturn) {
		this.allowreturn = allowreturn;
	}
	public String getThstatus() {
		return thstatus;
	}
	public void setThstatus(String thstatus) {
		this.thstatus = thstatus;
	}
	public String getChstatus() {
		return chstatus;
	}
	public void setChstatus(String chstatus) {
		this.chstatus = chstatus;
	}
	public Bitmap getImgBitmap() {
		return imgBitmap;
	}
	public void setImgBitmap(Bitmap imgBitmap) {
		this.imgBitmap = imgBitmap;
	}
	public String getSqth() {
		return sqth;
	}
	public void setSqth(String sqth) {
		this.sqth = sqth;
	}  
}
