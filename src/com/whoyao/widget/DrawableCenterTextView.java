package com.whoyao.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * drawableRight与文本一起居中显示
 * 
 * @author hyh 
 * creat_at：2014-2-20-下午6:56:45
 * @see 农民伯伯  http://www.cnblogs.com/over140/p/3464348.html
 * 
 */
public class DrawableCenterTextView extends TextView {

    private Drawable drawableRight;

	public DrawableCenterTextView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int gravity = getGravity();
        boolean isCenterHorizontal = gravity == Gravity.CENTER ||gravity == Gravity.CENTER_HORIZONTAL;
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null && isCenterHorizontal) {
        	float textWidth = getPaint().measureText(getText().toString());
        	int drawablePadding = getCompoundDrawablePadding();
        	float leftSpec = (getWidth() - textWidth) / 2;

            if(drawables[2] != null){
            	drawableRight = drawables[2];
            	drawables[2] = null;
            }
            super.onDraw(canvas);
            if (drawableRight != null) {
            	int drawableWidth = drawableRight.getIntrinsicWidth();
            	int left = (int) (leftSpec + textWidth + drawablePadding);
            	int right = left + drawableWidth;
            	int top = (getHeight() - drawableRight.getIntrinsicHeight())/2;
            	int bottom = top + drawableRight.getIntrinsicHeight();
            	drawableRight.setBounds( left, top, right, bottom);
            	drawableRight.draw(canvas);
            }
        }else{
        	super.onDraw(canvas);
        }
    }
}