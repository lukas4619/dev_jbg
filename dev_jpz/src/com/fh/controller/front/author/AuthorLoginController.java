package com.fh.controller.front.author;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Member;
import com.fh.entity.system.Menu;
import com.fh.entity.system.Role;
import com.fh.entity.system.User;
import com.fh.service.system.article.ArticleManager;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.memberlog.MemberLogManager;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.util.security.Encrypt;


/** 
 * 说明：作者登录管理
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value="/frontLogin")
public class AuthorLoginController extends BaseController {
	
	@Resource(name="memberService")
	private MemberManager memberService;
	@Resource(name="memberlogService")
	private MemberLogManager memberlogService;
	
	/**
	 * 首页
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
		pd.put("COPYRIGHT", Tools.readTxtFile(Const.COPYRIGHT)); //读取系统版权信息
		mv.setViewName("front/author/login");
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	
	/**请求登录，验证用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/login_login" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object login()throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "";
		String KEYDATA[] = pd.getString("KEYDATA").replaceAll("victa", "").replaceAll("wlsq", "").split(",ddj,");
		if(null != KEYDATA && KEYDATA.length == 3){
			Session session = Jurisdiction.getSession();
			String sessionCode = (String)session.getAttribute(Const.SESSION_SECURITY_CODE);		//获取session中的验证码
			String code = KEYDATA[2];
			if(null == code || "".equals(code)){//判断效验码
				errInfo = "nullcode"; 			//效验码为空
			}else{
				String USERNAME = KEYDATA[0];	//登录过来的用户名
				String PASSWORD  =Encrypt.MD5ByKey(KEYDATA[1]);	//登录过来的密码
				
				if(Tools.notEmpty(sessionCode) && sessionCode.equalsIgnoreCase(code)){		//判断登录验证码
				
				pd.put("PHONENUMBER", USERNAME);
				pd.put("PASSWORD", PASSWORD);
				pd=memberService.getMemberByNameAndPwd(pd);
				if(pd!=null){
					Member member = new Member();
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
					member.setMemberId(pd.getString("MEMBERID"));
					member.setPassWord(PASSWORD);
					//保存用户基本信息
					session.setAttribute(Const.SESSION_AUTHOR, member);
					session.removeAttribute(Const.SESSION_SECURITY_CODE);	//清除登录验证码的session
					pd = new PageData();
					pd.put("LOGTYPE", 1);
					pd.put("CONTENTS", member.getWeChatName()+"[登录]");
					pd.put("CREATEDATE", new Date());
					pd.put("LOGIP", PublicUtil.getIp());
					pd.put("MEMBERID", member.getMemberId());
					memberlogService.save(pd);//保存登录记录
				}else{
					errInfo = "usererror"; 				//用户名或密码有误
					logBefore(logger, USERNAME+"登录系统密码或用户名错误");
				}
				}else{
					errInfo = "codeerror";				 	//验证码输入有误
				}
				if(Tools.isEmpty(errInfo)){
					errInfo = "success";					//验证成功
					logBefore(logger, USERNAME+"登录系统");
				}
			}
		}else{
			errInfo = "error";	//缺少参数
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	
	/**访问系统首页
	 * @param changeMenu：切换菜单参数
	 * @return
	 */
	@RequestMapping(value="/main/{changeMenu}")
	public ModelAndView login_index(@PathVariable("changeMenu") String changeMenu){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Member member = new Member();
		try{
			Session session = Jurisdiction.getSession();
			 member = (Member)session.getAttribute(Const.SESSION_AUTHOR);						//读取session中的用户信息(单独用户信息)
			if (member != null) {
				mv.setViewName("front/author/main");
				mv.addObject("member", member);
			}else {
				mv.setViewName("front/author/index");//session失效后跳转登录页面
			}
		} catch(Exception e){
			mv.setViewName("front/author/index");
			logger.error(e.getMessage(), e);
		}
		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**
	 * 进入tab标签
	 * @return
	 */
	@RequestMapping(value="/tab")
	public String tab(){
		return "front/author/tab";
	}
	
	/**
	 * 进入首页后的默认页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/login_default")
	public ModelAndView defaultPage() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
		mv.addObject("pd",pd);
		mv.setViewName("front/author/default");
		return mv;
	}
	
	@RequestMapping(value="/logout")
	public ModelAndView logout(){
		ModelAndView mv = this.getModelAndView();
		Jurisdiction.getSession().removeAttribute(Const.SESSION_AUTHOR);
		PageData pd = new PageData();
		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
		mv.setViewName("front/author/login");
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**去修改用户页面(个人修改)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditMyU")
	public ModelAndView goEditMyU() throws Exception{
		ModelAndView mv = this.getModelAndView();
		Member member = (Member)Jurisdiction.getSession().getAttribute(Const.SESSION_AUTHOR);
		if(member==null){
			return null;
		}
		mv.addObject("member", member);
		mv.setViewName("front/author/member_edit");
		mv.addObject("msg", "editU");
		return mv;
	}
	
	/**
	 * 修改用户
	 */
	@RequestMapping(value="/editMember")
	public ModelAndView editU() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		Member member = (Member)Jurisdiction.getSession().getAttribute(Const.SESSION_AUTHOR);
		if(member==null){
			return null;
		}
		pd.put("password", Encrypt.MD5ByKey(pd.getString("PASSWORD")));
		pd.put("memberId", member.getMemberId());
		memberService.editPassWord(pd);	//执行修改
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
