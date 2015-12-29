package com.whoyao.venue.model;

import java.util.ArrayList;

public class BillInformation {
	private String time;
	private ArrayList<BillVenueItemModel> detail;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ArrayList<BillVenueItemModel> getDetail() {
		return detail;
	}

	public void setDetail(ArrayList<BillVenueItemModel> detail) {
		this.detail = detail;
	}

}
