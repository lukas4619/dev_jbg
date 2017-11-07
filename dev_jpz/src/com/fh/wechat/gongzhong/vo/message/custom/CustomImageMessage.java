package com.fh.wechat.gongzhong.vo.message.custom;

import com.fh.wechat.gongzhong.GongZhongObject;

/**
 * @describe 客服回复图片消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class CustomImageMessage extends GongZhongObject
{
	  private String toUser;
	  public String msgType = "image";
	  private String mediaId;

	  public String getToUser()
	  {
	    return this.toUser;
	  }

	  public void setToUser(String toUser) {
	    this.toUser = toUser;
	  }

	  public String getMediaId() {
	    return this.mediaId;
	  }

	  public void setMediaId(String mediaId) {
	    this.mediaId = mediaId;
	  }
	}