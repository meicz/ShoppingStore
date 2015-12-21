package com.shoppingstore.app.common.bean;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * 商品详情bean
 * @author mei
 * @date 2015-10-25 19:40:50
 */
public class SpdetailBean {
	private String id;			//序号编码
	private String code; 		//商品编号(型号+'-'+颜色编码)
	private String imageUrl;	//图片
	private String nameCN;		//中文名称
	private String nameEN;		//英文名称 
	private String price;		//吊牌价
	private String detailScription;	//商品描述
	private String activity_id;	//正常商品(-1),活动商品(活动ID)
	private String salePrice;		//销售价格
	private List<Map<String, String>> pictues; //图片信息 
	private List<SizeBean> sizeArraycolorid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getNameCN() {
		return nameCN;
	}
	public void setNameCN(String nameCN) {
		this.nameCN = nameCN;
	} 
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDetailScription() {
		return detailScription;
	}
	public void setDetailScription(String detailScription) {
		this.detailScription = detailScription;
	}
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}   
	public List getPictues() {
		return pictues;
	}
	public void setPictues(List<Map<String, String>> pictues) {
		this.pictues = pictues;
	}
	public List<SizeBean> getSizeArraycolorid() {
		return sizeArraycolorid;
	}
	public void setSizeArraycolorid(List<SizeBean> sizeArraycolorid) {
		this.sizeArraycolorid = sizeArraycolorid;
	}
	public String getNameEN() {
		return nameEN;
	}
	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}  
	
}
