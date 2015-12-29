package com.whoyao.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.Header;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.activity.EventDetialActivity;
import com.whoyao.activity.MainActivity;
import com.whoyao.activity.MyInviteDetailActivity;
import com.whoyao.activity.PrivateDetailActivity;
import com.whoyao.activity.SelectCityActivity;
import com.whoyao.activity.UserOtherDetialActivity;
import com.whoyao.activity.UserSelfDetialActivity;
import com.whoyao.adapter.EventListAdapter;
import com.whoyao.adapter.MessageInviteCreateListAdapter;
import com.whoyao.adapter.MessageNoticeAdapter;
import com.whoyao.adapter.MessagePrivateCreateListAdapter;
import com.whoyao.adapter.TopicListAdapter;
import com.whoyao.engine.event.EventEngine;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.EventListItem;
import com.whoyao.model.FriendAgreeOrDisAgreeTModel;
import com.whoyao.model.HomePageRModel;
import com.whoyao.model.InviteData;
import com.whoyao.model.InviteListItem;
import com.whoyao.model.MessageIsReadTModel;
import com.whoyao.model.NoticeData;
import com.whoyao.model.NoticeListItem;
import com.whoyao.model.PrivateListItem;
import com.whoyao.model.TopicItemRModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.topic.TopicDetialActivity;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author hyh creat_at：2013-9-3-下午4:42:56
 */
public class HomeFragment extends Fragment implements OnClickListener, OnItemClickListener {

