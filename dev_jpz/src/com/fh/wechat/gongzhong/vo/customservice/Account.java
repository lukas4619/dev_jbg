package com.fh.wechat.gongzhong.vo.customservice;

import com.fh.wechat.gongzhong.GongZhongObject;

/**
 * @describe 客服帐号实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class Account extends GongZhongObject {
	private String kf_account;
	private String kf_nick;
	private String kf_id;
	private int status;
	private int auto_accept;
	private int accepted_case;

	public String getKf_account() {
		return this.kf_account;
	}

	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}

	public String getKf_nick() {
		return this.kf_nick;
	}

	public void setKf_nick(String kf_nick) {
		this.kf_nick = kf_nick;
	}

	public String getKf_id() {
		return this.kf_id;
	}

	public void setKf_id(String kf_id) {
		this.kf_id = kf_id;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAuto_accept() {
		return this.auto_accept;
	}

	public void setAuto_accept(int auto_accept) {
		this.auto_accept = auto_accept;
	}

	public int getAccepted_case() {
		return this.accepted_case;
	}

	public void setAccepted_case(int accepted_case) {
		this.accepted_case = accepted_case;
	}
}
