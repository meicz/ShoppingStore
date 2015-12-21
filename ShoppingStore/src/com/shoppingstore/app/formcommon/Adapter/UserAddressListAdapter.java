package com.shoppingstore.app.formcommon.Adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.UserAddressBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.main.PayFragment;
import com.shoppingstore.app.formcommon.main.UserAddressFragment;
import com.shoppingstore.app.formcommon.main.UserAddressListFragment;
import com.shoppingstore.app.formcommon.utils.CallBackInterface;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.RadioButtonLayout;
import com.shoppingstore.app.formcommon.utils.SerializableMap;
import com.shoppingstore.app.internal.request.AddressInfoRequest;
import com.shoppingstore.app.internal.request.UserAddressRequest;
import com.shoppingstore.app.internal.response.AddressInfoResponse;
import com.shoppingstore.app.internal.response.UserAddressResponse;

/**
 * 用户地址适配器
 * @author meicunzhi
 * @date 2015-11-8 下午10:16:54
 */
public class UserAddressListAdapter extends BaseAdapter{

	int i=0;
	private Context mContext;  
	private List<UserAddressBean> mUserAddressList; 
	private int mPosition; 
	private boolean mIsShowButton = false;
	private int oldSelectIndex = -1; 
	private GridView mGridView;
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){  
			case FragmentEx.UPDATE:{
				UserAddressBean userAdd;
				int index = msg.arg1;
				RadioButtonLayout radiobut_shdz = (RadioButtonLayout) msg.obj;
				  
				//取消选中状态
				if(oldSelectIndex >= 0 && oldSelectIndex != index){
					cancelSelelct(oldSelectIndex);
					userAdd = mUserAddressList.get(oldSelectIndex);
					userAdd.setMrdz("0");
					mUserAddressList.set(oldSelectIndex, userAdd);
				} 
				
				//设置选中
				userAdd = mUserAddressList.get(index);
				userAdd.setMrdz("1");
				mUserAddressList.set(index, userAdd); 
				oldSelectIndex = index;
				radiobut_shdz.setSelect(true); 
				
				GlobalData app = (GlobalData) ((FragmentActivity)mContext).getApplication();
				FragmentManagerEx fm = app.getFragment();
				UserAddressListFragment back = (UserAddressListFragment) fm.findFragment(UserAddressListFragment.class.getName());
				
				//触发PayFragment的隐藏事件
				back.popBackStack();
				
				break;
			}
			}
		}
	};
		
	public UserAddressListAdapter(Context context, GridView gridView, List<UserAddressBean> userAddressList){
		this.mContext = context;
		this.mUserAddressList = userAddressList; 
		this.mGridView = gridView;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUserAddressList.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mUserAddressList.get(position);  
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub 
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		View view = inflater.inflate(R.layout.address_layout, null, false);  
		final RadioButtonLayout radiobut_shdz =  (RadioButtonLayout) view.findViewById(R.id.radiobutton_shdz);
		ImageView imageView_address_delete =  (ImageView) view.findViewById(R.id.imageView_address_delete); 
		ImageView imageView_shdz =  (ImageView) view.findViewById(R.id.imageView_shdz); 
		final ImageView imageView_address_edit =  (ImageView) view.findViewById(R.id.imageView_address_edit); 
		UserAddressBean addr = mUserAddressList.get(position);
		 
		//修改地址事件
		imageView_address_edit.setOnClickListener(new View.OnClickListener(){
			 public void onClick(View v) { 		 
				 UserAddressBean selectaddr = mUserAddressList.get(position);
				 GlobalData app = (GlobalData) ((FragmentActivity)mContext).getApplication();
				 FragmentManagerEx fm = app.getFragment();
				 UserAddressFragment addressFragment = (UserAddressFragment) fm.findFragment(UserAddressFragment.class.getName());
				 if(addressFragment == null)
					 addressFragment = new UserAddressFragment(); 
				 
				 Bundle args = addressFragment.getArguments(); 
				 if(args == null){ 
					 args = new Bundle();
					 args.putString("addressId", selectaddr.getId()); 
					 args.putString("isAddMrdz", ""); 
					 args.putInt("position", position);
					 args.putString(FragmentEx.CALLBACKFRAGMENTNAME, UserAddressListFragment.class.getName());
					 addressFragment.setUserAddress(selectaddr);
					 addressFragment.setArguments(args);
				 }
				 else{ 
					 args.putInt("position", position);
					 args.putString("isAddMrdz", ""); 
					 args.putString("addressId", selectaddr.getId()); 
					 args.putString(FragmentEx.CALLBACKFRAGMENTNAME, UserAddressListFragment.class.getName());
					 addressFragment.setUserAddress(selectaddr);
				 }
				 fm.add(true, false, addressFragment, UserAddressFragment.class.getName());
			 }
		}); 
		
		//删除地址事件
		imageView_address_delete.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) { 
				
				try {
					UserAddressBean userAdd = mUserAddressList.get(position);
					String id = userAdd.getId(); 
					String token = GlobalData.gUser.getUserToken();
					UserAddressRequest reqa = new UserAddressRequest();
					reqa.put("token", token);
					reqa.put("id", id);   
					reqa.put("token", GlobalData.gUser.getUserToken());
					reqa.put("Shrname", userAdd.getShrname());
					reqa.put("LinkPhone", userAdd.getLinkPhone());
					reqa.put("province", userAdd.getProvince());
					reqa.put("provinceCode", userAdd.getProvinceCode());
					reqa.put("city", userAdd.getCity());
					reqa.put("cityCode", userAdd.getCityCode());
					reqa.put("county", userAdd.getCounty());
					reqa.put("countyCode", userAdd.getCityCode());
					reqa.put("address", userAdd.getAddress());
					reqa.put("postCode", userAdd.getPostCode());  
					reqa.put("mrdz", userAdd.getMrdz());  
					UserAddressResponse res = reqa.doDelete(UserAddressResponse.class.getName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});  
		
		//判断是否是默认地址，是设置选中状态
		if("1".equals(addr.getMrdz())){
			radiobut_shdz.setSelect(true); 
			oldSelectIndex = position;
		}
		else{
			radiobut_shdz.setSelect(false); 
		} 
		//选择默认地址事件，提交修改
		radiobut_shdz.setTag(position);
		radiobut_shdz.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) { 
				/*if(radiobut_shdz.getSelect()){ 
					radiobut_shdz.setSelect(false); 
				}
				else{ 
					radiobut_shdz.setSelect(true); 
				}*/
				  
				String sindex = String.valueOf(position);
				final int index = Integer.valueOf(sindex); 
				//重新设置PayFragment的addressobj值
				GlobalData app = (GlobalData) ((FragmentActivity)mContext).getApplication();
				final FragmentManagerEx fm = app.getFragment();
				PayFragment pay = (PayFragment) fm.findFragment(PayFragment.class.getName()); 
				if(pay != null){
					Bundle bundle = pay.getArguments();
					if(bundle != null){
						SerializableMap serMap = new SerializableMap();
						serMap.put("addressobj", mUserAddressList.get(index)); 
						bundle.putSerializable("addressobj", serMap);
					}
				} 
				
				Thread thread = new Thread(new Runnable(){
		            @Override  
		            public void run(){ 
		            	if( oldSelectIndex == index){
		            		Message msg = new Message();
							msg.what = FragmentEx.UPDATE;
							msg.arg1 = index;
							msg.obj = radiobut_shdz;
							mCommodHandler.sendMessage(msg);
		            		return;
		            	} 
		            	else{
		            		Message msg = new Message();
							msg.what = FragmentEx.UPDATE;
							msg.arg1 = index;
							msg.obj = radiobut_shdz;
							mCommodHandler.sendMessage(msg);
		            	}
						
		            	/*//提交修改
						try{
							UserAddressBean userAdd = mUserAddressList.get(oldSelectIndex);
							UserAddressRequest reqa = new UserAddressRequest();
							reqa.put("token", GlobalData.gUser.getUserToken()); 
							reqa.put("Shrname", userAdd.getShrname());
							reqa.put("id", userAdd.getId());
							reqa.put("LinkPhone", userAdd.getLinkPhone());
							reqa.put("province", userAdd.getProvince());
							reqa.put("provinceCode", userAdd.getProvinceCode());
							reqa.put("city", userAdd.getCity());
							reqa.put("cityCode", userAdd.getCityCode());
							reqa.put("county", userAdd.getCounty());
							reqa.put("countyCode", userAdd.getCityCode());
							reqa.put("address", userAdd.getAddress());
							reqa.put("postCode", userAdd.getPostCode());  
							reqa.put("mrdz", "0");  
							UserAddressResponse res = reqa.doPut(UserAddressResponse.class.getName()); 
							//请求成功
							if(res.isAllStatus()){
								
								userAdd = mUserAddressList.get(index);
								reqa = new UserAddressRequest();
								reqa.put("token", GlobalData.gUser.getUserToken()); 
								reqa.put("Shrname", userAdd.getShrname());
								reqa.put("id", userAdd.getId());
								reqa.put("LinkPhone", userAdd.getLinkPhone());
								reqa.put("province", userAdd.getProvince());
								reqa.put("provinceCode", userAdd.getProvinceCode());
								reqa.put("city", userAdd.getCity());
								reqa.put("cityCode", userAdd.getCityCode());
								reqa.put("county", userAdd.getCounty());
								reqa.put("countyCode", userAdd.getCityCode());
								reqa.put("address", userAdd.getAddress());
								reqa.put("postCode", userAdd.getPostCode());  
								reqa.put("mrdz", "1");  
								res = reqa.doPut(UserAddressResponse.class.getName()); 
								if(res.isAllStatus()){
									Message msg = new Message();
									msg.what = FragmentEx.UPDATE;
									msg.arg1 = index;
									msg.obj = radiobut_shdz;
									mCommodHandler.sendMessage(msg);
								} 
							} 
						}
						catch(Exception e) {
							e.printStackTrace();
						}*/
		            }
				});
				thread.start(); 
			}
		}); 
		
		if(mIsShowButton){
			imageView_shdz.setVisibility(View.VISIBLE);
			imageView_address_delete.setVisibility(View.VISIBLE);
		}
		else{
			imageView_shdz.setVisibility(View.GONE);
			imageView_address_delete.setVisibility(View.VISIBLE);
		}
		
		TextView textView_phone = (TextView) view.findViewById(R.id.textView_phone);
		TextView textView_address = (TextView) view.findViewById(R.id.textView_address); 
		
		textView_phone.setText(addr.getShrname() + "	" + addr.getLinkPhone());
		String address = addr.getAddress();
		String post = addr.getPostCode();
		if(!"".equals(post)){
			address += "," + post;
		}
		textView_address.setText(address); 
		
		return view; 
	}

	public void setIsShowButton(boolean isShowButton) {
		this.mIsShowButton = isShowButton;
	}

	public void setmUserAddressList(List<UserAddressBean> mUserAddressList) {
		this.mUserAddressList = mUserAddressList;
	}  
	
	private void cancelSelelct(int index)  
    {   
        // 获取当前可以看到的item位置  
        int visiblePosition = mGridView.getFirstVisiblePosition();
        View view = mGridView.getChildAt(index - visiblePosition);   
        if(view == null) return;
        RadioButtonLayout radiobut_shdz =  (RadioButtonLayout) view.findViewById(R.id.radiobutton_shdz);
        radiobut_shdz.setSelect(false); 
    }  
	
}
