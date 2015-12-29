package com.whoyao.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.engine.client.ClientEngine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;

public class FileUtils {

	private static final String TAG = "FileUtils";
	@Deprecated
	public static File findImage(String id,String url){
		String name = HashUtils.mixHashStr(url);
		String subPaht = "";
		//LogUtil.i(TAG, "url= " + url + "name= " + name);
		if(!TextUtils.isEmpty(id)){
			subPaht = "/"+HashUtils.mixHashStr(url);
		}
		File dataPath = new File(AppContext.context.getFilesDir(),Const.PATH_IMAGE_TEMP+subPaht);
		File sdPath = new File(Const.SD_CACHE_DIR,Const.PATH_IMAGE_TEMP);
		
		File image;
		if(isSDCardExist()){
			image = new File(dataPath,name);
			if(image.exists()){
				return image;
			}else {
				image = new File(sdPath,name);
			}
		}else{
			image = new File(dataPath,name);
		}
		return image;
	}
	/**根据url获取文件存储路径*/
	public static File findImage(String url){
		//String name = url.substring(url.lastIndexOf("/")+1);
		String name = null;
		if(27 < url.length()){
			name = url.substring(27).replace("/", "_");
		}else{
			name = url.replace("/", "_");
		}
		//LogUtil.i(TAG, "url= " + url + "name= " + name);
		File dataPath = new File(Const.CACHE_DIR,Const.PATH_IMAGE_TEMP);
		File sdPath = new File(Const.SD_CACHE_DIR,Const.PATH_IMAGE_TEMP);
		File imagePath = null;
		if(isSDCardExist()){
			imagePath = sdPath;				
		}else{
			imagePath = dataPath;
		}
		if(!imagePath.exists()){
			imagePath.mkdirs();
		}
		return new File(imagePath, name);
	}
	
