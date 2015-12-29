package com.whoyao.engine.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.Const.Config;
import com.whoyao.activity.GuideActivity;
import com.whoyao.activity.SplashActivity;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.UpdateModel;
import com.whoyao.ui.Toast;
import com.whoyao.utils.FileUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 应用程序更新工具包
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.1
 * @created 2012-6-29
 */
public class UpdateManager {

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private static final int DIALOG_TYPE_LATEST = 0;
	private static final int DIALOG_TYPE_FAIL = 1;
	private static UpdateManager updateManager;
	private boolean isMustUpdate;
	// private Context mContext = Application.curActivity;
	// 通知对话框
	private Dialog noticeDialog;
	// 下载对话框
	private Dialog downloadDialog;
	// '已经是最新' 或者 '无法获取最新版本' 的对话框
	private Dialog latestOrFailDialog;
	// 进度条
	private ProgressBar mProgress;
	// 显示下载数值
	private TextView mProgressText;
	// 进度值
	private int progress;
	// 下载线程
	private Thread downLoadThread;
	// 终止标记
	private boolean interceptFlag;
	// 下载包保存路径
	private File savePath = null;
	// apk保存完整路径
	private String apkFilePath = "";
	// 临时下载文件路径
	private String tmpFilePath = "";
	// 下载文件大小
	private String apkFileSize;
	// 已下载文件大小
	private String tmpFileSize;

	private UpdateModel mUpdate;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk((File) msg.obj);
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				// Toast.makeText(Application.context, "无法下载安装文件，请检查SD卡是否挂载",
				// 3000).show();
				Toast.show(R.string.warn_no_sdcard);
				break;
			}
		};
	};

	public UpdateManager(UpdateModel mUModel) {
		super();
		this.mUpdate = mUModel;
	}

	public static UpdateManager getUpdateManager(UpdateModel mUModel) {
		if (updateManager == null) {
			updateManager = new UpdateManager(mUModel);
		} else if (null != mUModel) {
			updateManager.mUpdate = mUModel;
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}

	/**
	 * 显示'已经是最新'或者'无法获取版本信息'对话框
	 */
	public void showLatestOrFailDialog(int dialogType) {
		if (latestOrFailDialog != null) {
			// 关闭并释放之前的对话框
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}
		AlertDialog.Builder builder = new Builder(AppContext.curActivity);
		builder.setTitle("系统提示");
		if (dialogType == DIALOG_TYPE_LATEST) {
			builder.setMessage("您当前已经是最新版本");
		} else if (dialogType == DIALOG_TYPE_FAIL) {
			builder.setMessage("无法获取版本更新信息");
		}
		builder.setPositiveButton("确定", null);
		latestOrFailDialog = builder.create();
		latestOrFailDialog.show();
	}


	/**
	 * 显示版本更新通知对话框
	 */
	public void showNoticeDialog() {
		int currentVersion = ClientEngine.getMobileInfo().getClientVersion();
		if (currentVersion < mUpdate.getMinversion()) {
			// 强制更新
			isMustUpdate = true;
		}

		AlertDialog.Builder builder = new Builder(AppContext.curActivity);
		builder.setTitle("互邀V"+mUpdate.getLastversionname()+"版本发布啦！");
		builder.setMessage(mUpdate.getDescription());
		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cancleUpdate(true);
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				cancleUpdate(true);
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示下载对话框
	 */
	public void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(AppContext.curActivity);
		builder.setTitle("正在下载新版本");

		final LayoutInflater inflater = LayoutInflater.from(AppContext.context);
		View v = inflater.inflate(R.layout.dialog_update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
				cancleUpdate(false);
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				interceptFlag = true;
				cancleUpdate(false);
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				String apkName = getFileName(mUpdate.getPath());
				String tmpApk = apkName + ".tmp";
				// 判断是否挂载了SD卡
				if (FileUtils.isSDCardExist()) {
					savePath = Const.SD_DOWNLOADS_DIR;
				} else {// 没有挂载SD卡，无法下载文件
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}
				File apkFile = new File(savePath, apkName);

				// 是否已下载更新文件
				if (apkFile.exists()) {
					downloadDialog.dismiss();
					installApk(apkFile);
					return;
				}

				// 输出临时下载文件
				File tmpFile = new File(savePath, tmpApk);
				FileOutputStream fos = new FileOutputStream(tmpFile);

				URL url = new URL(mUpdate.getPath());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				// 显示文件大小格式：2个小数点显示
				DecimalFormat df = new DecimalFormat("0.00");
				// 进度条下面显示的总文件大小
				apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					// 进度条下面显示的当前下载文件大小
					tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
					// 当前进度值
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成 - 将临时下载文件转成APK文件
						if (tmpFile.renameTo(apkFile)) {
							// 通知安装
							Message msg = mHandler.obtainMessage();
							msg.what = DOWN_OVER;
							msg.obj = apkFile;
							mHandler.sendMessage(msg);
						}
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	public static void installApk(File apkfile) {
		// File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		AppContext.startActivityInNewTask(i);
		if(AppContext.curActivity instanceof SplashActivity){
			try {
				AppContext.curActivity.finish();
				System.exit(0);
			} catch (Exception e) {}
		}
	}
	/**
	 * 
	 * @param isIgnore 如果不是必须更新,下次将不再提醒
	 */
	private void cancleUpdate(boolean isIgnore){
		if (isMustUpdate) {
			AppContext.curActivity.finish();
			System.exit(0);
		}else if(AppContext.curActivity instanceof SplashActivity){
			if(isIgnore){
				Editor edit = AppContext.getConfigFile().edit();
				edit.putInt(Config.ignoreVersion, mUpdate.getLatestversion());
				edit.commit();
			}
			if(ClientEngine.isFirstTime()){
				AppContext.startAct(GuideActivity.class,true);
			}else{
				UserEngine.login();
			}
		}
	}
	
	/**
	 * 获取服务器文件的名称
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		return path.substring(path.lastIndexOf("/") + 1);
	}
}
