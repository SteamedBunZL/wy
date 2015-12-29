package com.whoyao.venue.model;

/**
 * @author hyh 
 * creat_at：2014-2-28-下午2:49:24
 */
public class PayTModel {
	private float paketpay;
	private float accountpay;
	private float otherpay;
	private float paytotalamount;
	private String payid;
	
	public float getPaketpay() {
		return paketpay;
	}
	public void setPaketpay(float paketpay) {
		this.paketpay = paketpay;
	}
	public float getAccountpay() {
		return accountpay;
	}
	public void setAccountpay(float accountpay) {
		this.accountpay = accountpay;
	}
	public float getOtherpay() {
		return otherpay;
	}
	public void setOtherpay(float otherpay) {
		this.otherpay = otherpay;
	}
	public float getPaytotalamount() {
		return paytotalamount;
	}
	public void setPaytotalamount(float paytotalamount) {
		this.paytotalamount = paytotalamount;
	}
	public String getPayid() {
		return payid;
	}
	public void setPayid(String payid) {
		this.payid = payid;
	}
}