	private View contentView;
	private ListView lvPrivate;
	private ListView lvEvent;
	private BaseAdapter mAdapterMsg;
	private LayoutInflater inflater;
	private BaseAdapter mAdapterEvent;
	private ListView lvMessage0;
	private MessagePrivateCreateListAdapter mAdapterPrivate;
	private MessageInviteCreateListAdapter mAdapterInvite;
	private MessageNoticeAdapter mAdapterNotice;
	private List<PrivateListItem> mListPrivate;
	private List<InviteListItem> mListInvite;
	private List<NoticeListItem> mListNotice;
	private List<EventListItem> mListEvent;
	private List<TopicItemRModel> mListTopic;
	private ListView lvInvite;
	private ListView lvNotice;
	private ListView lvTopic;
	private View vAddTag;
	private HomePageRModel rModel;
	private View emptyView;
	private View emptyContent;
	private boolean isChangeToEvent;
	private TopicListAdapter mAdapterTopic;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (null == contentView) {
			contentView = inflater.inflate(R.layout.frag_home, container, false);
			initView();
			initData();
		} else {
			((ViewGroup) contentView.getParent()).removeView(contentView);
		}
		return contentView;
	}

	private void initView() {
		contentView.findViewById(R.id.home_ll_add_tag);
		lvEvent = (ListView) contentView.findViewById(R.id.home_lv_event);
		lvPrivate = (ListView) contentView.findViewById(R.id.home_lv_privateletter);
		lvInvite = (ListView) contentView.findViewById(R.id.home_lv_invite);
		lvNotice = (ListView) contentView.findViewById(R.id.home_lv_notice);
		lvTopic = (ListView) contentView.findViewById(R.id.home_lv_topic);
		lvEvent.setSelector(android.R.color.transparent);
		lvPrivate.setSelector(android.R.color.transparent);
		lvInvite.setSelector(android.R.color.transparent);
		lvNotice.setSelector(android.R.color.transparent);
		lvTopic.setSelector(android.R.color.transparent);

		lvEvent.setOnItemClickListener(this);
		lvPrivate.setOnItemClickListener(this);
		lvInvite.setOnItemClickListener(this);
		lvNotice.setOnItemClickListener(this);
		lvTopic.setOnItemClickListener(this);

		emptyView = contentView.findViewById(R.id.home_empty_event);
		emptyContent = contentView.findViewById(R.id.event_ll_empty);
		emptyView.findViewById(R.id.event_empty_other_area).setOnClickListener(this);
		emptyView.findViewById(R.id.event_empty_add_event).setOnClickListener(this);
		
		inflater = LayoutInflater.from(getActivity());

		if (UserEngine.isFirstLogin) {
			UserEngine.isFirstLogin = false;
			vAddTag = contentView.findViewById(R.id.home_ll_add_tag);
			vAddTag.setVisibility(View.VISIBLE);
			vAddTag.setOnClickListener(this);

			lvMessage0 = (ListView) contentView.findViewById(R.id.home_lv_message0);
			lvMessage0.setVisibility(View.VISIBLE);
			mAdapterMsg = new BaseAdapter() {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					convertView = inflater.inflate(R.layout.item_my_message_private_content2, null);
					ImageView head = (ImageView) convertView.findViewById(R.id.iv_head);
					head.setImageResource(R.drawable.huyao);
					TextView title = (TextView) convertView.findViewById(R.id.tv_private_title);
					title.setText("系统消息");
					TextView content = (TextView) convertView.findViewById(R.id.tv_private_content);
					content.setText("欢迎你注册互邀网");
					TextView time = (TextView) convertView.findViewById(R.id.tv_private_time);
					Calendar cal = CalendarUtils.getCalendar();
					String timeStr = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
					time.setText(timeStr);
					return convertView;
				}

				@Override
				public long getItemId(int position) {
					return 0;
				}

				@Override
				public Object getItem(int position) {
					return null;
				}

				@Override
				public int getCount() {
					return 1;
				}
			};
			lvMessage0.setAdapter(mAdapterMsg);
			Utils.setListViewHeightForeachChildren(lvMessage0);
		}

		View emptyEvent = contentView.findViewById(R.id.home_empty_event);
		lvEvent.setEmptyView(emptyEvent);

		mListPrivate = new ArrayList<PrivateListItem>();
		mListInvite = new ArrayList<InviteListItem>();
		mListNotice = new ArrayList<NoticeListItem>();
		mListEvent = new ArrayList<EventListItem>();
		mListTopic = new ArrayList<TopicItemRModel>();

		mAdapterEvent = new EventListAdapter(mListEvent, inflater) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				view.setBackgroundResource(android.R.color.transparent);
				return view;
			}
		};
		mAdapterPrivate = new MessagePrivateCreateListAdapter(mListPrivate, inflater, this) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				view.setBackgroundResource(android.R.color.transparent);
				return view;
			}
		};
		mAdapterInvite = new MessageInviteCreateListAdapter(mListInvite, inflater, this) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				view.setBackgroundResource(android.R.color.transparent);
				return view;
			}
		};
		mAdapterNotice = new MessageNoticeAdapter(mListNotice, inflater, this) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				view.setBackgroundResource(android.R.color.transparent);
				return view;
			}
		};
		mAdapterTopic = new TopicListAdapter(inflater,mListTopic) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				view.setBackgroundResource(android.R.color.transparent);
				return view;
			}
		};

		lvEvent.setAdapter(mAdapterEvent);
		lvPrivate.setAdapter(mAdapterPrivate);
		lvInvite.setAdapter(mAdapterInvite);
		lvNotice.setAdapter(mAdapterNotice);
		lvTopic.setAdapter(mAdapterTopic);

		Utils.setListViewHeightForeachChildren(lvEvent);
		Utils.setListViewHeightForeachChildren(lvPrivate);
		Utils.setListViewHeightForeachChildren(lvInvite);
		Utils.setListViewHeightForeachChildren(lvNotice);
		Utils.setListViewHeightForeachChildren(lvTopic);
	}

	private void initData() {
		Net.request(null, WebApi.Event.getHomePage, new ResponseHandler(true) {
			@Override
			public void onSuccess(String content) {
				rModel = JSON.getObject(content, HomePageRModel.class);
				refreshData(lvEvent, mListEvent, rModel.getActivityListItem());
				refreshData(lvPrivate, mListPrivate, rModel.getPrivateListItem());
				refreshData(lvInvite, mListInvite, rModel.getInviteItem());
				refreshData(lvNotice, mListNotice, rModel.getNoticeItem());
				refreshData(lvTopic, mListTopic, rModel.getTopicListItem());
				
				if (UserEngine.isFirstLogin && lvMessage0 != null) {
					lvMessage0.requestFocus();
				} else {
					lvEvent.requestFocus();
				}
			}
			@Override
			public void onFinish() {
				super.onFinish();
				emptyView.setBackgroundResource(R.drawable.rectangle_radius_white);
				emptyContent.setVisibility(View.VISIBLE);
			}
		});
	}

	private void refreshData(ListView lv,List oldList,List newList){
		oldList.clear();
		if(newList == null || newList.isEmpty()){
			((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
			lv.setVisibility(View.GONE);
		}else{
			oldList.addAll(newList);
			((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
			lv.setVisibility(View.VISIBLE);
			Utils.setListViewHeightForeachChildren(lv);
		}
	}
		
	@Override
	public void onClick(View v) {
		FriendAgreeOrDisAgreeTModel friendModel;
		switch (v.getId()) {
		case R.id.home_ll_add_tag:
			AppContext.startAct(UserSelfDetialActivity.class);
			break;
		case R.id.btn_inform_agree:
			final NoticeListItem item = rModel.getNoticeItem().get((Integer) v.getTag(R.id.btn_inform_agree));
			item.setIsread(2);
			friendModel = new FriendAgreeOrDisAgreeTModel();
			friendModel.setFrienduserid(item.getSenduserid());
			friendModel.setIsagree(1);
			friendModel.setMesscontentid(item.getMessagecontentid());
			Net.cacheRequest(friendModel, WebApi.User.FriendRequestManage, new ResponseHandler(true) {

				@Override
				public void onSuccess(String content) {
					item.setResult(1);
					mAdapterNotice.notifyDataSetChanged();
				}

				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					super.onFailure(e, statusCode, content);
					if (statusCode == 417) {
						onSuccess(content);
					}
				}
			});
			break;
		case R.id.btn_inform_disagree:
			final NoticeListItem item1 = rModel.getNoticeItem().get((Integer) v.getTag(R.id.btn_inform_disagree));
			item1.setIsread(2);
			friendModel = new FriendAgreeOrDisAgreeTModel();
			friendModel.setFrienduserid(item1.getSenduserid());
			friendModel.setIsagree(0);
			friendModel.setMesscontentid(item1.getMessagecontentid());
			Net.cacheRequest(friendModel, WebApi.User.FriendRequestManage, new ResponseHandler(true) {

				@Override
				public void onSuccess(String content) {
					item1.setResult(2);
					mAdapterNotice.notifyDataSetChanged();
				}

				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					super.onFailure(e, statusCode, content);
					if (statusCode == 417) {
						onSuccess(content);
					}
				}
			});
			break;
		case R.id.event_empty_other_area:
			isChangeToEvent = true;
			Intent dataOther = new Intent(getActivity(), SelectCityActivity.class);
			startActivityForResult(dataOther, MainActivity.REQUEST_CODE_CITY);
			((MainActivity)getActivity()).setFragment(EventFragment.class);
			break;
		case R.id.event_empty_add_event:
			EventEngine.publishEvent();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String activtyId = null;
		String inviteId = null;
		MessageIsReadTModel setReadTModel = new MessageIsReadTModel();
		switch (parent.getId()) {
		case R.id.home_lv_event:
			Intent intentEvent = new Intent(getActivity(), EventDetialActivity.class);
			intentEvent.putExtra(Extra.SelectedID, rModel.getActivityListItem().get(position).getActivityid());
			startActivity(intentEvent);
			break;
		case R.id.home_lv_privateletter:
			PrivateListItem itemPrivate = (PrivateListItem) mAdapterPrivate.getItem(position);
			itemPrivate.setUnReadCount(0);
			mAdapterPrivate.notifyDataSetChanged();
			Intent intent = new Intent(getActivity(), PrivateDetailActivity.class);
			if (itemPrivate.getReceiverId() == MyinfoManager.getUserInfo().getUserID()) {
				intent.putExtra(Extra.SelectedID, itemPrivate.getSenderId());
			} else {
				intent.putExtra(Extra.SelectedID, itemPrivate.getReceiverId());
			}
			intent.putExtra(Extra.SelectedItemStr, itemPrivate.getUserName());
			intent.putExtra(Extra.Position, position);
			startActivityForResult(intent, MainActivity.REQUESTCODE_PRIVATE);
			break;
		case R.id.home_lv_invite:
			InviteListItem itemInvite = (InviteListItem) mAdapterInvite.getItem(position);
			Map<String, String> mapType = new TreeMap<String, String>();
			List<InviteData> dataList = new ArrayList<InviteData>();
			dataList = itemInvite.getData();

			if (dataList != null) {
				for (int i = 0; i < dataList.size(); i++) {
					mapType.put(dataList.get(i).getKey(), dataList.get(i).getValue());
				}
				if (mapType.containsKey("ActivityId")) {
					activtyId = mapType.get("ActivityId");
				}
				if (mapType.containsKey("InviteId")) {
					inviteId = mapType.get("InviteId");
				}
			}
			if (itemInvite.getIsread() != 2) {
				setReadTModel.setMessagecontentid(itemInvite.getMessagecontentid());
				Net.request(setReadTModel, WebApi.Message.IsRead, new ResponseHandler(true, position) {
					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {
						super.onSuccess(statusCode, headers, content);
						InviteListItem itemInvite = (InviteListItem) mAdapterInvite.getItem((Integer) params[0]);
						itemInvite.setIsread(2);
						mAdapterInvite.notifyDataSetChanged();
					}
				});
			}
			if (itemInvite.getContenttype() == 17) {
				Intent inviteIntent = new Intent(getActivity(), MyInviteDetailActivity.class);
				inviteIntent.putExtra("inviteid", Integer.parseInt(inviteId));
				startActivity(inviteIntent);
			} else if (itemInvite.getContenttype() == 15) {
				Intent activityInent = new Intent(getActivity(), EventDetialActivity.class);
				activityInent.putExtra(Extra.SelectedID, Integer.parseInt(activtyId));
				startActivity(activityInent);
			}
			break;
		case R.id.home_lv_notice:
			NoticeListItem item = (NoticeListItem) mAdapterNotice.getItem(position);
			Map<String, String> map = new TreeMap<String, String>();
			List<NoticeData> noticeDataList = item.getData();
			if (noticeDataList != null) {
				for (int i = 0; i < noticeDataList.size(); i++) {
					map.put(noticeDataList.get(i).getKey(), noticeDataList.get(i).getValue());
				}
				if (map.containsKey("ActivityId")) {
					activtyId = map.get("ActivityId");
				}
				if (map.containsKey("InviteId")) {
					inviteId = map.get("InviteId");
				}
			}
			item.setIsread(2);
			TextView tvTextView = (TextView) view.findViewById(R.id.tv_inform_name);
			tvTextView.setTextColor(getResources().getColor(R.color.gray_text));
			setReadTModel.setMessagecontentid(item.getMessagecontentid());
			Net.request(setReadTModel, WebApi.Message.IsRead, new ResponseHandler());
			int contentType = item.getContenttype();
			if (contentType==21||contentType==19||contentType==20||contentType==23) {
				Intent intentNotice = new Intent(getActivity(), UserOtherDetialActivity.class);
				int userId = item.getSenduserid();
				if (item.getResult() == 0) {
					int messageId = item.getMessagecontentid();
					intentNotice.putExtra(Extra.SelectedItem, messageId);
					intentNotice.putExtra(Extra.Position, position);
				}
				intentNotice.putExtra(Extra.SelectedID, userId);
				startActivityForResult(intentNotice, MainActivity.REQUESTCODE_NOTICE);
			}
			break;
		case R.id.home_lv_topic:
			TopicItemRModel itemTopic = (TopicItemRModel) mAdapterTopic.getItem(position);
			Intent intentTopic = new Intent(getActivity(),TopicDetialActivity.class);
			intentTopic.putExtra(Extra.SelectedID, itemTopic.getTopicid());
			startActivity(intentTopic);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case MainActivity.REQUESTCODE_PRIVATE:
			if (resultCode == Activity.RESULT_OK) {
				int position2 = data.getIntExtra(Extra.Position, -1);
				String content = data.getStringExtra(Extra.Snippet);
				String time = data.getStringExtra(Extra.Time_Latest);
				if (position2 != -1 && position2 < rModel.getPrivateListItem().size()) {
					if (content != null) {
						PrivateListItem item = rModel.getPrivateListItem().get(position2);
						item.setContent(content);
						item.setSendTime(time);
						mAdapterPrivate.notifyDataSetChanged();
					}
				}
			}
			break;
		case MainActivity.REQUESTCODE_NOTICE:
			if (resultCode == Activity.RESULT_OK) {
				int position = data.getIntExtra(Extra.Position, -1);
				if (position != -1) {
					int state = data.getIntExtra(Extra.State, State.None);
					NoticeListItem item = rModel.getNoticeItem().get(position);
					if (state != 0) {
						item.setResult(state);
						mAdapterNotice.notifyDataSetChanged();
					}
				}
			}
			break;
		}
	}
}
