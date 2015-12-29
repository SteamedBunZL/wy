package com.whoyao.model;

/**
 * @author hyh 
 * creat_at：2013-11-12-上午10:09:01
 */
public class ResultModel {
	private int result;

	public boolean isSuccess() {
		return 1 == result;
	}
	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
