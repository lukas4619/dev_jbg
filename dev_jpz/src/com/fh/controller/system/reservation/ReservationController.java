package com.fh.controller.system.reservation;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.fh.util.Constants;
import com.fh.util.Convert;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.TemplateMessage;
import com.fh.wechat.gongzhong.vo.message.template.Template;
import com.fh.service.system.product.ProductManager;
import com.fh.service.system.reservation.ReservationManager;
import com.fh.service.system.states.StatesManager;
import com.fh.service.system.types.TypesManager;

/** 
 * 说明：预订记录
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-17
 */
@Controller
@RequestMapping(value="/reservation")
public class ReservationController extends BaseController {
	
	String menuUrl = "reservation/list.do"; //菜单地址(权限用)
	@Resource(name="reservationService")
	private ReservationManager reservationService;
	@Resource(name = "statesService")
	private StatesManager statesService;
	@Resource(name = "typesService")
	private TypesManager typesService;
	
	@Resource(name = "productService")
	private ProductManager productService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Reservation");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
//		pd = this.getPageData();
//		pd.put("RESERVATIONID", this.get32UUID());	//主键
//		reservationService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Reservation");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		reservationService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Reservation");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		int RESERVATIONSTATEID = pd.getInt("RESERVATIONSTATEID");//45已经完成
		String reservationID=pd.get("RESERVATIONID")+"";
		String stateRemarks = pd.getString("STATEREMARKS");
		int payState = pd.getInt("PAYSTATE");//付款状态;
		if(Tools.notEmpty(stateRemarks)){
			pd.put("STATEREMARKS",stateRemarks);
		}
		if(pd.get("RESERVATIONSTATEID").toString().equals("45") && !pd.get("PAYSTATE").toString().equals("1")){
			pd.put("PAYDATE", new Date());
			pd.put("PAYSTATE", 1);
		}
		reservationService.edit(pd);
		//TODO 增加预订进度模版消息
		pd = new PageData();
		pd.put("reservationID", reservationID);
		pd=reservationService.findByViewId(pd);
		List<Template> templates = new ArrayList<Template>();
		Date VALIDITYDATE = new Date();
		if(Tools.notEmpty(pd.get("validityDate")+"")){
			VALIDITYDATE = Convert.strToDate(pd.get("validityDate")+"", VALIDITYDATE);
		}
		String openId = pd.get("openId")+"";
		if(Tools.notEmpty(openId)){
			if(!getRequertUrl().contains("localhost") && !getRequertUrl().contains("ddj.java.1yg.tv")){
				String content=pd.get("weChatName")+",您预订编号："+pd.get("reservationNumber")+"，预订状态为："+pd.get("stateName")+",点击查看详情。";
				templates = TemplateMessage.setTemplate(templates, "#173177", "first",content);
				templates = TemplateMessage.setTemplate(templates, "#173177", "productType",pd.get("typeName")+"");
				templates = TemplateMessage.setTemplate(templates, "#173177", "name",pd.get("proName")+"");
				templates = TemplateMessage.setTemplate(templates, "#173177", "number","1");
				templates = TemplateMessage.setTemplate(templates, "#173177", "expDate",Convert.dateToStr(VALIDITYDATE, "yyyy-MM-dd", "1900-01-01"));
				String url =getRequertUrl()+"myReservation/goDetails?id="+reservationID;
				TemplateMessage.sendTemplateMessage(openId, Constants.PRODUCT_RESERVE_TEMPLATE_ID, url, Constants.PRODUCT_RESERVE_TOPCOLOR, templates);
				mv.addObject("msg","success");
			}
			
		}
		String txno = pd.getString("reservationNumber");
		logger.info("RESERVATIONSTATEID:"+RESERVATIONSTATEID+" 支付状态 0未付款,1已经付款payState:"+payState);
		if(RESERVATIONSTATEID==45&&payState!=1){
			//RESERVATIONSTATEID==45已经完成 并且是未付款的状态
			//添加预定收益 和修改支付状态与商品成功预定的数量
			logger.info("进入分享预定...................");
			PageData rpd = new PageData();
			rpd.put("RESERVATIONNUMBER", txno); 
			rpd = reservationService.findByIdNumber(rpd);
			logger.info("订单号是.........."+txno);
			int ARTICLEID = rpd.getInt("ARTICLEID"); 
			logger.info("文章的主键id是........."+ARTICLEID);
			String shareId = rpd.getString("SHAREID");//得到分享者的会员id
			Double shareRevenue = rpd.getDouble("SHAREREVENUE");//分享者获得金额
			Double authorRevenue = rpd.getDouble("AUTHORREVENUE");//作者收益
			String AUTHORID = rpd.getString("AUTHORID");//作者id;
			logger.info("分享者shareId"+shareId+" 分享者获得金额"+shareRevenue+" 作者收益"+authorRevenue);
			if(rpd!=null){
				int PAYSTATE =0;//付款状态 0.等待付款 1.已付款 2.以取消
				if(Tools.notEmpty(rpd.get("PAYSTATE")+"")){
					//PAYSTATE = Convert.strToInt(rpd.get("PAYSTATE")+"", PAYSTATE);
					String PRODUCTID = "";//当前预订套餐标识
					if(Tools.notEmpty(rpd.get("PRODUCTID")+"")){
						PRODUCTID = rpd.get("PRODUCTID")+"";
					}
					String RESERVATIONID="";//预订编号
					if(Tools.notEmpty(rpd.get("RESERVATIONID")+"")){
						RESERVATIONID = rpd.get("RESERVATIONID")+"";
					}
					String PRONAME="";//套餐名称
					if(Tools.notEmpty(rpd.get("PRONAME")+"")){
						PRONAME = rpd.get("PRONAME")+"";
					}
					double ADVANCEMONEY=0.00;//预付金额
					if(Tools.notEmpty(rpd.get("ADVANCEMONEY")+"")){
						ADVANCEMONEY =Convert.strToDouble(rpd.get("ADVANCEMONEY")+"",ADVANCEMONEY);
					}
					
						rpd = new PageData();
						rpd.put("PRODUCTID", PRODUCTID);
						rpd = productService.findById(rpd);//查询当前套餐基本信息
						int PROCONSUMENUM=0;//当前套餐预订成功消费次数
						if(Tools.notEmpty(rpd.get("PROCONSUMENUM")+"")){
							PROCONSUMENUM = Convert.strToInt(rpd.get("PROCONSUMENUM")+"", PROCONSUMENUM)+1 ;
			    		}
						rpd = new PageData();//保存回写预订状态数据
						rpd.put("ARTICLEID", ARTICLEID);
						rpd.put("shareId", shareId);
						rpd.put("shareRevenue", shareRevenue);
						rpd.put("authorRevenue", authorRevenue);
						rpd.put("AUTHORID", AUTHORID);
						rpd.put("PAYSTATE", 1);
						rpd.put("RESERVATIONTYPE", 3);//3.预付定金 4.非预付定金，请见t_types表
						rpd.put("PAYDATE", new Date());
						rpd.put("STATEDATE", new Date());
						rpd.put("RESERVATIONNUMBER", txno);
						PageData p = new PageData();//保存回写套餐预订成功消费次数数据
						p.put("PROCONSUMENUM", PROCONSUMENUM);
						p.put("PRODUCTID", PRODUCTID);
						reservationService.editPayStateAndAddMoney(rpd,p);
						logger.info("预订编号"+txno+",回写成功,PAYSTATE:"+PAYSTATE);
					
				}
			}
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Reservation");
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
		if(!Tools.isEmpty(pd.get("memberId")+"")){
			pd.put("memberId",pd.get("memberId")+"");
		}
		pd.put("reservationType", Convert.strToInt(pd.get("reservationType")+"", -1));
		pd.put("reservationStateID", Convert.strToInt(pd.get("reservationStateID")+"", -1));
		page.setPd(pd);
		List<PageData>	varList = reservationService.list(page);	//列出Reservation列表
		List<PageData> reservationTypeList = new ArrayList<PageData>();
		List<PageData> reservationStateIDList = new ArrayList<PageData>();
		pd.put("typeClass", "3");
		reservationTypeList = typesService.listAll(pd);
		mv.addObject("reservationTypeList", reservationTypeList);
		pd.put("stateType", "1");
		reservationStateIDList = statesService.listAll(pd);
		mv.addObject("reservationStateIDList", reservationStateIDList);
		mv.setViewName("system/reservation/reservation_list");
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
		mv.setViewName("system/reservation/reservation_edit");
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
		pd = reservationService.findById(pd);	//根据ID读取
		List<PageData> reservationTypeList = new ArrayList<PageData>();
		List<PageData> reservationStateIDList = new ArrayList<PageData>();
		pd.put("typeClass", "3");
		reservationTypeList = typesService.listAll(pd);
		mv.addObject("reservationTypeList", reservationTypeList);
		pd.put("stateType", "1");
		reservationStateIDList = statesService.listAll(pd);
		mv.addObject("reservationStateIDList", reservationStateIDList);
		mv.setViewName("system/reservation/reservation_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Reservation");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			reservationService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Reservation到excel");
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
		pd.put("reservationType", Convert.strToInt(pd.get("reservationType")+"", -1));
		pd.put("reservationStateID", Convert.strToInt(pd.get("reservationStateID")+"", -1));
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("预订编号");	//1
		titles.add("预订方式");	//2
		titles.add("预订人");	//3
		titles.add("预留联系方式");	//4
		titles.add("产品名称");	//5
		titles.add("产品金额");	//6
		titles.add("预付金额");	//7
		titles.add("预订时间");	//8
		titles.add("预订有效期");	//9
		titles.add("预订状态");	//10
		titles.add("变更时间");	//11
		titles.add("备注");	//12
		dataMap.put("titles", titles);
		List<PageData> varOList = reservationService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("reservationNumber"));	    //1
			vpd.put("var2", varOList.get(i).get("typeName"));	//2
			int sex = Convert.strToInt(varOList.get(i).get("reservationSex").toString(), 1);
			String str="先生";
			if(sex==2){
				str="女士";
			}
			vpd.put("var3", varOList.get(i).get("reservationName")+"("+str+")");	    //3
			vpd.put("var4", varOList.get(i).get("reservedNumber"));	    //4
			vpd.put("var5", varOList.get(i).get("proName"));	    //5
			vpd.put("var6", varOList.get(i).get("proMoney")+"");	//6
			vpd.put("var7", varOList.get(i).get("advanceMoney")+"");	//7
			vpd.put("var8", varOList.get(i).get("createDate")+"");	    //8
			vpd.put("var9", varOList.get(i).get("validityDate")+"");	    //9
			vpd.put("var10", varOList.get(i).get("typeName"));	    //10
			vpd.put("var11", varOList.get(i).get("stateDate")+"");	    //11
			vpd.put("var12", varOList.get(i).get("stateRemarks"));	    //12
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
