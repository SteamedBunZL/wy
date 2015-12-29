package com.whoyao.venue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppException;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.BasicActivity;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.PickerDialog;
import com.whoyao.utils.JSON;
import com.whoyao.venue.engine.CartAdapter;
import com.whoyao.venue.engine.CartEngine;
import com.whoyao.venue.model.CartRModel;
import com.whoyao.venue.model.CartSubItemModel;
import com.whoyao.venue.model.VenueCartModel;

/**
 * @author hyh creat_at：2014-2-26-上午11:55:20
 */
public class CartActivity extends BasicActivity implements OnClickListener {
	private ListView mListview;
	private BaseAdapter mAdapter;
	private TextView tvHeader;
	private View vFooter;
	private TextView tvFooterCount;
	private TextView tvFooterAmount;
	private PickerDialog<CartSubItemModel> pickerDialog;
	private ResponseHandler updateCartHandler;
	private List<VenueCartModel> mList;
	private ResponseHandler mHandler;
	private CartRModel mCartModel;
	private MessageDialog msgDialog;
	private ResponseHandler delCartHandler;
	private ResponseHandler orderHandler;
	private int totalAmount;
	private int id;
	private Button btnAccount;
	private ArrayList<Integer> delCartIdList = new ArrayList<Integer>();
	private ArrayList<CartSubItemModel> cartList = new ArrayList<CartSubItemModel>();
	private int delId;
	private int updateValue2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_venue_cart);
		initView();
		initData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent intent = new Intent();
		intent.putExtra(Extra.SelectedItemStr,
				VenueDetialActivity.ORDER_CONFIM_BACK);
		setResult(RESULT_CANCELED, intent);
		finish();
	}

	private void initView() {
		// mCart = cartIntent.getIntExtra("mCart", -1);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		btnAccount = (Button) findViewById(R.id.venue_cart_btn_accounts);
		btnAccount.setOnClickListener(this);
		mListview = (ListView) findViewById(R.id.venue_cart_lv);
		tvHeader = (TextView) View.inflate(context, R.layout.header_venue_cart,
				null);
		vFooter = View.inflate(context, R.layout.footer_venue_cart, null);
		tvFooterCount = (TextView) vFooter
				.findViewById(R.id.footer_cart_tv_count);
		tvFooterAmount = (TextView) vFooter
				.findViewById(R.id.footer_cart_tv_amount);
		mListview.addHeaderView(tvHeader, null, false);
		mListview.addFooterView(vFooter, null, false);
		mList = new ArrayList<VenueCartModel>();
		mAdapter = new CartAdapter(context, mList);
		mListview.setAdapter(mAdapter);
	}

	private void initData() {

		mHandler = new ResponseHandler() {
			@Override
			public void onSuccess(String content) {
				mCartModel = JSON.getObject(content, CartRModel.class);
				CartEngine.initVenueCart(mCartModel);
				refreshHeaderAndFooter(mCartModel);
				mList.clear();

				mList.addAll(mCartModel.getVenueCartList());
				for (int i = 0; i < mList.size(); i++) {
					VenueCartModel venueCartModel = mList.get(i);
					List<CartSubItemModel> subList = mList.get(i).getSubList();
					for (int j = 0; j < subList.size(); j++) {
						CartSubItemModel cartSubItemModel = subList.get(j);
						cartList.add(cartSubItemModel);
					}

				}
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
				if (statusCode == 400) {
					mList.clear();
					btnAccount.setEnabled(false);
					tvHeader.setVisibility(View.INVISIBLE);
					vFooter.setVisibility(View.INVISIBLE);
					mAdapter.notifyDataSetChanged();
				}
			}

		};

		Net.request(new RequestParams(), WebApi.Venue.GetVenueCat, mHandler);
	}

	private MessageDialog getDialog() {
		if (msgDialog == null) {
			msgDialog = new MessageDialog(context);
			msgDialog.setMessage("失效场地不参加结算，部分场地数量不足，请修改场地数量后完成预订。");
			msgDialog.setLeftButton("确定", null);
		}
		return msgDialog;
	}

	private void refreshHeaderAndFooter(CartRModel model) {
		totalAmount = CartEngine.countTotalAmount(model);
		int validCount = CartEngine.countValidItem(model);
		int itemCount = CartEngine.countTotalItem(model);
		tvHeader.setText("总共场地" + itemCount + "块");
		tvFooterCount.setText("结算数量：" + validCount + "　总价：");
		tvFooterAmount.setText("￥" + totalAmount);
	}

	private void updateCartItem(int updateValue, final CartSubItemModel subItem) {
		NetCache.clearCaches();
		if (updateCartHandler == null) {
			updateCartHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					if (TextUtils.isEmpty(content)) {
						subItem.setReservecount(updateValue2);
						subItem.setAmount(subItem.getDiscountprice()
								* updateValue2);
						subItem.setState(0);
						mAdapter.notifyDataSetChanged();
						refreshHeaderAndFooter(mCartModel);
					} else {
						// initData();
						subItem.setReservecount(updateValue2);
						subItem.setAmount(subItem.getDiscountprice()
								* updateValue2);
						subItem.setState(3);
						mAdapter.notifyDataSetChanged();
						refreshHeaderAndFooter(mCartModel);
					}
				}
			};
		}
		updateValue2 = updateValue;
		RequestParams params = new RequestParams();
		params.put("count", updateValue2 + "");
		params.put("cartdetailid", subItem.getCartdetailid() + "");
		Net.request(params, WebApi.Venue.UpdateVenueCat, updateCartHandler);
	}

	private void delCartItem(final int cartdetailid) {
		if (delCartHandler == null) {
			delCartHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					initData();
				}
			};
		}
		Net.request("cartdetailid", cartdetailid + "",
				WebApi.Venue.DeleteVenueCat, delCartHandler);
	}

	private void creatOrder() {
		if (orderHandler == null) {
			orderHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					String result = null;
					try {
						JSONObject jo = new JSONObject(content);
						result = jo.getString("result");
					} catch (JSONException e) {
						AppException.handle(e);
					}
					if (TextUtils.isEmpty(result)) {
						initData();
					} else {
						Intent intent = new Intent(context,
								OrderConfirmActivity.class);
						intent.putExtra(Extra.SelectedID, result);
						// intent.putExtra(Extra.Data, mCartModel);
						startActivityForResult(intent,
								VenueDetialActivity.ORDER_CONFIM_BACK);
					}
				}
			};
		}
		WebView webView = new WebView(context);
		// webView.
		RequestParams params = new RequestParams();
		params.put("totalprice", totalAmount + "");
		params.put("cartdetailids", CartEngine.getCartSubItemIDs(mCartModel));
		Net.request(params, WebApi.Venue.CreatOrder, orderHandler);
	}

	public void backCart() {
		// NetCache.clearCaches();
		Intent cartIntent = new Intent();
		cartIntent.putExtra(Extra.SelectedItemStr,
				VenueDetialActivity.CART_BACK);
		cartIntent.putExtra("number", mList.size());
		cartIntent.putExtra("delCartList", delCartIdList);
		cartIntent.putExtra("cartList", (Serializable) cartList);
		setResult(RESULT_CANCELED, cartIntent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		backCart();
		finish();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			backCart();
			finish();
			break;
		case R.id.venue_cart_btn_accounts:
			if (mCartModel!=null) {
				if (mCartModel.getCheckError().length==1&&mCartModel.getCheckError()[0].getState()==4) {
					creatOrder();
				}else if(mCartModel.getCheckError()==null||mCartModel.getCheckError().length==0){
					creatOrder();
				}else {
					getDialog().show();
				}
			}
//			if (mCartModel != null
//					&& (mCartModel.getCheckError() == null || mCartModel
//							.getCheckError().length == 0)) {
//				
//				// finish();
//			} else {
//				
//			}
			break;
		case PickerDialog.ID_LEFT_BUTTON:
			pickerDialog = (PickerDialog<CartSubItemModel>) v.getTag();
			CartSubItemModel subItem = pickerDialog.getTag();
			int value = pickerDialog.getPicker().getValue();
			System.out.println("picketvalue----" + value);
			updateCartItem(value, subItem);

			break;
		case R.id.cart_sub_btn_del:
			// Toast.show("del");
			id = (Integer) v.getTag(R.id.btn_inform_agree);
			delId = (Integer) v.getTag(R.id.btn_inform_disagree);
			delCartIdList.add(delId);
			for (int i = 0; i < cartList.size(); i++) {
				if (cartList.get(i).getPlacestatisticid() == delId) {
					cartList.remove(i);
				}
			}
			delCartItem(id);
			break;
		default:
			break;
		}
	}
}
