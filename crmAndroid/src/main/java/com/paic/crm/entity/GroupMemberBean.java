package com.paic.crm.entity;

import java.io.Serializable;

public class GroupMemberBean implements Serializable {

	private String name;
	private String sortLetters;
	private String clientLogo;
	private boolean isShowChannelsBar=false;
	private String clientId;
	private String nickName;
	private String sex;
	private String carNo;
	private String certificateNo;
	private String mobileNo;
	private String clientImType;
	private String customerNo;
	private String country;
	private String province;
	private String city;
	private String age;
	private String email;
	private String time;
	private FlagEntity flag;
	public class FlagEntity{
		public String weixin;
		public String notMessage;
	}
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientImType() {
		return clientImType;
	}

	public void setClientImType(String clientImType) {
		this.clientImType = clientImType;
	}

	public String getClientLogo() {
		return clientLogo;
	}

	public void setClientLogo(String clientLogo) {
		this.clientLogo = clientLogo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public boolean isShowChannelsBar() {
		return isShowChannelsBar;
	}

	public void setIsShowChannelsBar(boolean isShowChannelsBar) {
		this.isShowChannelsBar = isShowChannelsBar;
	}
	public String getName() {
		return name;
	}

	public FlagEntity getFlag() {
		return flag;
	}

	public void setFlag(FlagEntity flag) {
		this.flag = flag;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
