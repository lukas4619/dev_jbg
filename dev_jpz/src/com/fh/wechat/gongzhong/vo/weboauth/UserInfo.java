package com.fh.wechat.gongzhong.vo.weboauth;

public class UserInfo extends com.fh.wechat.gongzhong.vo.user.UserInfo{
	private String[] privilege;

	public String[] getPrivilege() {
		return this.privilege;
	}

	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
	}
}
