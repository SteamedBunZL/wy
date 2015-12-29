package com.whoyao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.whoyao.AppContext;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.map.AddrInfoModel;
import com.whoyao.map.DistanceUtils;
import com.whoyao.map.LocManager;
import com.whoyao.map.LocReceiver;
import com.whoyao.map.MapManager;
import com.whoyao.map.OnLongTouchListener;
import com.whoyao.map.TouchPoiOverlay;
import com.whoyao.ui.Toast;
import com.whoyao.utils.AddressUtil;
import com.whoyao.utils.L;
import com.whoyao.utils.Utils;

/**
 * poi搜索功能
 */
public class SelectMapPointActivity extends BasicActivity implements OnClickListener, TextWatcher {

	private MapView mMapView = null;
	private MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	/**
	 * 搜索关键字输入窗口
	 */
	private InputMethodManager imm;
	protected BDLocation location;
	private LocReceiver locReceiver;
	private MyLocationOverlay myLoc;
	private TouchPoiOverlay touchOverlay;
	private String cityName = "北京";
	protected OverlayItem touchItem;
	private String msgTemple;
	// private LoadingDialog loading;
	private boolean isTag;
	private LocationData locData;
	private View vInfo;
	private TextView tvInfo;
	private EditText etDesc;
	private ImageButton btnEnter;
	private ImageButton btnMyLoacl;
	protected boolean isGoToMyLoc;
	protected GeoPoint curGP;
	private AddrInfoModel addrInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapManager app = MapManager.getInstance();

		setContentView(R.layout.activity_select_map_point);
		Intent intent = getIntent();
		// TODO 如果intent中指定左边,则不去自己的位置(isGoToMyLoc = false)
		msgTemple = intent.getStringExtra(Extra.Snippet);
		addrInfo = (AddrInfoModel) intent.getSerializableExtra(Extra.AddrModel);
		if (addrInfo == null || (addrInfo.getLatitudeE6() == 0 && addrInfo.getLongitudeE6() == 0)) {
			isGoToMyLoc = true;
		}
		initView();
		initMapView();
		initSearch(app);

