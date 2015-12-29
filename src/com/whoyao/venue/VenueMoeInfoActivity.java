package com.whoyao.venue;

import java.io.File;
import java.util.ArrayList;
import org.taptwo.widget.CircleFlowIndicator;
import org.taptwo.widget.ViewFlow;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.activity.BasicActivity;
import com.whoyao.model.EventPhotoModel;
import com.whoyao.venue.engine.VenueDetialManager;
import com.whoyao.venue.engine.VenuePhotosAdapter;
import com.whoyao.venue.model.VenueDetialModel;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VenueMoeInfoActivity extends BasicActivity implements OnClickListener {

	protected ArrayList<EventPhotoModel> mListImage;
	private TextView tvTitle;
	protected int userID;
	private ViewFlow vfPhoto;
	private TextView tvDesc;
	private TextView tvSuppleNone;
	private TextView tvTrans;
	private TextView[] tvSupples;
	private Drawable[] mSuppleIcons;
	private CircleFlowIndicator mIndicator;
	public static ArrayList<String> photos;
	
//	private static final int[] SUPPLE_ICONS = AppContext.getRes().getIntArray(R.array.venue_supple_icon);
	private static final int[] SUPPLE_ICONS = { R.drawable.venue_supple_park, R.drawable.venue_supple_taxi,
			R.drawable.venue_supple_box, R.drawable.venue_supple_rest, R.drawable.venue_supple_bath,
			R.drawable.venue_supple_sell };
	private static final String[] SUPPLE_NAMES = AppContext.getRes().getStringArray(R.array.venue_supple_name);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_venue_more_info);

		initView();
		initData();
	}

	private void initView() {
		photos = new ArrayList<String>();
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.page_tv_title);
		vfPhoto = (ViewFlow) findViewById(R.id.venue_info_vp);
		mIndicator = (CircleFlowIndicator) findViewById(R.id.venue_info_ci);
		vfPhoto.setFlowIndicator(mIndicator);
		tvDesc = (TextView) findViewById(R.id.venue_info_tv_desc);
		tvSuppleNone = (TextView) findViewById(R.id.venue_info_tv_none_supple);
		tvSupples = new TextView[6];
		tvSupples[0] = (TextView) findViewById(R.id.venue_info_tv_supple_0);
		tvSupples[1] = (TextView) findViewById(R.id.venue_info_tv_supple_1);
		tvSupples[2] = (TextView) findViewById(R.id.venue_info_tv_supple_2);
		tvSupples[3] = (TextView) findViewById(R.id.venue_info_tv_supple_3);
		tvSupples[4] = (TextView) findViewById(R.id.venue_info_tv_supple_4);
		tvSupples[5] = (TextView) findViewById(R.id.venue_info_tv_supple_5);
		tvTrans = (TextView) findViewById(R.id.venue_info_tv_transport);
		Resources res = getResources();
		int length = SUPPLE_ICONS.length;
		mSuppleIcons = new Drawable[length];
		for (int i = 0; i < length; i++) {
			Drawable dr = res.getDrawable(SUPPLE_ICONS[i]);
			dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
			mSuppleIcons[i] = dr;
			Log.i("互邀!!!", ""+dr);
		}
	}

	private void initData() {
		VenueDetialModel data = VenueDetialManager.getInstance().getData();
		if(data != null){
			tvTitle.setText(data.getFullname());
			if (data.getDescription()==null||TextUtils.equals(data.getDescription(), "")) {
				tvDesc.setText("商家很忙，还没有顾上填写信息呢！");
			}else {
				tvDesc.setText(data.getDescription());
			}
			if (data.getDescription()==null||TextUtils.equals(data.getDescription(), "")) {
				tvTrans.setText("商家很忙，还没有顾上填写信息呢！");
			}else {
				tvTrans.setText(data.getTransportline());
			}
			int[] supples = data.getSuppleinfolist();
			if(supples == null || supples.length == 0 ){
				tvSuppleNone.setVisibility(View.VISIBLE);
			}else{
				tvSuppleNone.setVisibility(View.GONE);
				for(int i = 0; i < supples.length; i++){
					Drawable left = mSuppleIcons[supples[i] - 1];
					tvSupples[i].setCompoundDrawables(left, null, null, null);
					tvSupples[i].setText(SUPPLE_NAMES[supples[i] - 1]);
					tvSupples[i].setVisibility(View.VISIBLE);
					
				}
				for(int i = supples.length;i< SUPPLE_ICONS.length;i++){
					tvSupples[i].setVisibility(View.INVISIBLE);
				}
			}
			for (int i = 0; i < data.getVenuephotolist().size(); i++) {
				photos.add(data.getVenuephotolist().get(i).getPhotopath());
			}
			vfPhoto.setAdapter(new VenuePhotosAdapter(context, data.getVenuephotolist()));
			
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.topic_detial_tv_nick:
		case R.id.topic_detial_iv_face:
		default:
			break;
		}
	}
}
