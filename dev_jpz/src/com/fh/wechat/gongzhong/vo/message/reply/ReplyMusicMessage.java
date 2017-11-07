package com.fh.wechat.gongzhong.vo.message.reply;

import com.fh.wechat.gongzhong.vo.message.Message;


/**
 * @describe 回复微信用户音乐消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReplyMusicMessage extends Message
{
	  public static final String TYPE_MUSIC = "music";
	  private String mediaId;
	  private String title;
	  private String description;
	  private String musicUrl;
	  private String hQMusicUrl;
	  private String thumbMediaId;

	  public String getMediaId()
	  {
	    return this.mediaId;
	  }

	  public void setMediaId(String mediaId) {
	    this.mediaId = mediaId;
	  }

	  public void setMsgType(String msgType)
	  {
	    if (!("music".equals(msgType)))
	      throw new RuntimeException("msgType必须为：music");

	    super.setMsgType(msgType);
	  }

	  public String getMsgType() {
	    return "music";
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

	  public String getHQMusicUrl() {
	    return this.hQMusicUrl;
	  }

	  public void setHQMusicUrl(String hQMusicUrl) {
	    this.hQMusicUrl = hQMusicUrl;
	  }

	  public String getThumbMediaId() {
	    return this.thumbMediaId;
	  }

	  public void setThumbMediaId(String thumbMediaId) {
	    this.thumbMediaId = thumbMediaId;
	  }
	}
