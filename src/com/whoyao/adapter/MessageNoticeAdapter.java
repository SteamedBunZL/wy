package com.whoyao.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.baidu.platform.comapi.map.r;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.R.color;
import com.whoyao.WebApi;
import com.whoyao.activity.MyMessageActivity;
import com.whoyao.activity.UserOtherDetialActivity;
import com.whoyao.adapter.EventCreatListAdapter.Holder;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.InviteListItem;
import com.whoyao.model.NoticeListItem;
import com.whoyao.model.PrivateListItem;
import com.whoyao.model.RecommendUser;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.TimeUtils;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
/**
 * 私信-通知适配器
 * @author zl
 *
 */
public class MessageNoticeAdapter extends BaseAdapter {
	private List<NoticeListItem> mList;
	private LayoutInflater inflater;
	private ImageAsyncLoader loader;
	private OnClickListener listener;
	private String html;
	String name1;//推荐好友第一人
	String name2;//推荐好友第二人
	String name3;//推荐好友第三人
	int length = 23;
	private SpannableString spannableString1;

	public MessageNoticeAdapter(List<NoticeListItem> mList,
			LayoutInflater inflater, OnClickListener listener) {
		super();
		this.mList = mList;
		this.inflater = inflater;
		this.listener = listener;
		loader = SmallImageAsyncLoader.getInstance();
	}

	public void deleteSingleItem(int position) {
		mList.remove(position);
	}

