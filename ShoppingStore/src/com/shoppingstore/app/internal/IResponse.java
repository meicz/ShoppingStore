package com.shoppingstore.app.internal;

/**
 * 响应接口
 * @author meicunzhi
 * @date 2015-10-21 上午9:16:32
 */
public interface IResponse {
	public String getStatus();
	public void setStatus(String status);
	public String getErrorMessage();
	public void setErrorMessage(String message);
	public String getResultvalue();
	public void setResultvalue(String resultvalue);
	public int getResponseCode();
	public void setResponseCode(int responseCode);
	public boolean convertStringToJson(String resultString);
}
