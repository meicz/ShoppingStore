package com.shoppingstore.app.common.bean;

/**
 * 收货地址信息
 * @author meicunzhi
 * @date 2015-11-8 下午5:13:08
 */
public class UserAddressBean {
	private String id;			//收货人姓名
	private String item;		//登录账号
	private String name;		//登录昵称
	private String shrname;		//收货人
	private String linkPhone;	//联系方式
	private String province;	//省
	private String provinceCode; //省编码
	private String city;		//城市
	private String cityCode;	//城市编码
	private String county;		//区县
	private String countyCode;	//区县编码
	private String address;		//详细地址
	private String postCode;	//邮编
	private String mrdz;  	//是否默认地址：1是 0否
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShrname() {
		return shrname;
	}
	public void setShrname(String shrname) {
		this.shrname = shrname;
	}
	public String getLinkPhone() {
		return linkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
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
	public String getMrdz() {
		return mrdz;
	}
	public void setMrdz(String mrdz) {
		this.mrdz = mrdz;
	} 
}