		if (null != AppContext.location) {
			cityName = AppContext.location.getCity();
		} else if (null != MyinfoManager.getUserInfo()) {
			cityName = AddressUtil.getSimpleAddr(MyinfoManager.getUserInfo().getUserCity());
		}

	}

	private void initView() {
		vInfo = findViewById(R.id.select_map_ll_info);
		tvInfo = (TextView) findViewById(R.id.select_map_tv_info);
		etDesc = (EditText) findViewById(R.id.select_map_et_desc);
		btnEnter = (ImageButton) findViewById(R.id.page_btn_right);
		btnMyLoacl = (ImageButton) findViewById(R.id.select_map_ibtn_local);

		findViewById(R.id.page_btn_back).setOnClickListener(this);
		btnEnter.setOnClickListener(this);
		btnEnter.setEnabled(false);
		btnMyLoacl.setOnClickListener(this);
		etDesc.addTextChangedListener(this);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	private void initSearch(MapManager app) {
		// 初始化搜索模块，注册搜索事件监听
		mSearch = new MKSearch();
		mSearch.init(app, new MKSearchListener() {

			// 在此处理详情页结果
			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
				if (error != 0) {
					Toast.show("抱歉，未找到结果");

				} else {
					Toast.show("成功，查看详情页面");
				}
			}

			/**
			 * 在此处理poi搜索结果
			 */
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// 错误号可参考MKEvent中的定义
				// LOG.i(Const.AppName,"onGetPoiResult");
				if (error != 0 || res == null) {
					Toast.show("抱歉，未找到结果");
					return;
				}
				// 将地图移动到第一个POI中心点
				if (res.getCurrentNumPois() > 0) {
					int indexOf = mMapView.getOverlays().indexOf(touchOverlay);
					if (-1 != indexOf) {
						mMapView.getOverlays().remove(indexOf);
					}
					// TODO touchOverlay.setData(res.getAllPoi());
					mMapView.getOverlays().clear();
					if (null != myLoc) {
						mMapView.getOverlays().add(myLoc);
					}
					mMapView.getOverlays().add(touchOverlay);
					mMapView.refresh();
					// 当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
					for (MKPoiInfo info : res.getAllPoi()) {
						if (info.pt != null) {
							mMapView.getController().animateTo(info.pt);
							break;
						}
					}

				} else if (res.getCityListNum() > 0) {
					// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
					String strInfo = "在";
					for (int i = 0; i < res.getCityListNum(); i++) {
						strInfo += res.getCityListInfo(i).city;
						strInfo += ",";
					}
					strInfo += "找到结果";
					Toast.show(strInfo);
				}
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
				L.i("1");
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
				L.i("2");
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
				L.i("3");
			}

			public void onGetAddrResult(MKAddrInfo result, int error) {
				// LOG.i(Const.AppName,"onGetAddrResult");
				if (error != 0 || result == null) {// 加载失败
					if (!isTag) {
						addrInfo = null;
						showInfoLayout();
						tvInfo.setText(R.string.addr_load_fail);
						btnEnter.setEnabled(false);
					}
					return;
				}
				L.i("Result: " + result.geoPt.getLatitudeE6() + "  " + result.geoPt.getLongitudeE6());
				L.i("Current: " + curGP.getLatitudeE6() + "  " + curGP.getLongitudeE6());
				if (isTag) {
					Intent intent = new Intent();
					intent.putExtra(Extra.AddrModel, new AddrInfoModel(result));
					setResult(RESULT_OK, intent);
					finish();
				} else {
					if (DistanceUtils.CompareGeoPoint(result.geoPt, curGP)) {
						// Toast.show("equal");
						if (null == addrInfo) {
							addrInfo = new AddrInfoModel(result);
						} else {
							addrInfo.setInfo(result, "");
						}
						// addrInfo.geoPt = result.geoPt;
						// addrInfo.addressComponents =
						// result.addressComponents;
						// addrInfo.strAddr = result.strAddr;
						// addrInfo.strBusiness = result.strBusiness;
						// addrInfo.poiList = result.poiList;
						// addrInfo.type = result.type;
						touchOverlay.setTouchPoint(result.geoPt, result.strAddr, result.strAddr);
						setInfoLayout(result.strAddr);
					}
				}
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
				L.i("4");
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				L.i("5");
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
				L.i("6");
			}
		});
	}

	private void initMapView() {
		mMapView = (MapView) findViewById(R.id.bmapEventView);
		// markerHeight =
		// getResources().getDrawable(R.drawable.poi_marker_active).getIntrinsicHeight();
		touchOverlay = new TouchPoiOverlay(mMapView);
		mMapView.getController().enableClick(true);
		mMapView.getController().setZoom(12);
		mMapView.setLongClickable(true);
		mMapView.setBuiltInZoomControls(false);
		mMapView.setOnTouchListener(new OnLongTouchListener(this) {
			@Override
			public void onLongPress(MotionEvent e) {
				e.getRawX();
				e.getRawY();
				int x = (int) e.getX();
				int y = (int) e.getY();
				// mapTips.setVisibility(View.GONE);
				Projection fromPixelspProjection = mMapView.getProjection();
				GeoPoint touchGeoPoint = fromPixelspProjection.fromPixels(x, y);
				curGP = touchGeoPoint;
				if (-1 == mMapView.getOverlays().indexOf(touchOverlay)) {
					mMapView.getOverlays().add(touchOverlay);
				}
				touchOverlay.setTouchPoint(touchGeoPoint, getString(R.string.loading), getString(R.string.loading));
				showInfoLayout();
				if (AppContext.isNetAvailable()) {
					mSearch.reverseGeocode(touchGeoPoint);
				} else {
					addrInfo = null;
					hideInfoLayout();
					btnEnter.setEnabled(false);
					Toast.show(R.string.warn_network_unavailable);
				}
			}
		});

		locReceiver = new LocReceiver(LocReceiver.Success_And_Fail) {

			@Override
			public void onReceive(Context context, Intent intent) {

				location = AppContext.location;
				if (null != location) {
					if (null == myLoc) {
						myLoc = new MyLocationOverlay(mMapView);
					} else {
						mMapView.getOverlays().remove(myLoc);
					}
					locData = new LocationData();
					locData.latitude = location.getLatitude();
					locData.longitude = location.getLongitude();
					// 如果不显示定位精度圈，将accuracy赋值为0即可
					locData.accuracy = location.getRadius();
					locData.direction = location.getDerect();
					myLoc.setData(locData);
					// myLoc.setMarker(AppContext.getRes().getDrawable(R.drawable.btn_footbar_mid));
					mMapView.getOverlays().add(myLoc);
					myLoc.enableCompass();
					mMapView.refresh();
					if (isGoToMyLoc) {
						mMapView.getController().animateTo(
								new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));
						isGoToMyLoc = false;
					}
				}
			}
		}.register();
		LocManager.getInstance().refresh();
		if (null != addrInfo && (addrInfo.getLatitudeE6() != 0 && addrInfo.getLongitudeE6() != 0)) {
			if (-1 == mMapView.getOverlays().indexOf(touchOverlay)) {
				mMapView.getOverlays().add(touchOverlay);
			}
			touchOverlay.setTouchPoint(addrInfo.getGeoPoint(), addrInfo.getStrAddr(), addrInfo.getStrAddr());
			mMapView.getController().setCenter(addrInfo.getGeoPoint());
			setInfoLayout(addrInfo.getStrAddr());
			etDesc.setText(addrInfo.getDesc());
			btnEnter.setEnabled(false);
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		locReceiver.unRegister();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		locReceiver.register();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	// 下一组数据 已删除
	// public void goToNextPage(View v) {
	// // 搜索下一组poi
	// int flag = mSearch.goToPoiPage(++load_Index);
	// if (flag != 0) {
	// Toast.show("先搜索开始，然后再搜索下一组数据");
	// }
	// }
	private void showInfoLayout() {
		vInfo.setVisibility(View.VISIBLE);
		tvInfo.setText(getString(R.string.loading));
		etDesc.setVisibility(View.GONE);
	}

	private void setInfoLayout(String addrStr) {
		vInfo.setVisibility(View.VISIBLE);
		tvInfo.setText(addrStr);
		etDesc.setVisibility(View.VISIBLE);
		etDesc.setText("");
		btnEnter.setEnabled(true);
	}

	private void hideInfoLayout() {
		vInfo.setVisibility(View.GONE);
		btnEnter.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			Utils.hideSoftKeyboard(context);
			finish();
			break;
		case R.id.page_btn_right:
			if (null != addrInfo) {
				String desc = etDesc.getText().toString();
				if(desc.length() > 20){
					Toast.show("详细地址最多20字");// TODO
				}
				Intent intent = new Intent();
				addrInfo.setDesc(etDesc.getText().toString());
				addrInfo.setGeoPint(null);
				intent.putExtra(Extra.AddrModel, addrInfo);
				setResult(RESULT_OK, intent);
				Utils.hideSoftKeyboard(context);
				finish();
			} else {
				v.setEnabled(false);
			}

			break;
		case R.id.search:
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.tvBackMyLoc:
			LocManager.getInstance().refresh();
			break;
		case R.id.select_map_ibtn_local:
			if (null != AppContext.location) {
				isGoToMyLoc = true;
				locReceiver.onReceive(context, null);
			} else {
				isGoToMyLoc = true;
				LocManager.getInstance().refresh();
			}
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (!btnEnter.isEnabled()) {
			btnEnter.setEnabled(true);
		}
		if(s.length() > 20){
			etDesc.setText(s.toString().subSequence(0, 20));
			etDesc.setSelection(20);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
