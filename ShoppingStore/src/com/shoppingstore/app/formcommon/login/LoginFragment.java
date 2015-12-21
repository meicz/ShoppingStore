package com.shoppingstore.app.formcommon.login; 
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.shoppingstore.app.R;
import com.shoppingstore.app.R.id;
import com.shoppingstore.app.R.layout;
import com.shoppingstore.app.areainfo.ProvinceModel;
import com.shoppingstore.app.common.bean.CityBean;
import com.shoppingstore.app.common.bean.CommodityListBean;
import com.shoppingstore.app.common.bean.CxBean;
import com.shoppingstore.app.common.bean.DistrictBean;
import com.shoppingstore.app.common.bean.FavoriteBean;
import com.shoppingstore.app.common.bean.ProvinceBean;
import com.shoppingstore.app.common.bean.QuanCenterBean;
import com.shoppingstore.app.common.bean.SizeBean;
import com.shoppingstore.app.common.bean.User;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.database.AreaDataBaseHelper; 
import com.shoppingstore.app.database.CityData;
import com.shoppingstore.app.exception.BusException;
import com.shoppingstore.app.formcommon.main.ShopActivity;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.ImageRoundView;
import com.shoppingstore.app.formcommon.utils.LoadingProgressDialog;
import com.shoppingstore.app.internal.WebUtils; 
import com.shoppingstore.app.internal.request.AddressInfoRequest;
import com.shoppingstore.app.internal.request.BrandRequest;
import com.shoppingstore.app.internal.request.CxListRequest;
import com.shoppingstore.app.internal.request.CxSpListRequest;
import com.shoppingstore.app.internal.request.FavoriteRequest;
import com.shoppingstore.app.internal.request.QuanCenterRequest;
import com.shoppingstore.app.internal.request.SalesdhRequest;
import com.shoppingstore.app.internal.request.ShoppingCartRequest;
import com.shoppingstore.app.internal.request.UserAddressRequest;
import com.shoppingstore.app.internal.request.UserLoginRequest;
import com.shoppingstore.app.internal.request.UsercenterRequest;
import com.shoppingstore.app.internal.request.YzmCodeRequest;
import com.shoppingstore.app.internal.response.AddressInfoResponse;
import com.shoppingstore.app.internal.response.CxListResponse; 
import com.shoppingstore.app.internal.response.FavoriteResponse;
import com.shoppingstore.app.internal.response.QuanCenterResponse;
import com.shoppingstore.app.internal.response.SalesdhResponse;
import com.shoppingstore.app.internal.response.ShoppingCartResponse;
import com.shoppingstore.app.internal.response.SplistResponse;
import com.shoppingstore.app.internal.response.UserAddressResponse;
import com.shoppingstore.app.internal.response.UserLoginResponse;
import com.shoppingstore.app.internal.response.UsercenterResponse;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 用户登录
 * @author meicunzhi
 * @date 2015-10-05 09:10:24
 */
