package com.fh.wechat.gongzhong;


import java.util.List;

import net.sf.json.JSONObject;

import com.fh.util.JSONUtils;
import com.fh.wechat.gongzhong.utils.GongZhongUtils;
import com.fh.wechat.gongzhong.vo.menu.Menu;


/**
 * @describe 公众号菜单管理
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class MenuManage extends GongZhongObject
{
  private static final String MENU_GET = "https://api.weixin.qq.com/cgi-bin/menu/get?";
  private static final String MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?";
  private static final String MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?";

  /**
   * 获取当前公众号发布的菜单
   * @return
   * @throws Exception
   */
  public static List<Menu> getMenu() throws Exception
  {
    String reslut = GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/menu/get?", "access_token=" + 
      GongZhongService.getAccessToken());
    JSONObject obj = JSONObject.fromObject(reslut).getJSONObject("menu");

    return JSONUtils.toList(obj.getJSONArray("button"), Menu.class);
  }

  
  /**
   * 删除所有菜单
   * @throws Exception
   */
  public static void deleteMenu() throws Exception
  {
    GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/menu/delete?", 
      "access_token=" + GongZhongService.getAccessToken());
  }

  /**
   * 创建菜单
   * @param menus
   * @throws Exception
   */
  public static void createMenu(List<Menu> menus) throws Exception
  {
    if ((menus == null) || (menus.size() <= 0))
    {
      throw new RuntimeException("菜单不能位空");
    }

    if (menus.size() > 3)
    {
      throw new RuntimeException("一级菜单数组，个数应为1~3个");
    }

    JSONObject obj = new JSONObject();
    obj.put("button", menus);
    GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + 
      GongZhongService.getAccessToken(), obj.toString());
  }
}
