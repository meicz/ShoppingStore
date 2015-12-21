package com.shoppingstore.app.common.bean;
 
/**
 * 全局对象
 * @author meicunzhi
 * @date 2015-10-02 10:18:57
 */
public class User {
	private String userName; 		//用户名称
	private String userToken;		//用户TOKEN
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	} 
}