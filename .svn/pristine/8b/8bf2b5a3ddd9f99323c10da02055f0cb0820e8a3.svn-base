package com.whoyao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author hyh 
 * creat_at：2013-11-6-下午4:12:33
 */
public class CustomRelativeLayout extends RelativeLayout{
    
    private KeyboardChangeListener listener;
    
    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////        Log.i("lanyan", "onMeasure");
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
    
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
////        Log.i("lanyan", "onLayout");
//        super.onLayout(changed, l, t, r, b);
//    }
    
    /**
     * 当前活动主窗口大小改变时调用
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        Log.i("lanyan", "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        
        if (null != listener) {
            listener.onKeyboardChange(w, h, oldw, oldh);
        }
    }
    
    public void setOnKeyboardChangeListener(KeyboardChangeListener listener) {
        this.listener = listener;
    }
    
    /**
     * Activity主窗口大小改变时的回调接口(本示例中，等价于软键盘显示隐藏时的回调接口)
     * @author mo
     *
     */
    public interface KeyboardChangeListener {
        public void onKeyboardChange(int w, int h, int oldw, int oldh);
    }
    
}
