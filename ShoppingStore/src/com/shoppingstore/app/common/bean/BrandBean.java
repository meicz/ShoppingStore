package com.shoppingstore.app.common.bean;

import android.graphics.Bitmap;

/**
 * 品牌文化
 * @author meicunzhi
 * @date 2015-11-13 上午1:25:05
 */
public class BrandBean {
	private String item;
	private String titleCN;
	private String titleEN;
	private String mem;
	private String imgUrl;
	private String xh;
	private String linkUrl;
	
	private Bitmap bitmap;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getTitleCN() {
		return titleCN;
	}
	public void setTitleCN(String titleCN) {
		this.titleCN = titleCN;
	}
	public String getTitleEN() {
		return titleEN;
	}
	public void setTitleEN(String titleEN) {
		this.titleEN = titleEN;
	}
	public String getMem() {
		return mem;
	}
	public void setMem(String mem) {
		this.mem = mem;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	} 
	
}
