package com.shoppingstore.app.internal.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.shoppingstore.app.common.bean.CommodityListBean;
import com.shoppingstore.app.common.bean.SxSetBean;
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 获取筛选条件
 * @author meicunzhi
 * @date 2015-12-6 下午1:55:25
 */
public class SxSetResponse extends AoHanResponse { 
	
	/**
	 * 获取除尺码的筛选条件
	 * @return
	 * @throws JSONException 
	 */
	public List<SxSetBean> getSxs() throws JSONException{
		List<SxSetBean> comList = new ArrayList<SxSetBean>();
		//检查状态是否正常，不正常返回空
		if(!"0".equals(status) && responseStatus < 0) return comList;
		 
		List<JSONObject> jsons = getDatas("resultValue");
		for(int i = 0; i < jsons.size(); i++){
			JSONObject json = jsons.get(i);
			SxSetBean com = new SxSetBean();
			com.setId(json.getString("id"));
			com.setItem(json.getString("item")); 
			com.setName(json.getString("name")); 
			com.setMinzk(json.getString("minzk")); 
			com.setMaxzk(json.getString("maxzk")); 
			com.setSx(json.getString("sx")); 
			com.setKind(json.getString("kind")); 
			com.setXh(json.getString("xh"));   
			comList.add(com);
		}
		
		return comList;
	}
	
	/**
	 * 获取尺码筛选条件
	 * @return
	 * @throws JSONException 
	 */
	public List<SxSetBean> getSizeSxs() throws JSONException{
		List<SxSetBean> comList = new ArrayList<SxSetBean>();
		//检查状态是否正常，不正常返回空
		if(!"0".equals(status) && responseStatus < 0) return comList;
		 
		List<JSONObject> jsons = getDatas("resultValue");
		for(int i = 0; i < jsons.size(); i++){
			JSONObject json = jsons.get(i);
			SxSetBean com = new SxSetBean();
			com.setId(json.getString("id"));
			com.setItem(json.getString("item")); 
			com.setName(json.getString("name")); 
			com.setFlag(json.getString("flag")); 
			com.setSort(json.getString("sort"));  
			comList.add(com);
		}
		
		return comList;
	}
}
