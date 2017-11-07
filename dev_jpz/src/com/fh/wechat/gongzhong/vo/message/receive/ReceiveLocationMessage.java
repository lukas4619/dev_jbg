package com.fh.wechat.gongzhong.vo.message.receive;

import com.fh.wechat.gongzhong.vo.message.Message;



/**
 * @describe 接收微信用户公众号地理位置实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReceiveLocationMessage extends Message {
	private String location_X;
	private String location_Y;
	private String scale;
	private String label;

	public String getLocation_X() {
		return this.location_X;
	}

	public void setLocation_X(String location_X) {
		this.location_X = location_X;
	}

	public String getLocation_Y() {
		return this.location_Y;
	}

	public void setLocation_Y(String location_Y) {
		this.location_Y = location_Y;
	}

	public String getScale() {
		return this.scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
