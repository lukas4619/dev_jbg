package com.fh.wechat.gongzhong;


import com.fh.util.JSONUtils;
import com.fh.util.Logger;
import com.fh.wechat.gongzhong.utils.GongZhongUtils;
import com.fh.wechat.gongzhong.vo.user.UserInfo;
import com.fh.wechat.gongzhong.vo.user.UserList;
import net.sf.json.JSONObject;

/**
 * @describe 微信用户管理
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class UserManage extends GongZhongObject
{
	protected static Logger logger = Logger.getLogger(UserManage.class);
  private static final String USER_GET = "https://api.weixin.qq.com/cgi-bin/user/get?";
  private static final String USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?";
  private static final String USER_INFO_UPDATEREMARK = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?";
  private static final String GROUPS_MEMBERS_UPDATE = "https://api.weixin.qq.com/cgi-bin/groups/members/update?";

  /**
   * 通过微信用户开始标识获取微信用户列表
   * @param startOpenId 微信用户开始标识
   * @return
   * @throws Exception
   */
  public static UserList getUser(String startOpenId) throws Exception
  {
    String reslut = GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/user/get?", "access_token=" + 
      GongZhongService.getAccessToken() + "&next_openid=" + 
      startOpenId);

    JSONObject reslutObj = JSONObject.fromObject(reslut);
    JSONObject data = (JSONObject)reslutObj.get("data");

    UserList userList = new UserList();
    userList.setTotal(((Integer)reslutObj.get("total")).intValue());
    userList.setCount(((Integer)reslutObj.get("count")).intValue());
    userList.setNextOpenid(reslutObj.get("next_openid").toString());
    userList.setOpenIdList(JSONUtils.toList(data.get("openid"), 
      String.class));

    return userList;
  }
   
  
  /**
   * 通过微信用户标识获取用户基本信息
   * @param openId
   * @return
   * @throws Exception
   */
  public static UserInfo getUserInfo(String openId) throws Exception
  {
	  UserInfo u = new UserInfo();
    String reslut = GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/user/info?", "access_token=" + GongZhongService.getAccessToken() + "&openid=" + openId);
    System.out.println("UserInfo:"+reslut);
    JSONObject obj = JSONObject.fromObject(reslut);
    if(obj==null){
    	return u;
    }
    u.setSubscribe(obj.get("subscribe")+"");
    u.setOpenid(obj.get("openid")+"");
    u.setNickname(obj.get("nickname")+"");
    u.setSex(obj.get("sex")+"");
    u.setLanguage(obj.get("language")+"");
    u.setCity(obj.get("city")+"");
    u.setProvince(obj.get("province")+"");
    u.setCountry(obj.get("country")+"");
    u.setHeadimgurl(obj.get("headimgurl")+"");
    u.setSubscribe_time(obj.get("subscribe_time")+"");
    u.setRemark(obj.get("remark")+"");
    u.setGroupid(obj.get("groupid")+"");
//    u.setTagid_list(obj.get("tagid_list"));
    // ((UserInfo)JSONUtils.toBean(obj, UserInfo.class));
    return u;
  }
  
  

  
  /**
   * 通过微信用户标识以及群组标识修改微信用户群组
   * @param openid 微信用户标识
   * @param groupId 修改为群组标识
   * @throws Exception
   */
  public static void moveUserGroup(String openid, int groupId) throws Exception
  {
    GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=" + 
      GongZhongService.getAccessToken(), "{\"openid\":\"" + openid + 
      "\",\"to_groupid\":" + groupId + "}");
  }

  /**
   * 通过微信用户标识修改用户备注
   * @param openid 微信用户标识
   * @param remark 备注
   * @throws Exception
   */
  public void updateRemark(String openid, String remark) throws Exception
  {
    GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=" + GongZhongService.getAccessToken(), 
      "{\"openid\":\"" + openid + "\",\"remark\":\"" + remark + "\"}");
  }
}
