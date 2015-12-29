package com.whoyao.db;

import com.whoyao.Const;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author WCT
 * create at：2012-11-20 下午02:14:57
 */
public class AreaSQLHelper extends DBHelper {

	public AreaSQLHelper() {
		super(Const.DB_AREAE);
	}
	
	@Override
	public SQLiteDatabase open() throws SQLException {
		return getReadableDatabase();
	}
}
