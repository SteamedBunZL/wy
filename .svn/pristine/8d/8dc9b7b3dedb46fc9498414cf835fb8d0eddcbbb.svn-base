package com.whoyao.model;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hyh 
 * creat_at：2013-9-9-下午3:35:59
 */
public class EventPhotoModel implements Parcelable{
	private int photoid;
	private int userid;
	private int activityid;
	private String photosubject;
	private String photopath;
	private String uploadtime;
	
	public EventPhotoModel() {
		super();
	}
	public EventPhotoModel(Map<String, Comparable> val) {
		super();
		photoid = (Integer) val.get("photoid");
		userid = (Integer) val.get("userid");
		activityid = (Integer) val.get("activityid");
		photosubject = (String) val.get("photosubject");
		photopath = (String) val.get("photopath");
		uploadtime = (String) val.get("uploadtime");
	}
	
	public int getPhotoid() {
		return photoid;
	}
	public void setPhotoid(int photoid) {
		this.photoid = photoid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getActivityid() {
		return activityid;
	}
	public void setActivityid(int activityid) {
		this.activityid = activityid;
	}
	public String getPhotosubject() {
		return photosubject;
	}
	public void setPhotosubject(String photosubject) {
		this.photosubject = photosubject;
	}
	public String getPhotopath() {
		return photopath;
	}
	public void setPhotopath(String photopath) {
		this.photopath = photopath;
	}
	public String getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		Map<String, Comparable> val = new HashMap<String, Comparable>();
		val.put("photoid", photoid);
		val.put("userid", userid);
		val.put("activityid", activityid);
		val.put("photosubject", photosubject);
		val.put("photopath", photopath);
		val.put("uploadtime", uploadtime);
		dest.writeMap(val);
	}
    public static final Parcelable.Creator<EventPhotoModel> CREATOR = new Parcelable.Creator<EventPhotoModel>() {   
        @Override  

        public EventPhotoModel createFromParcel(Parcel source) {   

        	@SuppressWarnings("unchecked")
			HashMap<String, Comparable> val = source.readHashMap(HashMap.class.getClassLoader());   
        	EventPhotoModel p = new EventPhotoModel(val);   
            return p;   
        }

		@Override
		public EventPhotoModel[] newArray(int size) {
			return new EventPhotoModel[size];
		}   
    }; 
}
