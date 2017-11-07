package com.fh.wechat.gongzhong.vo.message.receive;

import com.fh.wechat.gongzhong.vo.message.Message;


/**
 * @describe 接收微信用户公众号中发送文本消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReceiveTextMessage extends Message
{
	  private String Content;

	  public String getContent()
	  {
	    return this.Content;
	  }

	  public void setContent(String content) {
	    this.Content = content;
	  }
	}
