package com.fh.controller.front.login;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.service.system.article.ArticleManager;
import com.fh.service.system.member.MemberManager;
import com.fh.util.PageData;
import com.fh.util.Tools;


/** 
 * 说明：登录管理
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value="/frontsLogin")
public class frontLoginController extends BaseController {
	
	@Resource(name="memberService")
	private MemberManager memberService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/index")
	public ModelAndView index()
			throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		mv.addObject("msg", "成功");
		mv.addObject("pd", pd);
		mv.setViewName("front/login");
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
