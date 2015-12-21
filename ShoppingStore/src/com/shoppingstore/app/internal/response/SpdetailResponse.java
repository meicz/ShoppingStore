package com.shoppingstore.app.internal.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
 
import com.shoppingstore.app.common.bean.SizeBean;
import com.shoppingstore.app.common.bean.SpdetailBean;
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 获取商品详情 响应
 * @author meicunzhi
 * @date 2015-10-25 11:05:50
 */
public class SpdetailResponse extends AoHanResponse { 
	
	/**
	 * 获取商品详情
	 * @return
	 * @throws JSONException 
	 */
	public SpdetailBean getSpdetail() throws JSONException{
		SpdetailBean sp = null;
		//检查状态是否正常，不正常返回空
		if(!"0".equals(status) && responseStatus < 0) return null; 
		sp = new SpdetailBean();
		List<JSONObject> jsons = getDatas("resultValue");
		for(int i = 0; i < jsons.size(); i++){
			JSONObject json = jsons.get(i); 
			sp.setId(json.getString("id"));
			sp.setCode(json.getString("code")); 
			sp.setNameCN(json.getString("nameCN"));
			sp.setNameEN(json.getString("nameEN"));
			sp.setPrice(json.getString("price"));
			sp.setDetailScription(json.getString("detailScription"));
			sp.setActivity_id(json.getString("activity_id"));
			sp.setSalePrice(json.getString("salePrice")); 
			
			//获取图片
			List<JSONObject> imgjsons = getDatas("imageArray");
			List<Map<String, String>> picmaps = new ArrayList<Map<String, String>>(); 
			for(int x = 0; x < imgjsons.size(); x++){
				json = imgjsons.get(x);
				Map<String, String> picmap = new HashMap<String, String>(); 
				picmap.put("id", json.getString("id"));
				picmap.put("imageUrl", json.getString("imageUrl")); 
				picmaps.add(picmap);
			} 
			sp.setPictues(picmaps);
			
			//获取尺码
			List<JSONObject> sizejsons = getDatas("sizeArray");
			Map<String, String> sizemap = new HashMap<String, String>(); 
			List<SizeBean> sizes = new ArrayList<SizeBean>();
			for(int x = 0; x < sizejsons.size(); x++){
				json = sizejsons.get(x);
				SizeBean size = new SizeBean();
				size.setId(json.getString("id"));
				size.setCode(json.getString("code"));
				size.setName(json.getString("name"));
				size.setQuantity(json.getString("quantity")); 
				sizes.add(size);
			}
			sp.setSizeArraycolorid(sizes);
		}
		
		return sp;
	}
}
