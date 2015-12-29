package com.whoyao.common;

import java.io.File;

import com.whoyao.R;
import com.whoyao.utils.GraphicUtils;
import android.graphics.Bitmap;

/**
 * 加载正方形图片(100X100)
 * @author HYH
 * create at：2013-4-22 下午02:09:40
 */
public class FaceImageAsyncLoader extends ImageAsyncLoader {
	private static final FaceImageAsyncLoader loader = new FaceImageAsyncLoader();
	
	private FaceImageAsyncLoader() {
		super();
		this.setDefaultImage(R.drawable.default_face);
	}


	@Override
	protected Bitmap readImage(File imageFile) {	
		
		Bitmap temp = null;
		for(int i = 0 ;i<10;i++){
			temp = GraphicUtils.compressImageSize(imageFile.getPath(),100,100);
			if(temp != null){
				break;
			}
		}
		Bitmap bitmap = null;
		if(temp != null){
			for(int i = 0 ;i<10;i++){
				bitmap = GraphicUtils.cutToSquare(temp);
				if(bitmap != null){
					return bitmap;
				}
			}
		}else{
			imageFile.deleteOnExit();
		}
		return bitmap;
	}
	
	public static FaceImageAsyncLoader getInstance() {
		return loader;
	}
}
