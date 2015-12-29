package com.whoyao.venue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.Const.State;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.BasicActivity;
import com.whoyao.activity.SelectSingleWheelActivity;
import com.whoyao.common.BillImageAsyncLoader;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.PickerDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;
import com.whoyao.venue.engine.VenueDetialManager;
import com.whoyao.venue.engine.VenueEngine;
import com.whoyao.venue.engine.VenuePlaceAdapter;
import com.whoyao.venue.model.CartAddItemTModel;
import com.whoyao.venue.model.CartSubItemModel;
import com.whoyao.venue.model.PlaceStatisticModel;
import com.whoyao.venue.model.SiteVenueItem;
import com.whoyao.venue.model.VenueDetialModel;
import com.whoyao.venue.model.VenueReserveRModel;
import com.whoyao.widget.HorizontalPicker;

/**
 * @author hyh creat_at：2014-2-18-上午11:03:53
 */
public class VenueDetialActivity extends BasicActivity implements
		OnClickListener {

	private int mVenueID;
	private String mTitle;
	private TextView tvTitle;
	private ListView mListView;
	private TextView tvName;
	private TextView tvValue;
	private TextView tvType;
	private TextView tvTime;
	private TextView tvPhone;
	private LinearLayout btnTypeLine;
	private LinearLayout btnDateLine;
	private TextView btnType;
	private TextView btnDate;
	private TextView tvAddr;
	private ImageView ivLogo;
	private ImageView imgType;
	private VenueDetialModel model;
	private ArrayList<PlaceStatisticModel> mList;
	private Map<Integer, CartAddItemTModel> mCart;
	private VenueDetialManager mManager;
	private ImageAsyncLoader logoLoader;
	private VenuePlaceAdapter mAdapter;
	private Button btnAccounts;
	private TextView tvMartNum;
	private String[] typeItems;
	private String[] dateItems;
	private VenueReserveRModel rModel;
	private ResponseHandler mHandler;
	private int mTypeId;
	private int mDateIndex;
	private PickerDialog<CartAddItemTModel> pickerDialog;
	private String[] typs = {"类型","羽毛球场", "网球场", "乒乓球场", "篮球场", "足球场" };
	public static final int CART_BACK = 0;
	public static final int ORDER_CONFIM_BACK = 1;
	private RelativeLayout rlCart;
	private int cartCount;
	private int curIndex;
	private int venuetype;
	private TextView tvEmeptyView;
	private View vHeader;
	private boolean isTypeOrDate = true;
	private int originalHeaderHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mVenueID = intent.getIntExtra(Extra.SelectedID, 0);
		venuetype = intent.getIntExtra("venuetype", -1);
		if (mVenueID == 0) {
			finish();
			return;
		}
		// mVenueID = 19; // TODO Text Data
		rModel = new VenueReserveRModel(mVenueID);
		mTitle = intent.getStringExtra(Extra.Title);
		logoLoader = BillImageAsyncLoader.getInstance();
		mCart = new HashMap<Integer, CartAddItemTModel>();
		setContentView(R.layout.activity_venue_detial);
		// loader = FaceImageAsyncLoader.getInstance();
		// fragManager = getSupportFragmentManager();
		initView();
		initData();
	}

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.page_tv_title);
		if (!TextUtils.isEmpty(mTitle)) {
			tvTitle.setText(mTitle);
		}
		// tvEmeptyView = (TextView) findViewById(R.id.tv_emptyview);
		btnAccounts = (Button) findViewById(R.id.venue_detial_btn_accounts);
		tvMartNum = (TextView) findViewById(R.id.venue_detial_tv_place_num);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.page_btn_right).setOnClickListener(this);
		rlCart = (RelativeLayout) findViewById(R.id.venue_detial_rl_cart);
		rlCart.setOnClickListener(this);
		// rlCart.setEnabled(false);
		btnAccounts.setOnClickListener(this);

		vHeader = View.inflate(context, R.layout.header_venue_detial, null);
		initHeader(vHeader);
		mListView = (ListView) findViewById(R.id.venue_detial_lv);
		// mListView.setEmptyView(tvEmeptyView);
		mListView.addHeaderView(vHeader, null, false);
		mListView.addFooterView(new View(context));
		mList = new ArrayList<PlaceStatisticModel>();
		mAdapter = new VenuePlaceAdapter(context, mList);
		mListView.setAdapter(mAdapter);
		mListView.setSelector(R.color.gray_background);
		mListView.setItemsCanFocus(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		NetCache.clearCaches();
	}

	private void initHeader(View v) {
		tvEmeptyView = (TextView) v.findViewById(R.id.header_empty);
		tvName = (TextView) v.findViewById(R.id.venue_detial_tv_name);
		tvValue = (TextView) v.findViewById(R.id.venue_detial_tv_markvalue);
		tvType = (TextView) v.findViewById(R.id.venue_detial_tv_service_type);
		imgType = (ImageView) v.findViewById(R.id.venue_detial_img_type);
		tvTime = (TextView) v.findViewById(R.id.venue_detial_tv_business_time);
		tvPhone = (TextView) v.findViewById(R.id.venue_detial_tv_phone);
		tvAddr = (TextView) v.findViewById(R.id.venue_detial_tv_addr);
		btnTypeLine = (LinearLayout) v
				.findViewById(R.id.venue_detial_btn_types);
		btnDateLine = (LinearLayout) v
				.findViewById(R.id.venue_detial_btn_dates);
		btnType = (TextView) v.findViewById(R.id.venue_detial_btn_type);
		btnDate = (TextView) v.findViewById(R.id.venue_detial_btn_date);
		ivLogo = (ImageView) v.findViewById(R.id.venue_detial_iv_logo);
		tvAddr.setOnClickListener(this);
		btnTypeLine.setOnClickListener(this);
		btnDateLine.setOnClickListener(this);
	}

	private void initData() {
		mManager = VenueDetialManager.getInstance();
		mManager.getDetail2(mVenueID, venuetype, new CallBack<String>() {

			@Override
			public void onCallBack(boolean isSuccess, String data) {
				if (isSuccess) {
					model = mManager.getData();
					originalHeaderHeight = vHeader.getHeight();
					if (model.getReserveinfolist().size()==0) {
						ViewGroup.LayoutParams lp = vHeader.getLayoutParams();
						if (lp == null) {
							lp = new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);
						}
						lp.height = mListView.getHeight();
						vHeader.setLayoutParams(lp);
						if (model.getContractState()==2) {
							tvEmeptyView.setText("很抱歉，该场馆暂不提供网上预订服务");
						}else if(model.isIsreleaseplace()){
							if (isTypeOrDate) {
								tvEmeptyView.setText("该场馆暂未发布场次，可电话进行咨询预订");
							}else {
								tvEmeptyView.setText("该日期下场次已经售完或者已被商家下架");
							}
						}
						tvEmeptyView.setVisibility(View.VISIBLE);
					}else {
						tvEmeptyView.setVisibility(View.GONE);
						ViewGroup.LayoutParams lp = vHeader.getLayoutParams();
						if (lp == null) {
							lp = new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);
						}
						lp.height = originalHeaderHeight;
						vHeader.setLayoutParams(lp);
					}
					cartCount = model.getPlaceCount();
					if (model.getPlaceCount() == 0) {
						tvMartNum.setVisibility(View.GONE);
						rlCart.setEnabled(false);
						btnAccounts.setEnabled(false);
					} else {
						tvMartNum.setVisibility(View.VISIBLE);
						tvMartNum.setText(model.getPlaceCount() + "");
						rlCart.setEnabled(true);
						btnAccounts.setEnabled(true);
					}
					tvTitle.setText(model.getFullname());
					tvName.setText(model.getFullname());

					float remark = model.getRemark();
					if (remark > 0f) {
						tvValue.setText(model.getRemark() + "分");
					} else {
						tvValue.setText("");
					}
					tvType.setText(VenueEngine.getServiceType(model
							.getServicetypeList()));
					String phone = model.getTelephone();
					if (!TextUtils.isEmpty(phone)) {
						tvPhone.setText(phone);
					}
					tvAddr.setText(model.getAddress());
					String time = CalendarUtils.formatHM(model
							.getBusinessstart())
							+ "-"
							+ CalendarUtils.formatHM(model.getBusinessend());
					tvTime.setText(time);
					if (venuetype != -1) {
						L.i(Const.ZL,"VenueType = "+venuetype+" Type = "+VenueEngine.getPlaceType(venuetype));
						btnType.setText(VenueEngine.getPlaceType(venuetype));
						mTypeId = venuetype;
					} else {
						if (model.getPlacetypelist().length==0||(model.getPlacetypelist().length==1&&model.getPlacetypelist()[0]==0)) {
							btnType.setText("类型");
						}else {
							if (model.getPlacetypelist().length>=2&&model.getPlacetypelist()[0]==0) {
								mTypeId = model.getPlacetypelist()[1];
							}else {
								mTypeId = model.getPlacetypelist()[0];
							}
							L.i(Const.ZL,"VenueType = "+venuetype+" Type = "+VenueEngine.getPlaceType(mTypeId));
							btnType.setText(VenueEngine.getPlaceType(mTypeId));
						}
					}
					btnDate.setText("今天");
					logoLoader.loadBitmapByID(model.getLogourl(), ivLogo);
					if (model.getReserveinfolist() != null
							&& !model.getReserveinfolist().isEmpty()) {
						mList.clear();
						mList.addAll(model.getReserveinfolist());
						mAdapter.notifyDataSetChanged();
					} else {
						tvEmeptyView.setVisibility(View.VISIBLE);
						switch (model.getContractState()) {
						case 1:
							if (!model.isIsreleaseplace()) {
								tvEmeptyView.setText("该场馆暂未发布场次，可电话进行咨询预订");
							}
							break;
						case 2:
							tvEmeptyView.setText("很抱歉，该场馆暂不提供网上预订服务");
							break;
						}
					}
					// TODO [0,2]

					// VenueEngine.getPlaceTypes(model.getPlacetypelist());
					int length = model.getPlacetypelist().length;
					String[] types = new String[length];
					for (int i = 0; i < length; i++) {
						types[i] = typs[model.getPlacetypelist()[i]];
						Log.i("互邀types", "" + types[i].toString());
					}
					typeItems = types;
					Log.i("互邀!!!@@@@@@@@@@", "InitData" + typeItems.length);
					dateItems = CalendarUtils.getSevenDays();
					Log.i("互邀----++", "else ---->");
					if (!((typeItems.length == 1) || (typeItems.length == 0)))
						imgType.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void getVenueReserve(boolean isType) {
		isTypeOrDate = isType;
		rModel.setPlacetype(mTypeId);
		rModel.setDatetime(mDateIndex);
		Net.request(rModel, WebApi.Venue.GetVenueReserve, getResponseHandler());
	}

	private ResponseHandler getResponseHandler() {
		if (mHandler == null) {
			mHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<PlaceStatisticModel> list = JSON.getObject(
							content, VenueDetialModel.class)
							.getReserveinfolist();
					mList.clear();
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
					if (mList.size() == 0) {
						// LinearLayout.LayoutParams lp =
						// (android.widget.LinearLayout.LayoutParams)
						// vHeader.getLayoutParams();
						// lp = new
						// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						// LinearLayout.LayoutParams.MATCH_PARENT);
						// vHeader.setLayoutParams(lp);
						ViewGroup.LayoutParams lp = vHeader.getLayoutParams();
						if (lp == null) {
							lp = new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);
						}
						lp.height = mListView.getHeight();
						vHeader.setLayoutParams(lp);
						// int childMeasureWidth =
						// ViewGroup.getChildMeasureSpec(0, 0, lp.width);
						// int childMeasureheight;
						//
						// if (lp.height > 0) {
						// childMeasureheight =
						// MeasureSpec.makeMeasureSpec(lp.height,
						// MeasureSpec.EXACTLY);
						// } else {
						// childMeasureheight = MeasureSpec.makeMeasureSpec(0,
						// MeasureSpec.UNSPECIFIED);
						// }
						// mListView.measure(childMeasureWidth,
						// childMeasureheight);
						if (model.getContractState()==2) {
							tvEmeptyView.setText("很抱歉，该场馆暂不提供网上预订服务");
						}else if(model.isIsreleaseplace()){
							if (isTypeOrDate) {
								tvEmeptyView.setText("该场馆暂未发布场次，可电话进行咨询预订");
							}else {
								tvEmeptyView.setText("该日期下场次已经售完或者已被商家下架");
							}
						}
						tvEmeptyView.setVisibility(View.VISIBLE);
						// switch (model.getContractState()) {
						// case 1:
						// if (!model.isIsreleaseplace()) {
						// tvEmeptyView.setText("该场馆暂未发布场次，可电话进行咨询预订");
						// }
						// break;
						// case 2:
						// tvEmeptyView.setText("很抱歉，该场馆暂不提供网上预订服务");
						// break;
						// }
					} else {
						tvEmeptyView.setVisibility(View.GONE);
						ViewGroup.LayoutParams lp = vHeader.getLayoutParams();
						if (lp == null) {
							lp = new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);
						}
						lp.height = originalHeaderHeight;
						vHeader.setLayoutParams(lp);
					}
				}
			};
		}
		return mHandler;
	}

	private CartAddItemTModel getCartItem(PlaceStatisticModel data) {
		int placeID = data.getPlacestatisticid();
		boolean isNotContain = !mCart.containsKey(placeID);
		if (isNotContain) {
			return new CartAddItemTModel(mVenueID, data);
		} else {
			return mCart.get(placeID);
		}
	}

	private void addToCart(int value, CartAddItemTModel cartItem) {
		int placeID = cartItem.getPlacestatisticid();
		boolean isNotContain = !mCart.containsKey(placeID);
		// if (isNotContain) {
		// TODO 可以在这个方法中添加动画效果
		// cartItem.setCount(value);
		mCart.put(placeID, cartItem);
		cartCount += value;
		// TODO 调用添加接口
		// }else {
		//
		// }
		VenueEngine.addToCart(value, cartItem);
		// else if (value != cartItem.getCount()) {
		// // cartItem.setCount(value);
		// VenueEngine.addToCart(value, cartItem);
		//
		// cartCount += (value - cartItem.getCount());
		// // TODO 调用修改接口
		// // Toast.show("update");
		// }
		refreshCartShow();
	}

	private void removeFromCart(CartAddItemTModel cartItem) {
		int plcceID = cartItem.getPlacestatisticid();
		if (mCart.containsKey(plcceID)) {
			mCart.remove(plcceID);
			refreshCartShow();
			// TODO 调删除接口
		}
	}

	private void refreshCartShow() {
		if (mCart.isEmpty()) {
			btnAccounts.setEnabled(false);
			rlCart.setEnabled(false);
			tvMartNum.setVisibility(View.GONE);
		} else {
			btnAccounts.setEnabled(true);
			rlCart.setEnabled(true);
			tvMartNum.setText(cartCount + "");
			tvMartNum.setVisibility(View.VISIBLE);
		}
	}

	private void initPicker(int index) {
		if (null == pickerDialog) {
			pickerDialog = new PickerDialog<CartAddItemTModel>(context);
			pickerDialog.setRightButton("取消", null);
			pickerDialog.setLeftButton("确定", this);
		}
		PlaceStatisticModel item = mList.get(index);
		HorizontalPicker picker = pickerDialog.getPicker();
		picker.setMaxValue(item.getRemainder());
		picker.setMinValue(1);
		CartAddItemTModel cartItem = getCartItem(item);
		picker.setValue(1);
		pickerDialog.setTag(cartItem);
		pickerDialog.show();
	}

	// TODO 13011119560
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			// NetCache.clearCaches();
			finish();
			break;
		case R.id.page_btn_right:
			startActivity(VenueMoeInfoActivity.class);
			break;
		case R.id.venue_detial_tv_addr:
			// Toast.show("地图");
			SiteVenueItem item = new SiteVenueItem();
			item.setVenueid(model.getVenueid());
			item.setAddress(model.getAddress());
			item.setLatitude(model.getLatitude());
			item.setLongitude(model.getLongitude());
			if ((int) item.getLatitude() == 0 || (int) item.getLongitude() == 0) {
				break;
			}
			Intent intentAddr = new Intent(this, VenueAddrMapActivity.class);
			intentAddr.putExtra(Extra.SelectedItem, item);
			startActivity(intentAddr);

			break;
		case R.id.venue_detial_btn_types:
			Log.i("互邀======!!!!1", "" + model.getServicetypeList().length);
			if (!((typeItems.length == 1) || (typeItems.length == 0))) {
				Log.i("互邀======!!!!2", "" + model.getServicetypeList().length);
				Intent typeIntents = new Intent(this,
						SelectSingleWheelActivity.class);
				typeIntents.putExtra(Extra.ArrayStr, typeItems);
				startActivityForResult(typeIntents, R.id.venue_detial_btn_types);
			}
			break;
		case R.id.venue_detial_btn_dates:
			Intent dateIntents = new Intent(this,
					SelectSingleWheelActivity.class);
			dateIntents.putExtra(Extra.ArrayStr, dateItems);
			startActivityForResult(dateIntents, R.id.venue_detial_btn_dates);
			break;
		case R.id.venue_detial_rl_cart:
			// startActivity(CartActivity.class);
		case R.id.venue_detial_btn_accounts:
			Intent cartIntent = new Intent(this, CartActivity.class);
			// cartIntent.putExtra("mCart", mCart.size());
			startActivityForResult(cartIntent, CART_BACK);
			break;
		case R.id.place_sub_btn_order:
			int index = (Integer) v.getTag();
			initPicker(index);
			curIndex = index;
			break;
		case PickerDialog.ID_LEFT_BUTTON:
			PickerDialog<CartAddItemTModel> dialog = (PickerDialog<CartAddItemTModel>) v
					.getTag();
			int value = dialog.getPicker().getValue();
			CartAddItemTModel cartItem = dialog.getTag();
			addToCart(value, cartItem);
			PlaceStatisticModel curItem = mList.get(curIndex);
			curItem.setRemainder(curItem.getRemainder() - value);
			mAdapter.notifyDataSetChanged();
			if (value == 0) {
				removeFromCart(cartItem);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		NetCache.clearCaches();
		mManager.getDetial(mVenueID, new CallBack<String>() {
			@Override
			public void onCallBack(boolean isSuccess, String data) {
				if (isSuccess) {
					model = mManager.getData();
					if (model.getPlaceCount() == 0) {
						tvMartNum.setVisibility(View.GONE);
						rlCart.setEnabled(false);
						btnAccounts.setEnabled(false);
					} else {
						tvMartNum.setVisibility(View.VISIBLE);
						tvMartNum.setText(model.getPlaceCount() + "");
						rlCart.setEnabled(true);
						btnAccounts.setEnabled(true);
					}
					cartCount = model.getPlaceCount();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case R.id.venue_detial_btn_types:
				int typeid = data.getIntExtra(Extra.SelectedItem,
						State.Selected_cancle) + 1;
				L.i(Const.ZL,"TypeId--"+typeid);
				if (typeid > 0 && typeid != mTypeId) {
					mTypeId = typeid;
					btnType.setText(data.getStringExtra(Extra.SelectedItemStr));
					getVenueReserve(true);
				}
				break;
			case R.id.venue_detial_btn_dates:
				int dateIndex = data.getIntExtra(Extra.SelectedItem,
						State.Selected_cancle);
				if (dateIndex >= 0 && dateIndex != mDateIndex) {
					mDateIndex = dateIndex;
					btnDate.setText(data.getStringExtra(Extra.SelectedItemStr));
					getVenueReserve(false);
				}
				break;
			default:
				break;
			}
		} else {
			if (data != null) {
				switch (data.getIntExtra(Extra.SelectedItemStr, 0)) {
				case CART_BACK:
					mManager.getDetial(mVenueID, new CallBack<String>() {
						@Override
						public void onCallBack(boolean isSuccess, String data) {
							if (isSuccess) {
								model = mManager.getData();
								if (model.getPlaceCount() == 0) {
									tvMartNum.setVisibility(View.GONE);
									rlCart.setEnabled(false);
									btnAccounts.setEnabled(false);
								} else {
									tvMartNum.setVisibility(View.VISIBLE);
									tvMartNum.setText(model.getPlaceCount()
											+ "");
									rlCart.setEnabled(true);
									btnAccounts.setEnabled(true);
								}
							}
						}
					});
					// int number = data.getIntExtra("number", 1);
					// ArrayList<Integer> delList = data
					// .getIntegerArrayListExtra("delCartList");
					// ArrayList<CartSubItemModel> cartList =
					// (ArrayList<CartSubItemModel>) data
					// .getSerializableExtra("cartList");
					// int amount = 0;
					// for (int i = 0; i < cartList.size(); i++) {
					// if (mCart.containsKey(cartList.get(i)
					// .getPlacestatisticid())) {
					// CartAddItemTModel cartItem = mCart.get(cartList
					// .get(i).getPlacestatisticid());
					// amount += cartItem.getCount();
					// } else {
					// PlaceStatisticModel placeStatisticModel = new
					// PlaceStatisticModel();
					// placeStatisticModel.setPlacestatisticid(cartList
					// .get(i).getPlacestatisticid());
					// CartAddItemTModel cartItem =
					// getCartItem(placeStatisticModel);
					// cartItem.setPrice(cartList.get(i).getDiscountprice());
					// cartItem.setCount(cartList.get(i).getReservecount());
					// cartItem.setPlacetime(cartList.get(i).getPlacetime());
					// cartItem.setPlacetypedetail(cartList.get(i).getPlacetypedetail());
					// mCart.put(cartList.get(i).getPlacestatisticid(),
					// cartItem);
					// amount += cartItem.getCount();
					// }
					// }
					// cartCount = amount;
					// if (cartCount==0) {
					// btnAccounts.setEnabled(false);
					// rlCart.setEnabled(false);
					// tvMartNum.setVisibility(View.GONE);
					// }else {
					// tvMartNum.setText(cartCount + "");
					// }
					//
					// else {
					// if (number == 0) {
					// mCart.clear();
					// cartCount = 0;
					// refreshCartShow();
					// } else {
					// for (int i = 0; i < delList.size(); i++) {
					// CartAddItemTModel cartItem = mCart.get(delList
					// .get(i));
					// removeFromCart(cartItem);
					// cartCount -= cartItem.getCount();
					// }
					// tvMartNum.setText(cartCount + "");
					// }
					// }
					break;
				case ORDER_CONFIM_BACK:
					mCart.clear();
					cartCount = 0;
					refreshCartShow();
					break;
				}
			}

		}
	}
}
