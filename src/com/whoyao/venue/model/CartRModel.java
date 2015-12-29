package com.whoyao.venue.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author hyh 
 * creat_at：2014-2-26-下午1:32:59
 */
public class CartRModel implements Serializable {
	private static final long serialVersionUID = 1926778055694873987L;
	private float totalAmount;
	private int totalItem;
	private int validItem;
	private ArrayList<VenueCartModel> VenueCartList;
	private CartSubItemModel [] CartDetailList;
	private VenueBaseInfoModel[] VenueList;
	private CartSubItemStateModel[] CheckError;
	public ArrayList<VenueCartModel> getVenueCartList() {
		return VenueCartList;
	}
	public CartSubItemModel[] getCartDetailList() {
		return CartDetailList;
	}
	public VenueBaseInfoModel[] getVenueList() {
		return VenueList;
	}
	public CartSubItemStateModel[] getCheckError() {
		return CheckError;
	}
	public int getValidItem() {
		return validItem;
	}
	public void setValidItem(int validItem) {
		this.validItem = validItem;
	}
	public int getTotalItem() {
		return totalItem;
	}
	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}
	public float getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}
}
