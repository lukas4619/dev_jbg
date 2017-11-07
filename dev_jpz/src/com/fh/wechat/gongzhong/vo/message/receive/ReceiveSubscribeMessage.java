package com.fh.wechat.gongzhong.vo.message.receive;

import com.fh.wechat.gongzhong.vo.message.Message;


/**
 * @describe 接收微信用户公众号关注实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReceiveSubscribeMessage extends Message
{
	  public static final String EVENT_SUBSCRIBE = "subscribe";
	  public static final String EVENT_UNSUBSCRIBE = "unsubscribe";
	  private String event;
	  private String eventKey;
	  private String ticket;

	  public String getEvent()
	  {
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

	  public String getTicket() {
	    return this.ticket;
	  }

	  public void setTicket(String ticket) {
	    this.ticket = ticket;
	  }
	}
