package com.fh.controller.front.author;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Member;
import com.fh.entity.Page;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.memberbalance.MemberBalanceManager;
import com.fh.service.system.memberlog.MemberLogManager;
import com.fh.service.system.notelog.NoteLogManager;
import com.fh.service.system.types.TypesManager;
import com.fh.sms.TaoBaoDaYu;
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
 * 说明：用户管理 创建人：Lukas 18923798379 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value = "/frontAuthor")
public class AuthorMemberController extends BaseController {

	@Resource(name = "memberService")
	private MemberManager memberService;
	@Resource(name = "notelogService")
	private NoteLogManager notelogService;


	/**
	 * 去认证页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData s = new PageData();
		s.put("OPENID", getWeChatOpenId());
		s = memberService.findByOpenId(s);
		if(s!=null){
			int memberType =2;
			if(Tools.notEmpty(s.get("MEMBERTYPE")+"")){
				memberType = Convert.strToInt(s.get("MEMBERTYPE")+"", memberType);
			}
			if(memberType==1 ){
				mv.setViewName("front/bindauthor/success");
				return mv;
			}
		}
		mv.setViewName("front/bindauthor/bindMobile");
		return mv;
	}

	/**
	 * 发送短信验证码
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendPhoneCode")
	@ResponseBody
	public Object sendPhoneCode() throws Exception {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		String phone = "";
		long validity = 2L;
		if (Tools.isEmpty(pd.get("phone") + "")) {
			map.put("type", -1);
			map.put("msg", "请输入手机号码！");
			return AppUtil.returnObject(pd, map);
		}
		phone = pd.getString("phone");
		if (!Tools.checkMobileNumber(phone)) {
			map.put("type", -1);
			map.put("msg", "手机号码有误！");
			return AppUtil.returnObject(pd, map);
		}
		pd.put("phoneNumber", phone);
		pd.put("noteType", 1);
		pd = notelogService.findByPhoneNumber(pd);// 查询当前手机号码是否已经发送验证码
		long min = -1L;// 相差分钟
		// 判断验证码是否发送过
		if (pd != null && !Tools.isEmpty(pd.get("CREATEDATE") + "")) {
			min = DateUtil.getDaySubMin(pd.get("CREATEDATE") + "", Convert
					.dateToStr(new Date(), "yyyy-MM-dd HH:mm",
							"1900-01-01 00:00"));
		}else{
			pd = new PageData();
		}
		// 判断验证码是否过期，过期则再次发送
		if (min!=-1L && min < validity) {
			map.put("type", -1);
			map.put("msg", "已发送验证码，无需重复发送！");
			return AppUtil.returnObject(pd, map);
		}
		String Code = Tools.createRandomNum() + "";// 4为随机数作为验证码
		map = TaoBaoDaYu.sendMsgByAutoCode(phone, Code, 3);// 调用阿里大于短信平台发送验证码
		if (!map.get("type").toString().equals("1")) {
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}else if(map.get("type").toString().equals("-101")){
			map.put("type", -1);
			map.put("msg", "已发送验证码，无需重复发送！");
			return AppUtil.returnObject(pd, map);
		}
		pd = new PageData();
		pd.put("NOTETYPE", 2);
		pd.put("PHONENUMBER", phone);
		pd.put("TEXTS", Code);
		pd.put("VALIDITY", validity);
		pd.put("CREATEDATE", new Date());
		notelogService.save(pd);
		map.put("type", 1);
		map.put("msg", "发送成功！");
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 验证验证码并绑定手机号码
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/becomeAuthor")
	@ResponseBody
	public Object becomeAuthor() throws Exception {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		if(Tools.isEmpty(getMemberId())){
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		PageData  c= new PageData();
		c.put("memberId", getMemberId());
		c = memberService.findById(c);//验证是否已经是作者
		if(c==null){
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		if(c.getInt("MEMBERTYPE")==1){
			map.put("type", -1);
			map.put("msg", "您已是作者，无需再次验证！");
			return AppUtil.returnObject(pd, map);
		}
		String phone = "";
		long validity = 2L;
		if (Tools.isEmpty(pd.get("phone") + "")) {
			map.put("type", -1);
			map.put("msg", "请输入手机号码！");
			return AppUtil.returnObject(pd, map);
		}
		phone = pd.getString("phone");
		if (!Tools.checkMobileNumber(phone)) {
			map.put("type", -1);
			map.put("msg", "手机号码有误！");
			return AppUtil.returnObject(pd, map);
		}
		String code = "";
		if (Tools.isEmpty(pd.get("code") + "")) {
			map.put("type", -1);
			map.put("msg", "请输入验证码！");
			return AppUtil.returnObject(pd, map);
		}
		code = pd.getString("code");
		if (code.length() != 4) {
			map.put("type", -1);
			map.put("msg", "请输入4位数验证码！");
			return AppUtil.returnObject(pd, map);
		}
		String pwd = "";
		if (Tools.isEmpty(pd.get("pwd") + "")) {
			map.put("type", -1);
			map.put("msg", "请输入密码！");
			return AppUtil.returnObject(pd, map);
		}
		pwd = pd.getString("pwd");
		String pwdAgain = "";
		if (Tools.isEmpty(pd.get("pwdAgain") + "")) {
			map.put("type", -1);
			map.put("msg", "请输入确认密码！");
			return AppUtil.returnObject(pd, map);
		}
		pwdAgain = pd.getString("pwdAgain");
		if(!pwd.equals(pwdAgain)){
			map.put("type", -1);
			map.put("msg", "密码输入不一致！");
			return AppUtil.returnObject(pd, map);
		}
		String passWord=Encrypt.MD5ByKey(pwd);
		pd.put("phoneNumber", phone);
		pd.put("texts", code);
		pd.put("noteType", 2);
		pd = notelogService.findByPhoneNumber(pd);// 查询当前手机号码验证码记录
		if (pd == null || Tools.isEmpty(pd.get("PHONENUMBER") + "")) {
			map.put("type", -1);
			map.put("msg", "当前手机号码，未发送短信验证码！");
			return AppUtil.returnObject(pd, map);
		}
		PageData s = new PageData();
		s.put("phoneNumber", phone);
		s.put("memberId", getMemberId());
		s = memberService.findByPhoneNumber(s);
		if(s!=null){
			int verifyPhoneNumber =0;
			if(Tools.notEmpty(s.get("VERIFYPHONENUMBER")+"")){
				verifyPhoneNumber = Convert.strToInt(s.get("VERIFYPHONENUMBER")+"", verifyPhoneNumber);
			}
			if(verifyPhoneNumber==1){
				map.put("type", -1);
				map.put("msg", "当前手机号码，已被认证！");
				return AppUtil.returnObject(pd, map);
			}
		}
		long min = -1L;// 相差分钟
		String CREATEDATE = pd.get("CREATEDATE") + "";// 验证码发送时间
		if (Tools.isEmpty(CREATEDATE)) {
			map.put("type", -1);
			map.put("msg", "当前手机号码，未发送短信验证码！");
			return AppUtil.returnObject(pd, map);
		}
		String sendCode = pd.get("TEXTS") + "";// 发送的验证码
		if (Tools.isEmpty(sendCode)) {
			map.put("type", -1);
			map.put("msg", "当前手机号码，未发送短信验证码！");
			return AppUtil.returnObject(pd, map);
		}
		// 判断验证是否一致
		if (!code.equals(sendCode)) {
			map.put("type", -1);
			map.put("msg", "验证码不正确！");
			return AppUtil.returnObject(pd, map);
		}
		// 判断验证码是否过期
		min = DateUtil.getDaySubMin(CREATEDATE, Convert.dateToStr(new Date(),
				"yyyy-MM-dd HH:mm", "1900-01-01 00:00"));
		if (min !=-1L && min > validity) {
			map.put("type", -1);
			map.put("msg", "验证码已过期，请重新发送！");
			return AppUtil.returnObject(pd, map);
		}
		// 修改用户绑定手机信息
		pd.put("MEMBERTYPE", 1);
		pd.put("PHONENUMBER", phone);
		pd.put("VERIFYPHONENUMBER", 1);
		pd.put("MEMBERID", getMemberId());
		pd.put("PASSWORD",passWord);
		// 修改用户绑定手机信息
		// 记录用户操作信息
		PageData p = new PageData();
		p.put("LOGTYPE", 3);// 记录类型 0.未知 1.登录 2.退出 3.操作
		p.put("CONTENTS", getMemberName() + "[绑定手机号码" + phone + "]");
		p.put("CREATEDATE", new Date());
		p.put("LOGIP", PublicUtil.getIp());
		p.put("MEMBERID", getMemberId());
		// 记录用户操作信息
		try {
			memberService.editMemberType(pd, p);
		} catch (Exception e) {
			logger.info("认证成功为作者异常："+e.toString());
			e.printStackTrace();
		}
		Member member = new Member();
		pd = new PageData();
		Session session = Jurisdiction.getSession();
		pd.put("memberId", getMemberId());
		pd = memberService.findById(pd);
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
		//保存用户基本信息
		session.removeAttribute(Const.SESSION_MEMBER);
		session.setAttribute(Const.SESSION_MEMBER, member);
		map.put("type", 1);
		map.put("msg", "认证成功！");
		return AppUtil.returnObject(pd, map);
	}

	
	/**
	 * 去认证成功页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goBindSuccess")
	public ModelAndView goBindSuccess() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("front/bindauthor/bindsuccess");
		return mv;
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}
}
