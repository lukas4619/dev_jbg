package com.fh.controller.system.responselog;

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
import com.fh.service.system.article.ArticleManager;
import com.fh.service.system.responselog.ResponseLogManager;
import com.fh.service.system.types.TypesManager;

/** 
 * 说明：微信消息回复
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-23
 */
@Controller
@RequestMapping(value="/responselog")
public class ResponseLogController extends BaseController {
	
	String menuUrl = "responselog/list.do"; //菜单地址(权限用)
	@Resource(name="responselogService")
	private ResponseLogManager responselogService;
	@Resource(name = "typesService")
	private TypesManager typesService;
	@Resource(name="articleService")
	private ArticleManager articleService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增ResponseLog");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CREATEDATE", new Date());	//主键
		pd.put("ADMINID",Jurisdiction.getUsername());	
		responselogService.save(pd);
		mv.addObject("msg","新增成功");
		mv.addObject("url","responselog/list.do");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**修改状态
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/editByIsValId")
	public void editByIsValId(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改状态ResponseLog");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ADMINID",Jurisdiction.getUsername());	
		responselogService.editByIsValId(pd);
		out.write("success");
		out.close();
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除ResponseLog");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		responselogService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改ResponseLog");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EDITDATE", new Date());	//主键
		pd.put("ADMINID",Jurisdiction.getUsername());	
		responselogService.edit(pd);
		mv.addObject("msg","编辑成功");
		mv.addObject("url","responselog/list.do");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表ResponseLog");
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
		pd.put("responseType", Convert.strToInt(pd.get("responseType")+"", -1));
		pd.put("sendType", Convert.strToInt(pd.get("sendType")+"", -1));
		page.setPd(pd);
		List<PageData> typeList = new ArrayList<PageData>();
		pd.put("typeClass", "8");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		List<PageData>	varList = responselogService.list(page);	//列出ResponseLog列表
		mv.setViewName("system/responselog/responselog_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	
	/**
	 * 加载软文列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/findArticleList")
	public ModelAndView findArticleList(Page page)throws Exception{
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
		pd.put("articleTypeID", Convert.strToInt(pd.get("articleTypeID")+"", -1));
		pd.put("articleStateID", Convert.strToInt(pd.get("19")+"", 19));
		page.setShowCount(5);
		page.setPd(pd);
		List<PageData>	varList = articleService.list(page);	//列出Article列表
		List<PageData> typeList = new ArrayList<PageData>();
		pd.put("typeClass", "4");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		mv.addObject("varList", varList);
		mv.setViewName("system/responselog/article_list");
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
		pd.put("typeClass", "8");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		mv.setViewName("system/responselog/responselog_edit");
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
		pd = responselogService.findById(pd);	//根据ID读取
		List<PageData> typeList = new ArrayList<PageData>();
		pd.put("typeClass", "8");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		mv.setViewName("system/responselog/responselog_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除ResponseLog");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			responselogService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出ResponseLog到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("主键自增");	//1
		titles.add("接收消息类型");	//2
		titles.add("发送消息类型");	//3
		titles.add("关键字");	//4
		titles.add("内容");	//5
		titles.add("是否有效");	//6
		titles.add("创建时间");	//7
		titles.add("编辑时间");	//8
		titles.add("操作人");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = responselogService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString());	//1
			vpd.put("var2", varOList.get(i).get("RESPONSETYPE").toString());	//2
			vpd.put("var3", varOList.get(i).get("SENDTYPE").toString());	//3
			vpd.put("var4", varOList.get(i).getString("KEYWORDS"));	    //4
			vpd.put("var5", varOList.get(i).getString("CONTENT"));	    //5
			vpd.put("var6", varOList.get(i).get("ISVALID").toString());	//6
			vpd.put("var7", varOList.get(i).getString("CREATEDATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("EDITDATE"));	    //8
			vpd.put("var9", varOList.get(i).getString("ADMINID"));	    //9
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
