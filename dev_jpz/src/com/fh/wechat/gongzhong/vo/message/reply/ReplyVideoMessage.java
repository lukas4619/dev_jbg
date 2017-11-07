package com.fh.wechat.gongzhong.vo.message.reply;

import com.fh.wechat.gongzhong.vo.message.Message;


/**
 * @describe 回复微信用户视频消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReplyVideoMessage extends Message {
	private String mediaId;
	private String title;
	private String description;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public void setMsgType(String msgType) {
		if (!("video".equals(msgType)))
			throw new RuntimeException("msgType必须为：video");

		super.setMsgType(msgType);
	}

	public String getMsgType() {
		return "video";
	}
}
