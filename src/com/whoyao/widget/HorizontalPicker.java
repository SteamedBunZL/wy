package com.whoyao.widget;

import java.util.ArrayList;

import com.whoyao.R;
import com.whoyao.utils.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * <strong>新增方法:</strong><br/>
 * setTextBackground()<br/>
 * getMaxValue() / setMaxValue()<br/>
 * getMinValue() / setMinValue()<br/>
 * getValue() / setValue()<br/>
 * 
 * @author hyh creat_at：2014-2-18-下午6:33:51
 */
public class HorizontalPicker extends TextView implements TextWatcher {
	private static final int DEF_PLUS = R.drawable.selector_plus;
	private static final int DEF_MINUS = R.drawable.selector_minus;
	private static final int DEF_PLUS_UNABLED = R.drawable.plus_unable;
	private static final int DEF_MINUS_UNABLED = R.drawable.minus_unable;
	private static final int DEF_TEXT_BACKGROUND = R.drawable.rectangle_has_stroke_gray;
	private static final String DEF_TEXT = "";

	private int mMinValue = 0;
	private int mMaxValue = Integer.MAX_VALUE;
	private int mValue = mMinValue;

	private boolean enablePluse = true;

	private boolean enableMinus = false;

	private CharSequence mDefaultStr = DEF_TEXT;
	private CharSequence mCurrentStr = mDefaultStr;

	private Drawable mPlusDrawable;
	private Drawable mMinusDrawable;
	private Drawable mMinusDrawableUnable;
	private Drawable mPlusDrawableUnable;
	private Drawable mTextBackground;

	private Rect mRect = new Rect();
	private int mTextBackgroundResource;
	private boolean isTouchMinus;
	private boolean isTouchPlus;
	private ArrayList<TextWatcher> mWatcher;

	public HorizontalPicker(Context context) {
		this(context, null);
	}

	public HorizontalPicker(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public HorizontalPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		readStyleParameters(context, attrs);
		init();
	}

