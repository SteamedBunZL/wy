package com.whoyao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import com.whoyao.utils.L;

import android.annotation.SuppressLint;
import android.os.Build;

/**
 * @author hyh
 * @CreatTime 2013-7-1 下午1:04:16
 *            <p>
 *            异常统一处理
 */
public class AppException implements UncaughtExceptionHandler {
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
	
	/**
	 * 捕获异常统一处理
	 * 
	 * @param e
	 */
	public static void handle(Exception e) {
		switch (AppContext.AppState) {
		case Const.STATUS_DEBUG:
			L.e("System.err", "---------------Caught Error---------------");
			e.printStackTrace();
			storeCaughtLog(e);
			break;
		case Const.STATUS_RELEASE:
			//e.printStackTrace();
			break;
		default:
			break;
		}
	}

	/**
	 * 未捕获异常统一处理
	 * 
	 * @param t
	 * @param e
	 */
	public void uncaughtException(Thread t, Throwable e) {
		//对未捕获异常进行处理
		switch (AppContext.AppState) {
		case Const.STATUS_DEBUG:
			L.e("System.err", "---------------Uncaught Error---------------");
			e.printStackTrace();
			storeUncaughtLog(t,e);
			break;
		case Const.STATUS_RELEASE:
			//e.printStackTrace();
			break;
		default:
			break;
		}
		// 关闭程序.
//		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
//		UserEngine.login();
	}
	
	/**
	 * 存储未捕获异常
	 * @param t
	 * @param e
	 */
	private static void storeUncaughtLog(Thread t, Throwable e){
		try {
			File file = null;
			L.e(Const.SD_ErrorLog_DIR.getAbsolutePath());
			L.e(Const.ErrorLog_DIR.getAbsolutePath());
			if(Const.STATUS_DEBUG == AppContext.AppState){
				file = new File(Const.SD_ErrorLog_DIR,"UncaughtException.log");
			}else{
				file = new File(Const.ErrorLog_DIR,"UncaughtException.log");
			}
			FileOutputStream fos = new FileOutputStream(file, file.exists());//如果文件存在,启用追加模式
			//未捕获异常,头部添加设备信息
			if(!file.exists()){
				Field[] fileds = Build.class.getDeclaredFields();
				for (Field f : fileds) {
					f.setAccessible(true);// 暴力反射 获取私有字段.
					String result = f.getName() + ":" + f.get(null);
					L.i(Const.AppName,result);
					fos.write(result.getBytes());
					fos.write("\n".getBytes());
				}
			}
			//未捕获异常分割线
			String time = formatter.format(AppContext.serviceTimeMillis());
			fos.write("--------------------".getBytes());
			fos.write(("Uncaught Exception At:" + time).getBytes());
			fos.write("--------------------".getBytes());
			fos.write("\n".getBytes());
			//线程
			String threadName = "unknown";
			if(null != t){
				threadName = t.getName();
			}
			fos.write(("Thread Name: " + threadName).getBytes());
			fos.write("\n".getBytes());
			//异常内容
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			fos.write(sw.toString().getBytes());
			fos.close();
		
		} catch (Exception e1) {
			//e1.printStackTrace(); // Do nothing
		}
	}
	/**
	 * 存储未捕获异常
	 * @param t
	 * @param e
	 */
	private static void storeCaughtLog(Throwable e){
		try {
			File file = null;
			if(Const.STATUS_DEBUG == AppContext.AppState){
				file = new File(Const.SD_ErrorLog_DIR,"CaughtException.log");
			}else{
				file = new File(Const.ErrorLog_DIR,"CaughtException.log");
			}
			FileOutputStream fos = new FileOutputStream(file, file.exists());//如果文件存在,启用追加模式
			//未捕获异常,头部添加设备信息
			if(!file.exists()){
				Field[] fileds = Build.class.getDeclaredFields();
				for (Field f : fileds) {
					f.setAccessible(true);// 暴力反射 获取私有字段.
					String result = f.getName() + ":" + f.get(null);
					L.i(Const.AppName,result);
					fos.write(result.getBytes());
					fos.write("\n".getBytes());
				}
				
			}
			//未捕获异常分割线
			
			String time = formatter.format(AppContext.serviceTimeMillis());
			fos.write("--------------------".getBytes());
			fos.write(("Caught Exception At:" + time).getBytes());
			fos.write("--------------------".getBytes());
			fos.write("\n".getBytes());
			//异常内容
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			fos.write(sw.toString().getBytes());
			fos.close();
			
		} catch (Exception e1) {
			//e1.printStackTrace(); // Do nothing
		}
	}
}
