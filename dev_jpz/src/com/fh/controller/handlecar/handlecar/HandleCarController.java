package com.fh.controller.handlecar.handlecar;

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
import com.fh.util.ObjectExcelRead;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.PathUtil;
import com.fh.util.StringUtil;
import com.fh.util.Tools;
import com.fh.service.handlecar.handlecar.HandleCarManager;

/** 
 * 说明：提货卡补卡记录
 * 创建人：Lukas 18923798379
 * 创建时间：2017-09-27
 */
@Controller
@RequestMapping(value="/handlecar")
public class HandleCarController extends BaseController {
	
	String menuUrl = "handlecar/list.do"; //菜单地址(权限用)
	@Resource(name="handlecarService")
	private HandleCarManager handlecarService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增HandleCar");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ID", this.get32UUID());	//主键
		pd.put("CREATEDATE",new Date());	//主键
		handlecarService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除HandleCar");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		handlecarService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改HandleCar");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("HANDLEDATE", new Date());
		handlecarService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表HandleCar");
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
		page.setPd(pd);
		List<PageData>	varList = handlecarService.list(page);	//列出HandleCar列表
		mv.setViewName("handlecar/handlecar/handlecar_list");
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
		mv.setViewName("handlecar/handlecar/handlecar_edit");
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
		pd = handlecarService.findById(pd);	//根据ID读取
		mv.setViewName("handlecar/handlecar/handlecar_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除HandleCar");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			handlecarService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出HandleCar到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("标识");	//1
		titles.add("原始卡号");	//2
		titles.add("补发卡号");	//3
		titles.add("归属");	//4
		titles.add("状态");	//5
		titles.add("会员名称");	//6
		titles.add("联系方式");	//7
		titles.add("备注");	//8
		titles.add("创建时间");	//9
		titles.add("补办时间");	//10
		titles.add("发放面额");	//11
		titles.add("已使用金额");	//12
		titles.add("剩余金额");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = handlecarService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("CARNUM"));	    //2
			vpd.put("var3", varOList.get(i).getString("NEWCARNUM"));	    //3
			vpd.put("var4", varOList.get(i).getString("STORENAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("STATUS"));	    //5
			vpd.put("var6", varOList.get(i).getString("VIPNAME"));	    //6
			vpd.put("var7", varOList.get(i).getString("CONTACT"));	    //7
			vpd.put("var8", varOList.get(i).getString("REMARKS"));	    //8
			vpd.put("var9", varOList.get(i).getString("CREATEDATE"));	    //9
			vpd.put("var10", varOList.get(i).getString("HANDLEDATE"));	    //10
			vpd.put("var11", varOList.get(i).get("DENOMINATION").toString());	//11
			vpd.put("var12", varOList.get(i).get("USEMONEY").toString());	//12
			vpd.put("var13", varOList.get(i).get("SURPLUSMONEY").toString());	//13
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
		mv.setViewName("handlecar/handlecar/uploadexcel");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "123.xls", "123.xls");
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
		String fileName =  FileUpload.fileUp(file, filePath, "12312312");							//执行上传
		List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0);		//执行读EXCEL操作,读出的数据导入List 1:从第2行开始；0:从第A列开始；0:第0个sheet
		/*判断excel是否有数据*/
		if(listPd==null || listPd.size()<=0){
			mv.addObject("typeCode",1);
			mv.addObject("msg","无法读取EXCEL中的数据，请检查EXCEL模版或者EXCEL中是否有数据！");
			mv.setViewName("save_result");
			return mv;
		}
		List<PageData> handlecarList = new ArrayList<>();
		for(int i=0;i<listPd.size();i++){
			PageData p = new PageData();
			if(Convert.strToInt(listPd.get(i).getString("var0"),-1)==-1){
				continue;
			}
			p.put("ID", this.get32UUID());
			p.put("CARNUM", Convert.strToInt(listPd.get(i).getString("var0"),0));
			if(StringUtil.isEmpty(listPd.get(i).get("var1"))){
				p.put("NEWCARNUM", "");
			}else{
				p.put("NEWCARNUM", Convert.strToInt(listPd.get(i).getString("var1"),0));
			}
			
			p.put("STORENAME", listPd.get(i).getString("var2"));
			p.put("STATUS", listPd.get(i).getString("var6"));
			p.put("VIPNAME", listPd.get(i).getString("var7"));
			p.put("CONTACT", listPd.get(i).getString("var8"));
			p.put("REMARKS", listPd.get(i).getString("var9"));
			p.put("CREATEDATE", new Date());
			p.put("DENOMINATION",Convert.strToDouble(listPd.get(i).get("var3")+"",0));
			p.put("USEMONEY", Convert.strToDouble(listPd.get(i).get("var4")+"",0));
			p.put("SURPLUSMONEY",Convert.strToDouble(listPd.get(i).get("var5")+"",0));
			handlecarList.add(p);
		}
		handlecarService.batchSave(handlecarList);
		mv.addObject("msg"," 共： "+handlecarList.size()+" 条记录；成功导入： "+handlecarList.size()+" 条记录");
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
