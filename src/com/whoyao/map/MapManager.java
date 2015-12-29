package com.whoyao.map;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const;
import com.whoyao.ui.Toast;

public class MapManager extends BMapManager{
	/*
	 * 注意：为了给用户提供更安全的服务，Android SDK自v2.1.3版本开始采用了全新的Key验证体系。
	 * 因此，当您选择使用v2.1.3及之后版本的SDK时，需要到新的Key申请页面进行全新Key的申请， 申请及配置流程请参考开发指南的对应章节
	 */
	public boolean m_bKeyRight = true;
	private static MapManager mBMapManager;


	public static MapManager getInstance() {
		if(null == mBMapManager){
			mBMapManager = new MapManager();
		}
		return mBMapManager;
	}
	
	private MapManager() {
		super(AppContext.context);
		if (!init(getKeyStr(), new MyGeneralListener())) {
			Toast.show("地图初始化失败!");
			AppException.handle(new NullPointerException("BMapManager  初始化错误!"));
		}
	}
	/**
	 * 获取Key,
	 * @return
	 */
	private static String getKeyStr() {
		
//		return Const.MapKey;//用于2.1.2及以前版本
		// 以下代码用于2.1.3及以后版本
		if (AppContext.AppState == Const.STATUS_RELEASE) {
			return Const.MapKey_Release;
		} else {
			return Const.MapKey_Debug;
		}

	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.show("您的网络出错啦！");
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.show("输入正确的检索条件！");
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				// Toast.show("请在 DemoApplication.java文件输入正确的授权Key！");
				AppException.handle(new NullPointerException("请在MapEngine.java文件输入正确的授权Key！"));
				MapManager.getInstance().m_bKeyRight = false;
			}
		}
	}
}