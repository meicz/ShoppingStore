package com.shoppingstore.app.internal.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.shoppingstore.app.common.bean.BrandBean;
import com.shoppingstore.app.common.bean.CommodityListBean;
import com.shoppingstore.app.common.bean.BrandBean;
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 品牌文化响应
 * @author meicunzhi
 * @date 2015-11-13 上午1:26:54
 */
public class BrandResponse extends AoHanResponse { 
	
	public List<BrandBean> getBrandList() throws JSONException{
		List<BrandBean> brandList = new ArrayList<BrandBean>();
		//检查状态是否正常，不正常返回空
		if(!"0".equals(status) && responseStatus < 0) return brandList;
		 
		List<JSONObject> jsons = getDatas("resultValue");
		for(int i = 0; i < jsons.size(); i++){
			JSONObject json = jsons.get(i);
			BrandBean brand = new BrandBean();
			brand.setItem(json.getString("item"));
			brand.setTitleCN(json.getString("titleCN"));
			brand.setTitleEN(json.getString("titleEN"));
			brand.setMem(json.getString("mem"));
			brand.setImgUrl(json.getString("imgUrl")); 
			brand.setXh(json.getString("xh")); 
			brand.setLinkUrl(json.getString("linkUrl")); 
			brandList.add(brand);
		}
		
		return brandList;
	}
	
}
