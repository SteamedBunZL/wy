package com.whoyao.model;

import java.io.Serializable;

/**
 * Area数据Bean
 * @author HYH
 * create at：2013-3-28 上午11:19:27
 */
public class AreaData implements Serializable{
	private static final long serialVersionUID = -910034935124226128L;

	private int code;
	private String areaName;
	private int parentCode;
	private int haveChild;
	/*经纬度的值可以为NULL,不能使用float类型*/
	private Float longitude;
	private Float latitude;
	private String pinYin;
	
	public String getPinYin() {
		return pinYin;
	}
	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public int getParentCode() {
		return parentCode;
	}
	public void setParentCode(int parentCode) {
		this.parentCode = parentCode;
	}
	public Float getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	public int getHaveChild() {
		return haveChild;
	}
	public void setHaveChild(int haveChild) {
		this.haveChild = haveChild;
	}
	
	public boolean hasChild(){
		if(1 == haveChild){
			return true;
		}
		return false;
	}
	public boolean isProvince(){
		return 0 == parentCode;
	}
}
