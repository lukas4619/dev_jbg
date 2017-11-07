package com.fh.wechat.gongzhong.vo.message.reply;

import com.fh.wechat.gongzhong.vo.message.Message;


/**
 * @describe 回复微信用户文本消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReplyTextMessage extends Message {
	private String Content;

	public String getContent() {
		return this.Content;
	}

	public void setContent(String content) {
		this.Content = content;
	}

	public void setMsgType(String msgType) {
		if (!("text".equals(msgType)))
			throw new RuntimeException("msgType必须为：text");

		super.setMsgType(msgType);
	}

	public String getMsgType() {
		return "text";
	}
}