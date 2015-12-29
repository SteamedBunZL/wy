package com.whoyao.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.EventPhotoAddAdapter;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.Utils;

public class EventPhotoAddActivity extends BasicActivity implements OnClickListener {

	private EditText etTitle;
	private GridView mGridView;
	public static ArrayList<File> photos;
	public static ArrayList<String> titles;
	private BaseAdapter adapter;
	private int upIndex = 0;
	private int id;
	private boolean upSuccessful = false;
	private RequestParams params;
	private UpPhotoHandler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		photos = new ArrayList<File>();
		titles = new ArrayList<String>();
		setContentView(R.layout.activity_event_photo_add);
		id = getIntent().getIntExtra(Extra.SelectedID, 0);
		if(0 == id){
			finish();
			return;
		}
		findViewById(R.id.event_photo_add_btn).setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);

		etTitle = (EditText) findViewById(R.id.event_photo_add_et);
		mGridView = (GridView) findViewById(R.id.event_photo_add_gv);
		mGridView.setSelector(getResources().getDrawable(android.R.color.transparent));
		adapter = new EventPhotoAddAdapter(photos);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == photos.size()) {
					if(position == 9){
						Toast.show("一次最多上传9张图片");
						return;
					}
					if(TextUtils.isEmpty(etTitle.getText())){
						Toast.show("请输入图片标题");
						return;
					}
					if (20 < Utils.getStringLength(etTitle.getText().toString())) {
						Toast.show("图片标题20个字以内");
						return;
					}
					Intent intent = new Intent(AppContext.context, SelectPhotoActivity.class);
					startActivityForResult(intent, R.id.event_photo_add_gv);
					
				} else {
					Intent intent = new Intent(AppContext.context, EventPhotoCancelActivity.class);
					intent.putExtra(Extra.SelectedItem, position);
					startActivityForResult(intent, R.id.event_photo_add_gv);
				}
			}
		});
		params = new RequestParams();
		params.put(KEY.EventID, id + "");
		mHandler = new UpPhotoHandler(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.event_photo_add_btn:
			if (photos.isEmpty()) {
				if(TextUtils.isEmpty(etTitle.getText())){
					Toast.show("请输入图片标题");
					return;
				}
				Toast.show("请添加图片");
				return;
			}
			upPhoto();
			break;
		case R.id.page_btn_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode && null != data) {
			String path = data.getStringExtra(Extra.IMAGE_PATH);
			File file = new File(path);
			if(file.exists()){
				photos.add(file);
				titles.add(etTitle.getText().toString());
				etTitle.setText("");
				adapter.notifyDataSetChanged();
			}
		}
	}

	private void upPhoto() {

		try {
			params.put(KEY.PHOTO_SUBJECT, titles.get(upIndex));
			params.put(KEY.Image, photos.get(upIndex));
			Net.request(params, WebApi.Event.UpPhoto, mHandler);
		} catch (FileNotFoundException e) {
			AppException.handle(e);
		}
	}

	public class UpPhotoHandler extends ResponseHandler {
		
		public UpPhotoHandler(boolean isShowProgress) {
			super(isShowProgress);
		}

		@Override
		public void onSuccess(String content) {
			// {"ActivityPhotoId":970,"PhotoPath":"137/932/226/110/800"}
			upSuccessful = true;
			if (upIndex < photos.size() - 1) {
				++upIndex;
				upPhoto();
			} else {
				Toast.show("照片上传成功");
				EventDetialManager.getInstance().getDetial(id, new CallBack<String>(){
					@Override
					public void onCallBack(boolean isSuccess) {
						if(isSuccess){
							Intent intent = new Intent(context, EventAlbumActivity.class);
							intent.putExtra(Extra.isAvailable, 1);
							startActivity(intent);
						}
						finish();
					}
				});
			}
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
		if (null != titles) {
			titles.clear();
			titles = null;
		}
	}

	@Override
	protected boolean onBack() {
		if (upSuccessful) {
			NetCache.clearCaches();// 更新照片信息
			EventDetialManager.getInstance().getDetial(id, new CallBack<String>() {
				@Override
				public void onCallBack() {
					finish();
				}
			});
		} else {
			finish();
		}
		return true;
	}
}
