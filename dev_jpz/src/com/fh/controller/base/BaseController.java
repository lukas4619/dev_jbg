package com.fh.controller.base;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fh.entity.Member;
import com.fh.entity.Page;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import com.fh.util.Logger;
import com.fh.util.PageData;
import com.fh.util.UuidUtil;
import com.fh.wechat.gongzhong.vo.user.UserInfo;

/**
 * @author lukas 414024003@qq.com
 * controller基类
 * 修改时间：2015、12、11
 */
public class BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 6357869213649815390L;
	
	/** new PageData对象
	 * @return
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}

	
	/**得到ModelAndView
	 * @return
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	public String getIpAddr() {     
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
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
	
	/**得到request对象
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	
	/**得到request对象
	 * @return
	 */
	public HttpServletResponse getResponse() {
		ServletWebRequest servletWebRequest=new ServletWebRequest(getRequest());
		return  servletWebRequest.getResponse();
	}

	/**
	 * 获取项目虚拟地址
	 * @return
	 */
	public String getRequertUrl(){
	   String basePath = getRequest().getScheme()+"://"+getRequest().getServerName()+getRequest().getContextPath()+"/";
	   return basePath;
	}
	
	/**
	 * 获取当前页面地址(完整地址包含参数)
	 * @return
	 */
	public String getRequertNowUrl(){
	   String path = getRequertUrl()+getRequest().getServletPath().substring(1,getRequest().getServletPath().length());
		if(getRequest().getQueryString()!=null){
			path+="?" + getRequest().getQueryString();
		}
	   return path;
	}
	
	/**
	 * 获取物理路径
	 * @return
	 */
	public String getPath(){
	 String path = getRequest().getServletContext().getRealPath("");
		return path;
	}
	
	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	/**得到分页列表的信息
	 * @return
	 */
	public Page getPage(){
		return new Page();
	}
	
	/**
	 * 获取用户标识（注册过）
	 * @return
	 */
	public String getMemberId(){
		Session session = Jurisdiction.getSession();
		Member member = (Member) session.getAttribute(Const.SESSION_MEMBER);
		if(member==null){
			return "";
		}
		return member.getMemberId();
	}
	
	/**
	 * 获取用户类型（注册过）
	 * @return
	 */
	public int getMemberType(){
		Session session = Jurisdiction.getSession();
		Member member = (Member) session.getAttribute(Const.SESSION_MEMBER);
		if(member==null){
			return -1;
		}
		return member.getMemberType();
	}
	
	/**
	 * 获取用户微信标识（注册过）
	 * @return
	 */
	public String getMemberOpenId(){
		Session session = Jurisdiction.getSession();
		Member member = (Member) session.getAttribute(Const.SESSION_MEMBER);
		if(member==null){
			return "";
		}
		return member.getOpenId();
	}
	
	/**
	 * 获取用户微信昵称（注册过）
	 * @return
	 */
	public String getMemberName(){
		Session session = Jurisdiction.getSession();
		Member member = (Member) session.getAttribute(Const.SESSION_MEMBER);
		if(member==null){
			return "";
		}
		return member.getWeChatName();
	}
	
	/**
	 * 获取微信用户微信标识
	 * @return
	 */
	public String getWeChatOpenId(){
		Session session = Jurisdiction.getSession();
		UserInfo  user = (UserInfo) session.getAttribute(Const.SESSION_WECHATUSER);
		if(user==null){
			return "";
		}
		return user.getOpenid();
	}
	
	/**
	 * 获取微信用户微信昵称
	 * @return
	 */
	public String getWeChatName(){
		Session session = Jurisdiction.getSession();
		UserInfo  user = (UserInfo) session.getAttribute(Const.SESSION_WECHATUSER);
		if(user==null){
			return "";
		}
		return user.getNickname();
	}
	
	
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
	
}
