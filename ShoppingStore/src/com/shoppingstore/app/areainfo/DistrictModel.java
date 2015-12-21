package com.shoppingstore.app.areainfo;

import com.shoppingstore.app.common.bean.DistrictBean;

public class DistrictModel {
	private DistrictBean district;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(DistrictBean district) {
		super();
		this.district = district; 
	}

	public DistrictBean getDistrict() {
		return district;
	}

	public void setDistrict(DistrictBean district) {
		this.district = district;
	} 
}
