package com.shoppingstore.app.common.bean;

import android.graphics.Bitmap;

/**
 * 优惠券
 * @author meicunzhi
 * @date 2015-11-13 下午3:59:40
 */
public class QuanCenterBean {
	private String id;
	private String amount; 	//金额
	private String date;	//有效期
	private String type;	//券类型
	private String cdate;	//创建日期
	private String mem;		//说明
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCdate() {
		return cdate;
	}
	public void setCdate(String cdate) {
		this.cdate = cdate;
	}
	public String getMem() {
		return mem;
	}
	public void setMem(String mem) {
		this.mem = mem;
	} 
}
