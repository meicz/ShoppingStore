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
 * 优惠券信息
 * @author meicunzhi
 * @date 2015-12-13 上午9:09:01
 */
public class QuanInfoListAdapter extends BaseAdapter{

	int i=0;
	private Context mContext;  
	private List<QuanCenterBean> mQuanList; 
	private int mPosition;  
	 
	public QuanInfoListAdapter(Context context, List<QuanCenterBean> quanList){
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
		View view = inflater.inflate(R.layout.quan_info_item_layout, null, false);  
		if(mQuanList.isEmpty()) return view;
			
		TextView textView_type = (TextView) view.findViewById(R.id.textView_type);
		TextView textView_amount = (TextView) view.findViewById(R.id.textView_amount);
		TextView textView_mem = (TextView) view.findViewById(R.id.textView_mem);
		QuanCenterBean quanList = mQuanList.get(position);
		textView_type.setText("优惠券:" + quanList.getType());
		textView_amount.setText(quanList.getAmount()); 
		textView_mem.setText("有效期至:" + quanList.getDate() + " 获取日期:" + quanList.getCdate()); 
		
		return view; 
	} 
}
