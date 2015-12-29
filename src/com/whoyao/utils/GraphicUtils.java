package com.whoyao.utils;

import java.io.File;

import com.whoyao.AppContext;
import com.whoyao.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class GraphicUtils {
	
	/**
	 * 对图片进行压缩,获取小图(60dp*60dp)
	 * @param image 原图File
	 * @return
	 */
	public static Bitmap getSmallImage(File image){
		int pxSize = (int) AppContext.getRes().getDimension(R.dimen.image_height);
//		Bitmap bitmap = compressImageSize(image.getPath(),pxSize);
		Bitmap bitmap = compressImageSize(image.getPath(),pxSize,pxSize);
//		Bitmap bitmap = BitmapFactory.decodeFile(image.getPath()); 
		bitmap = cutToSquare(bitmap);
//		bitmap = compressImageQuality(bitmap,capacity);
		if(bitmap != null){
			bitmap = toRoundCorner(bitmap,6);
		}
		return bitmap;
	}
	
	
    /**
     * 创建圆边矩形
     * @param x
     * @param y
     * @param color
     * @return
     */
    public static Bitmap createRectF(int x, int y,int color) { 
        // 新建一个输出图片  
        Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888); 
        Canvas canvas = new Canvas(output); 
 
        // 新建一个矩形  
        RectF outerRect = new RectF(0, 0, x, y); 
 
        // 产生一个圆角矩形  
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); 
        paint.setColor(color);
        int r = x<=y?x:y;
        
        canvas.drawRoundRect(outerRect, r, r, paint); 
        return output; 
    }
    
	/**
	 * 图片本身加上圆角
	 * @param bitmap
	 * @param radiusPx
	 * @return
	 */

	public static Bitmap toRoundCorner(Bitmap bitmap, int radiusPx) {  
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);  
	        Canvas canvas = new Canvas(output);          
	        int radiusColor = 0xffffffff;  
	        Paint paint = new Paint();  
	        paint.setAntiAlias(true);  
	        paint.setColor(radiusColor);  
	        canvas.drawARGB(0, 0, 0, 0);  
	        
	        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
	        RectF rectF = new RectF(rect);  

	        canvas.drawRoundRect(rectF, radiusPx, radiusPx, paint);  
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
	        canvas.drawBitmap(bitmap, rect, rect, paint); 
			
	        return output;  
	    } 
	/**获取图像信息*/
    public static Options getBiemapOptions(String imagePath){
    	
    	BitmapFactory.Options options = new BitmapFactory.Options();  
    	//只获取图像信息,不将图片加载到内存
    	options.inJustDecodeBounds = true;   
    	BitmapFactory.decodeFile(imagePath,options);//获取这个图片的宽和高,保存在options中,返回null
    	return options;
    }
	
	/**
	 * 将图片压缩到指定的尺寸(宽高中交小的哪一个)
	 * @param image 图片路径
	 * @param pxSize 图片尺寸(px)
	 * @return
	 */
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
        if(!FileUtils.checkMemory(estimateMemory)){
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
	 * 将图片压缩到指定的尺寸(宽高中交小的哪一个)
	 * @param image 图片路径
	 * @param pxSize 图片尺寸(px)
	 * @return
	 */
	public static Bitmap compressImageSize(String image,int width,int height) {
        BitmapFactory.Options options = getBiemapOptions(image); 
        int scalingW = options.outWidth/width;
        int scalingH = options.outHeight/height;
        int scaling = scalingW>scalingH?scalingW:scalingH;
        if(1>scaling){
        	scaling = 1;
        }
        //获取足够内存空间,如果失败则返回Null
        long estimateMemory = estimateMemory(options.outWidth/scaling, options.outHeight/scaling);
        if(!FileUtils.checkMemory(estimateMemory)){
        	return null;// TODO 是否需要Toast提醒内存不足？
        }
        //有足够内存，开始读取图片
        if(scaling < 1)   
             scaling = 1;   
        options.inSampleSize =scaling;  
		
        //读入图片并压缩，注意这次要把options.inJustDecodeBounds设为false   
        options.inJustDecodeBounds =false;   
        Bitmap bitmap = BitmapFactory.decodeFile(image,options);   
        if(null == bitmap){
        	//LogUtil.i(TAG, "nullbit image" + image);
        }
		return bitmap;
	}
	
	/**
	 * 估算bitmap所需消耗的内存
	 * 
	 * @param width
	 * @param height
	 * @return 所需内存(byte)
	 */
	public static long estimateMemory(int width,int height){
		return width*height*3L + 54;
	}
	
	/**
	 * 从给的的Bitmap中心位置裁剪正方形
	 * @param image
	 * @param pxSize
	 * @return
	 */
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
}
