package com.fh.controller.handlecar.device;

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
import javax.tools.Tool;

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
import com.fh.util.ObjectExcelRead;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.PathUtil;
import com.fh.util.StringUtil;
import com.fh.util.Tools;
import com.fh.service.handlecar.device.DeviceManager;
import com.fh.service.system.cproperty.impl.CpropertyService;

/** 
 * 说明：办公设备管理
 * 创建人：Lukas 18923798379
 * 创建时间：2017-09-29
 */
@Controller
@RequestMapping(value="/device")
public class DeviceController extends BaseController {
	
	String menuUrl = "device/list.do"; //菜单地址(权限用)
	@Resource(name="deviceService")
	private DeviceManager deviceService;
	@Resource(name="cpropertyService")
	private CpropertyService cpropertyService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Device");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CREATEDATE", new Date());
		deviceService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Device");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		deviceService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Device");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("UPDATEDATE", new Date());
		deviceService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Device");
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
//		if(pd.getInt("deviceType")!=-1){
//			pd.put("deviceType", pd.getInt("deviceType"));
//		}
//		if(pd.getInt("deviceAttach")!=-1){
//			pd.put("deviceAttach", pd.getInt("deviceAttach"));
//		}
//		if(pd.getInt("deviceStatus")!=-1){
//			pd.put("deviceStatus", pd.getInt("deviceStatus"));
//		}
		page.setPd(pd);
		List<PageData>	varList = deviceService.list(page);	//列出Device列表
		mv.setViewName("handlecar/device/device_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		List<PageData> varcList = new ArrayList<PageData>();
		pd=new PageData();
		pd.put("CID", "11,12,13".split(","));
		varcList = cpropertyService.findAllByCId(pd);
		if(varcList!=null && varcList.size()>0){
			List<PageData> typeList = new ArrayList<PageData>();//设备类型集合
			List<PageData> attachList = new ArrayList<PageData>();//设备归属集合
			List<PageData> statusList = new ArrayList<PageData>();//设备状态集合
			for (Iterator iterator = varcList.iterator(); iterator.hasNext();) {
				PageData pageData = (PageData) iterator.next();
				int CID =pageData.getInt("CID");
				if(CID==11){//设备类型集合
					typeList.add(pageData);
				}else if(CID==12){//设备归属集合
					attachList.add(pageData);
				}else if(CID==13){//设备状态集合
					statusList.add(pageData);
				}
				
			}
			mv.addObject("typeList", typeList);
			mv.addObject("attachList", attachList);
			mv.addObject("statusList", statusList);
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
		mv.setViewName("handlecar/device/device_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		pd=new PageData();
		List<PageData> varcList = new ArrayList<PageData>();
		pd.put("CID", "11,12,13".split(","));
		varcList = cpropertyService.findAllByCId(pd);
		if(varcList!=null && varcList.size()>0){
			List<PageData> typeList = new ArrayList<PageData>();//设备类型集合
			List<PageData> attachList = new ArrayList<PageData>();//设备归属集合
			List<PageData> statusList = new ArrayList<PageData>();//设备状态集合
			for (Iterator iterator = varcList.iterator(); iterator.hasNext();) {
				PageData pageData = (PageData) iterator.next();
				int CID =pageData.getInt("CID");
				if(CID==11){//设备类型集合
					typeList.add(pageData);
				}else if(CID==12){//设备归属集合
					attachList.add(pageData);
				}else if(CID==13){//设备状态集合
					statusList.add(pageData);
				}
				
			}
			mv.addObject("typeList", typeList);
			mv.addObject("attachList", attachList);
			mv.addObject("statusList", statusList);
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
		pd = deviceService.findById(pd);	//根据ID读取
		mv.setViewName("handlecar/device/device_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		pd=new PageData();
		List<PageData> varcList = new ArrayList<PageData>();
		pd.put("CID", "11,12,13".split(","));
		varcList = cpropertyService.findAllByCId(pd);
		if(varcList!=null && varcList.size()>0){
			List<PageData> typeList = new ArrayList<PageData>();//设备类型集合
			List<PageData> attachList = new ArrayList<PageData>();//设备归属集合
			List<PageData> statusList = new ArrayList<PageData>();//设备状态集合
			for (Iterator iterator = varcList.iterator(); iterator.hasNext();) {
				PageData pageData = (PageData) iterator.next();
				int CID =pageData.getInt("CID");
				if(CID==11){//设备类型集合
					typeList.add(pageData);
				}else if(CID==12){//设备归属集合
					attachList.add(pageData);
				}else if(CID==13){//设备状态集合
					statusList.add(pageData);
				}
				
			}
			mv.addObject("typeList", typeList);
			mv.addObject("attachList", attachList);
			mv.addObject("statusList", statusList);
		}
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Device");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			deviceService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Device到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("设备类型");	//1
		titles.add("设备名称");	//2
		titles.add("设备归属");	//3
		titles.add("设备描述");	//4
		titles.add("设备状态");	//5
		titles.add("备注");	//6
		titles.add("创建时间");	//7
		titles.add("变更时间");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = deviceService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("TYPENAME").toString());	//1
			vpd.put("var2", varOList.get(i).getString("DEVICENAME"));	    //2
			vpd.put("var3", varOList.get(i).get("ATTACHNAME").toString());	//3
			vpd.put("var4", varOList.get(i).getString("DEVICEDEPICT"));	    //4
			vpd.put("var5", varOList.get(i).get("STATUSNAME").toString());	//5
			vpd.put("var6", varOList.get(i).getString("REMARKS"));	    //6
			vpd.put("var7", Convert.dateToStr(Convert.strToDate(varOList.get(i).get("CREATEDATE").toString(), new Date()),"yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00"));	    //7
			if(Tools.isEmpty(varOList.get(i).get("UPDATEDATE")+"")){
				vpd.put("var8","");
			}else{
				vpd.put("var8", Convert.dateToStr(Convert.strToDate(varOList.get(i).get("UPDATEDATE").toString(), new Date()),"yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00"));	    //8
			}
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
		mv.setViewName("handlecar/device/uploadexcel");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "device.xls", "device.xls");
	}
	
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	public ModelAndView readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		ModelAndView mv = this.getModelAndView();
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;}
		if (null == file || file.isEmpty()) {
			mv.addObject("typeCode",1);
			mv.addObject("msg","无法读取EXCEL中的数据，请检查EXCEL模版或者EXCEL中是否有数据！");
			mv.setViewName("save_result");
			return mv;
		}
		String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;								//文件上传路径
		String fileName =  FileUpload.fileUp(file, filePath, "device");							//执行上传
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, fileName, 2, 0, 0);		//执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
		/*判断excel是否有数据*/
		if(listPd==null || listPd.size()<=0){
			mv.addObject("typeCode",1);
			mv.addObject("msg","无法读取EXCEL中的数据，请检查EXCEL模版或者EXCEL中是否有数据！");
			mv.setViewName("save_result");
			return mv;
		}
		List<PageData> deviceList = new ArrayList<>();
		for(int i=0;i<listPd.size();i++){
			PageData p = new PageData();
			int DEVICETYPE = Convert.strToInt(listPd.get(i).getString("var0"),0);
			String DEVICENAME=listPd.get(i).getString("var1");
			int DEVICEATTACH=Convert.strToInt(listPd.get(i).getString("var2"),0);
			String DEVICEDEPICT =listPd.get(i).getString("var3");
			int DEVICESTATUS =  Convert.strToInt(listPd.get(i).getString("var4"),0);
			if(DEVICETYPE==0 ||  DEVICEATTACH==0 ||  DEVICESTATUS==0){
				continue;
			}
			p.put("DEVICETYPE", DEVICETYPE);//设备类型
			p.put("DEVICENAME", DEVICENAME);//设备名称
			p.put("DEVICEATTACH", DEVICEATTACH);//设备所属部门
			p.put("DEVICEDEPICT", DEVICEDEPICT);//设备描述
			p.put("DEVICESTATUS",DEVICESTATUS);//设备状态
			p.put("REMARKS", listPd.get(i).getString("var5"));//备注
			p.put("CREATEDATE",new Date());//时间
			deviceList.add(p);
			
		}
		deviceService.batchSave(deviceList);
		mv.addObject("msg"," 共： "+deviceList.size()+" 条记录；成功导入： "+deviceList.size()+" 条记录");
		mv.addObject("typeCode",0);
		mv.setViewName("save_result");
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
