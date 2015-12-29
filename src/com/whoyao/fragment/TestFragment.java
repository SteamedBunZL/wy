package com.whoyao.fragment;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.EventDetialActivity;
import com.whoyao.activity.EventJoinerVerifyActivity;
import com.whoyao.activity.InviteFriendActivity;
import com.whoyao.activity.SelectMapPointActivity;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.message.service.MessageService;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;
import com.whoyao.venue.OrderConfirmActivity;
import com.whoyao.venue.VenueDetialActivity;
import com.whoyao.venue.WebPayAcitivity;
import com.whoyao.venue.engine.PayEngine;
import com.whoyao.venue.engine.alipay.Result;
import com.whoyao.venue.model.OrderInfoModel;
import com.whoyao.widget.HorizontalPicker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/**
 * @author hyh creat_at：2013-9-3-下午4:42:56
 */
public class TestFragment extends Fragment implements Runnable {
	private IoSession session;
	private NioSocketConnector connector;
	private Serializable addrModel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_test, container, false);
		initTest(v);
		EventDetialManager.getInstance().getDetial(1, new CallBack<String>() {
			@Override
			public void onCallBack(boolean isSuccess, String data) {
			}
		});
		return v;
	}

	private void initTest(View v) {
		if (Const.STATUS_RELEASE == AppContext.AppState) {
			return;
		}
		// HorizontalPicker picker = new HorizontalPicker(getActivity());
		v.findViewById(R.id.main_test_sc).setVisibility(View.VISIBLE);
		TestOnClickListener listener = new TestOnClickListener();
		v.findViewById(R.id.button200).setOnClickListener(listener);
		v.findViewById(R.id.button400).setOnClickListener(listener);
		v.findViewById(R.id.test_btn_sendvcode).setOnClickListener(listener);
		v.findViewById(R.id.test_btn_checkvcode).setOnClickListener(listener);
		v.findViewById(R.id.test_btn_login).setOnClickListener(listener);
		v.findViewById(R.id.test_btn_checkaccount).setOnClickListener(listener);
		v.findViewById(R.id.test_btn_checkupdate).setOnClickListener(listener);
		v.findViewById(R.id.test_btn_regist).setOnClickListener(listener);
		v.findViewById(R.id.test_btn_loginact).setOnClickListener(listener);
		v.findViewById(R.id.dctv).setOnClickListener(listener);
		/*
		 * Mina
		 */
		// connector = new NioSocketConnector();
		// // TextLineCodecFactory factory = new
		// TextLineCodecFactory(Charset.forName("UTF-8"),
		// LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue());
		// TextLineCodecFactory factory = new
		// TextLineCodecFactory(Charset.forName("UTF-8"));
		// factory.setDecoderMaxLineLength(Integer.MAX_VALUE);
		// factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
		// connector.getFilterChain().addLast("codec", new
		// ProtocolCodecFilter(new TimeResponseCodecFactory(true)));
		// IMIOHandler handler = new IMIOHandler();
		// connector.setConnectTimeoutMillis(30000); // 设置连接超时时间
		// connector.setHandler(handler); // 设置默认的通讯处理器

		// new Thread(this).start();

	}

	/*
	 * Mina
	 */
	public static class IMIOHandler extends IoHandlerAdapter {
		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			L.i(Const.AppName, "收到:" + message.toString());
			super.messageReceived(session, message);
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			L.i(Const.AppName, "服务器断开");
			super.sessionClosed(session);
		}

	}

	// 测试数据
	public class TestOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button200:
				Net.request(null, WebApi.Api_Addr, new ResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, content);
					}

					@Override
					public void onFailure(Throwable e, int statusCode,
							String content) {
						// TODO Auto-generated method stub
						super.onFailure(e, statusCode, content);
					}

				});
				// Intent intentWeb = new
				// Intent(getActivity(),WebPayAcitivity.class);
				// intentWeb.putExtra("url",
				// "http://10.10.1.137:40008/venue/test");
				// startActivity(intentWeb);
				// setupInexactAlarm(false);
				// session.write("点击");
				// new Thread(HomeFragment.this).start();
				// HashMap<String, String> map = new HashMap<String, String>();
				// map.put("aa", "aaa");
				// map.put("bb", "bbb");
				// String json = JSON.toJson(map);
				// MessageNoticeRModel msg = new MessageNoticeRModel();
				// msg.NoticeListItem = new ArrayList<NoticeListItem>();
				// msg.NoticeListItem.add(new NoticeListItem());
				// String json = JSON.toJson(msg);
				// Net.request(null, WebApi.Common.GetServerTime, null);
				// L.i(Const.AppName, "点击:   " );
				// Handler handler = new Handler(){
				// public void handleMessage(android.os.Message msg) {
				// Result result = new Result((String) msg.obj);
				//
				// switch (msg.what) {
				// case Const.ALIYPE_RQF_PAY:
				// Toast.show(result.getResult());
				// break;
				// default:
				// break;
				// }
				// };
				// };
				// OrderInfoModel info = new OrderInfoModel();
				// info.setBody("我的第一个测试Body");
				// info.setEncodeNotify_url("http://www.whoyao.com/pay/notify_url");
				// info.setOut_trade_no("whoyao_dingdan_123456");
				// info.setTotal_fee("0.01");
				// PayEngine.startAliPayClient(getActivity(), info, handler);

				break;
			case R.id.button400:
				Intent intent = new Intent(getActivity(),
						SelectMapPointActivity.class);
				if (addrModel != null) {
					intent.putExtra(Extra.AddrModel, addrModel);
				}
				startActivityForResult(intent, 400);
				break;
			case R.id.test_btn_sendvcode:
				AppContext.startAct(EventJoinerVerifyActivity.class);
				break;
			case R.id.test_btn_checkvcode:
				// LoadingDialog dialog2 = LoadingDialog.getDialog();
				// dialog2.setMessage("aaaddddd");
				// dialog2.show();
				break;
			case R.id.test_btn_login:
				AppContext.startAct(InviteFriendActivity.class);
				break;
			case R.id.test_btn_checkaccount:

				// Response:
				// {"userEmail":true,"userNickName":true,"userPhone":true}
				break;
			case R.id.test_btn_checkupdate:
				break;
			case R.id.test_btn_regist:
				// RegisterModel regInfo = new RegisterModel();
				// regInfo.userphone = "18910080301";
				// regInfo.userpassword = "a123456";
				// regInfo.username = "h18910080301";
				// regInfo.userprovince = 110000;
				// regInfo.usercity = 110108;
				// regInfo.usercounty = 1101081;
				//
				//
				// UserEngine.register(regInfo);
				break;
			case R.id.test_btn_loginact:
				break;
			default:
				break;
			}
		}

	}

	@SuppressWarnings("unused")
	private void setupInexactAlarm(boolean cancel) {
		L.i(Const.AppName, "SetupAlarm");
		AlarmManager am = (AlarmManager) AppContext.context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(AppContext.context, MessageService.class);
		PendingIntent pendingIntent = PendingIntent.getService(getActivity(),
				0, intent, 0);

		if (cancel) {
			am.cancel(pendingIntent);
		} else {
			long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
			long firstInterval = DateUtils.MINUTE_IN_MILLIS;
			am.setInexactRepeating(AlarmManager.RTC, firstInterval, interval,
					pendingIntent);
		}
	}

	/*
	 * Mina
	 */
	@Override
	public void run() {
		if (session == null) {
			ConnectFuture future = connector.connect(new InetSocketAddress(
					"10.10.1.142", 9999)); // 连接服务端
			future.awaitUninterruptibly(); // 等待连接成功
			session = future.getSession();
			session.write("登陆");
		} else {
			session.write("点击:" + AppContext.serviceTimeMillis());
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 400:
			if (resultCode == Activity.RESULT_OK && data != null) {
				addrModel = data.getSerializableExtra(Extra.AddrModel);
			}
			break;

		default:
			break;
		}
	}

}
