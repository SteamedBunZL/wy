package com.whoyao.venue.model;

/**
 * @author hyh 
 * creat_at：2014-2-21-下午1:54:48
 */
public class PlaceStatisticModel {
	private int placestatisticid;
	private int placetype;
	private int placetypedetail;
	private String placedate;
	private int placetime;
	private float originalprice;
	private float discount;
	private float price;
	private int remainder;
	private int totalcount;
	private boolean isAdded;
	
	public int getPlacestatisticid() {
		return placestatisticid;
	}
	public void setPlacestatisticid(int placestatisticid) {
		this.placestatisticid = placestatisticid;
	}
	public int getPlacetype() {
		return placetype;
	}
	public void setPlacetype(int placetype) {
		this.placetype = placetype;
	}
	public int getPlacetypedetail() {
		return placetypedetail;
	}
	public void setPlacetypedetail(int placetypedetail) {
		this.placetypedetail = placetypedetail;
	}
	public String getPlacedate() {
		return placedate;
	}
	public void setPlacedate(String placedate) {
		this.placedate = placedate;
	}
	public int getPlacetime() {
		return placetime;
	}
	public void setPlacetime(int placetime) {
		this.placetime = placetime;
	}
	public float getOriginalprice() {
		return originalprice;
	}
	public void setOriginalprice(float originalprice) {
		this.originalprice = originalprice;
	}
	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getRemainder() {
		return remainder;
	}
	public void setRemainder(int remainder) {
		this.remainder = remainder;
	}
	public int getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}
	public boolean isAdded() {
		return isAdded;
	}
	public void setAdded(boolean isAdded) {
		this.isAdded = isAdded;
	}
	
	
}
