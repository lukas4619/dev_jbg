package com.fh.wechat.gongzhong.vo.message.template;

import com.fh.wechat.gongzhong.GongZhongObject;


/**
 * @describe 推送模版消息实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class Template extends GongZhongObject
{
	  private String key;
	  private String value;
	  private String color;

	  public String getKey()
	  {
	    return this.key; }

	  public void setKey(String key) {
	    this.key = key; }

	  public String getValue() {
	    return this.value; }

	  public void setValue(String value) {
	    this.value = value; }

	  public String getColor() {
	    return this.color; }

	  public void setColor(String color) {
	    this.color = color;
	  }

	@Override
	public String toString() {
		return "Template [key=" + key + ", value=" + value + ", color=" + color
				+ "]";
	}
	  
	}
