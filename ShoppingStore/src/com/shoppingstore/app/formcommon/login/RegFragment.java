package com.shoppingstore.app.formcommon.login; 

import java.util.List;
 
import com.shoppingstore.app.R;
import com.shoppingstore.app.R.id;
import com.shoppingstore.app.R.layout;
import com.shoppingstore.app.ShopMain; 
import com.shoppingstore.app.common.bean.User;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.main.ShopActivity;
import com.shoppingstore.app.formcommon.utils.LoadingProgressDialog;
import com.shoppingstore.app.internal.request.UserLoginRequest;
import com.shoppingstore.app.internal.request.UsercenterRequest;
import com.shoppingstore.app.internal.response.UserLoginResponse;
import com.shoppingstore.app.internal.response.UsercenterResponse;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 用户注册
 * @author meicunzhi
 * @date 2015-10-09 20:08:15
 */
public class RegFragment extends Fragment{  
	private EditText edit_username; 
	private EditText edit_email;
	private EditText edit_password;
	private EditText edit_password2; 
	private EditText edit_nc;
	private EditText edit_yqz;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.reg_layout, container, false);
		
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		edit_username = (EditText) getView().findViewById(R.id.edit_username); 
		edit_email = (EditText) getView().findViewById(R.id.edit_email);
		edit_password = (EditText) getView().findViewById(R.id.edit_password);
		edit_password2 = (EditText) getView().findViewById(R.id.edit_password2);
		edit_nc = (EditText) getView().findViewById(R.id.edit_nc);
		edit_yqz = (EditText) getView().findViewById(R.id.edit_yqz);
		
		Button butReg = (Button) getActivity().findViewById(R.id.butReg);
		butReg.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {    
				 
				 final String userName = edit_username.getText().toString().trim();
				 final String email = edit_email.getText().toString().trim(); 
				 final String passWord = edit_password.getText().toString().trim();
				 final String passWord2 = edit_password2.getText().toString().trim();
				 final String nickName = edit_nc.getText().toString().trim();
				 final String yqcode = edit_yqz.getText().toString().trim();
				 
				 if("".equals(userName)) {
					 Toast toast = Toast.makeText(getActivity(), "请输入用户名!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 if("".equals(email)) {
					 Toast toast = Toast.makeText(getActivity(), "请输入邮箱地址!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 if("".equals(nickName)) {
					 Toast toast = Toast.makeText(getActivity(), "请输入昵称!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 if("".equals(passWord)) {
					 Toast toast = Toast.makeText(getActivity(), "请输入密码!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 if(passWord.length() < 6 || passWord.length() > 20) {
					 Toast toast = Toast.makeText(getActivity(), "请输入6-20个字符!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 if("".equals(passWord2)) {
					 Toast toast = Toast.makeText(getActivity(), "请再次输入密码!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 if(!passWord.equals(passWord2)) {
					 Toast toast = Toast.makeText(getActivity(), "两次校验密码错误!", Toast.LENGTH_LONG);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show(); 
					 return;
				 }
				 
				 final LoadingProgressDialog loading = new LoadingProgressDialog(getActivity()); 
				 loading.show();
				 new Thread(){  
					   @Override  
					   public void run(){ 
						  
						   try {
							   UsercenterRequest req = new UsercenterRequest(); 
							   req.put("userName", userName);
							   req.put("email", email);
							   req.put("nickName", nickName);
							   req.put("Password", passWord);
							   req.put("yqcode", yqcode);  
							   UsercenterResponse res = req.doPost(UsercenterResponse.class.getName());
							   if(res.isAllStatus()){
								   String token = res.getData("token");
								   User user = GlobalData.gUser;
								   user.setUserToken(token); 
								   
								   //注册成功打开主界面
								   Intent intent = new Intent();  
								   intent.setClass(RegFragment.this.getActivity(), ShopActivity.class);
								   startActivity(intent);  
								   RegFragment.this.getActivity().finish(); 
							   }							   
						   } catch (Exception e) {
							   // TODO Auto-generated catch block
							   e.printStackTrace(); 
						   } finally {
							   loading.dismiss(); 
						   }
					   }
				 }.start();
				 
			 }
		}); 
		
		TextView butwczc = (TextView) getActivity().findViewById(R.id.textView_wczc);
		butwczc.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) {  
				 getActivity().getSupportFragmentManager().popBackStack();
				 Fragment ref = getActivity().getSupportFragmentManager().findFragmentById(R.id.regframe);
				 FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				 fragmentTransaction.setCustomAnimations(R.anim.close, 0);   
				 fragmentTransaction.addToBackStack(null);
				 fragmentTransaction.show(ref).commit(); 
				 fragmentManager.popBackStack(); 
			 }
		}); 
	}
	
	public void showLogin(View view) { 
		getActivity().getSupportFragmentManager().popBackStack();
	}
}
