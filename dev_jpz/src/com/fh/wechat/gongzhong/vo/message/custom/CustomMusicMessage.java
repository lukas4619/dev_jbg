package com.fh.wechat.gongzhong.vo.message.custom;

import com.fh.wechat.gongzhong.GongZhongObject;

/**
 * @describe 客服回复音乐消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class CustomMusicMessage extends GongZhongObject
{
	  private String toUser;
	  public String msgType = "music";
	  private String title;
	  private String description;
	  private String musicUrl;
	  private String hQMusicUrl;
	  private String thumbMediaId;

	  public String getToUser()
	  {
	    return this.toUser;
	  }

	  public void setToUser(String toUser) {
	    this.toUser = toUser;
	  }

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

	  public String getMusicUrl() {
	    return this.musicUrl;
	  }

	  public void setMusicUrl(String musicUrl) {
	    this.musicUrl = musicUrl;
	  }

	  public String gethQMusicUrl() {
	    return this.hQMusicUrl;
	  }

	  public void sethQMusicUrl(String hQMusicUrl) {
	    this.hQMusicUrl = hQMusicUrl;
	  }

	  public String getThumbMediaId() {
	    return this.thumbMediaId;
	  }

	  public void setThumbMediaId(String thumbMediaId) {
	    this.thumbMediaId = thumbMediaId;
	  }
	}
