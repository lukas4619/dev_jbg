package com.fh.controller.system.lottery;

import java.io.PrintWriter;
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
import com.fh.util.AppUtil;
import com.fh.util.Convert;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.service.system.lottery.LotteryManager;
import com.fh.service.system.states.StatesManager;

/** 
 * 说明：抽奖模块
 * 创建人：Lukas 18923798379
 * 创建时间：2016-09-09
 */
@Controller
@RequestMapping(value="/lottery")
public class LotteryController extends BaseController {
	
	String menuUrl = "lottery/list.do"; //菜单地址(权限用)
	@Resource(name="lotteryService")
	private LotteryManager lotteryService;
	@Resource(name = "statesService")
	private StatesManager statesService;
	
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Lottery");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		lotteryService.delete(pd);
		out.write("success");
		out.close();
	}
	
	
	/**使用
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public void edit(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Lottery");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("USEDATE", new Date());
		pd.put("LOTTERYSTATE", 48);
		lotteryService.edit(pd);
		out.write("success");
		out.close();
	}
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Lottery");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if(Tools.notEmpty(pd.getString("keywords"))){
			pd.put("keywords", pd.getString("keywords"));
		}
		if(!Tools.isEmpty(pd.get("lastStart")+"")){
			pd.put("lastStart",pd.get("lastStart")+"");
		}
		if(!Tools.isEmpty(pd.get("lastEnd")+"")){
			pd.put("lastEnd",pd.get("lastEnd")+"");
		}
		pd.put("lotteryState", Convert.strToInt(pd.get("lotteryState")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = lotteryService.list(page);	//列出Lottery列表
		List<PageData> stateList = new ArrayList<PageData>();
		pd.put("stateType", "11");
		stateList = statesService.listAll(pd);
		mv.addObject("stateList", stateList);
		mv.setViewName("system/lottery/lottery_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Lottery");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			lotteryService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Lottery到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("会员昵称");	//1
		titles.add("手机号码");	//2
		titles.add("抽奖时间");	//3
		titles.add("中间内容");	//4
		titles.add("使用时间");	//5
		titles.add("状态");	//6
		dataMap.put("titles", titles);
		List<PageData> varOList = lotteryService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("weChatName")+"");	//1
			vpd.put("var2", varOList.get(i).get("phoneNumber")+"");	    //2
			vpd.put("var3", varOList.get(i).getString("lotteryDate"));	    //3
			vpd.put("var4", varOList.get(i).getString("lotteryContent"));	    //4
			vpd.put("var5", varOList.get(i).getString("useDate"));	    //5
			vpd.put("var6", varOList.get(i).get("stateName").toString());	//6
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
