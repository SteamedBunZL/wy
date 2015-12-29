package com.whoyao.model;

import java.util.ArrayList;

/**
 * @author hyh creat_at：2013-8-21-下午4:27:51
 */
public class UserSpacetimeRModel {
	public ArrayList<UserAddressItem> UserAddress;

	public static class UserAddressItem {
		public int Type;
		public int UserProvince;
		public int UserCity;
		public int UserDistrict;
		public int BeginMinute;
		public int EndMinute;
		public float UserSpaceLongitude;
		public float UserSpaceLatitude;
		public String UserFullAddress;
	}
}
