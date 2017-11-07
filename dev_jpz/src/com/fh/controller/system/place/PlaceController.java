package com.fh.controller.system.place;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.cproperty.CpropertyManager;
import com.fh.service.system.place.PlaceManager;
import com.fh.service.system.placeplu.PlacepluManager;
import com.fh.service.system.shelves.ShelvesManager;
import com.fh.service.system.skuplu.SkupluManager;
import com.fh.service.system.store.StoreManager;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Convert;
import com.fh.util.FileDownload;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.PathUtil;
import com.fh.util.StringUtil;
import com.fh.util.Tools;

/** 
 * 说明：架位管理
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-21
 */
@Controller
@RequestMapping(value="/place")
public class PlaceController extends BaseController {
	
	String menuUrl = "place/list.do"; //菜单地址(权限用)
	@Resource(name="placeService")
	private PlaceManager placeService;
	@Resource(name="cpropertyService")
	private CpropertyManager cpropertyService;
	@Resource(name="storeService")
	private StoreManager storeService;
	@Resource(name="shelvesService")
	private ShelvesManager shelvesService;
	@Resource(name="skupluService")
	private SkupluManager skupluService;
	@Resource(name="placepluService")
	private PlacepluManager placepluService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Place");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CREATEDATE", new Date());
	    pd.put("LASTDATE", new Date());
		placeService.save(pd);
		mv.addObject("msg","新增成功");
		mv.addObject("url", menuUrl);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Place");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		placeService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Place");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
	    pd.put("LASTDATE", new Date());
		placeService.edit(pd);
		mv.addObject("msg","编辑成功");
		mv.addObject("url", menuUrl);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Place");
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
		pd.put("shelvesId", Convert.strToInt(pd.get("shelvesId")+"", -1));
		pd.put("placeTypeId", Convert.strToInt(pd.get("placeTypeId")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = placeService.list(page);	//列出Place列表
		mv.setViewName("system/place/place_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		pd=new PageData();
		pd.put("CID", "8".split(","));
		List<PageData>	placeTypeList =  new ArrayList<PageData>();	//列出架位状态列表
		placeTypeList = cpropertyService.findAllByCId(pd);
		mv.addObject("placeTypeList", placeTypeList);
		List<PageData> storeList = new ArrayList<PageData>();//列出门店列表
		pd=new PageData();
		storeList = storeService.listAll(pd);
		mv.addObject("storeList", storeList);
		return mv;
	}
	
	/**显示商品列表(弹窗选择用)
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/pluList")
	public ModelAndView pluList(Page page)throws Exception{
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
		String pluCodes ="";
		PageData p = new PageData();
		p.put("storeId", pd.getInt("storeId"));
		p.put("shelvesId", pd.getInt("shelvesId"));
		p.put("placeId", pd.getInt("placeId"));
		List<PageData>	placePlu = placepluService.listAll(p);
		if(!StringUtil.isPdList(placePlu)){
			for (PageData pageData : placePlu) {
				pluCodes += pageData.get("PLUCODE")+",";
			}
			if(!StringUtil.isEmpty(pluCodes)){
				if(pluCodes.endsWith(",")){
					pluCodes = pluCodes.substring(0, pluCodes.length()-1);
				}
			}
		}
		pd.put("pluCodes", pluCodes);
		pd.put("pluCode", Convert.strToInt(pd.get("pluCode")+"", -1));
		pd.put("pluClassId", Convert.strToInt(pd.get("pluClassId")+"", -1));
		pd.put("pluTypeId", Convert.strToInt(pd.get("pluTypeId")+"", -1));
		pd.put("pluStatus", Convert.strToInt(pd.get("pluStatus")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = skupluService.list(page);	//列出Skuplu列表
		mv.setViewName("system/place/plu_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		List<PageData> varcList = new ArrayList<PageData>();
		pd=new PageData();
		pd.put("CID", "1,2,5".split(","));
		varcList = cpropertyService.findAllByCId(pd);
		if(varcList!=null && varcList.size()>0){
			List<PageData> pluClassList = new ArrayList<PageData>();//商品分类集合
			List<PageData> pluTypeList = new ArrayList<PageData>();//商品类型集合
			List<PageData> pluStatusList = new ArrayList<PageData>();//商品状态集合
			for (Iterator iterator = varcList.iterator(); iterator.hasNext();) {
				PageData pageData = (PageData) iterator.next();
				int CID =pageData.getInt("CID");
				if(CID==1){//商品类型
					pluTypeList.add(pageData);
				}else if(CID==2){//商品分类
					pluClassList.add(pageData);
				}else if(CID==5){//商品状态
					pluStatusList.add(pageData);
				}
				
			}
			mv.addObject("pluClassList", pluClassList);
			mv.addObject("pluTypeList", pluTypeList);
			mv.addObject("pluStatusList", pluStatusList);
		}
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
		mv.setViewName("system/place/place_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		pd=new PageData();
		pd.put("CID", "8".split(","));
		List<PageData>	placeTypeList = new ArrayList<PageData>();	//列出架位状态列表
		placeTypeList = cpropertyService.findAllByCId(pd);
		mv.addObject("placeTypeList", placeTypeList);
		List<PageData> storeList = new ArrayList<PageData>();//列出门店列表
		pd=new PageData();
		storeList = storeService.listAll(pd);
		mv.addObject("storeList", storeList);
//		List<PageData> shelvesList = new ArrayList<PageData>();//列出货架列表
//		pd=new PageData();
//		shelvesList = shelvesService.listAll(pd);
//		mv.addObject("shelvesList", shelvesList);
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
		pd = placeService.findById(pd);	//根据ID读取
		mv.setViewName("system/place/place_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		pd=new PageData();
		pd.put("CID", "8".split(","));
		List<PageData>	placeTypeList =  new ArrayList<PageData>();	//列出架位状态列表
		placeTypeList = cpropertyService.findAllByCId(pd);
		mv.addObject("placeTypeList", placeTypeList);
		List<PageData> storeList = new ArrayList<PageData>();//列出门店列表
		pd=new PageData();
		storeList = storeService.listAll(pd);
		mv.addObject("storeList", storeList);
//		List<PageData> shelvesList = new ArrayList<PageData>();//列出货架列表
//		pd=new PageData();
//		shelvesList = shelvesService.listAll(pd);
//		mv.addObject("shelvesList", shelvesList);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Place");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			placeService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	
	    /**检查货架数量
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/checkPlace")
		@ResponseBody
		public Object checkPlace() throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"检查货架数量");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
			PageData pd = new PageData();		
			Map<String,Object> map = new HashMap<String,Object>();
			pd = this.getPageData();//shelvesId:货架标识    rankCount：货架允许货位数量
			int rankCount = pd.getInt("rankCount");
			pd = placeService.findByShelvesId(pd);
			int useRankCount=Convert.strToInt(pd.get("NUM")+"", -1);
			if(useRankCount>=rankCount){
				map.put("msg", "当前货架可用货位不足。（当前货架最多允许使用货位为："+rankCount+",已使用："+useRankCount+"）");
				map.put("type", "1");
			}else{
				map.put("msg", "ok");
				map.put("type", "0");
			}
			return AppUtil.returnObject(pd, map);
		}
	
	
	/**加载门店货架列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/loadShelvesList")
	@ResponseBody
	public Object loadShelvesList() throws Exception{
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();//获取参数storeId
		if(StringUtil.isEmpty(pd.get("storeId"))){
			map.put("type", "1");
			map.put("msg", "请选择所属门店！");
			return AppUtil.returnObject(pd, map);
		}
		List<PageData> shelvesList = new ArrayList<PageData>();//列出货架列表
		shelvesList = shelvesService.listAll(pd);
		if(StringUtil.isPdList(shelvesList)){
			map.put("type", "1");
			map.put("msg", "当前门店下不存在货架，请先给当前门店添加货架！");
			return AppUtil.returnObject(pd, map);
		}
		map.put("type", "0");
		map.put("msg", "成功");
		map.put("list", shelvesList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出Place到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("标识");	//1
		titles.add("门店标识");	//2
		titles.add("货架标识");	//3
		titles.add("架位名称");	//4
		titles.add("架位商品");	//5
		titles.add("架位状态");	//6
		titles.add("创建时间");	//7
		titles.add("最后编辑时间");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = placeService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString());	//1
			vpd.put("var2", varOList.get(i).get("STOREID").toString());	//2
			vpd.put("var3", varOList.get(i).get("SHELVESID").toString());	//3
			vpd.put("var4", varOList.get(i).getString("PLACENAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("PLACEPLU"));	    //5
			vpd.put("var6", varOList.get(i).get("PLACETYPEID").toString());	//6
			vpd.put("var7", varOList.get(i).getString("CREATEDATE"));	    //7
			vpd.put("var8", varOList.get(i).getString("LASTDATE"));	    //8
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
		mv.setViewName("system/place/uploadexcel");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "placeplu.xls", "placeplu.xls");
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
