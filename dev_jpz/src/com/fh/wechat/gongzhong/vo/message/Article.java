package com.fh.wechat.gongzhong.vo.message;

import com.fh.wechat.gongzhong.GongZhongObject;


/**
 * @describe 推送多图文消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class Article extends GongZhongObject
{
	  private String title;
	  private String description;
	  private String picUrl;
	  private String url;

	  public String getTitle()
	  {
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

	  public String getPicUrl() {
	    return this.picUrl;
	  }

	  public void setPicUrl(String picUrl) {
	    this.picUrl = picUrl;
	  }

	  public String getUrl() {
	    return this.url;
	  }

	  public void setUrl(String url) {
	    this.url = url;
	  }
	}