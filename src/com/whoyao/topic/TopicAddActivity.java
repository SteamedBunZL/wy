package com.whoyao.topic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.baidu.platform.comapi.map.l;
import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.R.color;
import com.whoyao.activity.BasicActivity;
import com.whoyao.activity.SelectPhotoActivity;
import com.whoyao.activity.EventPhotoAddActivity.UpPhotoHandler;
import com.whoyao.adapter.EventPhotoAddAdapter;
import com.whoyao.adapter.TopicPhotoAdapter;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.model.ResultModel;
import com.whoyao.model.SystemTagItem;
import com.whoyao.model.TopicAddTModel;
import com.whoyao.model.TopicItemRModel;
import com.whoyao.model.TopicPhotoItem;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TopicAddActivity extends BasicActivity implements OnClickListener{
	private com.whoyao.widget.NoScrollGridView mGridView;
	private EditText etContent;
	private EditText etTitle;
	private TextView tvTag;
	private TextView tvNum;
	public static ArrayList<File> photos;
	private BaseAdapter adapter;
	private int upIndex = 0;
	private int id;
	private boolean upSuccessful = false;
	private RequestParams params;
	private UpPhotoHandler mHandler;
	private String imageList = "";
	private int tagId;
	private String tag;
	private String title;
	private String tvContent;
	private TopicAddTModel tModel;
	private Intent intent;
	private int topicId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_add);
		initView();
		initData();
	}

	public void initView() {
		photos = new ArrayList<File>();
		tModel = new TopicAddTModel();
		tvTag = (TextView) findViewById(R.id.tv_tag);
		tvNum = (TextView) findViewById(R.id.topic_content_num);
		mGridView = (com.whoyao.widget.NoScrollGridView) findViewById(R.id.event_photo_add_gv);
		findViewById(R.id.invite_btn_back).setOnClickListener(this);
		findViewById(R.id.btn_add).setOnClickListener(this);
		findViewById(R.id.tv_camera).setOnClickListener(this);
		findViewById(R.id.ll_tag).setOnClickListener(this);
		findViewById(R.id.rl_content).setOnClickListener(this);
		etTitle = (EditText) findViewById(R.id.et_title);
		etTitle.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length()>30&&start<31) {
					Toast.show("标题最多30个字");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		etContent = (EditText) findViewById(R.id.et_content);
		etContent.addTextChangedListener(new TextNumWatcher(tvNum, 500));
		mGridView.setSelector(getResources().getDrawable(
				android.R.color.transparent));
		adapter = new TopicPhotoAdapter(photos);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == photos.size()) {
					if (position == 8) {
						Toast.show("一次最多上传8张图片");
						return;
					}
					// if(TextUtils.isEmpty(etTitle.getText())){
					// Toast.show("请输入图片标题");
					// return;
					// }
					// if (20 <
					// Utils.getStringLength(etTitle.getText().toString())) {
					// Toast.show("图片标题20个字以内");
					// return;
					// }
					Intent intent = new Intent(AppContext.context,
							SelectPhotoActivity.class);
					startActivityForResult(intent, R.id.event_photo_add_gv);
				} else {
					Intent intent = new Intent(AppContext.context,
							TopicPhotoCancelActivity.class);
					intent.putExtra(Extra.SelectedItem, position);
					startActivityForResult(intent, R.id.event_photo_add_gv);
				}
			}
		});
		params = new RequestParams();
		params.put(KEY.EventID, "0");
