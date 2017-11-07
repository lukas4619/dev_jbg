package com.fh.wechat.gongzhong.vo.customservice;

/**
 * @describe 客服聊天记录实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class Record {
	private String worker;
	private String openid;
	private String opercode;
	private long time;
	private String text;

	public String getWorker() {
		return this.worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getOpenid() {
		return this.openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpercode() {
		return this.opercode;
	}

	public void setOpercode(String opercode) {
		this.opercode = opercode;
	}

	public long getTime() {
		return this.time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
