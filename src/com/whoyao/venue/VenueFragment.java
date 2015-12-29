package com.whoyao.venue;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.EventDetialActivity;
import com.whoyao.activity.EventMapModeActivity;
import com.whoyao.activity.EventSearchInitialActivity;
import com.whoyao.activity.MainActivity;
import com.whoyao.adapter.ViewPagerAdapter;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.ImageResGetter;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventListManager;
import com.whoyao.model.AreaData;
import com.whoyao.model.EventListItem;
import com.whoyao.model.HotImageModel;
import com.whoyao.model.LocationPagingModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.FormatUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;
import com.whoyao.utils.Utils;
import com.whoyao.venue.engine.VenueListAdapter;
import com.whoyao.venue.model.VenueHomeRModel;
import com.whoyao.venue.model.VenueItemModel;

/**
 * @author hyh creat_at：2013-9-3-下午4:42:56
 */
public class VenueFragment extends Fragment implements OnClickListener,
		OnItemClickListener, OnRefreshListener2<ListView> {

	protected static final int PAGER_ID = 0;
	private ListView mListView;
	private View emptyView;
	private List<VenueItemModel> mList = new ArrayList<VenueItemModel>();
	private List<HotImageModel> hotList;
	private List<ImageView> views = new ArrayList<ImageView>();

	private BaseAdapter mAdapter;
	private View vHeader;
	private ViewPager glHotImage;
	private ViewPagerAdapter<HotImageModel> pagerAdapter;
	private ImageAsyncLoader loader;
	private LinearLayout llIndic;
	private PullToRefreshListView mPullRefreshListView;
	private View contentView;
	private LocationPagingModel model;
	private boolean isRefresh;
	private ResponseHandler moreHandler;
	private ResponseHandler initHandler;
	// private TextView tvCity;
	private View btnMapMode;
	private View btnSearch;
	// private Intent cityIntent;
	private Button btnPlace;
	private Button btnVenue;
//	private ViewGroup viewGroup;
//	private int groupSize;
//	private ImageView imageView;
//	private ImageView[] imageViews;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// cityIntent = new Intent(AppContext.curActivity,
		// EventSearchInitialActivity.class);
		if (null == contentView) {
			loader = OriginalImageAsyncLoader.getInstance();
			initRequestModel();
			contentView = inflater.inflate(R.layout.frag_venue, container,
					false);
			vHeader = inflater.inflate(R.layout.header_hot_venue, null);
			vHeader.setVisibility(View.INVISIBLE);
			initView();
			initDate();
			vHeader.setVisibility(View.VISIBLE);
		} else {
			((ViewGroup) contentView.getParent()).removeView(contentView);
		}
		if (pagerAdapter != null) {
			pagerAdapter.notifyDataSetChanged();
		}
		if (null != mAdapter) {
			mAdapter.notifyDataSetChanged();
		}
		return contentView;
	}

	private void initView() {
		// tvCity = (TextView)contentView.findViewById(R.id.event_tv_city);
		// tvCity.setOnClickListener(this);
		btnMapMode = contentView.findViewById(R.id.venue_iv_map);
		btnMapMode.setOnClickListener(this);
		btnSearch = contentView.findViewById(R.id.venue_iv_search);
		btnSearch.setOnClickListener(this);
		emptyView = contentView.findViewById(R.id.event_ll_empty);
		emptyView.findViewById(R.id.event_empty_other_area).setOnClickListener(
				this);
		emptyView.findViewById(R.id.event_empty_add_event).setOnClickListener(
				this);

		glHotImage = (ViewPager) vHeader.findViewById(R.id.header_viewpager);
		llIndic = (LinearLayout) vHeader.findViewById(R.id.header_ll_indic);
		btnPlace = (Button) vHeader.findViewById(R.id.header_btn_place);
		btnVenue = (Button) vHeader.findViewById(R.id.header_btn_venue);
//		viewGroup = (ViewGroup) vHeader.findViewById(R.id.viewGroup);
		btnPlace.setOnClickListener(this);
		btnVenue.setOnClickListener(this);

		mPullRefreshListView = (PullToRefreshListView) contentView
				.findViewById(R.id.venue_lv);
		mPullRefreshListView.setMode(Mode.BOTH);
		mListView = mPullRefreshListView.getRefreshableView();
		mListView.setHeaderDividersEnabled(false);
		mPullRefreshListView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT));
		// android.R.color.transparent
		// mListView.setEmptyView(emptyView);
		mPullRefreshListView.setOnItemClickListener(this);
		mListView.addHeaderView(vHeader, null, true);
		mListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mAdapter = new VenueListAdapter(getActivity(), mList);
		mPullRefreshListView.setAdapter(mAdapter);
		mPullRefreshListView.setOnRefreshListener(this);
		glHotImage.setOnPageChangeListener(new HeaderChangeListener());
		if (null != pagerAdapter) {
			pagerAdapter.notifyDataSetChanged();
		}
	}

	private void initRequestModel() {
		if (null == model) {
			model = new LocationPagingModel();
		}
		model.refreshLocation();
	}

	/**
	 * 初始化和下拉刷新
	 * <p>
	 * 刷新是不显示ProgressDialog
	 */
	private void initDate() {
		if (null == initHandler) {
			initHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					isRefresh = false;
					VenueHomeRModel data = JSON.getObject(content,
							VenueHomeRModel.class);
					ArrayList<VenueItemModel> list = data.getVenuelist();
					if (null != list) {
						mList.clear();
						mList.addAll(list);
						if (mList.size() == 0)
							mListView.setEmptyView(emptyView);
						mAdapter.notifyDataSetChanged();
						initHotEvent();
						mPullRefreshListView.setMode(Mode.BOTH);
					}
					if (mList.isEmpty()) {
						hideRightBtn();
					} else {
						showRightBtn();
					}
					mPullRefreshListView.onRefreshComplete();

					if (model.getPageindex() == 1
							&& data.getHotvenuelist() != null
							&& !data.getHotvenuelist().isEmpty()) {
						initHotImageAdapter(data.getHotvenuelist());
//						viewGroup.removeAllViews();
//						groupSize = data.getHotvenuelist().size();
//						initGroupView();
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					super.onFailure(e, statusCode, content);
					mPullRefreshListView.onRefreshComplete();
					if (400 == statusCode) {
						mList.clear();
						mAdapter.notifyDataSetChanged();
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
					hideRightBtn();
				}
			};
		}
		initHandler.setShowProgress(!isRefresh);
		model.setPageindex(Const.PAGE_DEFAULT_INDEX);
		Net.cacheRequest(model, WebApi.Venue.VenueHome, initHandler);
	}

