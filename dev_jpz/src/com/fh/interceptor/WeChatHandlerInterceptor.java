package com.fh.interceptor;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fh.entity.Member;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.memberlog.MemberLogManager;
import com.fh.util.Const;
import com.fh.util.Convert;
import com.fh.util.Jurisdiction;
import com.fh.util.Logger;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.StringUtil;
import com.fh.util.Tools;
import com.fh.util.UuidUtil;
import com.fh.wechat.gongzhong.UserManage;
import com.fh.wechat.gongzhong.WebOAuthManage;
import com.fh.wechat.gongzhong.vo.user.UserInfo;

/**
 * 
* 类名称：微信授权拦截器
* 类描述： 
* @author lukas 414024003@qq.com
* 作者单位： 
* 联系方式：
* 创建时间：2015年11月2日
* @version 1.6
 */
public class WeChatHandlerInterceptor extends HandlerInterceptorAdapter{
	
	@Resource(name = "memberService")
	private MemberManager memberService;
	protected Logger logger = Logger.getLogger(this.getClass());
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String basePath = getRequertUrl(request);
		logger.info("请求访问的URL====>-----------basePath:"+basePath);
		System.err.println("请求访问的URL====>-----------basePath:"+basePath);
		String path = request.getServletPath().substring(1,request.getServletPath().length());
		String STOREID ="-1";
		if(path.equals("frontStore/index.html") && request.getQueryString()!=null){
			STOREID=request.getQueryString();
		}
		if(request.getQueryString()!=null){
			path+="?" + request.getQueryString();
		}
		Session session = Jurisdiction.getSession();
		logger.info("请求访问的URL====>-----------path:"+path);
		String posturl =basePath+path;
		// 获取浏览器对象
		String ua = request.getHeader("user-agent").toLowerCase();
		Member member = new Member();
		// 判断是否是微信浏览器
		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
			// 如果是微信浏览器session未空或者取消授权，重定向到授权页面
			if(session.getAttribute(Const.SESSION_WECHATUSER) == null){
				logger.info("需要进行授权！");		
				 posturl = WebOAuthManage.getBaseOauth2Url(basePath+ "oAuthManage/index.do", basePath+path);
				logger.info("未编码微信授权链接====>" + posturl);
				response.sendRedirect(posturl);
				return false;
			}else{
				PageData pd = new PageData();
				UserInfo userInfo = (UserInfo) session.getAttribute(Const.SESSION_WECHATUSER);
				pd.put("OPENID", userInfo.getOpenid());
				//判断当前微信用户是否以及记录
				if(memberService.findByOpenIdCount(pd)==0){
					logger.info("微信标识OPENID未记录为会员====>" + userInfo.getOpenid());
					//当前微信用户未记录，增加记录
					pd = new PageData();
					pd.put("MEMBERID", UuidUtil.get32UUID());
					pd.put("MEMBERTYPE", 0);
					pd.put("OPENID", userInfo.getOpenid());
					pd.put("WECHATNAME", userInfo.getNickname());
					pd.put("SEX", Convert.strToInt(userInfo.getSex(), 1));
					pd.put("CITY", userInfo.getCity());
					pd.put("COUNTRY", userInfo.getCountry());
					pd.put("PROVINCE", userInfo.getProvince());
					pd.put("LANGUAGE", userInfo.getLanguage());
					pd.put("HEADIMGURL", userInfo.getHeadimgurl());
					pd.put("SUBSCRIBETIME", Convert.strToDate(userInfo.getSubscribe_time(), new Date()));
					pd.put("GROUPID", Convert.strToInt(userInfo.getGroupid(), 0));
					pd.put("REMARKS", userInfo.getRemark());
					pd.put("CREATEDATE", new Date());
					pd.put("VERIFYPHONENUMBER", 0);
					pd.put("SUBSCRIBE", Convert.strToInt(userInfo.getSubscribe(), 0));
					pd.put("VERIFYFAVOURABLE", 0);
					memberService.save(pd);
				}else{
					logger.info("微信标识OPENID已记录为会员====>" + userInfo.getOpenid());
					pd = new PageData();
					pd.put("OPENID", userInfo.getOpenid());
					pd = memberService.findByOpenId(pd);
					logger.info("SUBSCRIBE====>0,["+pd.get("SUBSCRIBE").toString()+"]" + pd.get("SUBSCRIBE").toString().equals("0"));
					logger.info("userInfo.getSubscribe()====>1,["+userInfo.getSubscribe()+"]" + userInfo.getSubscribe().equals("1"));
					if(pd.get("SUBSCRIBE").toString().equals("0") && userInfo.getSubscribe().equals("1")){
						//上次未关注，这次关注当前公众号需要更新用户关注状态
						PageData u = new PageData();
						logger.info("userInfo.getNickname()====>" + userInfo.getNickname());
						u.put("WECHATNAME", userInfo.getNickname());
						u.put("SEX", Convert.strToInt(userInfo.getSex(), 1));
						u.put("CITY", userInfo.getCity());
						u.put("COUNTRY", userInfo.getCountry());
						u.put("PROVINCE", userInfo.getProvince());
						u.put("LANGUAGE", userInfo.getLanguage());
						u.put("HEADIMGURL", userInfo.getHeadimgurl());
						u.put("SUBSCRIBETIME", Convert.strToDate(userInfo.getSubscribe_time(), new Date()));
						u.put("GROUPID", Convert.strToInt(userInfo.getGroupid(), 0));
						u.put("REMARKS", userInfo.getRemark());
						u.put("EDITDATE", new Date());
						u.put("SUBSCRIBE", Convert.strToInt(userInfo.getSubscribe(), 0));
						u.put("OPENID", userInfo.getOpenid());
						memberService.editOpenID(u);
					}
					//当前微信用户已记录，更新记录
					PageData p = new PageData();
					p.put("LASTIP", getIpAddr(request));
					p.put("LASTDATE", new Date());
					p.put("OPENID", userInfo.getOpenid());
					memberService.editLoginInfo(p);
					if (session.getAttribute(Const.SESSION_MEMBER) == null) {
						pd = new PageData();
						pd.put("OPENID", userInfo.getOpenid());
						pd = memberService.findByOpenId(pd);
						member.setCity(pd.getString("CITY"));
						member.setCountry(pd.getString("COUNTRY"));
						member.setHeadImgUrl(pd.getString("HEADIMGURL"));
						member.setLanguage(pd.getString("LANGUAGE"));
						member.setMemberId(pd.getString("MEMBERID"));
						member.setMemberType(pd.getInt("MEMBERTYPE"));
						member.setOpenId(pd.getString("OPENID"));
						member.setProvince(pd.getString("PROVINCE"));
						member.setSex(pd.getInt("SEX"));
						member.setWeChatName(pd.getString("WECHATNAME"));
						member.setID(pd.getInt("ID"));
						//保存用户基本信息
						session.setAttribute(Const.SESSION_MEMBER, member);
					}
					if(Tools.isEmpty(pd.getString("PHONENUMBER")) || pd.getInt("VERIFYPHONENUMBER")==0){
						logger.info("PHONENUMBER IS NOT VERIFYPHONENUMBER");	
						posturl =basePath+"frontMember/goVerify.do?"+STOREID;
						logger.info("go into "+posturl);
						response.sendRedirect(posturl);
						return false;
					}
					
					
				}
				
			}
			
			
		}else{
			logger.info("非微信浏览器");		
			//用于本地调试进行模拟，正式环境注释该模块
//			if (session.getAttribute(Const.SESSION_WECHATUSER) == null) {
//				if(basePath.contains("localhost") || basePath.contains("ddj.java.1yg.tv")){
//					 userInfo = (UserInfo) UserManage.getUserInfo("oJ0qCtzS5uv83EC30B0NgVmlywE0");
//				}else{
//					 userInfo = (UserInfo) UserManage.getUserInfo("oXeXowU94y21bfSd5mOtqbPr8hzY");
//				}
//				session.setAttribute(Const.SESSION_WECHATUSER, userInfo);
//				logger.info("微信昵称-------" + userInfo.getNickname());
//			} 
//			
			//用于本地调试进行模拟，正式环境注释该模块
			posturl=getRequertUrl(request)+"frontStore/er.html";
			response.sendRedirect(posturl);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 获取项目虚拟地址
	 * @return
	 */
	public String getRequertUrl(HttpServletRequest request){
	   String basePath = request.getScheme()+"://"+request.getServerName();
	   if(request.getServerPort()==8080){
		   basePath+=":"+request.getServerPort()+request.getContextPath()+"/";
	   }else{
		   basePath+=request.getContextPath()+"/";
	   }
	   return basePath;
	}
	
	public String getIpAddr(HttpServletRequest request) {     
	      String ip = request.getHeader("x-forwarded-for");     
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("Proxy-Client-IP");     
	     }     
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("WL-Proxy-Client-IP");     
	      }     
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	          ip = request.getRemoteAddr();     
	     }     
	     return ip;     
	}    
	
}
