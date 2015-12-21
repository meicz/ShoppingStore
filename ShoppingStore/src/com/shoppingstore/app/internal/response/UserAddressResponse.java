package com.shoppingstore.app.internal.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.shoppingstore.app.common.bean.UserAddressBean;
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 获取用户地址响应
 * @author meicunzhi
 * @date 2015-11-2 上午5:18:10
 */
public class UserAddressResponse extends AoHanResponse { 
	
	/**
	 * 获取用户寄送地址
	 * @return
	 */
	 public List<UserAddressBean> getUserAddress(){ 
		 
		 List<UserAddressBean> userAddList = new ArrayList<UserAddressBean>();
		//检查状态是否正常，不正常返回空
		 if(!"0".equals(status) && responseStatus < 0) return userAddList; 
		 
		 List<JSONObject> jsons = getDatas("resultValue");
		 for(int i = 0; i < jsons.size(); i++){
			 UserAddressBean userAdd = new UserAddressBean();
			 JSONObject json = jsons.get(i); 
			 try {
				 userAdd.setId(json.getString("id"));
				 userAdd.setItem(json.getString("item"));
				 userAdd.setName(json.getString("name"));
				 userAdd.setShrname(json.getString("shrname"));
				 userAdd.setLinkPhone(json.getString("linkPhone"));
				 userAdd.setProvince(json.getString("province"));
				 userAdd.setProvinceCode(json.getString("provinceCode"));
				 userAdd.setCity(json.getString("city"));
				 userAdd.setCityCode(json.getString("cityCode"));
				 userAdd.setCounty(json.getString("county"));
				 userAdd.setCountyCode(json.getString("countyCode"));
				 userAdd.setAddress(json.getString("address"));
				 userAdd.setPostCode(json.getString("postCode"));
				 userAdd.setMrdz(json.getString("mrdz"));
				 userAddList.add(userAdd);
			 } catch (JSONException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 } 
		 }
		 
		 return userAddList;
	 }  
}
