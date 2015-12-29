package com.whoyao.activity;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.whoyao.AppContext;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * @author hyh creat_at：2013-7-3-下午12:31:17
 *         <p>
 *         BasicActivity
 */
public abstract class BasicActivity extends Activity {

	public static Set<WeakReference<Activity>> wActivitySet = new HashSet<WeakReference<Activity>>();
	public static ReferenceQueue<Activity> query = new ReferenceQueue<Activity>();
	public final String TAG = this.getClass().getSimpleName();
	protected Activity context;
	// 集合事件的监听
	public static ArrayList<BackPressHandler> mListeners = new ArrayList<BackPressHandler>();

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
	protected void onPause() {
		super.onPause();
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnPause();
			}
	}

	public BasicActivity() {
		super();
		context = this;
		if (null == AppContext.curActivity) {
			AppContext.refreshCurAct(this);
		}
		// 用于注销or退出时,关闭Activity
		wActivitySet.add(new WeakReference<Activity>(this, query));
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

	// @Override
	// protected void onResume() {
	// AppContext.refreshCurAct(this);
	// super.onResume();
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		AppContext.refreshCurAct(this);
		super.onActivityResult(requestCode, resultCode, data);
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
		while (null != wr) {
			wActivitySet.remove(wr);
			wr = query.poll();
		}

	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Nothing need to be done here
		} else {
			// Nothing need to be done here
		}
	}

	public void startActivity(Class<? extends Activity> cls,
			boolean finishCurrent) {
		Intent intent = new Intent(context, cls);
		startActivity(intent);
		if (finishCurrent) {
			finish();
		}
	}

	public void startActivity(Class<? extends Activity> cls) {
		startActivity(cls, false);
	}

	/** 用于X-Position */
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	/** back键按下时的反应 */
	protected boolean onBack() {
		return false;
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 返回时提醒
		if (keyCode == KeyEvent.KEYCODE_BACK && onBack()) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public static abstract interface BackPressHandler {

		public abstract void activityOnResume();

		public abstract void activityOnPause();

	}
}
