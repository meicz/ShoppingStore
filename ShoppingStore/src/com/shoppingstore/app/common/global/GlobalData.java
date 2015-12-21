package com.shoppingstore.app.common.global;

import com.shoppingstore.app.common.bean.User;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.ShopCartImageView;

import android.app.Application; 
import android.content.Context;

public class GlobalData extends Application{
	private FragmentManagerEx gFragment;
	public static float GRATIO = 1; 	//比率
	public static int gShopCartQty = 0; //购物车商品数量
	public static User gUser = new User();  
	public static String GURL = "http://210.5.152.3:8028/"; 	//接口地址
	public static Context gContext;
	
	public FragmentManagerEx getFragment() {
		return gFragment;
	}

	public void setFragment(FragmentManagerEx gFragment) {
		this.gFragment = gFragment;
	}
	
	
	public User getUser() {
		return gUser;
	}

	public void setUser(User gUser) {
		this.gUser = gUser;
	}
 
	@Override  
	public void onCreate(){   
		super.onCreate();  
	}  
}
