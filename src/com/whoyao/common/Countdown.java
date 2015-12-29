package com.whoyao.common;

import com.whoyao.AppException;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * @author hyh 
 * creat_at：2013-7-22-下午2:51:11
 */
public class Countdown {

	private TextView tv;
	private String hint;
	private Thread t;
	private String text;
	private Handler handler;
	private int times;

	public Countdown(int times,TextView tv,String hint) {
		this.times = times;
		this.tv = tv;
		this.hint = hint;
		this.text = tv.getText().toString();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(0 == msg.what){
					Countdown.this.tv.setEnabled(true);
					Countdown.this.tv.setText(Countdown.this.text);
				}else{
					Countdown.this.tv.setEnabled(false);
//					Countdown.this.tv.setText("") ;
					Countdown.this.tv.setText(msg.what + Countdown.this.hint) ;
				}
			}
		};
	}

	public void start() {
		new Thread(){

			@Override
			public void run() {
				for(int i = Countdown.this.times;i >= 0;i--){
					handler.sendEmptyMessage(i);
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						AppException.handle(e);
					}
				}

			}
			
		}.start();
		
	}
	
}
