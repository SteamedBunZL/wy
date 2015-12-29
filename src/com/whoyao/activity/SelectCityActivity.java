package com.whoyao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.db.AreaListDB;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.map.LocManager;
import com.whoyao.map.LocReceiver;
import com.whoyao.model.AreaData;
import com.whoyao.ui.LoadingDialog;
import com.whoyao.widget.LetterListView;
import com.whoyao.widget.LetterListView.OnTouchingLetterChangedListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SelectCityActivity extends BasicActivity implements OnClickListener, OnItemClickListener {
	private BaseAdapter adapter;
	private ListView mListView;
	private TextView overlay;
	private LetterListView letterListView;
	/** 存放存在的汉语拼音首字母和与之对应的列表位置 */
	private HashMap<String, Integer> alphaIndexer;
	/** 存放存在的汉语拼音首字母 */
	private String[] sections;
	private Handler handler;
	private OverlayThread overlayThread;
	private ArrayList<AreaData> cityList;
	private TextView tvLocCity;
	private TextView tvRegCity;
	private AreaData areaReg;
	private AreaData areaLoc;
	private AreaListDB db;
	private LoadingDialog dialog;
	private TextView tvRegTitle;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_citylist);
		intiView();
	}

	private void intiView() {
		mListView = (ListView) findViewById(R.id.city_lv);
		letterListView = (LetterListView) findViewById(R.id.city_letter);
		letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());

		handler = new Handler();
		overlayThread = new OverlayThread();
		initOverlay();

		db = new AreaListDB();
		dialog = LoadingDialog.getDialog(this);
		dialog.show();
		new Thread(){
			public void run() {
				cityList = db.getCityList();
				handler.post(new Runnable() {
					@Override
					public void run() {
						adapter = new ListAdapter(SelectCityActivity.this, cityList);
						mListView.setAdapter(adapter);
						findViewById(R.id.city_letter).setVisibility(View.VISIBLE);
						dialog.dismiss();
					}
				});
			};
		}.start();
		
		
		View view = View.inflate(this, R.layout.header_citylist, null);
		tvLocCity = (TextView) view.findViewById(R.id.city_header_tv_loc);
		tvRegCity = (TextView) view.findViewById(R.id.city_header_tv_reg);
		tvRegTitle = (TextView) view.findViewById(R.id.city_header_tv_reg_title);
		tvLocCity.setClickable(false);
//		tvRegCity.setClickable(false);
		tvLocCity.setOnClickListener(this);
		tvRegCity.setOnClickListener(this);
		mListView.setSelector(android.R.color.transparent);
		mListView.addHeaderView(view,null,false);
		
		
		mListView.setOnItemClickListener(this);
		if(null != MyinfoManager.getUserInfo() && 0 != MyinfoManager.getUserInfo().getUserCity()){
			areaReg = db.getAreaByCode(MyinfoManager.getUserInfo().getUserCity());
			tvRegCity.setText(areaReg.getAreaName());
			tvRegCity.setVisibility(View.VISIBLE);
			tvRegTitle.setVisibility(View.VISIBLE);
		}else{
			tvRegCity.setVisibility(View.GONE);
			tvRegTitle.setVisibility(View.GONE);
		}
		
		if(null != AppContext.location){
			tvLocCity.setText(AppContext.location.getCity());
			try {
				areaLoc = db.getAreaByName(AppContext.location.getCity());
			} catch (Exception e) {
				AppException.handle(e);
				finish();
				return;
			}
			if(null != areaLoc){
				tvLocCity.setClickable(true);
			}
		}else{
			LocReceiver receiver = new LocReceiver(LocReceiver.Success_And_Fail) {
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if(LOC_FAIL.equals(action)){
						tvLocCity.setText("城市定位失败");
					}else{
						tvLocCity.setText(AppContext.location.getCity());
						areaLoc = db.getAreaByName(AppContext.location.getCity());
						if(null != areaLoc){
							tvLocCity.setClickable(true);
						}
					}
				}
			};
			receiver.register();
			LocManager.getInstance().refresh();
		}
	}

	private class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<AreaData> list;

		public ListAdapter(Context context, List<AreaData> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			alphaIndexer = new HashMap<String, Integer>();
			alphaIndexer.put("#", 0);
			sections = new String[list.size()];

			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentStr = getAlpha(list.get(i).getPinYin());
				// 上一个汉语拼音首字母，如果不存在为“ ”
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1).getPinYin()) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getPinYin());
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_citylist, null);
				holder = new ViewHolder();
				// holder.line = convertView.findViewById(R.id.item_city_line);
				holder.alpha = (TextView) convertView.findViewById(R.id.item_city_tv_alpha);
				holder.name = (TextView) convertView.findViewById(R.id.item_city_tv_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AreaData item = list.get(position);
			holder.name.setText(item.getAreaName());
			// String previewStr = (position - 1) >= 0 ?
			// getAlpha(list.get(position - 1).getPinYin()) : " ";
			// if (!previewStr.equals(currentStr)) {
			if (null != sections[position]) {
				// holder.line.setVisibility(View.GONE);
				holder.alpha.setVisibility(View.VISIBLE);// 设置为可见
				String currentStr = getAlpha(list.get(position).getPinYin());
				holder.alpha.setText(currentStr);
			} else {
				// holder.line.setVisibility(View.VISIBLE);
				holder.alpha.setVisibility(View.GONE);
			}
			return convertView;
		}

		private class ViewHolder {
			//public View line;
			public TextView alpha;
			public TextView name;
		}

	}

	/** 初始化汉语拼音首字母弹出提示框 */
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		// 默认设置为不可见
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, params);
	}

	private class LetterListViewListener implements OnTouchingLetterChangedListener {

		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				if ("#".equals(s)) {
					mListView.setSelection(0);
				} else {
					int position = alphaIndexer.get(s);
					mListView.setSelection(position + mListView.getHeaderViewsCount());
				}
				// overlay.setText(sections[position]);
				overlay.setText(s);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}

	}

	/** 设置overlay不可见 */
	private class OverlayThread implements Runnable {

		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	/** 获得汉语拼音首字母 */
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(Locale.ENGLISH);
		} else {
			return "#";
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.city_header_tv_loc:
			setResultIntent(areaLoc);
			finish();
			break;
		case R.id.city_header_tv_reg:
			setResultIntent(areaReg);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AreaData areaData = cityList.get(position - ((ListView)parent).getHeaderViewsCount());
		setResultIntent(areaData);
		finish();
	}
	private void setResultIntent(AreaData area){
		if(null != area){
			Intent intent = new Intent();
			intent.putExtra(Extra.SelectedItem, area);
			setResult(RESULT_OK, intent);
		}
	}
}