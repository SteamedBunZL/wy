package com.whoyao.utils;

import java.io.*;
import java.net.*;

public class ClientSocket {
	static Socket client;

	public ClientSocket(String site, int port) {
		try {
			// 实例化socket传入ip和端口号，两边端口号要统一
			client = new Socket(site, port);
			System.out.println("Client is created! site:" + site + " port:"
					+ port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String sendMsg(String msg) {
		try {
			// BufferedReader in = new BufferedReader(new InputStreamReader(
			// client.getInputStream()));
			// 将字符串转成输出流然后发送出去
			PrintWriter out = new PrintWriter(client.getOutputStream());
			out.println(msg);
			out.flush();
			// return in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void closeSocket() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}