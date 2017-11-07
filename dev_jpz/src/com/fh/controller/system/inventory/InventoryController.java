package com.fh.controller.system.inventory;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
import com.fh.service.system.inventory.InventoryManager;
import com.fh.service.system.skuplu.SkupluManager;
import com.fh.service.system.store.StoreManager;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Convert;
import com.fh.util.FileDownload;
import com.fh.util.FileUpload;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelRead;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.PathUtil;
import com.fh.util.StringUtil;
import com.fh.util.Tools;

/** 
 * 说明：门店商品库存
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-20
 */
@Controller
@RequestMapping(value="/inventory")
public class InventoryController extends BaseController {
	
	String menuUrl = "inventory/list.do"; //菜单地址(权限用)
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
		logBefore(logger, Jurisdiction.getUsername()+"新增Inventory");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//pd.put("INVENTORY_ID", this.get32UUID());	//主键
		inventoryService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Inventory");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		inventoryService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改预警值
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Inventory预警值");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		inventoryService.editTaps(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Inventory");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if(Tools.notEmpty(pd.getString("keywords"))){
			pd.put("keywords", pd.getString("keywords"));
		}
		if(Tools.isEmpty(pd.getString("sortsFied"))){
			pd.put("sortsFied", "inventoryCount");
		}else{
			pd.put("sortsFied", pd.getString("sortsFied"));
		}
		if(Tools.isEmpty(pd.getString("orders"))){
			pd.put("orders", "desc");
		}else{
			pd.put("orders", pd.getString("orders"));
		}
		pd.put("storeId", Convert.strToInt(pd.get("storeId")+"", -1));
		pd.put("pluCode", Convert.strToInt(pd.get("pluCode")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = inventoryService.list(page);	//列出Inventory列表
		mv.setViewName("system/inventory/inventory_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
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
		pd = inventoryService.findById(pd);	//根据ID读取
		mv.setViewName("system/inventorycheck/inventorycheck_edit");
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
		pd = inventoryService.findById(pd);	//根据ID读取
		mv.setViewName("system/inventory/inventory_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Inventory");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			inventoryService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Inventory到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("门店");	//1
		titles.add("商品编码");	//2
		titles.add("商品名称");	//3
		titles.add("商品条形码");	//4
		titles.add("商品单位");	//5
		titles.add("商品规格");	//6
		titles.add("商品售价");	//7
		titles.add("商品促销价");	//8
		titles.add("库存数量");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = inventoryService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("STORENAME").toString());	//1
			vpd.put("var2", varOList.get(i).get("PLUCODE").toString());	//2
			vpd.put("var3", varOList.get(i).get("PLUNAME").toString());	//3
			vpd.put("var4", varOList.get(i).get("BARCODE").toString());	//4
			vpd.put("var5", varOList.get(i).get("UNITS").toString());	//5
			vpd.put("var6", varOList.get(i).get("SPEC").toString());	//6
			vpd.put("var7", varOList.get(i).get("PRICE").toString());	//7
			vpd.put("var8", varOList.get(i).get("PPRICE").toString());	//8
			vpd.put("var9", varOList.get(i).get("INVENTORYCOUNT").toString());	//9
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
		mv.setViewName("system/store/uploadexcel");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "storeMode.xls", "storeMode.xls");
	}
	
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	public ModelAndView readExcel(@RequestParam(value="excel",required=false) MultipartFile file
			,@RequestParam(value="STOREID",required=false) int STOREID) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd=this.getPageData();
		int s=0;
		int f=0;
		if(STOREID==-1){
			mv.addObject("typeCode",2);
			mv.addObject("msg","网络繁忙，请稍后！");
			mv.setViewName("save_result");
			return mv;
		}
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
			pd.put("storeId", STOREID);
			List<PageData> listStorePlu =inventoryService.listAll(pd);
			
			/*存入数据库操作======================================*/
			/**
			 * var0 :商品编号
			 */
			List<PageData> saveStorePluList =new ArrayList<PageData>();
			String pluInfo="商品档案不存在商品编码为：";//商品档案提示信息
			String storeInfo="当前门店已初始化商品编码为：";//门店商品提示信息
			for(int i=0;i<listPd.size();i++){		
				PageData inpd = new PageData();
				int excPluCode = -1;
				int taps=-1;//库存不足多少时进行提示，-1永不提醒
				if(!StringUtil.isEmpty(listPd.get(i).getString("var0"))){
					boolean isPlu=false;//标识在商品档案和门店商品中是否存在
					excPluCode = Convert.strToInt(listPd.get(i).getString("var0"), excPluCode);//导入商品编码
					taps = Convert.strToInt(listPd.get(i).getString("var1"), taps);//库存不足值
					for (Iterator iterator = listPlu.iterator(); iterator.hasNext();) {
						PageData pageData = (PageData) iterator.next();
						if(pageData.getInt("pluCode")==excPluCode){
							isPlu=true;
							break;
						}
						
					}
					if(isPlu==false){
						pluInfo+=excPluCode+",";//在商品档案中不存在该商品，
						continue;
					}
					if(!StringUtil.isPdList(listStorePlu) && isPlu==true){
						for (Iterator iterator = listStorePlu.iterator(); iterator.hasNext();) {
							PageData pageData = (PageData) iterator.next();
							if(pageData.getInt("PLUCODE")==excPluCode){
								isPlu=false;
								break;
							}
							
						}
					}
					if(isPlu==false){
						storeInfo+=excPluCode+",";//该门店已初始化该商品
						continue;
					}else{
						inpd.put("STOREID", STOREID);
						inpd.put("PLUCODE", excPluCode);
						inpd.put("INVENTORYCOUNT", 0);
						inpd.put("TAPS", taps);
						saveStorePluList.add(inpd);
					}
					
					
				}
				
			}
			f=saveStorePluList.size();
			if(saveStorePluList==null || saveStorePluList.size()<=0){
				mv.addObject("typeCode",1);
				mv.addObject("msg","共："+s+"条记录;成功导入："+f+"条记录,失败导入："+(s-f)+"条记录; "+pluInfo+" ; "+storeInfo);
				mv.setViewName("save_result");
				return mv;
			}
			inventoryService.batchSave(saveStorePluList);
			/*存入数据库操作======================================*/
			mv.addObject("typeCode",0);
			if(s-f==0){
				mv.addObject("msg"," 共： "+s+" 条记录;成功导入： "+f+" 条记录 ");
			}else{
				mv.addObject("msg"," 共： "+s+" 条记录;成功导入： "+f+" 条记录,失败导入： "+(s-f)+" 条记录; "+pluInfo+" ; "+storeInfo);
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
