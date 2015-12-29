package com.whoyao.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.WebApi;
import com.whoyao.activity.TransActivity;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.service.Connect;
import com.whoyao.service.ConnectBreakReason;
import com.whoyao.service.ConnectStatus;
import com.whoyao.service.PushPrivateModel;
import com.whoyao.service.ReadThread;
import com.whoyao.service.WYService;
import com.whoyao.ui.MessageDialog;

public class SocketClient {
	private static SocketClient socket = new SocketClient();

	private SocketClient() {
		queneConnect = new LinkedBlockingQueue<Runnable>();
		poolConnect = new ThreadPoolExecutor(1, 50, 180, TimeUnit.SECONDS,
				queneConnect);
		socketList = new ArrayList<Socket>();
	}

	public static SocketClient getInstance() {
		return socket;
	}

	private ThreadPoolExecutor poolConnect;

	private LinkedBlockingQueue<Runnable> queneConnect;

	private ArrayList<Socket> socketList;

	public List<Socket> getSocketList() {
		return socketList;
	}

	public Socket connectSocket(WYService service) {
		if (socketList.size() > 0) {
			closeSocket(socketList.get(0));
		}
		Socket client = new Socket();
		socketList.add(client);
		NeedToConnect.needToReConnect(true);
		Connect connect = new Connect(client, service);
		poolConnect.execute(connect);
		return client;
	}

	public void closeSocket(Socket client) {
		try {
			client.close();
			client = null;
			socketList.remove(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket monitorConnect(Socket client, WYService service) {
		Socket monitorClient = client;
		if (monitorClient == null) {
			monitorClient = new Socket();
		}
		MonitorConnectThread monitorConnectThread = new MonitorConnectThread(
				WebApi.Ims, WebApi.Port, monitorClient, service);
		monitorConnectThread.start();
		return monitorClient;
	}

	public byte[] readBytes(InputStream in, long length) throws IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int read = 0;
		while (read < length) {
			int cur = in.read(buffer, 0, (int) Math.min(1024, length - read));
			if (cur < 0) {
				break;
			}
			read += cur;
			bo.write(buffer, 0, cur);
		}
		return bo.toByteArray();
	}

	public int toInt(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;
		for (int i = 0; i < bRefArr.length; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	public void readMessage(final Socket client, final WYService service) {
		ReadThread readThread = new ReadThread(client, service);
		poolConnect.execute(readThread);

	}

	public void readMessage1(final Socket client, final WYService service) {
		ReadThread readThread = new ReadThread(client, service);
		Thread t = new Thread(readThread);
		t.start();

	}

	public void handleMessage(String message, WYService service, Socket client) {
		JSONObject json = null;
		try {
			if (message == null || message.equals("")) {
				return;
			}
			json = new JSONObject(message);
			int t = (Integer) json.get("t");
			switch (t) {
			case 1:
				L.i(Const.ZL, "超时断开");
				ConnectStatus.setStatus(false);
				ConnectBreakReason.setReason(Const.REASON_OVER_TIME);
				connectSocket(service);
				break;
			case 2:
				L.i(Const.ZL, "其它客户端踢除");
				ConnectStatus.setStatus(false);
				ConnectBreakReason.setReason(Const.REASON_KICK_OUT);
				// TODO 判断在客户端在是否在前台？
				AppContext.startAct(TransActivity.class);
				break;
			case 3:
				L.i(Const.ZL, "密钥需更新");
				// NeedToConnect.setStatus(false);
				ConnectBreakReason.setReason(Const.REASON_KEY_OVERDUE);
				ClientEngine.storeSocketKey(null);
				break;
			case 4:
				L.i(Const.ZL, "消息统计");
				break;
			case 11000:
				L.i(Const.ZL, "私信");
				PushPrivateModel privateModel = new PushPrivateModel();
				privateModel.setT(11000);
				privateModel.setCid(json.getInt("cid"));
				privateModel.setSid(json.getInt("sid"));
				privateModel.setSna(json.getString("sna"));
				privateModel.setCon(json.getString("con"));
				String privateMessage = "0" + JSON.toJson(privateModel);
				// TODO 存入数据库
				service.newMessage(privateMessage);
				break;
			case 11001:
				L.i(Const.ZL, "通知");
				// {"sid":8265,"sna":"zzl1990","cid":10201,"con":"添加好友","t":11001}
				PushPrivateModel privateModel1 = new PushPrivateModel();
				privateModel1.setT(11001);
				privateModel1.setCid(json.getInt("cid"));
				privateModel1.setSid(json.getInt("sid"));
				privateModel1.setSna(json.getString("sna"));
				privateModel1.setCon(json.getString("con"));
				String noticeMessage = "1" + JSON.toJson(privateModel1);
				service.newMessage(noticeMessage);
				break;
			case 11002:
				L.i(Const.ZL, "邀请");
				PushPrivateModel privateModel2 = new PushPrivateModel();
				privateModel2.setT(11002);
				privateModel2.setCid(json.getInt("cid"));
				privateModel2.setSid(json.getInt("sid"));
				privateModel2.setSna(json.getString("sna"));
				privateModel2.setCon(json.getString("con"));
				String inviteMessage = "2" + JSON.toJson(privateModel2);
				service.newMessage(inviteMessage);
				break;
			default:
				L.i(Const.ZL, "活动");
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void writeMessage(String pushCode, Socket client) {
		byte[] body;
		int length;
		byte[] bodyLength;
		try {
			body = pushCode.getBytes("utf-8");
			length = pushCode.length();
			bodyLength = UserEngine.integerToBytes(length, 4);
			OutputStream outs = client.getOutputStream();
			outs.write(bodyLength);
			outs.write(body);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getPushCode() {
		String pushKey = ClientEngine.initSocketKey();
		String pushCode =null;
		if (pushKey == null) {
			pushCode = ClientEngine.downAndGetSocketKey();
			L.i(Const.ZL, "下载pushkey..." + pushCode);
		}else {
			pushCode = "{\"u\":" + MyinfoManager.getUserInfo().getUserID() + "," + "\"k\":" + "\"" + pushKey
					+ "\"" + "\"n\":" + "\""
					+ ClientEngine.getMobileInfo().getMobileNum() + "\"" + "}";
		}
		L.i(Const.ZL,"pushcode:"+pushCode);
		return pushCode;
	}
}
