package com.whoyao.db;

import java.util.ArrayList;
import com.whoyao.model.AreaData;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 地址(省,市)数据
 * 
 * @author WCT create at：2013-3-28 上午09:40:22
 */
public class AreaListDB extends DataBaseUtil<AreaData> {

	private static final String TABLE_NAME = "AreaList";

	private static final String KEY_ID = "_id";
	private static final String CODE = "Code";
	private static final String AREA_NAME = "AreaName";
	private static final String PARENT_CODE = "ParentCode";
	private static final String HAVE_CHILD = "HavChild";
	private static final String LONGITUDE = "Longitude";
	private static final String LATITUDE = "Latitude";
	private static final String PINYIN = "PinYin";
	private static final String[] KEY_LIST = { KEY_ID, CODE, AREA_NAME, PARENT_CODE, HAVE_CHILD, LONGITUDE, LATITUDE};
	private String[] cityListColumns = { CODE, AREA_NAME, PARENT_CODE, LONGITUDE, LATITUDE, PINYIN };

	public AreaListDB() {
		dbHelper = new AreaSQLHelper();
	}

	// public AreaListDB(String tag) {
	// dbHelper = new AreaSQLHelper();
	// }

	@Override
	public ContentValues getContentValues(AreaData value) {
		return null;
	}

	@Override
	public AreaData create(Cursor cursor) {
		return null;
	}

	@Override
	protected String[] getQueryKeyList() {
		return KEY_LIST;
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	/** 获取省列表 */
	public ArrayList<AreaData> getProvinces() {
		// ParentCode为0的Area是省
		ArrayList<AreaData> list = getChildArea(0);
		for(AreaData area : list){
			switch (area.getCode()) {//查询省时,去掉直辖市的"市"字
			case 110000://四个直辖市
			case 120000:
			case 310000:
			case 500000:
				// Do Nothing
				break;
			case 450000://广西
			case 540000://西藏
			case 640000://宁夏
			case 650000://新疆
				area.setAreaName(area.getAreaName().substring(0, 2));
				break;
			case 150000://内蒙古
				area.setAreaName(area.getAreaName().substring(0, 3));
				break;
			default:
				area.setAreaName(area.getAreaName().replace("省", ""));
				break;
			}
		}
		return list;
	}

	/** 获取市列表<p>北京的下一级是北京市</p> */
	@Deprecated
	private ArrayList<AreaData> getCities(int parentCode) {
		switch (parentCode) {
		case 110000:
		case 120000:
		case 310000:
		case 500000:
			ArrayList<AreaData> list = new ArrayList<AreaData>();
			AreaData area = getAreaByCode(parentCode);
			if(null != area){
				list.add(area);
			}
			return list;
		default:
			return getChildArea(parentCode);
		}
	}

	/** 根据ParentCode获取地区表 */
	public ArrayList<AreaData> getChildArea(int ParentCode) {
		Cursor cursor = null;
		AreaData area = null;
		ArrayList<AreaData> areae = null;

		String[] values = { "" + ParentCode };
		try {
			cursor = this.findBySelection("ParentCode=?", values, "Code");
			if (cursor != null && cursor.getCount() > 0) {
				areae = new ArrayList<AreaData>();
				while (cursor.moveToNext()) {
					area = new AreaData();
					area.setCode(cursor.getInt(cursor.getColumnIndex(CODE)));
					area.setAreaName(cursor.getString(cursor.getColumnIndex(AREA_NAME)));
					area.setParentCode(cursor.getInt(cursor.getColumnIndex(PARENT_CODE)));
					area.setHaveChild(cursor.getInt(cursor.getColumnIndex(HAVE_CHILD)));
					area.setLongitude(cursor.getFloat(cursor.getColumnIndex(LONGITUDE)));
					area.setLatitude(cursor.getFloat(cursor.getColumnIndex(LATITUDE)));
					areae.add(area);
				}
				return areae;
			}
		} finally {
			if (null != cursor) {
				// cursor.close();
				closeDataBase(cursor);
			}
		}
		return null;
	}

	/** 根据Code获取AreaName */
	public AreaData getAreaByCode(int Code) {
		Cursor cursor = null;
		AreaData area = null;
		String[] values = { "" + Code };
		try {
			cursor = this.findBySelection("Code=?", values, null);
			if (cursor != null && cursor.getCount() > 0) {
				area = new AreaData();
				cursor.moveToNext();
				area.setCode(cursor.getInt(cursor.getColumnIndex(CODE)));
				area.setAreaName(cursor.getString(cursor.getColumnIndex(AREA_NAME)));
				area.setParentCode(cursor.getInt(cursor.getColumnIndex(PARENT_CODE)));
				area.setHaveChild(cursor.getInt(cursor.getColumnIndex(HAVE_CHILD)));
				area.setLongitude(cursor.getFloat(cursor.getColumnIndex(LONGITUDE)));
				area.setLatitude(cursor.getFloat(cursor.getColumnIndex(LATITUDE)));
				return area;
			}
		} finally {
			if (null != cursor) {
				// cursor.close();
				closeDataBase(cursor);
			}
		}
		return null;
	}

	public AreaData getAreaByName(String areaName) {
		Cursor cursor = null;
		AreaData area = null;
		String selectionArgs[] = { areaName + "%" };
		try {
			cursor = findBySelection("AreaName Like ?", selectionArgs, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToNext();
				area = new AreaData();
				area.setCode(cursor.getInt(cursor.getColumnIndex(CODE)));
				area.setAreaName(cursor.getString(cursor.getColumnIndex(AREA_NAME)));
				area.setParentCode(cursor.getInt(cursor.getColumnIndex(PARENT_CODE)));
				area.setHaveChild(cursor.getInt(cursor.getColumnIndex(HAVE_CHILD)));
				area.setLongitude(cursor.getFloat(cursor.getColumnIndex(LONGITUDE)));
				area.setLatitude(cursor.getFloat(cursor.getColumnIndex(LATITUDE)));
				return area;
			}
		} finally {
			if (null != cursor) {
				closeDataBase(cursor);
			}
		}
		return area;
	}

	public ArrayList<AreaData> getCityList() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		AreaData area = null;
		ArrayList<AreaData> cityList = null;
		try {
			db = new CitiesSQLHelper().open();
			cursor = db.query("CityList", cityListColumns, null, null, null, null, PINYIN);
			if (cursor != null && cursor.getCount() > 0) {
				int codeIndex = cursor.getColumnIndex(CODE);
				int nameIndex = cursor.getColumnIndex(AREA_NAME);
				int pinyinIndex = cursor.getColumnIndex(PINYIN);
				int parentIndex = cursor.getColumnIndex(PARENT_CODE);
				int longitudeIndex = cursor.getColumnIndex(LONGITUDE);
				int latitudeIndex = cursor.getColumnIndex(LATITUDE);
				cityList = new ArrayList<AreaData>();
				while (cursor.moveToNext()) {
					area = new AreaData();
					area.setCode(cursor.getInt(codeIndex));
					area.setAreaName(cursor.getString(nameIndex));
					area.setPinYin(cursor.getString(pinyinIndex));
					area.setParentCode(cursor.getInt(parentIndex));
					// area.setHaveChild(cursor.getInt(cursor.getColumnIndex(HAVE_CHILD)));
					area.setLongitude(cursor.getFloat(longitudeIndex));
					area.setLatitude(cursor.getFloat(latitudeIndex));
					cityList.add(area);
				}
			}
		} finally {
			closeDataBase(cursor);
		}
		return cityList;
	}

