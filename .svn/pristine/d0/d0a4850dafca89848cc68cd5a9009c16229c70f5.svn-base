package com.whoyao.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.whoyao.Const;
import com.whoyao.service.ConnectStatus;
import com.whoyao.service.WYService;

public class MonitorConnectThread extends Thread {
	private String address = null;
	private int port = 0;
	private Socket client;
	private WYService mService;

	public MonitorConnectThread(String address, int port, Socket client,WYService service) {
		super();
		this.address = address;
		this.port = port;
		this.client = client;
		this.mService = service;
	}

	public void run() {
		while (NeedToConnect.getInstance()) {
			
			while (ConnectStatus.getInstance()) {
				try {
					client.sendUrgentData(0xFF);
					L.i(Const.ZL, "socket连接正常");
					Thread.sleep(10000);
				} catch (Exception e) {
					L.i(Const.ZL, "socket连接中断,1秒后自动重连");
					ConnectStatus.setStatus(false);
				}
			}
			try {
				Thread.sleep(1000);
				//TODO 重连后重新发送pushkey
//				SocketClient.getInstance().connectSocket(mService);
////				SocketClient.connectSocket(mService);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		L.i(Const.ZL,"MonitorConnecteThread终止运行");
	}

}
