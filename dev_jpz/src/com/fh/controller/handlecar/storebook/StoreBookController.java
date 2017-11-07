package com.fh.controller.handlecar.storebook;

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
import com.fh.service.handlecar.storebook.StoreBookManager;

/** 
 * 说明：门店通讯录
 * 创建人：Lukas 18923798379
 * 创建时间：2017-10-10
 */
@Controller
@RequestMapping(value="/storebook")
public class StoreBookController extends BaseController {
	
	String menuUrl = "storebook/list.do"; //菜单地址(权限用)
	@Resource(name="storebookService")
	private StoreBookManager storebookService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增StoreBook");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CREATEDATE", new Date());
		storebookService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除StoreBook");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		storebookService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改StoreBook");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EDITDATE", new Date());
		storebookService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表StoreBook");
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
		List<PageData>	varList = storebookService.list(page);	//列出StoreBook列表
		mv.setViewName("handlecar/storebook/storebook_list");
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
		mv.setViewName("handlecar/storebook/storebook_edit");
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
		pd = storebookService.findById(pd);	//根据ID读取
		mv.setViewName("handlecar/storebook/storebook_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除StoreBook");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			storebookService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出StoreBook到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("门店");	//2
		titles.add("远程设备名称");	//3
		titles.add("远程工具名称");	//4
		titles.add("账号");	//5
		titles.add("密码");	//6
		titles.add("门店电话");	//7
		titles.add("店长");	//8
		titles.add("店长工号");	//9
		titles.add("店长联系方式");	//10
		titles.add("创建时间");	//11
		titles.add("更新时间");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = storebookService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("STORENAME"));	    //2
			vpd.put("var2", varOList.get(i).getString("DEVNAME"));	    //3
			vpd.put("var3", varOList.get(i).getString("REMOTENAME"));	    //4
			vpd.put("var4", varOList.get(i).getString("REMOTEUSER"));	    //5
			vpd.put("var5", varOList.get(i).getString("REMOTEPASSWORD"));	    //6
			vpd.put("var6", varOList.get(i).getString("STORETEL"));	    //7
			vpd.put("var7", varOList.get(i).getString("STOREMANAGER"));	    //8
			vpd.put("var8", varOList.get(i).getString("MANAGERNUMBER"));	    //9
			vpd.put("var9", varOList.get(i).getString("MANAGERPHONE"));	    //10
			vpd.put("var10", Convert.dateToStr(Convert.strToDate(varOList.get(i).get("CREATEDATE").toString(), new Date()),"yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00"));	    //11
			if(Tools.isEmpty(varOList.get(i).get("EDITDATE")+"")){
				vpd.put("var11", "");	    //12
			}else{
				vpd.put("var11", Convert.dateToStr(Convert.strToDate(varOList.get(i).get("EDITDATE").toString(), new Date()),"yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00"));	    //12
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
		mv.setViewName("handlecar/storebook/uploadexcel");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "storebook.xls", "storebook.xls");
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
			if(Tools.isEmpty(listPd.get(i).getString("var0")) || Tools.isEmpty(listPd.get(i).getString("var1")) || Tools.isEmpty(listPd.get(i).getString("var2")) ||Tools.isEmpty(listPd.get(i).getString("var3"))){
				continue;
			}
			p.put("STORENAME", listPd.get(i).getString("var0"));//门店
			p.put("DEVNAME", listPd.get(i).getString("var1"));//远程设备名称
			p.put("REMOTENAME", listPd.get(i).getString("var2"));//远程工具名称
			p.put("REMOTEUSER", listPd.get(i).getString("var3"));//远程账号
			p.put("REMOTEPASSWORD",listPd.get(i).getString("var4"));//远程密码
			p.put("STORETEL", listPd.get(i).getString("var5"));//固话
			p.put("STOREMANAGER", listPd.get(i).getString("var6"));//店长
			p.put("MANAGERNUMBER", listPd.get(i).getString("var7"));//店长工号
			p.put("MANAGERPHONE", listPd.get(i).getString("var8"));//店长联系方式
			p.put("CREATEDATE",new Date());//时间
			bookList.add(p);
			
		}
		storebookService.batchSave(bookList);
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
