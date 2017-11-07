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
import com.fh.service.system.coupons.CouponsManager;
import com.fh.service.system.couponsdetail.CouponsDetailManager;
import com.fh.service.system.member.MemberManager;
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
 * 说明：我的优惠券 创建人：Lukas 18923798379 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value = "/myCoupon")
public class myCouponController extends BaseController {

	@Resource(name="couponsdetailService")
	private CouponsDetailManager couponsdetailService;
	
	/**
	 * 列表初始化
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("front/member/mycoupon_list");
		return mv;
	}
	
	
	/**
	 * 优惠券分页
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
		String memberId ="";
		if(Tools.isEmpty(getMemberId())){
			map.put("type", 1);
			map.put("msg", "成功！");
			map.put("data", varList);
			return AppUtil.returnObject(pd, map);
		}
		logger.info("memberId-----------------"+memberId);
		memberId=getMemberId();
		pd.put("memberId", memberId);//当前用户标识
		pd.put("keywords", keywords);//
		pd.put("stateId", -1);//
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
		varList = couponsdetailService.list(page);	//查询当前用户所有优惠券记录
		map.put("type", 1);
		map.put("msg", "成功！");
		map.put("data", varList);
		map.put("totalResult", page.getTotalResult());//总条数
		map.put("totalPage", page.getTotalPage());//总页数
		return AppUtil.returnObject(pd, map);
	}
	
	
	
	 /**去详情页面
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goDetails")
		public ModelAndView goDetails()throws Exception{
			ModelAndView mv = this.getModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			if(Tools.notEmpty(pd.get("id")+"")){
				logger.info("优惠券ID参数为："+pd.get("id")+"");
				pd.put("ID", pd.get("id")+"");
			}else{
				pd.put("numbers", pd.get("numbers")+"");
				logger.info("优惠券numbers参数为："+pd.get("numbers")+"");
			}
			pd = couponsdetailService.findByViewId(pd);	//根据ID读取
			mv.setViewName("front/member/mycoupon_detail");
			mv.addObject("pd", pd);
			return mv;
		}	
	
		
		

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}
}
