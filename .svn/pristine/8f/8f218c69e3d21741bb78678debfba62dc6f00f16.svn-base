package com.whoyao.common;

import java.io.File;

import com.whoyao.utils.GraphicUtils;

import android.graphics.Bitmap;

/**
 * 加载小图
 * @author HYH
 * create at：2013-4-22 下午02:09:40
 */
public class SmallImageAsyncLoader extends ImageAsyncLoader {
	private static final SmallImageAsyncLoader loader = new SmallImageAsyncLoader();
	
	private SmallImageAsyncLoader() {
		super();
	}
	
	@Override
	protected Bitmap readImage(File imageFile) {
		Bitmap bitmap = null;
		for(int i = 0 ;i<10;i++){
			bitmap = GraphicUtils.getSmallImage(imageFile);
			if(bitmap != null){
				return bitmap;
			}
		}
		imageFile.deleteOnExit();
		return bitmap;
	}
	
	public static SmallImageAsyncLoader getInstance() {
		return loader;
	}
}
