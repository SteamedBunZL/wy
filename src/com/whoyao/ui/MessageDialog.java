package com.whoyao.ui;

import com.whoyao.R;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author hyh creat_at：2013-7-24-下午2:44:32
 */
public class MessageDialog extends Dialog implements
		android.view.View.OnClickListener {

	private TextView tvTitle;
	private TextView tvMessage;
	private Button btnLeft;
	private Button btnRight;
	private android.view.View.OnClickListener leftListener;
	private android.view.View.OnClickListener rightListener;
	private TextView tvChecked;
	private String storeName;
	EditText etMessage;
	private SharedPreferences preferences;
	public static final int ID_LEFT_BUTTON = R.id.dialog_left;
	public static final int ID_RIGHT_BUTTON = R.id.dialog_right;
	public static final int ID_CHECKBOX = R.id.dialog_check;

	public MessageDialog(Context context) {
		super(context, R.style.custom_dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_message, null);
		setContentView(view);
		// this.setView(view, 0, 0, 0, 0);
		tvTitle = (TextView) view.findViewById(R.id.dialog_title);
		tvMessage = (TextView) view.findViewById(R.id.dialog_message);
		tvChecked = (TextView) view.findViewById(R.id.dialog_check);
		btnLeft = (Button) view.findViewById(R.id.dialog_left);
		btnRight = (Button) view.findViewById(R.id.dialog_right);
		etMessage = (EditText) view.findViewById(R.id.dialog_phone_message);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		tvChecked.setOnClickListener(this);

	}

	public void setTitle(String text) {
		tvTitle.setText(text);
	}

	public void setMessage(String text) {
		tvMessage.setText(text);
	}

	public void setHtmlMessage(String text) {
		tvMessage.setText(Html.fromHtml(text));
	}

	/**
	 * edittext输入号码
	 */
	public void setEditText() {
		tvMessage.setVisibility(View.GONE);
		//创建电话管理
		TelephonyManager tm = (TelephonyManager)
		//与手机建立连接
		getContext().getSystemService(Context.TELEPHONY_SERVICE);
		//获取手机号码
		String phoneId = tm.getLine1Number();
		etMessage.setVisibility(View.VISIBLE);
		etMessage.setText(phoneId);
		
	}
	public String getPhoneNumber(){
		return etMessage.getText().toString();
		
	}

	/** storeName不能为空 */
	public void setCheck(String text, SharedPreferences sp, String storeName) {
		if (null == storeName || null == sp) {
			throw new NullPointerException(
					"SharedPreferences and StoreName cannot be null");
		} else {
			this.storeName = storeName;
			this.preferences = sp;
		}
		if (!TextUtils.isEmpty(text)) {
			tvChecked.setText(text);
		}
		tvChecked.setVisibility(View.VISIBLE);
	}

	/** 左侧默认高亮,listener为null不保存Check */
	public void setLeftButton(String text,
			android.view.View.OnClickListener listener) {
		setLeftButton(text, listener, true);
	}

	/** 右侧侧默认不高亮,listener为null不保存Check */
	public void setRightButton(String text,
			android.view.View.OnClickListener listener) {
		setRightButton(text, listener, false);
	}

	/**
	 * @param text
	 * @param listener
	 * @param isHilite
	 *            是否高亮,默认true
	 */
	public void setLeftButton(String text,
			android.view.View.OnClickListener listener, boolean isHilite) {
		btnLeft.setVisibility(View.VISIBLE);
		btnLeft.setText(text);
		btnLeft.setSelected(!isHilite);
		leftListener = listener;
	}

	/**
	 * @param text
	 * @param listener
	 * @param isHilite
	 *            是否高亮,默认false
	 */
	public void setRightButton(String text,
			android.view.View.OnClickListener listener, boolean isHilite) {
		btnRight.setVisibility(View.VISIBLE);
		btnRight.setText(text);
		btnRight.setSelected(!isHilite);
		rightListener = listener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_left:
			dismiss();
			if (null != leftListener) {
				leftListener.onClick(v);
				storeCheckInfo();
			}
			break;
		case R.id.dialog_right:
			dismiss();
			if (null != rightListener) {
				rightListener.onClick(v);
				storeCheckInfo();
			}
			break;
		case R.id.dialog_check:
			v.setSelected(!v.isSelected());
			break;
		}
	}

	public boolean isChecked() {
		return tvChecked.isSelected();

	}

	private void storeCheckInfo() {
		if (View.VISIBLE == tvChecked.getVisibility()) {
			Editor edit = preferences.edit();
			edit.putBoolean(storeName, isChecked());
			edit.commit();
		}
	}

}
