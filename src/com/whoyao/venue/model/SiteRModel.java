package com.whoyao.venue.model;

import java.util.ArrayList;

public class SiteRModel {
//	ArrayList<E>
	private ArrayList<SiteVenueItem> venueList;
	private ConditionList conditionList;
	public ConditionList getConditionList() {
		return conditionList;
	}
	public void setConditionList(ConditionList conditionList) {
		this.conditionList = conditionList;
	}
	public ArrayList<SiteVenueItem> getVenueList() {
		return venueList;
	}
	public void setVenueList(ArrayList<SiteVenueItem> venueList) {
		this.venueList = venueList;
	}
	
	
}
