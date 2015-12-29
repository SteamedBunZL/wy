package com.whoyao.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

/**
 * @author hyh 
 * creat_at：2014-2-17-上午10:45:02
 */
public class ImageResGetter implements ImageGetter {
	
	private Context context;

	public ImageResGetter(Context context) {
		this.context = context;
	}

	@Override
	public Drawable getDrawable(String source) {
		int id = Integer.parseInt(source);
		Drawable drawable = context.getResources().getDrawable(id);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		return drawable;
	}
}
