package com.whoyao.activity;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.Const.KEY;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.HotTagAdapter;
import com.whoyao.adapter.TagListAdapter;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.HotTagModel;
import com.whoyao.model.TagItemModel;
import com.whoyao.model.TagModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 设置我的标签
 * 
 * @author hyh creat_at：2013-8-17-下午6:50:40
 */
public class MyTagActivity extends BasicActivity implements OnClickListener {
	private ListView lvEvent;
	private ListView lvInterest;
	private ArrayList<TagItemModel>[] hotTags;
	/** 换一换的次数 */
	private int nextTiems = 0;
	private TextView tvIntrestLabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_tab);
		initView();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setTags();
	}

	private void initView() {
		lvEvent = (ListView) findViewById(R.id.mytag_lv_event_tag);
		lvInterest = (ListView) findViewById(R.id.mytag_lv_interest);
		tvIntrestLabel = (TextView) findViewById(R.id.mytag_tv_intrest_label);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.mytag_btn_add).setOnClickListener(this);
		findViewById(R.id.mytag_tv_next_interest).setOnClickListener(this);
		hotTags = new ArrayList[5];
		lvEvent.setAdapter(new TagListAdapter(
				MyinfoManager.getManager().eventTags));
		// lvEvent.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// CheckBox cb = (CheckBox) view.findViewById(R.id.item_cb_tag);
		// Toast.show("checi item");
		//
		// }
		// });
		// lvInterest.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// CheckBox cb = (CheckBox) view.findViewById(R.id.item_cb_tag);
		//
		// Toast.show("checi item");
		// }
		// });

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			onBack();
			break;
		case R.id.mytag_btn_add:
			if (9 < MyinfoManager.getManager().interestTags.size()) {
				Toast.show(R.string.warn_tag_full);
			} else {
				AppContext.startAct(MyTagAddActivity.class);
			}
			break;
		case R.id.mytag_tv_next_interest:
			nextTiems++;
			setTags();
			break;
		default:
			break;
		}
	}

	private void setTags() {
		if (5 == nextTiems || 0 == nextTiems) {
			nextTiems = 0;
			tvIntrestLabel.setText("我的兴趣标签");
			ArrayList<TagItemModel> interestTags = MyinfoManager.getManager().interestTags;
			if (0 < interestTags.size()) {
				lvInterest.setAdapter(new TagListAdapter(interestTags));
				return;
			} else {
				nextTiems++;
				tvIntrestLabel.setText("您可能感兴趣的其他标签");
			}
		} else {
			tvIntrestLabel.setText("您可能感兴趣的其他标签");
			ArrayList<TagItemModel> tags = MyinfoManager.getManager().interestTags;
			for (int i = 0; i < tags.size(); i++) {
				if (!tags.get(i).isSelected()) {
					MyinfoManager.getManager().interestTags.remove(i);
				}
			}
		}
		if (null != hotTags[nextTiems]) {
			lvInterest.setAdapter(new HotTagAdapter(hotTags[nextTiems]));
		} else {
			MyinfoManager.getManager().getNextTags(nextTiems,
					new CallBack<HotTagModel>() {
						@Override
						public void onCallBack(HotTagModel data) {
							hotTags[nextTiems] = convertTagList(data.HotTag);
							lvInterest.setAdapter(new HotTagAdapter(
									hotTags[nextTiems]));
						}
					});
		}
	}

	@Override
	protected boolean onBack() {
		String newTagIds = MyinfoManager.getManager().getTagIds();
		String oldTagIds = getOldTagIds();
		if (TextUtils.isEmpty(newTagIds)) {
			Net.request(null, WebApi.User.DeleteAllTag,
					new ResponseHandler(true) {
						@Override
						public void onSuccess(String content) {

							NetCache.clearCaches();
							MyinfoManager.getManager().getMyDetil(true,
									new CallBack<UserDetialModel>() {
										@Override
										public void onCallBack(boolean isSuccess) {
											if (isSuccess) {
//												Toast.show("保存成功");
											}
											finish();
										}
									});

						}

						@Override
						public void onFailure(Throwable e, int statusCode,
								String content) {
							if (statusCode == 417) {
								NetCache.clearCaches();
								MyinfoManager.getManager().getMyDetil(true,
										new CallBack<UserDetialModel>() {
											@Override
											public void onCallBack(
													boolean isSuccess) {
												if (isSuccess) {
//													Toast.show("保存成功");
												}
												finish();
											}
										});
							} else {
								Toast.show(R.string.warn_netrequest_failure);
								finish();
							}
						}

					});
			return true;
		}
		if (!newTagIds.equals(oldTagIds)) {
			String tagNames = MyinfoManager.getManager().getTagNames();
			RequestParams params = new RequestParams();
			params.put(KEY.TagIDs, newTagIds);
			params.put(KEY.TagNames, tagNames);
			Net.request(params, WebApi.User.UpTags, new ResponseHandler() {
				@Override
				public void onSuccess(String content) {
					NetCache.clearCaches();
					MyinfoManager.getManager().getMyDetil(true,
							new CallBack<UserDetialModel>() {
								@Override
								public void onCallBack(boolean isSuccess) {
									if (isSuccess) {
										Toast.show("保存成功");
									}
									finish();
								}
							});
				}

				@Override
				public void onFailure(Throwable error, String content) {
					finish();
				}
			});
		} else {
			finish();
		}
		return true;
	}

	private ArrayList<TagItemModel> convertTagList(List<TagModel> list) {
		ArrayList<TagItemModel> iList = new ArrayList<TagItemModel>();
		for (TagModel tag : list) {
			iList.add(new TagItemModel(tag, false));
		}
		return iList;
	}

	private String getOldTagIds() {
		String tagIds = "";
		for (TagModel tag : MyinfoManager.userTag) {
			tagIds = tagIds + tag.getTagId() + ",";
		}
		if (tagIds == null || tagIds.length() == 0) {
			return tagIds;
		}
		return tagIds.substring(0, tagIds.length() - 1);
	}

	// @Override
	// public String toString() {
	// return "设置我的标签";
	// }
}
