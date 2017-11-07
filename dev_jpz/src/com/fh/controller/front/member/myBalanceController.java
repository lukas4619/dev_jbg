package com.fh.controller.front.member;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.balancedetails.BalanceDetailsManager;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.memberbalance.MemberBalanceManager;
import com.fh.service.system.memberlog.MemberLogManager;
import com.fh.service.system.notelog.NoteLogManager;
import com.fh.service.system.revenue.RevenueManager;
import com.fh.sms.TaoBaoDaYu;
import com.fh.util.AppUtil;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;

/**
 * 说明：我的结算记录 创建人：Lukas 18923798379 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value = "/myBalance")
public class myBalanceController extends BaseController {

	@Resource(name = "memberbalanceService")
	private MemberBalanceManager memberbalanceService;
	@Resource(name = "memberService")
	private MemberManager memberService;
	@Resource(name = "balancedetailsService")
	private BalanceDetailsManager balancedetailsService;
	
	
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
		mv.setViewName("front/member/income");
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	/**
	 * 提现记录列表初始化
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/outrecord")
	public ModelAndView outrecord() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("front/member/mybalance_list");
		mv.addObject("pd", pd);
		return mv;
	}
	

	/**
	 * 结算分页
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPage")
	@ResponseBody
	public Object listPage() throws Exception {
		Page page = new Page();
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		List<PageData>	varList  = new ArrayList<PageData>();
		pd = this.getPageData();
		String keywords="";
		if(!Tools.isEmpty(pd.get("keywords")+"")){
			keywords = pd.getString("keywords");
		}
		if(Tools.isEmpty(getMemberId())){
			map.put("type", 1);
			map.put("msg", "成功！");
			map.put("data", varList);
			return AppUtil.returnObject(pd, map);
		}
		pd.put("memberID", getMemberId());//当前用户标识
		pd.put("keywords", keywords);//
		pd.put("balanceState", -1);//
		int CurrentPage = 1;//当前页
		if(Tools.notEmpty(pd.get("CurrentPage")+"")){
			CurrentPage = Convert.strToInt(pd.get("CurrentPage")+"", CurrentPage);
		}
		page.setCurrentPage(CurrentPage);//设置当前页
		int ShowCount=10;
		if(Tools.notEmpty(pd.get("ShowCount")+"")){
			ShowCount = Convert.strToInt(pd.get("ShowCount")+"", ShowCount);
		}
		page.setShowCount(ShowCount);
		page.setPd(pd);
		varList = memberbalanceService.datalistPageByMember(page);	//查询当前用户所有预订记录
		map.put("type", 1);
		map.put("msg", "成功！");
		map.put("data", varList);
		map.put("totalResult", page.getTotalResult());//总条数
		map.put("totalPage", page.getTotalPage());//总页数
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 去结算详情页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goDetails")
	public ModelAndView goDetails() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String BALANCEID ="";
		if(!Tools.isEmpty(pd.get("id")+"")){
			BALANCEID=pd.getString("id");
		}
		pd.put("BALANCEID", BALANCEID);
		pd = memberbalanceService.findById(pd); // 根据ID读取
		PageData p = new PageData();
		p.put("balanceID", BALANCEID);
		List<PageData> varList=balancedetailsService.listAll(p);
		mv.setViewName("front/member/mybalance_detail");
		mv.addObject("pd", pd);
		mv.addObject("varList", varList);
		return mv;
	}


	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}
}
