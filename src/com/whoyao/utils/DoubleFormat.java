package com.whoyao.utils;

import java.text.DecimalFormat;

public class DoubleFormat {
	public static String format(double x){  
		//10的位数次方 如保留2位则
//		tempDouble=100  
		DecimalFormat df = new DecimalFormat("0.00");
		
		String result = df.format(x);
		return result;
	}

}
