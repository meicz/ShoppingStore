package com.shoppingstore.app.formcommon.main;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.shoppingstore.app.R;
import com.shoppingstore.app.areainfo.CityModel;
import com.shoppingstore.app.areainfo.DistrictModel;
import com.shoppingstore.app.areainfo.ProvinceModel;
import com.shoppingstore.app.database.AreaDataBaseHelper;
import com.shoppingstore.app.formcommon.utils.FragmentEx;
 
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
/**
 * 收货地址数据
 * @author meicunzhi
 * @date 2015-11-8 下午2:36:41
 */
public class AreaFragment extends FragmentEx {
	
	protected List<ProvinceModel> mPprovinceList; 
	
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	 
	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName = "";
	/**
	 * 当前省的编码
	 */
	protected String mCurrentProviceCode = "";
	
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName = "";
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityCode = "";
	
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";
	/**
	 * 当前区的编码
	 */
	protected String mCurrentDistrictCode = "";  
	
    protected void initProvinceDatas()
	{
    	AreaDataBaseHelper db = new AreaDataBaseHelper(getActivity(), "areainfo.db", null, 1);
    	mPprovinceList = db.getAreaData();  
		 
		mProvinceDatas = new String[mPprovinceList.size()];
    	for (int i = 0; i < mPprovinceList.size(); i++) {
    		// 遍历所有省的数据
    		mProvinceDatas[i] = mPprovinceList.get(i).getProvince().getName();
    		List<CityModel> cityList = mPprovinceList.get(i).getCityList();
    		String[] cityNames = new String[cityList.size()];
    		for (int j = 0; j < cityList.size(); j++) {
    			// 遍历省下面的所有市的数据
    			cityNames[j] = cityList.get(j).getCity().getName();
    			List<DistrictModel> districtList = cityList.get(j).getDistrictList();
    			String[] distrinctNameArray = new String[districtList.size()];
    			DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
    			for (int k = 0; k < districtList.size(); k++) {
    				// 遍历市下面所有区/县的数据
    				DistrictModel districtModel = new DistrictModel(districtList.get(k).getDistrict()); 
    				distrinctArray[k] = districtModel;
    				distrinctNameArray[k] = districtModel.getDistrict().getName();
    			}
    			// 市-区/县的数据，保存到mDistrictDatasMap
    			mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
    		}
    		// 省-市的数据，保存到mCitisDatasMap
    		mCitisDatasMap.put(mPprovinceList.get(i).getProvince().getName(), cityNames);
    	}
    
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addFragment(Boolean isAddStack, int fragmentLayoutId,
			Fragment fragment, String nowFragmentName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popBackStack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}
 
}
