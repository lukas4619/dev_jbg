package com.fh.wechat.gongzhong.vo.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
/**
 * @describe 微信子菜单实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class Menu extends SubMenu
{
	  private List<SubMenu> sub_button = new ArrayList();

	  public void setName(String name)
	  {
	    if (StringUtils.isBlank(name))
	    {
	      throw new RuntimeException("name不能位空");
	    }

	    if (name.getBytes().length > 16) {
	      throw new RuntimeException("菜单标题，不超过16个字节");
	    }

	    this.name = name;
	  }

	  public void addSubMenu(SubMenu subMenu)
	  {
	    if (this.sub_button.size() > 5) {
	      throw new RuntimeException("二级菜单数组，个数应为1~5个");
	    }

	    this.sub_button.add(subMenu);
	  }

	  public List<SubMenu> getSub_button() {
	    return this.sub_button;
	  }

	  public void setSub_button(List<SubMenu> sub_button) {
	    this.sub_button = sub_button;
	  }
	}