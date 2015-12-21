package com.shoppingstore.app.formcommon.Adapter;

import java.util.List;

import com.shoppingstore.app.R;
import com.shoppingstore.app.common.bean.CommodityListBean;
import com.shoppingstore.app.common.bean.SxSetBean;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
import com.shoppingstore.app.formcommon.utils.RadioButtonLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 查询条件适配器
 * @author meicunzhi
 * @date 2015-12-6 下午10:08:14
 */
public class CommodityFilterAdapter extends BaseAdapter{

	private Context mContext;  
	private List<SxSetBean> mCommodList;
	
	public CommodityFilterAdapter(Context context, List<SxSetBean> commodList){
		this.mContext = context;
		this.mCommodList = commodList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCommodList.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mCommodList.get(position);  
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//定义一个ImageView,显示在GridView里 
    	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		View view = inflater.inflate(R.layout.find_item_layout, null, false);   
		TextView textView_name = (TextView) view.findViewById(R.id.textView_name);   
		final SxSetBean com = mCommodList.get(position);
		textView_name.setText(com.getName()); 
		
		final RadioButtonLayout layout_bar = (RadioButtonLayout) view.findViewById(R.id.layout_bar);
		layout_bar.setSelect(com.isSelect()); 
		layout_bar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(layout_bar.getSelect()){ 
					layout_bar.setSelect(false);  
					com.setSelect(false);
				}
				else{ 
					layout_bar.setSelect(true);   
					com.setSelect(true);
				}  
			}
			
		});
		
		return view; 
	}
	
	public List<SxSetBean> getCommodList() {
		return mCommodList;
	}
	
	public void setCommodList(List<SxSetBean> commodList) {
		this.mCommodList = commodList;
	}

}
