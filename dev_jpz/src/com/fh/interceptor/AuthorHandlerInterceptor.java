package com.fh.interceptor;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fh.entity.Member;
import com.fh.entity.system.User;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.memberlog.MemberLogManager;
import com.fh.util.Const;
import com.fh.util.Convert;
import com.fh.util.Jurisdiction;
import com.fh.util.Logger;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.UserManage;
import com.fh.wechat.gongzhong.WebOAuthManage;
import com.fh.wechat.gongzhong.vo.user.UserInfo;

/**
 * 
* 类名称：作者登录拦截器
* 类描述： 
* @author lukas 414024003@qq.com
* 作者单位： 
* 联系方式：
* 创建时间：2015年11月2日
* @version 1.6
 */
public class AuthorHandlerInterceptor extends HandlerInterceptorAdapter{

	protected Logger logger = Logger.getLogger(this.getClass());
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String path = request.getServletPath();
		Member user = (Member)Jurisdiction.getSession().getAttribute(Const.SESSION_AUTHOR);
		if(user!=null){
			if(user.getMemberType()!=1){
				response.sendRedirect(request.getContextPath() + Const.LOGIN_AUTHOR);
			}
			return true;
		}else{
			//登陆过滤
			response.sendRedirect(request.getContextPath() + "/frontLogin/index.html");
			return false;		
		}

	}
	
	
	/**
	 * 获取项目虚拟地址
	 * @return
	 */
	public String getRequertUrl(HttpServletRequest request){
	   String basePath = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/";
	   return basePath;
	}
	
}
