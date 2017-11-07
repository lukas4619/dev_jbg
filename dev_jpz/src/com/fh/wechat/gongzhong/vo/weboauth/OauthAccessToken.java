package com.fh.wechat.gongzhong.vo.weboauth;

import com.fh.wechat.gongzhong.GongZhongObject;


/**
 * @describe 微信授权实体类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class OauthAccessToken extends GongZhongObject
{
	  private String access_token;
	  private int expires_in;
	  private String refresh_token;
	  private String openid;
	  private String scope;

	  public String getAccess_token()
	  {
	    return this.access_token;
	  }

	  public void setAccess_token(String access_token) {
	    this.access_token = access_token;
	  }

	  public int getExpires_in() {
	    return this.expires_in;
	  }

	  public void setExpires_in(int expires_in) {
	    this.expires_in = expires_in;
	  }

	  public String getRefresh_token() {
	    return this.refresh_token;
	  }

	  public void setRefresh_token(String refresh_token) {
	    this.refresh_token = refresh_token;
	  }

	  public String getOpenid() {
	    return this.openid;
	  }

	  public void setOpenid(String openid) {
	    this.openid = openid;
	  }

	  public String getScope() {
	    return this.scope;
	  }

	  public void setScope(String scope) {
	    this.scope = scope;
	  }
	}