	private void readStyleParameters(Context context, AttributeSet attributeSet) {
		TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.HorizontalPicker);
		try {
			mMinValue = a.getDimensionPixelSize(R.styleable.HorizontalPicker_MinValue, 0);
			mMaxValue = a.getDimensionPixelSize(R.styleable.HorizontalPicker_MaxValue, Integer.MAX_VALUE);
			mTextBackground = a.getDrawable(R.styleable.HorizontalPicker_TextBackground);
		} finally {
			a.recycle();
		}
	}

	private void init() {
		// 文字区域的背景
		mTextBackground = getResources().getDrawable(DEF_TEXT_BACKGROUND);
		// 获取DrawableRight,假如没有设置我们就使用默认的图片
		mMinusDrawable = getCompoundDrawables()[0];
		mPlusDrawable = getCompoundDrawables()[2];
		if (mMinusDrawable == null) {
			mMinusDrawable = getResources().getDrawable(DEF_MINUS);
		}
		if (mPlusDrawable == null) {
			mPlusDrawable = getResources().getDrawable(DEF_PLUS);
		}
		mMinusDrawableUnable = getResources().getDrawable(DEF_MINUS_UNABLED);
		mPlusDrawableUnable = getResources().getDrawable(DEF_PLUS_UNABLED);

		mMinusDrawable.setBounds(0, 0, mMinusDrawable.getIntrinsicWidth(), mMinusDrawable.getIntrinsicHeight());
		mPlusDrawable.setBounds(0, 0, mPlusDrawable.getIntrinsicWidth(), mPlusDrawable.getIntrinsicHeight());
		mMinusDrawableUnable.setBounds(0, 0, mMinusDrawableUnable.getIntrinsicWidth(),
				mMinusDrawableUnable.getIntrinsicHeight());
		mPlusDrawableUnable.setBounds(0, 0, mPlusDrawableUnable.getIntrinsicWidth(),
				mMinusDrawableUnable.getIntrinsicHeight());

		setCompoundDrawables(mMinusDrawable, getCompoundDrawables()[1], mPlusDrawable, getCompoundDrawables()[3]);

		mMaxValue = Integer.MAX_VALUE;// 应在addTextChangedListener给mMaxValue初始化
		super.addTextChangedListener(this);

		if (!TextUtils.isEmpty(getText())) {
			mValue = Integer.parseInt(getText().toString());
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mTextBackground != null) {
			mRect.left = getTotalPaddingLeft();
			mRect.top = getPaddingTop();
			mRect.right = getWidth() - getTotalPaddingRight();
			mRect.bottom = getHeight() - getPaddingBottom();
			if (mTextBackground != null) {
				mTextBackground.setBounds(mRect);
				mTextBackground.draw(canvas);
			}
		}
		if (isEnabled()) {
		}
		setMinusEnable(mValue > mMinValue);
		setPlusEnable(mValue < mMaxValue);
		super.onDraw(canvas);
	}

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (isFocusable()) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				determineTouchArea(event);
				if (isTouchMinus) {
					setPressed(mMinusDrawable, true);
				}
				if (isTouchPlus) {
					setPressed(mPlusDrawable, true);
				}
				return true;
			case MotionEvent.ACTION_UP:
				setPressed(mMinusDrawable, false);
				setPressed(mPlusDrawable, false);
				determineTouchArea(event);
				if (isTouchMinus) {
					if (mValue > mMinValue && enableMinus) {
						--mValue;
						setText(mValue + "");
					}
				}
				if (isTouchPlus) {
					if (mValue < mMaxValue && enablePluse) {
						++mValue;
						setText(mValue + "");
					}
				}
				return true;
			case MotionEvent.ACTION_CANCEL:
				setPressed(mMinusDrawable, false);
				setPressed(mPlusDrawable, false);
				return true;
			default:
				return super.onTouchEvent(event);
			}
		} else {
			return super.onTouchEvent(event);
		}
	}

	private void determineTouchArea(MotionEvent event) {
		isTouchMinus = event.getX() < getTotalPaddingLeft() && event.getX() > getPaddingLeft();
		isTouchPlus = event.getX() > (getWidth() - getTotalPaddingRight())
				&& (event.getX() < ((getWidth() - getPaddingRight())));
	}

    public void addTextChangedListener(TextWatcher watcher) {
        if (mWatcher == null) {
        	mWatcher = new ArrayList<TextWatcher>();
        }

        mWatcher.add(watcher);
    }
	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (!TextUtils.isEmpty(s)) {
			int value = 0;
			if (TextUtils.isDigitsOnly(s)) {
				value = Integer.parseInt(s.toString());
				mCurrentStr = s;
			} else {
				setText(mCurrentStr);
				return;
			}
			// TODO 可以添加一个判读,是否允许显示内容超出范围
//			boolean isAllowOutOfArea = false;
//			if (isAllowOutOfArea) {
//				if (value < mMinValue) {
//					value = mMinValue;
//				}
//				if (value > mMaxValue) {
//					value = mMaxValue;
//				}
//			}
			mValue = value;
			setMinusEnable(mValue > mMinValue);
			setPlusEnable(mValue < mMaxValue);
		} else {
			mCurrentStr = mDefaultStr;
		}
		
		if(mWatcher != null){
			int size = mWatcher.size();
			for(int i = 0; i < size; i++){
				mWatcher.get(i).onTextChanged(s, start, count, after);
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	public int getValue() {
		return mValue;
	}

	public void setValue(int value) {
		if (value > mMaxValue || value < mMinValue) {
			throw new NumberFormatException("value must be smaller than MaxValue and bigger than MinValue");
		}
		if (mValue != value) {
			setText(value + "");
		}
	}

	public int getMinValue() {
		return mMinValue;
	}

	public void setMinValue(int value) {
		mMinValue = value;
		if (mValue < value) {
			setText(value + "");
		}
	}

	public int getMaxValue() {
		return mMaxValue;
	}

	public void setMaxValue(int value) {
		mMaxValue = value;
		if (mValue > value) {
			setText(value + "");
		}
	}

	private void setMinusEnable(boolean minusable) {
		enableMinus = minusable;
		if (mMinusDrawable == null) {
			return;
		}
		setDrawableEnable(mMinusDrawable, minusable);
	}

	private void setPlusEnable(boolean plusable) {
		enablePluse = plusable;
		if (mPlusDrawable == null) {
			return;
		}
		setDrawableEnable(mPlusDrawable, plusable);
	}

	private void setDrawableEnable(Drawable dr, boolean enable) {
		if (enable) {
			Utils.addState(dr, android.R.attr.state_enabled);
		} else {
			Utils.removeState(dr, android.R.attr.state_enabled);
		}
	}

	private void setPressed(Drawable dr, boolean pressed) {
		if (dr == null) {
			return;
		}
		if (pressed) {
			Utils.addState(dr, android.R.attr.state_pressed);
		} else {
			Utils.removeState(dr, android.R.attr.state_pressed);
		}
	}

	public void setTextBackground(Drawable background) {
		mTextBackground = background;
		invalidate();
	}

	public void setTextBackgroundResource(int resid) {
		if (resid != 0 && resid == mTextBackgroundResource) {
			return;
		}
		Drawable d = null;
		if (resid != 0) {
			d = getResources().getDrawable(resid);
		}
		setTextBackground(d);
		mTextBackgroundResource = resid;
	}

	@SuppressLint("NewApi")
	public void setTextBackgroundColor(int color) {
		if (mTextBackground instanceof ColorDrawable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			((ColorDrawable) mTextBackground.mutate()).setColor(color);
			setTextBackground(mTextBackground);
			mTextBackgroundResource = 0;
		} else {
			setTextBackground(new ColorDrawable(color));
		}
	}
}
