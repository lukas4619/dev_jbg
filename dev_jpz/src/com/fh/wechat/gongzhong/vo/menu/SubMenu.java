package com.fh.wechat.gongzhong.vo.menu;

import org.apache.commons.lang.StringUtils;

import com.fh.wechat.gongzhong.GongZhongObject;

/**
 * @describe 微信菜单实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class SubMenu extends GongZhongObject {
	 protected String name;
	  private String type = "click";
	  private String key;
	  private String url;
	  public static final String CLICK = "click";
	  public static final String VIEW = "view";

	  public String getName()
	  {
	    return this.name;
	  }

	  public void setName(String name)
	  {
	    if (StringUtils.isBlank(name))
	    {
	      throw new RuntimeException("name不能位空");
	    }

	    if (name.getBytes().length > 40) {
	      throw new RuntimeException("子菜单不超过40个字节");
	    }

	    this.name = name;
	  }

	  public String getType() {
	    return this.type;
	  }

	  public void setType(String type)
	  {
	    if (StringUtils.isBlank(type))
	    {
	      throw new RuntimeException("type不能位空");
	    }

	    if ((!("click".equals(type))) && (!("view".equals(type))))
	    {
	      throw new RuntimeException("type类型只能是 click 或 view");
	    }

	    this.type = type;
	  }

	  public String getKey() {
	    return this.key;
	  }

	  public void setKey(String key)
	  {
	    if (StringUtils.isBlank(key))
	    {
	      throw new RuntimeException("key不能位空");
	    }

	    this.key = key;
	  }

	  public String getUrl() {
	    return this.url;
	  }

	  public void setUrl(String url)
	  {
	    if (StringUtils.isBlank(url))
	    {
	      throw new RuntimeException("key不能位空");
	    }

	    this.url = url;
	  }
}
