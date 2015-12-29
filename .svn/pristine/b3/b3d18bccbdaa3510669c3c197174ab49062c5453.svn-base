package com.whoyao.activity;

import com.whoyao.R;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

/**
 * 选择控件的父类
 * 
 * @author hyh creat_at：2013-8-8-上午11:47:28
 */
public class AbsSelectActivity extends BasicActivity {
	private View selectLayout;
	private View backgroundLayout;
	private Animation showAnim;
	private Animation hideAnim;
	private boolean hasFinish = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Translucent_NoTitleBar); // 这里设置主题未生效
		backgroundLayout = getWindow().getDecorView().findViewById(android.R.id.content);

		initAnim();
	}

	/**
	 * 显示选择界面
	 */
	public void show(boolean hasAnimation) {
		if (hasAnimation) {
			selectLayout.startAnimation(showAnim);
		} else {
			selectLayout.setVisibility(View.VISIBLE);
			backgroundLayout.setBackgroundResource(R.color.black_t);
		}
	}

	/**
	 * 隐藏选择界面
	 */
	public void hide(boolean hasAnimation) {
		this.hasFinish = false;
		if (hasAnimation) {
			selectLayout.startAnimation(hideAnim);
		} else {
			selectLayout.setVisibility(View.INVISIBLE);
			backgroundLayout.setBackgroundResource(android.R.color.transparent);
		}
	}

	/**
	 * 显示选择界面
	 */
	public void show() {
		show(true);
	}

	/**
	 * 隐藏选择界面
	 */
	public void hide() {
		hide(true);
	}

	public void dismiss() {
		this.hasFinish = true;
		selectLayout.startAnimation(hideAnim);
	}

	private void initAnim() {
		showAnim = AnimationUtils.loadAnimation(this, R.anim.plugin_show);
		showAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				backgroundLayout.setBackgroundResource(R.color.black_t);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});

		hideAnim = AnimationUtils.loadAnimation(this, R.anim.plugin_hide);
		hideAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				selectLayout.setVisibility(View.INVISIBLE);
				if (hasFinish) {
					backgroundLayout.setBackgroundResource(android.R.color.transparent);
					finish();
				}
			}
		});
	}

	protected void setSelectView(View selectLayout) {
		this.selectLayout = selectLayout;
	}

	protected void setBackgroundView(View backgroundLayout) {
		this.backgroundLayout = backgroundLayout;
	}

	@Override
	protected boolean onBack() {
		selectLayout.startAnimation(hideAnim);
		return true;

	}
}
