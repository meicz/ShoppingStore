package com.shoppingstore.app.common.bean;

import java.util.List;

/**
 * 订单信息
 * @author meicunzhi
 * @date 2015-11-22 下午6:05:52
 */
public class OrderDetailBean {
	private String item; 	//订单号
	private String date; 	//订单日期
	private String status; 	//订单状态  0：取消 2：待付款 3：已付款
	private String consigneeName; 	//收货人姓名
	private String linkPhone; 	//收货人联系电话
	private String openId; 	//下单账号
	private String exCompany; 	//快递名称
	private String exNo; 	//快递单号
	private String province; 	//省份
	private String city; 	//市
	private String county; 	//县
	private String address; 	//地址
	private String postCode; 	//邮编
	private String possend; 	//订单出货状态  1出货 0 未出
	private String quan_id; 	//使用券的ID
	private String quanje; 	//使用券的金额
	private String fkje; 	//付款金额
	private String sygold; 	//使用积分金额
	private String fkfs; 	//付款方式
	private String yditem; 	//运单号 
	private List<OrderDetailBeans> orderItems; 	//订单详情  
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getLinkPhone() {
		return linkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getExCompany() {
		return exCompany;
	}
	public void setExCompany(String exCompany) {
		this.exCompany = exCompany;
	}
	public String getExNo() {
		return exNo;
	}
	public void setExNo(String exNo) {
		this.exNo = exNo;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getPossend() {
		return possend;
	}
	public void setPossend(String possend) {
		this.possend = possend;
	}
	public String getQuan_id() {
		return quan_id;
	}
	public void setQuan_id(String quan_id) {
		this.quan_id = quan_id;
	}
	public String getQuanje() {
		return quanje;
	}
	public void setQuanje(String quanje) {
		this.quanje = quanje;
	}
	public String getFkje() {
		return fkje;
	}
	public void setFkje(String fkje) {
		this.fkje = fkje;
	}
	public String getSygold() {
		return sygold;
	}
	public void setSygold(String sygold) {
		this.sygold = sygold;
	}
	public String getFkfs() {
		return fkfs;
	}
	public void setFkfs(String fkfs) {
		this.fkfs = fkfs;
	}
	public String getYditem() {
		return yditem;
	}
	public void setYditem(String yditem) {
		this.yditem = yditem;
	}
	public List<OrderDetailBeans> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderDetailBeans> orderItems) {
		this.orderItems = orderItems;
	}
}
