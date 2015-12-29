package com.whoyao.topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TopicTagChoiceActivity extends BasicActivity implements
		OnClickListener, OnItemClickListener {
	// private EditText etTopicSearch;
	private com.whoyao.widget.NoScrollGridView gvTopicTag;
	private Intent intent;
	private TopicTagAdapter topicTagAdapter;
	// private String[] tags;
	// private TextView tvClear;
	// private TextView tvCancel;
	private List<SystemTagItem> systemTagLists = new ArrayList<SystemTagItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_choice);
		// tags = getResources().getStringArray(R.array.event_tags);
		initView();
		initData();
	}

	public void initView() {
		// tvClear = (TextView)
		// findViewById(R.id.topic_search_iv_clear_initial);
		// tvClear.setOnClickListener(this);
		// tvCancel = (TextView) findViewById(R.id.topic_search_tv_cancel);
		// tvCancel.setOnClickListener(this);
		// etTopicSearch = (EditText)
		// findViewById(R.id.topic_search_et_initial);
		// etTopicSearch.setOnFocusChangeListener(this);
		// etTopicSearch.addTextChangedListener(this);
		// etTopicSearch.setOnKeyListener(this);
		findViewById(R.id.page_btn_back_initial).setOnClickListener(this);
		findViewById(R.id.tv_free).setOnClickListener(this);
		gvTopicTag = (com.whoyao.widget.NoScrollGridView) findViewById(R.id.gv_topic_choice);
		gvTopicTag.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvTopicTag.setOnItemClickListener(this);
	}

	public void initData() {
		intent = new Intent();
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
						// Toast.show("加载失败");
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
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
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

	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before, int
	// count) {
	// if (etTopicSearch.getText().length() == 0) {
	// tvClear.setVisibility(View.GONE);
	// } else {
	// tvClear.setVisibility(View.VISIBLE);
	// }
	//
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// // TODO Auto-generated method stub
	//
	// }

	// @Override
	// public boolean onKey(View v, int keyCode, KeyEvent event) {
	// if (KeyEvent.KEYCODE_ENTER == keyCode
	// && KeyEvent.ACTION_UP == event.getAction()) {
	// if (TextUtils.isEmpty(etTopicSearch.getText())) {
	// Toast.show("请输入搜索内容");
	// return false;
	// }
	// // intent.putExtra(Extra.State, State.Search_Str);
	// intent.putExtra(Extra.Search_Keyword, etTopicSearch.getText()
	// .toString());
	// intent.putExtra(Extra.SelectedID, 0);
	// intent.putExtra(Extra.Search_List, (Serializable)systemTagLists);
	// startActivity(intent);
	// }
	// return false;
	// }

	// @Override
	// public void onFocusChange(View v, boolean hasFocus) {
	// if (hasFocus) {
	// etTopicSearch.setSelection(etTopicSearch.getText().length());
	// tvClear.setVisibility(View.VISIBLE);
	// tvCancel.setVisibility(View.VISIBLE);
	// }
	//
	// }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SystemTagItem item = (SystemTagItem) topicTagAdapter.getItem(position);
		intent.putExtra(Extra.SelectedID, item.getTagid());
		intent.putExtra(Extra.Search_Keyword, item.getTag());
		// intent.putExtra(Extra.Search_List, (Serializable)systemTagLists);
		// intent.putExtra(Extra.SelectedItem, item);
		setResult(RESULT_OK, intent);
		finish();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			intent.putExtra(Extra.SelectedID,
					data.getIntExtra(Extra.SelectedID, 0));
			intent.putExtra(Extra.Search_Keyword,
					data.getStringExtra(Extra.Search_Keyword));
			setResult(RESULT_OK, intent);
		} else {
			
		}
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back_initial:
			finish();
			break;
		case R.id.tv_free:
			Intent intent = new Intent(context,
					TopicTagFreeChoiceActivity.class);
			startActivityForResult(intent, R.id.btn_add);
			// finish();
			break;
		// case R.id.topic_search_iv_clear_initial:
		// etTopicSearch.setText("");
		// break;
		// case R.id.topic_search_tv_cancel:
		// etTopicSearch.setText("");
		// etTopicSearch.clearFocus();
		// // imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
		// // InputMethodManager.HIDE_NOT_ALWAYS);
		// Utils.hideSoftKeyboard(context);
		// tvClear.setVisibility(View.INVISIBLE);
		// tvCancel.setVisibility(View.GONE);
		// break;
		}

	}

}
