package com.whoyao.activity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.FaceImageAsyncLoader;
import com.whoyao.engine.event.EventEngine;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.fragment.EventFragment;
import com.whoyao.fragment.HomeFragment;
import com.whoyao.fragment.TestFragment;
import com.whoyao.fragment.TopicListFragment;
import com.whoyao.fragment.UserListFragment;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.service.ActivityCallBack;
import com.whoyao.service.ConnectStatus;
import com.whoyao.service.WYService;
import com.whoyao.topic.TopicAddActivity;
import com.whoyao.topic.TopicUserListActivity;
import com.whoyao.ui.Toast;
import com.whoyao.utils.L;
import com.whoyao.utils.SocketClient;
import com.whoyao.venue.MyBillActivity;
import com.whoyao.venue.VenueFragment;
import com.whoyao.venue.engine.alipay.Rsa;
import com.whoyao.venue.model.OrderInfoModel;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 首页
 * 
 * @author hyh creat_at：2013-8-28-下午3:00:39
 */
public class MainActivity extends BasicFragmentActivity implements
		OnClickListener, ActivityCallBack {

	public static final int REQUESTCODE_PRIVATE = 1;
	public static final int REQUESTCODE_NOTICE = 2;
	public static final int REQUEST_CODE_CITY = 3;
	private WYService mWyService;
	private SlidingMenu smenu;
	private Button btnLeft;
	private Button btnMid;
	private Button btnRight;
	private ImageView ivFace;
	private TextView tvNickname;
	private TextView tvMessageNum;
	private FaceImageAsyncLoader loader;
	private View cmenu;
	private Animation clockwiseAnim;
	private Animation anticlockwise;
	private Animation showAnim;
	private Animation hideAnim;
	private FragmentManager fragManager;
	private Fragment currentFrag;
	private long exitTime;
	private static Map<Class<?>, WeakReference<Fragment>> fragMap = new HashMap<Class<?>, WeakReference<Fragment>>();
	Bundle bundle = new Bundle();
	private Socket client;
	private int i = 0;
	private TextView tvCircle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppContext.curActivity = this;
		setContentView(R.layout.activity_main);
		loader = FaceImageAsyncLoader.getInstance();
		fragManager = getSupportFragmentManager();
		initView();
		startService(new Intent(MainActivity.this, WYService.class));
		bindWyService();
	}

	private void bindWyService() {
		Intent mServiceIntent = new Intent(this, WYService.class);
		bindService(mServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE
				+ Context.BIND_DEBUG_UNBIND);
	}
	private void unbindWyService(){
		try {
			unbindService(serviceConnection);
		} catch (IllegalArgumentException e) {
			L.e("Service wasn't bound!");
		}
	}

	ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mWyService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mWyService = ((WYService.WYBinder) service).getService();

		}
	};

	private void initView() {
		if (AppContext.AppState == Const.STATUS_DEBUG) {
			// setFragment(HomeFragment.class);
			setFragment(TestFragment.class); // TODO
		} else {
			setFragment(HomeFragment.class);
		}
		tvCircle = (TextView) findViewById(R.id.tv_message_circle);
		btnLeft = (Button) findViewById(R.id.main_btn_left);
		btnMid = (Button) findViewById(R.id.main_btn_mid);
		btnRight = (Button) findViewById(R.id.main_btn_right);
		btnLeft.setOnClickListener(this);
		btnMid.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		clockwiseAnim = AnimationUtils.loadAnimation(this, R.anim.clockwise);
		anticlockwise = AnimationUtils
				.loadAnimation(this, R.anim.anticlockwise);
		clockwiseAnim.setFillAfter(true);
		showAnim = AnimationUtils.loadAnimation(this, R.anim.plugin_show);
		hideAnim = AnimationUtils.loadAnimation(this, R.anim.plugin_hide);
		initCircleMenu();
		initSlidingMenu();
	}

	/**
	 * 初始化半圆形菜单
	 */
	private void initCircleMenu() {
		cmenu = findViewById(R.id.main_rl_circle_menu);
		CmenuOnClickListener listener = new CmenuOnClickListener();
		findViewById(R.id.menu_venue).setOnClickListener(listener);
		findViewById(R.id.menu_find_event).setOnClickListener(listener);
		findViewById(R.id.menu_add_event).setOnClickListener(listener);
		findViewById(R.id.menu_find_topic).setOnClickListener(listener);
		findViewById(R.id.menu_invite).setOnClickListener(listener);
	}

	/**
	 * 初始化左右滑动菜单
	 */
	private void initSlidingMenu() {
		smenu = new SlidingMenu(this);
		smenu.setMode(SlidingMenu.LEFT_RIGHT);
		smenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		smenu.setMenu(R.layout.smenu_left);
		smenu.setSecondaryMenu(R.layout.smenu_right);
		smenu.setBehindOffset(btnLeft.getBackground().getIntrinsicWidth());
		smenu.setShadowWidthRes(R.dimen.smenu_shadow_width);
		smenu.setShadowDrawable(R.drawable.shadow_right);
		smenu.setSecondaryShadowDrawable(R.drawable.shadow_left);
		smenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		OnClickListener smListener = new SmenuOnClickListener();
		/* 左侧菜单 */
		View smLeft = smenu.getMenu();
		smLeft.findViewById(R.id.smenu_home).setOnClickListener(smListener);
		smLeft.findViewById(R.id.smenu_event).setOnClickListener(smListener);
		smLeft.findViewById(R.id.smenu_venue).setOnClickListener(smListener);
		smLeft.findViewById(R.id.smenu_topic).setOnClickListener(smListener);
		smLeft.findViewById(R.id.smenu_user).setOnClickListener(smListener);
		/* 右侧菜单 */
		View smRight = smenu.getSecondaryMenu();
		tvMessageNum = (TextView) findViewById(R.id.tv_message_num);
		smRight.findViewById(R.id.smenu_myevent).setOnClickListener(smListener);
		smRight.findViewById(R.id.smenu_myintivation).setOnClickListener(
				smListener);
		smRight.findViewById(R.id.smenu_mytopic).setOnClickListener(smListener);
		smRight.findViewById(R.id.smenu_myfriend)
				.setOnClickListener(smListener);
		smRight.findViewById(R.id.smenu_mymessage).setOnClickListener(
				smListener);
		smRight.findViewById(R.id.smenu_myorder).setOnClickListener(smListener);
		smRight.findViewById(R.id.smenu_myfund).setOnClickListener(smListener);
		smRight.findViewById(R.id.smenu_set).setOnClickListener(smListener);
		/* 右侧头像&昵称 */
		tvNickname = (TextView) smRight.findViewById(R.id.smenu_nickname);
		ivFace = (ImageView) smRight.findViewById(R.id.smenu_face);
		tvNickname.setOnClickListener(smListener);
		ivFace.setOnClickListener(smListener);
		smenu.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				btnRight.setEnabled(false);
				hideCircleMenu(false);
			}
		});

		smenu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {// TODO BUG:右侧菜单展开时,不会触发该事件
				btnLeft.setEnabled(false);
				btnRight.setEnabled(false);
				cmenu.setVisibility(View.GONE);
				btnMid.clearAnimation();
			}
		});
		smenu.setOnCloseListener(new OnCloseListener() {
			@Override
			public void onClose() {
				btnLeft.setEnabled(true);
				btnRight.setEnabled(true);
			}
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (View.GONE == cmenu.getVisibility()) {
			showCircleMenu();
		} else {
			hideCircleMenu();
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_btn_left:
			smenu.showMenu(true);
			break;
		case R.id.main_btn_mid:
			switchCircleMenu();
			break;
		case R.id.main_btn_right:
			// TODO 右侧菜单打开不能触发Open事件,的弥补措施
			btnRight.setEnabled(false);
			smenu.showSecondaryMenu(true);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		NetCache.clearCaches();
		if (null != MyinfoManager.getUserInfo()) {
			tvNickname.setText(MyinfoManager.getUserInfo().getUserName());
			loader.loadBitmap(MyinfoManager.getUserInfo().getUserFace(), ivFace);
		}
		Net.cacheRequest(new RequestParams(),
				WebApi.Message.GetUnReadMessageNum, new ResponseHandler(false) {

					@Override
					public void onSuccess(String content) {
						if (content.equals("0")) {
							tvMessageNum.setVisibility(View.INVISIBLE);
							tvCircle.setVisibility(View.INVISIBLE);
						} else {
							tvMessageNum.setText(content);
							tvMessageNum.setVisibility(View.VISIBLE);
							tvCircle.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailure(Throwable e, int statusCode,
							String content) {
					}

				});

	}

	@Override
	protected void onPause() {
		L.i(Const.ZL, "onPause");
		unbindWyService();
		super.onPause();
	}

	/** 圆形菜单点击事件 */
	private class CmenuOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switchCircleMenu();
			switch (v.getId()) {
			case R.id.menu_venue:
				// TODO
				// bundle.putString(Extra.Title, "找场馆");
				// setFragment(DevelopingFragment.class, bundle);
				// currentFrag.onResume();
				setFragment(VenueFragment.class);
				break;
			case R.id.menu_find_event:
				AppContext.startAct(EventSearchInitialActivity.class);
				break;
			case R.id.menu_add_event:
				EventEngine.publishEvent();
				break;
			case R.id.menu_find_topic:
				// TODO
				bundle.putString(Extra.Title, "发话题");
				// setFragment(DevelopingFragment.class, bundle);
				// currentFrag.onResume();
				AppContext.startAct(TopicAddActivity.class);
				break;
			case R.id.menu_invite:
				setFragment(UserListFragment.class);
				break;
			default:
				break;
			}
		}
	}

	/** 左右菜单点击事件 */
	private class SmenuOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.smenu_home:
				smenu.toggle();
				setFragment(HomeFragment.class);
				break;
			case R.id.smenu_event:
				smenu.toggle();
				setFragment(EventFragment.class);
				break;
			case R.id.smenu_venue:
				smenu.toggle();
				setFragment(VenueFragment.class);
				break;
			case R.id.smenu_topic:
				smenu.toggle();
				// TODO
				// smenu.toggle();
				// bundle.putString(Extra.Title, "话题");
				// setFragment(DevelopingFragment.class, bundle);
				// currentFrag.onResume();
				setFragment(TopicListFragment.class);
				break;
			case R.id.smenu_user:// 朋友
				smenu.toggle();
				setFragment(UserListFragment.class);
				break;
			/* 右侧菜单 */
			/* 右侧头像&昵称 */
			case R.id.smenu_nickname:
			case R.id.smenu_face:
				AppContext.startAct(UserSelfDetialActivity.class);
				break;
			case R.id.smenu_myevent:
				AppContext.startAct(EventMyListActivity.class);
				break;
			case R.id.smenu_myintivation:
				AppContext.startAct(MyInviteActivity.class);
				break;
			case R.id.smenu_mytopic:
				AppContext.startAct(TopicUserListActivity.class);
				break;
			case R.id.smenu_myfriend:
				AppContext.startAct(MyFriendActivity.class);
				break;
			case R.id.smenu_mymessage:
				AppContext.startAct(MyMessageActivity.class);
				break;
			case R.id.smenu_myorder:
				// TODO
				AppContext.startAct(MyBillActivity.class);
				// smenu.toggle();
				// bundle.putString(Extra.Title, "我的订单");
				// setFragment(DevelopingFragment.class, bundle);
				// currentFrag.onResume();
				break;
			case R.id.smenu_myfund:
				// TODO
				// smenu.toggle();
				// bundle.putString(Extra.Title, "我的资金");
				// setFragment(DevelopingFragment.class,bundle);
				// currentFrag.onResume();
				AppContext.startAct(MyFundActivity.class);
				break;
			case R.id.smenu_set:
				AppContext.startAct(SettingActivity.class);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 切换圆形菜单状态
	 */
	private void switchCircleMenu() {
		if (View.GONE == cmenu.getVisibility()) {
			showCircleMenu();
		} else {
			hideCircleMenu();
		}
	}

	/** 显示圆形菜单 */
	private void showCircleMenu() {
		cmenu.setVisibility(View.VISIBLE);
		cmenu.startAnimation(showAnim);
		btnMid.startAnimation(clockwiseAnim);
	}

	/** 隐藏圆形菜单 */
	private void hideCircleMenu() {
		cmenu.setVisibility(View.GONE);
		cmenu.startAnimation(hideAnim);
		btnMid.startAnimation(anticlockwise);
	}

	public void hideCircleMenu(boolean hasAnimation) {
		if (hasAnimation) {
			hideCircleMenu();
		} else {
			cmenu.setVisibility(View.GONE);
			btnMid.clearAnimation();
		}
	}

	public void setFragment(Class<? extends Fragment> cls) {
		setFragment(cls, null);
	}

	// bundle.putString("key", Projsid);
	// sf.setArguments(bundle);
	// ft.add(R.id.fragmentRoot, sf, SEARCHPROJECT);
	// ft.addToBackStack(SEARCHPROJECT);
	// ft.commit();
	public void setFragment(Class<? extends Fragment> cls, Bundle bundle) {
		if (null != currentFrag && currentFrag.getClass().equals(cls)) {
			return;
		}
		try {
			WeakReference<Fragment> ref = fragMap.get(cls);
			Fragment fragment = null;
			if (null == ref || null == (fragment = ref.get())) {
				fragment = (Fragment) cls.newInstance();
				fragMap.put(cls, new WeakReference<Fragment>(fragment));
			}
			if (null != bundle) {
				fragment.setArguments(bundle);
			}
			FragmentTransaction ft = fragManager.beginTransaction();
			ft.replace(R.id.main_ll_fragment, fragment);
			ft.commit();
			currentFrag = fragment;
		} catch (InstantiationException e) {
			AppException.handle(e);
		} catch (IllegalAccessException e) {
			AppException.handle(e);
		}
	}

	// /**
	// * 连续按两次返回键就退出
	// */
	// private long firstTime;
	//
	// @Override
	// public void onBackPressed() {
	// if (System.currentTimeMillis() - firstTime < 3000) {
	// finish();
	// } else {
	// firstTime = System.currentTimeMillis();
	// Toast.show("再按一次退出程序");
	// }
	// }
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// moveTaskToBack(false);
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (smenu.isMenuShowing() || smenu.isSecondaryMenuShowing()) {
				smenu.toggle();
				return true;
			}
			if ((AppContext.serviceTimeMillis() - exitTime) > 2000) {
				Toast.show("再按一次退出程序");
				exitTime = AppContext.serviceTimeMillis();
			} else {
				ConnectStatus.setStatus(false);
				stopService(new Intent(this, WYService.class));
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public String toString() {
		return "首页";
	}

	@Override
	public WYService getService() {
		return mWyService;
	}

}