	public void deleteAll() {
		mList.clear();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		NoticeListItem item = mList.get(position);
		System.out.println(position);
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.item_message_notice, null);
			holder = new Holder();
			holder.ivInformHead = (ImageView) convertView
					.findViewById(R.id.iv_inform_head);
			holder.tvInformName = (TextView) convertView
					.findViewById(R.id.tv_inform_title);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_inform_name);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_message_inform_time);
			holder.btnAgree = (Button) convertView
					.findViewById(R.id.btn_inform_agree);
			holder.btnDisAgree = (Button) convertView
					.findViewById(R.id.btn_inform_disagree);
			holder.tvInformNotice = (TextView) convertView
					.findViewById(R.id.tv_inform_notice);
			if (null != listener) {
//				holder.ivInformHead.setOnClickListener(listener);
				holder.btnAgree.setOnClickListener(listener);
				holder.btnDisAgree.setOnClickListener(listener);
			}
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.ivInformHead.setTag(R.id.iv_inform_head, position);
		holder.btnAgree.setTag(R.id.btn_inform_agree, position);
		holder.btnDisAgree.setTag(R.id.btn_inform_disagree, position);
		holder.tvContent.setTag(position);
		holder.tvTime.setTag(position);
		holder.tvInformNotice.setTag(position);
		holder.tvInformName.setTag(position);

		// 区分系统通知还是好友通知 0为系统通知
		if (item.getSenduserid() == 0) {
			holder.tvInformName.setText("系统通知");
			holder.tvInformName.setTextColor(Color.BLACK);
			holder.tvContent.setText(item.getInformcontent());
			holder.tvContent.setTextColor(Color.BLACK);
			holder.btnAgree.setVisibility(View.GONE);
			holder.btnDisAgree.setVisibility(View.GONE);
			holder.ivInformHead.setImageResource(R.drawable.huyao);
			holder.tvInformNotice.setVisibility(View.GONE);
		} else {
			holder.btnAgree.setVisibility(View.GONE);
			holder.btnDisAgree.setVisibility(View.GONE);
			holder.tvInformNotice.setVisibility(View.GONE);
			if (item.getContenttype() == 18) {
				holder.tvInformName.setText(item.getSendusername());
				
				holder.tvInformName.setTextColor(AppContext.getRes().getColor(
						R.color.blue_text));
				final List<RecommendUser> userList = item.getUserList();
				int num = userList.size();
				Map<Integer, String> userMap = new TreeMap<Integer, String>();
				for (int i = 0; i < num; i++) {
					userMap.put(userList.get(i).getUserid(), userList.get(i)
							.getUsername());
				}
				String content = "\"我的好友哦，介绍给你认识！\",TA给您推荐了";
				switch (num) {
				case 1:
					name1 = userList.get(0).getUsername();
					content = content + name1 + "。";
					spannableString1 = new SpannableString(content);
					spannableString1.setSpan(new ClickableSpan() {

						@Override
						public void onClick(View widget) {
							int userId = userList.get(0).getUserid();
							UserEngine.startUserDetial(userId);
						}

						@Override
						public void updateDrawState(TextPaint ds) {
							super.updateDrawState(ds);
							ds.setColor(AppContext.getRes().getColor(R.color.blue_text));
							ds.setUnderlineText(false);
						}

					}, length, length + name1.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					break;
				case 2:
					name1 = userList.get(0).getUsername();
					name2 = userList.get(1).getUsername();
					content = content + name1 + "，" + name2 + "。";
					spannableString1 = new SpannableString(content);
					spannableString1.setSpan(new ClickableSpan() {

						@Override
						public void onClick(View widget) {
							int userId = userList.get(0).getUserid();
							UserEngine.startUserDetial(userId);
						}

						@Override
						public void updateDrawState(TextPaint ds) {
							super.updateDrawState(ds);
							ds.setColor(AppContext.getRes().getColor(R.color.blue_text));
							ds.setUnderlineText(false);
						}

					}, length, length + name1.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					spannableString1.setSpan(new ClickableSpan() {

						@Override
						public void onClick(View widget) {
							int userId = userList.get(1).getUserid();
							UserEngine.startUserDetial(userId);
						}

						@Override
						public void updateDrawState(TextPaint ds) {
							super.updateDrawState(ds);
							ds.setColor(AppContext.getRes().getColor(R.color.blue_text));
							ds.setUnderlineText(false);
						}
					}, length + name1.length() + 1, length + name1.length() + 1
							+ name2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					break;
				case 3:
					name1 = userList.get(0).getUsername();
					name2 = userList.get(1).getUsername();
					name3 = userList.get(2).getUsername();
					content = content + name1 + "，" + name2 + "，" + name3
							+ "。";
					spannableString1 = new SpannableString(content);
					spannableString1.setSpan(new ClickableSpan() {

						@Override
						public void onClick(View widget) {
							int userId = userList.get(0).getUserid();
							UserEngine.startUserDetial(userId);
						}

						@Override
						public void updateDrawState(TextPaint ds) {
							super.updateDrawState(ds);
							ds.setColor(AppContext.getRes().getColor(R.color.blue_text));
							ds.setUnderlineText(false);
						}

					}, length, length + name1.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					spannableString1.setSpan(new ClickableSpan() {

						@Override
						public void onClick(View widget) {
							int userId = userList.get(1).getUserid();
							UserEngine.startUserDetial(userId);
						}

						@Override
						public void updateDrawState(TextPaint ds) {
							super.updateDrawState(ds);
							ds.setColor(AppContext.getRes().getColor(
									R.color.blue_text));
							ds.setUnderlineText(false);
						}
					}, length + name1.length() + 1, length + name1.length() + 1
							+ name2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					spannableString1.setSpan(new ClickableSpan() {

						@Override
						public void onClick(View widget) {
							int userId = userList.get(2).getUserid();
							UserEngine.startUserDetial(userId);
						}

						@Override
						public void updateDrawState(TextPaint ds) {
							super.updateDrawState(ds);
							ds.setColor(AppContext.getRes().getColor(R.color.blue_text));
							ds.setUnderlineText(false);
						}
					}, length + name1.length() + 2 + name2.length(),
							length + name1.length() + 2 + name2.length()
									+ name3.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					break;
				}
				// CharSequence charSequence = Html.fromHtml(name1);
				holder.tvContent.setText(spannableString1);
				holder.tvContent.setMovementMethod(LinkMovementMethod
						.getInstance());
			} else if (item.getContenttype() == 24) {
				holder.tvInformName.setText(item.getSendusername());
				holder.tvInformName.setTextColor(AppContext.getRes().getColor(
						R.color.blue_text));
				holder.tvContent.setText(item.getInformcontent());
				holder.btnAgree.setVisibility(View.GONE);
				holder.btnDisAgree.setVisibility(View.GONE);
				holder.ivInformHead.setImageResource(R.drawable.huyao);
				holder.tvInformNotice.setVisibility(View.GONE);
			} else if (item.getContenttype()==22) {
				holder.tvInformName.setText(item.getSendusername());
				holder.tvInformName.setTextColor(AppContext.getRes().getColor(
						R.color.blue_text));
				holder.tvContent.setText(item.getInformcontent());
				holder.btnAgree.setVisibility(View.GONE);
				holder.btnDisAgree.setVisibility(View.GONE);
				holder.ivInformHead.setImageResource(R.drawable.huyao);
				holder.tvInformNotice.setVisibility(View.GONE);
			}
			else {
				int result = item.getResult();
				holder.tvInformName.setText("好友请求");
				holder.tvInformName.setTextColor(Color.BLACK);
				if (result == 0) {
					holder.btnAgree.setVisibility(View.VISIBLE);
					holder.btnDisAgree.setVisibility(View.VISIBLE);
					holder.tvInformNotice.setVisibility(View.GONE);

				} else if (result == 1) {
					holder.btnAgree.setVisibility(View.INVISIBLE);
					holder.btnDisAgree.setVisibility(View.INVISIBLE);
					holder.tvInformNotice.setVisibility(View.VISIBLE);
					holder.tvInformNotice.setText("已加好友");
				} else {
					holder.btnAgree.setVisibility(View.INVISIBLE);
					holder.btnDisAgree.setVisibility(View.INVISIBLE);
					holder.tvInformNotice.setVisibility(View.VISIBLE);
					holder.tvInformNotice.setText("已忽略");
				}
				html = "<font color='#2DABE0'>" + item.getSendusername()
						+ "</font>" + " 请求加你为好友。";
				CharSequence charSequence = Html.fromHtml(html);
				holder.tvContent.setText(charSequence);
			}
			String pictureID = item.getSenduserpicture();
			loader.loadBitmap(pictureID, holder.ivInformHead);

		}
		holder.tvTime.setText(item.getSendusername());
		if (item.getEntertime() != null) {
			holder.tvTime.setText(TimeUtils.getTime(item.getEntertime()));
		}
		if (item.getIsread() == 2) {
			holder.tvContent.setTextColor(Color.GRAY);
		} else if (item.getIsread() == 1) {
			holder.tvContent.setTextColor(Color.BLACK);
		}
		return convertView;
	}

	/** Holder的持有对象 */
	public static class Holder {
		ImageView ivInformHead;
		TextView tvInformName;
		TextView tvContent;
		TextView tvTime;
		TextView tvInformNotice;
		Button btnAgree;
		Button btnDisAgree;
	}

}
