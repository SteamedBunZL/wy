package com.whoyao.activity;

import org.taptwo.widget.ViewFlow;
import org.taptwo.widget.ViewFlow.ViewSwitchListener;

import com.whoyao.AppContext;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.common.AnimPost;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.ui.MessageDialog;
import com.whoyao.utils.L;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 活动照片
 * 
 * @author hyh creat_at：2013-9-26-上午10:03:16
 * @param <item>
 */
public class EventPhotoCancelActivity<item> extends BasicActivity implements OnClickListener {
	private TextView tvTitle;
	private ViewFlow mViewFlow;
	private ImageAsyncLoader loader;
	private LayoutInflater inflater;
	private MessageDialog dialog;
	private BaseAdapter mAdatper;
	private int curPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loader = OriginalImageAsyncLoader.getInstance();
		dialog = new MessageDialog(this);
		dialog.setMessage("确定要删除本张照片吗？");
		dialog.setLeftButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = curPosition;
				L.i("getCurrentItem: " + position);
				EventPhotoAddActivity.titles.remove(position);
				EventPhotoAddActivity.photos.remove(position);
				mAdatper.notifyDataSetChanged();
				if (EventPhotoAddActivity.photos.isEmpty()) {
					finish();
				} else {
					if (position < EventPhotoAddActivity.photos.size()) {
						mViewFlow.setSelection(position);
						tvTitle.setText(position + 1 + " of " + EventPhotoAddActivity.photos.size());
					} else {
						mViewFlow.setSelection(position - 1);
						tvTitle.setText(position + " of " + EventPhotoAddActivity.photos.size());
					}
				}
			}
		});
		dialog.setRightButton("取消", null);
		inflater = LayoutInflater.from(this);
		setContentView(R.layout.activity_event_photo_cancel);
		initView();
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.page_btn_delete).setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.page_tv_title);

		mViewFlow = (ViewFlow) findViewById(R.id.event_photo_vp);
		// adatper = new ViewPagerAdatper();
		mAdatper = new ViewPagerAdatper();
		mViewFlow.setAdapter(mAdatper);
		mViewFlow.setOnViewSwitchListener(new ViewSwitchListener() {

			@Override
			public void onSwitched(View view, int position) {
				setPhotoInfo(position);
			}
		});
		int position = getIntent().getIntExtra(Extra.SelectedItem, 0);
		mViewFlow.setSelection(position);
		setPhotoInfo(position);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.page_btn_delete:
			dialog.show();
			break;
		default:
			break;
		}
	}

	class GalleryListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			tvTitle.setText((position + 1) + " of " + EventPhotoAddActivity.photos.size());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	}

	class ViewPagerAdatper extends BaseAdapter {

		@Override
		public int getCount() {
			return EventPhotoAddActivity.photos.size();
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Handler handler = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_view_flow, null);
				handler = new Handler();
				handler.iv = (ImageView) convertView.findViewById(R.id.item_iv_viewflow);
				handler.anim = (AnimationDrawable) AppContext.getRes().getDrawable(R.drawable.anim_loading_white);
				convertView.setTag(handler);
			} else {
				handler = (Handler) convertView.getTag();
				handler.anim.stop();
			}
			handler.iv.setImageDrawable(handler.anim);
			handler.iv.post(new AnimPost(handler.anim));
			loader.loadBitmap(EventPhotoAddActivity.photos.get(position), handler.iv);
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	public class Handler {
		public ImageView iv;
		public AnimationDrawable anim;
	}

	private void setPhotoInfo(int position) {
		curPosition = position;
		tvTitle.setText((position + 1) + " of " + EventPhotoAddActivity.photos.size());
	}
}
