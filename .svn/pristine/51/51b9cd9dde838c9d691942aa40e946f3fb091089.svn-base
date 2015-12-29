package com.whoyao.model;

import com.whoyao.AppContext;
import com.whoyao.Const;

/**
 * @author hyh 
 * creat_at：2014-2-19-下午2:53:04
 */
public class LocationPagingModel {
	private double longitude;
	private double latitude;
	private int pagesize = Const.PAGE_SIZE;
	private int pageindex = Const.PAGE_DEFAULT_INDEX;


	public LocationPagingModel() {
		if(null != AppContext.location){
			latitude = AppContext.location.getLatitude();
			longitude = AppContext.location.getLongitude();
		}
	}
	
	public void refreshLocation(){
		if(null != AppContext.location){
			latitude = AppContext.location.getLatitude();
			longitude = AppContext.location.getLongitude();
		}
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getPageindex() {
		return pageindex;
	}
	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}
}
