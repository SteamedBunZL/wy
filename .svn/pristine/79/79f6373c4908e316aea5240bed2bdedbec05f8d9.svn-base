package com.whoyao.model;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.engine.event.EventEngine;
import com.whoyao.utils.CalendarUtils;

/**
 * 活动详情中活动信息
 * @author hyh 
 * creat_at：2013-9-9-下午3:22:55
 */
public class EventInfoModel {
	private int userid;
	private int activityid;
	private int typeid;
	private String title;
	private String begintime;
	private String endtime;
	private int userprovince;
	private int usercity;
	private int userdistrict;
	private String address;
	private float longitude;
	private float latitude;
	private int minnum;
	private int maxnum;
	private int predictminconsume;
	private int predictmaxconsume;
	private int paytype;
	private String keyword;
	private String activitydescription;
	private int convertstate;
	private String activitypicture;
	private int sex;
	private int minage;
	private int maxage;
	private int joinnum;
	private int activitystate;
	private float atmosphereavgvalue;
	private float attendedavgvalue;
	private float priceavgvalue;
	private float addressavgvalue;
	
	
	private float activityatmospherevalue;
	private float activityattendedvalue;
	private float activitypricevalue;
	private float activityaddressvalue;

	private int activityprogressstate;
	private int commentcount;
	private int interestcount;
	private int isinterest;
	
	public float getActivityatmospherevalue() {
		return activityatmospherevalue;
	}
	public void setActivityatmospherevalue(float activityatmospherevalue) {
		this.activityatmospherevalue = activityatmospherevalue;
	}
	public float getActivityattendedvalue() {
		return activityattendedvalue;
	}
	public void setActivityattendedvalue(float activityattendedvalue) {
		this.activityattendedvalue = activityattendedvalue;
	}
	public float getActivitypricevalue() {
		return activitypricevalue;
	}
	public void setActivitypricevalue(float activitypricevalue) {
		this.activitypricevalue = activitypricevalue;
	}
	public float getActivityaddressvalue() {
		return activityaddressvalue;
	}
	public void setActivityaddressvalue(float activityaddressvalue) {
		this.activityaddressvalue = activityaddressvalue;
	}
	
	public int getUserid() {
		return userid;
	}
	public float getAtmosphereavgvalue() {
		return atmosphereavgvalue;
	}
	public void setAtmosphereavgvalue(float atmosphereavgvalue) {
		this.atmosphereavgvalue = atmosphereavgvalue;
	}
	public float getAttendedavgvalue() {
		return attendedavgvalue;
	}
	public void setAttendedavgvalue(float attendedavgvalue) {
		this.attendedavgvalue = attendedavgvalue;
	}
	public float getPriceavgvalue() {
		return priceavgvalue;
	}
	public void setPriceavgvalue(float priceavgvalue) {
		this.priceavgvalue = priceavgvalue;
	}
	public float getAddressavgvalue() {
		return addressavgvalue;
	}
	public void setAddressavgvalue(float addressavgvalue) {
		this.addressavgvalue = addressavgvalue;
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
	public int getTypeid() {
		return typeid;
	}
	public String getType() {
		String[] array = AppContext.getRes().getStringArray(R.array.event_tags);
		if(0<typeid && array.length>=typeid ){
			return array[typeid-1];
		}
		return null;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getEndtime() {
		return endtime;
	}
	public long getEndtimeMillis() {
		return CalendarUtils.parseDate(endtime);
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public int getUserprovince() {
		return userprovince;
	}
	public void setUserprovince(int userprovince) {
		this.userprovince = userprovince;
	}
	public int getUsercity() {
		return usercity;
	}
	public void setUsercity(int usercity) {
		this.usercity = usercity;
	}
	public int getUserdistrict() {
		return userdistrict;
	}
	public void setUserdistrict(int userdistrict) {
		this.userdistrict = userdistrict;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public int getMinnum() {
		return minnum;
	}
	public void setMinnum(int minnum) {
		this.minnum = minnum;
	}
	public int getMaxnum() {
		return maxnum;
	}
	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}
	public int getPredictminconsume() {
		return predictminconsume;
	}
	public void setPredictminconsume(int predictminconsume) {
		this.predictminconsume = predictminconsume;
	}
	public int getPredictmaxconsume() {
		return predictmaxconsume;
	}
	public void setPredictmaxconsume(int predictmaxconsume) {
		this.predictmaxconsume = predictmaxconsume;
	}
	public int getPaytype() {
		return paytype;
	}
	public String getPaytypeDesc() {
		String[] array = AppContext.getRes().getStringArray(R.array.pay_type);
		if(0<paytype && array.length>=paytype ){
			return array[paytype-1];
		}
		return null;
	}
	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getActivitydescription() {
		return activitydescription;
	}
	public void setActivitydescription(String activitydescription) {
		this.activitydescription = activitydescription;
	}
	public int getConvertstate() {
		return convertstate;
	}
	public void setConvertstate(int convertstate) {
		this.convertstate = convertstate;
	}
	public String getActivitypicture() {
		return activitypicture;
	}
	public void setActivitypicture(String activitypicture) {
		this.activitypicture = activitypicture;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getMinage() {
		return minage;
	}
	public void setMinage(int minage) {
		this.minage = minage;
	}
	public int getMaxage() {
		return maxage;
	}
	public void setMaxage(int maxage) {
		this.maxage = maxage;
	}
	public int getJoinnum() {
		return joinnum;
	}
	public void setJoinnum(int joinnum) {
		this.joinnum = joinnum;
	}
	public int getActivitystate() {
		return activitystate;
	}
	public void setActivitystate(int activitystate) {
		this.activitystate = activitystate;
	}

	/**氛围描述*/
	public String getAtmosphereDesc() {
		return EventEngine.getAtmosphereDesc(atmosphereavgvalue);
	}
	/**人员出席分值描述*/
	public String getAttendedDesc() {
		return EventEngine.getAttendedDesc(attendedavgvalue);
	}

	/**活动费用分值描述*/
	public String getPriceDesc() {
		return EventEngine.getPriceDesc(priceavgvalue);
	}

	/**活动环境分值描述*/
	public String getAddressDesc() {
		return EventEngine.getAddressDesc(addressavgvalue);
	}
	public int getActivityprogressstate() {
		return activityprogressstate;
	}
	public void setActivityprogressstate(int activityprogressstate) {
		this.activityprogressstate = activityprogressstate;
	}
	public int getCommentcount() {
		return commentcount;
	}
	public void setCommentcount(int commentcount) {
		this.commentcount = commentcount;
	}
	public int getInterestcount() {
		return interestcount;
	}
	public void setInterestcount(int interestcount) {
		this.interestcount = interestcount;
	}
	public int getIsinterest() {
		return isinterest;
	}
	public boolean isInterest() {
		return 0!= isinterest;
	}
	public void setIsinterest(int isinterest) {
		this.isinterest = isinterest;
	}
	public void setIsinterest(boolean isinterest) {
		if(isinterest){
			this.isinterest = 1;
		}else{
			this.isinterest = 0;
		}
	}
}
