package com.shoppingstore.app.areainfo;

import java.util.List;

import com.shoppingstore.app.common.bean.CityBean;

public class CityModel {
	private CityBean city;
	private List<DistrictModel> districtList;
	
	public CityModel() {
		super();
	}

	public CityModel(CityBean city, List<DistrictModel> districtList) {
		super();
		this.city = city;
		this.districtList = districtList;
	}

	public CityBean getCity() {
		return city;
	}

	public void setCity(CityBean city) {
		this.city = city;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	} 
}
