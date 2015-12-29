package com.whoyao.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.adapter.FreeAdapter;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.FreeModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.ui.Toast;

/**
 * 查看我的闲人预告
 * @author hyh creat_at：2013-7-30-上午11:00:47
 */
public class MyFreeActivity extends BasicActivity implements OnClickListener {
	private ListView listview;
	private FreeAdapter freeAdapter;
	private List<FreeModel> freeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_free);
		initView();
	}

	private void initView() {
		findViewById(R.id.myfree_btn_back).setOnClickListener(this);
		findViewById(R.id.myfree_btn_add).setOnClickListener(this);
		listview = (ListView)findViewById(R.id.myfree_listview);
		freeList = MyinfoManager.getManager().getFree();
		freeAdapter = new FreeAdapter(freeList);
		listview.setAdapter(freeAdapter);

	}

	@Override
	protected void onResume() {
		super.onResume();
		MyinfoManager.getManager().getMyDetil(true,new CallBack<UserDetialModel>(){
			@Override
			public void onCallBack() {
				List<FreeModel> list = MyinfoManager.getManager().getFree();
				if(freeList.size() != list.size()){
					freeList.clear();
					freeList.addAll(list);
				}
				freeAdapter.notifyDataSetChanged();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myfree_btn_back:
			finish();
			break;
		case R.id.myfree_btn_add:
			if(4<MyinfoManager.getManager().getFree().size()){
				Toast.show("最多只能发布5条闲人预告");
			}else {
				AppContext.startAct(MyFreeAddActivity.class);
			}
			break;
		default:
			break;
		}
		
	}
	
//	@Override
//	public String toString() {
//		return "查看我的闲人预告";
//	}
	
}
