package com.whoyao.model;

import android.text.TextUtils;

import com.whoyao.utils.CalendarUtils;

/**
 * @author hyh creat_at：2013-7-22-下午1:39:33
 */
public class UserInfoModel {
	protected int UserID;
	protected String UserName;
	protected String UserPic;
	protected String UserPassWord;	
	protected String UserFace;
	protected int UserBackImg;
	protected String UserPhone;
	protected String UserEmail;
	protected String UserRelName;
	protected int UserState;
	protected int UserEmailState;
	protected int UserMobileState;
	protected int UserHonestyState;
	protected int UserSex;
	protected int UserHeight;
	protected int UserWaist;
	protected String UserBirthday;
	protected int UserBloodGroup;
	protected String SessionId;
	protected String QQ;
	protected String MSN;
	protected String UserMicroMessage;
	protected String UserMicroblog;
	protected int ActivityCount;
	protected int TopicCount;
	protected String UserContent;
	protected String UserPendingTime;
	protected String UserRemark;
	protected int UserProvince;
	protected int UserCity;
	/**区县*/
	protected int UserDistrict;

	
	
	
	public String getSessionId() {
		return SessionId;
	}

	public void setSessionId(String sessionId) {
		SessionId = sessionId;
	}

	public String getUserPassWord() {
		return UserPassWord;
	}

	public void setUserPassWord(String userPassWord) {
		UserPassWord = userPassWord;
	}
    
	public String getUserPic() {
		return UserPic;
	}
	
	public void setUserPic(String userPic) {
		UserPic = userPic;
	}
	
	
	public String getUserContent() {
		return UserContent;
	}

	public void setUserContent(String userContent) {
		UserContent = userContent;
	}

	public int getUserProvince() {
		return UserProvince;
	}

	public void setUserProvince(int userProvince) {
		UserProvince = userProvince;
	}

	public int getUserCity() {
		return UserCity;
	}

	public void setUserCity(int userCity) {
		UserCity = userCity;
	}

	public int getUserDistrict() {
		return UserDistrict;
	}

	public void setUserDistrict(int userDistrict) {
		UserDistrict = userDistrict;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserFace() {
		return UserFace;
	}

	public void setUserFace(String userFace) {
		UserFace = userFace;
	}

	public int getUserBackImg() {
		return UserBackImg;
	}

	public void setUserBackImg(int userBackImg) {
		UserBackImg = userBackImg;
	}

	public String getUserPhone() {
		return UserPhone;
	}

	public void setUserPhone(String userPhone) {
		UserPhone = userPhone;
	}

	public String getUserEmail() {
		return UserEmail;
	}

	public void setUserEmail(String userEmail) {
		UserEmail = userEmail;
	}

	public int getUserState() {
		return UserState;
	}

	public void setUserState(int userState) {
		UserState = userState;
	}

	public String getUserRelName() {
		return UserRelName;
	}

	public void setUserRelName(String userRelName) {
		UserRelName = userRelName;
	}

	public int getUserEmailState() {
		return UserEmailState;
	}

	public void setUserEmailState(int userEmailState) {
		UserEmailState = userEmailState;
	}

	public int getUserMobileState() {
		return UserMobileState;
	}

	public void setUserMobileState(int userMobileState) {
		UserMobileState = userMobileState;
	}

	public boolean isEmailV() {
		return UserEmailState == 2;
	}
	public boolean isMobileV() {
		return UserMobileState == 2;
	}

	public int getUserHonestyState() {
		return UserHonestyState;
	}

	public void setUserHonestyState(int userHonestyState) {
		UserHonestyState = userHonestyState;
	}

	public boolean isHonestyV() {
		return UserHonestyState == 1;
	}

	public int getUserSex() {
		return UserSex;
	}

	public void setUserSex(int userSex) {
		UserSex = userSex;
	}

	public String getSexStr() {
		String sexStr = null;
		switch (UserSex) {
		case 1:
			sexStr = "男";
			break;
		case 2:
			sexStr = "女";
			break;
		default:
			sexStr = "";
			break;
		}
		return sexStr;
	}

	public int getUserHeight() {
		return UserHeight;
	}

	public void setUserHeight(int userHeight) {
		UserHeight = userHeight;
	}

	public int getUserWaist() {
		return UserWaist;
	}

	public void setUserWaist(int userWaist) {
		UserWaist = userWaist;
	}

	public String getUserBirthday() {
		return UserBirthday;
	}
	public String getUserAge() {
		if(TextUtils.isEmpty(UserBirthday)){
			return "";
		}
		return CalendarUtils.getAge(UserBirthday) + "岁";
	}
	public String getUserConstellation() {
		if(TextUtils.isEmpty(UserBirthday)){
			return "";
		}
		return CalendarUtils.getConstellation(UserBirthday);
	}

	public void setUserBirthday(String userBirthday) {
		UserBirthday = userBirthday;
	}

	public int getUserBloodGroup() {
		return UserBloodGroup;
	}

	public void setUserBloodGroup(int userBloodGroup) {
		UserBloodGroup = userBloodGroup;
	}

	public String getBloodStr() {
		String bloodStr = null;
		switch (UserBloodGroup) {
		case 1:
			bloodStr = "A型";
			break;
		case 2:
			bloodStr = "B型";
			break;
		case 3:
			bloodStr = "O型";
			break;
		case 4:
			bloodStr = "AB型";
			break;
		case 5:
			bloodStr = "其他血型";
			break;
		default:
			bloodStr = "";
			break;
		}
		return bloodStr;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qq) {
		QQ = qq;
	}

	public String getMSN() {
		return MSN;
	}

	public void setMSN(String mSN) {
		MSN = mSN;
	}

	public String getUserMicroMessage() {
		return UserMicroMessage;
	}

	public void setUserMicroMessage(String userMicroMessage) {
		UserMicroMessage = userMicroMessage;
	}

	public String getUserMicroblog() {
		return UserMicroblog;
	}

	public void setUserMicroblog(String userMicroblog) {
		UserMicroblog = userMicroblog;
	}

	public int getActivityCount() {
		return ActivityCount;
	}

	public void setActivityCount(int activityCount) {
		ActivityCount = activityCount;
	}

	public int getTopicCount() {
		return TopicCount;
	}

	public void setTopicCount(int topicCount) {
		TopicCount = topicCount;
	}
	public String getUserPendingTime() {
		return UserPendingTime;
	}

	public void setUserPendingTime(String userPendingTime) {
		UserPendingTime = userPendingTime;
	}

	public String getUserRemark() {
		return UserRemark;
	}

	public void setUserRemark(String userRemark) {
		UserRemark = userRemark;
	}
}
