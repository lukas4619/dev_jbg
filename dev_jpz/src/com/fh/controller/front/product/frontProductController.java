package com.fh.controller.front.product;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.product.ProductManager;
import com.fh.service.system.reservation.ReservationManager;
import com.fh.service.system.states.StatesManager;
import com.fh.service.system.states.impl.StatesService;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Constants;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.gongzhong.TemplateMessage;
import com.fh.wechat.gongzhong.vo.message.template.Template;
import com.fh.wechat.gongzhong.vo.user.UserInfo;
import com.fh.wechat.pay.Sha1Util;

/**
 * 说明：产品套餐 创建人：Lukas 18923798379 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value = "/frontProduct")
public class frontProductController extends BaseController {

	@Resource(name = "productService")
	private ProductManager productService;
	@Resource(name="reservationService")
	private ReservationManager reservationService;
	@Resource(name="statesService")
	private StatesManager statesService;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/index")
	public ModelAndView index() throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("front/product/product_list");
		return mv;
	}
	
	/**
	 * 套餐分页
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPage")
	@ResponseBody
	public Object listPage() throws Exception {
		Page page = new Page();
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		String keywords="";
		if(!Tools.isEmpty(pd.get("keywords")+"")){
			keywords = pd.getString("keywords");
		}
		pd.put("proType", -1);
		pd.put("proState", 15);
		pd.put("keywords", keywords);//
		int CurrentPage = 1;//当前页
		if(Tools.notEmpty(pd.get("CurrentPage")+"")){
			CurrentPage = Convert.strToInt(pd.get("CurrentPage")+"", CurrentPage);
		}
		page.setCurrentPage(CurrentPage);//设置当前页
		int ShowCount=10;
		if(Tools.notEmpty(pd.get("ShowCount")+"")){
			ShowCount = Convert.strToInt(pd.get("ShowCount")+"", ShowCount);
		}
		page.setShowCount(ShowCount);
		page.setPd(pd);
		List<PageData>	varList = productService.list(page);	//查询所有套餐
		map.put("type", 1);
		map.put("msg", "成功！");
		map.put("data", varList);
		map.put("totalResult", page.getTotalResult());//总条数
		map.put("totalPage", page.getTotalPage());//总页数
		return AppUtil.returnObject(pd, map);
	}
	
	 /**去详情页面
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goDetails")
		public ModelAndView goDetails()throws Exception{
			ModelAndView mv = this.getModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			String  PRODUCTID="";
			if(!Tools.isEmpty(pd.get("id")+"")){
				PRODUCTID = pd.getString("id");
			}
			pd.put("PRODUCTID", PRODUCTID);
			pd = productService.findById(pd);	//根据ID读取
			//封装用户分享参数
			String openid = getWeChatOpenId();
			String timestamp = Sha1Util.getTimeStamp();
			String nonce_str = Sha1Util.getNonceStr();
			String pageUrl = getRequertNowUrl();
			String signature = Sha1Util.createSignature(timestamp,nonce_str,pageUrl);
			String shareUrl = pageUrl+"&shareId="+openid;
			mv.addObject("timestamp", timestamp);
			mv.addObject("nonce_str", nonce_str);
			mv.addObject("signature", signature);
			mv.addObject("shareUrl", shareUrl);
			mv.addObject("openid", openid);
			mv.addObject("appId", GongZhongService.appId);
			mv.setViewName("front/product/product_details");
			mv.addObject("msg", "edit");
			mv.addObject("pd", pd);
			
			String shareId = pd.getString("shareId");
			mv.addObject("shareId", shareId);
			
			int PROVALIDITY =0;//套餐设定预订有效天数
			Date VALIDITYDATE = new Date();//预订多久有效
			if(Tools.notEmpty(pd.get("PROVALIDITY")+"")){
				  PROVALIDITY =Convert.strToInt(pd.get("PROVALIDITY")+"", PROVALIDITY);
				 Calendar   calendar   =   new   GregorianCalendar(); 
			     calendar.setTime(VALIDITYDATE); 
			     calendar.add(calendar.DATE,PROVALIDITY);//把日期往后增加VALIDITY天.整数往后推,负数往前移动 
			     VALIDITYDATE=calendar.getTime();   //这个时间就是日期往后推VALIDITY天的结果 
			     logger.info("VALIDITYDATE:"+VALIDITYDATE);
			}
			Session session = Jurisdiction.getSession();
			UserInfo  user = (UserInfo) session.getAttribute(Const.SESSION_WECHATUSER);
			mv.addObject("subscribe", user.getSubscribe());
			mv.addObject("toDay", DateUtil.getDay());
			mv.addObject("maxDay", DateUtil.getDay(VALIDITYDATE));
			
			
			
			//查询时间段列表
			PageData reservationStatePd = new PageData();
			reservationStatePd.put("stateType", "9");
			List<PageData> reservationStateIDList = statesService.listAll(reservationStatePd);
			mv.addObject("reservationStateIDList", reservationStateIDList);
			return mv;
		}	
	
		
		
		/**
		 * 套餐预订
		 * @throws Exception
		 */
		@RequestMapping(value = "/promptlyAdd")
		@ResponseBody
		public Object promptlyAdd() throws Exception {
			PageData pd = new PageData();
			Map<String, Object> map = new HashMap<String, Object>();
			pd = this.getPageData();
			String PRODUCTID ="";//预订套餐标识
			if(Tools.isEmpty(pd.get("PRODUCTID")+"")){
				map.put("type", -1);
				map.put("msg", "网络繁忙，请稍后再试！");
				return AppUtil.returnObject(pd, map);
			}
			PRODUCTID=pd.get("PRODUCTID")+"";
			String RESERVEDNUMBER ="";//预订人预留联系方式
			if(Tools.isEmpty(pd.get("RESERVEDNUMBER")+"")){
				map.put("type", -1);
				map.put("msg", "请输入联系方式！");
				return AppUtil.returnObject(pd, map);
			}
			RESERVEDNUMBER=pd.get("RESERVEDNUMBER")+"";
			String RESERVATIONNAME ="";//预订人
			if(Tools.isEmpty(pd.get("RESERVATIONNAME")+"")){
				map.put("type", -1);
				map.put("msg", "请输入联系人！");
				return AppUtil.returnObject(pd, map);
			}
			RESERVATIONNAME=pd.get("RESERVATIONNAME")+"";
			String CREATEDATE ="";//预订消费时间
			if(Tools.isEmpty(pd.get("CREATEDATE")+"")){
				map.put("type", -1);
				map.put("msg", "请预订消费时间！");
				return AppUtil.returnObject(pd, map);
			}
			CREATEDATE=pd.get("CREATEDATE")+"";
			pd = new PageData();
			pd.put("PRODUCTID", PRODUCTID);
			pd = productService.findByViweId(pd);	//根据ID读取
			if(pd==null){
				map.put("type", -1);
				map.put("msg", "网络繁忙，请稍后再试！");
				return AppUtil.returnObject(pd, map);
			}
			String PRONAME="";//预订套餐名称
			if(Tools.notEmpty(pd.get("proName")+"")){
				PRONAME = pd.get("proName")+"";
			}
			String PROCLASSNAME="";//预订套餐类型名称
			if(Tools.notEmpty(pd.get("typeName")+"")){
				PROCLASSNAME = pd.get("typeName")+"";
			}
			int  PRORESERVATIONNUM=0;//当前套餐现有预订次数
			if(Tools.notEmpty(pd.get("proReservationNum")+"")){
				PRORESERVATIONNUM =Convert.strToInt(pd.get("proReservationNum")+"", PRORESERVATIONNUM)+1;
			}
			double PROMONEY=0.00;//套餐金额
			if(Tools.notEmpty(pd.get("proMoney")+"")){
				PROMONEY = Convert.strToDouble(pd.get("proMoney")+"", PROMONEY);
			}
			double ADVANCEMONEY=0.00;//套餐预付金额
			if(Tools.notEmpty(pd.get("proAdvanceMoney")+"")){
				ADVANCEMONEY = Convert.strToDouble(pd.get("proAdvanceMoney")+"", ADVANCEMONEY);
			}
			String PROIMG ="";//套餐封面图片
			if(Tools.notEmpty(pd.get("proAdvanceMoney")+"")){
				PROIMG =  pd.get("proImg")+"";
			}
			int PROVALIDITY =0;//套餐设定预订有效天数
			Date VALIDITYDATE = new Date();//预订多久有效
			if(Tools.notEmpty(pd.get("proValidity")+"")){
				  PROVALIDITY =Convert.strToInt(pd.get("proValidity")+"", PROVALIDITY);
				 Calendar   calendar   =   new   GregorianCalendar(); 
			     calendar.setTime(VALIDITYDATE); 
			     calendar.add(calendar.DATE,PROVALIDITY);//把日期往后增加VALIDITY天.整数往后推,负数往前移动 
			     VALIDITYDATE=calendar.getTime();   //这个时间就是日期往后推VALIDITY天的结果 
			}
			long d =DateUtil.getDaySub(CREATEDATE, Convert.dateToStr(VALIDITYDATE, "1900-01-01 00:00:00"));
			if(d<0){
				map.put("type", -1);
				map.put("msg", "预订有效时间需在当天以内！");
				return AppUtil.returnObject(pd, map);
			}
			if(d>0 && PROVALIDITY<d){
				map.put("type", -1);
				if(PROVALIDITY==0){
					map.put("msg", "预订有效时间需在当天以内！");
				}else{
					map.put("msg", "预订有效时间需在"+PROVALIDITY+"已天内！");
				}
				return AppUtil.returnObject(pd, map);
			}

			PageData p = new PageData();//保存套餐修改数据
			p.put("PRORESERVATIONNUM", PRORESERVATIONNUM);
			p.put("PRODUCTID", PRODUCTID);
			PageData yd = new PageData();//保存预订数据
			String RESERVATIONID =this.get32UUID();
			String RESERVATIONNUMBER = Tools.createNumber();
			yd.put("RESERVATIONID", RESERVATIONID);
			yd.put("RESERVATIONNUMBER", RESERVATIONNUMBER);
			if(Tools.notEmpty(getWeChatOpenId())){
				yd.put("MEMBERID", getWeChatOpenId());
			}else{
				yd.put("MEMBERID", -1);
			}
			yd.put("RESERVATIONTYPE", 4);
			yd.put("RESERVATIONNAME", RESERVATIONNAME);
			yd.put("RESERVATIONSEX", 1);
			yd.put("RESERVEDNUMBER", RESERVEDNUMBER);
			yd.put("PROCLASSNAME", PROCLASSNAME);
			yd.put("PRODUCTID", PRODUCTID);
			yd.put("PRONAME", PRONAME);
			yd.put("PROMONEY", PROMONEY);
			yd.put("ADVANCEMONEY", ADVANCEMONEY);
			yd.put("PROIMG", PROIMG);
			yd.put("CREATEDATE", CREATEDATE);
			yd.put("RESERVATIONSTATEID", 12);
			yd.put("AUTHORREVENUE", 0.00);
			yd.put("SHAREREVENUE", 0.00);
			yd.put("VALIDITYDATE", VALIDITYDATE);
			yd.put("STATEDATE", new Date());
			yd.put("STATEREMARKS", "");
			yd.put("PAYSTATE", "0");
			yd.put("PAYDATE", null);
			yd.put("shareId", "-1");
			reservationService.save(yd,pd);
			if(!getRequertUrl().contains("localhost") && !getRequertUrl().contains("ddj.java.1yg.tv")){
				List<Template> templates = new ArrayList<Template>();
				templates = TemplateMessage.setTemplate(templates, "#173177", "first",getWeChatName()+",您已成功预订套餐："+PRONAME+"，请尽快付款。");
				templates = TemplateMessage.setTemplate(templates, "#173177", "productType",PROCLASSNAME);
				templates = TemplateMessage.setTemplate(templates, "#173177", "name",PRONAME);
				templates = TemplateMessage.setTemplate(templates, "#173177", "number","1");
				templates = TemplateMessage.setTemplate(templates, "#173177", "expDate",Convert.dateToStr(VALIDITYDATE, "yyyy-MM-dd", "1900-01-01"));
				String url =getRequertUrl()+"myReservation/goDetails?id="+RESERVATIONID;
				TemplateMessage.sendTemplateMessage(getWeChatOpenId(), Constants.PRODUCT_RESERVE_TEMPLATE_ID, url, Constants.PRODUCT_RESERVE_TOPCOLOR, templates);
			}
			map.put("type", 1);
			map.put("msg", "成功！");
			map.put("data", RESERVATIONID);
			return AppUtil.returnObject(pd, map);
		}
		
		
		
		 /**去套餐预订支付页面
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/goPayment")
		public ModelAndView goPayment()throws Exception{
			ModelAndView mv = this.getModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			String  RESERVATIONID="";
			if(!Tools.isEmpty(pd.get("id")+"")){
				RESERVATIONID = pd.getString("id");
			}
			pd.put("RESERVATIONID", RESERVATIONID);
			pd = reservationService.findById(pd);	//根据ID读取
			//封装用户分享参数
			String openid = getWeChatOpenId();
			String timestamp = Sha1Util.getTimeStamp();
			String nonce_str = Sha1Util.getNonceStr();
			String pageUrl = getRequertNowUrl();
			String signature = Sha1Util.createSignature(timestamp,nonce_str,pageUrl);
			String shareUrl = pageUrl+"&shareId="+openid;
			pd.put("wxtimestamp", timestamp);
			pd.put("wxnonce_str", nonce_str);
			pd.put("wxsignature", signature);
			pd.put("shareUrl", shareUrl);
			pd.put("appId", GongZhongService.appId);
			String TotalFee ="";
			if(Tools.notEmpty(pd.get("ADVANCEMONEY")+"")){
//				TotalFee = Tools.getMoney(pd.get("ADVANCEMONEY")+"");
				TotalFee = Tools.getMoney("0.01");
			}
			String reservationNumber ="";
			if(Tools.notEmpty(pd.get("RESERVATIONNUMBER")+"")){
				reservationNumber = pd.get("RESERVATIONNUMBER")+"";
			}
            if(Tools.notEmpty(TotalFee) && Tools.notEmpty(reservationNumber)){
            	JSONObject j = null;
				try {
					String packageUrl = Sha1Util.weChatPay("大东江美食", getWeChatOpenId(), TotalFee, reservationNumber,
							"2", openid, PublicUtil.getIp(),getRequertUrl());
					j = JSONObject.fromObject(packageUrl);
				} catch (Exception e) {
					logger.info("预订发起支付请求异常："+e.toString());
					e.printStackTrace();
				}
    			if (j != null) {
    				pd.put("timeStamp", j.get("timeStamp").toString());
    				pd.put("nonceStr", j.get("nonceStr").toString());
    				pd.put("packages", j.get("package").toString());
    				pd.put("signType", j.get("signType").toString());
    				pd.put("paySign", j.get("paySign").toString());
    			}
            }
			mv.setViewName("front/product/weChatPay");
			mv.addObject("msg", "goPay");
			mv.addObject("pd", pd);
			return mv;
		}	
		
		
		
		
		
		
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}
}
