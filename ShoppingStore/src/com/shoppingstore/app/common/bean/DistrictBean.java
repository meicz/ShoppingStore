package com.shoppingstore.app.common.bean;

/**
 * 区县
 * @author meicunzhi
 * @date 2015-11-7 下午9:31:38
 */
public class DistrictBean {
	private String id;
	private String name;
	private String code;
	private String cityId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
}
