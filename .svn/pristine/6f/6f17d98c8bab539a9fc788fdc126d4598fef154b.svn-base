package com.whoyao.venue.engine;

import com.alipay.android.app.sdk.AliPay;
import com.whoyao.Const;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

/**
 * @author hyh 
 * creat_at：2014-3-2-下午12:20:36
 */
public class AlipayThread extends Thread {

	private Activity context;
	private String mOrderInfo;
	private Handler mHandler;

	public AlipayThread(Activity context, String orderInfo, Handler handler) {
		super();
		this.context = context;
		mOrderInfo = orderInfo;
		mHandler = handler;
	}

	@Override
	public void run() {
		AliPay alipay = new AliPay(context, mHandler);
		
		//设置为沙箱模式，不设置默认为线上环境
		//alipay.setSandBox(true);

		String result = alipay.pay(mOrderInfo);

		Message msg = new Message();
		msg.what = Const.ALIYPE_RQF_PAY;
		msg.obj = result;
		mHandler.sendMessage(msg);
	}
	
}
