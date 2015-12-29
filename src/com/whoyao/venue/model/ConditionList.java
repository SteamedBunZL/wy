package com.whoyao.venue.model;

import java.util.ArrayList;

public class ConditionList {
	private ArrayList<PriceList> price;
	private ArrayList<PlaceDataList> placedate;
	private ArrayList<PlaceTimeList> placetime;
	private ArrayList<AreaList> area;

	public ArrayList<PriceList> getPrice() {
		return price;
	}

	public void setPrice(ArrayList<PriceList> price) {
		this.price = price;
	}

	public ArrayList<PlaceDataList> getPlacedate() {
		return placedate;
	}

	public void setPlacedate(ArrayList<PlaceDataList> placedate) {
		this.placedate = placedate;
	}

	public ArrayList<PlaceTimeList> getPlacetime() {
		return placetime;
	}

	public void setPlacetime(ArrayList<PlaceTimeList> placetime) {
		this.placetime = placetime;
	}

	public ArrayList<AreaList> getArea() {
		return area;
	}

	public void setArea(ArrayList<AreaList> area) {
		this.area = area;
	}

}
