package com.whoyao.db;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.utils.L;

/**
 * @author WCT
 * create at：2012-11-20 下午02:14:57
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = Const.DB_CACHE;
	private static final int DATABASE_VERSION = 1;
	private static final String CREATE_USERFACE_TABLE = "CREATE TABLE UserFaceTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, username text,userfacename text, userfacebyte blob not null)";
	private static final String CREATE_PRIVATELETTERLIST_TABLE = "CREATE TABLE PrivateLetterListTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, room_id text unique, current_user text, contacter_user text, contacter_userfaceurl text, contacter_nickname text, lastchat_content text, lastchat_time text, lastchat_from int,user_id text)";
	private static final String CREATE_PRIVATELETTERRECORD_TABLE = "CREATE TABLE PrivateLetterRecordTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, room_id text, chat_content text, chat_time text, chat_from int)";
	private static final String CREATE_INFORM_TABLE = "CREATE TABLE InformTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, current_ofusername text, message_type int, is_friend_request int, processed_status int, message_eventid text, message_eventstatus int, event_userstatus int, message_title text, message_content text, message_from text, message_to text, message_createtime text, message_state int, friend_imgurl text, friend_nickname text, is_user int, user_id text)";
	private static final String CREATE_INVITE_TABLE = "CREATE TABLE InviteTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, current_ofusername text, message_type int, is_friend_request int, message_eventid text, message_eventstatus int, event_userstatus int, message_title text, message_content text, message_from text, message_to text, message_createtime text, message_state int, friend_imgurl text, friend_nickname text, is_user int, user_id text)";
	private static final String CREATE_GOODFRIEND_TABLE = "CREATE TABLE GoodFriendListTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, friend_imgurl text, friend_nickname text, friend_age text, friend_sex text, friend_sig text, friend_ofusername text, friend_userid text, current_user text)";
	private static final String DROP_PURCHASE_RECORDS = "DROP TABLE IF EXISTS purchchase_records;";
	
	public DBHelper() {
		super(AppContext.context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	

	public DBHelper(String name) {
		super(AppContext.context, name, null, DATABASE_VERSION);
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		L.i("database", "创建数据库..");
		db.execSQL(CREATE_USERFACE_TABLE);
		db.execSQL(CREATE_PRIVATELETTERLIST_TABLE);
		db.execSQL(CREATE_PRIVATELETTERRECORD_TABLE);
		db.execSQL(CREATE_INFORM_TABLE);
		db.execSQL(CREATE_INVITE_TABLE);
		db.execSQL(CREATE_GOODFRIEND_TABLE);
		db.setLockingEnabled(false);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_PURCHASE_RECORDS);
		onCreate(db);
	}

	/**
	 * 打开数据库
	 * 
	 * @return
	 * @throws SQLException
	 */
	public SQLiteDatabase open() throws SQLException {
		return getWritableDatabase();
	}
}
