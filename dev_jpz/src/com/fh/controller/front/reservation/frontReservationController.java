package com.fh.controller.front.reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.reservation.ReservationManager;
import com.fh.service.system.states.StatesManager;
import com.fh.service.system.types.TypesManager;
import com.fh.service.system.types.impl.TypesService;
import com.fh.util.AppUtil;
import com.fh.util.Convert;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import com.fh.util.Tools;

/**
 * 预定信息Controller
 * @author zzx
 * @date 2016-09-02 17:46
 * 
 */
@RequestMapping(value="/frontReservate")
@Controller
public class frontReservationController extends BaseController{
	
	@Resource(name="reservationService")
	private ReservationManager reservationService;
	@Resource(name="typesService")
	private TypesManager typesService;
	@Resource(name="statesService")
	private StatesManager statesService;
	
	@RequestMapping(value="/index")
	public ModelAndView index(){
		ModelAndView mv = this.getModelAndView();
		String openId = getWeChatOpenId();
		mv.setViewName("front/reservate/reservate");
		mv.addObject("openId", openId);
		return mv;
	}
	
	@RequestMapping(value="/ajaxReservatePage")
	@ResponseBody
	public Object ajaxReservatePage() throws Exception{
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		Page page = new Page();
		
		PageData pd = new PageData();
		pd = this.getPageData();
		if(Tools.notEmpty(pd.getString("currentPage"))){
			page.setCurrentPage(Integer.parseInt(pd.getString("currentPage")));
		}
		if(Tools.notEmpty(pd.getString("showCount"))){
			page.setShowCount(Integer.parseInt(pd.getString("showCount")));
		}
		if(Tools.notEmpty(pd.getString("keywords"))){
			pd.put("keywords", pd.getString("keywords"));
		}
		if(!Tools.isEmpty(pd.get("lastStart")+"")){
			pd.put("lastStart",pd.get("lastStart")+"");
		}
		if(!Tools.isEmpty(pd.get("lastEnd")+"")){
			pd.put("lastEnd",pd.get("lastEnd")+"");
		}
		List<PageData> varList=null;
		if(!Tools.isEmpty(pd.get("memberId")+"")){
			pd.put("memberId",pd.get("memberId")+"");
			pd.put("reservationType", Convert.strToInt(pd.get("reservationType")+"", -1));
			pd.put("reservationStateID", Convert.strToInt(pd.get("reservationStateID")+"", -1));
			page.setPd(pd);
			varList = reservationService.list(page);	//列出Reservation列表
		}
		//AppUtil.returnObject(pd, map)
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type", 1);
		map.put("msg", "成功！");
		map.put("data", varList);
		map.put("total", page.getTotalResult());
		return AppUtil.returnObject(pd, map);
	}
	
	/**
	 * 跳转到预定详情页面
	 * @throws Exception 
	 */
	@RequestMapping(value="/goToOrderDetail")
	public ModelAndView goToOrderDetail() throws Exception{
		PageData pd = this.getPageData();
		ModelAndView mv = this.getModelAndView();
		pd = reservationService.findById(pd);
		mv.setViewName("front/reservate/orderdetail");
		mv.addObject("pd", pd);
		return mv;
	}
}
