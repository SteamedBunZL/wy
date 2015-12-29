package com.whoyao.venue.model;

/**
 * @author hyh creat_at：2014-2-19-上午10:12:16
 */
public class VenueItemModel {
	private int venueid;
	private String fullname;
	private String logourl;
	private String address;
	private int[] servicetype;

	private float lowestprice;
	private float markvalue;
	private float distance;
	private float latitude;
	private float longitude;

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

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public float getLowestprice() {
		return lowestprice;
	}

	public void setLowestprice(float lowestprice) {
		this.lowestprice = lowestprice;
	}

	public float getMarkvalue() {
		return markvalue;
	}

	public void setMarkvalue(float markvalue) {
		this.markvalue = markvalue;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public int[] getServicetype() {
		return servicetype;
	}

	public void setServicetype(int[] servicetype) {
		this.servicetype = servicetype;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

}
