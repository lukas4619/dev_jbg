package com.fh.controller.system.coupons;

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
import com.fh.util.Constants;
import com.fh.util.Convert;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.TemplateMessage;
import com.fh.wechat.gongzhong.vo.message.template.Template;
import com.fh.service.system.coupons.CouponsManager;
import com.fh.service.system.couponsdetail.CouponsDetailManager;
import com.fh.service.system.states.StatesManager;
import com.fh.service.system.types.TypesManager;

/** 
 * 说明：优惠券主表
 * 创建人：Lukas 18923798379
 * 创建时间：2016-09-01
 */
@Controller
@RequestMapping(value="/coupons")
public class CouponsController extends BaseController {
	
	String menuUrl = "coupons/list.do"; //菜单地址(权限用)
	@Resource(name="couponsService")
	private CouponsManager couponsService;
	@Resource(name="couponsdetailService")
	private CouponsDetailManager couponsdetailService;
	@Resource(name = "statesService")
	private StatesManager statesService;
	@Resource(name = "typesService")
	private TypesManager typesService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Coupons");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("COUPONSID", this.get32UUID());	//主键
		pd.put("CREATEDATE",new Date());	
		pd.put("EDITDATE",new Date());	
		pd.put("COUPONSTOTALNUM",0);	
		pd.put("COUPONSUSENUM",0);	
		pd.put("ADMINID",Jurisdiction.getUsername());	
		couponsService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Coupons");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		couponsService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Coupons");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EDITDATE",new Date());	
		pd.put("ADMINID",Jurisdiction.getUsername());	
		couponsService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Coupons");
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
		pd.put("couponsType", Convert.strToInt(pd.get("couponsType")+"", -1));
		pd.put("couponsState", Convert.strToInt(pd.get("couponsState")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = couponsService.list(page);	//列出Coupons列表
		List<PageData> typeList = new ArrayList<PageData>();
		List<PageData> stateList = new ArrayList<PageData>();
		pd.put("typeClass", "9");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		pd.put("stateType", "7");
		stateList = statesService.listAll(pd);
		mv.addObject("stateList", stateList);
		mv.setViewName("system/coupons/coupons_list");
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
		List<PageData> typeList = new ArrayList<PageData>();
		List<PageData> stateList = new ArrayList<PageData>();
		pd.put("typeClass", "9");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		pd.put("stateType", "7");
		stateList = statesService.listAll(pd);
		mv.addObject("stateList", stateList);
		mv.setViewName("system/coupons/coupons_edit");
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
		pd = couponsService.findById(pd);	//根据ID读取
		List<PageData> typeList = new ArrayList<PageData>();
		List<PageData> stateList = new ArrayList<PageData>();
		pd.put("typeClass", "9");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		pd.put("stateType", "7");
		stateList = statesService.listAll(pd);
		mv.addObject("stateList", stateList);
		mv.setViewName("system/coupons/coupons_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Coupons");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			couponsService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	
	
	/**优惠券使用
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/couponsUse")
	public ModelAndView couponsUse()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		int isUse =-1;
		String NUMBERS = pd.get("numbers")+"";
		pd=couponsdetailService.findByViewId(pd);
		if(pd!=null){
			if(pd.getInt("stateId")==25){
				isUse=1;
				mv.addObject("msg", "当前优惠券已经使用，无法再次使用！");
			}else{
				PageData p = new PageData();//优惠券主表回写数据
				int COUPONSUSENUM =0;
				if(Tools.notEmpty(pd.get("couponsUseNum")+"")){
					COUPONSUSENUM = Convert.strToInt(pd.get("couponsUseNum")+"", COUPONSUSENUM);
				}
				p.put("COUPONSUSENUM", COUPONSUSENUM+1);
				p.put("EDITDATE", new Date());
				p.put("ADMINID", Jurisdiction.getUsername());
				p.put("COUPONSID", pd.get("couponsId")+"");
				PageData pdd = new PageData();//优惠券详表使用数据
				pdd.put("STATEID", 25);
				pdd.put("USEDATE", new Date());
				pdd.put("USERREMARK", Jurisdiction.getUsername()+"操作");
				pdd.put("ID", pd.get("id")+"");
				couponsService.editByCouponsUse(p, pdd);
				String openId="";
				String weChatName="";
				if(Tools.notEmpty(pd.get("openId")+"")){
					openId = pd.getString("openId");
				}
				if(Tools.notEmpty(pd.get("weChatName")+"")){
					weChatName = pd.getString("weChatName");
				}
				if(!getRequertUrl().contains("localhost") && !getRequertUrl().contains("ddj.java.1yg.tv")){
					if(Tools.notEmpty(openId) && Tools.notEmpty(weChatName)){
						List<Template> templates = new ArrayList<Template>();
						templates = TemplateMessage.setTemplate(templates, "#173177", "first",weChatName+",您的优惠券已成功使用。");
						templates = TemplateMessage.setTemplate(templates, "#173177", "keyword1",NUMBERS);
						templates = TemplateMessage.setTemplate(templates, "#173177", "keyword2",pd.get("denomination")+"元");
						templates = TemplateMessage.setTemplate(templates, "#173177", "keyword3",Convert.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00"));
						templates = TemplateMessage.setTemplate(templates, "#173177", "remark","感谢您的使用，点击可查看优惠券详情！");
						String url =getRequertUrl()+"myCoupon/goDetails?numbers="+NUMBERS;
						TemplateMessage.sendTemplateMessage(openId, Constants.PRODUCT_COUPON_TEMPLATE_ID, url, Constants.PRODUCT_RESERVE_TOPCOLOR, templates);
					}
				}
				
			}
			
		}
		pd=couponsdetailService.findByViewId(pd);
		mv.setViewName("system/coupons/coupons_use");
		mv.addObject("pd", pd);
		mv.addObject("isUse", isUse);
		return mv;
	}	
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出Coupons到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("主键标识");	//1
		titles.add("优惠券类型");	//2
		titles.add("优惠券名称");	//3
		titles.add("优惠券发布总数");	//4
		titles.add("发布优惠券已使用总数");	//5
		titles.add("有效期");	//6
		titles.add("创建时间");	//7
		titles.add("编辑时间");	//8
		titles.add("操作人");	//9
		titles.add("状态");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = couponsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("COUPONSID"));	    //1
			vpd.put("var2", varOList.get(i).get("COUPONSTYPE").toString());	//2
			vpd.put("var3", varOList.get(i).getString("COUPONSNAME"));	    //3
			vpd.put("var4", varOList.get(i).get("COUPONSTOTALNUM").toString());	//4
			vpd.put("var5", varOList.get(i).get("COUPONSUSENUM").toString());	//5
			vpd.put("var6", varOList.get(i).get("VALIDITY").toString());	//6
			vpd.put("var7", varOList.get(i).getString("CREATEDATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("EDITDATE"));	    //8
			vpd.put("var9", varOList.get(i).getString("ADMINID"));	    //9
			vpd.put("var10", varOList.get(i).get("COUPONSSTATE").toString());	//10
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
