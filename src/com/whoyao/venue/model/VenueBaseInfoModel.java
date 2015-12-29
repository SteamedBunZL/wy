package com.whoyao.venue.model;

/**
 * @author hyh 
 * creat_at：2014-2-26-下午1:34:19
 */
public class VenueBaseInfoModel {
	private int venueid;
	private String fullname;
	private String shortname;
	
	public int getVenueid() {
		return venueid;
	}
	public void setVenueid(int venueid) {
		this.venueid = venueid;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	@Override
	public String toString() {
		return "VenueBaseInfoModel [venueid=" + venueid + ", fullname="
				+ fullname + ", shortname=" + shortname + "]";
	}
	
	
	
}
