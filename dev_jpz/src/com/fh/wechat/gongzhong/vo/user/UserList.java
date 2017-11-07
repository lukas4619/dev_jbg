package com.fh.wechat.gongzhong.vo.user;

import java.util.List;

import com.fh.wechat.gongzhong.GongZhongObject;


/**
 * @describe 微信用户列表
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class UserList extends GongZhongObject
{
	  private int total;
	  private int count;
	  private List<String> openIdList;
	  private String nextOpenid;

	  public int getTotal()
	  {
	    return this.total;
	  }

	  public void setTotal(int total) {
	    this.total = total;
	  }

	  public int getCount() {
	    return this.count;
	  }

	  public void setCount(int count) {
	    this.count = count;
	  }

	  public List<String> getOpenIdList() {
	    return this.openIdList;
	  }

	  public void setOpenIdList(List<String> openIdList) {
	    this.openIdList = openIdList;
	  }

	  public String getNextOpenid() {
	    return this.nextOpenid;
	  }

	  public void setNextOpenid(String nextOpenid) {
	    this.nextOpenid = nextOpenid;
	  }
	}
