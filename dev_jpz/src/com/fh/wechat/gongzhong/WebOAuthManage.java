package com.fh.wechat.gongzhong;

import java.net.URLEncoder;

import net.sf.json.JSONObject;

import com.fh.util.JSONUtils;
import com.fh.util.Logger;
import com.fh.wechat.gongzhong.utils.GongZhongUtils;
import com.fh.wechat.gongzhong.vo.user.UserInfo;
import com.fh.wechat.gongzhong.vo.weboauth.OauthAccessToken;

/**
 * @describe 微信授权管理
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class WebOAuthManage extends GongZhongObject
{
  protected static Logger log = Logger.getLogger(WebOAuthManage.class);
  private static final String CONNECT_OAUTH2 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=";
  private static final String SNS = "https://api.weixin.qq.com/sns/";
  private static final String OAUTH2_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=";
  private static final String OAUTH2_REFRESH = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=";
  private static final String USERINFO = "https://api.weixin.qq.com/sns/userinfo?access_token=";

  
  /**
   * 静默申请授权
   * @param redirectUri 授权成功后，返回地址(该地必须再公众号中《网页授权获取基本信息》中设置该返回地址的域名，允许携带参数，并且地址需要进行URLEncoder.encode(par, "utf-8"))
   * @param state 固定参数，允许长度128个字节
   * @return 返回申请授权地址
   */
  public static String getBaseOauth2Url(String redirectUri, String state)
  {
    String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + GongZhongService.appId + "&redirect_uri=" + 
      redirectUri + "&response_type=code&scope=snsapi_base&state=" + 
      state + "#wechat_redirect";

    return url;
  }

  
  /**
   * 需要用户确认申请授权
   * @param redirectUri 授权成功后，返回地址(该地必须再公众号中《网页授权获取基本信息》中设置该返回地址的域名，允许携带参数，并且地址需要进行URLEncoder.encode(par, "utf-8"))
   * @param state 固定参数，允许长度128个字节
   * @return  返回申请授权地址
   */
  public static String getUserinfoOauth2Url(String redirectUri, String state)
  {
    String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + GongZhongService.appId + "&redirect_uri=" + 
      redirectUri + 
      "&response_type=code&scope=snsapi_userinfo&state=" + state + 
      "#wechat_redirect";

    return url;
  }

  /**
   * 通过申请授权成功返回的code,获取当前公众号用户的微信标识
   * @param code  申请授权成功返回的code
   * @return 返回授权实体类
   * @throws Exception
   */
  public static OauthAccessToken getAccessToken(String code)
  {
    String result="";
	try {
		result = GongZhongUtils.sendPost("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + 
		  GongZhongService.appId + "&secret=" + 
		  GongZhongService.appSecret + "&code=" + code + 
		  "&grant_type=authorization_code", "");
	} catch (Exception e) {
		log.info("通过申请授权成功返回的code,获取当前公众号用户的微信标识异常："+e.toString());
		e.printStackTrace();
	}
    return ((OauthAccessToken)JSONUtils.toBean(JSONObject.fromObject(result), OauthAccessToken.class));
  }

  /**
   * 用户刷新access_token 
   * @param refreshToken 用户刷新access_token 
   * @return
   * @throws Exception
   */
  public static OauthAccessToken refreshAccessToken(String refreshToken) throws Exception
  {
    String result = 
      GongZhongUtils.sendPost("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + GongZhongService.appId + 
      "&grant_type=refresh_token&refresh_token=" + 
      refreshToken, "");

    return ((OauthAccessToken)JSONUtils.toBean(JSONObject.fromObject(result), 
      OauthAccessToken.class));
  }

  /**
   * 通过微信令牌和微信用户标识获取用户基本资料
   * @param accessToken 令牌
   * @param openId 微信用户标识
   * @return 微信用户实体类
   * @throws Exception
   */
  public static UserInfo getUserInfo(String accessToken, String openId) throws Exception
  {
    String result = GongZhongUtils.sendPost("https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + 
      "&openid=" + openId, "");

    return ((UserInfo)JSONUtils.toBean(JSONObject.fromObject(result), UserInfo.class));
  }
}
