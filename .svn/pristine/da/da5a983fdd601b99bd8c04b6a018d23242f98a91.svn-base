package com.whoyao.db;

import java.util.ArrayList;
import java.util.List;

import com.whoyao.utils.L;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 数据库工具类
 * 
 * @author WCT 
 * create at：2012-11-20 下午02:12:08
 */
public abstract class DataBaseUtil<T extends Object> {
	private static final String TAG = "DataBaseUtil";
	private static final Object dBLock = new Object();// Constant.DB_LOCK;

	protected DBHelper dbHelper;
	private String tag;

	public DataBaseUtil() {
		dbHelper = new DBHelper();
		this.tag = "";
	}

	public DataBaseUtil(String tag) {
		dbHelper = new DBHelper();
		this.tag = tag;
	}

	/**
	 * 插入一条数据
	 * 
	 * @param value
	 * @return
	 */
	public long insert(T value) {
		L.d(TAG, "[insert]<" + getTableName() + ">:" + value.toString());
		try {
			synchronized (dBLock) {
				L.i(TAG, "data insert " + getTableName() + " succeed!");
				return dbHelper.open().insert(getTableName(), null,
						getContentValues(value));
			}
		} finally {
			dbHelper.close();
		}
	}

	/**
	 * 插入多条数据
	 * 
	 * @param values
	 * @return
	 */
	public boolean insert(List<T> values) {
		L.d(TAG, "[insert]<" + tag + ">:" + values.toString());
		synchronized (dBLock) {
			SQLiteDatabase db = dbHelper.open();
			db.beginTransaction();
			try {
				for (T t : values) {
					db.insert(getTableName(), null, getContentValues(t));
				}
				Log.i(TAG, "insert count: " + values.size());
				db.setTransactionSuccessful();
				return true;
			} catch (Exception e) {
				return false;
			} finally {
				db.endTransaction();
				dbHelper.close();
			}
		}
	}

	/**
	 * 修改一条数据
	 * 
	 * @param value
	 * @param id
	 * @return
	 */
	public boolean update(T value, long id) {
		L.d(TAG, "[update]<" + tag + ">:" + value.toString());
		try {
			synchronized (dBLock) {
				return dbHelper.open().update(getTableName(), getContentValues(value), getQueryKeyList()[0] + "=?",
						new String[] { String.valueOf(id) }) > 0;
			}
		} finally {
			dbHelper.close();
		}
	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 * @return
	 */
	public boolean delete(long id) {
		L.d(TAG, "[delete]<" + tag + ">:" + id);
		try {
			synchronized (dBLock) {
				return dbHelper.open().delete(getTableName(), getQueryKeyList()[0] + "=?",
						new String[] { String.valueOf(id) }) > 0;
			}
		} finally {
			dbHelper.close();
		}
	}

	/**
	 * 删除数据
	 * 
	 * @param ids
	 * @return
	 */
	public boolean delete(long[] ids) {
		L.d(TAG, "[delete]<" + tag + ">:" + ids);
		synchronized (dBLock) {
			SQLiteDatabase db = dbHelper.open();
			db.beginTransaction();
			try {
				for (int i = 0; i < ids.length; i++) {
					db.delete(getTableName(), getQueryKeyList()[0] + "=?", new String[] { String.valueOf(ids[i]) });
				}
				db.setTransactionSuccessful();
				return true;
			} catch (Exception e) {
				return false;
			} finally {
				db.endTransaction();
				dbHelper.close();
			}
		}
	}

	/**
	 * 清除表中所有数据
	 */
	public void deleteAll() {
		L.d(TAG, "deleteAll");
		SQLiteDatabase db = dbHelper.open();
		db.beginTransaction();
		db.delete(getTableName(), null, null);
		db.setTransactionSuccessful();
		db.endTransaction();
		dbHelper.close();
	}

	/**
	 * 根据ID获得数据
	 * 
	 * @param id
	 * @return
	 */
	public T get(long id) {
		L.d(TAG, "[get]<" + tag + ">:" + id);
		T reslut = null;
		Cursor cursor = null;
		try {
			cursor = find(getQueryKeyList(), getQueryKeyList()[0] + "=?",
					new String[] { String.valueOf(id) }, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				reslut = create(cursor);
			}
		} finally {
			if(null != cursor){
				cursor.close();
			}
		}
		return reslut;
	}

	/**
	 * 查找全部数据
	 * 
	 * @param orderBy
	 * @return
	 */
	public Cursor findAll(String orderBy) {
		L.d(TAG, "[findAll]<" + tag + "><order:" + orderBy + ">");
		return find(getQueryKeyList(), null, null, null, null, orderBy);

	}

	/**
	 * 查询全部数据
	 * 
	 * @param orderBy
	 * @return
	 */
	public List<T> findAllList(String orderBy) {
		List<T> result = null;
		Cursor cursor = null;
		try {
			cursor = findAll(orderBy);
			if(null != cursor){
				result = new ArrayList<T>();
				while (cursor.moveToNext()) {
					result.add(create(cursor));
				}
			}
		} finally {
			if(null != cursor){
				cursor.close();
			}
		}
		return result;
	}

	/**
	 * 自定义查询
	 * 
	 * @param selection
	 *            查询条件
	 * @param values
	 *            查询参数
	 * @param orderBy
	 *            排序
	 * @return
	 */
	public Cursor findBySelection(String selection, String[] values, String orderBy) {
		return find(getQueryKeyList(), selection, values, null, null, orderBy);
	}

	/**
	 * 更新对应表中某一行的某个字段值
	 * 
	 * @param values
	 * @param selectColumnName
	 * @param id
	 * @return
	 */
	public boolean updateMsgIsRead(ContentValues values, String selectColumnName, String id) {
		return dbHelper.open().update(getTableName(), values, selectColumnName + "=?", new String[] { String.valueOf(id) }) > 0;
	}

	/**
	 * 更新一行数据记录
	 * 
	 * @param updateColumn
	 *            需要更新的字段名、字段值
	 * @param condition
	 *            更新条件
	 */
	public void updataRow(String updateColumn, String condition) {
		dbHelper.open().execSQL("UPDATE " + getTableName() + " SET " + updateColumn + " WHERE " + condition);
	}

	/**
	 * 获得当前表的数据条数
	 * 
	 * @return
	 */
	public int getCount() {
		Cursor cursor = null;
		int count = -1;
		try {
			cursor = findAll(null);
			if(null != cursor){
				count = cursor.getCount();
			}
		} finally {
			if(null != cursor && !cursor.isClosed()){
				cursor.close();
			}
		}
		return count;
	}

	/**
	 * 查找数据
	 * 
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	protected Cursor find(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		L.d(TAG, "[find]<" + tag + ">:");
		synchronized (dBLock) {
			return dbHelper.open().query(getTableName(), columns, selection,
					selectionArgs, groupBy, having, orderBy);
		}
	}

	public void closeDataBase(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		dbHelper.close();
	}

	/**
	 * 根据对象生成ContentValues
	 * 
	 * @param value
	 * @return
	 */
	public abstract ContentValues getContentValues(T value);

	/**
	 * 根据Cursor生成对象
	 * 
	 * @param cursor
	 * @return
	 */
	public abstract T create(Cursor cursor);

	/**
	 * 获得查询数据字段列表<br/>
	 * ******<b>第一位必须是当前表的ID</b>
	 * 
	 * @return
	 */
	protected abstract String[] getQueryKeyList();

	/**
	 * 获得表名
	 * 
	 * @return
	 */
	protected abstract String getTableName();

}
