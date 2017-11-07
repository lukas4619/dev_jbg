package com.fh.wechat.gongzhong.vo.message.reply;

import com.fh.wechat.gongzhong.vo.message.Message;


/**
 * @describe 回复微信用户图片消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReplyImageMessage extends Message
{
	  private String mediaId;

	  public String getMediaId()
	  {
	    return this.mediaId;
	  }

	  public void setMediaId(String mediaId) {
	    this.mediaId = mediaId;
	  }

	  public void setMsgType(String msgType)
	  {
	    if (!("image".equals(msgType)))
	      throw new RuntimeException("msgType必须为：image");

	    super.setMsgType(msgType);
	  }

	  public String getMsgType() {
	    return "image";
	  }
	}
