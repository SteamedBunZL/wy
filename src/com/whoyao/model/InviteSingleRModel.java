package com.whoyao.model;


public class InviteSingleRModel {
	private int InviteId;
	private String Title;
	private String Time;
	private String Address;
	private float Longitude;
	private float Latitude;
	private String Description;
	private int InviteState;
	private int PredictConsume;
	private int PayType;
	private int ToMessage;
	private String CreateTime;
	private int UserID;
	private int SendUserID;
	private String DisposeTime;
	public int getInviteId() {
		return InviteId;
	}
	public void setInviteId(int inviteId) {
		InviteId = inviteId;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public float getLongitude() {
		return Longitude;
	}
	public void setLongitude(float longitude) {
		Longitude = longitude;
	}
	public float getLatitude() {
		return Latitude;
	}
	public void setLatitude(float latitude) {
		Latitude = latitude;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getInviteState() {
		return InviteState;
	}
	public void setInviteState(int inviteState) {
		InviteState = inviteState;
	}
	public int getPredictConsume() {
		return PredictConsume;
	}
	public void setPredictConsume(int predictConsume) {
		PredictConsume = predictConsume;
	}
	public int getPayType() {
		return PayType;
	}
	public void setPayType(int payType) {
		PayType = payType;
	}
	public int getToMessage() {
		return ToMessage;
	}
	public void setToMessage(int toMessage) {
		ToMessage = toMessage;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public int getSendUserID() {
		return SendUserID;
	}
	public void setSendUserID(int sendUserID) {
		SendUserID = sendUserID;
	}
	public String getDisposeTime() {
		return DisposeTime;
	}
	public void setDisposeTime(String disposeTime) {
		DisposeTime = disposeTime;
	}
	@Override
	public String toString() {
		return "InviteSingleRModel [InviteId=" + InviteId + ", Title=" + Title
				+ ", Time=" + Time + ", Address=" + Address + ", Longitude="
				+ Longitude + ", Latitude=" + Latitude + ", Description="
				+ Description + ", InviteState=" + InviteState
				+ ", PredictConsume=" + PredictConsume + ", PayType=" + PayType
				+ ", ToMessage=" + ToMessage + ", CreateTime=" + CreateTime
				+ ", UserID=" + UserID + ", SendUserID=" + SendUserID
				+ ", DisposeTime=" + DisposeTime + "]";
	}
	

}
