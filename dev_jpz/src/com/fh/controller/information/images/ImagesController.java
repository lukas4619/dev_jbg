package com.fh.controller.information.images;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

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
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.service.information.images.ImagesManager;
import com.fh.service.system.types.TypesManager;

/** 
 * 说明：图片库
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-19
 */
@Controller
@RequestMapping(value="/images")
public class ImagesController extends BaseController {
	
	String menuUrl = "images/list.do"; //菜单地址(权限用)
	@Resource(name="imagesService")
	private ImagesManager imagesService;
	@Resource(name = "typesService")
	private TypesManager typesService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Images");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("IMGID", this.get32UUID());	//主键
		pd.put("CREATEDATE", new Date());
		pd.put("EDITDATE", new Date());
		imagesService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Images");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		imagesService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Images");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		imagesService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Images");
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
		pd.put("isUpload", Convert.strToInt(pd.get("isUpload")+"", -1));
		pd.put("imgType", Convert.strToInt(pd.get("imgType")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = imagesService.list(page);	//列出Images列表
		List<PageData> typeList = new ArrayList<PageData>();
		pd.put("typeClass", "7");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		mv.setViewName("information/images/images_list");
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
		List<PageData> typeList = new ArrayList<PageData>();
		pd.put("typeClass", "7");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		mv.setViewName("information/images/images_edit");
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
		pd = imagesService.findById(pd);	//根据ID读取
		List<PageData> typeList = new ArrayList<PageData>();
		pd.put("typeClass", "7");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		mv.setViewName("information/images/images_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Images");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			imagesService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	
		/**上传到微信服务器
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/imagesUpLoad")
		@ResponseBody
		public Object imagesUpLoad() throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"上传到微信服务器Images");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
			PageData pd = new PageData();		
			Map<String,Object> map = new HashMap<String,Object>();
			List<PageData> pdList = new ArrayList<PageData>();
			pd = this.getPageData();
			pdList.add(pd);
//			map.put("list", pdList);
			PageData p = new PageData();		
			p = imagesService.findById(pd);	//根据ID读取
//			String filePath =getPath()+"/uploadFiles/uploadImgs/"+p.getString("IMGPATH");
//			logger.info("filePath--------------"+filePath);
//			JSONObject j = new JSONObject();
//			j =GongZhongService.uploadMaterial("image", filePath);
//			if(!j.getString("type").equals("1")){
//				logger.info("上传到微信服务器Images："+j.get("msg"));
//				pd.put("type", -1);
//				pd.put("msg", j.get("msg"));
//				return AppUtil.returnObject(pd, map);
//			}
//			if(j.get("media_id") == null){
//				logger.info("上传到微信服务器Images："+j.get("msg"));
//				pd.put("type", -1);
//				pd.put("msg", j.get("msg"));
//				return AppUtil.returnObject(pd, map);
//			}
//			p.put("IMGID", pd.get("IMGID"));
//			p.put("MEDIA_ID", j.get("media_id"));
//			p.put("ISUPLOAD", 1);
//			p.put("EDITDATE", new Date());
//			imagesService.editUpLoad(p);
			map.put("type", 1);
			map.put("msg", "ok");
			return AppUtil.returnObject(pd, map);
		}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出Images到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("32位标识");	//1
		titles.add("图片库类型");	//2
		titles.add("标题");	//3
		titles.add("描述");	//4
		titles.add("保存路径");	//5
		titles.add("分组名称");	//6
		titles.add("图片归属");	//7
		titles.add("图片微信标识");	//8
		titles.add("是否上传微信服务器");	//9
		titles.add("排序");	//10
		titles.add("备注");	//11
		titles.add("创建时间");	//12
		titles.add("编辑时间");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = imagesService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("IMGID"));	    //1
			vpd.put("var2", varOList.get(i).get("IMGTYPE").toString());	//2
			vpd.put("var3", varOList.get(i).getString("IMGTITLE"));	    //3
			vpd.put("var4", varOList.get(i).getString("IMGDESCRIPTION"));	    //4
			vpd.put("var5", varOList.get(i).getString("IMGPATH"));	    //5
			vpd.put("var6", varOList.get(i).getString("IMGGROUPNAME"));	    //6
			vpd.put("var7", varOList.get(i).getString("MASTERID"));	    //7
			vpd.put("var8", varOList.get(i).getString("MEDIA_ID"));	    //8
			vpd.put("var9", varOList.get(i).get("ISUPLOAD").toString());	//9
			vpd.put("var10", varOList.get(i).get("IMGSORT").toString());	//10
			vpd.put("var11", varOList.get(i).getString("IMGREMARK"));	    //11
			vpd.put("var12", varOList.get(i).getString("CREATEDATE"));	    //12
			vpd.put("var13", varOList.get(i).getString("EDITDATE"));	    //13
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
