package com.fh.wechat.gongzhong;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.fh.util.JSONUtils;
import com.fh.util._String;
import com.fh.wechat.gongzhong.utils.GongZhongUtils;
import com.fh.wechat.gongzhong.vo.user.UserGroup;

/**
 * @describe 微信用户群组管理
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class UserGroupManage extends GongZhongObject
{
  private static final String GROUPS_GET = "https://api.weixin.qq.com/cgi-bin/groups/get?";
  private static final String GROUPS_CREATE = "https://api.weixin.qq.com/cgi-bin/groups/create?";
  private static final String GROUPS_UPDATE = "https://api.weixin.qq.com/cgi-bin/groups/update?";
  private static final String GROUPS_GETID = "https://api.weixin.qq.com/cgi-bin/groups/getid?";

  /**
   * 获取所有微信用户群组信息
   * @return
   * @throws Exception
   */
  public static List<UserGroup> getAllUserGroup() throws Exception
  {
    String reslut = GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/groups/get?", "access_token=" + 
      GongZhongService.getAccessToken());
    JSONArray obj = JSONObject.fromObject(reslut).getJSONArray("groups");

    return JSONUtils.toList(obj, UserGroup.class);
  }

  /**
   * 创建群组
   * @param name
   * @return
   * @throws Exception
   */
  public static UserGroup createUserGroup(String name) throws Exception
  {
    if (StringUtils.isBlank(name)) {
      throw new RuntimeException("分租名称不能为空");
    }

    if (_String.getLength(name) > 30) {
      throw new RuntimeException("分组名称不能大于30个字符");
    }

    String reslut = GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/groups/create?access_token=" + 
      GongZhongService.getAccessToken(), "{\"group\":{\"name\":\"" + 
      name + "\"}}");

    return ((UserGroup)JSONUtils.toBean(JSONObject.fromObject(reslut).get("group"), 
      UserGroup.class));
  }

  /**
   * 通过群组标识更新群组
   * @param groupId群组标识
   * @param name
   * @throws Exception
   */
  public static void updateUserGroup(int groupId, String name) throws Exception
  {
    if (StringUtils.isBlank(name)) {
      throw new RuntimeException("分租名称不能为空");
    }

    if (_String.getLength(name) > 30) {
      throw new RuntimeException("分组名称不能大于30个字符");
    }

    GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/groups/update?access_token=" + 
      GongZhongService.getAccessToken(), "{\"group\":{\"id\":" + 
      groupId + ",\"name\":\"" + name + "\"}}");
  }

  /**
   * 通过用户微信标识获取群组
   * @param openId 微信用户标识
   * @return
   * @throws Exception
   */
  public static String getGruopIdByOpenId(String openId) throws Exception
  {
    String reslut = GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=" + 
      GongZhongService.getAccessToken(), "{\"openid\":\"" + openId + 
      "\"}");

    return JSONUtils.getString(JSONObject.fromObject(reslut), "groupid").toString
      ();
  }
}