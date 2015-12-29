package com.whoyao.model;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 照片信息
 * @author hyh 
 * creat_at：2013-8-20-下午2:38:22
 */
public class UserPhotoModel implements Parcelable {
	/**
	 * 
	 */
	private int PhotoID;
	private String PhotoSubject;
	private String PhotoPath;
	private String PhotoDescription;
	public UserPhotoModel(){
		super();
	}
	public UserPhotoModel(HashMap<String, Comparable> val) {
		PhotoID = (Integer) val.get("PhotoID");
		PhotoSubject = (String) val.get("PhotoSubject");
		PhotoPath = (String) val.get("PhotoPath");
		PhotoDescription = (String) val.get("PhotoDescription");
	}
	public int getPhotoID() {
		return PhotoID;
	}
	public void setPhotoID(int photoID) {
		PhotoID = photoID;
	}
	public String getPhotoSubject() {
		return PhotoSubject;
	}
	public void setPhotoSubject(String photoSubject) {
		PhotoSubject = photoSubject;
	}
	public String getPhotoPath() {
		return PhotoPath;
	}
	public void setPhotoPath(String photoPath) {
		PhotoPath = photoPath;
	}
	public String getPhotoDescription() {
		return PhotoDescription;
	}
	public void setPhotoDescription(String photoDescription) {
		PhotoDescription = photoDescription;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		Map<String, Comparable> val = new HashMap<String, Comparable>();
		val.put("PhotoID", PhotoID);
		val.put("PhotoSubject", PhotoSubject);
		val.put("PhotoPath", PhotoPath);
		val.put("PhotoDescription", PhotoDescription);
		
		dest.writeMap(val);
	}
    public static final Parcelable.Creator<UserPhotoModel> CREATOR = new Parcelable.Creator<UserPhotoModel>() {   
        @Override  
        public UserPhotoModel createFromParcel(Parcel source) {   
        	@SuppressWarnings("unchecked")
			HashMap<String, Comparable> val = source.readHashMap(HashMap.class.getClassLoader());   
        	UserPhotoModel p = new UserPhotoModel(val);   
            return p;   
        }

		@Override
		public UserPhotoModel[] newArray(int size) {
			return null;
		}   
    };

	@Override
	public int describeContents() {
		return 0;
	} 
}
