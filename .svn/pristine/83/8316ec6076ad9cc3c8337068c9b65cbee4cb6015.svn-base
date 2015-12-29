package com.whoyao.venue.model;

import java.util.Calendar;

import com.whoyao.AppContext;
import com.whoyao.utils.CalendarUtils;

/**
 * @author hyh 
 * creat_at：2014-2-24-上午11:06:54
 */
public class VenueReserveRModel {
	private int placetype;
	private int serviceype;
	private String datetime;
	private int venueId;
	public VenueReserveRModel(int venueID) {
		venueId = venueID;
	}
	public int getPlacetype() {
		return placetype;
	}
	public void setPlacetype(int placetype) {
		this.placetype = placetype;
		serviceype = placetype;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public void setDatetime(int datetime) {
		Calendar calendar = CalendarUtils.getNewCalendar();
//		calendar.setTimeInMillis(AppContext.serviceTimeMillis());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + datetime);
		this.datetime = CalendarUtils.formatYMD(calendar.getTime());
	}
	public int getVenueId() {
		return venueId;
	}
	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}

}
