package com.fh.wechat.gongzhong.vo.message.receive;

import com.fh.wechat.gongzhong.vo.message.Message;


/**
 * @describe 接收微信用户公众号中发送语音消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReceiveVoiceMessage extends Message {
	private String mediaId;
	private String format;
	private String recognition;

	public String getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRecognition() {
		return this.recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}
}
