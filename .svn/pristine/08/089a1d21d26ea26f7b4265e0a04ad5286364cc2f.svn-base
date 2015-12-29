package com.whoyao.utils;

import java.io.File;
import java.util.Date;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.whoyao.AppException;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.common.ImageResGetter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Utils {

	private static InputMethodManager imm;

	private static ImageGetter imageGetter = new ImageResGetter(AppContext.context);
	/**
	 * 全屏显示 需要在 setContentView 之前执行
	 * 
	 * @param activity
	 */
	public static void setFullScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置窗口全屏显示
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 取得屏幕信息
	}

	/**
	 * 去掉标题栏 需要在 setContentView 之前执行
	 * 
	 * @param activity
	 */
	public static void setHideTileBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	/**
	 * 隐藏软键盘
	 * @param activity
	 */
	public static void hideSoftKeyboard(Activity activity){
		if(imm == null){
			imm = (InputMethodManager)AppContext.context.getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		View currentFocus = activity.getCurrentFocus();
		if(null != currentFocus){
			imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	
	/**获取一个升序的int数组*/
	public static int[] getRiseArray(int begin, int end) {
		int length = end - begin + 1;
		int[] array = new int[length];
		for (int i = 0; i < length; i++) {
			array[i] = begin + i;
		}
		return array;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void openURL(Context context,String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
		context.startActivity(intent);
	}
	
	/**
	 * 下载指定URL地址的图片
	 */
	/*
	 * public static Bitmap downloadImg(URL url) { try { URLConnection
	 * openConnection = url.openConnection();// 打开连接 InputStream is =
	 * openConnection.getInputStream();// 获取流
	 * 
	 * // 通过工厂方法，获取下载图片的方法decodeStream Bitmap bitmap =
	 * BitmapFactory.decodeStream(is); return bitmap; } catch
	 * (MalformedURLException e) { AppException.handle(e); return null; } catch
	 * (IOException e) { AppException.handle(e); return null; } }
	 */
	
	public static void addSpinnerText(TextView v){
		v.append(Html.fromHtml("<img src='" + R.drawable.selector_triangle + "'/>", imageGetter, null));
	}
	
	/**
	 * 防止连续点击
	 */
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = AppContext.serviceTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * @author HYH 上传头像,返回名称
	 * @param headImg
	 *            头像的Bitmap
	 * @return
	 */
	/*
	 * @SuppressWarnings("finally") public static String uploadHeadImg(Bitmap
	 * headImg) { String backStr = null; HttpURLConnection conn = null; try {
	 * URL url = null; url = new URL(InterfaceAddress.SaveImageUserFaceURL);
	 * conn = (HttpURLConnection) url.openConnection();// 打开连接
	 * conn.setDoInput(true); conn.setDoOutput(true);
	 * 
	 * ByteArrayOutputStream baos = new ByteArrayOutputStream();
	 * headImg.compress(Bitmap.CompressFormat.PNG, 100, baos); byte[] buffer =
	 * baos.toByteArray(); OutputStream out = conn.getOutputStream();
	 * out.write(buffer); // 获得返回值 InputStream is; if (200 ==
	 * conn.getResponseCode()) { is = conn.getInputStream(); byte[] data = new
	 * byte[10240]; int length = is.read(data); backStr = new String(data, 0,
	 * length); } } catch (MalformedURLException e) { e.printStackTrace(); }
	 * catch (IOException e) { e.printStackTrace(); } finally {
	 * conn.disconnect(); return backStr; } }
	 */
	/**
	 * @author HYH 上传照片,返回名称
	 * @param photo
	 *            照片文件
	 * @return
	 */
	/*
	 * @SuppressWarnings("finally") // public static String uploadPhoto(File
	 * photo) { // URL url = null; // HttpURLConnection conn = null; // String
	 * backStr = null; // try { // url = new URL(InterfaceAddress.SavePhotoURL);
	 * // conn = (HttpURLConnection) url.openConnection();// 打开连接 //
	 * conn.setDoInput(true); // conn.setDoOutput(true); // //
	 * BufferedInputStream in = new BufferedInputStream(new
	 * FileInputStream(photo)); // BufferedOutputStream out = new
	 * BufferedOutputStream(conn.getOutputStream()); // byte[] buffer = new
	 * byte[10240]; // int length = -1; // while ((length = in.read(buffer)) !=
	 * -1) { // out.write(buffer); // } // out.flush(); // out.close(); // out =
	 * null; // in.close(); // in = null; // // 获得返回值 // InputStream
	 * responseInS; // if (200 == conn.getResponseCode()) { // responseInS =
	 * conn.getInputStream(); // byte[] data = new byte[10240]; // int
	 * ins_length = responseInS.read(data); // backStr = new String(data, 0,
	 * ins_length); // } // } catch (MalformedURLException e) { //
	 * e.printStackTrace(); // } catch (IOException e) { // e.printStackTrace();
	 * // } finally { // conn.disconnect(); // return backStr; // } // }
	 */
	/**
	 * 获取应用最后修改时间
	 * 
	 * @param context
	 * @return
	 */
	public static String getApkLastModifiedTime(Context context) {
		return new Date(new File(context.getApplicationInfo().sourceDir).lastModified()).toLocaleString();
	}
	/**
	 * 向Drawable中添加一个state,如果已经存在则不重复添加
	 * @param dr
	 * @param state
	 */
	public static void addState(Drawable dr, int state){
		int[] oldState = dr.getState();
        final int length = oldState.length;
        for (int i = 0; i < length; i++) {
            if (oldState[i] == state) {
            	return;
            }
        }
        final int[] currentState = new int[length + 1];
        System.arraycopy(oldState, 0, currentState, 0, length);
        currentState[length] = state;
        dr.setState(currentState);
	}
	/**
	 * 从Drawable中移除一个state,如果不存在则不重复操作
	 * @param dr
	 * @param state
	 */
	public static void removeState(Drawable dr, int state){
		int[] oldState = dr.getState();
        final int length = oldState.length;
        for (int i = 0; i < length; i++) {
            if (oldState[i] == state) {
                final int[] currentState = new int[length - 1];
                System.arraycopy(oldState, 0, currentState, 0, i);
                System.arraycopy(oldState, i + 1, currentState, i, length - i - 1);
                dr.setState(currentState);
                return;
            }
        }
	}
	
	/**
	 * 获取应用版本号名称
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static String getApkVersionName(Context context) {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			AppException.handle(e);
		}
		return versionName;
	}

	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static int getApkVersionCode(Context context) {
		int versionCode = -1;
		try {
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			AppException.handle(e);
		}
		return versionCode;
	}

		/**变量每个子View的高度*/
	   public static void setListViewHeightForeachChildren(ListView listView) {  
	       ListAdapter listAdapter = listView.getAdapter();   
	       if (listAdapter == null) {  
	           return;  
	       }  
	 
	       int totalHeight = 0;  
	       if(listAdapter.getCount() != 0){
	    	   for (int i = 0; i < listAdapter.getCount(); i++) {  
	    		   View listItem = listAdapter.getView(i, null, listView);  
	    		   if(listItem instanceof ViewGroup){
	    			   Utils.measureView(listItem);
	    		   }else{
	    			   listItem.measure(0, 0);
	    		   }
	    		   totalHeight += listItem.getMeasuredHeight();  
	    	   }  
	       }else{
	    	   View emptyView = listView.getEmptyView();
	    	   if(null != emptyView){
	    		   if(emptyView instanceof ViewGroup){
	    			   Utils.measureView(emptyView);
	    		   }else{
	    			   emptyView.measure(0, 0);
	    		   }
	    		   totalHeight = emptyView.getMeasuredHeight();
	    	   }
	       }
	 
	       ViewGroup.LayoutParams params = listView.getLayoutParams();  
	       params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
//	       params.height += 5;//if without this statement,the listview will be a little short  
	       listView.setLayoutParams(params);  
	   }
	  /** 根据第一个View的高度  <font color=red>未完成</font>*/
	   public static void setListViewHeightBasedOneChildren(ListView listView) {  
		   ListAdapter listAdapter = listView.getAdapter();   
		   if (listAdapter == null) {  
			   return;  
		   }  
		   
		   int totalHeight = 0;  
		   for (int i = 0; i < listAdapter.getCount(); i++) {  
			   View listItem = listAdapter.getView(i, null, listView);  
			   if(listItem instanceof ViewGroup){
	        	   Utils.measureView(listItem);
	           }else{
	        	   listItem.measure(0, 0);
	           } 
			   totalHeight += listItem.getMeasuredHeight();  
		   }  
		   
		   ViewGroup.LayoutParams params = listView.getLayoutParams();  
		   params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
		   params.height += 5;//if without this statement,the listview will be a little short  
		   listView.setLayoutParams(params);  
	   }  
	
	/**
	 * 判断字符串的长度,每个汉字为1,每个字母为0.5
	 * 
	 * @param string
	 * @return
	 */
	public static int getStringLength(String str) {
		return str.length();
	}

	public static String getSubString(String string, int length){
		double num = 0;
		String len;
		char[] c = string.toCharArray();
		for (int i = 0; i < c.length; i++) {
			len = Integer.toBinaryString(c[i]);
			if (len.length() > 8) {
				num++;
			} else {
				num += 0.5;
			}
			if(num == length){
				return string.substring(0, i+1);
			}
			if(num > length){
				return string.substring(0, i);
			}
		}
		
		return string;
	}
	
	/**
	 * BYZ 及时屏幕尺寸(PX)
	 * 
	 * @param unit
	 *            参数的单位,请使用TypedValue中的常量
	 * @param size
	 *            非PX的其他单位
	 * @return
	 */
	public static float getRawSize(int unit, float size) {
		Context c = AppContext.context;
		Resources r;
		if (c == null)
			r = Resources.getSystem();
		else
			r = c.getResources();
		return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}

	/**
	 * 测量View的宽高
	 * 
	 * @param child
	 *            调用被测量View的getMeasuredHeight() or getMeasuredWidth()
	 */
	public static void measureView(View v) {
		ViewGroup.LayoutParams lp = v.getLayoutParams();

		if (lp == null) {
			lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
		int childMeasureheight;

		if (lp.height > 0) {
			childMeasureheight = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
		} else {
			childMeasureheight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		v.measure(childMeasureWidth, childMeasureheight);
	}
	/**维度,经度*/
	public static GeoPoint getGeoPoint(double latitude,double longitude){
		int Latitude = (int)(latitude*1e6);
		int Longitude = (int)(longitude*1e6);
		return new GeoPoint(Latitude, Longitude);
	}
}
