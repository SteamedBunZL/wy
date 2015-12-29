package com.whoyao.activity;

import com.whoyao.Const.Extra;
import com.whoyao.Const.Type;
import com.whoyao.AppContext;
import com.whoyao.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author hyh 
 * creat_at：2013-9-3-下午3:23:54
 */
public class EventAddGuideActivity extends BasicActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_guide);
		findViewById(R.id.event_guide_rl_imm).setOnClickListener(this);
		findViewById(R.id.event_guide_rl_normall).setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.event_guide_rl_imm:
			intent.putExtra(Extra.SelectedItem, Type.Category_Immediately);
			break;
		case R.id.event_guide_rl_normall:
			intent.putExtra(Extra.SelectedItem, Type.Category_Normal);
			break;
		default:
			break;
		}
		intent.setClass(this, EventAddActivity.class);
		AppContext.startAct(intent, true);
	}
}
