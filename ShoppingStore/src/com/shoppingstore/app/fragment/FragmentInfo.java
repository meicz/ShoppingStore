package com.shoppingstore.app.fragment;

/**
 * Fragment基本信息
 * @author meicunzhi
 * @date 2015-12-9 下午3:07:14
 */
public class FragmentInfo {
	private String name;			//名称
	private boolean isBackDestroy;	//是否按返回键销毁Fragment，true=返回就销毁，false=返回不销毁
	private boolean isBack;			//是否按返回键返回，true=能返回，false=不能返回
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isBackDestroy() {
		return isBackDestroy;
	}
	public void setBackDestroy(boolean isBackDestroy) {
		this.isBackDestroy = isBackDestroy;
	}
	public boolean isBack() {
		return isBack;
	}
	public void setBack(boolean isBack) {
		this.isBack = isBack;
	}
	
}