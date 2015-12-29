package com.whoyao.activity;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.activity.BasicActivity.BackPressHandler;
import com.whoyao.utils.L;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

/**
 * @author hyh 
 * creat_at：2013-9-11-下午4:51:11
 */
public class BasicFragmentActivity extends FragmentActivity {
	public static Set<WeakReference<Activity>> wActivitySet = BasicActivity.wActivitySet;
	public static ReferenceQueue<Activity> query = BasicActivity.query;
	public Activity context;
	public BasicFragmentActivity() {
		super();
		context = this;
		if(null == AppContext.curActivity){
			AppContext.refreshCurAct(this);
		}
		//用于注销or退出时,关闭Activity
		wActivitySet.add(new WeakReference<Activity>(this,query) );
	}
	// 集合事件的监听
	public static ArrayList<BackPressHandler> mListeners = new ArrayList<BackPressHandler>();


	@Override
	protected void onPause() {
		super.onPause();
		L.i(Const.ZL, "Size--" + mListeners.size());
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnPause();
			}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppContext.refreshCurAct(this);
	}
	@Override
	protected void onRestart() {
		AppContext.refreshCurAct(this);
		super.onRestart();
	}
	@Override
	protected void onResume() {
		super.onResume();
		AppContext.refreshCurAct(this);
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnResume();
			}
	}
	@Override
	protected void onDestroy() {
		AppContext.releaseCurAct(this);
		super.onDestroy();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		// 从wActivitySet中移除无效的WeakReference
		Reference<? extends Activity> wr = query.poll();
		while(null != wr ){
			wActivitySet.remove(wr);
			wr = query.poll();
		}

	}

	
	
//	@Override
//	public void finish() {
//		AppContext.releaseCurAct(this);
//		super.finish();
//	}

	/** 用于X-Position */
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
	/**back键按下时的反应*/
	protected boolean onBack(){
		return false;
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//  返回时提醒
		if (keyCode == KeyEvent.KEYCODE_BACK && onBack()) {
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
