package com.fh.controller.front.member;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.memberbalance.MemberBalanceManager;
import com.fh.service.system.memberlog.MemberLogManager;
import com.fh.service.system.notelog.NoteLogManager;
import com.fh.service.system.types.TypesManager;
import com.fh.sms.TaoBaoDaYu;
import com.fh.sms.ssm;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.util.security.Encrypt;
import com.fh.wechat.gongzhong.vo.user.UserInfo;

/**
 * 说明：用户管理 创建人：Lukas 18923798379 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value = "/frontMember")
public class frontMemberController extends BaseController {

	@Resource(name = "memberService")
	private MemberManager memberService;
	@Resource(name = "notelogService")
	private NoteLogManager notelogService;
	@Resource(name="memberbalanceService")
	private MemberBalanceManager memberbalanceService;
	@Resource(name = "typesService")
	private TypesManager typesService;

	/**
	 * 用户信息列表
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd.put("memberId", getMemberId());
		pd = memberService.findById(pd);
		mv.addObject("msg", "成功");
		mv.addObject("pd", pd);
		mv.setViewName("front/member/centre");
		return mv;
	}

	/**
	 * 去验证手机号码有效性页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goVerify")
	public ModelAndView goVerify() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd =this.getPageData();
		mv.addObject("pd", pd);
		mv.setViewName("front/member/bindMobile");
		return mv;
	}
	
	/**
	 * 跳转到修改密码页面
	 */
	@RequestMapping(value = "/goModifyPass")
	public ModelAndView goModifyPass() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("front/member/modifyPass");
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
		long validity = 5L;
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
			min = DateUtil.getDaySubMin(pd.get("CREATEDATE") + "", Convert.dateToStr(new Date(), "yyyy-MM-dd HH:mm","1900-01-01 00:00"));
		}else{
			pd = new PageData();
		}
		// 判断验证码是否过期，过期则再次发送
		if (min!=-1L && min < validity) {
			map.put("type", -1);
			map.put("msg", "已发送验证码，无需重复发送！");
			return AppUtil.returnObject(pd, map);
		}
		int sendType = 1;
		String Code = Tools.createRandomNum() + "";// 4为随机数作为验证码
		map =ssm.sendSms(phone, Code,sendType);// TaoBaoDaYu.sendMsgByAutoCode(phone, Code, sendType);// 调用阿里大于短信平台发送验证码
		if(!map.get("type").toString().equals("0")){
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		pd = new PageData();
		pd.put("NOTETYPE", 1);
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
	@RequestMapping(value = "/codeVerify")
	@ResponseBody
	public Object codeVerify() throws Exception {
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
		pd.put("phoneNumber", phone);
		pd.put("texts", code);
		pd.put("noteType", 1);
		pd = notelogService.findByPhoneNumber(pd);// 查询当前手机号码验证码记录
		if (pd == null || Tools.isEmpty(pd.get("PHONENUMBER") + "")) {
			map.put("type", -1);
			map.put("msg", "当前手机号码，未发送短信验证码！");
			return AppUtil.returnObject(pd, map);
		}
		validity = pd.getInt("VALIDITY");
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
				map.put("msg", "当前手机号码，已被绑定！");
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
		min = DateUtil.getDaySubMin(CREATEDATE, Convert.dateToStr(new Date(),"yyyy-MM-dd HH:mm", "1900-01-01 00:00"));
		if (min !=-1L && min > validity) {
			map.put("type", -1);
			map.put("msg", "验证码已过期，请重新发送！");
			return AppUtil.returnObject(pd, map);
		}
		// 修改用户绑定手机信息
		pd.put("PHONENUMBER", phone);
		pd.put("VERIFYPHONENUMBER", 1);
		pd.put("VERIFYFAVOURABLE", 1);
		pd.put("MEMBERID", getMemberId());
		// 修改用户绑定手机信息
		memberService.editPhoneNumber(pd);
		map.put("type", 1);
		map.put("msg", "验证成功！");
		return AppUtil.returnObject(pd, map);
	}
	/**
	 * 修改密码校验手机验证码逻辑
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="verPhoneToUpdatePass")
	@ResponseBody
	public Object verPhoneToUpdatePass() throws Exception{
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
		pd.put("phoneNumber", phone);
		pd.put("texts", code);
		pd.put("noteType", 1);
		pd = notelogService.findByPhoneNumber(pd);// 查询当前手机号码验证码记录
		if (pd == null || Tools.isEmpty(pd.get("PHONENUMBER") + "")) {
			map.put("type", -1);
			map.put("msg", "当前手机号码，未发送短信验证码！");
			return AppUtil.returnObject(pd, map);
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
		
		
		map.put("type", 1);
		map.put("msg", "验证成功！");
		return AppUtil.returnObject(pd, map);
	
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value="goModifyPassInput")
	public ModelAndView goModifyPassInput(){
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("front/member/modifyPassInput");
		return mv;
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	@RequestMapping(value="/modifyPass")
	@ResponseBody
	public Object modifyPass()throws Exception{
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		//Encrypt.MD5ByKey(KEYDATA[1]);	//登录过来的密码
		String password = pd.getString("passWord");
		pd.put("password", Encrypt.MD5ByKey(password));
		String memberId = getMemberId();
		pd.put("memberId", memberId);
		memberService.editPassWord(pd);
		map.put("type", 1);
		map.put("msg", "验证成功！");
		return AppUtil.returnObject(pd, map);
	} 
	
	/**
	 * 去绑定手机号码成功页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goBindSuccess")
	public ModelAndView goBindSuccess() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("front/member/bindsuccess");
		return mv;
	}
	
	/**
	 * 去申请提现页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goWithdraw")
	public ModelAndView goWithdraw() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd.put("memberId", getMemberId());
		pd = memberService.findById(pd);
		int verifyPhoneNumber=0;
		if(!Tools.isEmpty(pd.get("VERIFYPHONENUMBER")+"")){
			verifyPhoneNumber = Convert.strToInt(pd.get("VERIFYPHONENUMBER")+"", verifyPhoneNumber);
		}
		if(verifyPhoneNumber==0){
			mv.setViewName("front/member/bindMobile");
			return mv;
		}
		mv.addObject("pd", pd);
		pd = new PageData();
		List<PageData> typeList = new ArrayList<PageData>();
		pd.put("typeClass", "6");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		mv.setViewName("front/member/withdraw");
		return mv;
	}

	/**
	 * 提交提现申请
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/subWithdraw")
	@ResponseBody
	public Object subWithdraw() throws Exception {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		String memberId= getMemberId();
		if (Tools.isEmpty(memberId)) {
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后！");
			return AppUtil.returnObject(pd, map);
		}
		int balanceType = -1;// 体现类型 卡券26、现金27 
		if (Tools.isEmpty(pd.get("balanceType") + "")) {
			map.put("type", -1);
			map.put("msg", "请选择提现方式！");
			return AppUtil.returnObject(pd, map);
		}
		balanceType = Convert.strToInt(pd.get("balanceType") + "", balanceType);
		if (balanceType == -1) {
			map.put("type", -1);
			map.put("msg", "请选择提现方式！");
			return AppUtil.returnObject(pd, map);
		}
		String balanceNumber = Tools.createNumber();// 提现编号
		double balanceMoney = 0.00;// 体现金额
		if (Tools.isEmpty(pd.get("balanceMoney") + "")) {
			map.put("type", -1);
			map.put("msg", "请输入提现金额！");
			return AppUtil.returnObject(pd, map);
		}
		balanceMoney = Convert.strToDouble(pd.get("balanceMoney") + "",
				balanceMoney);
		if (balanceMoney < 0) {
			map.put("type", -1);
			map.put("msg", "请输入提现金额！");
			return AppUtil.returnObject(pd, map);
		}
		pd = new PageData();
		pd.put("memberId",memberId);
		pd = memberService.findById(pd);
		double bMoney = 0.00;// 可体现金额
		if (Tools.isEmpty(pd.get("BALANCEMONEY") + "")) {
			map.put("type", -1);
			map.put("msg", "账户可提现余额不足！");
			return AppUtil.returnObject(pd, map);
		}
		bMoney = Convert.strToDouble(pd.get("BALANCEMONEY") + "", bMoney);
		if (bMoney <= 0) {
			map.put("type", -1);
			map.put("msg", "账户可提现余额不足！");
			return AppUtil.returnObject(pd, map);
		}
		if (balanceMoney > bMoney) {
			map.put("type", -1);
			map.put("msg", "账户可提现余额不足！");
			return AppUtil.returnObject(pd, map);
		}
		double freezeMoney=0.00;//当前冻结金额
		if (Tools.isEmpty(pd.get("FREEZEMONEY") + "")) {
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后！");
			return AppUtil.returnObject(pd, map);
		}
		freezeMoney = Convert.strToDouble(pd.get("FREEZEMONEY") + "", freezeMoney);
		// 增加会员提现记录数据
		pd= new PageData();
		String BALANCEID=get32UUID();
		pd.put("BALANCEID", BALANCEID);
		pd.put("BALANCETYPE", balanceType);
		pd.put("BALANCENUMBER", balanceNumber);
		pd.put("MEMBERID", memberId);
		pd.put("BALANCEMONEY", balanceMoney);
		pd.put("BALANCESTATE", 28);
		pd.put("CREATEDATE", new Date());
		pd.put("BALANCEREMARK", "");
		// 增加会员提现记录数据
		// 修改会员可提现金额和冻结金额数据
		PageData member = new PageData();
		member.put("BALANCEMONEY", bMoney-balanceMoney);
		member.put("FREEZEMONEY", freezeMoney+balanceMoney);
		member.put("MEMBERID", memberId);
		// 修改会员可提现金额和冻结金额数据
		// 记录用户操作信息
		PageData p = new PageData();
		p.put("LOGTYPE", 3);// 记录类型 0.未知 1.登录 2.退出 3.操作
		p.put("CONTENTS", getMemberName() + "[申请提现]，提现编号："+balanceNumber+",提现金额为：" + balanceMoney);
		p.put("CREATEDATE", new Date());
		p.put("LOGIP", PublicUtil.getIp());
		p.put("MEMBERID", memberId);
		// 记录用户操作信息
		// 记录提现进度
		PageData d = new PageData();
		d.put("DETAILS", getMemberName() + "[申请提现]");
		d.put("CREATEDATE", new Date());
		d.put("OPERATIONNAME", getMemberName());
		d.put("BALANCEID", BALANCEID);
		// 记录提现进度
		try {
			memberbalanceService.save(pd, member, p,d);
			// TODO 增加提现进度通知
		} catch (Exception e) {
			logger.info("提交提现申请异常："+e.toString());
			e.printStackTrace();
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后！");
			return AppUtil.returnObject(pd, map);
		}		
		map.put("type", 1);
		map.put("msg", "成功！");
		map.put("id",BALANCEID );
		return AppUtil.returnObject(pd, map);
	}

	
	/**
	 * 去提现提交完成页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/withdrawFinish")
	public ModelAndView withdrawFinish() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String  BALANCEID="";
		if(!Tools.isEmpty(pd.get("id")+"")){
			BALANCEID = pd.getString("id");
		}
		pd.put("BALANCEID", BALANCEID);
		pd = memberbalanceService.findById(pd);
		mv.addObject("pd", pd);
		mv.setViewName("front/member/sub_withdraw");
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}
}