public class LoginFragment extends Fragment{ 
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login_layout, container, false);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		
		LinearLayout inclue_footer_menu_layout = (LinearLayout) getActivity().findViewById(R.id.inclue_footer_menu_layout);
		if(inclue_footer_menu_layout != null)
			inclue_footer_menu_layout.setVisibility(View.GONE); 
		
		OnClickListener clickReg = new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Fragment ref = getActivity().getSupportFragmentManager().findFragmentById(R.id.regframe);
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.setCustomAnimations(R.anim.open, R.anim.close);   
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.show(ref).commit(); 
			}
			
		};  
		TextView textView_userName = (TextView) getView().findViewById(R.id.textview_reg);
		ImageView imageview_reg = (ImageView) getView().findViewById(R.id.imageview_reg); 
		textView_userName.setOnClickListener(clickReg);
		imageview_reg.setOnClickListener(clickReg);
		
		Button butLogin = (Button) getActivity().findViewById(R.id.butLogin);
		butLogin.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {   
				 /*Intent intentpay = new Intent();  
				 intentpay.setClass(LoginFragment.this.getActivity(), PayDemoActivity.class);
				 startActivity(intentpay);  
				 LoginFragment.this.getActivity().finish();
				   if(true)return;*/
				 final LoadingProgressDialog loading = new LoadingProgressDialog(getActivity()); 
				 loading.show();   
				 /*CityData cityData = new CityData();
				 AreaDataBaseHelper db = new AreaDataBaseHelper(getActivity(), "areainfo.db", null, 1); 
				  for(int i = 0; i < cityData.provinceData.length; i++){
					 String sql = cityData.provinceData[i];
					 db.insertSql(sql); 
				 } 
				 for(int i = 0; i < cityData.cityData.length; i++){
					 String sql = cityData.cityData[i];
					 db.insertSql(sql); 
				 }
				 
				 for(int i = 0; i < cityData.countyData.length; i++){
					 String sql = cityData.countyData[i];
					 db.insertSql(sql); 
				 } */
				 /*ProvinceBean province = new ProvinceBean();
				 province.setCode("01");
				 province.setId("1");
				 province.setName("上海市");
				 province.setOrdernum("01");
				 db.insertProvince(province); 
				 
				 province.setCode("02");
				 province.setId("2");
				 province.setName("安徽省");
				 province.setOrdernum("02");
				 db.insertProvince(province); 
				 
				 
				 CityBean city = new CityBean();
				 city.setCode("01");
				 city.setId("01");
				 city.setName("上海市");
				 city.setProvinceId("01");
				 db.insertCity(city); 
				  
				 city.setCode("02");
				 city.setId("02");
				 city.setName("芜湖市");
				 city.setProvinceId("02");
				 db.insertCity(city); 
				 
				 city.setCode("03");
				 city.setId("03");
				 city.setName("合肥市");
				 city.setProvinceId("02");
				 db.insertCity(city); 
				 
				 DistrictBean district = new DistrictBean();
				 district.setCode("01");
				 district.setId("01");
				 district.setName("静安区");
				 district.setCityId("01");
				 db.insertDistrict(district); 
				 
				 district.setCode("02");
				 district.setId("02");
				 district.setName("长宁区");
				 district.setCityId("01");
				 db.insertDistrict(district); 
				 
				 district.setCode("03");
				 district.setId("03");
				 district.setName("宝山区");
				 district.setCityId("01");
				 db.insertDistrict(district); 
				 
				 district.setCode("04");
				 district.setId("04");
				 district.setName("南陵县");
				 district.setCityId("02");
				 db.insertDistrict(district); 
				 
				 district.setCode("05");
				 district.setId("05");
				 district.setName("三山区");
				 district.setCityId("02");
				 db.insertDistrict(district); 
				 
				 district.setCode("06");
				 district.setId("06");
				 district.setName("芜湖县");
				 district.setCityId("02");
				 db.insertDistrict(district); 
				  
				 List<ProvinceModel> provinceModels = db.getAreaData();*/
				 
				 EditText edit_userName = (EditText) getView().findViewById(R.id.edit_userName);
				 EditText edit_userPass = (EditText) getView().findViewById(R.id.edit_userPass);
				 final String userName = edit_userName.getText().toString().trim();
				 final String userPassword = edit_userPass.getText().toString().trim(); 
				
				 /*if("".equals(userName)) {
					 Toast toast = Toast.makeText(getActivity(), "请输入用户名!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
					
				 if("".equals(userPassword)) {
					 Toast toast = Toast.makeText(getActivity(), "请输入密码!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 } */
				 
				 new Thread(){  
					   @Override  
					   public void run(){ 
						   try {
							   /*UsercenterRequest zc = new UsercenterRequest();
							   zc.put("userName", "testabc");
							   zc.put("email", "testabc@163.com");
							   zc.put("nickName", "testabc");
							   zc.put("Password", "123456");
							   zc.put("yqcode", "");  
							   UsercenterResponse dd = zc.doPost(UsercenterResponse.class.getName());*/
							   
							   UserLoginRequest req = new UserLoginRequest(); 
							   if("".equals(userName) && "".equals(userPassword)){
								 //参数
								   req.put("userName", "testmmm"); 
								   req.put("userPassword", "123456");
								 }
							   else{
								 //参数
								   req.put("userName", userName); 
								   req.put("userPassword", userPassword);
							   }
							   
							   //获取数据
							   UserLoginResponse userRsp;
							   userRsp = req.doPost(UserLoginResponse.class.getName());
							  
								//{resultValue=[{"gwcQty":"0","sessionId":"96c5579e-32fc-4d6d-90bc-6cd252ea6b80"}]}
							   //成功
							   String token = userRsp.getToken();
							   int quantity = Integer.valueOf(userRsp.getGwcQuantity());
							   GlobalData.gShopCartQty = quantity;
							   User user = GlobalData.gUser;
							   user.setUserToken(token);    
							  
							   //token = "a9d710f0-cc07-4b2b-ac7b-b00e35ab474a";
							   /*SalesdhRequest salesReq = new SalesdhRequest(); 
							   salesReq.put("token", token);
							   salesReq.put("item", "1"); 
							   SalesdhResponse salesRes = salesReq.doGet(SalesdhResponse.class.getName());
							   salesRes.getOrderDetail();*/
							   /*UserAddressRequest r = new UserAddressRequest();
							   r.put("token", token); 
							   r.put("id", "242");
							   UserAddressResponse s = r.doGet(UserAddressResponse.class.getName());*/
							    
								/*SalesdhRequest salesReq = new SalesdhRequest(); 
								salesReq.put("token", token);
								salesReq.put("item", "3700013558"); 
								SalesdhResponse salesRes = salesReq.doGet(SalesdhResponse.class.getName());*/
								
							   //打开商城首页
							   Intent intent = new Intent();  
							   intent.setClass(LoginFragment.this.getActivity(), ShopActivity.class);
							   startActivity(intent);  
							   LoginFragment.this.getActivity().finish(); 
						   } catch (Exception e) {
							   // TODO Auto-generated catch block
							   e.printStackTrace();  
						   } finally {
							   loading.dismiss(); 
						   }
					   }
				 }.start();
				 /*new Thread(){  
					   @Override  
					   public void run()  
					   {  
					   //把网络访问的代码放在这里    
						   URL ur1l = null;
							HttpURLConnection http = null;
			String urls ="http://code.chics-mart.com:8018/api/app/splist";
							try {
								ur1l = new URL(urls);
								http = (HttpURLConnection) ur1l.openConnection();
								http.setDoInput(true);
								http.setDoOutput(true);
								http.setUseCaches(false);
								http.setConnectTimeout(50000);//设置连接超时
							//如果在建立连接之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
								http.setReadTimeout(50000);//设置读取超时
							//如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。			
								http.setRequestMethod("POST");
								// http.setRequestProperty("Content-Type","text/xml; charset=UTF-8");
								http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
								http.connect(); 
								String param = "&category=" + "01" 
										+ "&subcategory=" + "01" ;

								OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "utf-8");
								osw.write(param);
								osw.flush();
								osw.close();
								String result = "";
								if (http.getResponseCode() == 200) {
									BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
									String inputLine;
									while ((inputLine = in.readLine()) != null) {
										result += inputLine;
									}
									in.close();
									//result = "["+result+"]";
								}
							} catch (Exception e1) {
								System.out.println("err");
							} finally {
								if (http != null) http.disconnect(); 
							}
					  }  
					}.start();  
					*/
				 
				/* Intent intent = new Intent();  
				 intent.setClass(LoginFragment.this.getActivity(), ShopActivity.class);
	             startActivity(intent);  
	             LoginFragment.this.getActivity().finish();*/
			 }
		});
	}
	
	@Override  
	public void onHiddenChanged(boolean hidden){
		super.onHiddenChanged(hidden);  
		if(hidden){
			//隐藏界面 
			getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		}
		else{
			//显示界面 
			getActivity().findViewById(R.id.inclue_footer_menu_layout).setVisibility(View.GONE); 
		}
	}
}
