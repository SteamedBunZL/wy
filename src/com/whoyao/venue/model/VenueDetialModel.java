package com.whoyao.venue.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author hyh creat_at：2014-2-19-上午10:12:16
 */
public class VenueDetialModel {
	private int venueid;

	private String fullname;
	private String logourl;
	private String telephone;
	private String description;
	private String businessstart;
	private String businessend;
	private int contractState;
	private boolean isreleaseplace;
	private float lowestprice;
	private float remark;

	private int district;
	private int city;
	private int province;
	private float latitude;
	private float longitude;
	private float distance;
	private String detailaddress;
	private String transportline;
	private int[] servicetypeList;
	private int[] suppleinfolist;
	private int[] placetypelist;
	private int cartcount;
	private int placeCount;
	private ArrayList<PlaceStatisticModel> reserveinfolist;
	private ArrayList<VenuePhotoModel> venuephotolist;

	public int getPlaceCount() {
		return placeCount;
	}

	public void setPlaceCount(int placeCount) {
		this.placeCount = placeCount;
	}

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

	public float getLowestprice() {
		return lowestprice;
	}

	public void setLowestprice(float lowestprice) {
		this.lowestprice = lowestprice;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getDistrict() {
		return district;
	}

	public void setDistrict(int district) {
		this.district = district;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public String getAddress() {
		return detailaddress;
	}

	public void setAddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBusinessstart() {
		return businessstart;
	}

	public void setBusinessstart(String businessstart) {
		this.businessstart = businessstart;
	}

	public String getBusinessend() {
		return businessend;
	}

	public void setBusinessend(String businessend) {
		this.businessend = businessend;
	}

	public float getRemark() {
		return remark;
	}

	public void setRemark(float remark) {
		this.remark = remark;
	}

	public int[] getServicetypeList() {
		return servicetypeList;
	}

	public void setServicetypeList(int[] servicetypeList) {
		this.servicetypeList = servicetypeList;
	}

	public int[] getSuppleinfolist() {
		return suppleinfolist;
	}

	public void setSuppleinfolist(int[] suppleinfolist) {
		this.suppleinfolist = suppleinfolist;
	}

	public ArrayList<PlaceStatisticModel> getReserveinfolist() {
		return reserveinfolist;
	}

	public void setReserveinfolist(
			ArrayList<PlaceStatisticModel> reserveinfolist) {
		this.reserveinfolist = reserveinfolist;
	}

	public String getTransportline() {
		return transportline;
	}

	public void setTransportline(String transportline) {
		this.transportline = transportline;
	}

	public ArrayList<VenuePhotoModel> getVenuephotolist() {
		return venuephotolist;
	}

	public void setVenuephotolist(ArrayList<VenuePhotoModel> venuephotolist) {
		this.venuephotolist = venuephotolist;
	}

	public int[] getPlacetypelist() {
		return placetypelist;
	}

	public void setPlacetypelist(int[] placetypelist) {
		this.placetypelist = placetypelist;
	}

	public int getCartcount() {
		return cartcount;
	}

	public void setCartcount(int cartcount) {
		this.cartcount = cartcount;
	}

	public int getContractState() {
		return contractState;
	}

	public void setContractState(int contractState) {
		this.contractState = contractState;
	}

	public boolean isIsreleaseplace() {
		return isreleaseplace;
	}

	public void setIsreleaseplace(boolean isreleaseplace) {
		this.isreleaseplace = isreleaseplace;
	}



}
