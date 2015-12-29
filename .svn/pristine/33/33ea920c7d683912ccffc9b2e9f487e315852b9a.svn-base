package com.whoyao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.whoyao.AppContext;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.map.LocManager;
import com.whoyao.map.LocReceiver;
import com.whoyao.map.MapManager;
import com.whoyao.map.OnLongTouchListener;
import com.whoyao.map.PoiItemOverlay;
import com.whoyao.map.PoiItemOverlay.OnPopClickListener;
import com.whoyao.ui.LoadingDialog;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.AddressUtil;

/**
 * poi搜索功能
 */
public class SettingSpaceTimeMapActivity extends BasicActivity implements OnClickListener {

	private MapView mMapView = null;
	private MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	/**
	 * 搜索关键字输入窗口
	 */
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private InputMethodManager imm;
	protected BDLocation location;
	private LocReceiver locReceiver;
	private MyLocationOverlay myLoc;
	private PoiItemOverlay poiOverlay;
	// private EditText editSearch;
	private String cityName = "北京";
	protected OverlayItem touchItem;
	private String msgTemple;
	// private MKSearch selectSearch;
	private LoadingDialog loading;
	private boolean isTag;
	private TextView tvBackMyLoc;
	private LocationData locData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapManager app = MapManager.getInstance();
		setContentView(R.layout.activity_poisearch);
		msgTemple = getIntent().getStringExtra(Extra.Snippet);
		initMapView();
		initSearch(app);

		if (null != AppContext.location) {
			cityName = AppContext.location.getCity();
		} else if (null != MyinfoManager.getUserInfo()) {
			cityName = AddressUtil.getSimpleAddr(MyinfoManager.getUserInfo().getUserCity());
		}

