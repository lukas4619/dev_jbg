package com.fh.controller.system.memberbalance;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.entity.system.User;
import com.fh.service.system.memberbalance.EnterPrisePlayMoneyManager;
import com.fh.service.system.memberbalance.MemberBalanceManager;
import com.fh.service.system.states.impl.StatesService;
import com.fh.service.system.types.impl.TypesService;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Tools;

/** 
 * 说明：MemberBalanceController
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-17
 */
@Controller
@RequestMapping(value="/memberbalance")
public class MemberBalanceController extends BaseController {
	
	String menuUrl = "memberbalance/list.do"; //菜单地址(权限用)
	@Resource(name="memberbalanceService")
	private MemberBalanceManager memberbalanceService;
	
	@Resource(name="typesService")
	private TypesService typesService;
	
	@Resource(name="statesService")
	private StatesService statesService;
	
	@Resource(name="enterPrisePlayMoneyService")
	private EnterPrisePlayMoneyManager enterPrisePlayMoneyService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增MemberBalance");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MEMBERBALANCE_ID", this.get32UUID());	//主键
		memberbalanceService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除MemberBalance");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		memberbalanceService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改MemberBalance");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
//		pd.put("BALANCENUMBER", Tools.createNumber());
		pd.put("EDITDATE", new Date());
		User user = (User)Jurisdiction.getSession().getAttribute(Const.SESSION_USER);
		pd.put("OPERATIONNAME", user.getUSERNAME());
		PageData memPd = memberbalanceService.findById(pd);
		
		//memberbalanceService.edit(pd);
		enterPrisePlayMoneyService.EditAndPlayMoney(memPd, pd,getRequest().getServletContext().getRealPath("/"));//打款和保存
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 
	 */
	@RequestMapping(value="/getNumber")
	@ResponseBody
	public JSONObject getNumber(){
		JSONObject obj = new JSONObject();
		obj.put("code", Tools.createNumber());
		obj.put("msg", "success");
		return obj;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表MemberBalance");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		String keytype = pd.getString("keytype");				//检索条件
		if(!Tools.isEmpty(keytype)){
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
				pd.put("keytype", keytype.trim());
			}
		}
		String  lastStart = pd.getString("lastStart");
		if(!Tools.isEmpty(lastStart)){
			pd.put("lastStart", lastStart+" 00:00:00");
		}
		String lastEnd = pd.getString("lastEnd");
		if(!Tools.isEmpty(lastEnd)){
			pd.put("lastEnd", lastEnd+" 23:59:59");
		}
//		String memberType = pd.getString("memberType");
//		pd.put("memberType", memberType);
		
		String type = pd.getString("type");
		String status = pd.getString("status");
		if(!Tools.isEmpty(type)){
			pd.put("type", type.trim());
		}
		if(!Tools.isEmpty(status)){
			pd.put("status", status.trim());
		}
		
		page.setPd(pd);
		List<PageData>	varList = memberbalanceService.list(page);	//列出MemberBalance列表
		
		List<PageData> reservationTypeList = new ArrayList<PageData>();
		List<PageData> reservationStateIDList = new ArrayList<PageData>();
		pd.put("typeClass", "6");
		reservationTypeList = typesService.listAll(pd);
		mv.addObject("reservationTypeList", reservationTypeList);
		pd.put("stateType", "6");
		reservationStateIDList = statesService.listAll(pd);
		mv.addObject("reservationStateIDList", reservationStateIDList);
		
		mv.setViewName("system/memberbalance/memberbalance_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/memberbalance/memberbalance_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = memberbalanceService.findById(pd);	//根据ID读取
		mv.setViewName("system/memberbalance/memberbalance_edit");
		
		List<PageData> reservationTypeList = new ArrayList<PageData>();
		List<PageData> reservationStateIDList = new ArrayList<PageData>();
		pd.put("typeClass", "6");
		reservationTypeList = typesService.listAll(pd);
		mv.addObject("reservationTypeList", reservationTypeList);
		pd.put("stateType", "6");
		reservationStateIDList = statesService.listAll(pd);
		mv.addObject("reservationStateIDList", reservationStateIDList);
		mv.addObject("reservationTypeList", reservationTypeList);
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除MemberBalance");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			memberbalanceService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出MemberBalance到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("id");	//1
		titles.add("结算类型");	//2
		titles.add("结算编号");	//3
		titles.add("用户id");	//4
		titles.add("结算金额");	//5
		titles.add("结算状态");	//6
		titles.add("结算时间");	//7
		titles.add("创建时间");	//8
		titles.add("结算备注");	//9
		titles.add("操作人名称");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = memberbalanceService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("BALANCEID"));	    //1
			vpd.put("var2", varOList.get(i).get("BALANCETYPE").toString());	//2
			vpd.put("var3", varOList.get(i).getString("BALANCENUMBER"));	    //3
			vpd.put("var4", varOList.get(i).getString("MEMBERID"));	    //4
			vpd.put("var5", varOList.get(i).get("BALANCEMONEY").toString());	//5
			vpd.put("var6", varOList.get(i).get("BALANCESTATE").toString());	//6
			vpd.put("var7", varOList.get(i).getString("EDITDATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("CREATEDATE"));	    //8
			vpd.put("var9", varOList.get(i).getString("BALANCEREMARK"));	    //9
			vpd.put("var10", varOList.get(i).getString("OPERATIONNAME"));	    //10
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
