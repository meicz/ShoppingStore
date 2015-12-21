package com.shoppingstore.app.formcommon.Adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.QuanCenterBean;
import com.shoppingstore.app.common.global.GlobalData;
import com.shoppingstore.app.formcommon.main.PayFragment; 
import com.shoppingstore.app.formcommon.main.UserAddressListFragment;
import com.shoppingstore.app.formcommon.utils.CallBackInterface;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.FragmentManagerEx;
import com.shoppingstore.app.formcommon.utils.RadioButtonLayout;
import com.shoppingstore.app.internal.request.AddressInfoRequest;
import com.shoppingstore.app.internal.request.UserAddressRequest;
import com.shoppingstore.app.internal.response.AddressInfoResponse;
import com.shoppingstore.app.internal.response.UserAddressResponse;

/**
 * 用户地址适配器
 * @author meicunzhi
 * @date 2015-11-8 下午10:16:54
 */
public class QuanListAdapter extends BaseAdapter{

	int i=0;
	private Context mContext;  
	private List<QuanCenterBean> mQuanList; 
	private int mPosition; 
	private boolean mIsShowButton = false;
	private int oldSelectIndex = -1;
	private CallBackInterface callBackFragment; 
	
	//和UI线程连接，左右这么操作才能才子线程中操作控件
	private Handler mCommodHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){  
			case FragmentEx.UPDATE:{
				QuanCenterBean userAdd;
				int index = msg.arg1;
				RadioButtonLayout radiobut_shdz = (RadioButtonLayout) msg.obj;
				 
				boolean isok = callBackFragment.callBack(mQuanList.get(index)); 
				if(isok){
					
				}
				
				break;
			}
			}
		}
	};
		
	public QuanListAdapter(Context context, List<QuanCenterBean> quanList){
		this.mContext = context;
		this.mQuanList = quanList;  
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mQuanList.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mQuanList.get(position);
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
		View view = inflater.inflate(R.layout.quan_select_item_layout, null, false);   
		TextView textView_type = (TextView) view.findViewById(R.id.textView_type);
		TextView textView_amount = (TextView) view.findViewById(R.id.textView_amount);
		QuanCenterBean quanList = mQuanList.get(position);
		textView_type.setText(quanList.getType());
		textView_amount.setText(quanList.getAmount()); 
		
		return view; 
	}

	public void setIsShowButton(boolean isShowButton) {
		this.mIsShowButton = isShowButton;
	}

	public void setmQuanList(List<QuanCenterBean> quanList) {
		this.mQuanList = quanList;
	}   
	
}
