package com.shoppingstore.app;  
   
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.exception.CrashHandler;
import com.shoppingstore.app.formcommon.utils.ShopCartImageView;

import android.os.Bundle; 
import android.support.v4.app.Fragment; 
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Menu;  
import android.view.WindowManager;

public class ShopMain extends FragmentActivity {  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_main_layout);  
		
		Fragment ref = getSupportFragmentManager().findFragmentById(R.id.regframe);
		getSupportFragmentManager().beginTransaction().hide(ref).commit();
	  
		//获取屏幕比率
		DisplayMetrics displayMetrics = new DisplayMetrics();  
		WindowManager wm = this.getWindowManager(); 
		wm.getDefaultDisplay().getMetrics(displayMetrics);   
		int screenWidth = displayMetrics.widthPixels;  
		int screenHeight = displayMetrics.heightPixels;  
		float ratioWidth = (float)screenWidth / 480; 	//480开发时的屏幕宽度
		float ratioHeight = (float)screenHeight / 800;  //800开发时的屏幕高度
		GlobalData.GRATIO = Math.min(ratioWidth, ratioHeight);   
		GlobalData.gContext = getApplicationContext();
		
		/*if(savedInstanceState == null){
			LoginFragment login = new LoginFragment();
			final FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.add(R.id.id_content, login);  
			fragmentTransaction.commit(); 
		} */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shop_main, menu);
		return true;
	} 
}
