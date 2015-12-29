package com.whoyao.service;

public class ConnectStatus {
	private static boolean status = true;
	private ConnectStatus(){
		
	}
	public static void setStatus(boolean status) {
		ConnectStatus.status = status;
	}

	public static boolean getInstance(){
		return status;
	}

}
