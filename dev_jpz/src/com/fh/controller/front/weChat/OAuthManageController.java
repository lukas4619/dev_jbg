package com.fh.controller.front.weChat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fh.controller.base.BaseController;
import com.fh.entity.Member;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.memberlog.MemberLogManager;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.UserManage;
import com.fh.wechat.gongzhong.WebOAuthManage;
import com.fh.wechat.gongzhong.vo.user.UserInfo;
import com.fh.wechat.gongzhong.vo.weboauth.OauthAccessToken;

/**
 * 说明：微信授权 创建人：Lukas 18923798379 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value = "/oAuthManage")
public class OAuthManageController extends BaseController {

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public String OAuthManage(HttpServletRequest request, HttpServletResponse r) {
		logger.info("进入授权方法");
		UserInfo userInfo = new UserInfo();
		Member member = new Member();
		Session session = Jurisdiction.getSession();
		// 如果session未空或者取消授权，重定向到授权页面
		String resulturl = getRequertUrl() + Const.LOGIN_URL;
		if (request.getParameter("state") != null) {
			resulturl = request.getParameter("state");// 返回页面
			logger.info("获取state参数");
		} else {
			logger.info("没有state参数");
		}
		logger.info("--获取code:" + request.getParameter("state"));
		logger.info("state参数：" + resulturl);
		try {
			if (session.getAttribute(Const.SESSION_WECHATUSER) == null) {
				// 1》用户授权，获取code
				if (request.getParameter("code") != null) {
					String code = request.getParameter("code");
					// 2》通过code换取网页授权access_token
					logger.info("--获取code:" + code);
					OauthAccessToken oauthAccessToken = WebOAuthManage.getAccessToken(code);
					// 3》access_token中的openId获得用户信息
					logger.info("--获取oauthAccessToken:" + oauthAccessToken.getAccess_token());
					logger.info("--获取GetOpenid:" + oauthAccessToken.getOpenid());
//					userInfo = (UserInfo) UserManage.getUserInfo(oauthAccessToken.getOpenid(),oauthAccessToken.getAccess_token());
					userInfo = (UserInfo) UserManage.getUserInfo(oauthAccessToken.getOpenid());
					if (userInfo == null) {
						logger.info("--获取微信信息失败");
					} else {
						// 保存当前微信用户信息
						session.setAttribute(Const.SESSION_WECHATUSER, userInfo);
						logger.info("--获取微信信息" + userInfo);
						
					}
				} else {
					logger.info("code参数异常");
				}

			} else {
				logger.info("SESSION_WECHATUSER记录已经存在无需授权！");
				userInfo = (UserInfo) session.getAttribute(Const.SESSION_WECHATUSER);
			}
			
			logger.info("resulturl---" + resulturl);
			r.sendRedirect(resulturl);
		} catch (InvalidSessionException e) {
			logger.info("InvalidSessionException异常："+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("IOException异常："+e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("Exception异常："+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}
}
