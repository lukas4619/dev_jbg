package com.fh.entity;


/**
 * 
* 类名称：用户
* 类描述： 
* @author lukas 414024003@qq.com
* 作者单位： 
* 联系方式：
* 创建时间：2016年8月30日 11:14:23
* @version 1.0
 */
public class Member {
	int ID;
	String  memberId;
	int memberType;
    String openId;
    String weChatName;
    int sex;
    String city;
    String country;
    String province;
    String language;
    String headImgUrl;
    String passWord;
    
    
    public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getMemberType() {
		return memberType;
	}
	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getWeChatName() {
		return weChatName;
	}
	public void setWeChatName(String weChatName) {
		this.weChatName = weChatName;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	@Override
	public String toString() {
		return "Member [ID=" + ID + ", memberId=" + memberId + ", memberType="
				+ memberType + ", openId=" + openId + ", weChatName="
				+ weChatName + ", sex=" + sex + ", city=" + city + ", country="
				+ country + ", province=" + province + ", language=" + language
				+ ", headImgUrl=" + headImgUrl + ", passWord=" + passWord + "]";
	}
	
}
