package com.whoyao.service;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.baidu.location.f.c;
import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.WebApi;
import com.whoyao.utils.NeedToConnect;
import com.whoyao.utils.L;
import com.whoyao.utils.SocketClient;

public class Connect implements Runnable {
	private Socket client;
	private WYService service;


	public Connect(Socket client, WYService service) {
		super();
		this.client = client;
		this.service = service;
	}

	@Override
	public void run() {
		L.i(Const.ZL, "连接线程开启");
		while(SocketClient.getInstance().getPushCode()==null){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while (NeedToConnect.getInstance()) {
			if (NeedToConnect.getInstance()) {
				try {
					client = new Socket();
					client.connect(new InetSocketAddress(WebApi.Ims,
							WebApi.Port), 3000);
					if (client.isConnected()) {
						L.i(Const.ZL, "ims连接成功，等待接发数据了");
						String pushCode = SocketClient.getInstance()
								.getPushCode();
						SocketClient.getInstance().writeMessage(pushCode,
								client);
						// poolRead.execute(readMessageThread);
						NeedToConnect.needToReConnect(false);
						ConnectStatus.setStatus(true);
					}
					// needConnect = false;
				} catch (Exception e) {
					L.i(Const.ZL, "ims连接失败");
					e.printStackTrace();
					try {
						L.i(Const.ZL, "等待新连接");
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					// NeedToConnect.needToReConnect(true);
				}
			}
			if (NeedToConnect.getInstance()) {
				try {
					client.connect(new InetSocketAddress(WebApi.Ims1,
							WebApi.Port1), 3000);
					// TODO Ims连接失败后转而连接其它地址
					if (client.isConnected()) {
						L.i(Const.ZL, "ims1连接成功，等待接发数据了");
						// NeedToConnect.setStatus(true);
						String pushCode = SocketClient.getInstance()
								.getPushCode();
						SocketClient.getInstance().writeMessage(pushCode,
								client);
						// poolRead.execute(readMessageThread);
						NeedToConnect.needToReConnect(false);
						ConnectStatus.setStatus(true);
					}
				} catch (Exception e) {
					L.i(Const.ZL, "ims1连接失败");
					try {
						L.i(Const.ZL, "等待新连接");
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					// needConnect = true;
				}
			}
			if (NeedToConnect.getInstance()) {
				try {
					client.connect(new InetSocketAddress(WebApi.Ims2,
							WebApi.Port2), 3000);
					// TODO Ims连接失败后转而连接其它地址
					if (client.isConnected()) {
						L.i(Const.ZL, "ims连接成功，等待接发数据了");
						// NeedToConnect.setStatus(true);
						String pushCode = SocketClient.getInstance()
								.getPushCode();
						SocketClient.getInstance().writeMessage(pushCode,
								client);
						// poolRead.execute(readMessageThread);
						NeedToConnect.needToReConnect(false);
						ConnectStatus.setStatus(true);
					}
					// needConnect = false;
				} catch (Exception e) {
					L.i(Const.ZL, "ims2连接失败");
					try {
						L.i(Const.ZL, "等待新连接");
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					// needConnect = true;
				}
			}
		}
		if (ConnectStatus.getInstance()) {
			SocketClient.getInstance().readMessage(client, service);
		}
		L.i(Const.ZL, "连接线程关闭");
	}

}