package com.shoppingstore.app.internal;

import java.io.IOException;

/**
 * 请求接口
 * @author meicunzhi
 * @date 2015-10-21 上午9:12:02
 */
public interface IRequest{
	public void setUrl(String url); 
	public <T extends AoHanResponse> T doPost(String responseclassName) throws Exception;
	public <T extends AoHanResponse> T doGet(String responseclassName) throws Exception;
	public <T extends AoHanResponse> T doDelete(String responseclassName) throws Exception;
	public <T extends AoHanResponse> T doPut(String responseclassName) throws Exception;
}
