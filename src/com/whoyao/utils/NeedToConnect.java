package com.whoyao.utils;

import java.util.Set;

import com.whoyao.AppContext;

public class NeedToConnect {
	private static boolean status = true;
	private NeedToConnect(){
		
	}
	public static void needToReConnect(boolean status) {
		NeedToConnect.status = status;
	}
	public static boolean getInstance(){
		return status;
	}

}
