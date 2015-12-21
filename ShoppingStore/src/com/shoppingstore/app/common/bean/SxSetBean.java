package com.shoppingstore.app.common.bean;

/**
 * 获取筛选条件
 * @author meicunzhi
 * @date 2015-12-6 下午1:49:07
 */
public class SxSetBean {
	private String id;
	private String item;
	private String name;
	private String minzk;
	private String maxzk;
	private String sx;
	private String kind;
	private String xh;
	private boolean isSelect;
	
	//尺寸属性
	private String flag;
	private String sort;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMinzk() {
		return minzk;
	}
	public void setMinzk(String minzk) {
		this.minzk = minzk;
	}
	public String getMaxzk() {
		return maxzk;
	}
	public void setMaxzk(String maxzk) {
		this.maxzk = maxzk;
	}
	public String getSx() {
		return sx;
	}
	public void setSx(String sx) {
		this.sx = sx;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
}
