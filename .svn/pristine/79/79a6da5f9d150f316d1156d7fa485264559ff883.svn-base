package com.whoyao.db;

import com.whoyao.Const;
import com.whoyao.utils.L;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDBHelper extends SQLiteOpenHelper {
	public static String CHAT_TABLE = "chat";
	public static String CHAT_ID = "_id";
	public static String CHAT_USER = "chatuser";
	public static String CHAT_HOST = "host";
	public static String CHAT_FROM = "chatfrom";
	public static String CHAT_CONTENT = "content";
	public static String CHAT_TIME = "chattime";
	public static String CHAT_HEAD = "chathead";
	public static String CHAT_MESSAGE_ID = "messageid";

	public ChatDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table chat(_id integer primary key autoincrement,content varchar,chattime varchar,chatuser integer,chatfrom integer,chathead varchar,messageid integer,host integer)");
//		db.execSQL("create table relation(_id integer primary key autoincrement,userid integer,chathead text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
