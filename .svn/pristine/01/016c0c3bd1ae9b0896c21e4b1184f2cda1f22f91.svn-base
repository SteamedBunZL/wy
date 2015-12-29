package com.whoyao.activity;

import java.util.ArrayList;

import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.adapter.EventAlbumAdapter;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.model.EventPhotoModel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

/**
 * 活动相册
 * 
 * @author hyh creat_at：2013-9-16-上午11:21:38
 */
public class EventAlbumActivity extends BasicActivity implements
		OnClickListener {
	private GridView mGridView;
	private ArrayList<EventPhotoModel> photos;
	private EventAlbumAdapter adapter;
	private Button addButton;
	private Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_album);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		addButton = (Button) findViewById(R.id.event_album_btn_add);
		addButton.setOnClickListener(this);
		// user 0 代表既不是发起人也不是参与人 1代表是两者之中的一个
		Intent intent = getIntent();
		int isAvailable = intent.getIntExtra(Extra.isAvailable, 0);
		if (isAvailable==0) {
			addButton.setVisibility(View.GONE);
		}else {
			addButton.setVisibility(View.VISIBLE);
		}
		mGridView = (GridView) findViewById(R.id.event_album_gv);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context,
						EventPhotoActivity.class);
				intent.putExtra(Extra.SelectedItem, position);
				startActivity(intent);
			}
		});
		photos = EventDetialManager.photoList;
		adapter = new EventAlbumAdapter(photos);
		mGridView.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
//		adapter.notifyDataSetChanged();
		new Thread(){
			public void run() {
				SystemClock.sleep(500);
				handler.post(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			};
		}.start();
	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.event_album_btn_add:
			Intent intent = new Intent(this, EventPhotoAddActivity.class);
			intent.putExtra(Extra.SelectedID, EventDetialManager.getCurrentID());
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
