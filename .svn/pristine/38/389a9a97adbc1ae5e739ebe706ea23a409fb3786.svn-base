package com.whoyao.activity;

import org.taptwo.widget.CircleFlowIndicator;
import org.taptwo.widget.ViewFlow;
import com.whoyao.R;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.utils.Utils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
/**
 * 引导页
 * @author hyh 
 * creat_at：2013-8-27-下午3:23:32
 */
public class GuideActivity extends BasicActivity implements OnClickListener {

	private ViewFlow mViewFlow;
	private CircleFlowIndicator mIndicator;
	private int helpImages[];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.setFullScreen(this);
		helpImages = new int[] {R.drawable.help1,R.drawable.help2,R.drawable.help3,R.drawable.help4,R.drawable.help5};
		setContentView(R.layout.activity_guide);
		mViewFlow = (ViewFlow) findViewById(R.id.guide_vf);

		mViewFlow.setAdapter(new BaseAdapter() {
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView iv = null;
				if(null == convertView){
					iv = new ImageView(context);
					iv.setLayoutParams(lp);
					iv.setScaleType(ScaleType.FIT_XY);
				}else{
					iv = (ImageView) convertView;
				}
				iv.setImageResource(helpImages[position]);
				if(position+1 == helpImages.length){
					iv.setOnClickListener(GuideActivity.this);
				}else{
					iv.setOnClickListener(null);
				}
				return iv;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return null;
			}
			
			@Override
			public int getCount() {
				return helpImages.length;
			}
		});
		mIndicator = (CircleFlowIndicator) findViewById(R.id.guide_fi);
		mViewFlow.setFlowIndicator(mIndicator);
	}

	@Override
	public void onClick(View v) {
		UserEngine.login();
	}
}