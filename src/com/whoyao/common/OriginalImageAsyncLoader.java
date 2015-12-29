package com.whoyao.common;

import java.io.File;

import com.whoyao.R;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.model.MobileInfoModel;
import com.whoyao.utils.GraphicUtils;
import com.whoyao.utils.L;

import android.graphics.Bitmap;

/**
 * 根据屏幕大小加载图片(宽高最大的一个跟屏幕相同)
 * @author HYH
 * create at：2013-4-22 下午02:09:40
 */
public class OriginalImageAsyncLoader extends ImageAsyncLoader {
	private static final OriginalImageAsyncLoader loader = new OriginalImageAsyncLoader();
	private int width;
	private int height;
	
	private OriginalImageAsyncLoader() {
		super();
//		setDefaultImage(R.drawable.default_image);
		MobileInfoModel info = ClientEngine.getMobileInfo();
		width = info.getVgaWidth();
		height = info.getVgaHeight();
		setImageDemen("/"+width+"x"+height+".jpg");
	}


	@Override
	protected Bitmap readImage(File imageFile) {
		Bitmap bitmap = null;
		for(int i = 0 ;i<10;i++){
			bitmap = GraphicUtils.compressImageSize(imageFile.getPath(), width, height);
			if(bitmap != null){
				return bitmap;
			}
		}
		imageFile.deleteOnExit();
		return bitmap;
	}
	
//	@Override
//	protected void setImageBitmap(ImageView iv, Bitmap bitmap) {
//		iv.setBackgroundDrawable(new BitmapDrawable(bitmap));
//	}
	
	public static OriginalImageAsyncLoader getInstance() {
		return loader;
	}
}
