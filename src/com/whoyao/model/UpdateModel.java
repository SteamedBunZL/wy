package com.whoyao.model;
/**
 * @author hyh 
 * creat_at：2013-7-3-上午10:02:19 <p>
 * 新版本信息
 */
public class UpdateModel {
	
	public int latestversion;
	public int minversion;
	public String lastversionname;
	public String minversionname;
	public String description;
	public String path;
	public String nowtime;
	
	public String getMinversionname() {
		return minversionname;
	}
	public String getLastversionname() {
		return lastversionname;
	}
	public void setLastversionname(String lastversionname) {
		this.lastversionname = lastversionname;
	}
	public void setMinversionname(String minversionname) {
		this.minversionname = minversionname;
	}
	public int getLatestversion() {
		return latestversion;
	}
	public void setLatestversion(int latestversion) {
		this.latestversion = latestversion;
	}
	public int getMinversion() {
		return minversion;
	}
	public void setMinversion(int minversion) {
		this.minversion = minversion;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
