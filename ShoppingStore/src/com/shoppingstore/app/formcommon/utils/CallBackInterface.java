package com.shoppingstore.app.formcommon.utils;
  
import java.util.Map; 

/**
 * 回调接口，主要实现，FragmentB的数据返回给FragmentA处理
 * @author meicunzhi
 * @date 2015-11-9 下午4:20:34
 */
public interface CallBackInterface {
	public boolean callBack(Object object); 
}