package com.shoppingstore.app.areainfo;

import java.util.List;

import com.shoppingstore.app.common.bean.ProvinceBean;

public class ProvinceModel {
	private ProvinceBean province;
	private List<CityModel> cityList;
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(ProvinceBean province, List<CityModel> cityList) {
		super();
		this.province = province;
		this.cityList = cityList;
	}

	public ProvinceBean getProvince() {
		return province;
	}

	public void setProvince(ProvinceBean province) {
		this.province = province;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	} 
}