		initView();

	}

	private void initView() {
		tvBackMyLoc = (TextView) findViewById(R.id.tvBackMyLoc);
		tvBackMyLoc.setOnClickListener(this);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		findViewById(R.id.search).setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);

		// editSearch = (EditText) findViewById(R.id.searchkey);
		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
		keyWorldsView.setDropDownVerticalOffset(11);
		// keyWorldsView.setOnFocusChangeListener(new OnFocusChangeListener() {
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// keyWorldsView.setSelection(keyWorldsView.getText().length());
		// }
		// });
		sugAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line);
		keyWorldsView.setAdapter(sugAdapter);

		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		TextWatcher textWatcher = new TextWatcher() {

			private String keyword;

			@Override
			public void onTextChanged(CharSequence cs, int start, int before, int count) {
				if (TextUtils.isEmpty(cs) || !cs.toString().equals(keyword)) {
					keyword = cs.toString();
					mSearch.suggestionSearch(cs.toString(), cityName);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		keyWorldsView.addTextChangedListener(textWatcher);
		// TODO
		keyWorldsView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

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
					int indexOf = mMapView.getOverlays().indexOf(poiOverlay);
					if (-1 != indexOf) {
						mMapView.getOverlays().remove(indexOf);
					}
					poiOverlay.setData(res.getAllPoi());
					mMapView.getOverlays().clear();
					if (null != myLoc) {
						mMapView.getOverlays().add(myLoc);
					}
					mMapView.getOverlays().add(poiOverlay);
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
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
			}

			public void onGetAddrResult(MKAddrInfo result, int error) {
				// LOG.i(Const.AppName,"onGetAddrResult");
				if (error != 0 || result == null) {// 加载失败
					return;
				}
				if (isTag) {
					Intent intent = new Intent();
					intent.putExtra(Extra.LatitudeE6, result.geoPt.getLatitudeE6());
					intent.putExtra(Extra.LongitudeE6, result.geoPt.getLongitudeE6());
					intent.putExtra(Extra.ProvinceName, result.addressComponents.province);
					intent.putExtra(Extra.CityName, result.addressComponents.city);
					intent.putExtra(Extra.DistrictName, result.addressComponents.district);
					intent.putExtra(Extra.Snippet, result.strAddr);
					setResult(RESULT_OK, intent);
					if (null != loading && loading.isShowing()) {
						loading.dismiss();
					}
					finish();
				} else {
					poiOverlay.setTouchPoint(result.geoPt, result.strAddr, result.strAddr);
				}
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			/**
			 * 更新建议列表
			 */
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				if (res == null || res.getAllSuggestions() == null) {
					return;
				}
				sugAdapter.clear();
				for (MKSuggestionInfo info : res.getAllSuggestions()) {
					if (info.key != null)
						sugAdapter.add(info.key);
				}
				sugAdapter.notifyDataSetChanged();
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
			}

		});

		// selectSearch = new MKSearch();
		// selectSearch.init(app, new MKSearchListener(){
		//
		// @Override
		// public void onGetAddrResult(MKAddrInfo result, int iError) {
		// Intent intent = new Intent();
		// intent.putExtra(Extra.LatitudeE6, result.geoPt.getLatitudeE6());
		// intent.putExtra(Extra.LongitudeE6,result.geoPt.getLongitudeE6());
		// intent.putExtra(Extra.ProvinceName,
		// result.addressComponents.province);
		// intent.putExtra(Extra.CityName, result.addressComponents.city);
		// intent.putExtra(Extra.DistrictName,
		// result.addressComponents.district);
		// intent.putExtra(Extra.Snippet, result.strAddr);
		// setResult(RESULT_OK, intent);
		// if(null != loading && loading.isShowing()){
		// loading.dismiss();
		// }
		// finish();
		// }
		// @Override
		// public void onGetBusDetailResult(MKBusLineResult result, int iError)
		// {
		// }
		// @Override
		// public void onGetDrivingRouteResult(MKDrivingRouteResult result, int
		// iError) {
		// }
		// @Override
		// public void onGetPoiDetailSearchResult(int type, int iError) {
		// }
		// @Override
		// public void onGetPoiResult(MKPoiResult result, int type, int iError)
		// {
		// }
		// @Override
		// public void onGetShareUrlResult(MKShareUrlResult result, int type,
		// int error) {
		// }
		// @Override
		// public void onGetSuggestionResult(MKSuggestionResult result, int
		// iError) {
		// }
		// @Override
		// public void onGetTransitRouteResult(MKTransitRouteResult result, int
		// iError) {
		// }
		// @Override
		// public void onGetWalkingRouteResult(MKWalkingRouteResult result, int
		// iError) {
		// }});
	}

	private void initMapView() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		poiOverlay = new PoiItemOverlay(mMapView);
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
				if (-1 == mMapView.getOverlays().indexOf(poiOverlay)) {
					mMapView.getOverlays().add(poiOverlay);
				}
				poiOverlay.setTouchPoint(touchGeoPoint, "正在加载地址...", "正在加载地址...");

				mSearch.reverseGeocode(touchGeoPoint);
			}
		});

		poiOverlay.setOnPopClickListener(new OnPopClickListener() {

			private OverlayItem mItem;

			@Override
			public void onClick(OverlayItem item) {
				// Toast.show("item:   "+
				// item.getTitle()+"-----"+item.getSnippet());
				// LOG.i(Const.AppName,"LOADING");
				mItem = item;
				MessageDialog dialog = new MessageDialog(SettingSpaceTimeMapActivity.this);
				dialog.setTitle("标注");
				dialog.setHtmlMessage("确定将<font color='#3B9AC6'>" + item.getSnippet() + "</font>标注为" + msgTemple + "吗?");
				dialog.setLeftButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						isTag = true;
						mSearch.reverseGeocode(mItem.getPoint());
						loading = LoadingDialog.getDialog();
					}
				});
				dialog.setRightButton("取消", null);
				dialog.show();
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
					mMapView.getController().animateTo(
							new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));
				}
			}
		}.register();
		LocManager.getInstance().refresh();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.search:
			mSearch.poiSearchInCity(cityName, keyWorldsView.getText().toString());
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.tvBackMyLoc:
			LocManager.getInstance().refresh();
		}
	}

}
