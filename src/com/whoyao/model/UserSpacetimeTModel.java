package com.whoyao.model;

/**
 * @author hyh 
 * creat_at：2013-8-16-下午3:23:51
 */
public class UserSpacetimeTModel {
	public int beginminute;
	public int endminute;
	private int type;
	public int userprovince;
	public int usercity;
	public int userdistrict;
	public float userspacelongitude;
	public float userspacelatitude;
	public String userfulladdress;
	
	public UserSpacetimeTModel(int type) {
		this.type = type;
		
	}
	public UserSpacetimeTModel(UserSpacetimeRModel.UserAddressItem item) {
		type = item.Type;
		beginminute = item.BeginMinute;
		endminute = item.EndMinute;
		userprovince = item.UserProvince;
		usercity = item.UserCity;
		userdistrict = item.UserDistrict;
		userspacelatitude = item.UserSpaceLatitude;
		userspacelongitude = item.UserSpaceLongitude;
		userfulladdress = item.UserFullAddress;
	}

}
