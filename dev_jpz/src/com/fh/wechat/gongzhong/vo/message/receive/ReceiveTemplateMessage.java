package com.fh.wechat.gongzhong.vo.message.receive;

import com.fh.wechat.gongzhong.vo.message.Message;


/**
 * @describe 接收微信用户公众号模版消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReceiveTemplateMessage extends Message
{
	  private String event;
	  private String status;

	  public String getEvent()
	  {
	    return this.event; }

	  public void setEvent(String event) {
	    this.event = event; }

	  public String getStatus() {
	    return this.status; }

	  public void setStatus(String status) {
	    this.status = status;
	  }
	}