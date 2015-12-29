package com.whoyao.venue.model;

public class MyBillItem {
	private String venuename;
	private long ordernumber;
	private String ordertime;
	private double money;
	private int orderstate;
	private long orderid;
	private long[] payid;

	public String getVenuename() {
		return venuename;
	}

	public void setVenuename(String venuename) {
		this.venuename = venuename;
	}

	public long getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(long ordernumber) {
		this.ordernumber = ordernumber;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getOrderstate() {
		return orderstate;
	}

	public void setOrderstate(int orderstate) {
		this.orderstate = orderstate;
	}

	public long getOrderid() {
		return orderid;
	}

	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}

	public long[] getPayid() {
		return payid;
	}

	public void setPayid(long[] payid) {
		this.payid = payid;
	}

	

}
