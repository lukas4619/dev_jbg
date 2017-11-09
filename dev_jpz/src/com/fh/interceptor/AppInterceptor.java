package com.fh.interceptor;

import java.util.Enumeration;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fh.entity.Member;
import com.fh.entity.system.User;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
/**
 * 
* 类名称：接口验证
* 类描述： 
* @author lukas 414024003@qq.com
* 作者单位： 
* 联系方式：
* 创建时间：2015年11月2日
* @version 1.6
 */
public class AppInterceptor extends HandlerInterceptorAdapter{
	
	//接口路径
	public static final String NO_INTERCEPTOR_PATH = ".*/((appIndex)).*";	//不对匹配该值的访问路径拦截（正则）
	//|(logout)|(code)|(frontStore)|(app)|(MP_verify_0eQuFBh07Z8oNTQz.txt)|(weixin)|(wechat)|(news)|(oAuthManage)|(frontLogin)|(frontMember)|(frontAuthor)|(myRevenue)|(frontReservate)|(myBalance)|(myReservation)|(myArticle)|(myCoupon)|(frontProduct)|(plugins)|(weChatPay)|(frontLottery)|(uploadFiles)|(static)|(main)|(websocket)

	//接口名称
	public static final String NO_INTERCEPTOR_APPNAME = ".*/((index))|(suborder).*";	//不对匹配该值的访问路径拦截（正则）
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		 String path = request.getServletPath();///app/testApp
		if(path.matches(NO_INTERCEPTOR_APPNAME)){
			return true;
		}
		return false;
	}
	
}