	// public void find() {
	// LogUtil.d(TAG, "[find]<" + 0 + ">:");
	// SQLiteDatabase db = dbHelper.open();
	// Hanyu hanyu = new Hanyu();;
	// ContentValues values = new ContentValues();
	// String selection = "_id=?" ;
	// String whereClause = "_id=?";
	// for(int i = 1;i<3889;i++){
	// String[] selectionArgs = {i+""};
	// Cursor cursor = db.query(TABLE_NAME, columns , selection ,
	// selectionArgs , null, null, null);
	// if(cursor.moveToNext()){
	// String name = cursor.getString(1);
	// LogUtil.i("WhoYao", i+"----"+name);
	// String strPinyin = hanyu.getStringPinYin(name);
	// LogUtil.i("WhoYao", i+"----"+strPinyin);
	// LogUtil.i("WhoYao", "id----"+cursor.getString(0));
	// values.put(PINYIN, strPinyin);
	// String[] whereArgs = {cursor.getString(0)};
	// db.update(TABLE_NAME, values, whereClause, whereArgs);
	// }
	// }
	// //dbHelper.close();
	// db.close();
	//
	// }

	// public void update(List<PinYin> list){
	// SQLiteDatabase db = dbHelper.open();
	// ContentValues values = new ContentValues();
	// String whereClause = "where _id in ?";
	// for(int i = 1;i<list.size();i++){
	//
	// values.put(PINYIN, list.get(i).getPinyin());
	// String[] whereArgs = {list.get(i).getId()};
	// db.update(TABLE_NAME, values , whereClause , whereArgs );
	//
	// }
	// db.close();
	// }

}
