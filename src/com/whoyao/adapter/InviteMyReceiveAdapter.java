package com.whoyao.adapter;

import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.adapter.EventCreatListAdapter.Holder;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.model.EventListItem;
import com.whoyao.model.InviteDetailListItem;
import com.whoyao.utils.CalendarUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InviteMyReceiveAdapter extends BaseAdapter {
	private List<InviteDetailListItem> mList;
	private LayoutInflater inflater;
	private static String[] states = {"未处理","邀请中","已拒绝","已过期","已接受","全部"};
	private static String[] order = {"由远及近","由近及远"};
	private OnClickListener listener;

	public InviteMyReceiveAdapter(List<InviteDetailListItem> list,LayoutInflater inflater,OnClickListener listener) {
		super();
		mList = list;
		this.inflater = inflater;
		this.listener = listener;
		
	}

	@Override
	public int getCount() {
		if (null==mList) {
			return 0;
		}
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_my_invite, null);
			holder = new Holder();
			holder.tvinviteTitle = (TextView) convertView.findViewById(R.id.invite_title);
			holder.tvinviteSendPerson = (TextView) convertView.findViewById(R.id.invite_send_person);
			holder.tvinviteTime = (TextView) convertView.findViewById(R.id.invite_time);
			holder.tvinviteState = (TextView) convertView.findViewById(R.id.invite_state);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		InviteDetailListItem item = mList.get(position);
		holder.tvinviteState.setTag(position);
		holder.tvinviteTitle.setText(item.getTitle());
		holder.tvinviteSendPerson.setText("发起人："+item.getSendusername());
		holder.tvinviteTime.setText("邀请时间："+CalendarUtils.formatYMDHM(item.getTime()));
		int state = item.getInviteState();
		if (state==-1) {
			holder.tvinviteState.setText("状态："+states[5]);
		}else {
			holder.tvinviteState.setText("状态："+states[state]);
		}
		
		return convertView;
	}
	public static class Holder{
		public TextView tvinviteTitle;
		public TextView tvinviteSendPerson;
		public TextView tvinviteTime;
		public TextView tvinviteState;
		
		
	}

}
