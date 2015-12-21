package com.shoppingstore.app.common.bean;

/**
 * 城市
 * @author meicunzhi
 * @date 2015-11-7 下午9:28:49
 */
public class CityBean {
	private String id;
	private String name;
	private String code;
	private String provinceId;
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
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
}