//		params.put(KEY., values)
		mHandler = new UpPhotoHandler(true);
	}
	
	public void initData() {

	}
	@Override
	protected boolean onBack() {
		MessageDialog dialog = new MessageDialog(this);
		dialog.setTitle("操作提示");
		dialog.setMessage("确定要退出本次发话题吗？");
		dialog.setLeftButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		dialog.setRightButton("取消", null);
		dialog.show();
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invite_btn_back:
			onBack();
			break;
		case R.id.ll_tag:
			Intent tagIntent = new Intent(context, TopicTagChoiceActivity.class);
			startActivityForResult(tagIntent, R.id.ll_tag);
			break;
		// case R.id.tv_camera:
		//
		// params = new RequestParams();
		// params.put(KEY.EventID, "0");
		// mHandler = new UpPhotoHandler(true);
		// break;
		case R.id.btn_add:
			// 检查是否没输入全
			// if (photos) {
			//
			// }
			title = etTitle.getText().toString();
			tvContent = etContent.getText().toString();
			if (TextUtils.isEmpty(tag)) {
				Toast.show("请选择标签");
				return;
			}
			if (TextUtils.isEmpty(title)) {
				Toast.show("请输入标题");
				return;
			}
			if (TextUtils.isEmpty(tvContent)) {
				Toast.show("请输入内容");
				return;
			}
			if (title.length()>30) {
				Toast.show("标题最多30个字");
				return;
			}
			if (tvContent.length()>500||tvContent.length()<3) {
				Toast.show("话题内容为3-500个字");
				return;
			}
			if (photos.size() > 0) {
				upPhoto();
			} else {
				tModel.setTagid(tagId);
				tModel.setTag(tag);
				tModel.setTitle(title);
				tModel.setContent(tvContent);
				tModel.setImage(imageList);
				Net.request(tModel, WebApi.Topic.AddTopic, new ResponseHandler(
						true) {

					@Override
					public void onSuccess(String content) {
						ResultModel object = JSON.getObject(content, ResultModel.class);
						topicId = (int) object.getResult();
						Intent detailIntent = new Intent(context,
								TopicDetialActivity.class);
						
						detailIntent.putExtra(Extra.SelectedID, topicId);
						startActivity(detailIntent);
						finish();
					}

					@Override
					public void onFailure(Throwable e, int statusCode,
							String content) {
						super.onFailure(e, statusCode, content);
					}

				});
			}
			// imageList = imageList.substring(0,imageList.length()-1);
			// System.out.println(imageList);
			break;
		}

	}

	private void upPhoto() {

		try {
			params.put(KEY.PHOTO_SUBJECT, "Topic");
			params.put(KEY.Image, photos.get(upIndex));
			params.put("istemp", "1");
			Net.request(params, WebApi.Event.UpPhoto, mHandler);
		} catch (FileNotFoundException e) {
			AppException.handle(e);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != photos) {
			photos.clear();
			photos = null;
		}
		// if (null != titles) {
		// titles.clear();
		// titles = null;
		// }
	}

	// @Override
	// protected boolean onBack() {
	// if (upSuccessful) {
	// NetCache.clearCaches();// 更新照片信息
	// EventDetialManager.getInstance().getDetial(id, new CallBack<String>() {
	// @Override
	// public void onCallBack() {
	// finish();
	// }
	// });
	// } else {
	// finish();
	// }
	// return true;
	// }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == R.id.ll_tag) {
			if (resultCode == RESULT_OK) {
				tag = data.getStringExtra(Extra.Search_Keyword);
				tvTag.setText(tag);
				tagId = data.getIntExtra(Extra.SelectedID, 0);

			}
		} else {
			if (RESULT_OK == resultCode && null != data) {
				String path = data.getStringExtra(Extra.IMAGE_PATH);
				File file = new File(path);
				if (file.exists()) {
					photos.add(file);
					// titles.add(etTitle.getText().toString());
					// etTitle.setText("");
					adapter.notifyDataSetChanged();
				}
			}

		}
	}

	public class UpPhotoHandler extends ResponseHandler {

		public UpPhotoHandler(boolean isShowProgress) {
			super(isShowProgress);
		}

		@Override
		public void onSuccess(String content) {
			// {"ActivityPhotoId":970,"PhotoPath":"137/932/226/110/800"}
			// String[] Paths=content.split(":");
			// imageList = imageList+Paths[1]+",";
			// System.out.println(content);
			TopicPhotoItem item = JSON.getObject(content, TopicPhotoItem.class);
			imageList = imageList + item.getPhotoPath() + ",";
			upSuccessful = true;
			if (upIndex < photos.size() - 1) {
				++upIndex;
				upPhoto();
			} else {
				imageList = imageList.substring(0, imageList.length() - 1);
				tModel.setTagid(tagId);
				tModel.setTag(tag);
				tModel.setTitle(title);
				tModel.setContent(tvContent);
				tModel.setImage(imageList);
				Net.request(tModel, WebApi.Topic.AddTopic, new ResponseHandler(
						true) {

					@Override
					public void onSuccess(String content) {
						ResultModel object = JSON.getObject(content, ResultModel.class);
						topicId = (int) object.getResult();
						Intent detailIntent = new Intent(context,
								TopicDetialActivity.class);
						detailIntent.putExtra(Extra.SelectedID, topicId);
						startActivity(detailIntent);
						finish();
					}

					@Override
					public void onFailure(Throwable e, int statusCode,
							String content) {
						super.onFailure(e, statusCode, content);
					}

				});

			}

			// else {
			// Toast.show("照片上传成功");
			// EventDetialManager.getInstance().getDetial(id,
			// new CallBack<String>() {
			// @Override
			// public void onCallBack(boolean isSuccess) {
			// if (isSuccess) {
			// Intent intent = new Intent(context,
			// EventAlbumActivity.class);
			// intent.putExtra(Extra.isAvailable, 1);
			// startActivity(intent);
			// }
			// finish();
			// }
			// });
			// }
		}
	}

	
}
