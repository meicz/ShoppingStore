package com.shoppingstore.app.internal.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.shoppingstore.app.common.bean.OrderDetailBean;
import com.shoppingstore.app.common.bean.OrderDetailBeans;
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.internal.AoHanResponse;

/**
 * 申请退货
 * @author meicunzhi
 * @date 2015-12-14 上午6:03:48
 */
public class SalesdhthResponse extends AoHanResponse { 
	
	/**
	 * 订单号
	 * @return
	 */
	 public String getItem(){
		 //检查状态是否正常，不正常返回空
		 if(!"0".equals(status) && responseStatus < 0) return "";
		 String item = getData("item");
		 return item;
	 } 
	 
	 public List<OrderDetailBean> getOrderDetail(){
		 List<OrderDetailBean> orderDetails = new ArrayList<OrderDetailBean>();
		 if(!isAllStatus()) return orderDetails;
		 
		 try{
			 List<JSONObject> jsons = getDatas("resultValue");
			 for(int i = 0; i < jsons.size(); i++){
				 JSONObject json = jsons.get(i); 
				 OrderDetailBean orderDetail = new OrderDetailBean();
				 orderDetail.setItem(json.getString("item")); 	//订单号
				 orderDetail.setDate(json.getString("date")); 	//订单日期
				 orderDetail.setStatus(json.getString("status")); 	//订单状态  0：取消 2：待付款 3：已付款
				 orderDetail.setConsigneeName(json.getString("consigneeName")); 	//收货人姓名
				 orderDetail.setLinkPhone(json.getString("linkPhone")); 	//收货人联系电话
				 orderDetail.setOpenId(json.getString("openId")); 	//下单账号
				 orderDetail.setExCompany(json.getString("exCompany")); 	//快递名称
				 orderDetail.setExNo(json.getString("exNo")); 	//快递单号
				 orderDetail.setProvince(json.getString("province")); 	//省份
				 orderDetail.setCity(json.getString("city")); 	//市
				 orderDetail.setCounty(json.getString("county")); 	//县
				 orderDetail.setAddress(json.getString("address")); 	//地址
				 orderDetail.setPostCode(json.getString("postCode")); 	//邮编
				 orderDetail.setPossend(json.getString("possend")); 	//订单出货状态  1出货 0 未出
				 orderDetail.setQuan_id(json.getString("quan_id")); 	//使用券的ID
				 orderDetail.setQuanje(json.getString("quanje")); 	//使用券的金额
				 orderDetail.setFkje(json.getString("fkje")); 	//付款金额
				 orderDetail.setSygold(json.getString("sygold")); 	//使用积分金额
				 orderDetail.setFkfs(json.getString("fkfs")); 	//付款方式
				 orderDetail.setYditem(json.getString("yditem")); 	//运单号
				
				 //获取订单详情
				 List<OrderDetailBeans> orderItemLists = new ArrayList<OrderDetailBeans>();
				 String orderItems = json.getString("orderItems");  
				 JSONArray jsonArray = new JSONArray(orderItems); 
				 for(int x = 0; x < jsonArray.length(); x++){ 
					 JSONObject jsObj = (JSONObject) jsonArray.get(x);
					 OrderDetailBeans detas = new OrderDetailBeans();
					 detas.setItem(jsObj.getString("item"));
					 detas.setSpItem(jsObj.getString("spItem"));
					 detas.setCommodityCode(jsObj.getString("commodityCode"));
					 detas.setName(jsObj.getString("name"));
					 detas.setColor(jsObj.getString("color"));
					 detas.setColorName(jsObj.getString("colorName"));
					 detas.setSize(jsObj.getString("size"));
					 detas.setSizeName(jsObj.getString("sizeName")); 
					 detas.setPrice(jsObj.getString("price"));
					 detas.setDiscount(jsObj.getString("discount"));
					 detas.setAmount(jsObj.getString("amount"));
					 detas.setQuantity(jsObj.getString("quantity"));
					 detas.setImgUrl(jsObj.getString("imgUrl"));
					 detas.setAllowreturn(jsObj.getString("allowreturn"));
					 detas.setThstatus(jsObj.getString("thstatus"));
					 detas.setChstatus(jsObj.getString("chstatus")); 
					 orderItemLists.add(detas);
				 }
				 orderDetail.setOrderItems(orderItemLists);
				 
				 orderDetails.add(orderDetail);
			 }
		 }
		 catch(Exception e){
			 BusException busEx = new BusException("订单", "转换JSON字符串错误", e);
			 busEx.showException();
			 throw busEx; 
		 } 
		 
		 return orderDetails;
	 }
}
