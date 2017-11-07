package com.fh.controller.system.replenish;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Convert;
import com.fh.util.FileDownload;
import com.fh.util.FileUpload;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.PathUtil;
import com.fh.util.StringUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelRead;
import com.fh.util.Tools;
import com.fh.service.system.inventory.InventoryManager;
import com.fh.service.system.replenish.ReplenishManager;
import com.fh.service.system.skuplu.SkupluManager;
import com.fh.service.system.store.StoreManager;

/** 
 * 说明：补货记录
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-21
 */
@Controller
@RequestMapping(value="/replenish")
public class ReplenishController extends BaseController {
	
	String menuUrl = "replenish/list.do"; //菜单地址(权限用)
	@Resource(name="replenishService")
	private ReplenishManager replenishService;
	@Resource(name="inventoryService")
	private InventoryManager inventoryService;
	@Resource(name="skupluService")
	private SkupluManager skupluService;
	@Resource(name="storeService")
	private StoreManager storeService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Replenish");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//pd.put("REPLENISH_ID", this.get32UUID());	//主键
		replenishService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Replenish");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		replenishService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Replenish");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		replenishService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Replenish");
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
		page.setPd(pd);
		List<PageData>	varList = replenishService.list(page);	//列出Replenish列表
		mv.setViewName("system/replenish/replenish_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		List<PageData> storeList = new ArrayList<PageData>();//列出门店列表
		pd=new PageData();
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
		mv.setViewName("system/replenish/replenish_edit");
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
		pd = replenishService.findById(pd);	//根据ID读取
		mv.setViewName("system/replenish/replenish_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Replenish");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			replenishService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Replenish到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("所属门店");	//1
		titles.add("商品名称");	//2
		titles.add("商品编码");	//3
		titles.add("补货数量");	//4
		titles.add("补货时间");	//5
		titles.add("补货人");	//6
		dataMap.put("titles", titles);
		List<PageData> varOList = replenishService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("STORENAME").toString());	//1
			vpd.put("var2", varOList.get(i).get("PLUNAME").toString());	//2
			vpd.put("var3", varOList.get(i).get("PLUCODE").toString());	//3
			vpd.put("var4", varOList.get(i).get("REPLENISHCOUNT").toString());	//4
			vpd.put("var5",Convert.dateToStr(Convert.strToDate(varOList.get(i).get("CREATEDATE").toString(), new Date()),"yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00"));	    //5
			vpd.put("var6", varOList.get(i).getString("CREATENAME"));	    //6
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	
	/**打开上传EXCEL页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUploadExcel")
	public ModelAndView goUploadExcel()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd=this.getPageData();
		mv.setViewName("system/replenish/uploadexcel");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "replenish.xls", "replenish.xls");
	}
	
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	public ModelAndView readExcel(@RequestParam(value="excel",required=false) MultipartFile file,@RequestParam(value="STOREID",required=false) int STOREID) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd=this.getPageData();
		int s=0;
		int f=0;
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;}
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;								//文件上传路径
			String fileName =  FileUpload.fileUp(file, filePath, "userexcel");							//执行上传
			List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, fileName, 2, 0, 0);		//执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
			/*判断excel是否有数据*/
			if(listPd==null || listPd.size()<=0){
				mv.addObject("typeCode",1);
				mv.addObject("msg","无法读取EXCEL中的数据，请检查EXCEL模版或者EXCEL中是否有数据！");
				mv.setViewName("save_result");
				return mv;
			}
			s=listPd.size();
			listPd = new ArrayList<PageData>(new HashSet<PageData>(listPd));
			/*读取商品档案数据*/
			//查询条件
			pd.put("pluCode", -1);
			pd.put("pluClassId", -1);
			pd.put("pluStatus", -1);
			pd.put("pluTypeId", -1);
			pd.put("lastStart", "");
			pd.put("lastEnd", "");
			List<PageData> listPlu =skupluService.listAll(pd);
			if(StringUtil.isPdList(listPlu)){
				mv.addObject("typeCode",1);
				mv.addObject("msg","商品档案数据不存在，请先建立商品档案信息！");
				mv.setViewName("save_result");
				return mv;
			}
			/*获取当前门店已初始化商品档案数据*/
			pd=new PageData();
			pd.put("STOREID", STOREID);
			List<PageData> listStorePlu =inventoryService.listAll(pd);
			/*存入数据库操作======================================*/
			/**
			 * var0 :商品编号
			 */
			List<PageData> pdList =new ArrayList<PageData>();
			String pluInfo="商品档案已存在商品编码为：";//商品档案提示信息
			String storeInfo="当前门店未初始化商品编码为：";//门店商品提示信息
			for(int i=0;i<listPd.size();i++){
				pd = new PageData();
				int excPluCode = -1;//导入商品编码
				int excCount =0;//导入库存数量
				if(!StringUtil.isEmpty(listPd.get(i).getString("var0"))){
					excPluCode = Convert.strToInt(listPd.get(i).getString("var0"), excPluCode);//导入商品编码
					excCount=Convert.strToInt(listPd.get(i).getString("var1"), excCount);//导入库存数量
					//判断当前门店补货中的商品编码是否在档案信息中存在
					/*
					 * 如果存在商品信息档案中那么则继续遍历
					 * 如果不存在商品信息档案中那么则continue本次，并记录商品编码
					 */
					if(!StringUtil.isListHas(listPlu, "pluCode", excPluCode)){
						pluInfo+=excPluCode+",";//在商品档案中不存在该商品，
						mv.addObject("typeCode",1);
						mv.addObject("msg","商品编码为："+excPluCode+" 在商品档案数据不存在，请先建立商品档案信息！");
						mv.setViewName("save_result");
						return mv;
					}
					if(!StringUtil.isPdList(listStorePlu)){
						//判断当前门店补货记录中的商品编码在该门店是否初始化
						/**  前置条件为存在初始化商品记录
						 *   如果当前补货记录商品编码不存在该门店初始化中，那么则continue本次，并记录商品编码
						 *   如果当前补货记录商品编码存在该门店初始化中，那么则继续遍历
						 */
						if(!StringUtil.isListHas(listStorePlu, "PLUCODE", excPluCode)){
							storeInfo+=excPluCode+",";//该门店未初始化该商品
							mv.addObject("typeCode",1);
							mv.addObject("msg","商品编码为："+excPluCode+" 在该门店未初始化！");
							mv.setViewName("save_result");
							return mv;
						}
					}
					pd.put("STOREID", STOREID);
					pd.put("PLUCODE", excPluCode);
					pd.put("REPLENISHCOUNT", excCount);
					pd.put("CREATENAME", Jurisdiction.getUsername());
					pdList.add(pd);
					
				}else{
					mv.addObject("typeCode",1);
					mv.addObject("msg","无法读取EXCEL中的数据，请检查EXCEL模版或者EXCEL中是否有数据！");
					mv.setViewName("save_result");
					return mv;
				}
				
			}
			f=pdList.size();
			inventoryService.batchEdit(pdList);
			/*存入数据库操作======================================*/
			mv.addObject("typeCode",0);
			if(s==f){
				mv.addObject("msg"," 共： "+s+" 条记录；成功导入： "+f+" 条记录");
			}else{
				mv.addObject("msg"," 共："+s+" 条记录；成功导入： "+f+" 条记录,失败导入： "+(s-f)+" 条记录; "+pluInfo+" ;"+storeInfo+"");
			}
			
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
