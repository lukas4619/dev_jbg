package com.fh.controller.system.t_menu;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import net.sf.json.JSONArray;
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
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.gongzhong.utils.GongZhongUtils;
import com.fh.service.system.t_menu.T_menuManager;

/** 
 * 说明：微信菜单模块
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-21
 */
@Controller
@RequestMapping(value="/t_menu")
public class T_menuController extends BaseController {
	
	String menuUrl = "t_menu/tmenu_ztreeMenu.do"; //菜单地址(权限用)
	@Resource(name="t_menuService")
	private T_menuManager t_menuService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增T_menu");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Long MaxId = (t_menuService.findMaxId(pd).get("MID"))==null?0:Long.parseLong((t_menuService.findMaxId(pd).get("MID").toString()));
		pd.put("ID",MaxId+1);
		pd.put("PID", pd.getString("PID"));
		pd.put("RELEASED_TIME", new Date());
		t_menuService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除T_menu");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		t_menuService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改T_menu");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("UPDATE_TIME", new Date());
		t_menuService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 微信菜单列表
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/tmenu_ztreeMenu")
	public ModelAndView ZtreeMenu() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表ZtreeMenu");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd=this.getPageData();
		List<PageData>	dataList = t_menuService.listAll(pd);	//列出T_menu列表
		JSONArray arry = new JSONArray();
		if(dataList!=null&& dataList.size()>0){
			for(PageData data:dataList){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("id", data.get("ID"));
				map.put("pId", data.get("PID"));
				map.put("name", data.get("NAME"));
				map.put("tpid", data.get("PID"));
				arry.add(map);
			}
		}
		if(Tools.isEmpty(pd.getString("pid"))){
			pd.put("pid", 1);
		}
		mv.setViewName("system/t_menu/t_menu_Ztree");
		mv.addObject("zTreeNodes", arry);
		mv.addObject("pd",pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	@RequestMapping(value="/getMenuSumByParentId")
	@ResponseBody
	public Object getMenuSumByParentId() throws Exception{
		PageData pd = new PageData();
		pd=this.getPageData();
		pd = t_menuService.getMenuSumByParentId(pd);
		JSONObject obj = new JSONObject();
		obj.put("sum", Integer.parseInt(pd.get("total").toString()));
		return obj;
	}
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表T_menu");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String pid= pd.getString("PID");
		if(!Tools.isEmpty(pid)){
			pd.put("pid", pid.trim());
		}
		String lastStart = pd.getString("lastStart");
		if(!Tools.isEmpty(lastStart)){
			pd.put("lastStart", lastStart.trim()+" 00:00:00");
		}
		String lastEnd = pd.getString("lastEnd");
		if(!Tools.isEmpty(lastEnd)){
			pd.put("lastEnd", lastEnd.trim()+" 23:59:59");
		}
		page.setPd(pd);
		List<PageData>	varList = t_menuService.list(page);	//列出T_menu列表
		mv.setViewName("system/t_menu/t_menu_list");
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
		mv.setViewName("system/t_menu/t_menu_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	/**
	 * 删除一级菜单和他下面的子菜单
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="deleteParentAndAllChild")
	@ResponseBody
	public Object deleteParentAndAllChild() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"deleteParentAndAllChild");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		t_menuService.deleteParentAndAllChildById(pd);
		return AppUtil.returnObject(pd, map);
	}
	
	@RequestMapping(value="setMenu")
	@ResponseBody
	public Object setlistMenu(Page page){
		logBefore(logger, Jurisdiction.getUsername()+"setMenu");
		JSONObject obj = new JSONObject();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("pid", 0+"");
		page.setPd(pd);
		Map<String,Object> buttonMap = new HashMap<String,Object>();
		JSONArray arry = new JSONArray();;
		try {
			List<PageData>	varList = t_menuService.list(page);	//列出T_menu列表
			if(varList!=null&&varList.size()>0){
				for(PageData data:varList){
					pd.put("pid", data.get("ID"));
					page.setPd(pd);
					List<PageData>	childs = t_menuService.list(page);	//列出childs列表
					Map<String,Object> subMap= new HashMap<String,Object>();//有二级菜单的map
					if(childs!=null&&childs.size()>=0){
						if(childs.size()>=1){
							//说明此菜单下有二级菜单,那么此一级菜单的格式有 sub_button属性
							JSONArray subArry = new JSONArray();;
							for(PageData child:childs){
								Map<String,Object> map = resultMenuMap(child);
								subArry.add(map);
							}
							subMap.put("name", data.get("NAME").toString());
							subMap.put("sub_button", subArry);
							arry.add(subMap);
						}else{
							//没有二级菜单,对象属性没有sub_button
							Map<String,Object> map = resultMenuMap(data);
							arry.add(map);
						}
					}
					
				}
				buttonMap.put("button", arry);
			}
			obj.put("msg", "success");
			obj.put("code", 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("msg", "false");
			obj.put("code", 2);
			logBefore(logger, "设置菜单的查询方法出错setMenu");
			e.printStackTrace();
			
		}
		System.out.println(JSONObject.fromObject(buttonMap));
		logBefore(logger, "菜单的json格式"+buttonMap);
		/**
		 * 调用微信设置接口
		 */
		try {
			GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + 
				      GongZhongService.getAccessToken(),JSONObject.fromObject(buttonMap).toString());
		} catch (Exception e) {
			// TODO: handle exception
			obj.put("msg", "false");
			obj.put("code", 2);
			e.printStackTrace();
			logBefore(logger, "设置菜单调用微信的接口出错setMenu");
		}
		return obj;
	}
	
	/**
	 * 删除微信菜单
	 * @return
	 */
	@RequestMapping(value="/clearMenu")
	@ResponseBody
	public JSONObject clearMenu(){
		JSONObject obj = new JSONObject();
		/**
		 * 调用微信设置接口
		 */
		try {
			 GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/menu/delete?", 
				      "access_token=" + GongZhongService.getAccessToken());
		} catch (Exception e) {
			// TODO: handle exception
			obj.put("msg", "false");
			obj.put("code", 2);
			e.printStackTrace();
			logBefore(logger, "设置菜单调用微信的接口出错setMenu");
		}
		obj.put("msg", "success");
		obj.put("code", 1);
		return obj;
	}
	
	/**
	 * 封装查询的菜单对象数据
	 * @return map  返回map
	 * @param data 数据库查询的数据
	 */
	
	public Map resultMenuMap(PageData data){
		Map<String,Object> map = new HashMap<String,Object>();
		String type = data.get("TYPE").toString();
		map.put("type", type);
		map.put("name", data.get("NAME").toString());
		if(type.equals("click")){
			map.put("key", data.get("KEY_TYPE").toString());
		}else{
			map.put("url",data.get("URL").toString());
		}
		return map;
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
		pd = t_menuService.findById(pd);	//根据ID读取
		mv.setViewName("system/t_menu/t_menu_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除T_menu");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String str = pd.getString("str");
		if(null != str && !"".equals(str)){
			String ArrayDATA_IDS[] = str.split(",");
			t_menuService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出T_menu到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("ID");	//1
		titles.add("父类ID");	//2
		titles.add("响应类型");	//3
		titles.add("菜单标题");	//4
		titles.add("KEY类型");	//5
		titles.add("链接地址");	//6
		titles.add("创建时间");	//7
		titles.add("修改时间");	//8
		titles.add("排序");	//9
		titles.add("推送内容");	//10
		dataMap.put("titles", titles);
		List<PageData> varOList = t_menuService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString());	//1
			vpd.put("var2", varOList.get(i).get("PID").toString());	//2
			vpd.put("var3", varOList.get(i).getString("TYPE"));	    //3
			vpd.put("var4", varOList.get(i).getString("NAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("KEY_TYPE"));	    //5
			vpd.put("var6", varOList.get(i).getString("URL"));	    //6
			vpd.put("var7", varOList.get(i).getString("RELEASED_TIME"));	    //7
			vpd.put("var8", varOList.get(i).getString("UPDATE_TIME"));	    //8
			vpd.put("var9", varOList.get(i).get("SORT").toString());	//9
			vpd.put("var10", varOList.get(i).getString("CONTENT"));	    //10
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
	
	public static void main(String[] args) {
		System.out.println("11");
	}
}