//	private void initGroupView() {
//		imageViews = new ImageView[groupSize];
//		for (int i = 0; i < groupSize; i++) {
//			LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
//					LinearLayout.LayoutParams.WRAP_CONTENT,
//					LinearLayout.LayoutParams.WRAP_CONTENT);
//			margin.setMargins(10, 0, 0, 0);
//			imageView = new ImageView(AppContext.curActivity);
//			imageView.setLayoutParams(new LayoutParams(15, 15));
//			imageViews[i] = imageView;
//			if (i == 0) {
//				imageViews[i]
//						.setBackgroundResource(R.drawable.page_indicator_focused);
//			} else {
//				imageViews[i]
//						.setBackgroundResource(R.drawable.page_indicator_unfocused);
//			}
//			viewGroup.addView(imageViews[i], margin);
//		}
//	}

	private void loadMore() {
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(!isRefresh) {
				@Override
				public void onSuccess(String content) {
					ArrayList<VenueItemModel> list = JSON.getObject(content,
							VenueHomeRModel.class).getVenuelist();
					mPullRefreshListView.onRefreshComplete();
					if (null != list) {
						mList.addAll(list);
						mAdapter.notifyDataSetChanged();
						if (model.getPagesize() > list.size()) {
							mPullRefreshListView.setMode(Mode.PULL_FROM_START);
						}
					} else {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
					if (!mList.isEmpty()) {
						showRightBtn();
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					super.onFailure(e, statusCode, content);
					mPullRefreshListView.onRefreshComplete();
					if (400 == statusCode) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				}
			};
		}
		model.setPageindex(model.getPageindex() + 1);
		Net.cacheRequest(model, WebApi.Venue.VenueHome, moreHandler);
	}

	private void initHotImageAdapter(List<HotImageModel> list) {
		if (null == pagerAdapter) {
			pagerAdapter = new ViewPagerAdapter<HotImageModel>(getActivity(),
					list) {

				@Override
				public int getCount() {
					return contentList.size();
				}

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					if (null == convertView) {
						convertView = View.inflate(getActivity(),
								R.layout.item_hotevent, null);
						convertView.setId(PAGER_ID);
						convertView.setOnClickListener(VenueFragment.this);
					}
					convertView.setTag(R.id.about_tv_copyright, contentList
							.get(position).getLink());
					loader.loadBitmap(contentList.get(position).getPath(),
							(ImageView) convertView);
					return convertView;
				}
			};
			glHotImage.setAdapter(pagerAdapter);
			// HotImageModel item = list.get(0);
			// tvHotTitle.setText(item.getTitle());
			glHotImage.setVisibility(View.VISIBLE);
			llIndic.setVisibility(View.VISIBLE);
			mListView.setHeaderDividersEnabled(true);
		}
	}

	class HeaderChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < views.size(); i++) {
				views.get(i).setBackgroundResource(R.drawable.ring_gary);
			}
			views.get(arg0).setBackgroundResource(R.drawable.ring_white);
