package com.fh.wechat.gongzhong.vo.message.receive;

import com.fh.wechat.gongzhong.vo.message.Message;


/**
 * @describe 接收微信用户点击菜单操作实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReceiveClickMessage extends Message {
	public static final String EVENT_CLICK = "CLICK";
	public static final String EVENT_VIEW = "VIEW";
	private String event;
	private String eventKey;

	public String getEvent() {
		return this.event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return this.eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
}