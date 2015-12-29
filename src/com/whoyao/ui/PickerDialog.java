package com.whoyao.ui;

import com.whoyao.R;
import com.whoyao.venue.model.CartAddItemTModel;
import com.whoyao.venue.model.PlaceStatisticModel;
import com.whoyao.widget.HorizontalPicker;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author hyh creat_at：2013-7-24-下午2:44:32
 * @param <T>
 */
public class PickerDialog<T> extends Dialog implements android.view.View.OnClickListener {

	private TextView tvTitle;
	private Button btnLeft;
	private Button btnRight;
	private android.view.View.OnClickListener leftListener;
	private android.view.View.OnClickListener rightListener;
	private TextView tvChecked;
	private HorizontalPicker mPicker;
	private T mTag;
	public static final int ID_LEFT_BUTTON = R.id.dialog_left;
	public static final int ID_RIGHT_BUTTON = R.id.dialog_right;
	public static final int ID_CHECKBOX = R.id.dialog_check;

	public PickerDialog(Context context) {
		super(context, R.style.custom_dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_picker, null);
		setContentView(view);
		// this.setView(view, 0, 0, 0, 0);
		tvTitle = (TextView) view.findViewById(R.id.dialog_title);
		mPicker = (HorizontalPicker) view.findViewById(R.id.dialog_picker);
		mPicker.setValue(1);
		btnLeft = (Button) view.findViewById(R.id.dialog_left);
		btnRight = (Button) view.findViewById(R.id.dialog_right);
		btnLeft.setTag(this);
		btnRight.setTag(this);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		mPicker.setOnClickListener(this);
	}

	public void setTitle(String text) {
		tvTitle.setText(text);
	}

	/** storeName不能为空 */
	public void setCheck(String text, SharedPreferences sp, String storeName) {
		if (null == storeName || null == sp) {
			throw new NullPointerException("SharedPreferences and StoreName cannot be null");
		} else {
		}
		if (!TextUtils.isEmpty(text)) {
			tvChecked.setText(text);
		}
		tvChecked.setVisibility(View.VISIBLE);
	}

	/** 左侧默认高亮,listener为null不保存Check */
	public void setLeftButton(String text, android.view.View.OnClickListener listener) {
		setLeftButton(text, listener, true);
	}

	/** 右侧侧默认不高亮,listener为null不保存Check */
	public void setRightButton(String text, android.view.View.OnClickListener listener) {
		setRightButton(text, listener, false);
	}

	/**
	 * @param text
	 * @param listener
	 * @param isHilite
	 *            是否高亮,默认true
	 */
	public void setLeftButton(String text, android.view.View.OnClickListener listener, boolean isHilite) {
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
	public void setRightButton(String text, android.view.View.OnClickListener listener, boolean isHilite) {
		btnRight.setVisibility(View.VISIBLE);
		btnRight.setText(text);
		btnRight.setSelected(!isHilite);
		rightListener = listener;
	}

	public HorizontalPicker getPicker() {
		return mPicker;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_left:
			dismiss();
			if (null != leftListener ) {
				leftListener.onClick(v);
			}
			break;
		case R.id.dialog_right:
			dismiss();
			if (null != rightListener) {
				rightListener.onClick(v);
			}
			break;
		case R.id.dialog_picker:
			Toast.show("");
			break;
		default:
			break;
		}
	}
	public void setTag(T tag) {
		mTag = tag;
	}
	public T getTag() {
		return mTag;
	}

}
