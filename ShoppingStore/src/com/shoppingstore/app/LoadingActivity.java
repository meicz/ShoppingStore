package com.shoppingstore.app; 

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;

import com.shoppingstore.app.database.AreaDataBaseHelper;
import com.shoppingstore.app.database.CityData;
  
public class LoadingActivity extends Activity{ 
	
	private Handler mCommodHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) { 
        	Intent intentpay = new Intent();  
			intentpay.setClass(load, ShopMain.class);
			startActivity(intentpay);  
			load.finish();
        }
	};
	
	private LoadingActivity load;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_layout);
		load = this;
		
		new Thread(){  
			   @Override  
			   public void run(){ 
				   CityData cityData = new CityData();
				   AreaDataBaseHelper db = new AreaDataBaseHelper(load, "areainfo.db", null, 1); 
				   
				   try {
					   for(int i = 0; i < cityData.provinceData.length; i++){
						   String sql = cityData.provinceData[i];
						   db.insertSql(sql); 
					   } 
				   }
				   catch(Exception e){
					   e.printStackTrace();
				   }
				   
				   try {
					   for(int i = 0; i < cityData.cityData.length; i++){
						   String sql = cityData.cityData[i];
						   db.insertSql(sql); 
					   }
				   }
				   catch(Exception e1){
					   e1.printStackTrace();
				   }
				   
				   try {
					   for(int i = 0; i < cityData.countyData.length; i++){
						   String sql = cityData.countyData[i];
						   db.insertSql(sql); 
					   }
				   }
				   catch(Exception e2){
					   e2.printStackTrace();
				   }
				    
				   mCommodHandler.sendMessage(new Message()); 
			   }
		 }.start();
		 
		new Thread(){  
			   @Override  
			   public void run(){
				   long i=0; 
				   try {
					sleep(4000);
					mCommodHandler.sendMessage(new Message());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }
		 }.start(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present. 
		return true;
	}
}
