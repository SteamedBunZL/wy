package com.whoyao.map;

import java.io.Serializable;
import java.util.ArrayList;

import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * @author hyh 
 * creat_at：2013-12-19-下午2:27:47
 */
@SuppressWarnings("serial")
public class AddrInfoModel implements Serializable{
	private static final long serialVersionUID = -4272754992883588345L;
	
	private String desc;

	private int latitudeE6;

	private int longitudeE6;

	private String city;

	private String district;

	private String province;

	private String street;

	private String streetNumber;

	private GeoPoint gp;

	public GeoPoint getGeoPoint(){
		if(gp == null){
			gp = new GeoPoint(latitudeE6, longitudeE6);
		}
		return gp;
	}
	public void setGeoPint(GeoPoint gp){
		if(null != gp){
			latitudeE6 = gp.getLatitudeE6();
			longitudeE6 = gp.getLongitudeE6();
		}
		this.gp = gp;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getStrAddr() {
		return strAddr;
	}

	public void setStrAddr(String strAddr) {
		this.strAddr = strAddr;
	}

	public String getStrBusiness() {
		return strBusiness;
	}

	public void setStrBusiness(String strBusiness) {
		this.strBusiness = strBusiness;
	}


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private String strAddr;

	private String strBusiness;


	private int type;

	public AddrInfoModel(MKAddrInfo info) {
		super();
		setInfo(info,"");
	}
	
	public void setInfo(MKAddrInfo info,String desc){
		this.desc = desc;
		
		setLatitudeE6(info.geoPt.getLatitudeE6());
		setLongitudeE6(info.geoPt.getLongitudeE6());
		strAddr = info.strAddr;
		strBusiness = info.strBusiness;
		type = info.type;
		
		MKGeocoderAddressComponent ac = info.addressComponents;
		province = ac.province;
		city = ac.city;
		district = ac.district;
		street = ac.street;
		streetNumber = ac.streetNumber;
		
	}

	public int getLatitudeE6() {
		return latitudeE6;
	}

	public void setLatitudeE6(int latitudeE6) {
		this.latitudeE6 = latitudeE6;
	}

	public int getLongitudeE6() {
		return longitudeE6;
	}

	public void setLongitudeE6(int longitudeE6) {
		this.longitudeE6 = longitudeE6;
	}
}