    /**
     * 递归删除文件和文件夹
     * @param file    要删除的根目录
     */
    public static void deleteFile(File file){
    	if(!file.exists()){
    		return;
    	}
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                deleteFile(f);
            }
            file.delete();
        }
    }

	/** 
     * 复制单个文件 
     * @param oldPath String 原文件路径 如：c:/fqf.txt 
     * @param newPath String 复制后路径 如：f:/fqf.txt 
     * @return boolean 
     */ 
   public static boolean copyFile(String oldPath, String newPath) { 
	   boolean isok = true;
       try { 
           int bytesum = 0; 
           int byteread = 0; 
           File oldfile = new File(oldPath); 
           if (oldfile.exists()) { //文件存在时 
               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
               FileOutputStream fs = new FileOutputStream(newPath); 
               byte[] buffer = new byte[1024]; 
               int length; 
               while ( (byteread = inStream.read(buffer)) != -1) { 
                   bytesum += byteread; //字节数 文件大小 
                   //LOG.i(Const.AppName,bytesum); 
                   fs.write(buffer, 0, byteread); 
               } 
               fs.flush(); 
               fs.close(); 
               inStream.close(); 
           }
           else
           {
			isok = false;
		   }
       } 
       catch (Exception e) { 
          // LOG.i(Const.AppName,"复制单个文件操作出错"); 
          // e.printStackTrace(); 
           isok = false;
       } 
       return isok;

   } 

   /** 
     * 复制整个文件夹内容 
     * @param oldPath String 原文件路径 如：c:/fqf 
     * @param newPath String 复制后路径 如：f:/fqf/ff 
     * @return boolean 
     */ 
   public static boolean copyFolder(String oldPath, String newPath) { 
	   boolean isok = true;
       try { 
           (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
           File a=new File(oldPath); 
           String[] file=a.list(); 
           File temp=null; 
           for (int i = 0; i < file.length; i++) { 
               if(oldPath.endsWith(File.separator)){ 
                   temp=new File(oldPath+file[i]); 
               } 
               else
               { 
                   temp=new File(oldPath+File.separator+file[i]); 
               } 

               if(temp.isFile()){ 
                   FileInputStream input = new FileInputStream(temp); 
                   FileOutputStream output = new FileOutputStream(newPath + "/" + 
                           (temp.getName()).toString()); 
                   byte[] b = new byte[1024 * 5]; 
                   int len; 
                   while ( (len = input.read(b)) != -1) { 
                       output.write(b, 0, len); 
                   } 
                   output.flush(); 
                   output.close(); 
                   input.close(); 
               } 
               if(temp.isDirectory()){//如果是子文件夹 
                   copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
               } 
           } 
       } 
       catch (Exception e) { 
    	    isok = false;
       } 
       return isok;
   }

	
//	public static File findImage(String url){
//		String name = url.substring(url.lastIndexOf("/")+1);
//		String subPath = url.substring(27, url.lastIndexOf("/")).replace("/", "_");
//		//LogUtil.i(TAG, "url= " + url + "name= " + name);
//		File dataPath = new File(AppContext.context.getFilesDir(),Const.IMAGE_TEMP_PATH);
//		File sdPath = new File(Const.FILE_LOCAL,Const.IMAGE_TEMP_PATH);
//		File imagePath = null;
//		File image;
//		if(isSDCardExist()){
//			imagePath = new File(sdPath,subPath);				
//		}else{
//			imagePath = new File(dataPath,subPath);
//		}
//		if(!imagePath.exists()){
//			imagePath.mkdirs();
//		}
//		return new File(imagePath, name);
//	}
	
	/**
	 * 获取原图Get the Get the original image
	 * @param image
	 * @return
	 */
   @Deprecated
	public static Bitmap getOriginalImage(File image){
		// TODO 将图片压缩到屏幕大小
		int pxSize = ClientEngine.getMobileInfo().getVgaHeight();

		Bitmap bitmap = compressImageSize(image.getPath(),pxSize);
		//return compressImageQuality(bitmap,capacity);
		return bitmap;
	}
	
	/**
	 * 对图片进行压缩,获取小图(60dp*60dp)
	 * @param image 原图File
	 * @return
	 */
	@Deprecated
	public static Bitmap getSmallImage(File image){
//		int dipSize = 60;
//		int pxSize = Utils.dip2px(AppContext.context, dipSize);
		int pxSize = (int) AppContext.getRes().getDimension(R.dimen.image_height);
//		Bitmap bitmap = compressImageSize(image.getPath(),pxSize);
		Bitmap bitmap = compressImageSize(image.getPath(),pxSize);
//		Bitmap bitmap = BitmapFactory.decodeFile(image.getPath()); 
		bitmap = cutToSquare(bitmap);
//		bitmap = compressImageQuality(bitmap,capacity);
		return toRoundCorner(bitmap,6);
	}


	
	
	
	
	
	/*-----------------------------------------------------------------*/
	
	
	/**
	 * 判断SD卡是否存在
	 * @return
	 */
	public static boolean isSDCardExist(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 创建文件夹
	 * 
	 * @param context
	 */
/*	@Deprecated
	private static void mkdir(Context context) {
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(Const.SD_CAMERA_DIR,Const.PATH_IMAGE_TEMP);
			if (!file.exists()) {
				file.mkdir();
			}
		}
	}*/


	/**
	 * 读取图片工具
	 * 
	 * @param path
	 * @param name
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	private static byte[] readData(String path, String name) throws IOException {
		// String name = MyHash.mixHashStr(url);
		ByteArrayBuffer buffer = null;
		String paths = path + name;
		File file = new File(paths);
		if (!file.exists()) {
			return null;
		}
		InputStream inputstream = new FileInputStream(file);
		buffer = new ByteArrayBuffer(1024);
		byte[] tmp = new byte[1024];
		int len;
		while (((len = inputstream.read(tmp)) != -1)) {
			buffer.append(tmp, 0, len);
		}
		inputstream.close();
		return buffer.toByteArray();
	}


	

	/**
	 * 将图片压缩到指定的尺寸
	 * 内存不足返回null
	 * @param image 图片路径
	 * @param dipSize 图片尺寸(dip)
	 * @return
	 */
	public static Bitmap compressImageSize(int dipSize,String image) {
		//计算缩放比   
        int pxSize = Utils.dip2px(AppContext.context, dipSize);
		return compressImageSize(image, pxSize);
	}
	
	/**
	 * 将图片压缩到指定的尺寸(宽高中交小的哪一个)
	 * @param image 图片路径
	 * @param pxSize 图片尺寸(px)
	 * @return
	 */
	@Deprecated
	public static Bitmap compressImageSize(String image,int pxSize) {
		Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();  
        //获取图像信息
        options.inJustDecodeBounds = true;   
        BitmapFactory.decodeFile(image,options);//获取这个图片的宽和高,保存在options中,返回null
        int h = options.outHeight;
        int w = options.outWidth;
        //获取足够内存空间,如果失败则返回Null
        long estimateMemory = estimateMemory(w, h);
        if(!checkMemory(estimateMemory)){
        	// TODO 是否需要Toast提醒内存不足？
        	return null;
        }
        //有足够内存，开始读取图片
        int out = (h<w)?h:w;
        int scaling = (int)(out/pxSize);   
        if(scaling < 1)   
             scaling = 1;   
        options.inSampleSize =scaling;  
		
        //读入图片并压缩，注意这次要把options.inJustDecodeBounds设为false   
        options.inJustDecodeBounds =false;   
        bitmap = BitmapFactory.decodeFile(image,options);   
        if(null == bitmap){
        	//LogUtil.i(TAG, "nullbit image" + image);
        }
		return bitmap;
	}
	/**
	 * 从给的的Bitmap中心位置裁剪正方形
	 * @param image
	 * @param pxSize
	 * @return
	 */
	@Deprecated
	public static Bitmap cutToSquare(Bitmap bitmap){
		
		if(null == bitmap){
			return null;
		}
		int x,y,length;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		
		if(w == h){
			return bitmap;
		}
		
		length = (w<h)?w:h;
		x = (w-length)/2;
		y = (h-length)/2;
		return Bitmap.createBitmap(bitmap, x, y, length, length);
	}
	/**
	 * 对图片质量进行,压缩到小于指定的容量
	 * <br>压缩输出的PNG/JPG格式文件大小，
	 * 
	 * <p><b><font color="#FF0000" size="3">Wring:</font></b>
	 * 不会缩小Bitmap所需内存
	 * @param image	要压缩的图片
	 * @param capacity 压缩后的最大容量,单位是K
	 * @return
	 */
	@Deprecated
	public static Bitmap compressImageQuality(Bitmap image,int capacity) {
		if(null == image){
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, capacity, baos);			//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length > capacity*1024) {					//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();												//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, options, baos);	//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次质量降低10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());	//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);				//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	/**
	 * 图片本身加上圆角
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	@Deprecated
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {  
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);  
	        Canvas canvas = new Canvas(output);  
	         int color = 0xff424242;  
	         Paint paint = new Paint();  
	         Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
	         RectF rectF = new RectF(rect);  
	         float roundPx = pixels;  
	        paint.setAntiAlias(true);  //防锯齿
	        canvas.drawARGB(0, 0, 0, 0);  
	        paint.setColor(color);  
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
	        canvas.drawBitmap(bitmap, rect, rect, paint);
	        return output;  
	    } 

	/**
	 * 估算bitmap所需消耗的内存
	 * 
	 * @param width
	 * @param height
	 * @return 所需内存(byte)
	 */
	@Deprecated
	public static long estimateMemory(int width,int height){
		return width*height*3L + 54;
	}
	/**
	 * 检查内存释放有足够空间，如果不够则尝试释放
	 * @param size
	 * @return
	 */
	public static boolean checkMemory(long size){
		Runtime runtime = Runtime.getRuntime();;

		long freeMemory = runtime.freeMemory();//空闲内存
		long maxMemory = runtime.maxMemory();//总可以内存
		//long toatlMemory = Runtime.getRuntime().totalMemory();//已用内存
		if(size*2 < freeMemory){
			return true;
		}else{
			System.gc();
		}
		SystemClock.sleep(500);
		if(size*2 < freeMemory){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 下载指定URL地址的文件,保存到指定位置
	 */
	public static void downloadFile(String urlStr,File file) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(urlStr);
			URLConnection openConnection = url.openConnection();// 打开连接
			is = openConnection.getInputStream();// 获取流
			
			File filePath = file.getParentFile();
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			File temp = new File(file.getPath() + ".tmp");
			fos = new FileOutputStream(temp);
			
			byte[] buffer = new byte[10240];
			int len = 0;
			while((len = is.read(buffer))!=-1){
				fos.write(buffer, 0, len);
			}
			fos.flush();
			temp.renameTo(file);
			return ;
		} catch (MalformedURLException e) {
			file = null;
			e.printStackTrace();
		} catch (IOException e) {
			file = null;
			e.printStackTrace();
		}finally{
			try {
				if(null != fos){
					fos.close();
				}
				if(null != is){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}

	
	
	/**
	 * 从文件复制文件
	 * @param is 源文件的输入流
	 * @param path 拷贝的文件的路径
	 * @return 返回空代表拷贝失败
	 */
//	public static File copyFile(File source, File destination){
//		try {
//			if(!destination.getParentFile().exists()){
//				destination.getParentFile().mkdirs();
//			}
//			FileInputStream ins = new FileInputStream(source);
//			FileOutputStream fos = new FileOutputStream(destination);
//			
//			byte[] buffer = new byte[10240];
//			int len = 0;
//			
//			while((len = ins.read(buffer))!=-1){
//				fos.write(buffer, 0, len);
//			}
//			fos.flush();
//			fos.close();
//			ins.close();
//			return destination;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	
	
	
	
	

	/**
	 * 从流复制文件
	 * @param is 源文件的输入流
	 * @param path 拷贝的文件的路径
	 * @return 返回空代表拷贝失败
	 */
	public static File copyFile(InputStream is, File destination){
		try {
			
			if(!destination.getParentFile().exists()){
				destination.getParentFile().mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(destination);
			
			byte[] buffer = new byte[10240];
			int len = 0;
			
			while((len = is.read(buffer))!=-1){
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
			is.close();
			return destination;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
