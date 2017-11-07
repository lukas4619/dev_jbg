package com.fh.controller.front.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.HttpRequest;
import org.apache.shiro.session.Session;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.stat.TableStat.Mode;
import com.fh.controller.base.BaseController;
import com.fh.service.system.article.ArticleManager;
import com.fh.service.system.likerecorde.LikeRecordeManager;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.member.impl.MemberService;
import com.fh.service.system.product.ProductManager;
import com.fh.service.system.reservation.ReservationManager;
import com.fh.service.system.states.StatesManager;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Constants;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.util.UuidUtil;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.gongzhong.TemplateMessage;
import com.fh.wechat.gongzhong.vo.message.template.Template;
import com.fh.wechat.gongzhong.vo.user.UserInfo;
import com.fh.wechat.pay.HttpUtil;
import com.fh.wechat.pay.RequestHandler;
import com.fh.wechat.pay.Sha1Util;
import com.fh.wechat.pay.TenpayUtil;


/** 
 * 说明：软文管理
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value="/news")
public class NewsController extends BaseController {
	
	@Resource(name="articleService")
	private ArticleManager articleService;
	@Resource(name="likerecordeService")
	private LikeRecordeManager likerecordeService;
	@Resource(name="reservationService")
	private ReservationManager reservationService;
	@Resource(name="productService")
	private ProductManager productService;
	@Resource(name="memberService")
	private MemberManager memberService;
	@Resource(name="statesService")
	private StatesManager statesService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse r)
			throws Exception {
		long startTime = System.currentTimeMillis();
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd.put("ARTICLEID", request.getParameter("id"));
		pd=articleService.findById(pd);
		System.out.println(pd.getString("PRODUCTID"));
		//根据产品id查询产品信息;
		PageData productPd = new PageData();
		productPd.put("PRODUCTID", pd.getString("PRODUCTID"));
		productPd = productService.findById(productPd);
		
		//查询此文章是否被点当前用户赞过
		PageData likePd = new PageData();
		Session session = Jurisdiction.getSession();
		UserInfo  user = (UserInfo) session.getAttribute(Const.SESSION_WECHATUSER);
		likePd.put("OPENID", user.getOpenid());
		likePd.put("ARTICLEID", request.getParameter("id").trim());
		likePd = likerecordeService.findByOpenIdAndArticleId(likePd);
		if(likePd!=null){
			mv.addObject("islike", 1);
		}
		mv.addObject("msg", "成功");
		mv.addObject("pd", pd);
		mv.setViewName("front/news/article_details");
		
		String shareId = request.getParameter("shareId");
		mv.addObject("shareId", shareId);
		
		//封装用户分享参数
		String timestamp = Sha1Util.getTimeStamp();
		String nonce_str = Sha1Util.getNonceStr();
		String pageUrl = getRequertNowUrl();
		String signature = createSignature(timestamp,nonce_str,pageUrl);
		String shareUrl = pageUrl+"&shareId="+user.getOpenid();
		
		int PROVALIDITY =0;//套餐设定预订有效天数
		Date VALIDITYDATE = new Date();//预订多久有效
		
		if(productPd!=null&&Tools.notEmpty(productPd.get("PROVALIDITY")+"")){
			  PROVALIDITY =Convert.strToInt(productPd.get("PROVALIDITY")+"", PROVALIDITY);
			 Calendar   calendar   =   new   GregorianCalendar(); 
		     calendar.setTime(VALIDITYDATE); 
		     calendar.add(calendar.DATE,PROVALIDITY);//把日期往后增加VALIDITY天.整数往后推,负数往前移动 
		     VALIDITYDATE=calendar.getTime();   //这个时间就是日期往后推VALIDITY天的结果 
		     logger.info("VALIDITYDATE:"+VALIDITYDATE);
		}
		
		mv.addObject("toDay", DateUtil.getDay());
		mv.addObject("maxDay", DateUtil.getDay(VALIDITYDATE));
		
		//查询时间段列表
		PageData reservationStatePd = new PageData();
		reservationStatePd.put("stateType", "9");
		List<PageData> reservationStateIDList = statesService.listAll(reservationStatePd);
		
		mv.addObject("subscribe", user.getSubscribe());
		mv.addObject("timestamp", timestamp);
		mv.addObject("nonce_str", nonce_str);
		mv.addObject("signature", signature);
		mv.addObject("shareUrl", shareUrl);
		mv.addObject("openid", user.getOpenid());
		mv.addObject("appId", GongZhongService.appId);
		mv.addObject("productPd", productPd);//产品信息
		mv.addObject("reservationStateIDList", reservationStateIDList);
		long endTime = System.currentTimeMillis();
		logger.info("时间:"+(endTime-startTime));
		return mv;
	}
	
	@RequestMapping(value="/checkDateDisable")
	@ResponseBody
	public Map checkDateDisable() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuffer sb= new StringBuffer();
		List<PageData> reservationStateIDList=null;
		PageData pd = this.getPageData();
		String date = pd.getString("date");
		if(DateUtil.getDay().equals(date)){
			//是今天
			//查询stateType=9的时间点
			pd.put("stateType", "9");
			reservationStateIDList = statesService.listAll(pd);
			for(PageData state:reservationStateIDList){
				String reservateTime = state.getString("NAME");
				if(DateUtil.comparaDateTime(reservateTime)==1){
					//说明现在的时间点 reservateTime不可以选择
					sb.append(state.getInt("ID")+",");
				}else{
				}
			}
			map.put("type", 1);
		}else{
			//不是今天
			map.put("type", 2);
		}
		
		map.put("msg", "成功！");
		map.put("data", sb);
		
		
		return map;
	}
	/**
	 * 用户阅读 添加数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/addReadNum")
	@ResponseBody
	public String addReadNum(HttpServletRequest request, HttpServletResponse response){
		JSONObject obj = new JSONObject();
		Session session = Jurisdiction.getSession();
		UserInfo  user = (UserInfo) session.getAttribute(Const.SESSION_WECHATUSER);
		System.out.println(user+"");
		String ARTICLEID = request.getParameter("ARTICLEID");
		String shareId = request.getParameter("shareId");
		try {
			articleService.addReadNumAndMoney(user,ARTICLEID,shareId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			obj.put("code", 0);
		}
		obj.put("code", 1);
		return obj.toString();
	}	
	
	/**
	 * 点赞添加数量和作者金额
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="addLikeNum")
	@ResponseBody
	public String addLikeNum(HttpServletRequest request, HttpServletResponse response){
		JSONObject obj = new JSONObject();
		try {
			Session session = Jurisdiction.getSession();
			UserInfo  user = (UserInfo) session.getAttribute(Const.SESSION_WECHATUSER);
			System.out.println(user+"");
			String ARTICLEID = request.getParameter("ARTICLEID");
			System.out.println("ARTICLEID:"+ARTICLEID);
			logger.info("=========="+ARTICLEID);
			articleService.addLikeNumAndMoney(user,ARTICLEID);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			obj.put("code", 0);
		}
		obj.put("code", 1);
		return obj.toString();
	}
	/**
	 * 打赏页面跳转
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/reward")
	public ModelAndView reward(HttpServletRequest request) throws Exception{
		ModelAndView mv = this.getModelAndView();
		String ARTICLEID = request.getParameter("ARTICLEID");
		PageData pd = new PageData();
		pd = getPageData();
		String appId=GongZhongService.appId;
		String timestamp=Sha1Util.getTimeStamp();
		String nonceStr=Sha1Util.getNonceStr();
		String signature=Sha1Util.createSignature(timestamp, nonceStr, getRequertNowUrl());
		pd.put("wxsignature", signature);
		pd.put("appId", appId);
		pd.put("wxtimestamp", timestamp);
		pd.put("wxnonce_str", nonceStr);
		pd.put("ARTICLEID", ARTICLEID);
		
		//打赏金额列表
		List<PageData> statesList = null;
		PageData statesPd = new PageData();
		statesPd.put("stateType", "10");
		statesList = statesService.listAll(statesPd);
		mv.addObject("statesList", statesList);
		mv.addObject("msg", "成功");
		mv.addObject("pd", pd);
		mv.setViewName("front/news/reward");
		return mv;
	}
	
	 /**批量删除
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/createPay")
		@ResponseBody
		public Object createPay() throws Exception{
			PageData pd = new PageData();		
			Map<String,Object> map = new HashMap<String,Object>();
			pd = this.getPageData();
			String trodeNumber = TenpayUtil.getCurrTime()+TenpayUtil.buildRandom(6);
			String packageUrl = Sha1Util.weChatPay("大东江", getWeChatOpenId(), "10", trodeNumber,
					"1", "324dsfsdf", PublicUtil.getIp(),getRequertUrl());
			JSONObject j = JSONObject.fromObject(packageUrl);
			if (j != null) {
				map.put("timeStamp", j.get("timeStamp").toString());
				map.put("nonceStr", j.get("nonceStr").toString());
				map.put("packages", j.get("package").toString());
				map.put("signType", j.get("signType").toString());
				map.put("paySign", j.get("paySign").toString());
			}
			return AppUtil.returnObject(pd, map);
		}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="rewardPay")
	public ModelAndView rewardPay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		ModelAndView mv = this.getModelAndView();	
		String resjson = getPostWx();
		JSONObject obj = JSONObject.fromObject(resjson);
		if (obj != null) {
			request.setAttribute("timeStamp", obj.get("timeStamp").toString());
			request.setAttribute("nonceStr", obj.get("nonceStr").toString());
			request.setAttribute("packages", obj.get("package").toString());
			request.setAttribute("signType", obj.get("signType").toString());
			request.setAttribute("paySign", obj.get("paySign").toString());
		}
		
		//所有微信页面需要的参数
		String timestamp = Sha1Util.getTimeStamp();
		String nonce_str = Sha1Util.getNonceStr();
		String pageUrl = getRequertNowUrl();
		String signature = createSignature(timestamp,nonce_str,pageUrl);
		mv.addObject("wxtimestamp", timestamp);
		mv.addObject("wxnonce_str", nonce_str);
		mv.addObject("wxsignature", signature);
		
		request.setAttribute("appId", GongZhongService.appId);
		mv.setViewName("front/news/wxPay");
		return mv;
	}
	
	
	
	private final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//封装微信支付接口数据
	public String getPostWx() throws Exception{
		
		StringBuilder sbuf = new StringBuilder();
		// 1 参数
		String openId = "oXeXowU94y21bfSd5mOtqbPr8hzY";
		// 附加数据 原样返回
		String attach = "大东江酒店";
		// 总金额以分为单位，不带小数点
		String totalFee = "10";
		// 订单生成的机器 IP
		InetAddress addr = InetAddress.getLocalHost();
		String ip=addr.getHostAddress();//获得本机IP
		String spbill_create_ip = ip;
		// ---必须参数
		// 商品描述根据情况修改
		String body = "腾讯充值中心-QQ会员充值";
		// 商户订单号
		String out_trade_no = TenpayUtil.getCurrTime()+TenpayUtil.buildRandom(6);
		SortedMap<String, String> params = new TreeMap<String, String>();
		// 公众账号ID
		params.put("appid", GongZhongService.appId);// 微信分配的公众账号ID（企业号corpid即为此appId）
		// 商户号
		params.put("mch_id", GongZhongService.mch_id);// 微信支付分配的商户号
		// 随机字符串
		String nonce_str = Sha1Util.getNonceStr();
		params.put("nonce_str", nonce_str);// 随机字符串，不长于32位。推荐随机数生成算法:https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=4_3
		// 商品描述
		
		params.put("body", body);
		// 附加数据
		params.put("attach", attach);// 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
		// 商户订单号
		params.put("out_trade_no", out_trade_no);// 商户系统内部的订单号,32个字符内、可包含字母,
													// 其他说明见https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=4_2
		// 总金额 Int类型，已做单位处理
		params.put("total_fee", totalFee);// 订单总金额，单位为分，详见支付金额
		// 终端IP
		params.put("spbill_create_ip", spbill_create_ip);// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
		// 通知地址
		String notify_url = "http://ddj.1yg.tv/wxNotify.html";// NOTIFYURL
		params.put("notify_url", notify_url);
		String trade_type = "JSAPI";
		params.put("trade_type", trade_type);
		params.put("openid", openId);
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(GongZhongService.appId, GongZhongService.appSecret, GongZhongService.partner_key);
		logger.info("appid：" + GongZhongService.appId + ",appsecret：" + GongZhongService.appSecret
				+ ",partnerkey：" + GongZhongService.partner_key + "");
		String sign = reqHandler.createSign(params);
		String xml = "<xml>" + "<appid>" + GongZhongService.appId + "</appid>" + "<mch_id>"
				+ GongZhongService.mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" + "<out_trade_no>"
				+ out_trade_no + "</out_trade_no>" + "<attach>" + attach
				+ "</attach>" + "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openId + "</openid>"
				+ "</xml>";
		logger.info("获取到的预支付IDxml数据：" + xml);

		String resultXML =httpsRequest(url, "POST", xml);
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(resultXML);
		} catch (DocumentException e) {
			logger.info("获取resultXML异常");
			e.printStackTrace();
		}
		logger.info("获取到的预支付resultXML:" + resultXML);
		final Element root = doc.getRootElement();
		String prepay_id = root.element("prepay_id").getText();
		if (Tools.isEmpty(prepay_id)) {
			logger.info("获取到的预支付ID为空");
		}
		// 获取prepay_id后，拼接最后请求支付所需要的package
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id=" + prepay_id;
		finalpackage.put("appId", GongZhongService.appId);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonce_str);
		finalpackage.put("package", packages);
		finalpackage.put("signType", "MD5");
		// 要签名
		String finalsign = reqHandler.createSign(finalpackage);
		String finaPackage = "\"appId\":\"" + GongZhongService.appId + "\",\"timeStamp\":\""
				+ timestamp + "\",\"nonceStr\":\"" + nonce_str
				+ "\",\"package\":\"" + packages + "\",\"signType\" : \"MD5"
				+ "\",\"paySign\":\"" + finalsign + "\"";
		JSONObject j = new JSONObject();
		j.put("appId", GongZhongService.appId);
		j.put("timeStamp", timestamp);
		j.put("nonceStr", nonce_str);
		j.put("package", packages);
		j.put("signType", "MD5");
		j.put("paySign", finalsign);
		logger.info("finaPackage :" + finaPackage);
		return j.toString();
	}
	
	public static String httpsRequest(String requestUrl, String requestMethod,
			String output) {
		try {
			System.out.println("进入了请求微信的post方法");
			URL url = new URL(requestUrl);
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod(requestMethod);
			if (null != output) {
				OutputStream outputStream = connection.getOutputStream();
				outputStream.write(output.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			connection.disconnect();
			System.out.println(buffer+"buffer");
			return buffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("post异常");
		}

		return "";
	}
	
	/**
	 * 获取签名 用于页面wx.config注入
	 * @param timestamp Sha1Util.getTimeStamp()
	 * @param nonce_str Sha1Util.getNonceStr()
	 * @param url 当前网页的URL不包含#及其后面部分(参数需要带上,必须是完整的URL)
	 * @return "" 表示获取签名出现异常
	 * @throws Exception 
	 */
	public String createSignature(String timestamp,String nonce_str,String url) throws Exception{
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		String jsapi_ticket=GongZhongService.getTicket();
		if(Tools.isEmpty(jsapi_ticket)){
			logger.info("获取jsapi_ticket异常");
			return "";
		}
		if(Tools.isEmpty(nonce_str)){
			logger.info("nonce_str参数不能为空");
			return "";
		}
		signParams.put("nonce_str", nonce_str);
		signParams.put("jsapi_ticket",jsapi_ticket);
		if(Tools.isEmpty(timestamp)){
			logger.info("timestamp参数不能为空");
			return "";
		}
		signParams.put("timestamp", timestamp);
		if(Tools.isEmpty(url)){
			logger.info("url参数不能为空");
			return "";
		}
		signParams.put("url", url);
		String signature="";
		try {
			signature =Sha1Util.getSignature(signParams);
		} catch (IOException e) {
			logger.info("wx.config签名生成异常："+e.getMessage());
			e.printStackTrace();
		}
		if(signature.equals("false")){
			return "";
		}
		return signature;
	}
	/**
	 * 读者添加预定记录
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/readerReservation")
	@ResponseBody
	public Object readerReservation() throws Exception{
		PageData yd = this.getPageData();
		//查询软文信息;
		PageData articlePd=new PageData();
		articlePd.put("ARTICLEID", yd.getString("ARTICLEID"));
		articlePd=articleService.findById(articlePd);
		
		//查询产品信息;
		PageData productPd = new PageData();
//		productPd.put("PRODUCTID", yd.getString("PRODUCTID"));
//		productPd = productService.findById(productPd);
		productPd.put("PRODUCTID", yd.getString("PRODUCTID"));
		productPd=productService.findByViweId(productPd);
		
		System.out.println(yd);
		int PROVALIDITY =0;//套餐设定预订有效天数
		Date VALIDITYDATE = new Date();//预订多久有效
		if(Tools.notEmpty(productPd.get("proValidity")+"")){
			  PROVALIDITY =Convert.strToInt(productPd.get("proValidity")+"", PROVALIDITY);
			 Calendar   calendar   =   new   GregorianCalendar(); 
		     calendar.setTime(VALIDITYDATE); 
		     calendar.add(calendar.DATE,PROVALIDITY);//把日期往后增加VALIDITY天.整数往后推,负数往前移动 
		     VALIDITYDATE=calendar.getTime();   //这个时间就是日期往后推VALIDITY天的结果 
		     logger.info("VALIDITYDATE:"+VALIDITYDATE);
		}
		long d =DateUtil.getDaySub(yd.getString("date")+" "+yd.getString("time")+":00", Convert.dateToStr(VALIDITYDATE, "1900-01-01 00:00:00"));
		Map<String,Object> map = new HashMap<String,Object>();
		if(d<0){
			map.put("type", -1);
			map.put("msg", "预订有效时间需在当天以内！");
			return AppUtil.returnObject(articlePd, map);
		}
		if(d>0 && PROVALIDITY<d){
			map.put("type", -1);
			if(PROVALIDITY==0){
				map.put("msg", "预订有效时间需在当天以内！");
			}else{
				map.put("msg", "预订有效时间需在"+PROVALIDITY+"已天内！");
			}
			return AppUtil.returnObject(articlePd, map);
		}
		
		
		yd.put("RESERVATIONID", get32UUID());
		yd.put("RESERVATIONNUMBER", TenpayUtil.getCurrTime()+TenpayUtil.buildRandom(4));
		yd.put("MEMBERID", getWeChatOpenId());
		yd.put("RESERVATIONTYPE", 4);//预定方式3预付定金,4非预付定金
		Session session = Jurisdiction.getSession();
		UserInfo  user = (UserInfo) session.getAttribute(Const.SESSION_WECHATUSER);
		yd.put("RESERVATIONSEX", user.getSex());
		yd.put("PROCLASSNAME", productPd.getString("typeName"));
		yd.put("PRONAME", productPd.getString("proName"));
		yd.put("PROMONEY", productPd.getDouble("proMoney"));
		yd.put("ADVANCEMONEY", productPd.getDouble("proAdvanceMoney"));
		yd.put("PROIMG", productPd.getString("proImg"));
		yd.put("ARTICLEID", articlePd.getInt("ID"));
		
		yd.put("CREATEDATE", DateUtil.fomatDate1(yd.getString("date")+" "+yd.getString("time")+":00"));
		yd.put("RESERVATIONSTATEID", 12);//预定中
		yd.put("PAYSTATE", 0);//付款状态 0 代付款
//		yd.put("PAYDATE", new Date());
		double LastAUTHORREVENUE = 0.0;//作者收益
		double LastSHAREREVENUE = 0.0;//分享者收益
		
		double memRevenueConM = 0.0;
		double memRevenueCon = 0.0;
		
		double artRevenueConM = articlePd.getDouble("REVENUECONM");//软文给分享者带来的收益
		double artRevenueCon = articlePd.getDouble("REVENUECON");//软文给作者带来的收益
		//查询会员的消费成功比例和作者的收益比例
		PageData mempd = new PageData();
		if(!Tools.isEmpty(yd.getString("SHAREID"))){
			mempd.put("OPENID", yd.getString("SHAREID"));
			mempd = memberService.findByOpenId(mempd);
			if(mempd!=null&&mempd.getInt("MEMBERTYPE")==2){
				//只有用户存在并且用户是普通用户才有用户的收益比例提成 
				memRevenueConM = mempd.getDouble("REVENUECONM");//用户分享收益比例
			}
		}
		//给作者带来的收益
		if(!Tools.isEmpty(articlePd.getString("AUTHORID"))){
			mempd.put("memberId", articlePd.getString("AUTHORID"));
			mempd = memberService.findById(mempd);
			if(mempd!=null&&mempd.getInt("MEMBERTYPE")==1){
				//只有作者存在并且用户是作者才有收益比例提成 
				memRevenueCon = mempd.getDouble("REVENUECON");//作者收益比例
			}
		}
		LastAUTHORREVENUE = (artRevenueCon+memRevenueCon)*productPd.getDouble("proAdvanceMoney")*0.01;
		LastSHAREREVENUE = (artRevenueConM+memRevenueConM)*productPd.getDouble("proAdvanceMoney")*0.01;
		
		yd.put("AUTHORREVENUE", LastAUTHORREVENUE);//软文作者收益 在成功后计算
		yd.put("SHAREREVENUE", LastSHAREREVENUE);//分享者收益  在成功后计算
		
		
		yd.put("STATEDATE", new Date());
		yd.put("STATEREMARKS", user.getNickname()+"预定"+productPd.getString("proName"));
		yd.put("VALIDITYDATE", VALIDITYDATE);//预定订单最后有效期时间;
		
		productPd.put("PRORESERVATIONNUM", productPd.getInt("proReservationNum")+1);
		reservationService.save(yd, productPd);
		if(!getRequertUrl().contains("localhost") && !getRequertUrl().contains("ddj.java.1yg.tv")){
			//消息模板
			List<Template> templates = new ArrayList<Template>();
			templates = TemplateMessage.setTemplate(templates, "#173177", "first",getWeChatName()+",您已成功预订套餐："+productPd.getString("proName")+"，请尽快付款。");
			templates = TemplateMessage.setTemplate(templates, "#173177", "productType","预定产品");
			templates = TemplateMessage.setTemplate(templates, "#173177", "name",productPd.getString("proName"));
			templates = TemplateMessage.setTemplate(templates, "#173177", "number","1");
			templates = TemplateMessage.setTemplate(templates, "#173177", "expDate",Convert.dateToStr(VALIDITYDATE, "yyyy-MM-dd", "1900-01-01"));
			String url =getRequertUrl()+"myReservation/goDetails?id="+yd.getString("RESERVATIONID");
			TemplateMessage.sendTemplateMessage(getWeChatOpenId(), Constants.PRODUCT_RESERVE_TEMPLATE_ID, url, Constants.PRODUCT_RESERVE_TOPCOLOR, templates);
			
		}
		map.put("type", 1);
		map.put("msg", "成功！");
		map.put("data", yd.getString("RESERVATIONID"));
		return AppUtil.returnObject(yd, map);
	}
	
	@RequestMapping(value="/testService1")
	public void testService() throws Exception{
		PageData productPd = new PageData();
		productPd.put("PRODUCTID", "3c743c24f9fe4f0191ff6327678c475f");
		productPd=productService.findByViweId(productPd);
		System.out.println(productPd.getString("proTitle"));
		System.out.println(productPd.getDouble("proMoney"));
		System.out.println( productPd.getDouble("proAdvanceMoney"));
		System.out.println(productPd.get("proValidity"));
		System.out.println(productPd.getDouble("proAdvanceMoney"));
	}
	
	/*
	 * 广告位预定
	 */
	@RequestMapping(value="/goPayment")
	public ModelAndView goPayment() throws Exception{
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
//			TotalFee = Tools.getMoney(pd.get("ADVANCEMONEY")+"");
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
						"3", openid, PublicUtil.getIp(),getRequertUrl());
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
		mv.setViewName("front/news/weChatPay");
		mv.addObject("msg", "goPay");
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping(value="/testService")
	public void TestService() throws Exception{
		PageData pd = new PageData();
		pd.put("RESERVATIONID", "df0a4fe4ae684d1fa3c980500f3d2a82");
		pd = reservationService.findById(pd);
		reservationService.editPayStateAndAddMoney(pd, new PageData());
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
