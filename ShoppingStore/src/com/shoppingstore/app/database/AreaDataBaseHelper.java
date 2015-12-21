package com.shoppingstore.app.database;

import java.util.ArrayList;
import java.util.List;

import com.shoppingstore.app.areainfo.CityModel;
import com.shoppingstore.app.areainfo.DistrictModel;
import com.shoppingstore.app.areainfo.ProvinceModel;
import com.shoppingstore.app.common.bean.CityBean;
import com.shoppingstore.app.common.bean.DistrictBean;
import com.shoppingstore.app.common.bean.ProvinceBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 地区数据库连接
 * @author meicunzhi
 * @date 2015-11-7 下午9:10:09
 */
public class AreaDataBaseHelper extends SQLiteOpenHelper {

	public AreaDataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		SQLiteDatabase db = this.getReadableDatabase(); 
		onCreate(db);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		/*db.execSQL("DROP TABLE IF EXISTS app_province");
		db.execSQL("DROP TABLE IF EXISTS app_city");
		db.execSQL("DROP TABLE IF EXISTS app_county");*/
		
		db.execSQL("create table if not exists app_province("
				  + "id integer primary key,"
				  + "name varchar,"
				  + "code varchar,"
				  + "ordernum integer)"); 
		
		db.execSQL("create table if not exists app_city("
				  + "id integer primary key,"
				  + "name varchar,"
				  + "code varchar,"
				  + "provinceid integer)");
		
		db.execSQL("create table if not exists app_county("
				  + "id integer primary key,"
				  + "name varchar,"
				  + "code varchar,"
				  + "cityid integer)");
	}
	
	public long insertProvince(ProvinceBean province){ 		
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		String id = province.getId();
		cv.put("id", id);
		cv.put("name", province.getName());
		cv.put("code", province.getCode());
		cv.put("ordernum", province.getOrdernum());
		long row = -1;
		row = queryCount("select count(*) from app_province where id = ? ", new String[]{ id });
		if(row <= 0)
			row = db.insert("app_province", null, cv);
		else
			row = updateProvince(id, cv);
		
		//db.close();
		
		return row;
	}
	
	private long updateProvince(String id, ContentValues cv){ 		
		SQLiteDatabase db = this.getReadableDatabase(); 
		String where = "id = ?";
		String[] whereValue ={ id }; 
		long row = db.update("app_province", cv, where, whereValue);
		
		//db.close();
		
		return row;
	}
	
	public long insertCity(CityBean city){ 		
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		String id = city.getId();
		cv.put("id", id);
		cv.put("name", city.getName());
		cv.put("code", city.getCode());
		cv.put("provinceid", city.getProvinceId());
		long row = -1;
		row = queryCount("select count(*) from app_city where id = ? ", new String[]{ id });
		if(row <= 0)
			row = db.insert("app_city", null, cv);
		else
			row = updateCity(id, cv);
		
		//db.close();
		
		return row;
	}
	
	private long updateCity(String id, ContentValues cv){ 		
		SQLiteDatabase db = this.getReadableDatabase(); 
		String where = "id = ?";
		String[] whereValue ={ id }; 
		long row = db.update("app_city", cv, where, whereValue);
		
		//db.close();
		
		return row;
	}
	
	public long insertDistrict(DistrictBean district){ 		
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		String id = district.getId();
		cv.put("id", id);
		cv.put("name", district.getName());
		cv.put("code", district.getCode());
		cv.put("cityid", district.getCityId());
		long row = -1;
		row = queryCount("select count(*) from app_county where id = ? ", new String[]{ id });
		if(row <= 0)
			row = db.insert("app_county", null, cv);
		else
			row = updateDistrict(id, cv);
		
		//db.close();
		
		return row;
	}
	
	private long updateDistrict(String id, ContentValues cv){ 		
		SQLiteDatabase db = this.getReadableDatabase(); 
		String where = "id = ?";
		String[] whereValue ={ id }; 
		long row = db.update("app_county", cv, where, whereValue);
		
		//db.close();
		
		return row;
	} 
	
	public List<ProvinceModel> getAreaData(){
		List<ProvinceModel> provinceModels = new ArrayList<ProvinceModel>();
		SQLiteDatabase db = this.getReadableDatabase();
		SQLiteDatabase dbc = this.getReadableDatabase();
		SQLiteDatabase dbd = this.getReadableDatabase();
		
		//查找省份
		Cursor curp = db.rawQuery("select * from app_province order by ordernum ", null); 
		while (curp.moveToNext()) {
			ProvinceModel pmod = new ProvinceModel(); 
			ProvinceBean pbean = new ProvinceBean();
			pbean.setId(curp.getString(curp.getColumnIndex("id")));
			pbean.setCode(curp.getString(curp.getColumnIndex("code")));
			pbean.setName(curp.getString(curp.getColumnIndex("name")));
			pbean.setOrdernum(curp.getString(curp.getColumnIndex("ordernum")));
			pmod.setProvince(pbean);
			
			//获取城市
			Cursor curc = dbc.rawQuery("select * from app_city where provinceid = ? ", new String[]{ pbean.getId() });  
			List<CityModel> cityModels = new ArrayList<CityModel>();
			while (curc.moveToNext()) {
				CityModel cmod = new CityModel(); 
				CityBean cbean = new CityBean();
				cbean.setId(curc.getString(curc.getColumnIndex("id")));
				cbean.setCode(curc.getString(curc.getColumnIndex("code")));
				cbean.setName(curc.getString(curc.getColumnIndex("name")));
				cbean.setProvinceId(curc.getString(curc.getColumnIndex("provinceid"))); 
				cmod.setCity(cbean);
				 
				//获取区县
				Cursor curd =dbd.rawQuery("select * from app_county where cityid = ? ", new String[]{ cbean.getId() });  
				List<DistrictModel> districtModels = new ArrayList<DistrictModel>();
				while (curd.moveToNext()) {
					DistrictModel dmod = new DistrictModel(); 
					DistrictBean dbean = new DistrictBean();
					dbean.setId(curd.getString(curd.getColumnIndex("id")));
					dbean.setCode(curd.getString(curd.getColumnIndex("code")));
					dbean.setName(curd.getString(curd.getColumnIndex("name")));
					dbean.setCityId(curd.getString(curd.getColumnIndex("cityid")));  
					dmod.setDistrict(dbean);
					districtModels.add(dmod);
				}
				cmod.setDistrictList(districtModels);
				cityModels.add(cmod);
			} 
			
			pmod.setCityList(cityModels);
			provinceModels.add(pmod);
		}  
		
		//db.close();
		
		return provinceModels;
	}
	
	public int queryCount(String sql, String sqlwhere[]){
		int count = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, sqlwhere); 
		while (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		
		cursor.close(); 
		//db.close();
		
		return count;
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void insertSql(String sql){ 		
		try{
			SQLiteDatabase db = this.getReadableDatabase(); 
			db.execSQL(sql); 
		}
		catch(Exception e){
			e.printStackTrace();
		} 
	}
}
