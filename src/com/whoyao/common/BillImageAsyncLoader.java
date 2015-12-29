package com.whoyao.common;

import java.io.File;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.utils.GraphicUtils;
import com.whoyao.utils.L;

import android.content.res.Resources;
import android.graphics.Bitmap;

/**
 * 根据屏幕大小加载图片(宽高最大的一个跟屏幕相同)
 * @author HYH
 * create at：2013-4-22 下午02:09:40
 */
public class BillImageAsyncLoader extends ImageAsyncLoader {
	private static final BillImageAsyncLoader loader = new BillImageAsyncLoader();
	private int height;
	private int width;
	
	private BillImageAsyncLoader() {
		super();
		this.setDefaultImage(R.drawable.default_bill);
		Resources res = AppContext.getRes();
		width = res.getDimensionPixelSize(R.dimen.image_bill_width);
		height = res.getDimensionPixelSize(R.dimen.image_height);
	}


	@Override
	protected Bitmap readImage(File imageFile) {
		Bitmap bitmap = null;
		for(int i = 0 ;i<10;i++){
			bitmap = GraphicUtils.compressImageSize(imageFile.getPath(), width, height);
			if(bitmap != null){
				return GraphicUtils.toRoundCorner(bitmap, 6);
			}
		}
		imageFile.deleteOnExit();
		return bitmap;
	}
	
//	@Override
//	protected void setImageBitmap(ImageView iv, Bitmap bitmap) {
//		iv.setBackgroundDrawable(new BitmapDrawable(bitmap));
//	}
	
	public static BillImageAsyncLoader getInstance() {
		return loader;
	}
}
