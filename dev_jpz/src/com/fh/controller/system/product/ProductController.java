package com.fh.controller.system.product;

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
import com.fh.service.system.product.ProductManager;
import com.fh.service.system.states.StatesManager;
import com.fh.service.system.types.TypesManager;

/**
 * 说明：产品模块 创建人：Lukas 18923798379 创建时间：2016-08-17
 */
@Controller
@RequestMapping(value = "/product")
public class ProductController extends BaseController {

	String menuUrl = "product/list.do"; // 菜单地址(权限用)
	@Resource(name = "productService")
	private ProductManager productService;
	@Resource(name = "statesService")
	private StatesManager statesService;
	@Resource(name = "typesService")
	private TypesManager typesService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "新增Product");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
			return null;
		} // 校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String PRODUCTID =this.get32UUID();
		pd.put("PRODUCTID", PRODUCTID); // 主键
		pd.put("CREATEDATE", new Date());
		pd.put("ADMINID", Jurisdiction.getUsername());
		pd.put("PROCONTENT", pd.get("editorValue")+"");
		pd.put("PROURL","frontProduct/goDetails.do?id="+PRODUCTID);
		pd.put("EDITDATE", new Date());
		productService.save(pd);
		mv.addObject("msg","新增成功");
		mv.addObject("url","product/list.do");
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	public void delete(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除Product");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
			return;
		} // 校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		productService.delete(pd);
		out.write("success");
		out.close();
	}
	
	
	/**
	 * 修改状态
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/editByState")
	public void editByState(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "修改状态Product");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			return;
		} // 校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ADMINID", Jurisdiction.getUsername());
		productService.editByState(pd);
		out.write("success");
		out.close();
	}

	/**
	 * 修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView edit() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "修改Product");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			return null;
		} // 校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EDITDATE", new Date());
		pd.put("ADMINID", Jurisdiction.getUsername());
		pd.put("PROCONTENT", pd.get("editorValue")+"");
		productService.edit(pd);
		mv.addObject("msg","编辑成功");
		mv.addObject("url","product/list.do");
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Page page) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "列表Product");
		// if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		// //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
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
		pd.put("CID", Convert.strToInt(pd.get("CID")+"", -1));
		page.setPd(pd);
		List<PageData> varList = productService.list(page); // 列出Product列表
		List<PageData> proTypeList = new ArrayList<PageData>();
		List<PageData> proStateList = new ArrayList<PageData>();
		pd.put("typeClass", "2");
		proTypeList = typesService.listAll(pd);
		mv.addObject("proTypeList", proTypeList);
		pd.put("stateType", "2");
		proStateList = statesService.listAll(pd);
		mv.addObject("proStateList", proStateList);
		mv.setViewName("system/product/product_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX", Jurisdiction.getHC()); // 按钮权限
		return mv;
	}

	/**
	 * 去新增页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAdd")
	public ModelAndView goAdd() throws Exception {
		List<PageData> proTypeList = new ArrayList<PageData>();
		List<PageData> proStateList = new ArrayList<PageData>();
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/product/product_edit");
//		pd.put("typeClass", "2");
//		proTypeList = typesService.listAll(pd);
//		mv.addObject("proTypeList", proTypeList);
//		pd.put("stateType", "2");
//		proStateList = statesService.listAll(pd);
//		mv.addObject("proStateList", proStateList);
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 去修改页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	public ModelAndView goEdit() throws Exception {
		List<PageData> proTypeList = new ArrayList<PageData>();
		List<PageData> proStateList = new ArrayList<PageData>();
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = productService.findById(pd); // 根据ID读取
		pd.put("typeClass", "2");
		proTypeList = typesService.listAll(pd);
		mv.addObject("proTypeList", proTypeList);
		pd.put("stateType", "2");
		proStateList = statesService.listAll(pd);
		mv.addObject("proStateList", proStateList);
		mv.setViewName("system/product/product_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 批量删除
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "批量删除Product");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
			return null;
		} // 校验权限
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (null != DATA_IDS && !"".equals(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			productService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		} else {
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 导出到excel
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel")
	public ModelAndView exportExcel() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "导出Product到excel");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
			return null;
		}
		ModelAndView mv = new ModelAndView();
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
		pd.put("proState", Convert.strToInt(pd.get("proState")+"", -1));
		pd.put("proType", Convert.strToInt(pd.get("proType")+"", -1));
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("产品类型"); // 1
		titles.add("产品名称"); // 2
		titles.add("产品金额"); // 3
		titles.add("产品预付金额"); // 4
		titles.add("产品标题"); // 5
		titles.add("产品描述"); // 6
		titles.add("产品预订次数"); // 7
		titles.add("产品成功消费次数"); // 8
		titles.add("产品创建时间"); // 9
		titles.add("产品编辑时间"); // 10
		titles.add("产品有效期"); // 11
		titles.add("产品状态"); // 12
		titles.add("操作人"); // 13
		dataMap.put("titles", titles);
		List<PageData> varOList = productService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("typeName").toString()); // 1
			vpd.put("var2", varOList.get(i).get("proName").toString()); // 2
			vpd.put("var3", varOList.get(i).get("proMoney").toString()); // 3
			vpd.put("var4", varOList.get(i).get("proAdvanceMoney").toString()); // 4
			vpd.put("var5", varOList.get(i).get("proTitle").toString()); // 5
			vpd.put("var6", varOList.get(i).get("proDescription").toString()); // 6
			vpd.put("var7", varOList.get(i).get("proReservationNum").toString()); // 7
			vpd.put("var8", varOList.get(i).get("proconsumeNum").toString()); // 8+
			if(varOList.get(i).get("createDate")!=null){
				vpd.put("var9", varOList.get(i).get("createDate").toString()); // 9
			}else{
				vpd.put("var9", ""); // 9
			}
			if(varOList.get(i).get("editDate")!=null){
				vpd.put("var10", varOList.get(i).get("editDate").toString()); // 10
			}else{
				vpd.put("var10", ""); // 10
			}
			vpd.put("var11", varOList.get(i).get("proValidity").toString()); // 11
			vpd.put("var12", varOList.get(i).get("stateName").toString()); // 12
			vpd.put("var13", varOList.get(i).get("adminId").toString()); // 13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}
}
