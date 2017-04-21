package com.jarry.jayvoice.bean;

import cn.bmob.v3.datatype.BmobFile;

public class Singer extends BaseBean{
	private String name;
	private BmobFile userPic;
	private String gender;
	private String description;
	private String countryType;
	private String detail;
	private String detailUrl;
	private String newsUrl;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BmobFile getUserPic() {
		return userPic;
	}
	public void setUserPic(BmobFile userPic) {
		this.userPic = userPic;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCountryType() {
		return countryType;
	}
	public void setCountryType(String countryType) {
		this.countryType = countryType;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public String getNewsUrl() {
		return newsUrl;
	}
	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}
	
}
