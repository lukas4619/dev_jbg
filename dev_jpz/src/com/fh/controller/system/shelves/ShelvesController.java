package com.fh.controller.system.shelves;

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
import com.fh.service.system.cproperty.CpropertyManager;
import com.fh.service.system.shelves.ShelvesManager;
import com.fh.service.system.store.StoreManager;
import com.fh.util.AppUtil;
import com.fh.util.Convert;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Tools;

/** 
 * 说明：货架
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-19
 */
@Controller
@RequestMapping(value="/shelves")
public class ShelvesController extends BaseController {
	
	String menuUrl = "shelves/list.do"; //菜单地址(权限用)
	@Resource(name="shelvesService")
	private ShelvesManager shelvesService;
	@Resource(name="cpropertyService")
	private CpropertyManager cpropertyService;
	@Resource(name="storeService")
	private StoreManager storeService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Shelves");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CREATEDATE", new Date());
	    pd.put("LASTDATE", new Date());
		shelvesService.save(pd);
		mv.addObject("msg","新增成功");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Shelves");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		shelvesService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Shelves");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("LASTDATE", new Date());
		shelvesService.edit(pd);
		mv.addObject("msg","编辑成功");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Shelves");
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
		pd.put("storeId", Convert.strToInt(pd.get("storeId")+"", -1));
		pd.put("pluCode", Convert.strToInt(pd.get("pluCode")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = shelvesService.list(page);	//列出Shelves列表
		mv.setViewName("system/shelves/shelves_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		List<PageData> shelvesTypeList = new ArrayList<PageData>();
		pd=new PageData();
		pd.put("CID", "7".split(","));
		shelvesTypeList = cpropertyService.findAllByCId(pd);
		mv.addObject("shelvesTypeList", shelvesTypeList);
		List<PageData> storeList = new ArrayList<PageData>();
		storeList = storeService.listAll(pd);
		mv.addObject("storeList", storeList);
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
		mv.setViewName("system/shelves/shelves_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		List<PageData> shelvesTypeList = new ArrayList<PageData>();
		pd=new PageData();
		pd.put("CID", "7".split(","));
		shelvesTypeList = cpropertyService.findAllByCId(pd);
		mv.addObject("shelvesTypeList", shelvesTypeList);
		List<PageData> storeList = new ArrayList<PageData>();
		storeList = storeService.listAll(pd);
		mv.addObject("storeList", storeList);
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
		pd = shelvesService.findById(pd);	//根据ID读取
		mv.setViewName("system/shelves/shelves_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		List<PageData> shelvesTypeList = new ArrayList<PageData>();
		pd=new PageData();
		pd.put("CID", "7".split(","));
		shelvesTypeList = cpropertyService.findAllByCId(pd);
		mv.addObject("shelvesTypeList", shelvesTypeList);
		List<PageData> storeList = new ArrayList<PageData>();
		storeList = storeService.listAll(pd);
		mv.addObject("storeList", storeList);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Shelves");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			shelvesService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Shelves到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("标识");	//1
		titles.add("货架名称");	//2
		titles.add("货位数量");	//3
		titles.add("所属门店");	//4
		titles.add("货架类型");	//5
		titles.add("创建时间");	//6
		titles.add("最后编辑时间");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = shelvesService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString());	//1
			vpd.put("var2", varOList.get(i).getString("SHELVESNAME"));	    //2
			vpd.put("var3", varOList.get(i).get("RANKCOUNT").toString());	//3
			vpd.put("var4", varOList.get(i).get("STOREID").toString());	//4
			vpd.put("var5", varOList.get(i).get("SHELVESTYPEID").toString());	//5
			vpd.put("var6", varOList.get(i).getString("CREATEDATE"));	    //6
			vpd.put("var7", varOList.get(i).getString("LASTDATE"));	    //7
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
