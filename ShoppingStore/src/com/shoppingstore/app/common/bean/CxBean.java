package com.shoppingstore.app.common.bean;

import android.graphics.Bitmap;

/**
 * 促销活动信息
 * @author meicunzhi
 * @date 2015-11-12 上午4:36:52
 */
public class CxBean {
	private String item;
	private String titleCN;
	private String titleEN;
	private String mem;
	private String imgUrl;
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
}
