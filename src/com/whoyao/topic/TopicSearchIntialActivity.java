package com.whoyao.topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.activity.BasicActivity;
import com.whoyao.model.SystemTagItem;
import com.whoyao.model.SystemTagRModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TopicSearchIntialActivity extends BasicActivity implements
		OnClickListener, OnItemClickListener, OnFocusChangeListener,
		OnKeyListener, TextWatcher {
	private EditText etTopicSearch;
	private GridView gvTopicTag;
	private Intent intent;
	private TopicTagAdapter topicTagAdapter;
	private String[] tags;
	private TextView tvClear;
	private TextView tvCancel;
	private List<SystemTagItem> systemTagLists = new ArrayList<SystemTagItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_initial);
		tags = getResources().getStringArray(R.array.event_tags);
		initView();
		initData();
	}

	public void initView() {
		findViewById(R.id.page_btn_back_initial).setOnClickListener(this);
		tvClear = (TextView) findViewById(R.id.topic_search_iv_clear_initial);
		tvClear.setOnClickListener(this);
		tvCancel = (TextView) findViewById(R.id.topic_search_tv_cancel);
		tvCancel.setOnClickListener(this);
		etTopicSearch = (EditText) findViewById(R.id.topic_search_et_initial);
		etTopicSearch.setOnFocusChangeListener(this);
		etTopicSearch.addTextChangedListener(this);
		etTopicSearch.setOnKeyListener(this);
		gvTopicTag = (GridView) findViewById(R.id.topic_search_gv_hot_area);
		gvTopicTag.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvTopicTag.setOnItemClickListener(this);
	}

	public void initData() {
		intent = new Intent(this, TopicSearchActivity.class);
		// Net.request(null, WebApi.Common.GetSystemTag,
		// new ResponseHandler());
		Net.request(null, WebApi.Common.GetSystemTag,
				new ResponseHandler(true) {

					@Override
					public void onSuccess(String content) {
						List<SystemTagItem> list = JSON.getObject(content,
								SystemTagRModel.class).TagItem;
						systemTagLists.addAll(list);
						topicTagAdapter = new TopicTagAdapter();
						topicTagAdapter.addList(list);
						gvTopicTag.setAdapter(topicTagAdapter);
						topicTagAdapter.notifyDataSetChanged();

					}

					@Override
					public void onFailure(Throwable e, int statusCode,
							String content) {
						super.onFailure(e, statusCode, content);
						if (statusCode == 0) {
							finish();
						}
					}

				});

	}

	public class TopicTagAdapter extends BaseAdapter {
		List<SystemTagItem> mlist = new ArrayList<SystemTagItem>();

		public void addList(List<SystemTagItem> data) {
			mlist.addAll(data);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.item_event_search_initial, null);
			TextView tv_event_initial_area = (TextView) view
					.findViewById(R.id.tv_event_initial);
			SystemTagItem item = (SystemTagItem) getItem(position);
			tv_event_initial_area.setText(item.getTag());
			return view;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (etTopicSearch.getText().length() == 0) {
			tvClear.setVisibility(View.GONE);
		} else {
			tvClear.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_ENTER == keyCode
				&& KeyEvent.ACTION_UP == event.getAction()) {
			if (TextUtils.isEmpty(etTopicSearch.getText())) {
				Toast.show("请输入搜索内容");
				return false;
			}
			// intent.putExtra(Extra.State, State.Search_Str);
			intent.putExtra(Extra.Search_Keyword, etTopicSearch.getText()
					.toString());
			intent.putExtra(Extra.SelectedID, 0);
			intent.putExtra(Extra.Search_List, (Serializable) systemTagLists);
			startActivity(intent);
		}
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			etTopicSearch.setSelection(etTopicSearch.getText().length());
			tvClear.setVisibility(View.VISIBLE);
			tvCancel.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SystemTagItem item = (SystemTagItem) topicTagAdapter.getItem(position);
		intent.putExtra(Extra.SelectedID, item.getTagid());
		intent.putExtra(Extra.Search_Keyword, "");
		intent.putExtra(Extra.Search_List, (Serializable) systemTagLists);
		intent.putExtra(Extra.SelectedItem, item);
		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topic_search_iv_clear_initial:
			etTopicSearch.setText("");
			break;
		case R.id.topic_search_tv_cancel:
			etTopicSearch.setText("");
			etTopicSearch.clearFocus();
			// imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
			// InputMethodManager.HIDE_NOT_ALWAYS);
			Utils.hideSoftKeyboard(context);
			tvClear.setVisibility(View.INVISIBLE);
			tvCancel.setVisibility(View.GONE);
			break;
		case R.id.page_btn_back_initial:
			finish();
			break;
		}

	}

}
