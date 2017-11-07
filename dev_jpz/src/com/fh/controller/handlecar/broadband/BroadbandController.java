package com.fh.controller.handlecar.broadband;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.fh.util.Tools;
import com.fh.service.handlecar.broadband.BroadbandManager;

/** 
 * 说明：宽带信息
 * 创建人：Lukas 18923798379
 * 创建时间：2017-10-10
 */
@Controller
@RequestMapping(value="/broadband")
public class BroadbandController extends BaseController {
	
	String menuUrl = "broadband/list.do"; //菜单地址(权限用)
	@Resource(name="broadbandService")
	private BroadbandManager broadbandService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Broadband");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("createDate", new Date());
		broadbandService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Broadband");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		broadbandService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Broadband");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		broadbandService.edit(pd);
		mv.addObject("msg","编辑成功");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**续费
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/renew")
	public ModelAndView renew() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Broadband续费");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		broadbandService.editDate(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Broadband");
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
		List<PageData>	varList = broadbandService.list(page);	//列出Broadband列表
		mv.setViewName("handlecar/broadband/broadband_list");
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
		mv.setViewName("handlecar/broadband/broadband_edit");
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
		pd = broadbandService.findById(pd);	//根据ID读取
		mv.setViewName("handlecar/broadband/broadband_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
		/**去续费
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goRenew")
		public ModelAndView goRenew()throws Exception{
			ModelAndView mv = this.getModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			pd = broadbandService.findById(pd);	//根据ID读取
			mv.setViewName("handlecar/broadband/broadband_edit_renew");
			mv.addObject("msg", "renew");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Broadband");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			broadbandService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Broadband到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("标识");	//1
		titles.add("所属");	//2
		titles.add("名称");	//3
		titles.add("宽带编号");	//4
		titles.add("用户名");	//5
		titles.add("密码");	//6
		titles.add("宽带运营商");	//7
		titles.add("带宽");	//8
		titles.add("到期时间");	//9
		titles.add("联系人");	//10
		titles.add("联系方式");	//11
		titles.add("最近续期时间");	//12
		titles.add("创建时间");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = broadbandService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString());	//1
			vpd.put("var2", varOList.get(i).getString("ATTACH"));	    //2
			vpd.put("var3", varOList.get(i).getString("WBNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("WBNUMBER"));	    //4
			vpd.put("var5", varOList.get(i).getString("USERNAME"));	    //5
			vpd.put("var6", varOList.get(i).getString("USERPASSWORD"));	    //6
			vpd.put("var7", varOList.get(i).getString("WBTYPE"));	    //7
			vpd.put("var8", varOList.get(i).getString("BW"));	    //8
			vpd.put("var9", varOList.get(i).getString("EXPIREDATE"));	    //9
			vpd.put("var10", varOList.get(i).getString("CONTACTS"));	    //10
			vpd.put("var11", varOList.get(i).getString("CSTEL"));	    //11
			if(Tools.isEmpty(varOList.get(i).get("RENEWDATE")+"")){
				vpd.put("var12", "");	    //12
			}else{
				vpd.put("var12", Convert.dateToStr(Convert.strToDate(varOList.get(i).get("RENEWDATE").toString(), new Date()),"yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00"));	    //12
			}
			vpd.put("var13", Convert.dateToStr(Convert.strToDate(varOList.get(i).get("CREATEDATE").toString(), new Date()),"yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00"));	    //13
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
		mv.setViewName("handlecar/broadband/uploadexcel");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "broadband.xls", "broadband.xls");
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
		List<PageData> bookList = new ArrayList<>();
		for(int i=0;i<listPd.size();i++){
			PageData p = new PageData();
			if(Tools.isEmpty(listPd.get(i).getString("var0")) || Tools.isEmpty(listPd.get(i).getString("var3")) || Tools.isEmpty(listPd.get(i).getString("var4")) ||Tools.isEmpty(listPd.get(i).getString("var5"))){
				continue;
			}
			p.put("ATTACH", listPd.get(i).getString("var0"));//所属
			p.put("WBNAME", listPd.get(i).getString("var1"));//宽带名称
			p.put("WBNUMBER", listPd.get(i).getString("var2"));//宽带编码
			p.put("USERNAME", listPd.get(i).getString("var3"));//宽带用户名
			p.put("USERPASSWORD",listPd.get(i).getString("var4"));//密码
			p.put("WBTYPE", listPd.get(i).getString("var5"));//宽带类型
			p.put("BW", listPd.get(i).getString("var6"));//带宽
			p.put("EXPIREDATE", listPd.get(i).getString("var7"));//到期时间
			p.put("CONTACTS", listPd.get(i).getString("var8"));//联系人
			p.put("CSTEL", listPd.get(i).getString("var9"));//联系方式
			p.put("CREATEDATE",new Date());//时间
			bookList.add(p);
			
		}
		broadbandService.batchSave(bookList);
		mv.addObject("msg"," 共： "+listPd.size()+" 条记录；成功导入： "+bookList.size()+" 条记录");
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
