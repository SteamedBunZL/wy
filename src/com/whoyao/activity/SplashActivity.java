package com.whoyao.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.Const.Config;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.utils.FileUtils;
import com.whoyao.utils.Utils;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends BasicActivity {
	private ImageView splashImageView;
	private int loading = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.setFullScreen(this);
		setContentView(R.layout.activity_splash);
		splashImageView = (ImageView) findViewById(R.id.splash_iv);
		// splashImageView.setBackgroundResource(R.drawable.splash);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);

		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				loadCompleted();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

		splashImageView.startAnimation(animation);

		init();
	}

	private void init() {
		releaseDB();
		checkUpdate();
		moveImg();
	}

	private void checkUpdate() {
		Editor edit = AppContext.getConfigFile().edit();
		edit.putString(Config.UpdateInfo, "");
		edit.commit();
		ClientEngine.checkUpdate(false, new CallBack<Object>() {
			@Override
			public void onCallBack(boolean isSuccess, Object data) {
				loadCompleted();
			}
		});
	}

	private void releaseDB() {
		new Thread() { // 拷贝地址信息数据库
			public void run() {
				try {
					File dbFile = getDatabasePath(Const.DB_AREAE);
					if (!dbFile.exists() || dbFile.length() != Const.DB_SIZE_AREAE) {//
						InputStream is = getAssets().open(Const.DB_AREAE);
						File file = FileUtils.copyFile(is, dbFile);// 释放文件到当前应用程序的目录
						if (file == null || !file.exists()) {// 拷贝失败,程序直接退出
							android.os.Process.killProcess(android.os.Process.myPid());// 关闭程序
							return;
						}
					}
					dbFile = getDatabasePath(Const.DB_CITIES);
					if (!dbFile.exists() || dbFile.length() != Const.DB_SIZE_CITIES) {//
						InputStream is = getAssets().open(Const.DB_CITIES);
						File file = FileUtils.copyFile(is, dbFile);// 释放文件到当前应用程序的目录
						if (file == null || !file.exists()) {// 拷贝失败,程序直接退出
							android.os.Process.killProcess(android.os.Process.myPid());// 关闭程序
							return;
						}
					}
					
					loadCompleted();
				} catch (IOException e) {// 拷贝失败,程序直接退出.
					AppException.handle(e);
					android.os.Process.killProcess(android.os.Process.myPid());// 关闭程序
				}
			};

		}.start();
	}

	private void moveImg() {
		if (!FileUtils.isSDCardExist()) {
			loadCompleted();
			return;
		}
		new Thread() {
			public void run() {

				File dataImageTempPath = new File(Const.CACHE_DIR, Const.PATH_IMAGE_TEMP);
				if (dataImageTempPath.exists()) {
					File[] dataimgs = dataImageTempPath.listFiles();
					if (null != dataimgs && 0 < dataimgs.length) {
						File sdImageTempPath = new File(Const.SD_CACHE_DIR, Const.PATH_IMAGE_TEMP);
						FileUtils.copyFolder(dataImageTempPath.getAbsolutePath(), sdImageTempPath.getAbsolutePath());

						// File sdImage;
						// for (int i = 0; i < dataimgs.length; i++) {
						// sdImage = new File(sdImageTempPath,
						// dataimgs[i].getName());
						// if (!sdImage.exists()) {
						// FileUtils.copyFile(dataimgs[i], sdImage);
						// }
						// dataimgs[i].delete();
						// }
					}
				}
				// 删除Cache/Camera和Cache/Shot目录中的文件
				FileUtils.deleteFile(Const.CACHE_DIR);
				loadCompleted();
			}
		}.start();
	}

	private void loadCompleted() {
		--loading;
		if (0 == loading) {
			if (ClientEngine.isFirstTime()) {
				AppContext.startAct(GuideActivity.class, true);
			} else {
				UserEngine.login();
			}
		}
	}

	@Override
	public String toString() {
		return "启动页";
	}
}