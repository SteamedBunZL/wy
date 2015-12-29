package com.whoyao.ui;

import com.whoyao.AppContext;
import com.whoyao.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


/**
 * 新数据Toast提示控件(带音乐播放)
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-8-30
 */
public class Toast extends android.widget.Toast{
	
	@SuppressWarnings("unused")
	private MediaPlayer mPlayer;
	@SuppressWarnings("unused")
	private boolean isSound;
	
	public Toast(Context context) {
		this(context, false);
	}
	
	public Toast(Context context, boolean isSound) {
		super(context);
		
		this.isSound = isSound;
// TODO 初始化声音
//        mPlayer = MediaPlayer.create(context, R.raw.newdatatoast);
//        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
//			@Override
//			public void onCompletion(MediaPlayer mp) {
//				mp.release();
//			}        	
//        });

    }

	@Override
	public void show() {
		super.show();
		// TODO 这里还可以增加震动
		// 声音可以考虑换成SoundPool
//		if(isSound){
//			mPlayer.start();
//		}
	}
	
	/**
	 * 设置是否播放声音
	 */
//	public void setIsSound(boolean isSound) {
//		this.isSound = isSound;
//	}
	
	/**
	 * 获取控件实例
	 * @param context
	 * @param text 提示消息
	 * @param isSound 是否播放声音
	 * @return
	 */
	public static Toast makeText(Context context, CharSequence text, boolean isSound) {
		Toast result = new Toast(context, isSound);
		
        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        
        View v = inflate.inflate(R.layout.toast, null);
        v.setMinimumWidth(dm.widthPixels/3);//设置控件最小宽度为手机屏幕宽度
        
        TextView tv = (TextView)v.findViewById(R.id.toast_message);
        tv.setText(text);
        
        result.setView(v);
//        result.setDuration(600);
//        result.setGravity(Gravity.CENTER, 0, (int)(dm.density*75));
        result.setDuration(2000);
        result.setGravity(Gravity.CENTER, 0, 0);

        return result;
    }
	
	public static void show(CharSequence text){
		makeText(AppContext.context,text,false).show();
	}
	public static void show(int stringResID){
		makeText(AppContext.context,AppContext.getRes().getString(stringResID),false).show();
	}
}
