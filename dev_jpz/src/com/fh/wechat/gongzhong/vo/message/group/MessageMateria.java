package com.fh.wechat.gongzhong.vo.message.group;

import com.fh.wechat.gongzhong.GongZhongObject;

/**
 * @describe 按微信用户群组推送消息
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class MessageMateria extends GongZhongObject
{
	  private String thumb_media_id;
	  public String thumbMediaFilePath = "";
	  private String author;
	  private String title;
	  private String content_source_url;
	  private String content;
	  private String digest;

	  public String getThumb_media_id()
	  {
	    return this.thumb_media_id;
	  }

	  public void setThumb_media_id(String thumb_media_id) {
	    this.thumb_media_id = thumb_media_id;
	  }

	  public String getAuthor() {
	    return this.author;
	  }

	  public void setAuthor(String author) {
	    this.author = author;
	  }

	  public String getTitle() {
	    return this.title;
	  }

	  public void setTitle(String title) {
	    this.title = title;
	  }

	  public String getContent_source_url() {
	    return this.content_source_url;
	  }

	  public void setContent_source_url(String content_source_url) {
	    this.content_source_url = content_source_url;
	  }

	  public String getContent() {
	    return this.content;
	  }

	  public void setContent(String content) {
	    this.content = content;
	  }

	  public String getDigest() {
	    return this.digest;
	  }

	  public void setDigest(String digest) {
	    this.digest = digest;
	  }
	}
