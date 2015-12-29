package com.whoyao.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.utils.Utils;

/**
 * @author hyh 
 * creat_at：2013-8-7-下午5:26:29
 */
public 	class TextNumWatcher implements TextWatcher {
	
	private TextView numTextView;
	private int maxNum;
	private int defaultColor;
	private int warnColor;

	public TextNumWatcher(TextView numTextView, int maxNum) {
		this.numTextView = numTextView;
		this.maxNum = maxNum;
		defaultColor = AppContext.getRes().getColor(R.color.gray_text);
		warnColor = AppContext.getRes().getColor(R.color.red);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		;
		if(s.length() > maxNum){
			numTextView.setTextColor(warnColor);
		}else{
			numTextView.setTextColor(defaultColor);
		}
		numTextView.setText(s.toString().length()+"/"+maxNum);
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		
	}
}
