package com.fh.controller.system.skuplu;

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
import com.fh.service.system.cproperty.CpropertyManager;
import com.fh.service.system.skuplu.SkupluManager;
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
 * 说明：商品基本信息
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-19
 */
@Controller
@RequestMapping(value="/skuplu")
public class SkupluController extends BaseController {
	
	String menuUrl = "skuplu/list.do"; //菜单地址(权限用)
	@Resource(name="skupluService")
	private SkupluManager skupluService;
	@Resource(name="cpropertyService")
	private CpropertyManager cpropertyService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Skuplu");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//pd.put("SKUPLU_ID", this.get32UUID());	//主键
		pd.put("CREATEDATE", new Date());
	    pd.put("LASTDATE", new Date());
		skupluService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Skuplu");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		skupluService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Skuplu");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("LASTDATE", new Date());
		skupluService.edit(pd);
		mv.addObject("msg","编辑成功");
		mv.setViewName("save_result");
		return mv;
	}
	
	
	/**修改状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editStatus")
	public ModelAndView editStatus() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Skuplu状态");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		skupluService.editStatus(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Skuplu");
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
		pd.put("pluCode", Convert.strToInt(pd.get("pluCode")+"", -1));
		pd.put("pluClassId", Convert.strToInt(pd.get("pluClassId")+"", -1));
		pd.put("pluTypeId", Convert.strToInt(pd.get("pluTypeId")+"", -1));
		pd.put("pluStatus", Convert.strToInt(pd.get("pluStatus")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = skupluService.list(page);	//列出Skuplu列表
		mv.setViewName("system/skuplu/skuplu_list");
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
		mv.setViewName("system/skuplu/skuplu_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
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
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = skupluService.findById(pd);	//根据ID读取
		mv.setViewName("system/skuplu/skuplu_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
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
	
	
	/**去修改状态页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditStatus")
	public ModelAndView goEditStatus()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = skupluService.findById(pd);	//根据ID读取
		mv.setViewName("system/skuplu/skuplu_plu_status");
		mv.addObject("msg", "editStatus");
		mv.addObject("pd", pd);
		List<PageData> pluStatusList = new ArrayList<PageData>();
		pd=new PageData();
		pd.put("CID", "5".split(","));
		pluStatusList = cpropertyService.findAllByCId(pd);
		mv.addObject("pluStatusList", pluStatusList);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Skuplu");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			skupluService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Skuplu到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("商品编码");	//2
		titles.add("商品名称");	//3
		titles.add("商品条形码");	//4
		titles.add("单位");	//5
		titles.add("规格");	//6
		titles.add("图片");	//7
		titles.add("售价");	//8
		titles.add("促销价");	//9
		titles.add("商品分类");	//10
		titles.add("商品类型");	//11
		titles.add("商品状态");	//12
		titles.add("创建时间");	//13
		titles.add("最后编辑时间");	//14
		dataMap.put("titles", titles);
		List<PageData> varOList = skupluService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("pluCode").toString());	//2
			vpd.put("var2", varOList.get(i).getString("pluName"));	    //3
			vpd.put("var3", varOList.get(i).getString("barCode"));	    //4
			vpd.put("var4", varOList.get(i).getString("units"));	    //5
			vpd.put("var5", varOList.get(i).getString("spec"));	    //6
			vpd.put("var6", varOList.get(i).getString("pluImage"));	    //7
			vpd.put("var7", varOList.get(i).get("price")+"");	    //8
			vpd.put("var8", varOList.get(i).get("pPrice")+"");	    //9
			vpd.put("var9", varOList.get(i).get("pluClassName").toString());	//10
			vpd.put("var10", varOList.get(i).get("pluStatusName").toString());	//11
			vpd.put("var11", varOList.get(i).get("pluTypeName").toString());	//12
			vpd.put("var12",Convert.dateToStr(Convert.strToDate(varOList.get(i).get("createDate").toString(), new Date()),"yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00"));	    //13
			vpd.put("var13",Convert.dateToStr(Convert.strToDate(varOList.get(i).get("LastDate").toString(), new Date()),"yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00"));	    //14
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
		mv.setViewName("system/skuplu/uploadexcel");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "pluMode.xls", "pluMode.xls");
	}
	
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	public ModelAndView readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
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
			//查询条件
			pd.put("pluCode", -1);
			pd.put("pluClassId", -1);
			pd.put("pluStatus", -1);
			pd.put("pluTypeId", -1);
			pd.put("lastStart", "");
			pd.put("lastEnd", "");
			List<PageData> listPlu =skupluService.listAll(pd);///*读取商品档案数据*/
			/*存入数据库操作======================================*/
			/**
			 * var0 :商品编号
			 */
			List<PageData> saveStorePluList =new ArrayList<PageData>();
			String pluInfo="商品档案已存在商品编码为：";//商品档案提示信息
			for(int i=0;i<listPd.size();i++){
				PageData inpd = new PageData();
				int excPluCode = -1;
				if(!StringUtil.isEmpty(listPd.get(i).getString("var0"))){
					excPluCode = Convert.strToInt(listPd.get(i).getString("var0"), excPluCode);//导入商品编码
					if(!StringUtil.isPdList(listPlu)){
						for (Iterator iterator = listPlu.iterator(); iterator.hasNext();) {
							PageData pageData = (PageData) iterator.next();
							if(pageData.getInt("pluCode")==excPluCode){
								mv.addObject("typeCode",1);
								mv.addObject("msg","商品档案已存在商品编码为："+excPluCode);
								mv.setViewName("save_result");
								return mv;
							}
						}
					}
					inpd.put("PLUCODE", excPluCode);
					inpd.put("PLUNAME", listPd.get(i).getString("var1"));
					inpd.put("BARCODE", listPd.get(i).getString("var2"));
					inpd.put("UNITS", listPd.get(i).getString("var3"));
					inpd.put("SPEC", listPd.get(i).getString("var4"));
					inpd.put("PLUIMAGE", listPd.get(i).getString("var5"));
					inpd.put("PRICE", Convert.strToDouble(listPd.get(i).getString("var6"), 0));
					inpd.put("PPRICE", Convert.strToDouble(listPd.get(i).getString("var7"), 0));
					inpd.put("PLUCLASSID", Convert.strToInt(listPd.get(i).getString("var8"), 0));
					inpd.put("PLUTYPEID", Convert.strToInt(listPd.get(i).getString("var9"), 0));
					inpd.put("PLUSTATUS", Convert.strToInt(listPd.get(i).getString("var10"), 0));
					inpd.put("CREATEDATE", new Date());
					inpd.put("LASTDATE", new Date());
					saveStorePluList.add(inpd);
					
					
				}
				
			}
			f=saveStorePluList.size();
			if(f>0){
				skupluService.batchSave(saveStorePluList);
			}
			/*存入数据库操作======================================*/
			mv.addObject("typeCode",0);
			if(s-f==0){
				mv.addObject("msg"," 共： "+s+" 条记录；成功导入： "+f+" 条记录");
			}else{
				mv.addObject("msg"," 共："+s+" 条记录；成功导入： "+f+" 条记录,失败导入： "+(s-f)+" 条记录; "+pluInfo+" ;");
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
