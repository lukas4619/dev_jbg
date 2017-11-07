package com.fh.wechat.gongzhong;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.fh.util.JSONUtils;
import com.fh.wechat.gongzhong.utils.GongZhongUtils;
import com.fh.wechat.gongzhong.vo.customservice.Account;
import com.fh.wechat.gongzhong.vo.customservice.Record;
import com.fh.wechat.gongzhong.vo.message.Message;

/**
 * @describe 转客服服务
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class CustomServiceManage extends GongZhongObject
{
	  private static final String GET_KF_LIST = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=";
	  private static final String GET_ONLINE_KF_LIST = "https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist?access_token=";
	  private static final String GET_RECORD_LIST = "https://api.weixin.qq.com/cgi-bin/customservice/getrecord?access_token=";

	  public static void transferCustomerService(HttpServletResponse response, Message message)
	    throws IOException
	  {
	    response.getWriter().print("<xml><ToUserName><![CDATA[" + message.getFromUserName() + "]]></ToUserName>" + 
	      "<FromUserName><![CDATA[" + message.getToUserName() + "]]></FromUserName>" + 
	      "<CreateTime>" + message.getCreateTime() + 1 + "</CreateTime><MsgType>" + 
	      "<![CDATA[transfer_customer_service]]></MsgType></xml>");
	  }

	  public static void transferCustomerService(HttpServletResponse response, Message message, String kfAccount)
	    throws IOException
	  {
	    response.getWriter().print("<xml><ToUserName><![CDATA[" + message.getFromUserName() + "]]></ToUserName>" + 
	      "<FromUserName><![CDATA[" + message.getToUserName() + "]]></FromUserName>" + 
	      "<CreateTime>" + message.getCreateTime() + 1 + "</CreateTime><MsgType>" + 
	      "<![CDATA[transfer_customer_service]]></MsgType>" + 
	      "<TransInfo><KfAccount>" + kfAccount + "</KfAccount></TransInfo></xml>");
	  }

	  public static List<Account> getKFList() throws Exception
	  {
	    String reslut = GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=" + GongZhongService.getAccessToken(), "");

	    JSONObject reslutObj = JSONObject.fromObject(reslut);

	    return JSONUtils.toList(reslutObj.get("kf_list"), Account.class);
	  }

	  public static List<Account> getOnlineKFList() throws Exception
	  {
	    String reslut = GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist?access_token=" + GongZhongService.getAccessToken(), "");

	    JSONObject reslutObj = JSONObject.fromObject(reslut);

	    return JSONUtils.toList(reslutObj.get("kf_online_list"), Account.class);
	  }

	  public static List<Record> getRecordList(Date starttime, Date endtime, String openid, int pagesize, int pageindex) throws Exception
	  {
	    String str = "{\"starttime\" : " + (starttime.getTime() / 1000L) + ",\"endtime\" : " + (endtime.getTime() / 1000L) + ",\"openid\" : \"" + openid + "\",\"pagesize\" : " + pagesize + ",\"pageindex\" : " + pageindex + "}";
	    String reslut = GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/customservice/getrecord?access_token=" + GongZhongService.getAccessToken(), str);
	    JSONObject reslutObj = JSONObject.fromObject(reslut);

	    return JSONUtils.toList(reslutObj.get("recordlist"), Record.class);
	  }
	}