//			for (int i = 0; i < imageViews.length; i++) {
//				imageViews[arg0]
//						.setBackgroundResource(R.drawable.page_indicator_focused);
//
//				if (arg0 != i) {
//					imageViews[i]
//							.setBackgroundResource(R.drawable.page_indicator_unfocused);
//				}
//			}

		}

	}

	private void initHotEvent() {
		EventListManager.getInstance().getHotEvent(
				new CallBack<List<EventListItem>>() {
					@Override
					public void onCallBack(boolean isSuccess,
							List<EventListItem> data) {
						if (null != data && !data.isEmpty()) {
							// hotList = data;
							if (null != pagerAdapter) {
								pagerAdapter.notifyDataSetChanged();
							}
							for (View view : views) {
								llIndic.removeView(view);
							}
							views.clear();
							for (int i = 0; i < hotList.size(); i++) {
								ImageView view = (ImageView) View.inflate(
										getActivity(),
										R.layout.item_hotevent_pointer, null);
								int dimension = (int) (getResources()
										.getDimension(R.dimen.dip8));
								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
										dimension, dimension);

								params.setMargins(0, 0, dimension / 2, 0);
								view.setLayoutParams(params);
								views.add(view);
								if (i == 0) {
									view.setBackgroundResource(R.drawable.ring_white);
								} else {
									view.setBackgroundResource(R.drawable.ring_gary);
								}
								llIndic.addView(view);
							}

							glHotImage.setAdapter(pagerAdapter);
							// EventListItem item = hotList.get(0);
							// tvHotTitle.setText(item.getTitle());
							glHotImage.setVisibility(View.VISIBLE);
							llIndic.setVisibility(View.VISIBLE);
							mListView.setHeaderDividersEnabled(true);
						}
					}
				});
	}

	private void showRightBtn() {
		btnMapMode.setVisibility(View.VISIBLE);
		// btnSearch.setVisibility(View.VISIBLE);
	}

	private void hideRightBtn() {
		btnMapMode.setVisibility(View.INVISIBLE);
		// btnSearch.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		((MainActivity) getActivity()).hideCircleMenu(false);
		switch (v.getId()) {
		// case R.id.venue_tv_city:
		// Intent data = new Intent(getActivity(), SelectCityActivity.class);
		// startActivityForResult(data, MainActivity.REQUEST_CODE_CITY);
		// break;
		case R.id.venue_iv_map:
			Intent intent = new Intent(getActivity(),
					VenueMapModeActivity.class);
			startActivity(intent);
			break;
		case R.id.venue_iv_search:
			// TODO
			// getActivity().startActivity(cityIntent);
			// AppContext.startAct(EventSearchInitialActivity.class);

			break;
		case PAGER_ID:
			String link = (String) v.getTag(R.id.about_tv_copyright);
			// String link =
			// "http://d.whoyao.com/venue/54?type=1&date=0001-01-01&time=-1#showBook";
			if (FormatUtils.isVenueUrl(link)) {
				Uri uri = Uri.parse(link);
				List<String> segments = uri.getPathSegments();
				try {
					String type = segments.get(0);
					int id = Integer.parseInt(segments.get(1));
					HashMap<String, String> map = JSON.getObject(getActivity()
							.getString(R.string.rest_class_map), HashMap.class);
					type = map.get(type);
					Class<?> cls = Class.forName(type);
					Intent intent2 = new Intent(getActivity(), cls);
					intent2.putExtra(Extra.SelectedID, id);
					getActivity().startActivity(intent2);
				} catch (Exception e) {
					AppException.handle(new NumberFormatException(
							"URL不符合约定的REST格式"));
				}

			} else if (FormatUtils.isEventUrl(link)) {
				Uri uri = Uri.parse(link);
				List<String> segments = uri.getPathSegments();
				try {
					int id = Integer.parseInt(segments.get(0));
					Intent intent2 = new Intent(getActivity(),
							EventDetialActivity.class);
					intent2.putExtra(Extra.SelectedID, id);
					getActivity().startActivity(intent2);
				} catch (Exception e) {
					AppException.handle(new NumberFormatException(
							"URL不符合约定的REST格式"));
				}

			} else {
				Utils.openURL(getActivity(), link);
			}
			break;
		case R.id.header_btn_place:
			AppContext.startAct(SiteSearchInitialActivity.class);
			break;

		case R.id.header_btn_venue:
			AppContext.startAct(VenueSearchInitialActivity.class);
			break;

		// case R.id.venue_empty_other_area:
		// Intent dataOther = new Intent(getActivity(),
		// SelectCityActivity.class);
		// startActivityForResult(dataOther, MainActivity.REQUEST_CODE_CITY);
		// break;
		// case R.id.venue_empty_add_event:
		// EventEngine.publishEvent();
		// break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case MainActivity.REQUEST_CODE_CITY:
			// if (Activity.RESULT_OK == resultCode && null != data) {
			// AreaData area = (AreaData)
			// data.getSerializableExtra(Extra.SelectedItem);
			// if (null != area) {
			// cityIntent.putExtra(Extra.SelectedItem, area);
			// // tvCity.setText(area.getAreaName());
			// initRequestModel();
			// initDate();
			// }
			// }
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int hearders = mListView.getHeaderViewsCount();
		Intent intent = new Intent(getActivity(), VenueDetialActivity.class);
		intent.putExtra(Extra.SelectedID, mList.get(position - hearders)
				.getVenueid());
		intent.putExtra(Extra.Title, mList.get(position - hearders)
				.getFullname());
		startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
		isRefresh = true;
		NetCache.clearCaches();
		initDate();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadMore();
	}

}
