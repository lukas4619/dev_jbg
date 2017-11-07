package com.fh.controller.system.couponsdetail;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.fh.util.DateUtil;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.util.TwoDimensionCode;
import com.fh.service.system.coupons.CouponsManager;
import com.fh.service.system.couponsdetail.CouponsDetailManager;
import com.fh.service.system.states.StatesManager;

/** 
 * 说明：优惠券
 * 创建人：Lukas 18923798379
 * 创建时间：2016-09-01
 */
@Controller
@RequestMapping(value="/couponsdetail")
public class CouponsDetailController extends BaseController {
	
	String menuUrl = "couponsdetail/list.do"; //菜单地址(权限用)
	@Resource(name="couponsdetailService")
	private CouponsDetailManager couponsdetailService;
	@Resource(name = "statesService")
	private StatesManager statesService;
	@Resource(name="couponsService")
	private CouponsManager couponsService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增CouponsDetail");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		List<PageData> lp = new ArrayList<PageData>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String COUPONSID ="";
		if(!Tools.isEmpty(pd.get("COUPONSID")+"")){
			COUPONSID = pd.getString("COUPONSID");
		}
		double DENOMINATION =0.00;
		if(!Tools.isEmpty(pd.get("DENOMINATION")+"")){
			DENOMINATION =Convert.strToDouble(pd.get("DENOMINATION")+"", DENOMINATION);
		}
		int createNum = Convert.strToInt(pd.get("CREATENUM")+"", 1);
		pd = new PageData();
		pd.put("COUPONSID", COUPONSID);
		pd = couponsService.findById(pd);
		if(pd!=null){
			for (int i = 0; i < createNum; i++) {
				PageData p = new PageData();
				String NUMBERS=Tools.createRandomNum12();
				p.put("COUPONSID", COUPONSID);
				p.put("NUMBERS",NUMBERS);
				int VALIDITY =0;
				Date ENDDATE = new Date();
				if(!Tools.isEmpty(pd.get("VALIDITY")+"")){
					VALIDITY =Convert.strToInt(pd.get("VALIDITY")+"", VALIDITY);
					 Calendar   calendar   =   new   GregorianCalendar(); 
				     calendar.setTime(ENDDATE); 
				     calendar.add(calendar.DATE,VALIDITY);//把日期往后增加VALIDITY天.整数往后推,负数往前移动 
				     ENDDATE=calendar.getTime();   //这个时间就是日期往后推VALIDITY天的结果 
				}
				p.put("DENOMINATION", DENOMINATION);
				String imgName=DateUtil.getDays()+"/"+System.currentTimeMillis()+".png";
				String QRCODE="uploadFiles/twoDimensionCode/"+imgName;
				String imgPath = getRequest().getServletContext().getRealPath("/")+"uploadFiles/twoDimensionCode/"+imgName;
				TwoDimensionCode handler = new TwoDimensionCode();
				handler.encoderQRCode(NUMBERS, imgPath, "png");
				p.put("QRCODE", QRCODE);
				p.put("STATEID", 24);
				p.put("CREATEDATE", new Date());
				p.put("ENDDATE", ENDDATE);
				lp.add(p);
			}
			couponsdetailService.batchSave(lp);
			pd.put("COUPONSID", COUPONSID);
			int COUPONSTOTALNUM = 0;
			if(Tools.notEmpty(pd.get("COUPONSTOTALNUM")+"")){
				COUPONSTOTALNUM =Convert.strToInt(pd.get("COUPONSTOTALNUM")+"", COUPONSTOTALNUM)+createNum;
			}
			pd.put("COUPONSTOTALNUM", COUPONSTOTALNUM);
			pd.put("EDITDATE", new Date());
			pd.put("ADMINID", Jurisdiction.getUsername());
			couponsService.editByCouponsTotalNum(pd);
		}
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
		logBefore(logger, Jurisdiction.getUsername()+"删除CouponsDetail");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		couponsdetailService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改CouponsDetail");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		couponsdetailService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表CouponsDetail");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if(Tools.notEmpty(pd.getString("couponsId"))){
			pd.put("couponsId", pd.getString("couponsId"));
		}
		if(Tools.notEmpty(pd.getString("keywords"))){
			pd.put("keywords", pd.getString("keywords"));
		}
		if(Tools.notEmpty(pd.get("lastStart")+"")){
			pd.put("lastStart",pd.get("lastStart")+"");
		}
		if(Tools.notEmpty(pd.get("lastEnd")+"")){
			pd.put("lastEnd",pd.get("lastEnd")+"");
		}
		pd.put("stateId", Convert.strToInt(pd.get("stateId")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = couponsdetailService.list(page);	//列出CouponsDetail列表
		List<PageData> stateList = new ArrayList<PageData>();
		pd.put("stateType", "8");
		stateList = statesService.listAll(pd);
		mv.addObject("stateList", stateList);
		mv.setViewName("system/couponsdetail/couponsdetail_list");
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
		mv.setViewName("system/couponsdetail/couponsdetail_edit");
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
		pd = couponsdetailService.findById(pd);	//根据ID读取
		mv.setViewName("system/couponsdetail/couponsdetail_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除CouponsDetail");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			couponsdetailService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出CouponsDetail到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("主键");	//1
		titles.add("优惠券主表标识");	//2
		titles.add("优惠券码");	//3
		titles.add("面值");	//4
		titles.add("二维码存储路径");	//5
		titles.add("会员标识");	//6
		titles.add("状态");	//7
		titles.add("创建时间");	//8
		titles.add("编辑时间");	//9
		titles.add("备注10");	//10
		titles.add("有效截止日期");	//11
		titles.add("使用日期");	//12
		titles.add("使用备注");	//13
		titles.add("操作备注");	//14
		dataMap.put("titles", titles);
		List<PageData> varOList = couponsdetailService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString());	//1
			vpd.put("var2", varOList.get(i).getString("COUPONSID"));	    //2
			vpd.put("var3", varOList.get(i).getString("NUMBERS"));	    //3
			vpd.put("var4", varOList.get(i).get("DENOMINATION").toString());	//4
			vpd.put("var5", varOList.get(i).getString("QRCODE"));	    //5
			vpd.put("var6", varOList.get(i).getString("MEMBERID"));	    //6
			vpd.put("var7", varOList.get(i).get("STATEID").toString());	//7
			vpd.put("var8", varOList.get(i).getString("CREATEDATE"));	    //8
			vpd.put("var9", varOList.get(i).getString("EDITDATE"));	    //9
			vpd.put("var10", varOList.get(i).getString("ACQUIREDATE"));	    //10
			vpd.put("var11", varOList.get(i).getString("ENDDATE"));	    //11
			vpd.put("var12", varOList.get(i).getString("USEDATE"));	    //12
			vpd.put("var13", varOList.get(i).getString("USERREMARK"));	    //13
			vpd.put("var14", varOList.get(i).getString("REMARKS"));	    //14
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
