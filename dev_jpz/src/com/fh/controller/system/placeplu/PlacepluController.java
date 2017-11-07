package com.fh.controller.system.placeplu;

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
import com.fh.util.FileUpload;
import com.fh.util.ObjectExcelRead;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.PathUtil;
import com.fh.util.StringUtil;
import com.fh.util.Tools;
import com.fh.service.system.cproperty.CpropertyManager;
import com.fh.service.system.inventory.InventoryManager;
import com.fh.service.system.placeplu.PlacepluManager;
import com.fh.service.system.shelves.ShelvesManager;
import com.fh.service.system.skuplu.SkupluManager;
import com.fh.service.system.store.StoreManager;

/** 
 * 说明：货位商品
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-29
 */
@Controller
@RequestMapping(value="/placeplu")
public class PlacepluController extends BaseController {
	
	String menuUrl = "placeplu/list.do"; //菜单地址(权限用)
	@Resource(name="placepluService")
	private PlacepluManager placepluService;
	@Resource(name="cpropertyService")
	private CpropertyManager cpropertyService;
	@Resource(name="storeService")
	private StoreManager storeService;
	@Resource(name="skupluService")
	private SkupluManager skupluService;
	@Resource(name="inventoryService")
	private InventoryManager inventoryService;
	@Resource(name="shelvesService")
	private ShelvesManager shelvesService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Placeplu");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		placepluService.save(pd);
		mv.addObject("msg","新增成功");
		mv.setViewName("save_result");
		return mv;
	}
	
	    /**批量增加架位商品
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/savePlu")
		@ResponseBody
		public Object savePlu() throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"批量增加架位商品Placeplu");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
			PageData pd = new PageData();		
			Map<String,Object> map = new HashMap<String,Object>();
			pd = this.getPageData();
			placepluService.batchSave(pd);
			map.put("type", 0);
			map.put("msg", "绑定成功！");
			return AppUtil.returnObject(pd, map);
		}
		
		
	
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Placeplu");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		placepluService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Placeplu");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		placepluService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Placeplu");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if(Tools.notEmpty(pd.getString("keywords"))){
			pd.put("keywords", pd.getString("keywords"));
		}
		pd.put("storeId", Convert.strToInt(pd.get("storeId")+"", -1));
		pd.put("shelvesId", Convert.strToInt(pd.get("shelvesId")+"", -1));
		pd.put("pluClassId", Convert.strToInt(pd.get("pluClassId")+"", -1));
		pd.put("pluTypeId", Convert.strToInt(pd.get("pluTypeId")+"", -1));
		pd.put("pluStatus", Convert.strToInt(pd.get("pluStatus")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = placepluService.list(page);	//列出Placeplu列表
		mv.setViewName("system/placeplu/placeplu_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		pd=new PageData();
		pd.put("CID", "1,2,5".split(","));
		List<PageData> varcList = new ArrayList<PageData>();
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
		List<PageData> storeList = new ArrayList<PageData>();
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
		mv.setViewName("system/placeplu/placeplu_edit");
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
		pd = placepluService.findById(pd);	//根据ID读取
		mv.setViewName("system/placeplu/placeplu_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Placeplu");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			placepluService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
		/**加载货架
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/loadShelves")
		@ResponseBody
		public Object loadShelves() throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"批量删除Placeplu");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
			PageData pd = new PageData();		
			Map<String,Object> map = new HashMap<String,Object>();
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			pdList = shelvesService.listAll(pd);//storeId
			if(StringUtil.isPdList(pdList)){
				map.put("msg", "no");
			}else{
				map.put("msg", "ok");
			}
			map.put("pdList", pdList);
			return AppUtil.returnObject(pd, map);
		}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出Placeplu到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("商品编码");	//1
		titles.add("商品名称");	//2
		titles.add("商品条码");	//3
		titles.add("商品单价");	//4
		titles.add("商品促销价");	//5
		titles.add("规格");	//6
		titles.add("单位");	//7
		titles.add("商品分类");	//8
		titles.add("商品类型");	//9
		titles.add("商品状态");	//10
		titles.add("所属门店");	//11
		titles.add("所属货架");	//12
		titles.add("所属货位");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = placepluService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("PLUCODE").toString());	//1
			vpd.put("var2", varOList.get(i).get("PLUNAME").toString());	//2
			vpd.put("var3", varOList.get(i).get("BARCODE").toString());	//3
			vpd.put("var4", varOList.get(i).get("PRICE").toString());	//4
			vpd.put("var5", varOList.get(i).get("PPRICE").toString());	//5
			vpd.put("var6", varOList.get(i).get("SPEC").toString());	//6
			vpd.put("var7", varOList.get(i).get("UNITS").toString());	//7
			vpd.put("var8", varOList.get(i).get("PLUCLASSNAME").toString());	//8
			vpd.put("var9", varOList.get(i).get("PLUTYPENAME").toString());	//9
			vpd.put("var10", varOList.get(i).get("PLUSTATUSNAME").toString());	//10
			vpd.put("var11", varOList.get(i).get("STORENAME").toString());	//11
			vpd.put("var12", varOList.get(i).get("SHELVESNAME").toString());	//12
			vpd.put("var13", varOList.get(i).get("PLACENAME").toString());	//13
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	public ModelAndView readExcel(@RequestParam(value="excel",required=false) MultipartFile file,@RequestParam(value="storeId",required=false) int storeId
			,@RequestParam(value="shelvesId",required=false) int shelvesId
			,@RequestParam(value="placeId",required=false) int placeId) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd=this.getPageData();
		int STOREID = storeId;
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
			
			/*获取当前门店架位已存放商品档案数据*/
			pd = new PageData();
			pd.put("storeId", STOREID);
			pd.put("shelvesId", shelvesId);
			pd.put("placeId", placeId);
			List<PageData> listPlacePlu =placepluService.listAll(pd);
			
			/*存入数据库操作======================================*/
			/**
			 * var0 :商品编号
			 */
			List<PageData> saveStorePluList =new ArrayList<PageData>();
			String pluInfo="商品档案不存在商品编码为：";//商品档案提示信息
			String storeInfo="当前门店还未初始化商品编码为：";//门店商品提示信息
			String pluCodes="";
			for(int i=0;i<listPd.size();i++){		
				PageData inpd = new PageData();
				int excPluCode = -1;
				if(!StringUtil.isEmpty(listPd.get(i).getString("var0"))){
					boolean isPlu=false;//标识在商品档案和门店商品中是否存在
					boolean isPluInit=false;//标识在商品档案和门店商品中是否存在
					excPluCode = Convert.strToInt(listPd.get(i).getString("var0"), excPluCode);//导入商品编码
					for (Iterator iterator = listPlu.iterator(); iterator.hasNext();) {
						PageData pageData = (PageData) iterator.next();
						if(pageData.getInt("pluCode")==excPluCode){
							isPlu=true;
							break;
						}
						
					}
					if(isPlu==false){
						mv.addObject("typeCode",1);
						mv.addObject("msg",pluInfo+=excPluCode);
						mv.setViewName("save_result");
						return mv;
					}
					if(!StringUtil.isPdList(listStorePlu)){
						for (Iterator iterator = listStorePlu.iterator(); iterator.hasNext();) {
							PageData pageData = (PageData) iterator.next();
							if(pageData.getInt("PLUCODE")==excPluCode){
								isPluInit=true;
								break;
							}
							
						}
					}
					if(isPluInit==false){
						mv.addObject("typeCode",1);
						mv.addObject("msg",storeInfo+=excPluCode);
						mv.setViewName("save_result");
						return mv;
					}
					if(!StringUtil.isPdList(listPlacePlu)){
						for (Iterator iterator = listPlacePlu.iterator(); iterator.hasNext();) {
							PageData pageData = (PageData) iterator.next();
							if(pageData.getInt("PLUCODE")==excPluCode){
								isPlu=false;
								break;
							}
						}
					}
					if(isPlu==false){
						mv.addObject("typeCode",1);
						mv.addObject("msg","当前货架已存在商品，商品编码："+excPluCode);
						mv.setViewName("save_result");
						return mv;
					}
					pluCodes+=excPluCode+",";
				}
				
			}
			if(StringUtil.isEmpty(pluCodes)){
				mv.addObject("typeCode",1);
				mv.addObject("msg","无法读取EXCEL中的数据，请检查EXCEL模版或者EXCEL中是否有数据！");
				mv.setViewName("save_result");
				return mv;
			}
			pluCodes = StringUtil.StrLists(pluCodes);
			if(pluCodes.endsWith(",")){
				pluCodes =pluCodes.substring(0,pluCodes.length()-1);
			}
			f=pluCodes.split(",").length;
			if(pluCodes.split(",").length<=0){
				mv.addObject("typeCode",1);
				mv.addObject("msg","共："+s+"条记录;成功导入："+f+"条记录,失败导入："+(s-f)+"条记录; "+pluInfo+" ; "+storeInfo);
				mv.setViewName("save_result");
				return mv;
			}
			pd = new PageData();
			pd.put("storeId", STOREID);
			pd.put("shelvesId", shelvesId);
			pd.put("placeId", placeId);
			pd.put("pluCodes", pluCodes);
			placepluService.batchSave(pd);
			/*存入数据库操作======================================*/
			mv.addObject("typeCode",0);
			if(s-f==0){
				mv.addObject("msg"," 共： "+s+" 条记录;成功导入： "+f+" 条记录 ");
			}else{
				mv.addObject("msg"," 共： "+s+" 条记录;成功导入： "+f+" 条记录,失败导入： "+(s-f)+" 条记录; ");
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
