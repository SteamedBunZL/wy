package com.whoyao.service;

import java.io.DataInputStream;
import java.net.Socket;

import android.text.TextUtils;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.utils.L;
import com.whoyao.utils.NeedToConnect;
import com.whoyao.utils.SocketClient;

public class ReadThread implements Runnable {
	private Socket client;
	private WYService service;

	public ReadThread(Socket client, WYService service) {
		this.client = client;
		this.service = service;
	}

	@Override
	public void run() {
		DataInputStream in = null;
		L.i(Const.ZL, "读取线程开启");
		while (ConnectStatus.getInstance()) {
			int readLength;
			String message;
			try {
				in = new DataInputStream(client.getInputStream());
				readLength = SocketClient.getInstance().toInt(
						SocketClient.getInstance().readBytes(in, 4));
				message = new String(SocketClient.getInstance().readBytes(in,
						readLength)).trim();
				SocketClient.getInstance().handleMessage(message, service,
						client);
				L.i(Const.ZL, message);
			} catch (Exception e) {
				ConnectStatus.setStatus(false);
				if (TextUtils.equals(ConnectBreakReason.getInstance(),
						Const.REASON_OVER_TIME)) {
					L.i(Const.ZL, "Reason : " + Const.REASON_OVER_TIME);
					ConnectBreakReason.setReason(Const.REASON_OVER_TIME);
					SocketClient.getInstance().connectSocket(service);

				} else if (TextUtils.equals(ConnectBreakReason.getInstance(),
						Const.REASON_KICK_OUT)) {
					L.i(Const.ZL, "Reason : " + Const.REASON_KICK_OUT);
					ConnectBreakReason.setReason(Const.REASON_KICK_OUT);
				} else if (TextUtils.equals(ConnectBreakReason.getInstance(),
						Const.REASON_KEY_OVERDUE)) {
					L.i(Const.ZL, "Reason : " + Const.REASON_KEY_OVERDUE);
					ConnectBreakReason.setReason(Const.REASON_KEY_OVERDUE);

				} else {
					if (!AppContext.isNetAvailable()) {
						L.i(Const.ZL, "Reason: " + Const.REASON_NET_ERROR);
						ConnectStatus.setStatus(false);
						NeedToConnect.needToReConnect(false);
					} else if (AppContext.isNetAvailable()) {
						L.i(Const.ZL,
								"连接失败原因：" + ConnectBreakReason.getInstance());
						if (!TextUtils.equals(ConnectBreakReason.getInstance(),
								Const.REASON_LOGOUT)) {
							L.i(Const.ZL,
									"Reason : "
											+ ConnectBreakReason.getInstance());
							ConnectStatus.setStatus(false);
							ConnectBreakReason
									.setReason(Const.REASON_READ_EXCEPTION);
							NeedToConnect.needToReConnect(true);
							SocketClient.getInstance().connectSocket(service);

						}
					}
				}

			}
		}
		L.i(Const.ZL, "读取线程关闭");
	}

}
