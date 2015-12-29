package com.whoyao.activity;

import java.net.CacheRequest;

import com.whoyao.R;
import com.whoyao.model.MessageInviteTModel;
import com.whoyao.net.Net;

import android.os.Bundle;

public class NetReturnTestActivity extends BasicActivity{
	private MessageInviteTModel model;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_net);
		model= new MessageInviteTModel();
		model.setPageindex(1);
		model.setPagesize(20);
		
	}
	

}
