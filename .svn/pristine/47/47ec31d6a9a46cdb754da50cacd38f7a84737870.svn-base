package com.whoyao.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.whoyao.Const.KEY;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.TagItemModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.Utils;

/**
 * 添加兴趣标签
 * 
 * @author huiyh 2013-8-18上午8:56:44
 */
public class MyTagAddActivity extends BasicActivity implements OnClickListener,
		TextWatcher, OnFocusChangeListener {

	private ArrayList<EditText> etList;
	private EditText etCurrent;
	private int tagNum;
	private int listTagNum;
	private TextView tvNum;
	int currentNum = 0;
	private EditText lastEdit;
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_tab_add);
		tvNum = (TextView) findViewById(R.id.tv_num);
		tvTitle = (TextView) findViewById(R.id.title);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.myat_btn_save).setOnClickListener(this);
		etList = new ArrayList<EditText>();
		etList.add(((EditText) findViewById(R.id.myat_et_1)));
		etList.add(((EditText) findViewById(R.id.myat_et_2)));
		etList.add(((EditText) findViewById(R.id.myat_et_3)));
		etList.add(((EditText) findViewById(R.id.myat_et_4)));
		etList.add(((EditText) findViewById(R.id.myat_et_5)));
		etList.add(((EditText) findViewById(R.id.myat_et_6)));
		etList.add(((EditText) findViewById(R.id.myat_et_7)));
		etList.add(((EditText) findViewById(R.id.myat_et_8)));
		etList.add(((EditText) findViewById(R.id.myat_et_9)));
		etList.add(((EditText) findViewById(R.id.myat_et_10)));

		for (EditText et : etList) {
			et.addTextChangedListener(this);
			et.setOnFocusChangeListener(this);
		}

		ArrayList<TagItemModel> tags = MyinfoManager.getManager().interestTags;
		if (null != tags) {
			for (TagItemModel tag : tags) {
				if (tag.isSelected()) {
					tagNum++;
					System.out.println("**tagNum**" + tagNum);
				}
			}
		}
		tvNum.setText("还能添加" + (10 - tagNum) + "个兴趣标签");
		if (0 != tagNum) {
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!v.isEnabled()) {
						Toast.show("兴趣标签最多10个");
					}
				}
			};
			for (int i = etList.size() - tagNum; i < etList.size(); i++) {
				// etList.get(i).setEnabled(false);
				etList.get(i).setOnClickListener(listener);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.myat_btn_save:
			int checknum = 0;
			for (int k = 0; k < etList.size(); k++) {
				String tagCheck = etList.get(k).getText().toString();
				if (tagCheck.length() > 8) {
					checknum++;
				}
			}
			if (checknum > 0) {
				Toast.show("标签字数为1-8个字，支持汉字和英文字母");
				return;
			}
			String tagNames = MyinfoManager.getManager().getTagNames();
			String tagIds = "";
			List<EditText> contentList = new ArrayList<EditText>();
			for (int i = 0; i < etList.size(); i++) {
				String contentStr = etList.get(i).getText().toString();
				EditText et = etList.get(i);
				if (contentStr.length() != 0) {
					contentList.add(et);
				}
			}
			for (int i = 0; i < contentList.size(); i++) {
				String tagStr = contentList.get(i).getText().toString();

				int et = 0;
				for (int j = 0; j < etList.size(); j++) {
					if (etList.get(j).getText().toString().length() != 0
							|| etList.get(j).getText().toString() == null) {
						et++;
					}
				}
				if (et + tagNum > 10) {
					Toast.show(R.string.warn_tag_full);
					return;
				}
				if (!TextUtils.isEmpty(tagStr)) {
					tagNames = tagNames + "," + tagStr;
					tagIds = tagIds + "," + 0;
				}
			}
			if (TextUtils.isEmpty(tagIds)) {
				finish();
				return;
			}
			tagIds = MyinfoManager.getManager().getTagIds() + tagIds;
			RequestParams params = new RequestParams();
			params.put(KEY.TagIDs, tagIds);
			params.put(KEY.TagNames, tagNames);

			Net.request(params, WebApi.User.UpTags, new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					NetCache.clearCaches();
					MyinfoManager.getManager().getMyDetil(true,
							new CallBack<UserDetialModel>() {
								@Override
								public void onCallBack() {
									Toast.show("标签添加成功");
									finish();
								}
							});
				}
			});
			break;
		default:
			break;
		}
	}

	// @Override
	// public String toString() {
	// return "添加自定义兴趣标签";
	// }
	public int getEtNum() {
		int et = 0;
		for (int j = 0; j < etList.size(); j++) {
			if (etList.get(j).getText().toString().length() != 0
					|| etList.get(j).getText().toString() == null) {
				et++;
			}
		}
		return et;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		currentNum = 10 - (getEtNum() + tagNum);
		if (currentNum < 1) {
			tvNum.setTextColor(Color.RED);
			tvNum.setText("兴趣标签最多10个，您已经添加满了");
		}else {
			tvNum.setTextColor(Color.BLACK);
			tvNum.setText("还能添加" + currentNum + "个兴趣标签");
		}
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		currentNum = 10 - (getEtNum() + tagNum);
		if (currentNum < 0) {
			tvNum.setTextColor(Color.RED);
			tvNum.setText("兴趣标签最多10个，您已经添加满了");
		}else {
			tvNum.setTextColor(Color.BLACK);
			tvNum.setText("还能添加" + currentNum + "个兴趣标签");
		}
		System.out.println("**start**"+start);
		System.out.println("**before**"+before);
		System.out.println("**count**"+count);
		
		if (null != etCurrent) {
			int length = Utils.getStringLength(etCurrent.getText().toString());
//			if (8 < length) {
			if(length>8&&start<9){
				Toast.show("标签字数1-8个字，支持汉字和英文字母");
				etCurrent.setTextColor(Color.RED);
			}else if(length<9){
				etCurrent.setTextColor(Color.BLACK);
			}
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
//		getCurrentFocus()
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// if(hasFocus){
		// etCurrent = (EditText) v;
		// //etCurrent.setSelection(etCurrent.getText().length());
		// }else{
		// if(v.equals(etCurrent)){
		// etCurrent = null;
		// }
		// }
		// int et = 0;
		// for (int i = 0; i < etList.size(); i++) {
		// if (etList.get(i).getText().toString().length() != 0
		// || etList.get(i).getText().toString() == null) {
		// et++;
		// }
		// }
//		if (hasFocus) {
////			if (lastEdit != null) {
////				int length = Utils.getStringLength(lastEdit.getText()
////						.toString());
////				
////					if (v!=lastEdit&&length>8) {
////						v.clearFocus();
//////						tvTitle.requestFocusFromTouch();
////						Toast.show("标签字数1-8个字，支持汉字和英文字母");
////				
////					// etCurrent.setText(Utils.getSubString(etCurrent.getText()
////					// .toString(), 8));
////					// etCurrent.setSelection(etCurrent.getText().length());
////				}
////			}
////			lastEdit = (EditText) v;
//		} else {
//
//		}
		
		if (hasFocus) {
			etCurrent = (EditText)v;
			
		} else {
			lastEdit = (EditText) v;
			int length = Utils.getStringLength(lastEdit.getText().toString());
			if (8 < length) {
				Toast.show("标签字数1-8个字，支持汉字和英文字母");
				lastEdit.setTextColor(Color.RED);
				// etCurrent.setText(Utils.getSubString(etCurrent.getText()
				// .toString(), 8));
				// etCurrent.setSelection(etCurrent.getText().length());
			}else {
				lastEdit.setTextColor(Color.BLACK);
			}
		}
		// EditText currentEdit = (EditText) getCurrentFocus();
		// if (v.equals(currentEdit)
		// && currentEdit.getText().toString().length() == 0) {
		// int length = Utils.getStringLength(etCurrent.getText().toString());
		// if (8 < length) {
		// Toast.show("标签字数1-8个字，支持汉字和英文字母");
		// etCurrent.setText(Utils.getSubString(etCurrent.getText()
		// .toString(), 8));
		// etCurrent.setSelection(etCurrent.getText().length());
		// }
		// }
	}
}
