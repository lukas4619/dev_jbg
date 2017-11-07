package com.fh.controller.system.order;

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
import com.fh.service.system.cproperty.CpropertyManager;
import com.fh.service.system.order.OrderManager;
import com.fh.service.system.store.StoreManager;

/** 
 * 说明：订单管理
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-22
 */
@Controller
@RequestMapping(value="/order")
public class OrderController extends BaseController {
	
	String menuUrl = "order/list.do"; //菜单地址(权限用)
	@Resource(name="orderService")
	private OrderManager orderService;
	@Resource(name="storeService")
	private StoreManager storeService;
	@Resource(name="cpropertyService")
	private CpropertyManager cpropertyService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Order");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//pd.put("ORDER_ID", this.get32UUID());	//主键
		orderService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Order");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		orderService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Order");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//orderService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Order");
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
		pd.put("payMentStatusID", Convert.strToInt(pd.get("payMentStatusID")+"", -1));
		pd.put("orderStatusID", Convert.strToInt(pd.get("orderStatusID")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = orderService.list(page);	//列出Order列表
		mv.setViewName("system/order/order_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		List<PageData> storeList = new ArrayList<PageData>();//列出门店列表
		pd=new PageData();
		storeList = storeService.listAll(pd);
		mv.addObject("storeList", storeList);
		List<PageData> varcList = new ArrayList<PageData>();
		pd=new PageData();
		pd.put("CID", "9,10".split(","));
		varcList = cpropertyService.findAllByCId(pd);
		if(varcList!=null && varcList.size()>0){
			List<PageData> orderStatusList = new ArrayList<PageData>();//订单状态集合
			List<PageData> payMentStatusList = new ArrayList<PageData>();//付款状态集合
			for (Iterator iterator = varcList.iterator(); iterator.hasNext();) {
				PageData pageData = (PageData) iterator.next();
				int CID =pageData.getInt("CID");
				if(CID==9){//订单状态
					orderStatusList.add(pageData);
				}else if(CID==10){//付款状态
					payMentStatusList.add(pageData);
				}
				
			}
			mv.addObject("orderStatusList", orderStatusList);
			mv.addObject("payMentStatusList", payMentStatusList);
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
		mv.setViewName("system/order/order_edit");
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
		pd = orderService.findById(pd);	//根据ID读取
		mv.setViewName("system/order/order_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Order");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			orderService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Order到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
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
		pd.put("payMentStatusID", Convert.strToInt(pd.get("payMentStatusID")+"", -1));
		pd.put("orderStatusID", Convert.strToInt(pd.get("orderStatusID")+"", -1));
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("订单编号");	//1
		titles.add("订单状态");	//2
		titles.add("付款状态");	//3
		titles.add("商品数量");	//4
		titles.add("订单金额");	//5
		titles.add("付款时间");	//6
		titles.add("订单提交时间");	//7
		titles.add("所属门店");	//8
		titles.add("下单人");	//9
		titles.add("联系电话");	//10
		titles.add("备注");	//11
		dataMap.put("titles", titles);
		List<PageData> varOList = orderService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("BILLNO"));	    //1
			vpd.put("var2", varOList.get(i).get("STATUSNAME"));	//2
			vpd.put("var3", varOList.get(i).get("PAYSTATUSNAME"));	//3
			vpd.put("var4", varOList.get(i).get("PLUCOUNT"));	//4
			vpd.put("var5", varOList.get(i).get("TOTALPRICE"));	    //5
			vpd.put("var6", varOList.get(i).get("PAYMENTDATE"));	    //6
			vpd.put("var7", varOList.get(i).get("CREATEDATE"));	    //7Convert.dateToStr(Convert.strToDate(varOList.get(i).get("CREATEDATE").toString(), new Date()),"yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00")
			vpd.put("var8", varOList.get(i).get("STORENAME"));	//8
			vpd.put("var9", varOList.get(i).get("WECHATNAME"));	//9
			vpd.put("var10", varOList.get(i).get("PHONENUMBER"));	//10
			vpd.put("var11", varOList.get(i).get("REMARKS"));	    //11
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
