package com.shoppingstore.app.internal.request;

import com.shoppingstore.app.common.bean.UserAddressBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.internal.AoHanRequest;
import com.shoppingstore.app.internal.AoHanResponse;
import com.shoppingstore.app.internal.response.UserAddressResponse;
/**
 * 获取用户地址请求
 * @author meicunzhi
 * @date 2015-11-2 上午5:16:09
 */
public class UserAddressRequest extends AoHanRequest{
	public UserAddressRequest(){
		setUrl(GlobalData.GURL + "api/app/useraddress");
	} 
	
	/**
	 * 查询用户地址
	 * @param responseclassName
	 * @return
	 * @throws Exception 
	 */
	public <T extends UserAddressResponse> T getUserAddres(String responseclassName) throws Exception {
		return doGet(responseclassName);
	}
	
	/**
	 * 添加用户地址
	 * @param responseclassName
	 * @return
	 * @throws Exception 
	 */
	public <T extends UserAddressResponse> T addUserAddres(String responseclassName) throws Exception {
		return doPost(responseclassName);
	}
	
	/**
	 * 修改用户地址
	 * @param responseclassName
	 * @return
	 * @throws Exception 
	 */
	public <T extends UserAddressResponse> T updateUserAddres(String responseclassName) throws Exception {
		return doPut(responseclassName);
	}
	
	/**
	 * 删除用户地址
	 * @param responseclassName
	 * @return
	 * @throws Exception 
	 */
	public <T extends UserAddressResponse> T deleteUserAddres(String responseclassName) throws Exception {
		return doDelete(responseclassName);
	}
}
